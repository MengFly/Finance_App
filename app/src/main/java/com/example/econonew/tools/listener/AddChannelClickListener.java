package com.example.econonew.tools.listener;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.econonew.entity.ChannelEntity;
import com.example.econonew.resource.Constant;
import com.example.econonew.resource.msg.ChannelMessage;
import com.example.econonew.server.NetClient;
import com.example.econonew.server.URLManager;
import com.example.econonew.server.json.JsonCast;
import com.example.econonew.view.activity.BaseActivity;
import com.example.econonew.view.activity.FinanceApplication;
import com.example.econonew.view.activity.channel.ChannelAddActivity;

import org.json.JSONObject;

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

	private BaseActivity mContext;


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
		mContext.showProDialog();
		final String url = URLManager.getSetChannelURL(Constant.user.getName(), mAddChannel);
		final NetClient.OnResultListener responseListener = new NetClient.OnResultListener() {

			@Override
			public void onSuccess(String response) {
				JSONObject obj = JsonCast.getJsonObject(response);
				int id = JsonCast.getInt(obj, "result");
				mAddChannel.setChannelId(id);
				addData(mAddChannel);
			}

			@Override
			public void onError(VolleyError error){}
		};
		// 给服务器发送定制信息
		new Thread() {
			public void run() {
				NetClient.getInstance().executeGetForString(mContext, url, responseListener);
			};
		}.start();

	}

	private DialogInterface.OnClickListener okListener, cancelListener;

	private void addData(ChannelEntity entity) {
		List<ChannelEntity> list = Arrays.asList(entity);
		ChannelMessage messageManager = ChannelMessage.getInstance("自定义");// 将设置的频道信息设置到自定义信息里面并进行存储
		if (messageManager != null) {
			messageManager.setMessage(list, true, true);
		}
		FinanceApplication.getInstance().refreshUserData(Constant.user);
		initListener();
		mContext.showTipDialog(null, "设置成功，是否再次设置", okListener, cancelListener);
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
				mContext.openOtherActivity(ChannelAddActivity.class, true);
			}
		};
	}

	/**
	 * 判断是不是已经添加过这个频道
	 * @param data 频道信息
	 */
	private boolean isHaveData(ChannelEntity data) {
		ChannelMessage channelMessage = ChannelMessage.getInstance("自定义");
		return channelMessage != null && channelMessage.getMessage().contains(data);
	}

}
