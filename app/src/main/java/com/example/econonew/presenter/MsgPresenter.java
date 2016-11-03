package com.example.econonew.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.example.econonew.db.ChannelTable;
import com.example.econonew.db.DBManager;
import com.example.econonew.entity.ChannelEntity;
import com.example.econonew.entity.UserEntity;
import com.example.econonew.resource.Constant;
import com.example.econonew.resource.msg.ChannelMessage;
import com.example.econonew.resource.msg.MainMessage;
import com.example.econonew.server.NetClient;
import com.example.econonew.server.URLManager;
import com.example.econonew.server.json.ChannelJsonHelper;
import com.example.econonew.tools.Voice;
import com.example.econonew.view.activity.FinanceApplication;
import com.example.econonew.view.activity.main.MainActivity;

import java.util.List;

import static com.example.econonew.view.activity.FinanceApplication.app;

/**
 * 主界面有关消息的Presenter，处理有关消息的逻辑
 * Created by mengfei on 2016/10/23.
 */

public class MsgPresenter extends BasePresenter<MainActivity> {

    private static final String TAG = "MsgPresenter";


    public MsgPresenter(MainActivity activity) {
        super(activity);
    }

    public void refreshUserData(UserEntity user) {
        if (user != null && user.isVIP()) {
            getChannelFromNet(user);
        }
    }

    /**
     * 	从网络上面获取用户的频道信息
     * @param user 用户
     */
    private void getChannelFromNet(final UserEntity user) {
        final String url = URLManager.getChannelURL(user.getName());
        final NetClient.OnResultListener responseListener = new NetClient.OnResultListener() {

            @Override
            public void onSuccess(String response) {
                ChannelJsonHelper jsonHelper = new ChannelJsonHelper(app);
                List<ChannelEntity> channels = jsonHelper.excuteJsonForItems(response);
                if (channels != null) {
                    new DBManager().deleteAllItem(new ChannelTable());//删除所有的用户数据，重新进行缓存
                    ChannelMessage.getInstance("自定义").setMessage(channels, false, true);
                }
                FinanceApplication.getInstance().refreshPublicData();
            }

            @Override
            public void onError(VolleyError error) {
                super.onError(error);
                ChannelMessage.getInstance("自定义").stopFresh();
            }
        };
        new Thread() {
            public void run() {
                NetClient.getInstance().executeGetForString(app, url, responseListener);
            }
        }.start();
    }

    /**
     *  对消息进行阅读
     * @param tab   要从哪一个tab开始阅读
     */
    public void setRead(int tab) {
        Log.d(TAG, "setRead: " + tab);
        Voice voice = Voice.getInstance();
        if(tab < Constant.publicItemNames.length && voice.getReadStat() == Voice.VOICE_STAT_NO_READ) {
            for (int i = tab; i < Constant.publicItemNames.length; i ++) {
                MainMessage message = MainMessage.getInstance(Constant.publicItemNames[i]);
                if (message != null) {
                    voice.setList(message.getMessage());//设置要进行阅读的列表
                }
            }
        }
        voice.read();
    }
}
