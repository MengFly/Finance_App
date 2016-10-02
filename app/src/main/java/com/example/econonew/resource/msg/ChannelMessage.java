package com.example.econonew.resource.msg;

import android.content.Context;

import com.example.econonew.entity.ChannelEntity;
import com.example.econonew.resource.DB_Information;
import com.example.econonew.view.fragment.ChannelMessageFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户自定义频道的消息管理类
 * Created by mengfei on 2016/10/2.
 */

public class ChannelMessage implements IMessage<ChannelEntity> {

    private static Map<String, ChannelMessage> msgManager = new HashMap<>();

    private String allMsgName;
    private ChannelMessageFragment mFragment;

    private List<ChannelEntity> mChannelList; // 自定义频道的列表

    private DB_Information dataBaseManager;

    /**
     * 定义消息管理的类
     *
     * @param context
     *            context
     * @param allMsgName
     *            每一栏消息的名称，可以通过消息的名称获得具体消息的实例
     */
    public ChannelMessage(Context context, ChannelMessageFragment fragment, String allMsgName) {
        this.allMsgName = allMsgName;
        this.mFragment = fragment;
        mChannelList = new ArrayList<>();
        this.dataBaseManager = new DB_Information(context);
        msgManager.put(allMsgName, this);
    }

    public static ChannelMessage getInstance(String name) {
        if(msgManager.containsKey(name)) {
            return msgManager.get(name);
        } else {
            return null;
        }
    }


    /**
     * 通知Fragment停止刷新操作
     */
    public void stopFresh() {
        mFragment.stopFresh();
    }

    /**
     * 获得频道的名称
     */
    public String getName() {
        return allMsgName;
    }


    @Override
    public boolean removeMsg(ChannelEntity item) {
        boolean isRemove = false;
        if (this.mChannelList.contains(item)) {
            dataBaseManager.removeChannel(item);
            isRemove = this.mChannelList.remove(item);
            sentMsgToUiAndVoice();
        }
        return isRemove;
    }

    /**
     * 向UI线程和Voice发送消息
     */
    private void sentMsgToUiAndVoice() {
        this.mFragment.setList(mChannelList);
    }

    @Override
    public void setMessage(List<ChannelEntity> msgList, boolean isAddEnd, boolean isSaveToDatabase) {
        if (!isAddEnd) {
            mChannelList.clear();
        }
        if (msgList != null) {
            mChannelList.addAll(msgList);
        }
        sentMsgToUiAndVoice();
        if (isSaveToDatabase) {
            dataBaseManager.saveAllChannel(msgList);
        }
    }

    @Override
    public List<ChannelEntity> getMessage() {
        return mChannelList;
    }

    @Override
    public int getMsgCount() {
        return getMessage().size();
    }

}
