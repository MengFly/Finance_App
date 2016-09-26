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
import com.example.econonew.server.json.JsonHelperImpl;
import com.example.econonew.view.activity.FinanceApplication;

import java.util.List;

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

	public void excuteGetForString(Context context, final String url, final OnResultListener listener) {
		Log.v("NetClient", "url-->" + url);
		final Handler handler = new Handler(context.getMainLooper());
		MyStringRequest request = new MyStringRequest(context, Method.GET, url, new Listener<String>() {

			@Override
			public void onResponse(final String response) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						Log.v("json", "response-->" + response);
						if (listener != null)
							listener.onSuccess(response);
					}
				});
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(final VolleyError error) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						if (listener != null)
							listener.onError(error);
					}
				});
			}
		});
		FinanceApplication.getRequestQueue().add(request);
	}

	/**
	 * 执行get请求，并且返回我们想要的解析过的Item
	 *
	 * @param url
	 *            请求的Url
	 * @param listener
	 *            请求的回调方法
	 * @param jsonHelper
	 *            解析器，用于解析传输过来的Json字符串 这个解析器必须实现的方法是里面的getItem 方法
	 */
	public <T> void excuteGetForItem(Context context, final String url, final OnResultItemListener listener,
									 final JsonHelperImpl<T> jsonHelper) {
		Log.v("NetClient", "url-->" + url);
		final Handler handler = new Handler(context.getMainLooper());
		MyStringRequest request = new MyStringRequest(context, Method.GET, url, new Listener<String>() {

			@Override
			public void onResponse(final String response) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						Log.v("NetClient", "response-->" + response);
						if (listener != null)
							listener.onSuccess(jsonHelper.excuteJsonForItem(response));
					}
				});
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(final VolleyError error) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						if (listener != null)
							listener.onError(error);
					}
				});
			}
		});
		FinanceApplication.getRequestQueue().add(request);

	}

	/**
	 * 执行get请求，并且返回我们想要的解析过的Item
	 *
	 * @param context
	 *            context
	 * @param url
	 *            请求的Url
	 * @param listener
	 *            请求的回调方法
	 * @param jsonHelper
	 *            解析器，用于解析传输过来的Json字符串 这个解析器必须实现的方法是里面的getItems 方法
	 */
	public <T> void excuteGetForItems(Context context, final String url, final OnResultItemsListener listener,
									  final JsonHelperImpl<T> jsonHelper) {
		Log.v("NetClient", "url-->" + url);
		final Handler handler = new Handler(context.getMainLooper());
		MyStringRequest request = new MyStringRequest(context, Method.GET, url, new Listener<String>() {

			@Override
			public void onResponse(final String response) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						Log.v("NetClient", "response-->" + response);
						if (listener != null)
							listener.onSuccess(jsonHelper.excuteJsonForItems(response));
					}
				});
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(final VolleyError error) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						if (listener != null)
							listener.onError(error);
					}
				});
			}
		});
		FinanceApplication.getRequestQueue().add(request);
	}

	// public void excutePost(Context context, String url,
	// Map<String, String> params, OnResultListener listener) {
	// MyStringRequest request = new MyStringRequest(context, Method.GET, url,
	// listener, errorListener);
	// }

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

		public void onError(VolleyError error) {
			error(error);
		}
	}

	/**
	 * Item请求的回调方法
	 *
	 * @author mengfei
	 *
	 */
	 static abstract class OnResultItemListener {
		 abstract <T> void onSuccess(T item);

		 void onError(VolleyError error) {
			error(error);
		}
	}

	/**
	 * Item请求的回调方法
	 *
	 * @author mengfei
	 *
	 */
	 static abstract class OnResultItemsListener {

		 abstract <T> void onSuccess(List<T> items);

		 void onError(VolleyError error) {
			error(error);
		}
	}

	/**
	 * 解析错误信息，并产生相应的提示
	 *
	 * @param error
	 *            错误类型
	 */
	 private static void error(VolleyError error) {
		if (error instanceof NoConnectionError) {
			Toast.makeText(FinanceApplication.getInstance(), "网络链接失败，请检查网络设置", Toast.LENGTH_SHORT).show();
		} else if (error instanceof TimeoutError) {
			Toast.makeText(FinanceApplication.getInstance(), "链接超时，请稍后重试", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(FinanceApplication.getInstance(), "网络链接错误" + error, Toast.LENGTH_SHORT).show();
		}

	}

}
