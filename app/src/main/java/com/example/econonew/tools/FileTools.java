package com.example.econonew.tools;

import android.os.Environment;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileTools {
    private static final String DOWNLOAD_FILE_DIR = "finance";

    public static File getDownLoadFileDir() {
        return mkDir(DOWNLOAD_FILE_DIR);
    }

    public static File mkDir(String dirType) {
        File file  = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), dirType);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdir();
        }
        return file;
    }

    static public void downloadFileFromNet(final String urlName, final CallBack<String> back,
                                           final CallBack<Integer> lengthCallBack) {
        new Thread() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                FileOutputStream outputStream = null;
                InputStream stream = null;
                try {
                    URL url = new URL(urlName);
                    connection = (HttpURLConnection) url.openConnection();
                    File file = new File(getDownLoadFileDir(), urlName.split("/")[urlName.split("/").length - 1]);
                    if (file.exists()) {
                        if (Math.abs(file.length() - connection.getContentLength()) < 5 * 1024) {
                            back.callBack(file.getAbsolutePath(), "文件已存在");
                            connection.disconnect();
                            return;
                        } else {
                            file.deleteOnExit();
                        }
                    }
                    if (file.createNewFile()) {
                        back.callBack(null, "文件创建成功");
                    } else {
                        back.callBack(null, "文件创建失败");
                    }
                    stream = connection.getInputStream();
                    int fileLength = connection.getContentLength();
                    outputStream = new FileOutputStream(file);
                    readContentFromStream(stream, outputStream, fileLength, lengthCallBack);
                    back.callBack(file.getAbsolutePath());
                } catch (Exception e) {
                    back.callBack(null, "加载失败" + e.getMessage());
                } finally {
                    closeStream(stream, outputStream);
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }

        }.start();
    }

    private static void readContentFromStream(InputStream inputStream, OutputStream outputStream,
                                              int length, CallBack<Integer> lengthCallBack) throws IOException {
        int downLoadLength = 0;
        byte[] bs = new byte[5 * 1024];
        int buffer;
        while ((buffer = inputStream.read(bs)) != -1) {
            outputStream.write(bs, 0, buffer);
            downLoadLength += buffer / 1024;
            if (length != 0 && lengthCallBack != null) {
                lengthCallBack.callBack(downLoadLength, length / 1024);
            }
        }
    }

    private static void closeStream(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            try {
                if (closeable != null) {
                    closeable.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
