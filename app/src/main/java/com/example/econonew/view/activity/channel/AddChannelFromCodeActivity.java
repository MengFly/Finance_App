package com.example.econonew.view.activity.channel;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.econonew.R;
import com.example.econonew.databinding.ActAddchannelFromCodeBinding;
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

    private ActAddchannelFromCodeBinding mBinding;

    @Override
    protected void initView(Bundle savedInstanceState) {
        mBinding = DataBindingUtil.setContentView(mContext, R.layout.act_addchannel_from_code);
        mBinding.specialChannelSure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addChannelFromCode(mBinding.expertInfoShow.getText().toString());
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
                            mBinding.expertInfoShow.setText("");
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
