package com.weiwu.ql.main.contact.chat.util;

import android.content.Context;
import android.widget.Toast;

import com.weiwu.ql.AppConstant;
import com.weiwu.ql.utils.SPUtils;

public class Util {
    public static final String ws = "ws://47.108.192.155:8801/ws;" + SPUtils.getValue(AppConstant.USER, AppConstant.USER_TOKEN);//websocket测试地址

    public static void showToast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }
}
