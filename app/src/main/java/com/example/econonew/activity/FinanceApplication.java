package com.example.econonew.activity;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.econonew.entity.ChannelEntity;
import com.example.econonew.main.object.AllMessage;
import com.example.econonew.resource.Constant;
import com.example.econonew.resource.DB_Information;
import com.example.econonew.resource.UserInfo;
import com.example.econonew.server.ChannelJsonHelper;
import com.example.econonew.server.JsonCast;
import com.example.econonew.server.NetClient;
import com.example.econonew.server.ResponseJsonHelper;
import com.example.econonew.tools.SettingManager;
import com.example.econonew.tools.URLManager;
import com.example.econonew.utils.MsgListUtils;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FinanceApplication extends Application {

	public static final int HANDLER_STATE_OK = 0x09786;

	private static final String TAG = "FinanceApplication";

	public static FinanceApplication app = null;

	private static RequestQueue mRequestQueue;

	private List<BaseActivity> mActManager;

	@Override
	public void onCreate() {
		super.onCreate();
		app = this;
		mRequestQueue = Volley.newRequestQueue(app);
		mActManager = new ArrayList<>();
		SpeechUtility.createUtility(this, SpeechConstant.APPID + "=552a964f");
	}

	public void refreshPublicData() {
		new Thread() {
			public void run() {
				String url = URLManager.getConnectURL();
				NetClient.OnResultListener listener = new NetClient.OnResultListener() {

					@Override
					public void onSuccess(String response) {
						JSONObject map = JsonCast.getJsonObject(response);
						new ResponseJsonHelper().handleInfomation(map);
					}

					public void onError(VolleyError error) {
						super.onError(error);
						JSONObject map = JsonCast.getJsonObject(MsgListUtils.getUtliMsgList(app));
						new ResponseJsonHelper().handleInfomation(map);
					};
				};
				NetClient.getInstance().excuteGetForString(app, url, listener);
			};
		}.start();
	}

	public void refreshUserData(UserInfo user) {
		List<ChannelEntity> channelList = null;
		if (user != null && user.isVIP()) {
			channelList = new DB_Information(app).getChannel(user);
			if (!todayIsUppdate(user.getName())) {
				getChannelFromNet(user);
			} else {
				AllMessage.getInstance("自定义").setChannels(channelList, false, false);
			}
		} else {
			if (SettingManager.getInstance().isInitDataFinsh())
				AllMessage.getInstance("自定义").setChannels(new ArrayList<ChannelEntity>(), false, false);
		}
	}

	private boolean todayIsUppdate(String userName) {
		SharedPreferences spf = getSharedPreferences(Constant.SPF_KEY_UPDATE_DATE, MODE_PRIVATE);
		String updateDate = spf.getString(userName, null);
		String nowDate = SimpleDateFormat.getDateInstance().format(new Date());
		return nowDate.equals(updateDate);
	}

	private void saveUpdateDate(String userName) {
		SharedPreferences spf = getSharedPreferences(Constant.SPF_KEY_UPDATE_DATE, MODE_PRIVATE);
		SharedPreferences.Editor edit = spf.edit();
		edit.putString(userName, SimpleDateFormat.getDateInstance().format(new Date()));
		edit.apply();
	}

	/**
	 * 	从网络上面获取用户的频道信息
	 * @param user 用户
	 */
	public void getChannelFromNet(final UserInfo user) {
		final String url = URLManager.getChannelURL(user.getName());
		final NetClient.OnResultListener responseListener = new NetClient.OnResultListener() {

			@Override
			public void onSuccess(String response) {
				Log.d(TAG, "onSuccess: " + response);
				saveUpdateDate(user.getName());
				ChannelJsonHelper jsonHelper = new ChannelJsonHelper(app);
				List<ChannelEntity> channels = jsonHelper.excuteJsonForItems(response);
				if (channels == null) {
					Toast.makeText(app, jsonHelper.getErrorTip(), Toast.LENGTH_SHORT).show();
				} else {
					new DB_Information(app).removeSelfChannel(user.getName());
					AllMessage.getInstance("自定义").setChannels(channels, false, true);
				}
			}

		};
		new Thread() {
			public void run() {
				NetClient.getInstance().excuteGetForString(app, url, responseListener);
			}
		}.start();
	}

	public static FinanceApplication getInstance() {
		return app;
	}

	public File getApplicationFile() {
		File file = null;
		PackageManager manager = this.getPackageManager();
		try {
			ApplicationInfo info = manager.getApplicationInfo(getPackageName(), 0);
			file = new File(info.sourceDir);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return file;
	}

	public static RequestQueue getRequestQueue() {
		return mRequestQueue;
	}

	public List<BaseActivity> getmActManager() {
		return mActManager;
	}

	public void exit() {
		for (BaseActivity act : mActManager) {
			act.finish();
		}
	}

}
