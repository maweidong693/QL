package com.weiwu.ql;

import android.app.Application;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.smtt.sdk.QbSdk;
import com.weiwu.ql.greendao.db.DaoMaster;
import com.weiwu.ql.greendao.db.DaoSession;
import com.weiwu.ql.main.login.LoginActivity;
import com.weiwu.ql.utils.SPUtils;
import com.youth.banner.util.LogUtils;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/4/25 09:34 
 */
public class MyApplication extends Application {

    public static Application mApplicationContext;

    private static MyApplication instance;

    public static MyApplication instance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationContext = this;

        instance = this;
        /**
         * TUIKit的初始化函数
         *
         * @param context  应用的上下文，一般为对应应用的ApplicationContext
         * @param sdkAppID 您在腾讯云注册应用时分配的sdkAppID
         * @param configs  TUIKit的相关配置项，一般使用默认即可，需特殊配置参考API文档
         */
        TUIKit.init(this, GenerateTestUserSig.SDKAPPID, new ConfigHelper().getConfigs());
//        initX5();

        setDatabase();

    }

    /* 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。*/

    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private void setDatabase() {
        mHelper = new DaoMaster.DevOpenHelper(this, "notes-db", null);
        db = mHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    private void initX5() {
        //非wifi情况下，主动下载x5内核
        QbSdk.setDownloadWithoutWifi(true);
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                LogUtils.d("开启TBS===X5加速成功");
            }

            @Override
            public void onCoreInitFinished() {
                LogUtils.d("开启TBS===X5加速失败");

            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }

    private static volatile long lastJump = 0;

    public static void loginAgain() {
        if (System.currentTimeMillis() - lastJump > 2000) {
            boolean b = SPUtils.clearValue(AppConstant.USER);
            lastJump = System.currentTimeMillis();
            Intent intent = new Intent(mApplicationContext, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            mApplicationContext.startActivity(intent);
        }
    }
}
