package com.weiwu.ql.main.message;

import android.content.Context;

import com.weiwu.ql.AppConstant;
import com.weiwu.ql.greendao.db.DaoMaster;
import com.weiwu.ql.greendao.db.DaoSession;
import com.weiwu.ql.utils.SPUtils;

import org.greenrobot.greendao.query.QueryBuilder;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/26 17:00 
 */
public class DBCore {
    private static final String DEFAULT_DB_NAME = SPUtils.getValue(AppConstant.USER, AppConstant.USER_ID) + ".db";  //数据库名字
    private static final String DEFAuLT_DA_PASSWORD = SPUtils.getValue(AppConstant.USER, AppConstant.USER_ID);  //加密数据库的密码
    private static Context mContext;
    private static String mDbName;//得到数据库的名字的字段
    private static DaoMaster mDaoMaster; //数据库的管理者
    private static DaoSession sDaoSession; //此对象是对数据库进行增删改查的
    private static DaoMaster.DevOpenHelper helper;

    public static void init(Context context) {
        init(context, DEFAULT_DB_NAME);
    }

    private static void init(Context context, String dbName) {
        if (context == null) {
            throw new IllegalArgumentException("出错了");
        }
        mContext = context.getApplicationContext();
        mDbName = dbName;
    }

    /**
     * 得到数据库的管理类
     *
     * @return
     */
    private static DaoMaster getDaoMasterNormal() {
        if (helper == null) {
            helper = new MyOpenHelper(mContext, mDbName);
        }
        if (mDaoMaster == null) {
            mDaoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return mDaoMaster;
    }


    /**
     * 得到数据库加密的管理类
     *
     * @return
     */
    private static DaoMaster getmDaoMasterEncryption() {
        if (helper == null) {
            helper = new MyOpenHelper(mContext, mDbName);
        }
        if (mDaoMaster == null) {
            mDaoMaster = new DaoMaster(helper.getEncryptedReadableDb(DEFAuLT_DA_PASSWORD));
        }
        return mDaoMaster;
    }


    public static DaoSession getDaoSessionNormal() {
        if (sDaoSession == null) {
            if (mDaoMaster == null) {
                mDaoMaster = getDaoMasterNormal();
            }
            sDaoSession = mDaoMaster.newSession();
        }
        return sDaoSession;
    }


    public static DaoSession getDoaSessionEncryption() {
        if (sDaoSession == null) {
            if (mDaoMaster == null) {
                mDaoMaster = getmDaoMasterEncryption();
            }
            sDaoSession = mDaoMaster.newSession();
        }
        return sDaoSession;
    }


    /**
     * 在 QueryBuilder 类中内置两个 Flag 用于方便输出执行的 SQL 语句与传递参数的值
     */
    public static void enableQueryBuilderLog() {
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    public static class MyOpenHelper extends DaoMaster.DevOpenHelper {
        public MyOpenHelper(Context context, String name) {
            super(context, name);
        }
    }
}
