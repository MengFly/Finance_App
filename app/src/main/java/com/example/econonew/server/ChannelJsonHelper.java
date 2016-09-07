package com.example.econonew.server;


import android.content.Context;
import android.util.Log;

import com.example.econonew.entity.ChannelEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import static com.example.econonew.server.JsonCast.*;


/**
 * 
 * @author mengfei
 *
 */
public class ChannelJsonHelper extends JsonHelperImpl<ChannelEntity> {

	public ChannelJsonHelper(Context context) {
		super(context);
	}

	@Override
	public ChannelEntity getItemFromJson(String jsonString) {

		return null;
	}

	@Override
	public List<ChannelEntity> getItemAllFromJson(String jsonString) {
		List<ChannelEntity> channels = new ArrayList<>();
		Log.v("json", jsonString);
		JSONObject channelObj = getJsonObject(jsonString);
		JSONArray channelArray = getJsonArray(channelObj, "result");
		for (int i = 0; i < channelArray.length(); i++) {
			JSONObject obj = getJsonObject(channelArray, i);
			ChannelEntity entity = getChannel(obj);
			channels.add(entity);
		}
		return channels;
	}

	/** ��JsonObject�����ȡChannel */
	private ChannelEntity getChannel(JSONObject obj) {
		JSONObject businessTypeObj = getJSONObject(obj, "businessType");
		JSONObject businessDomainObj = getJSONObject(obj, "businessDomain");
		JSONObject stairObj = getJSONObject(obj, "stair");
		String channelName = null;
		String channelType = null;
		String channelAttribute = null;
		if (businessTypeObj != null) {
			channelName = getString(businessTypeObj, "name");
		}
		if (businessDomainObj != null) {
			channelType = getString(businessDomainObj, "name");
		}
		if (stairObj != null) {
			channelAttribute = getString(stairObj, "name");
		}
		String code = getString(obj, "stock");
		int id = getInt(obj, "id");
		Log.v("testUtils", id + "");
		ChannelEntity entity = new ChannelEntity();
		entity.setName(channelName);
		entity.setType(channelType);
		entity.setAttribute(channelAttribute);
		entity.setCode(code);
		entity.setId(id);
		return entity;
	}

}
