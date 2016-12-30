package com.example.econonew.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.example.econonew.db.DBManager;
import com.example.econonew.db.MsgTable;
import com.example.econonew.entity.MsgItemEntity;
import com.example.econonew.resource.Constant;
import com.example.econonew.resource.msg.MainMessage;
import com.example.econonew.server.NetClient;
import com.example.econonew.server.URLManager;
import com.example.econonew.server.json.JsonCast;
import com.example.econonew.server.json.ResponseJsonHelper;
import com.example.econonew.tools.Voice;
import com.example.econonew.view.activity.BaseActivity;

import org.json.JSONObject;

import java.util.List;

import static com.example.econonew.view.activity.FinanceApplication.app;

/**
 * 主界面有关消息的Presenter，处理有关消息的逻辑
 * Created by mengfei on 2016/10/23.
 */

public class MsgPresenter extends BasePresenter<BaseActivity> {

    private static final String TAG = "MsgPresenter";


    public MsgPresenter(BaseActivity activity) {
        super(activity);
    }

    public void refreshPublicData() {
        new Thread() {
            public void run() {
                String url = URLManager.getConnectURL();
                NetClient.OnResultListener listener = new NetClient.OnResultListener() {

                    @Override
                    public void onSuccess(String response) {
                        Log.d(TAG, "onSuccess: " + response);
                        JSONObject map = JsonCast.getJsonObject(response);
                        new ResponseJsonHelper().handleInformation(map);
                    }

                    public void onError(VolleyError error) {
                        loadDatasFromDatabase();
                    }
                };
                NetClient.getInstance().executeGetForString(app, url, listener);
            }
        }.start();
    }

    //从数据库里面加载数据
    private void loadDatasFromDatabase() {
        for(String tabName : Constant.publicItemNames) {
            MsgTable table = new MsgTable(tabName);
            MainMessage message = MainMessage.getInstance(tabName);
            List<MsgItemEntity> list = new DBManager().getDbItems(table, null, null);
            Log.e(TAG, "loadDatasFromDatabase: " + tabName + " " + list.size());
            if (message != null) {
                message.setMessage(list, false, false);
            }
        }
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
