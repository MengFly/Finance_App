package com.example.econonew.resource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.econonew.entity.ChannelEntity;
import com.example.econonew.entity.MsgItemEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 修改了类名 将建表语句拆分分在了两个方法里面 createPublicTable 创建公共信息表 createSelfTable
 * 创建自定义频道需要的数据表
 * <p/>
 * 另外修改了自定义信息表的内容： 增加了用户名这一列 用于根据用户命来加载数据
 */
public class DB_Information extends SQLiteOpenHelper {

    // 数据库的名称
    private static final String DB_NAME = "db_information";

    public DB_Information(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createPublicTable(db);
        createSelfTable(db);

        // 时间表
        db.execSQL("CREATE TABLE time("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "weekend varchar(20) DEFAULT 0,"
                + "startTime double(20) DEFAULT 0,"
                + "endTime double(20) DEFAULT 0)");
        // 用户表
        db.execSQL("CREATE TABLE user(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name VARCHAR(20) DEFAULT NONE,password VARCHAR(20) DEFAULT NONE,"
                + "datalogin INT(10) DEFAULT 0," + "header text )");
    }

    // 创建公共信息表 包括--（股票， 理财，基金，期货,外汇）
    private void createPublicTable(SQLiteDatabase db) {

        for (String messageName : Constant.publicItemNames) {
            String sqlStr = "CREATE TABLE "
                    + Constant.getTableName(messageName) + "("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "context TEXT DEFAULT NONE,"
                    + "contentUrl varchar(50) DEFAULT NONE,"
                    + "businessDomain_id integer default 0,"
                    + "businessType_id integer default 0,"
                    + "stair_id integer default 0,"
                    + "is_love boolean," + "id integer," + "is_vip boolean,"
                    + "image_title varchar(50) DEFAULT NONE,"
                    + "title varchar(45) DEFAULT NONE" + ")";
            db.execSQL(sqlStr);
        }
    }

    // 创建自定义频道需要的数据表
    private void createSelfTable(SQLiteDatabase db) {

        for (int i = 0; i < Constant.selfDataTableNames.length; i++) {
            String sqlStr = "CREATE TABLE "
                    + Constant.selfDataTableNames[i]
                    + "("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "businessDomain_id integer default 0,"
                    + "businessType_id integer default 0,"
                    + "stair_id integer default 0,"
                    + "user_name varchar(20) not null,"
                    + "name varchar(10) DEFAULT NONE,"
                    + "type varchar(10) DEFAULT NONE,attribute varchar(10) DEFAULT NONE,"
                    + "code text DEFAULT NONE,"
                    + "channel_id integer"
                    + ")";
            db.execSQL(sqlStr);
        }
    }

    /**
     * 获取收藏的列表， 根据频道的名称
     *
     * @param channelName 频道的名称
     * @return 频道的列表
     */
    public ArrayList<MsgItemEntity> getLoveChannelMsgs(String channelName) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor query = db.query(channelName, null, "is_love=?",
                new String[]{String.valueOf(true)}, null, null, null);
        ArrayList<MsgItemEntity> loveMsgs = new ArrayList<>();
        for (query.moveToFirst(); query.moveToNext(); ) {
            String msgTitle = query.getString(query.getColumnIndex("title"));
            String titleImageUrl = query.getString(query.getColumnIndex("image_title"));
            String contentUrl = query.getString(query.getColumnIndex("contentUrl"));
            MsgItemEntity entity = new MsgItemEntity(msgTitle, null, contentUrl, null, titleImageUrl);
            entity.setLove(true);
            loveMsgs.add(entity);
        }
        query.close();
        db.close();
        return loveMsgs;

    }

    /**
     * 更新收藏列表
     *
     * @param contentUrl 根据列表的URL来更新（因为k'k）
     */
    public void upDateMsgContentIsLove(String contentUrl, String channelName,
                                       boolean isLove) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_love", isLove);
        db.update(channelName, values, "contentUrl=?",
                new String[]{contentUrl});
        db.close();
    }

    /**
     * 删除用户数据
     *
     * @param userName 用户名
     */
    public void removeSelfChannel(String userName) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();
        for (String tableName : Constant.selfDataTableNames) {
            writableDatabase.delete(tableName, "user_name=?",
                    new String[]{userName});
        }
        writableDatabase.setTransactionSuccessful();
        writableDatabase.endTransaction();
        writableDatabase.close();
    }

    /**
     * 判断数据是否已经存储过
     *
     * @param tableName 要查询的数据表的名字
     * @param entity    要查询的消息
     * @return 是否已经添加过这条消息
     */
    public boolean dataHasSave(String tableName, MsgItemEntity entity) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = "contentUrl=?";
        String[] selectionArgs = new String[]{entity.getMsgContentUrl()};
        Cursor cursor = db.query(tableName, null, selection, selectionArgs,
                null, null, null, null);
        boolean isSave = !(cursor.getCount() == 0);
        cursor.close();
        db.close();
        return isSave;
    }

    /**
     * 存储消息
     *
     * @param tableName 频道名称
     */
    public void saveAllMsg(String tableName, List<MsgItemEntity> list) {
        ArrayList<ContentValues> valueses = new ArrayList<>();
        for (MsgItemEntity entity : list) {
            if (!dataHasSave(tableName, entity)) {
                ContentValues values = new ContentValues();
                values.put("context", entity.getMsgContent() == null ? "" : entity.getMsgContent());
                values.put("title", entity.getMsgTitle() == null ? "" : entity.getMsgTitle());
                values.put("image_title", entity.getImageTitleUrl() == null ? "" : entity.getImageTitleUrl());
                values.put("is_love", false);
                values.put("contentUrl", entity.getMsgContentUrl());
                values.put("is_vip", entity.isVip());
                values.put("businessDomain_id", entity.getBusinessDomainId());
                values.put("businessType_id", entity.getBusinessTypeId());
                values.put("stair_id", entity.getStairId());
                valueses.add(values);
            }
        }
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        for (ContentValues contentValues : valueses) {
            db.insert(tableName, null, contentValues);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    /**
     * 从数据库里面获取用户定制的信息
     *
     * @param user 用户
     * @return 用户定制的信息
     */
    public List<ChannelEntity> getChannel(UserInfo user) {
        List<ChannelEntity> channels = new ArrayList<>();
        if(user == null) {
            return channels;
        }
        SQLiteDatabase db = getReadableDatabase();
        for (String channelTableName : Constant.selfDataTableNames) {
            // 根据用户的名称来获得用户指定的信息
            Cursor cursor = db.rawQuery("select * from " + channelTableName
                    + " where user_name=" + user.getName(), null);
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
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 保存所有的频道消息
     *
     * @param list 频道消息列表
     */
    public void saveAllChannel(List<ChannelEntity> list) {
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
            SQLiteDatabase db = getWritableDatabase();
            db.beginTransaction();
            for (int i = 0; i < valueses.size(); i++) {
                Log.v("testUtils", tableNames.get(i));
                db.insert(tableNames.get(i), null, valueses.get(i));
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }

    }

    public void removeChannel(ChannelEntity item) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.delete(Constant.getSelfTableName(item.getName()),
                "channel_id=? and type=?",
                new String[]{String.valueOf(item.getId()), item.getType()});
        writableDatabase.close();
    }

}
