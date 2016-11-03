package com.example.econonew.tools;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileTools {
	
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
