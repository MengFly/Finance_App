package com.example.econonew.presenter;

import android.content.DialogInterface;

import com.android.volley.VolleyError;
import com.example.econonew.entity.ChannelEntity;
import com.example.econonew.resource.Constant;
import com.example.econonew.resource.msg.ChannelMessage;
import com.example.econonew.server.NetClient;
import com.example.econonew.server.json.JsonCast;
import com.example.econonew.tools.URLManager;
import com.example.econonew.view.activity.FinanceApplication;
import com.example.econonew.view.activity.channel.BaseChannelActivity;
import com.example.econonew.view.activity.channel.ChannelAddActivity;

import org.json.JSONObject;

import java.util.Arrays;


/**
 * 频道添加的Presenter，用于处理频道添加的一些逻辑
 * Created by mengfei on 2016/9/28.
 */

public class ChannelPresenter extends BasePresenter<BaseChannelActivity> {

    private Thread addChannelThread;//添加频道的线程
    private Thread removeChannelThread;//移除频道的线程
    private Thread getChannelsThread;//获取频道的线程

    public ChannelPresenter(BaseChannelActivity activity) {
        super(activity);
    }

    /**
     * 开启添加频道的线程
     * @param channelEntity 频道类个体
     */
    public void openAddChannelThread(ChannelEntity channelEntity) {
        if(!isHaveData(channelEntity)) {
            initAddChannelThread(channelEntity);
            addChannelThread.start();
        } else {
            mActivity.showToast("你已经添加过这个频道了，不必在此添加了");
        }
    }

    //初始化添加频道的线程
    private void initAddChannelThread(final ChannelEntity channelEntity) {
        final String url = URLManager.getSetChannelURL(Constant.user.getName(), channelEntity);
        final NetClient.OnResultListener responseListener = new NetClient.OnResultListener() {

            @Override
            public void onSuccess(String response) {
                JSONObject obj = JsonCast.getJsonObject(response);
                if (obj != null) {
                    if ("success".equals(JsonCast.getString(obj, "status"))) {
                        // TODO: 2016/10/5  这里添加解析json的逻辑
                        addData(channelEntity);
                    } else {
                        mActivity.showToast(JsonCast.getString(obj, "result"));
                    }
                } else {
                    mActivity.showToast("添加频道失败");
                }
                mActivity.hintProDialog();
            }

            @Override
            public void onError(VolleyError error) {
                super.onError(error);
                mActivity.hintProDialog();
            }
        };
        addChannelThread = new Thread() {
            @Override
            public void run() {
                NetClient.getInstance().excuteGetForString(mActivity, url, responseListener);
            }
        };
    }

    private void addData(ChannelEntity... entity) {
        ChannelMessage messageManager = ChannelMessage.getInstance("自定义");// 将设置的频道信息设置到自定义信息里面并进行存储
        if (messageManager != null) {
            messageManager.setMessage(Arrays.asList(entity), true, true);
        }
        FinanceApplication.getInstance().refreshUserData(Constant.user);
        mActivity.showTipDialog(null, "设置成功，是否再次设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mActivity.backHomeActivity();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mActivity.openOtherActivity(ChannelAddActivity.class, true);
            }
        });
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
