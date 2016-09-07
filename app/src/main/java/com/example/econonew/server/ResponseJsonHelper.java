package com.example.econonew.server;

import com.example.econonew.entity.MsgItemEntity;
import com.example.econonew.main.object.AllMessage;
import com.example.econonew.resource.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * ResponseJsonHelper 此类对中间服务器返回的结果解析并存储在本地的SQLite数据库
 *
 * @author agnes
 *
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
	public void initMsgItemList(JSONObject json) {
		HashMap<String, AllMessage> manager = AllMessage.getAllMsgManager();
		for (int i = 0; i < Constant.publicItemNames.length; i++) {
			AllMessage allMessage = manager.get(Constant.publicItemNames[i]);
			try {
				ArrayList<MsgItemEntity> msgList = new ArrayList<>();
				JSONArray array = json.getJSONArray(allMessage.getName());
				for (int j = 0; j < array.length(); j++) {
					JSONObject object = (JSONObject) array.getJSONObject(j);
					MsgItemEntity mi = getEntityFromJson(object);
					msgList.add(mi);
				}
				allMessage.setMsg(msgList, false, true);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * 从JsonObject里面获取Msg实例
	 * @param object	JsonObject
	 * @return
	 * @throws JSONException
	 */
	private MsgItemEntity getEntityFromJson(JSONObject object) throws JSONException {
		String msgItemTitle = object.getString("title");
		String msgItemContent = object.getString("context");
		int msgItemId = object.getInt("id");
		String msgItemContentUrl = Constant.URL + "/" + Constant.OPERATION_MSG + "?id=" + msgItemId;
		String msgItemGeneral = getMsgGeneralByContent(msgItemContent);
		String msgItemImageUrl = object.getString("picture");
		int level = object.getInt("level");
		MsgItemEntity entity = new MsgItemEntity(msgItemTitle, msgItemContent, msgItemContentUrl, msgItemGeneral, msgItemImageUrl);
		if(level == 1) {
			entity.setVip(true);
		}
		return entity;
	}

	public String getMsgGeneralByContent(String msgContent) {
		return null;
	}

	public void handleChannel(JSONObject json) {

	}
}
