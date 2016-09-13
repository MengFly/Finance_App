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

	private static final String TAG = "ChannelJsonHelper";
	
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
		JSONObject channelObj = getJsonObject(jsonString);
		JSONArray channelArray = getJsonArray(channelObj, "result");
		if(channelArray == null) {
			return channels;
		}
		for (int i = 0; i < channelArray.length(); i++) {
			JSONObject obj = getJsonObject(channelArray, i);
			ChannelEntity entity = getChannel(obj);
			channels.add(entity);
		}
		return channels;
	}

	private ChannelEntity getChannel(JSONObject obj) {
		JSONObject businessTypeObj = getJSONObject(obj, "businessType");
		JSONObject businessDomainObj = getJSONObject(obj, "businessDomain");
		JSONObject stairObj = getJSONObject(obj, "stair");
		String channelName = getString(businessTypeObj, "name");;
		String channelType =  getString(businessDomainObj, "name");
		String channelAttribute = getString(stairObj, "name");
		int channelId = getInt(obj, "id");
		int businessTypeId = getInt(businessTypeObj, "id");
		int businessDomainId = getInt(businessDomainObj, "id");
		int stairId = getInt(stairObj, "id");
		String code = getString(obj, "stock");
		Log.d(TAG, "getChannel: " + "channelId " + channelId + " typeId " + businessTypeId + "domainId " + businessDomainId + " stairId " + stairId);
		
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
