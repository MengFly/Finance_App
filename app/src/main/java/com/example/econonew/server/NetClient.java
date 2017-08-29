package com.example.econonew.server;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.econonew.server.json.JsonCast;
import com.example.econonew.view.activity.BaseActivity;
import com.example.econonew.view.activity.FinanceApplication;

import org.json.JSONObject;

/**
 * 实现网络操作的类
 *
 * @author mengfei
 *
 */
public class NetClient {

	private static NetClient mClient;// 単例模式

	private NetClient() {
	}

	public void executeGetForString(final Context context, final String url, final OnResultListener listener) {
		final Handler handler = new Handler(context.getMainLooper());
		MyStringRequest request = new MyStringRequest(context, Method.GET, url, new Listener<String>() {

			@Override
			public void onResponse(final String response) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						Log.v("json", "url-->" + url +  "\nresponse-->" + response);
						if(context instanceof BaseActivity) {
							((BaseActivity) context).hintProDialog();
						}
						if (listener != null)
							listener.success(response);
					}
				});
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(final VolleyError error) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						if(context instanceof BaseActivity) {
							((BaseActivity) context).hintProDialog();
						}
						if (listener != null)
							listener.onError(error);
					}
				});
			}
		});
		request.setTag(url);
		FinanceApplication.getRequestQueue().add(request);
	}

	/**
	 * 获取VooleyDemo实例
	 */
	public static NetClient getInstance() {
		if (mClient == null) {
			synchronized (NetClient.class) {
				if (mClient == null) {
					mClient = new NetClient();
				}
			}
		}
		return mClient;
	}

	/**
	 * String请求的回调方法
	 *
	 * @author mengfei
	 *
	 */
	public static abstract class OnResultListener {
		public abstract void onSuccess(String response);

		public void onError(VolleyError error){
			if (error instanceof NoConnectionError) {
				Toast.makeText(FinanceApplication.getInstance(), "网络链接失败，请检查网络设置", Toast.LENGTH_SHORT).show();
			} else if (error instanceof TimeoutError) {
				Toast.makeText(FinanceApplication.getInstance(), "链接超时，请稍后重试", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(FinanceApplication.getInstance(), "网络链接错误" + error, Toast.LENGTH_SHORT).show();
			}
		}

		//程序的统一接收到数据的处理逻辑
		private void success(String response) {
			JSONObject jsonObject = JsonCast.getJsonObject(response);
			String status = JsonCast.getString(jsonObject, "status");
			Log.e("result", "success: ---------------------------------->" + status);
			Log.e("result", "success: ---------------------------------->" + response);
			if("success".equals(status)) {
				onSuccess(response);
			} else {
				String result = JsonCast.getString(jsonObject, "result");
				if(result != null) {
					Toast.makeText(FinanceApplication.getInstance(), result, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(FinanceApplication.getInstance(), "请求失败,请稍后在进行请求", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

}
