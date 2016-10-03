package com.example.econonew.tools;

import com.example.econonew.resource.Constant;
import com.example.econonew.resource.msg.MainMessage;


public class SettingManager {

    private static SettingManager mInstance;

    private SettingManager() {
    }

    public boolean isInitDataFinish() {
        for (String tabName : Constant.publicItemNames) {
            if (MainMessage.getInstance(tabName) != null) {
                return true;
            }
        }
        return false;
    }

    public boolean isLoadedDatas() {
        for (String tabName : Constant.publicItemNames) {
            MainMessage message = MainMessage.getInstance(tabName);
            if (message != null && message.getMsgCount() > 0) {
               return true;
            }
        }
        return false;
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
