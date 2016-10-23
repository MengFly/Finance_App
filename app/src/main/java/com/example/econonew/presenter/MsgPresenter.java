package com.example.econonew.presenter;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.econonew.entity.ChannelEntity;
import com.example.econonew.resource.Constant;
import com.example.econonew.resource.DB_Information;
import com.example.econonew.resource.UserInfo;
import com.example.econonew.resource.msg.ChannelMessage;
import com.example.econonew.resource.msg.MainMessage;
import com.example.econonew.server.NetClient;
import com.example.econonew.server.json.ChannelJsonHelper;
import com.example.econonew.tools.SettingManager;
import com.example.econonew.tools.URLManager;
import com.example.econonew.tools.Voice;
import com.example.econonew.view.activity.FinanceApplication;
import com.example.econonew.view.activity.main.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
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

    public void refreshUserData(UserInfo user) {
        if (user != null && user.isVIP()) {
            getChannelFromNet(user);
        } else {
            if (SettingManager.getInstance().isInitDataFinish()) {
                List<ChannelEntity> channelList = new DB_Information(app).getChannel(user);
                ChannelMessage.getInstance("自定义").setMessage(channelList, false, false);
            }
        }
    }

    /**
     * 	从网络上面获取用户的频道信息
     * @param user 用户
     */
    private void getChannelFromNet(final UserInfo user) {
        final String url = URLManager.getChannelURL(user.getName());
        final NetClient.OnResultListener responseListener = new NetClient.OnResultListener() {

            @Override
            public void onSuccess(String response) {
                saveUpdateDate(user.getName());
                ChannelJsonHelper jsonHelper = new ChannelJsonHelper(app);
                List<ChannelEntity> channels = jsonHelper.excuteJsonForItems(response);
                if (channels == null) {
                    Toast.makeText(app, jsonHelper.getErrorTip(), Toast.LENGTH_SHORT).show();
                } else {
                    new DB_Information(app).removeSelfChannel(user.getName());
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
                NetClient.getInstance().excuteGetForString(app, url, responseListener);
            }
        }.start();
    }

    private void saveUpdateDate(String userName) {
        SharedPreferences spf = mActivity.getSharedPreferences(Constant.SPF_KEY_UPDATE_DATE, MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString(userName, SimpleDateFormat.getDateInstance().format(new Date()));
        edit.apply();
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
