package com.example.econonew.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.econonew.entity.ChannelEntity;
import com.example.econonew.resource.Constant;

import java.util.ArrayList;
import java.util.List;

import static com.example.econonew.resource.Constant.user;

/**
 * 频道列表的管理类
 * 表的信息
 * ============================
 * _id(id)
 * businessDomain_id(频道id)
 * businessType_id(频道一级标签id)
 * stair_id(频道二级标签id)
 * user_name(用户名)
 * name(频道名称)
 * type(一级标签)
 * attribute(二级标签)
 * code(交易代码)
 * channel_id(频道Id)
 * ===========================
 * Created by mengfei on 2016/11/1.
 */

public class ChannelTable extends BaseTable<ChannelEntity> {


    @Override
    public List<ChannelEntity> queryForItems(String selection, String[] selectionArgs) {
        List<ChannelEntity> channels = new ArrayList<>();
        if (user == null) {
            return channels;
        }
        SQLiteDatabase db = db_information.getReadableDatabase();
        for (String channelTableName : Constant.selfDataTableNames) {
            // 根据用户的名称来获得用户指定的信息
            Cursor cursor = db.query(channelTableName, null, selection, selectionArgs, null, null, null);
            int count = cursor.getCount();
            if (count > 0) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    String channelName = cursor.getString(cursor.getColumnIndex("name"));
                    String channelType = cursor.getString(cursor.getColumnIndex("type"));
                    String channelArrtibute = cursor.getString(cursor.getColumnIndex("attribute"));
                    String code = cursor.getString(cursor.getColumnIndex("code"));
                    int channelId = cursor.getInt(cursor.getColumnIndex("channel_id"));
                    int businessDomainId = cursor.getInt(cursor.getColumnIndex("businessDomain_id"));
                    int businessTypeId = cursor.getInt(cursor.getColumnIndex("businessType_id"));
                    int stairId = cursor.getInt(cursor.getColumnIndex("stair_id"));
                    ChannelEntity entity = new ChannelEntity();
                    entity.setName(channelName);
                    entity.setType(channelType);
                    entity.setAttribute(channelArrtibute);
                    entity.setCode(code);
                    entity.setId(channelId);
                    entity.setBusinessDomainId(businessDomainId);
                    entity.setBusinessTypeId(businessTypeId);
                    entity.setStairId(stairId);
                    channels.add(entity);
                }
            }
            cursor.close();
        }
        db.close();
        return channels;
    }

    @Override
    public void insertItem(ChannelEntity channelEntity) {

    }

    @Override
    public void insertItems(List<ChannelEntity> list) {
        if (Constant.user != null) {
            List<ContentValues> valueses = new ArrayList<>();
            List<String> tableNames = new ArrayList<>();
            for (ChannelEntity entity : list) {
                ContentValues values = new ContentValues();

                values.put("user_name", Constant.user.getName());
                values.put("name", entity.getName());
                values.put("type", entity.getType());
                values.put("attribute", entity.getAttribute());
                values.put("code", entity.getCode());
                values.put("channel_id", entity.getId());
                values.put("businessDomain_id", entity.getBusinessDomainId());
                values.put("businessType_id", entity.getBusinessTypeId());
                values.put("stair_id", entity.getStairId());
                valueses.add(values);
                tableNames.add(Constant.getSelfTableName(entity.getName()));
            }
            SQLiteDatabase db = db_information.getWritableDatabase();
            db.beginTransaction();
            for (int i = 0; i < valueses.size(); i++) {
                String tableName = tableNames.get(i);
                if (tableName == null) {
                    tableName = "stock_channel";
                }
                db.insert(tableName, null, valueses.get(i));
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }

    }


    @Override
    public void deleteItem(ChannelEntity channelEntity) {
        SQLiteDatabase writableDatabase = db_information.getWritableDatabase();
        writableDatabase.delete(Constant.getSelfTableName(channelEntity.getName()),
                "channel_id=? and type=?",
                new String[]{String.valueOf(channelEntity.getId()), channelEntity.getType()});
        writableDatabase.close();
    }

    @Override
    void deleteAll() {
        SQLiteDatabase writableDatabase = db_information.getWritableDatabase();
        writableDatabase.beginTransaction();
        for (String tableName : Constant.selfDataTableNames) {
            writableDatabase.delete(tableName, "user_name=?",
                    new String[]{Constant.user.getName()});
        }
        writableDatabase.setTransactionSuccessful();
        writableDatabase.endTransaction();
        writableDatabase.close();
    }
}
