package com.example.econonew.view.activity;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.econonew.db.ChannelTable;
import com.example.econonew.db.DBManager;
import com.example.econonew.db.MsgTable;
import com.example.econonew.entity.ChannelEntity;
import com.example.econonew.entity.MsgItemEntity;
import com.example.econonew.entity.UserEntity;
import com.example.econonew.resource.Constant;
import com.example.econonew.resource.msg.ChannelMessage;
import com.example.econonew.resource.msg.MainMessage;
import com.example.econonew.server.NetClient;
import com.example.econonew.server.URLManager;
import com.example.econonew.server.json.ChannelJsonHelper;
import com.example.econonew.server.json.JsonCast;
import com.example.econonew.server.json.ResponseJsonHelper;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class FinanceApplication extends Application {


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
						Log.d(TAG, "onSuccess: " + response);
						JSONObject map = JsonCast.getJsonObject(response);
						new ResponseJsonHelper().handleInfomation(map);
					}

					public void onError(VolleyError error) {
						loadDatasFromDatabase();
					}
				};
				NetClient.getInstance().executeGetForString(app, url, listener);
			}
		}.start();
	}

	//从数据库里面加载数据
	private void loadDatasFromDatabase() {
		for(String tabName : Constant.publicItemNames) {
			MsgTable table = new MsgTable(tabName);
			MainMessage message = MainMessage.getInstance(tabName);
			List<MsgItemEntity> list = new DBManager().getDbItems(table, null, null);
			Log.e(TAG, "loadDatasFromDatabase: " + tabName + " " + list.size());
			if (message != null) {
				message.setMessage(list, false, false);
			}
		}
	}

	public void refreshUserData(UserEntity user) {
		if (user != null && user.isVIP()) {
			getChannelFromNet(user);
		} else {
			ChannelMessage message = ChannelMessage.getInstance("自定义");
			if (message != null) {
				message.setMessage(new ArrayList<ChannelEntity>(), false, false);
			}
		}
	}


	/**
	 * 	从网络上面获取用户的频道信息
	 * @param user 用户
	 */
	public void getChannelFromNet(final UserEntity user) {
		final String url = URLManager.getChannelURL(user.getName());
		final ChannelMessage message = ChannelMessage.getInstance("自定义");
		final NetClient.OnResultListener responseListener = new NetClient.OnResultListener() {

			@Override
			public void onSuccess(String response) {
				ChannelJsonHelper jsonHelper = new ChannelJsonHelper(app);
				List<ChannelEntity> channels = jsonHelper.excuteJsonForItems(response);
				if (channels != null) {
					new DBManager().deleteAllItem(new ChannelTable());
					if (message != null) {
						message.setMessage(channels, false, true);
					}
				}
				FinanceApplication.getInstance().refreshPublicData();
			}

			@Override
			public void onError(VolleyError error) {
				super.onError(error);
				List<ChannelEntity> channels = new DBManager().getDbItems(new ChannelTable(), "user_name=?",new String[]{Constant.user.getName()});
				if (message != null) {
					message.setMessage(channels, false, false);
				}
			}
		};
		new Thread() {
			public void run() {
				NetClient.getInstance().executeGetForString(app, url, responseListener);
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

	public List<BaseActivity> getActManager() {
		return mActManager;
	}

	public void exit() {
		for (BaseActivity act : mActManager) {
			act.finish();
		}
		System.exit(1);
	}

}
