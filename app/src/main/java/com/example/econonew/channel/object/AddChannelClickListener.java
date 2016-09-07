package com.example.econonew.channel.object;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.econonew.activity.BaseActivity;
import com.example.econonew.activity.ChannelAddActivity;
import com.example.econonew.activity.FinanceApplication;
import com.example.econonew.activity.MainActivity;
import com.example.econonew.entity.ChannelEntity;
import com.example.econonew.main.object.AllMessage;
import com.example.econonew.resource.Constant;
import com.example.econonew.resource.DB_Information;
import com.example.econonew.server.JsonCast;
import com.example.econonew.server.NetClient;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;


/**
 * 这是新增的一个类 实现了DialogInterface.OnClickListener接口
 * 主要是为了将每个添加频道的Activity里面的Listener抽取出来 简化代码，便于对代码的修改
 *
 * @author mengfei
 *
 */
public class AddChannelClickListener implements DialogInterface.OnClickListener {

	private ChannelEntity mAddChannel;

	private Context mContext;

	private String mTableName;

	/**
	 * 点击事件的初始化构造方法
	 * @param addChannel
	 *            具体的添加的频道的对象
	 * @param context
	 *            context
	 */
	public AddChannelClickListener(ChannelEntity addChannel, BaseActivity context) {
		this.mAddChannel = addChannel;
		this.mContext = context;
		mTableName = Constant.getSelfTableName(addChannel.getName());
		initListener();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (isHaveData(mAddChannel)) {// 如果已经添加过这个频道，就不用再添加了
			Toast.makeText(mContext, "你已经添加过这个频道，不用再次添加了", Toast.LENGTH_SHORT).show();
		} else {
			addChannelThread();// 开启一个添加频道的线程
		}
	}

	private void addChannelThread() {
		final String msg = encodeParams();// 获取转码后的url
		Log.v("channel_info", msg);
		((BaseActivity) mContext).showProDialog();
		final String url = Constant.URL + "/" + Constant.OPERATION_SETCHNL + "?" + msg;
		final NetClient.OnResultListener responseListener = new NetClient.OnResultListener() {

			@Override
			public void onSuccess(String response) {
				JSONObject obj = JsonCast.getJsonObject(response);
				if (obj != null) {
					if ("success".equals(JsonCast.getString(obj, "status"))) {
						int id = JsonCast.getInt(obj, "result");
						mAddChannel.setId(id);
						addData(Constant.user.getName(), mAddChannel);
					} else {
						Toast.makeText(mContext, JsonCast.getString(obj, "result"), Toast.LENGTH_LONG).show();
						((BaseActivity) mContext).showTipDialog(null, JsonCast.getString(obj, "result"), null, null);
					}
				} else {
					Toast.makeText(FinanceApplication.app, "添加频道失败", Toast.LENGTH_SHORT).show();
				}

				((BaseActivity) mContext).hintProDialog();
			}

			@Override
			public void onError(VolleyError error) {
				super.onError(error);
				((BaseActivity) mContext).hintProDialog();
			}
		};
		// 给服务器发送定制信息
		new Thread() {
			public void run() {
				NetClient.getInstance().excuteGetForString(mContext, url, responseListener);
			};
		}.start();

	}

	// 对咬传输的Url进行转码
	private String encodeParams() {
		StringBuilder msg = new StringBuilder();
		try {
			msg.append("phone=");
			msg.append(Constant.user.getName());
			msg.append("&typeName=");
			msg.append(URLEncoder.encode(mAddChannel.getName(), "utf-8"));
			msg.append("&domainName=");
			msg.append(URLEncoder.encode(mAddChannel.getType(), "utf-8"));
			msg.append("&stairName=");
			msg.append(URLEncoder.encode(mAddChannel.getAttribute(), "utf-8"));
			msg.append("&stockCode=");
			msg.append(URLEncoder.encode(mAddChannel.getCode(), "utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg.toString();
	}

	private DialogInterface.OnClickListener okListener, cancelListener;

	private void addData(String name, ChannelEntity entity) {
		List<ChannelEntity> list = Arrays.asList(entity);
		AllMessage.getInstance("自定义").setChannels(list, true, true);// 将设置的频道信息设置到自定义信息里面并进行存储
		initListener();
		((BaseActivity) mContext).showTipDialog(null, "设置成功，是否再次设置", okListener, cancelListener);
	}

	// 初始化点击事件
	private void initListener() {
		cancelListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				Intent intent = new Intent();
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setClass(mContext, MainActivity.class);
				mContext.startActivity(intent);
			}
		};
		okListener = new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent();
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setClass(mContext, ChannelAddActivity.class);
				mContext.startActivity(intent);
			}
		};
	}

	/**
	 * 判断数据库中 是不是已经添加过这个频道
	 *
	 * @param data
	 *            频道信息
	 */
	private boolean isHaveData(ChannelEntity data) {
		SQLiteDatabase db = new DB_Information(mContext).getReadableDatabase();
		String sql = "select * from " + mTableName + " where user_name=" + Constant.user.getName();
		Log.v("testUtils", sql);
		Cursor cursor = db.rawQuery(sql, null);
		int count = cursor.getCount();
		if (count > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String type = cursor.getString(cursor.getColumnIndex("type"));
				String attribute = cursor.getString(cursor.getColumnIndex("attribute"));

				if (data.getName().equals(name) && data.getType().equals(type) && data.getAttribute().equals(attribute))
					return true;
			}
			return false;
		} else {
			return false;
		}
	}

}
