package com.example.econonew.db;

/**
 * 数据库实现类的工具类
 * Created by mengfei on 2017/1/12.
 */
public class DBHelperFactory {

    /**
     * 获取到数据库管理的实体类
     * @return 数据库管理的实体类
     */
    public static DataBaseManager getDBHelper() {
        return new DataBaseHelperLitePal();
    }
}
