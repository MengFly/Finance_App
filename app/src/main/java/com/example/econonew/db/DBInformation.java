package com.example.econonew.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.econonew.resource.Constant;

/**
 * 修改了类名 将建表语句拆分分在了两个方法里面 createPublicTable 创建公共信息表 createSelfTable
 * 创建自定义频道需要的数据表
 * <p/>
 * 另外修改了自定义信息表的内容： 增加了用户名这一列 用于根据用户命来加载数据
 */
public class DBInformation extends SQLiteOpenHelper {

    // 数据库的名称
    private static final String DB_NAME = "db_information";

    public DBInformation(Context context) {
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
                    + MsgTable.getTableName(messageName) + "("
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
                    + "type varchar(10) DEFAULT NONE,"
                    + "attribute varchar(10) DEFAULT NONE,"
                    + "code text DEFAULT NONE,"
                    + "channel_id integer"
                    + ")";
            db.execSQL(sqlStr);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
