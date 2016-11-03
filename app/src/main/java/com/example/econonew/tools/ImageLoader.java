package com.example.econonew.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.econonew.tools.io.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * 从网络上加载图片，并将其在本地和硬盘中进行缓存 当传入的ImageView想要加载图片的时候，先从硬盘加载到本地图片 如果本地不存在的话就从网络上加载
 *
 * @author mengfei
 */
public class ImageLoader {

    private static final String TAG = "ImageLoader";
    // 本地缓存的类
    private LruCache<String, Bitmap> mCaches;

    private ListView msgItemList;

    // 记录正在下载或等待下载的任务
    private Set<MsgItemsAsyncTask> taskCollection;

    // 硬盘缓存的类
    private DiskLruCache mDiskLruCache;

    public ImageLoader(Context context, ListView listView) {
        taskCollection = new HashSet<>();
        this.msgItemList = listView;
        // 获取最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        mCaches = new LruCache<String, Bitmap>(cacheSize) {
            @SuppressLint("NewApi")
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
        try {
            // 获取图片缓存地址
            File cacheDir = getDiskCacheDir(context, "bitmap");
            if (!cacheDir.exists()) {
                cacheDir.mkdir();
            }
            mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取应用的版本号
     *
     * @return 应用的版本号
     */
    private int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 根据传入的uniqueName来获取硬盘缓存路径
     *
     * @param context    context
     * @param uniqueName 缓存路径下的文件夹，用于给缓存进行分类
     */
    @SuppressLint("NewApi")
    private File getDiskCacheDir(Context context, String uniqueName) {

        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 根据图片的url地址来显示图片
     *
     * @param imageView imageView
     * @param imageUrl  图片的url地址
     */
    public void showImageFromURL(ImageView imageView, String imageUrl) {
        Log.d(TAG, "showImageFromURL: imageUrl : " + imageUrl);
        if (imageUrl == null) {
            return;
        }
        Bitmap bitmap = getBitmapFromCache(imageUrl);

        if (bitmap == null) {
            MsgItemsAsyncTask task = new MsgItemsAsyncTask();
            taskCollection.add(task);
            task.execute(imageUrl);
        } else {
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    /**
     * 从缓存中获取数据
     *
     * @param key 图片的url
     * @return bitmap
     */
    public Bitmap getBitmapFromCache(String key) {
        return mCaches.get(key);
    }

    // * 将一张图片存储到LruCache中
    public void addBitmapToCaches(String key, Bitmap bitmap) {
        if (getBitmapFromCache(key) == null) {
            mCaches.put(key, bitmap);
        }
    }

    /**
     * 取消所有正在下载的任务
     */
    public void cacelAllTasks() {
        if (taskCollection != null) {
            for (MsgItemsAsyncTask task : taskCollection) {
                task.cancel(false);
            }
        }
    }

    /**
     * 同步缓存
     */
    public void flushCache() {
        if (mDiskLruCache != null) {
            try {
                mDiskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通过AsyncTask异步加载来获取图片资源
     *
     * @author mengfei
     */
    private class MsgItemsAsyncTask extends AsyncTask<String, Void, Bitmap> {

        private String imageUrl;

        @Override
        protected Bitmap doInBackground(String... params) {
            imageUrl = params[0];
            FileDescriptor fileDescriptor = null;
            FileInputStream fileInputStream = null;
            DiskLruCache.Snapshot snapShot;
            try {
                final String key = EncodeStrTool.getInstance().getEncodeMD5Str(imageUrl);
                snapShot = mDiskLruCache.get(key);
                if (snapShot == null) {
                    // 如果没有找到对应缓存，则从网络上下载
                    DiskLruCache.Editor edit = mDiskLruCache.edit(key);
                    if (edit != null) {
                        OutputStream outputStream = edit.newOutputStream(0);
                        if (downloadFromUrl(imageUrl, outputStream)) {
                            edit.commit();
                        } else {
                            edit.abort();
                        }
                        flushCache();
                    }
                    // 缓存被写入后，再次查找key对应的缓存
                    snapShot = mDiskLruCache.get(key);
                }
                if (snapShot != null) {
                    fileInputStream = (FileInputStream) snapShot.getInputStream(0);
                    fileDescriptor = fileInputStream.getFD();
                }
                // 将缓存解析称bitmap对象
                Bitmap bitmap = null;
                if (fileDescriptor != null) {
                    bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                }
                if (bitmap != null) {
                    // 将bitmap对象添加到内存缓存中
                    addBitmapToCaches(key, bitmap);
                }
                return bitmap;
            } catch (IOException e) {
                return null;
            } finally {
                if (fileDescriptor == null && fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        /**
         * 从网络上下载资源到指定的输出流里面
         *
         * @return 下载成功返回true 下载失败返回false
         */
        private boolean downloadFromUrl(String urlString, OutputStream outputStream) {
            HttpURLConnection urlConnection = null;
            BufferedOutputStream out = null;
            BufferedInputStream in = null;

            try {
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream());
                out = new BufferedOutputStream(outputStream, 8 * 1024);
                int b;
                while ((b = in.read()) != -1) {
                    out.write(b);
                }
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                try {
                    if (in != null) {
                        in.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            ImageView imageView = (ImageView) msgItemList.findViewWithTag(imageUrl);
            if (imageView != null && result != null) {
                imageView.setImageBitmap(result);
            }
            taskCollection.remove(this);
        }

    }

}
