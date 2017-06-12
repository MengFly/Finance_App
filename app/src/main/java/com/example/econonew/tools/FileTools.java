package com.example.econonew.tools;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileTools {

	static public void downloadFileFromNet(final String urlName, final CallBack<String> back, final CallBack<Integer> lengthCallBack) {
		new Thread() {
			@Override
			public void run() {
				HttpURLConnection connection = null;
				FileOutputStream outputStream = null;
				InputStream stream = null;
				try {
					File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
							urlName.split("/")[urlName.split("/").length - 1]);
					if (file.exists()) {
						back.callBack(file.getAbsolutePath(), "文件已存在");
						return;
					} else {
						file.createNewFile();
					}
					URL url = new URL(urlName);
					connection = (HttpURLConnection) url.openConnection();
					stream = connection.getInputStream();
					int fileLength = connection.getContentLength();
					int downLoadLength = 0;
					outputStream = new FileOutputStream(file);
					byte[] bs = new byte[5 * 1024];
					int length = 0;
					while ((length = stream.read(bs)) != -1) {
						outputStream.write(bs, 0, length);
						downLoadLength += length/1024;
						lengthCallBack.callBack(fileLength/1024, downLoadLength);
					}
					back.callBack(file.getAbsolutePath());
				} catch (Exception e) {
					back.callBack(null, "加载失败" + e.getMessage());
				} finally {
					if (stream!= null) {
						try {
							stream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (outputStream != null) {

						try {
							outputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (connection != null) {
						connection.disconnect();
					}
				}
			}

		}.start();
	}
	
	static public void writeFile(Context context, String fileName,
			String content) {
		File file = new File(context.getExternalCacheDir(), fileName);
		if (file.exists()) {
			file.delete();
		} else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileOutputStream out = null;

		try {
			out = new FileOutputStream(file);
			out.write(content.getBytes(), 0, content.getBytes().length);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Toast.makeText(context, "true", Toast.LENGTH_SHORT).show();
	}
}
