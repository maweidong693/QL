package com.tencent.qcloud.tim.uikit.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;

/**
 * @author Joseph_Yan
 * on 2020/10/19
 * 自定义群信息
 */
public class CustomInfo {
    public final static String IS_FRIEND = "addFriend"; //是否允许加好友 0是允许，1是禁止  默认禁止
    public final static String IS_MUTE = "Mute";//是否禁言  0 允许    1禁止
    public final static String GROUP_ADD_OPT = "addOpt"; //无需审核直接进群    0 需要审核     1 无需审核
    public final static String GROUP_MANAGEMENT = "GROUP_MANAGEMENT";//群管理员  {"GROUP_MANAGEMENT": ["mbigrxsf"],"LAST_EDIT": "GROUP_MANAGEMENT"}
    public final static String LAST_EDIT = "LAST_EDIT";//最后一次编辑的哪项
    public final static String SINGLE_MUTE = "SINGLE_MUTE";//禁言
    public final static String CANCEL_MUTE = "CANCEL_MUTE";//取消禁言

    public static HashMap<String, Object> InfoStrToMap(String groupIntroduction) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (TextUtils.isEmpty(groupIntroduction) || TextUtils.equals("这是群简介", groupIntroduction)) {
            hashMap = new HashMap();
            hashMap.put(IS_FRIEND, "1");
            hashMap.put(IS_MUTE, "0");
            hashMap.put(GROUP_ADD_OPT, "0");
        } else {
            try {
                Gson gson = new Gson();
                hashMap = gson.fromJson(groupIntroduction, HashMap.class);
                if (hashMap!=null){
                    if (!hashMap.containsKey(IS_FRIEND)){
                        hashMap.put(IS_FRIEND, "1");
                    }
                    if (!hashMap.containsKey(IS_MUTE)){
                        hashMap.put(IS_MUTE, "0");
                    }
                    if (!hashMap.containsKey(GROUP_ADD_OPT)){
                        hashMap.put(GROUP_ADD_OPT, "0");
                    }
                }
            } catch (Exception e) {
                hashMap = new HashMap();
                hashMap.put(IS_FRIEND, "1");
                hashMap.put(IS_MUTE, "0");
                hashMap.put(GROUP_ADD_OPT, "0");
                e.printStackTrace();
            }
        }
        return hashMap;

    }

    public static String InfoMapToStr(HashMap hashMap) {
        Gson gson = new Gson();
        String s = gson.toJson(hashMap);

        return s;

    }
}
