package com.example.econonew.server.json;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public abstract class JsonHelperImpl<T> implements JsonHelper<T> {
	protected Context context;
	private String errorTip;
	private Handler handler;

	public JsonHelperImpl(Context context) {
		this.context = context;
		handler = new Handler(context.getMainLooper());
	}

	/**
	 * 从Json字符串中获取想要的Item 要想获取到相应的Item需要重写getItemFromJson方法
	 *
	 * @param jsonString
	 * @return
	 */
	public T excuteJsonForItem(String jsonString) {
		if (stateIsSuccess(jsonString)) {
			return getItemFromJson(jsonString);
		} else {
			jsonErrorOrJsonFail();
			return null;
		}
	}

	/**
	 * 设置出错时提示的信息<br>
	 * 此时设置的errorTip只有在<b>没有 联网或从服务器获取信息失败</b>的时候才会显示<br>
	 * 否则会被从服务器里面的result信息所覆盖<br>
	 * 所以最好的方式是先设置好自己要显示的errTip再进行获取ErrorTip
	 *
	 * @param errorTip
	 *            出错时提示的信息
	 *
	 */
	public void setErrorTip(String errorTip) {
		this.errorTip = errorTip;
	}

	/**
	 * 获取出错的信息
	 *
	 * @return 出错的信息
	 */
	public String getErrorTip() {
		return errorTip;
	}

	/**
	 * 从Json字符串中获取想要的Items
	 *
	 * @param jsonString
	 *            Json字符串
	 * @return
	 */
	public List<T> excuteJsonForItems(String jsonString) {
		Log.v("json", jsonString);
		if (stateIsSuccess(jsonString)) {
			return getItemAllFromJson(jsonString);
		} else {
			jsonErrorOrJsonFail();
			return null;
		}
	}

	@Override
	public boolean stateIsSuccess(String jsonString) {
		if (jsonString == null)
			return false;
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			if (jsonObject.getString("result") != null) {
				errorTip = jsonObject.getString("result");
			}
			return "success".equals(jsonObject.get("status"));
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void jsonErrorOrJsonFail() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(context, errorTip == null ? "连接失败" : errorTip,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

}
