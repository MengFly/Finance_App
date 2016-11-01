package com.example.econonew.db;

import com.example.econonew.resource.DB_Information;
import com.example.econonew.view.activity.FinanceApplication;

import java.util.List;

/**
 * 所有的数据库表的基类
 * Created by mengfei on 2016/11/1.
 */

public abstract class BaseTable<T> {

    protected DB_Information db_information;

    public BaseTable() {
        db_information = new DB_Information(FinanceApplication.getInstance());
    }

    /**
     * 查询多条信息
     *
     * @return
     */
     List<T> queryForItems(String selection, String[] selectionArgs){return null;}

    /**
     * 插入一条信息
     *
     * @param t 要插入的信息
     */
     void insertItem(T t){}

    /**
     * 插入多条信息
     *
     * @param list 要插入的信息
     */
     void insertItems(List<T> list){}

    /**
     * 更新一条信息
     *
     * @param t 要更新的信息
     */
     void updateItem(T t,String whereClause, String[] args){}

    /**
     * 删除单挑信息
     * @param t 要修改的信息
     */
     void deleteItem(T t){}

    void deleteAll() {}
}