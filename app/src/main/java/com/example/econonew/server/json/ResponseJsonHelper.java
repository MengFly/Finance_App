package com.example.econonew.server.json;

import android.util.Log;

import com.example.econonew.entity.MsgItemEntity;
import com.example.econonew.resource.Constant;
import com.example.econonew.resource.msg.MainMessage;
import com.example.econonew.server.URLManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * ResponseJsonHelper 此类对中间服务器返回的结果解析并存储在本地的SQLite数据库
 *
 * @author agnes
 */
public class ResponseJsonHelper {


    public void handleInformation(JSONObject json) {
        handleInformation(json, false);
    }

    /**
     * 解析Json字符串，并显示在主界面上面
     * @param json 要解析的Json对象
     * @param isAddEnd  解析后的结果是否添加到当前结果的后面， 如果为false的话，那么就会清除之前的结果，只保留这一次的结果
     */
    public void handleInformation(JSONObject json, boolean isAddEnd) {
        if (json == null) {
            return;
        }
        initMsgItemList(json, isAddEnd);

    }

    /**
     * 初始化各栏目中的数据并进行数据库缓存
     */
    private void initMsgItemList(JSONObject json, boolean isAddEnd) {
//        Log.e("json", json.toString());
        JSONObject obj = JsonCast.getJSONObject(json, "info");
//        Log.e("isTrue", "initMsgItemList: " + String.valueOf(obj == null));
        if (obj != null) {
            MsgItemEntity entity = getEntityFromJson(obj);
            Log.e("isTrue", "initMsgItemList: " + String.valueOf(entity == null));
            MainMessage msgManager = MainMessage.getInstance(entity.getMsgType());
            if (msgManager != null) {
                List<MsgItemEntity>  list = new ArrayList<>(1);
                list.add(entity);
                msgManager.setMessage(list, true, false);
            }
            
        } else {
            for (String itemName : Constant.publicItemNames) {
                MainMessage msgManager = MainMessage.getInstance(itemName);
                ArrayList<MsgItemEntity> msgList = new ArrayList<>();
                JSONArray array = JsonCast.getJsonArray(json, itemName);
                if (array != null) {
                    for (int j = 0; j < array.length(); j++) {
                        JSONObject object = JsonCast.getJsonObject(array, j);
                        MsgItemEntity mi = getEntityFromJson(object);
                        msgList.add(mi);
                    }
                    if (msgManager != null) {
                        msgManager.setMessage(msgList, isAddEnd, true);
                    }
                }
            }
        }
    }


    /**
     * 从JsonObject里面获取Msg实例
     *
     * @param object JsonObject
     */
    private MsgItemEntity getEntityFromJson(JSONObject object) {
        String msgItemTitle = JsonCast.getString(object, "title");
        String context = JsonCast.getString(object, "context");
//        int msgItemId = JsonCast.getInt(object, "id");
//        String msgItemContentUrl = URLManager.getMsgContentURL(msgItemId);
        String msgItemContentUrl = URLManager.getMsgContentUrl(context);
        String msgItemImageUrl = JsonCast.getString(object, "picture");
        int level = JsonCast.getInt(object, "level");
        int businessDomainId = JsonCast.getInt(JsonCast.getJSONObject(object, "businessDomain"), "id");
        int businessTypeId = JsonCast.getInt(JsonCast.getJSONObject(object, "businessType"), "id");
        int stairId = JsonCast.getInt(JsonCast.getJSONObject(object, "stair"), "id");
        String msgType = JsonCast.getString(JsonCast.getJSONObject(object, "businessType"), "name");
        MsgItemEntity entity = new MsgItemEntity(msgItemTitle, context, msgItemContentUrl, null, msgItemImageUrl);
        entity.setBusinessDomainId(businessDomainId);
        entity.setBusinessTypeId(businessTypeId);
        entity.setStairId(stairId);
        entity.setMsgType(msgType);
        entity.setVip(level == 1);
        return entity;
    }

    //保存从采集器哪里获取到的信息
    public void saveBackMsg(JSONObject object, String[] childTypes) {
        for (String msgType : childTypes) {
            saveChildMessage(object, msgType);
        }
    }

    private void saveChildMessage(JSONObject parentObj, String childType) {
        JSONObject childMsgObj = JsonCast.getJSONObject(parentObj, childType);
        if (childMsgObj != null) {
            MsgItemEntity entity = getEntityFromJson(childMsgObj);
            MainMessage message = MainMessage.getInstance(entity.getMsgType());
            if (message != null) {
                message.setMessage(Arrays.asList(entity), true, true);
            }
        }
    }
}
