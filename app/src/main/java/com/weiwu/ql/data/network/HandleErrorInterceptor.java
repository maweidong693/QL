package com.weiwu.ql.data.network;

import com.weiwu.ql.base.ServerException;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import kotlin.jvm.internal.Intrinsics;
import okhttp3.Response;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/5/6 16:40 
 */
public class HandleErrorInterceptor extends ResponseBodyInterceptor {
    @Override
    Response intercept(@NotNull Response response, String url, String body) throws IOException {
        Intrinsics.checkParameterIsNotNull(response, "response");
        JSONObject jsonObject = (JSONObject) null;
        try {
            jsonObject = new JSONObject(body);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject != null) {
            if (jsonObject.optInt("code", -1) != 200 && jsonObject.has("message")) {

                try {
                    String msg = jsonObject.getString("message");
                    int code = jsonObject.getInt("code");
                    throw new ServerException(msg, code);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }
}
