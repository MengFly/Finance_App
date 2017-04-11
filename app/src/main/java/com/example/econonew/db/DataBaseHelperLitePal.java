package com.example.econonew.db;

import android.content.ContentValues;
import android.support.annotation.Nullable;

import org.litepal.crud.ClusterQuery;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 使用LitePal框架实现的数据库功能
 * Created by mengfei on 2017/1/12.
 */
public class DataBaseHelperLitePal implements DataBaseManager {
    @Override
    public <T> List<T> queryAllItems(Class<T> dbEntityClass) {
        return queryItems(dbEntityClass, 0, null, new String[0]);
    }

    @Override
    public <T> List<T> queryItemsByArgs(Class<T> dbEntityClass, String... whereConditions) {
        return queryItems(dbEntityClass, 0, null, whereConditions);
    }

    @Override
    public <T> List<T> queryItems(Class<T> dbEntityClass, int limit, String sortStr, @Nullable String... whereConditions) {
        List<T> queryList = null;
        ClusterQuery query = null;
        if (whereConditions != null || whereConditions.length == 1) {
            query = DataSupport.where(whereConditions);
        }
        if(limit != 0 && query != null) {
            query = query.limit(limit);
        }
        if (sortStr != null && query != null) {
            query = query.order(sortStr);
        }
        if (query != null) {
            queryList = query.find(dbEntityClass);
        }else {
            queryList = DataSupport.findAll(dbEntityClass);
        }
        if (queryList == null) {
            queryList = Collections.emptyList();
        }
        return queryList;
    }

    @Override
    public <T> T findItemById(Class<T> dbEntityClass, int id) {
        return DataSupport.find(dbEntityClass, id);
    }

    @Override
    public <T> void updateItemById(Class<T> dbEntityClass, ContentValues updateValues, int id) {
        DataSupport.update(dbEntityClass, updateValues, id);
    }

    @Override
    public <T extends Saveable> void insertItems(Class<T> dbEntityClass, T item) {
        List<T> saveList = new ArrayList<>(1);
        saveList.add(item);
        insertAllItems(dbEntityClass, saveList);
    }

    @Override
    public <T extends Saveable> void insertAllItems(Class<T> dbEntityClass, List<T> items) {
        for (T item : items) {
            item.saveSelf();
        }
    }

    @Override
    public <T> void deleteItemById(Class<T> dbEntityClass, int id) {
        DataSupport.delete(dbEntityClass, id);
    }

    @Override
    public <T> void deleteAll(Class<T> dbEntityClass) {
        DataSupport.deleteAll(dbEntityClass);
    }
}
