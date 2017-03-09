package com.example.econonew.presenter;

import com.android.volley.VolleyError;
import com.example.econonew.db.DBHelperFactory;
import com.example.econonew.entity.ChannelEntity;
import com.example.econonew.entity.UserEntity;
import com.example.econonew.resource.Constant;
import com.example.econonew.resource.msg.ChannelMessage;
import com.example.econonew.server.NetClient;
import com.example.econonew.server.URLManager;
import com.example.econonew.server.json.ChannelJsonHelper;
import com.example.econonew.view.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * 频道添加的Presenter，用于处理频道添加的一些逻辑
 * Created by mengfei on 2016/9/28.
 */

public class ChannelPresenter extends BasePresenter<BaseActivity> {

    public ChannelPresenter(BaseActivity activity) {
        super(activity);
    }

    /**
     * 刷新用户的频道
     */
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
    private void getChannelFromNet(final UserEntity user) {
        final String url = URLManager.getChannelURL(user.getName());
        final ChannelMessage message = ChannelMessage.getInstance("自定义");
        final NetClient.OnResultListener responseListener = new NetClient.OnResultListener() {

            @Override
            public void onSuccess(String response) {
                ChannelJsonHelper jsonHelper = new ChannelJsonHelper(mActivity);
                List<ChannelEntity> channels = jsonHelper.excuteJsonForItems(response);
                if (channels != null) {
                    DBHelperFactory.getDBHelper().deleteAll(ChannelEntity.class);
                    if (message != null) {
                        message.setMessage(channels, false, true);
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                super.onError(error);
                List<ChannelEntity> channelEntities = DBHelperFactory.getDBHelper().queryItems(ChannelEntity.class, 0, null, "username=?", Constant.user.getName());
                if (message != null) {
                    message.setMessage(channelEntities, false, false);
                }
            }
        };
        new Thread() {
            public void run() {
                NetClient.getInstance().executeGetForString(mActivity, url, responseListener);
            }
        }.start();
    }

    /**
     * 删除频道
     */
    public void deleteChannel(String messageName, final ChannelEntity entity) {
        if (Constant.user != null) {
            mActivity.showProDialog();
            deleteChannelThread(messageName, entity);// 开启一个删除频道的线程
        } else {
            mActivity.showToast("当前还没有登录，不能进行删除频道的操作");
        }
    }
    //请求删除频道的线程
    private void deleteChannelThread(final String messaegName, final ChannelEntity entity) {
        final String url = URLManager.getDeleteChannelURL(Constant.user.getName(), entity);
        final NetClient.OnResultListener listener = new NetClient.OnResultListener() {

            @Override
            public void onSuccess(String response) {
                mActivity.showToast("频道删除成功");
                ChannelMessage message = ChannelMessage.getInstance(messaegName);
                if (message != null) {
                    message.removeMsg(entity);
                }
            }
        };
        new Thread() {
            @Override
            public void run() {
                NetClient.getInstance().executeGetForString(mActivity, url, listener);
            }
        }.start();
    }

}
