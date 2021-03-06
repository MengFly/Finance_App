package com.example.econonew.entity;

import com.example.econonew.db.Saveable;
import com.example.econonew.server.URLManager;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;


/**
 * 这个类是用来管理从服务器哪里接收到的消息 将从服务器哪里接收到的消息内容存储在这个类中
 *
 * @author mengfei
 */
public class MsgItemEntity extends DataSupport implements Serializable, Saveable, Comparable<MsgItemEntity> {

    private static final long serialVersionUID = 1L;

    public static final String ADDRESS_SERVER = "server";//中间服务器
    public static final String ADDRESS_SERVER_BACK = "server_back";//采集服务器

    private String msgType;
    //消息的时间信息
    public int msgVerifierTime;

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public void setMsgVerifierTime(int msgVerifierTime) {
        this.msgVerifierTime = msgVerifierTime;
    }

    @Column(unique = true)
    // 存储消息标题
    private String msgTitle;

    // 存储消息内容
    private String msgContent;


    @Column(unique = true)
    private String msgContentUrl;

    // 存储消息摘要内容，主要用于朗读使用
    private String msgGeneral;

    // 存储消息的图片，即在ListView里面显示的图片
    private String imageTitleUrl;

    private boolean isLove;// 是否收藏

    private int businessDomainId;

    private int businessTypeId;

    private int stairId;

    public void setBusinessDomainId(int businessDomainId) {
        this.businessDomainId = businessDomainId;
    }

    public void setBusinessTypeId(int businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public void setStairId(int stairId) {
        this.stairId = stairId;
    }

    public int getBusinessDomainId() {
        return businessDomainId;
    }

    public int getBusinessTypeId() {
        return businessTypeId;
    }

    public int getStairId() {
        return stairId;
    }


    /**
     * @param msgTitle      消息标题
     * @param msgContent    消息内容-主要是用于展示在webView里面
     * @param msgGeneral    消息摘要--主要是用在朗读部分
     * @param msgContentUrl 消息内容目标网页的url
     * @param imageTitleUrl 消息列表的图片的Url地址
     */
    public MsgItemEntity(String msgTitle, String msgContent, String msgContentUrl, String msgGeneral,
                         String imageTitleUrl) {
        this.msgTitle = msgTitle;
        this.msgContent = msgContent;
        this.msgContentUrl = msgContentUrl;
        this.msgGeneral = msgGeneral;
        this.imageTitleUrl = imageTitleUrl;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public String getImageTitleUrl() {
        return URLManager.getCaiBianURL() + imageTitleUrl;
    }

    public String getMsgGeneral() {
        return msgGeneral;
    }

    public String getMsgContentUrl() {
        return msgContentUrl;
    }

    public boolean isLove() {
        return isLove;
    }

    public void setLove(boolean isLove) {
        this.isLove = isLove;
    }

    private boolean isVip = false; // 是否是VIP信息

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean isVip) {
        this.isVip = isVip;
    }


    /**
     * 判断此消息是否是这个频道的定制消息
     *
     * @param entity 要判断的频道
     * @return 返回这条消息是否是这个频道的定制消息
     */
    public boolean isDingZhi(ChannelEntity entity) {
        return this.getBusinessDomainId() == entity.getBusinessDomainId() &&
                this.getBusinessTypeId() == entity.getBusinessTypeId() &&
                this.getStairId() == entity.getStairId();
    }

    public int compareTo(MsgItemEntity o) {              //利用时间戳实现对象间的比较
        if (msgVerifierTime > o.msgVerifierTime) {
            return 1;
        } else if (msgVerifierTime < o.msgVerifierTime) {
            return -1;
        }
        return 0;
    }

    public String toString() {
        return String.valueOf(msgVerifierTime);
    }

    @Override
    public boolean saveSelf() {
        return save();
    }

}
