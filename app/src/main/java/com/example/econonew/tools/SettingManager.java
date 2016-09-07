package com.example.econonew.tools;

import com.example.econonew.main.object.AllMessage;

import java.util.Map;


public class SettingManager {

    private static SettingManager mInstance;

    private SettingManager() {
    }

    public boolean isInitDataFinsh() {
        return AllMessage.getAllMsgManager().size() != 0;
    }

    public boolean isLoadedDatas() {
        boolean isLoadedDatas = false;
        for (Map.Entry<String, AllMessage> entity : AllMessage
                .getAllMsgManager().entrySet()) {
            if (!"自定义".equals(entity.getValue().getName())
                    && entity.getValue().getMsgList().size() > 0) {
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
