package com.weiwu.ql.main.message;

import android.content.Context;

import com.weiwu.ql.greendao.db.DaoSession;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/26 17:12 
 */
public class DBMaster {
    public static void init(Context context) {
        DBCore.init(context);
        initDatabase(context);
    }

    private static void initDatabase(Context context) {
        DBCore.enableQueryBuilderLog();
    }

    /**
     * 得到正常的不加密的
     */
    public static DaoSession getDBNormal() {
        return DBCore.getDaoSessionNormal();
    }

    /**
     * 得到加密的
     *
     * @return
     */
    public static DaoSession getDBEncryption() {
        return DBCore.getDoaSessionEncryption();
    }
}
