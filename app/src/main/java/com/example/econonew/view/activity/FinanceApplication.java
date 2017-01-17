package com.example.econonew.view.activity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.econonew.db.DBHelperFactory;
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
import org.litepal.LitePalApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 用用的Application,用于提供一些全局的方法，在应用内进行调用
 */
public class FinanceApplication extends LitePalApplication {


	private static final String TAG = "FinanceApplication";

	public static FinanceApplication app = null;
	private static RequestQueue mRequestQueue;//网络请求队列
	private List<BaseActivity> mActManager;//Activity队列

	@Override
	public void onCreate() {
		super.onCreate();
		app = this;
		mRequestQueue = Volley.newRequestQueue(app);
		mActManager = new ArrayList<>();
		SpeechUtility.createUtility(this, SpeechConstant.APPID + "=552a964f");
	}

	//刷新公共消息
	public void refreshPublicData() {
		refreshPublicDatasConnection();

//		refreshPublicDatasSave();
	}

	public void refreshPublicDatasConnection() {
		new Thread() {
			public void run() {
				String url = URLManager.getConnectURL();
				NetClient.OnResultListener listener = new NetClient.OnResultListener() {

					@Override
					public void onSuccess(String response) {
						JSONObject map = JsonCast.getJsonObject(response);
						new ResponseJsonHelper().handleInformation(map,false);
						refreshPublicDatasCurrent();
						refreshPublicDatasCache();
						refreshPublicDatasSave();
					}

					public void onError(VolleyError error) {
						loadDatasFromDatabase();
					}
				};
				NetClient.getInstance().executeGetForString(app, url, listener);
			}
		}.start();
	}

	private void refreshPublicDatasCurrent() {
		new Thread() {
			public void run() {
				String currentUrl = URLManager.getBACKCurrentMsgUrl();
				NetClient.OnResultListener listener = new NetClient.OnResultListener() {

					@Override
					public void onSuccess(String response) {
						Log.e("json", "onSuccess:  Current" + response);
						JSONObject map = JsonCast.getJsonObject(response);
						new ResponseJsonHelper().saveBackMsg(map, Constant.MESSAGE_CURRENT_TYPES);
					}

					public void onError(VolleyError error) {
						loadDatasFromDatabase();
					}
				};
				NetClient.getInstance().executeGetForString(app, currentUrl, listener);
			}
		}.start();
	}


	private void refreshPublicDatasCache() {
		new Thread() {
			public void run() {
				String cacheUrl = URLManager.getBACKCacheMsgUrl();
				NetClient.OnResultListener listener = new NetClient.OnResultListener() {

					@Override
					public void onSuccess(String response) {
						Log.e("json", "onSuccess:  Cache" + response);
						JSONObject map = JsonCast.getJsonObject(response);
						new ResponseJsonHelper().saveBackMsg(map, Constant.MESSAGE_CACHE_TYPES);
					}

					public void onError(VolleyError error) {
						loadDatasFromDatabase();
					}
				};
				NetClient.getInstance().executeGetForString(app, cacheUrl, listener);
			}
		}.start();
	}

	private void refreshPublicDatasSave() {
		new Thread() {
			public void run() {
				String saveUrl = URLManager.getBACKSaveMsgUrl();
				NetClient.OnResultListener listener = new NetClient.OnResultListener() {

					@Override
					public void onSuccess(String response) {
						Log.e("json", "onSuccess:  Save" + response);
						JSONObject map = JsonCast.getJsonObject(response);
						new ResponseJsonHelper().saveBackMsg(map, Constant.MESSAGE_SAVE_TYPES);
					}

					public void onError(VolleyError error) {
						loadDatasFromDatabase();
					}
				};
				NetClient.getInstance().executeGetForString(app, saveUrl, listener);
			}
		}.start();
	}


	//从数据库里面加载数据
	private void loadDatasFromDatabase() {
		for(String tabName : Constant.publicItemNames) {
			MainMessage message = MainMessage.getInstance(tabName);
			List<MsgItemEntity> list = DBHelperFactory.getDBHelper().queryItemsByArgs(MsgItemEntity.class, "msgType=?" , tabName);
			Log.e(TAG, "loadDatasFromDatabase: " + tabName + " " + list.size());
			if (message != null) {
				message.setMessage(list, false, false);
			}
		}
	}

	//刷新用户信息
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
					DBHelperFactory.getDBHelper().deleteAll(ChannelEntity.class);
					if (message != null) {
						message.setMessage(channels, false, true);
					}
				}
				FinanceApplication.getInstance().refreshPublicData();
			}

			@Override
			public void onError(VolleyError error) {
				super.onError(error);
				List<ChannelEntity> channels = DBHelperFactory.getDBHelper().queryItemsByArgs(ChannelEntity.class, "username=?" , Constant.user.getName());
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

	//获取到应用的安装包
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

	//退出应用
	public void exit() {
		for (BaseActivity act : mActManager) {
			act.finish();
		}
		System.exit(1);
	}

}
