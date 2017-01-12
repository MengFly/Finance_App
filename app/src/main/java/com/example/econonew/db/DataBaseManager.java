package com.example.econonew.db;


import android.content.ContentValues;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * 数据库业务逻辑的基础接口，所有支持数据库的查询逻辑都定义在这个接口里面
 * Created by mengfei on 2017/1/12.
 */
public interface DataBaseManager {

    /**
     * 查询所有的Item
     * @param dbEntityClass 有关数据库的实体类
     */
    <T> List<T> queryAllItems(Class<T> dbEntityClass);

    /**
     * 带有条件的查询数据库信息
     */
    <T> List<T> queryItemsByArgs(Class<T> dbEntityClass, String... whereConditions);

    /**
     * 待条件的查询，支持条件查询，排序，分页
     * @param sortStr 排序的语句
     */
    <T> List<T> queryItems(Class<T> dbEntityClass, int limit, String sortStr, @Nullable String... whereConditions);

    /**
     * 查询通过id
     * @param id id
     */
    <T> T findItemById(Class<T> dbEntityClass , int id);

    <T> void updateItemById(Class<T> dbEntityClass, ContentValues updateValues, int id);

    <T extends Saveable> void insertItems(Class<T> dbEntityClass, T item);
    <T extends Saveable> void insertAllItems(Class<T> dbEntityClass,List<T> items);

    <T> void deleteItemById(Class<T> dbEntityClass, int id);
    <T> void deleteAll(Class<T> dbEntityClass);

}
