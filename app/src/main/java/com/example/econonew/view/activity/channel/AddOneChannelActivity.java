package com.example.econonew.view.activity.channel;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.econonew.R;
import com.example.econonew.entity.ChannelEntity;
import com.example.econonew.tools.ChannelListManager;
import com.example.econonew.tools.DialogTool;
import com.example.econonew.tools.listener.AddChannelClickListener;

/**
 * 添加频道的Activity
 * Created by mengfei on 2016/9/28.
 */

public class AddOneChannelActivity extends BaseChannelActivity {

    public static final String CHANNEL_NAME = "channel_name";

    private String channelName;//频道的名称
    private String[] channelFirstLabel;//频道的一级标签
    private String[] channelSecondLabel;//频道的二级标签

    private Button channelNameBtn;
    private Button channelInfoBtn;
    private Button sureAddChannelBtn;
    private TextView channelInfoTv;
    private TextView channelNameTv;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.act_add_one_channel);
        channelNameBtn = (Button) findViewById(R.id.act_add_one_channel_name_btn);
        channelInfoBtn = (Button) findViewById(R.id.act_add_one_channel_info_btn);
        sureAddChannelBtn = (Button) findViewById(R.id.act_add_one_channel_sure_btn);
        channelNameTv = (TextView) findViewById(R.id.act_add_one_channel_name_tv);
        channelInfoTv = (TextView) findViewById(R.id.act_add_one_channel_info_tv);
        initListener();

    }

    private void initListener() {
        channelNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogTool dialogName = new DialogTool(mContext, channelFirstLabel, channelNameTv);
                dialogName.setDialog();
            }
        });
        channelInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogTool dialogInfo = new DialogTool(mContext, channelSecondLabel, channelInfoTv);
                dialogInfo.setDialog();
            }
        });
        sureAddChannelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {addChannel(channelName, channelNameTv.getText().toString(), channelInfoTv.getText().toString());}
        });
    }

    public void addChannel(String channelName, String firstLabel, String secondLabel) {
        if (isAddChannel(firstLabel, secondLabel)) {
            ChannelEntity channelEntity = new ChannelEntity();
            channelEntity.setName(channelName);
            channelEntity.setType(firstLabel);
            channelEntity.setAttribute(secondLabel);
            DialogInterface.OnClickListener listener = new AddChannelClickListener(channelEntity, mContext);
            showTipDialog(null, "是否确定添加频道：" + channelEntity, listener, null);
        }
    }

    /**
     * 判断是否应该添加频道
     *
     * @param type 频道的类型
     * @param attribute 频道的子类型
     */
    public boolean isAddChannel(String type, String attribute) {
        if (!type.equals("请选择") && !attribute.equals("请选择") && !TextUtils.isEmpty(type) && !TextUtils.isEmpty(attribute)) {
            return true;
        } else {
            showTipDialog(null, "请先选择"+ channelName +"信息", null, null);
            return false;
        }
    }


    @Override
    protected void initDatas() {
        channelName = getIntent().getStringExtra(CHANNEL_NAME);
        channelFirstLabel = ChannelListManager.getChannelFirstLabel(channelName);
        channelSecondLabel = ChannelListManager.getChannelSecondLabel();
        initActionBar(false , getString(R.string.label_add_one_channel) + "\t\t" +channelName, true);
        updateUI();
    }

    //根据获取的信息来更新UI
    private void updateUI() {
        String channelNameBtnText = channelName + channelNameBtn.getText();
        channelNameBtn.setText(channelNameBtnText);
    }


}
