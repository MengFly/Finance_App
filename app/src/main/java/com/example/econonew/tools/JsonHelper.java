package com.example.econonew.tools;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * JsonHelper 实现TTS功能的json结果解析帮助类
 *
 * @author agnes 在Voice中调用
 */
public class JsonHelper {
	public static String parseIatResult(String json) {
		StringBuffer ret = new StringBuffer();
		try {
			JSONTokener tokener = new JSONTokener(json);
			JSONObject joResult = new JSONObject(tokener);

			JSONArray words = joResult.getJSONArray("ws");
			for (int i = 0; i < words.length(); i++) {
				// 转写结果词，默认使用第一个结果
				JSONArray items = words.getJSONObject(i).getJSONArray("cw");
				JSONObject obj = items.getJSONObject(0);
				ret.append(obj.getString("w"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret.toString();
	}
}
