package com.example.econonew.resource.msg;

import java.util.List;

/**
 * 所有消息管理类的接口，定义了消息管理类的一些接口以及规范
 * Created by mengfei on 2016/10/2.
 */

 public interface IMessage<T> {

    /**
     * 为消息管理类设置消息列表
     * @param msgList   消息列表的内容
     * @param isAddEnd 是否添加到现有列表的后面
     * @param isSaveToDatabase  是否向数据库存储
     */
    void setMessage(List<T> msgList, boolean isAddEnd, boolean isSaveToDatabase);

    /**
     * 获取到消息管理类的消息列表
     * @return  消息列表
     */
    List<T> getMessage();

    /**获取消息的数量*/
    int getMsgCount();

    /**
     * 移除消息列表中的项
     * @param item 要移除的项目
     * @return  被移除的项
     */
    boolean removeMsg(T item);
}
