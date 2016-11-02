package com.example.econonew.server.json;

import com.example.econonew.entity.MsgItemEntity;
import com.example.econonew.resource.Constant;
import com.example.econonew.resource.msg.MainMessage;
import com.example.econonew.tools.URLManager;

import org.json.JSONArray;
import org.json.JSONException;
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
     *
     * @param json
     */
    private void initMsgItemList(JSONObject json) {
        for (String itemName : Constant.publicItemNames) {
            MainMessage msgManager = MainMessage.getInstance(itemName);
            try {
                ArrayList<MsgItemEntity> msgList = new ArrayList<>();
                JSONArray array = json.getJSONArray(itemName);
                for (int j = 0; j < array.length(); j++) {
                    JSONObject object = array.getJSONObject(j);
                    MsgItemEntity mi = getEntityFromJson(object);
                    msgList.add(mi);
                }
                if (msgManager != null) {
                    msgManager.setMessage(msgList, false, true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 从JsonObject里面获取Msg实例
     *
     * @param object JsonObject
     * @throws JSONException
     */
    private MsgItemEntity getEntityFromJson(JSONObject object) throws JSONException {
        String msgItemTitle = object.getString("title");
        String msgItemContent = object.getString("context");
        int msgItemId = object.getInt("id");
        String msgItemContentUrl = URLManager.getMsgContentURL(msgItemId);
        String msgItemGeneral = getMsgGeneralByContent(msgItemContent);
        String msgItemImageUrl = object.getString("picture");
        int level = object.getInt("level");
        int businessDomainId = JsonCast.getInt(JsonCast.getJSONObject(object, "businessDomain"), "id");
        int businessTypeId = JsonCast.getInt(JsonCast.getJSONObject(object, "businessType"), "id");
        int stairId = JsonCast.getInt(JsonCast.getJSONObject(object, "stair"), "id");
        MsgItemEntity entity = new MsgItemEntity(msgItemTitle, msgItemContent, msgItemContentUrl, msgItemGeneral, msgItemImageUrl);
        entity.setBusinessDomainId(businessDomainId);
        entity.setBusinessTypeId(businessTypeId);
        entity.setStairId(stairId);
        entity.setVip(level == 1);
        return entity;
    }

    // TODO: 2016/11/2 这里要添加获取摘要信息的逻辑 
    private String getMsgGeneralByContent(String msgContent) {
        return null;
    }

}
