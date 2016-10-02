package com.example.econonew.tools;

import com.example.econonew.resource.Constant;
import com.example.econonew.resource.msg.MainMessage;


public class SettingManager {

    private static SettingManager mInstance;

    private SettingManager() {
    }

    public boolean isInitDataFinish() {
        // TODO: 2016/10/2  等会实现这个方法，实现判断Fragment是否已经加载 
        return true;
    }

    public boolean isLoadedDatas() {
        boolean isLoadedDatas = false;
        for (String tabName : Constant.publicItemNames) {
            MainMessage message = MainMessage.getInstance(tabName);
            if(message != null && message.getMsgCount() > 0) {
                isLoadedDatas = true;
                break;
            }
        }
        return isLoadedDatas;
    }

    public static SettingManager getInstance() {
        if (mInstance == null) {
            synchronized (SettingManager.class) {
                if (mInstance == null) {
                    mInstance = new SettingManager();
                }
            }
        }
        return mInstance;
    }
}
