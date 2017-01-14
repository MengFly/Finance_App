package com.example.econonew.view.activity.channel;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.example.econonew.R;
import com.example.econonew.databinding.ActAddOneChannelBinding;
import com.example.econonew.presenter.AddOneChannelPresenter;
import com.example.econonew.tools.ChannelListManager;
import com.example.econonew.tools.DialogTool;
import com.example.econonew.view.activity.BaseActivity;

/**
 * 添加频道的Activity
 * Created by mengfei on 2016/9/28.
 */

public class AddOneChannelActivity extends BaseActivity<AddOneChannelPresenter> {

    private ActAddOneChannelBinding mBinding;
    public static final String CHANNEL_NAME = "channel_name";
    private String channelName;//频道的名称

    @Override
    protected void initView(Bundle savedInstanceState) {
        mBinding = DataBindingUtil.setContentView(mContext, R.layout.act_add_one_channel);
        bindPresenter(new AddOneChannelPresenter(this));
        mBinding.setPresenter(mPresenter);
        initListener();
    }

    private void initListener() {
        mBinding.actAddOneChannelNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogTool dialogName = new DialogTool(mContext, ChannelListManager.getChannelFirstLabel(channelName), mBinding.actAddOneChannelNameTv);
                dialogName.setDialog();
            }
        });
        mBinding.actAddOneChannelInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogTool dialogInfo = new DialogTool(mContext, ChannelListManager.getChannelSecondLabel(), mBinding.actAddOneChannelInfoTv);
                dialogInfo.setDialog();
            }
        });
        mBinding.actAddOneChannelSureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addChannel(channelName,
                        mBinding.actAddOneChannelNameTv.getText().toString(),
                        mBinding.actAddOneChannelInfoTv.getText().toString());
            }
        });
    }


    @Override
    protected void initDatas() {
        channelName = getIntent().getStringExtra(CHANNEL_NAME);
        mBinding.setChannelType(channelName);
        initActionBar(false, getString(R.string.label_add_one_channel) + "\t\t" + channelName, true);
    }

}
