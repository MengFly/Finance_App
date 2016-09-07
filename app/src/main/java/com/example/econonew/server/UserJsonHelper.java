package com.example.econonew.server;

import android.content.Context;
import android.util.Log;

import com.example.econonew.resource.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 这个类用来解析Json字符串，解析为User对象
 *
 * @author mengfei
 *
 */
public class UserJsonHelper extends JsonHelperImpl<UserInfo> {

	public UserJsonHelper(Context context) {
		super(context);
	}

	@Override
	public UserInfo getItemFromJson(String jsonString) {
		Log.v("testLog", "get User");
		UserInfo user = null;
		Log.v("testLog", jsonString);
		try {
			JSONObject object = new JSONObject(jsonString);
			user = new UserInfo("", "");
			JSONObject result = object.getJSONObject("result");
			int isVip = result.getInt("isVIP");
			Log.v("testLog", isVip + "isVip");
			user.setVIP(isVip);
		} catch (JSONException e) {
			e.printStackTrace();
			Log.v("testLog", "get User");
		}
		return user;
	}

	@Override
	public List<UserInfo> getItemAllFromJson(String jsonString) {
		return null;
	}

}
