package com.example.econonew.view.activity.channel;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.econonew.R;
import com.example.econonew.entity.ChannelEntity;
import com.example.econonew.resource.Constant;
import com.example.econonew.resource.msg.ChannelMessage;
import com.example.econonew.server.NetClient;
import com.example.econonew.server.URLManager;
import com.example.econonew.view.activity.BaseActivity;
import com.example.econonew.view.activity.FinanceApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * AddChannelFromCodeActivity
 * 在channelActivity中调用共同生成频道添加界面
 */

public class AddChannelFromCodeActivity extends BaseActivity {
    private EditText expert_info_show;
    private Button channel_sure;  //确认按钮

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.act_addchannel_from_code);
        channel_sure = (Button) findViewById(R.id.special_channel_sure);
        expert_info_show = (EditText) findViewById(R.id.expert_info_show);

        channel_sure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addChannelFromCode(expert_info_show.getText().toString());
            }
        });
        initActionBar(false, getResources().getString(R.string.label_add_one_channel), true);
    }

    private void addChannelFromCode(final String s) {
        if (TextUtils.isEmpty(s)) {
            showToast("请输入股票代码");
        } else {
            new Thread() {
                @Override
                public void run() {
                    final ChannelEntity entity = new ChannelEntity();
                    entity.setCode(s);
                    String setChannelURL = URLManager.getSetChannelURL(Constant.user.getName(), entity);
                    Log.d("url", "run: " + setChannelURL);
                    NetClient.getInstance().executeGetForString(mContext, setChannelURL, new NetClient.OnResultListener() {
                        @Override
                        public void onSuccess(String response) {
                            List<ChannelEntity> list = new ArrayList<>(1);
                            list.add(entity);
                            ChannelMessage.getInstance("自定义").setMessage(list, true, true);
                            mContext.showTipDialog(null, "频道添加成功", null, null);
                            expert_info_show.setText("");
                            FinanceApplication.getInstance().refreshUserData(Constant.user);
                        }

                    });
                }
            }.start();
        }
    }

    @Override
    protected void initDatas() {

    }
}
