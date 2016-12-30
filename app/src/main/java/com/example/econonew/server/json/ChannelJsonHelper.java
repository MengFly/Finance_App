package com.example.econonew.server.json;


import android.content.Context;

import com.example.econonew.entity.ChannelEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author mengfei
 *
 */
public class ChannelJsonHelper extends JsonHelperImpl<ChannelEntity> {

	//private static final String TAG = "ChannelJsonHelper";
	
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
		JSONObject channelObj = JsonCast.getJsonObject(jsonString);
		JSONArray channelArray = JsonCast.getJsonArray(channelObj, "result");
		if(channelArray == null) {
			return channels;
		}
		for (int i = 0; i < channelArray.length(); i++) {
			JSONObject obj = JsonCast.getJsonObject(channelArray, i);
			ChannelEntity entity = getChannel(obj);
			channels.add(entity);
		}
		return channels;
	}

	private ChannelEntity getChannel(JSONObject obj) {
		JSONObject businessTypeObj = JsonCast.getJSONObject(obj, "businessType");
		JSONObject businessDomainObj = JsonCast.getJSONObject(obj, "businessDomain");
		JSONObject stairObj = JsonCast.getJSONObject(obj, "stair");
		String channelName = JsonCast.getString(businessTypeObj, "name");
		String channelType =  JsonCast.getString(businessDomainObj, "name");
		String channelAttribute = JsonCast.getString(stairObj, "name");
		int channelId = JsonCast.getInt(obj, "id");
		int businessTypeId = JsonCast.getInt(businessTypeObj, "id");
		int businessDomainId = JsonCast.getInt(businessDomainObj, "id");
		int stairId = JsonCast.getInt(stairObj, "id");
		String code = JsonCast.getString(obj, "stock");
		//Log.d(TAG, "getChannel: " + "channelId " + channelId + " typeId " + businessTypeId + "domainId " + businessDomainId + " stairId " + stairId);
		
		ChannelEntity entity = new ChannelEntity();
		entity.setBusinessTypeId(businessTypeId);
		entity.setBusinessDomainId(businessDomainId);
		entity.setStairId(stairId);
		entity.setName(channelName);
		entity.setType(channelType);
		entity.setAttribute(channelAttribute);
		entity.setCode(code);
		entity.setId(channelId);
		return entity;
	}

}
