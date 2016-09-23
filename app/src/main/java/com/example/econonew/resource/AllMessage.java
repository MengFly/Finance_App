package com.example.econonew.resource;

import android.content.Context;
import android.util.Log;

import com.example.econonew.entity.ChannelEntity;
import com.example.econonew.entity.MsgItemEntity;
import com.example.econonew.view.fragment.MsgListFragment;
import com.example.econonew.tools.Voice;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 这个类只用管理消息（里面包括消息的列表，读取消息的Voice）
 * AllMessage通过引用Fragment对象，可以在消息改变的时候通知fragment进行相应的视图的改变
 *
 * @author mengfei
 *
 */
public class AllMessage implements InterMessage<MsgItemEntity> {

    private static final String TAG = "AllMessage";
    private static Map<String, AllMessage> msgManager = new HashMap<>();

    private JSONObject info; // 关于信息的介绍
    private String allMsgName;
    private MsgListFragment mFragment;

    private Voice voice;
    private List<MsgItemEntity> mAllList;// 消息列表
    private List<MsgItemEntity> mVipList;//Vip消息列表
    private List<MsgItemEntity> mNotVipList; // 不是VIP的列表

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
    public AllMessage(Context context, MsgListFragment fragment, String allMsgName) {
        this.allMsgName = allMsgName;
        this.mFragment = fragment;
        if (allMsgName.equals("自定义")) {
            mChannelList = new ArrayList<>();
        } else {
            mAllList = new ArrayList<>();
            mNotVipList = new ArrayList<>();
            mVipList = new ArrayList<>();
        }
        this.voice = new Voice(context);
        this.dataBaseManager = new DB_Information(context);
        msgManager.put(allMsgName, this);
    }

    /**
     * 重置语音状态
     */
    public void resetVoice() {
        voice.flag = 0;
    }

    @Override
    public JSONObject getInfo() {
        return info;
    }

    @Override
    public void setInfo() {

    }

    public void readMsgList() {
        if (getMsgCount() == 0) {
            voice.readStr("当前没有信息，刷新后再查看");
        } else {
            voice.read();
        }
    }

    /**
     * 通知Fragment停止刷新操作
     */
    public void stopFreash() {
        mFragment.stopFresh();
    }

    /**
     * 获得频道的名称
     */
    public String getName() {
        return allMsgName;
    }

    public List<MsgItemEntity> getMsgList() {
        if (Constant.user != null && Constant.user.isVIP()) {
            return mVipList;
        } else {
            return mNotVipList;
        }

    }

    /**
     * 获取自定义的频道的信息
     */
    public List<ChannelEntity> getChannelList() {
        return mChannelList;
    }

    /**
     * 为消息列表添加信息 当列表消息发生改变的时候，就通知Fragment来给变视图
     *
     * @param list
     *            信息
     * @param isAddEnd
     *            是否添加在已有信息的后面
     */
    public void setMsg(final List<MsgItemEntity> list, boolean isAddEnd, boolean isSaveToDataBase) {
        if (!isAddEnd) {
            mAllList.clear();
            mNotVipList.clear();
            mVipList.clear();
        }
        if (list != null) {
            mAllList.addAll(list);
            mNotVipList.addAll(getNotVipMsgList(list));
            mVipList.addAll(getVipMsgList(list));
        }
        sentMsgToUiAndVoice();
        if (isSaveToDataBase) {
            dataBaseManager.saveAllMsg(Constant.getTableName(allMsgName), list);// 如果保存，就保存道数据库面
        }
    }

    private List<MsgItemEntity> getVipMsgList(List<MsgItemEntity> list) {
        List<MsgItemEntity> vipList = new ArrayList<>();
        for (MsgItemEntity entity : list) {
            if(!entity.isVip()) {
                vipList.add(entity);
            } else if(isDingZhiMsg(entity)) {//如果是定制的消息就添加到列表里面
                vipList.add(entity);
            }
        }
        return vipList;
    }

    private boolean isDingZhiMsg(MsgItemEntity entity) {
        AllMessage channelMessage = AllMessage.getInstance("自定义");
        if(channelMessage!= null) {
            List<ChannelEntity> channels = channelMessage.getChannelList();
            for (ChannelEntity channelEntity : channels) {
                boolean isAddMsg = channelEntity.getBusinessDomainId() == entity.getBusinessDomainId() &&
                        channelEntity.getBusinessTypeId() == entity.getBusinessTypeId() &&
                        channelEntity.getStairId() == entity.getStairId();
               if(isAddMsg) {
                   return true;
               }
            }
            return false;
        } else {
            return false;
        }
    }


    private List<MsgItemEntity> getNotVipMsgList(List<MsgItemEntity> allList) {
        List<MsgItemEntity> notVipMsgList = new ArrayList<>();
        for (MsgItemEntity msgItemEntity : allList) {
            if (!msgItemEntity.isVip()) {
                notVipMsgList.add(msgItemEntity);
            }
        }
        return notVipMsgList;
    }

    /**
     * 添加频道
     *
     * @param list
     *            channelList
     * @param isAddEnd
     *            是否添加到现有list的后面
     * @param isSaveToDataBase
     *            是否存储到本地数据库里面
     */
    public void setChannels(List<ChannelEntity> list, boolean isAddEnd, boolean isSaveToDataBase) {
        if (!isAddEnd) {
            mChannelList.clear();
        }
        if (list != null) {
            for (ChannelEntity channelEntity : list) {
                mChannelList.add(channelEntity);
            }
        }
        sentMsgToUiAndVoice();
        if (isSaveToDataBase) {
            dataBaseManager.saveAllChannel(list);
        }
    }

    /**
     * 获得消息列表里面的消息的数量
     *
     * @return 消息的数量
     */
    public int getMsgCount() {
        if (Constant.user != null && Constant.user.isVIP()) {
            return mVipList.size();
        } else {
            return mNotVipList.size();
        }
    }

    /**
     * 通过频道的名称获得具体的频道
     *
     * @param allMsgName
     *            频道的名称
     * @return AllMessage
     */
    public static AllMessage getInstance(String allMsgName) {
        if (msgManager.containsKey(allMsgName)) {
            return msgManager.get(allMsgName);
        } else {
            return null;
        }
    }

    /**
     * 移除消息
     *
     * @param item
     *            要移除的消息
     */
    public void removeChannel(ChannelEntity item) {
        if (this.mChannelList.contains(item)) {
            Log.d(TAG, "removeChannel: " + mChannelList.indexOf(item));
            Log.d(TAG, "removeChannel: " + this.mChannelList.get(0).toString() + " item " + item.toString());
            dataBaseManager.removeChannel(item);
            this.mChannelList.remove(item);
            sentMsgToUiAndVoice();
        }
    }

    /**
     * 向UI线程和Voice发送消息
     */
    private void sentMsgToUiAndVoice() {
        if (allMsgName.equals("自定义")) {
            this.mFragment.setChannelList(mChannelList);
        } else {
            if (Constant.user != null && Constant.user.isVIP()) {
                this.mFragment.setList(mVipList);
                voice.setList(mVipList);
            } else {
                this.mFragment.setList(mNotVipList);
                voice.setList(mNotVipList);
            }
        }

    }

    /**
     * 刷新所有公共频道的信息（防止语音和显示不同步）
     */
    public static void refreshPublicMsg() {
        for (AllMessage message : msgManager.values()) {
            if (message.getName().equals("自定义")) {
                message.sentMsgToUiAndVoice();
            }
        }
    }

    /**
     * 获得管理消息列表的map
     */
    public static HashMap<String, AllMessage> getAllMsgManager() {
        return (HashMap<String, AllMessage>) msgManager;
    }

}
