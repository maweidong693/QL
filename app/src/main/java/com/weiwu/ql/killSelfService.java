package com.weiwu.ql;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/14 10:34 
 */

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

/***
 * 该服务只用来让APP重启，生命周期也仅仅是只是重启APP。重启完即自我杀死
 */
public class killSelfService extends Service {
    /**
     * 关闭应用后多久重新启动
     */
    private static long stopDelayed = 2000;
    private Handler handler;
    private String PackageName;

    public killSelfService() {
        handler = new Handler();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        stopDelayed = intent.getLongExtra("Delayed", 2000);
        PackageName = intent.getStringExtra("PackageName");
        handler.postDelayed(() -> {
            Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(PackageName);
            startActivity(LaunchIntent);
            killSelfService.this.stopSelf();
        }, stopDelayed);
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
