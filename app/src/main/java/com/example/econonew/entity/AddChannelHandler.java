package com.example.econonew.entity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.econonew.resource.AllMessage;
import com.example.econonew.resource.Constant;
import com.example.econonew.resource.DB_Information;
import com.example.econonew.view.activity.BaseActivity;
import com.example.econonew.view.activity.channel.ChannelAddActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个类是用于频道添加的Handler类 用于生成频道添加处理信息的Handler
 *
 * 用于各添加频道界面使用的Handler 将handler里面要处理的事情，重复的代码抽取出来组合成一个类
 *
 * @author mengfei
 *
 */
public class AddChannelHandler extends Handler {

	private BaseActivity mContext;

	// 各频道的父类，用于初始化频道
	private ChannelEntity mAddChannel;

	// 数据表名称，在初始化类的时候通过回调函数进行设置
	private String dataTableName;

	private DialogInterface.OnClickListener okListener, cancelListener;

	/**
	 *
	 * @param context
	 *            context
	 * @param addChannel
	 *            各频道的管理类
	 */
	public AddChannelHandler(BaseActivity context, ChannelEntity addChannel) {
		super();
		this.mContext = context;
		this.mAddChannel = addChannel;
		dataTableName = Constant.getSelfTableName(addChannel.getName());
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		String result = (String) msg.obj;
		JSONObject obj;
		try {
			obj = new JSONObject(result);
			if("success".equals(obj.getString("status"))) {
				addData(Constant.user.getName(), this.mAddChannel);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}


		mContext.hintProDialog();
	}

	private void addData(String name, ChannelEntity entity) {

		// 创建股票定制对象并存储数据库
		SQLiteDatabase db = new DB_Information(mContext).getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("user_name", name);
		values.put("name", entity.getName());
		values.put("type", entity.getType());
		values.put("attribute", entity.getAttribute());
		if (!isHaveData(entity)) {
			List<ChannelEntity> list = new ArrayList<ChannelEntity>();
			db.insert(dataTableName, null, values);
			list.add(entity);
			AllMessage.getInstance("自定义").setChannels(list, true, false);// 将设置的频道信息设置到自定义信息里面
			initListener();
			mContext.showTipDialog(null, "设置成功，是否再次设置",
					okListener, cancelListener);
			Toast.makeText(mContext, "频道添加成功", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(mContext, "你已经添加过这个频道，不用再次添加了", Toast.LENGTH_SHORT)
					.show();
		}
		db.close();

	}

	// 初始化点击事件
	private void initListener() {
		cancelListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				mContext.backHomeActivity();
			}
		};
		okListener = new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mContext.openOtherActivity(ChannelAddActivity.class, false);
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
		String sql = "select * from " + dataTableName + " where user_name="
				+ Constant.user.getName();
		Log.v("testUtils", sql);
		Cursor cursor = db.rawQuery(sql, null);
		int count = cursor.getCount();
		if (count > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String type = cursor.getString(cursor.getColumnIndex("type"));
				String attribute = cursor.getString(cursor.getColumnIndex("attribute"));
				if (data.getName().equals(name) && data.getType().equals(type)
						&& data.getAttribute().equals(attribute))
					return true;
			}
			return false;
		} else {
			return false;
		}
	}
}