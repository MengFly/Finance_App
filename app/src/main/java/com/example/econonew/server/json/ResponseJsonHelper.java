package com.example.econonew.server.json;

import com.example.econonew.entity.MsgItemEntity;
import com.example.econonew.resource.Constant;
import com.example.econonew.resource.msg.MainMessage;
import com.example.econonew.server.URLManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * ResponseJsonHelper 此类对中间服务器返回的结果解析并存储在本地的SQLite数据库
 *
 * @author agnes
 */
public class ResponseJsonHelper {


    public void handleInfomation(JSONObject json) {
        if (json == null) {
            return;
        }
        initMsgItemList(json);
    }

    /**
     * 初始化各栏目中的数据并进行数据库缓存
     */
    private void initMsgItemList(JSONObject json) {
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
                    msgManager.setMessage(msgList, false, true);
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
        MsgItemEntity entity = new MsgItemEntity(msgItemTitle, context, msgItemContentUrl, null, msgItemImageUrl);
        entity.setBusinessDomainId(businessDomainId);
        entity.setBusinessTypeId(businessTypeId);
        entity.setStairId(stairId);
        entity.setVip(level == 1);
        return entity;
    }
}
