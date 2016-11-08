package com.example.econonew.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.econonew.entity.MsgItemEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主界面的消息数据表
 * ================================================
 * _id 数据库Id
 * context 排版网页的url
 * contentUrl 排版网页的url
 * businessDomain_id
 * businessType_id
 * stair_id integer
 * is_love 是否是收藏消息
 * id  频道id"
 * is_vip 是否是VIP消息
 * image_title 标题图片的URL
 * title 标题
 =========================================================
 * Created by mengfei on 2016/11/1.
 */

public class MsgTable extends BaseTable<MsgItemEntity> {

    private static final Map<String, String> publicDataTableNames = new HashMap<>();
    static {
        publicDataTableNames.put("股票", "stock_info");
        publicDataTableNames.put("理财", "money_info");
        publicDataTableNames.put("基金", "funds_info");
        publicDataTableNames.put("期货", "futures_info");
        publicDataTableNames.put("外汇", "exchange_info");
    }

    private String tableName;

    public MsgTable(String tabName) {
        this.tableName = getTableName(tabName);
    }

    /**
     * 根据消息的名称获取公共消息有关的数据库
     *
     * @param messageName 消息名称
     * @return 数据库名称
     */
    public static String getTableName(String messageName) {
        return publicDataTableNames.get(messageName);
    }

    @Override
    List<MsgItemEntity> queryForItems(String selection, String[] selectionArgs) {
        SQLiteDatabase db = db_information.getReadableDatabase();
        Cursor query = db.query(tableName, null, selection, selectionArgs,null, null, null);
        ArrayList<MsgItemEntity> msgList = new ArrayList<>();
        if(query.getCount() > 0) {
            query.moveToFirst();
            do {
                String msgTitle = query.getString(query.getColumnIndex("title"));
                String titleImageUrl = query.getString(query.getColumnIndex("image_title"));
                String contentUrl = query.getString(query.getColumnIndex("contentUrl"));
                String isLoveStr = query.getString(query.getColumnIndex("is_love"));
                String isVipStr = query.getString(query.getColumnIndex("is_vip"));
                boolean isLove = Boolean.parseBoolean(isLoveStr) || isLoveStr.equals("1");
                boolean isVip = Boolean.parseBoolean(isVipStr) || isVipStr.equals("1");
                Log.d("db", "queryForItems: " + msgTitle + " is_love " + isLove);
                Log.d("db", "queryForItems: " + msgTitle + " is_Vip " + isVip);
                MsgItemEntity entity = new MsgItemEntity(msgTitle, null, contentUrl, null, titleImageUrl);
                entity.setLove(isLove);
                entity.setVip(isVip);
                msgList.add(entity);
            } while (query.moveToNext());
        }
        query.close();
        db.close();
        return msgList;
    }

    @Override
    void insertItem(MsgItemEntity msgItemEntity) {
        super.insertItem(msgItemEntity);
    }

    @Override
    void insertItems(List<MsgItemEntity> list) {
        ArrayList<ContentValues> valueses = new ArrayList<>();
        for (MsgItemEntity entity : list) {
            if (!dataHasSave(tableName, entity)) {
                ContentValues values = new ContentValues();
                values.put("context", entity.getMsgContent() == null ? "" : entity.getMsgContent());
                values.put("title", entity.getMsgTitle() == null ? "" : entity.getMsgTitle());
                values.put("image_title", entity.getImageTitleUrl() == null ? "" : entity.getImageTitleUrl());
                values.put("is_love", entity.isLove());
                values.put("contentUrl", entity.getMsgContentUrl());
                values.put("is_vip", entity.isVip());
                values.put("businessDomain_id", entity.getBusinessDomainId());
                values.put("businessType_id", entity.getBusinessTypeId());
                values.put("stair_id", entity.getStairId());
                valueses.add(values);
            }
        }
        SQLiteDatabase db = db_information.getWritableDatabase();
        db.beginTransaction();
        for (ContentValues contentValues : valueses) {
            db.insert(tableName, null, contentValues);
            Log.d("db", "insertItems: success");
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    /**
     * 判断数据是否已经存储过
     *
     * @param tableName 要查询的数据表的名字
     * @param entity    要查询的消息
     * @return 是否已经添加过这条消息
     */
    private boolean dataHasSave(String tableName, MsgItemEntity entity) {
        boolean isHadSave = false;
        SQLiteDatabase db = db_information.getReadableDatabase();
        String selection = "contentUrl=?";
        String[] selectionArgs = new String[]{entity.getMsgContentUrl()};
        Cursor cursor = db.query(tableName, null, selection, selectionArgs, null, null, null, null);
        if(cursor.getCount() != 0) {//如果结果不为0,那么说明已经存储过这个信息了
            isHadSave = true;
        }
        cursor.close();
        db.close();
        return isHadSave;
    }

    @Override
    void updateItem(MsgItemEntity msgItemEntity, String whereClause, String[] args) {
        SQLiteDatabase db = db_information.getWritableDatabase();
        ContentValues values = getItem(msgItemEntity);
        db.update(tableName, values, whereClause, args);
        db.close();
    }

    private ContentValues getItem(MsgItemEntity msgItemEntity) {
        ContentValues values = new ContentValues();
        values.put("is_love", String.valueOf(msgItemEntity.isLove()));
        return values;
    }

    @Override
    void deleteItem(MsgItemEntity msgItemEntity) {
        super.deleteItem(msgItemEntity);
    }

    @Override
    void deleteAll() {
        super.deleteAll();
    }
}