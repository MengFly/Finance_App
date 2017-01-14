package com.example.econonew.presenter;

import android.content.DialogInterface;
import android.text.TextUtils;

import com.example.econonew.entity.ChannelEntity;
import com.example.econonew.tools.listener.AddChannelClickListener;
import com.example.econonew.view.activity.channel.AddOneChannelActivity;

/**
 * Created by mengfei on 2017/1/14.
 */
public class AddOneChannelPresenter extends BasePresenter<AddOneChannelActivity> {

    private String channelName;
    public AddOneChannelPresenter(AddOneChannelActivity activity) {
        super(activity);
    }
    /**
     * 判断是否应该添加频道
     *
     * @param type      频道的类型
     * @param attribute 频道的子类型
     */
    public boolean isAddChannel(String type, String attribute) {
        if (!type.equals("请选择") && !attribute.equals("请选择") && !TextUtils.isEmpty(type) && !TextUtils.isEmpty(attribute)) {
            return true;
        } else {
            mActivity.showTipDialog(null, "请先选择" + channelName + "信息", null, null);
            return false;
        }
    }

    public void addChannel(String channelName, String firstLabel, String secondLabel) {
        if (isAddChannel(firstLabel, secondLabel)) {
            ChannelEntity channelEntity = new ChannelEntity();
            channelEntity.setName(channelName);
            channelEntity.setType(firstLabel);
            channelEntity.setAttribute(secondLabel);
            DialogInterface.OnClickListener listener = new AddChannelClickListener(channelEntity, mActivity);
            mActivity.showTipDialog(null, "是否确定添加频道：" + channelEntity, listener, null);
        }
    }

}
