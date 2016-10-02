package com.example.econonew.view.fragment;

import android.support.v4.app.Fragment;

import com.example.econonew.resource.msg.IMessage;

import java.util.List;

/**
 * 用于显示消息的Fragment的基类
 * Created by mengfei on 2016/10/2.
 */

public abstract class MsgBaseFragment<T extends IMessage, V> extends Fragment {

    protected T msgManager;

    /**
     * 为Fragment设置MsgManager;
     */
    protected void bindMsgManager(T msgManager) {
        this.msgManager = msgManager;
    }

    public abstract void setList(List<V> list);


}
