package com.example.econonew.server;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MyStringRequest extends StringRequest {

	private final SharedPreferences spf;

	private static String SESSIONID = null;// 本地存储的SESSIONID

	public MyStringRequest(Context context, int method, String url,
						   Listener<String> listener, ErrorListener errorListener) {
		super(method, url, listener, errorListener);
		spf = context.getSharedPreferences("set-cookie", Context.MODE_PRIVATE);
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> header = new HashMap<String, String>(super.getHeaders());
		if (SESSIONID == null) {
			SESSIONID = getLocalSessionID();
		}
		header.put("Cookie", SESSIONID);
		return header;
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		SESSIONID = getseSessionId(response);
		return super.parseNetworkResponse(response);
	}

	/** 获取SessionId，通过这个来保持用户登录状态 */
	private String getseSessionId(NetworkResponse response) {
		if (response != null) {
			Map<String, String> header = response.headers;
			System.out.println(header);
			String cookieTotal = header.get("Set-Cookie");
			if(cookieTotal == null) return null;
			String sessionId = cookieTotal.split(";")[0];// 在Set-Cookie里面的第一个属性就是SESSIONID
			Log.v("sessionid", sessionId);
			saveSessionID(SESSIONID, sessionId);
			return sessionId;
		} else {
			return null;
		}
	}

	/**
	 * 保存SESSIONID
	 *
	 * @param currentSessionID
	 *            当前的SESSIONID
	 * @param sessionID
	 *            要保存的SESSIONID
	 */
	private void saveSessionID(String currentSessionID, String sessionID) {
		if (!sessionID.equals(currentSessionID)) {
			SharedPreferences.Editor edit = spf.edit();
			edit.putString("SESSIONID", sessionID);
			edit.apply();
		}
	}

	/**
	 * 从本地获取SESSIONID
	 *
	 * @return 本地SharedPreferences存储的SESSIONID
	 */
	private String getLocalSessionID() {
		return spf.getString("SESSIONID", null);
	}

}
