package com.example.econonew.tools;

import com.example.econonew.resource.Constant;
import com.example.econonew.resource.msg.MainMessage;


public class SettingManager {

    /**
     * 判断主界面的数据是否已经加载过了
     */
    public static boolean isLoadedDatas() {
        for (String tabName : Constant.publicItemNames) {
            MainMessage message = MainMessage.getInstance(tabName);
            if (message != null && message.getMsgCount() > 0) {
               return true;
            }
        }
        return false;
    }
}
