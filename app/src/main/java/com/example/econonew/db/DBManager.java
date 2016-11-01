package com.example.econonew.db;

import java.util.List;

/**
 * 数据库的管理类
 * 提供数据库管理的基本操作函数
 * Created by mengfei on 2016/11/1.
 */

public class DBManager {


    public <T> List<T> getDbItems(BaseTable<T> table,String selection, String[] selectionArgs) {
        return table.queryForItems(selection, selectionArgs);
    }
    public <T> void updateItem(BaseTable<T> table, T item, String whereClause, String[] arg) {
        table.updateItem(item, whereClause, arg);
    }
    public <T> void insertItem(BaseTable<T> table, T item) {
        table.insertItem(item);
    }
    public <T> void insertItems(BaseTable<T> table, List<T> items) {
        table.insertItems(items);
    }
    public <T> void deleteItem(BaseTable<T> table, T item) {
        table.deleteItem(item);
    }


    public <T> void deleteAllItem(BaseTable<T> table) {
        table.deleteAll();
    }


}
