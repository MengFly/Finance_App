package com.example.econonew.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MsgListUtils {

	/**
	 * 获取测试的列表项
	 */
	public static String getUtliMsgList(Context context) {
		AssetManager manager = context.getAssets();
		InputStream inputStream = null;
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		try {
			inputStream = manager.open("json/msglist.json");
			reader = new BufferedReader(new InputStreamReader(inputStream));
			String lineString = null;
			while ((lineString = reader.readLine()) != null) {
				sb.append(lineString);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
