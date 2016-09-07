package com.example.econonew.server;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class JsonCast {

	private static final String LOG_TAG = "JsonCast";

	public static JSONObject getJsonObject(String jsonStr) {
		try {
			return new JSONObject(jsonStr);
		} catch (JSONException e) {
			Log.e(LOG_TAG, "JSONException in getJsonObject", e);
			return null;

		}
	}

	public static JSONArray getJsonArray(JSONObject obj, String where) {
		try {
			return obj.getJSONArray(where);
		} catch (JSONException e) {
			Log.e(LOG_TAG, "JSONException in getJsonArray", e);
			return null;
		}
	}

	public static JSONObject getJsonObject(JSONArray josnArray, int i) {
		try {
			return josnArray.getJSONObject(i);
		} catch (JSONException e) {
			Log.e(LOG_TAG, "JSONException in getJsonObject", e);
			return null;
		}
	}
	
	public static JSONArray getJsonArray(String jsonStr) {
		try {
			return new JSONArray(jsonStr);
		} catch (JSONException e) {
			Log.e(LOG_TAG, "JSONException in getJsonArray", e);
			return null;
		}
	}

	public static String getString(JSONObject obj, String where) {
		try {
			return obj.getString(where);
		} catch (JSONException e) {
			Log.e(LOG_TAG, "JSONException in getString", e);
			return null;
		}
	}
	
	public static JSONObject getJSONObject(JSONObject obj, String string) {
		try {
			return obj.getJSONObject(string);
		} catch (JSONException e) {
			Log.e(LOG_TAG, "JSONException in getJSONObject", e);
			return null;
		}
	}
	
	public static int getInt(JSONObject obj, String string) {
		try {
			return obj.getInt(string);
		} catch (JSONException e) {
			Log.e(LOG_TAG, "JSONException in getInt", e);
			return 0;
		}
	}

}
