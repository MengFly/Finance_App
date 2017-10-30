package com.example.econonew.resource.msg;

import com.example.econonew.db.DBHelperFactory;
import com.example.econonew.entity.ChannelEntity;
import com.example.econonew.entity.MsgItemEntity;
import com.example.econonew.resource.Constant;
import com.example.econonew.view.fragment.MainMessageFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于管理主界面的公共消息的消息管理类
 * Created by mengfei on 2016/10/2.
 */

public class MainMessage implements IMessage<MsgItemEntity> {

    private static Map<String, MainMessage> msgManager = new HashMap<>();

    private String msgName;//消息管理的名称，用于获取具体的消息管理的实例
    private MainMessageFragment mFragment;//视图的管理类，提供一些更新UI的方法

    private List<MsgItemEntity> mAllList;// 消息列表
    private List<MsgItemEntity> mVipList;//Vip消息列表
    private List<MsgItemEntity> mNotVipList; // 不是VIP的列表

    public MainMessage(MainMessageFragment fragment, String allMsgName) {
        this.msgName = allMsgName;
        this.mFragment = fragment;
        mAllList = new ArrayList<>();
        mVipList = new ArrayList<>();
        mNotVipList = new ArrayList<>();
        msgManager.put(allMsgName, this);
    }


    public static MainMessage getInstance(String msgName) {
        if(msgManager.containsKey(msgName)) {
            return msgManager.get(msgName);
        } else {
            return null;
        }
    }

    @Override
    public void setMessage(List<MsgItemEntity> msgList, boolean isAddEnd, boolean isSaveToDatabase) {
        if (!isAddEnd) {
            clearAllMsg();
        }
        Collections.sort(msgList);
        addList(msgList);
        sentMsgToUiAndVoice();
        if (isSaveToDatabase) {
            DBHelperFactory.getDBHelper().insertAllItems(MsgItemEntity.class, msgList);
        }
    }

    private void addList(List<MsgItemEntity> msgList) {
        if (msgList != null) {
            mAllList.addAll(msgList);
            mNotVipList.addAll(getNotVipMsgList(msgList));
            mVipList.addAll(getVipMsgList(msgList));
        }
    }

    //获取不是Vip的消息
    private List<MsgItemEntity> getNotVipMsgList(List<MsgItemEntity> allList) {
        List<MsgItemEntity> notVipMsgList = new ArrayList<>();
        for (MsgItemEntity msgItemEntity : allList) {
            if (!msgItemEntity.isVip()) {
                notVipMsgList.add(msgItemEntity);
            }
        }
        return notVipMsgList;
    }

    //获取Vip的消息
    private List<MsgItemEntity> getVipMsgList(List<MsgItemEntity> list) {
        List<MsgItemEntity> vipList = new ArrayList<>();
        for (MsgItemEntity entity : list) {
            if (!entity.isVip()) {
                vipList.add(entity);
            } else if (isDingZhiMsg(entity)) {//如果是定制的消息就添加到列表里面
                vipList.add(entity);
            }
        }
        return vipList;
    }

    //是否是定制的消息
    private boolean isDingZhiMsg(MsgItemEntity entity) {
        ChannelMessage channelMessage = ChannelMessage.getInstance("自定义");
        boolean isDingZhi = false;
        if (channelMessage != null) {
            List<ChannelEntity> channels = channelMessage.getMessage();
            for (ChannelEntity channelEntity : channels) {
                if (entity.isDingZhi(channelEntity)) {
                    isDingZhi =  true;
                    break;
                }
            }
        }
        return  isDingZhi;
    }

    //清除消息列表
    private void clearAllMsg() {
        mAllList.clear();
        mNotVipList.clear();
        mVipList.clear();
    }

    @Override
    public List<MsgItemEntity> getMessage() {
        if (Constant.user != null && Constant.user.isVIP()) {
            return mVipList;
        } else {
            return mNotVipList;
        }
    }

    @Override
    public int getMsgCount() {
        return getMessage().size();
    }


    @Override//Not Use This Method
    public boolean removeMsg(MsgItemEntity item) {
        return false;
    }

    //向UI线程和Voice发送消息
    private void sentMsgToUiAndVoice() {
        this.mFragment.setList(getMessage());
    }


}
