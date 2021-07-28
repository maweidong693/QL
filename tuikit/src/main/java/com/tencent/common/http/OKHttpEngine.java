package com.tencent.common.http;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.tencent.common.Constant;
import com.tencent.common.UserInfo;
import com.tencent.common.log.YLog;
import com.tencent.qcloud.tim.uikit.utils.MD5Utils;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import com.tencent.common.dialog.DialogMaker;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static okhttp3.internal.Util.EMPTY_REQUEST;


/**
 * OKHttp简单实现
 */
public class OKHttpEngine implements IHttpEngine {

    private OkHttpClient client = null;
    private Context mContext;
    private int cacheSize = 10 * 1024 * 1024;

    public OKHttpEngine() {
        try {
            client = new OkHttpClient().newBuilder()
//                .connectionSpecs(Arrays.asList(spec, spec1))
                    .retryOnConnectionFailure(true)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
//                    .hostnameVerifier(new HostnameVerifier() {
//                        @Override
//                        public boolean verify(String hostname, SSLSession session) {
//                            return true;
//                        }
//                    })
//                    .sslSocketFactory(SSLSocketFactoryUtils.getInstance().getmSslSocketFactory(),
//                            SSLSocketFactoryUtils.getInstance().getmTrustManager())
//                .cache(new Cache(App.getInstance().getCacheDir(), cacheSize))
                    .build();
        } catch (Exception e) {
            YLog.d(e.getMessage());
        }
    }

    @Override
    public void get(final String url, Map<String, Object> params, final HttpCallBack callBack) {
        try {
            YLog.map(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Request request = new Request.Builder().url((url.equals(Constant.DOMAIN) ? url : (Constant.getBaseUrl() + url)) + getUrlParamsByMap(params))
                .addHeader("token",  UserInfo.getInstance().getToken())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                YLog.d("失败onFailure:" + e.toString());
                try {
                    DialogMaker.dismissProgressDialog();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                YHttp.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
//                            if (e.toString().contains("java.net.UnknownHostException")
//                                    ||e.toString().contains("java.io.IOException")
//                            ||e.toString().contains("java.io")
//                            ||e.toString().contains("java.net")){
//                                YToast.normal("通讯错误 请重新尝试！");
//                            }else{
//                                callBack.onFailed(e.toString());
//                            }
                            if (!url.equals(Constant.URL_LOGIN)) {
                                //不是登录

                                ToastUtil.toastShortMessage("通讯错误 请重新尝试！");
                            } else {
                                //是登录
                                callBack.onFailed("通讯错误 请重新尝试！");
                            }
                        } catch (Exception e1) {
                            ToastUtil.toastShortMessage("通讯错误 请重新尝试！");
                            e1.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                YLog.d("返回内容response:" + response.toString());
                if (response.isSuccessful()) {
                    final String result = response.body().string();
                    YLog.d("返回内容result:" + result);
                    YLog.json(result);
                    YHttp.handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                DialogMaker.dismissProgressDialog();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            BaseBean baseBean = null;
                            try {
                                baseBean = JSON.parseObject(result, BaseBean.class);
                            } catch (Exception e) {
                                e.printStackTrace();
                                ToastUtil.toastShortMessage("服务器异常 请稍候再试！");
                            }
                            if (baseBean == null) {
                                return;
                            }
                            YLog.d("解析内容baseBean:" + baseBean.toString());
                            if (baseBean.getError_code() == 200) {
                                try {
                                    callBack.onSuccess(JSON.parseObject(result, YHttp.analysisClassInfo(callBack)));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (baseBean.getError_code() == 401) {
                                //token过期
//                                YToast.normal(TextUtils.isEmpty(baseBean.getError_message()) ? "您的账号在别的设备登录或登录过期，请重新登录" : baseBean.getError_message());
                                BaseBean event = new BaseBean();
                                event.setError_message(baseBean.getError_message());
                                EventBus.getDefault().post(event);
                            } else {
                                ToastUtil.toastShortMessage(baseBean.getError_message() == null ? "请求失败" : baseBean.getError_message());
                                callBack.onFailed(baseBean.getError_message() == null ? "" : baseBean.getError_message());
                            }
                        }
                    });
                } else {
                    YHttp.handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                DialogMaker.dismissProgressDialog();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                ToastUtil.toastShortMessage("通讯错误 请重新尝试！");
                                callBack.onFailed(response.message());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            }
        });
    }

    @Override
    public void post(final String url, Map<String, Object> params, final HttpCallBack callBack) {
        try {
            YLog.map(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        params = params == null ? new HashMap<String, Object>() : params;
        RequestBody body = EMPTY_REQUEST;
//        YLog.d("请求参数:" + JSON.toJSONString(params));
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        String time = System.currentTimeMillis() / 1000 + "";
        String token = MD5Utils.md5Password(MD5Utils.md5Password(Constant.key) + MD5Utils.md5Password(time));
        if (params.isEmpty()){
            builder.addFormDataPart("time", time);
        }
//        builder.addFormDataPart("token", token);
        YLog.d(token + "---------" + time);
        for (String key : params.keySet()) {
            Object value = params.get(key);
            if (value instanceof File) {
                File file = (File) value;
                builder.addFormDataPart(key, file.getName(),
                        RequestBody.create(MediaType.parse("image/png"), file));//"application/octet-stream"
            }
            if (value instanceof List) {
                String s = listToString((List) value, ',');
//                String jsonString = JSON.toJSONString(value);
//                MediaType mediaType = MediaType.parse("application/json");
////                //生成RequestBody
//                RequestBody requestBody = RequestBody.create(mediaType,jsonString);
//                builder.addPart(requestBody);
                builder.addFormDataPart(key, s);
//                ArrayList<Object> lists = (ArrayList) value;
//                for (Object obj : lists) {
//                    if (obj instanceof File) {
//                        File file = (File) obj;
//                        builder.addFormDataPart(key, file.getName(),
//                                RequestBody.create(MediaType.parse("image/png"), file));
//                    }
//                }
            } else {
                builder.addFormDataPart(key, value == null ? "" : value.toString());
            }
        }
        body = builder.build();

        Request request = new Request.Builder()
                .url(url.equals(Constant.DOMAIN) ? url : Constant.getBaseUrl() + url)
                .addHeader("token",  UserInfo.getInstance().getToken())
                .post(body)
                .build();
        YLog.d("addHeader_TOKEN:" +  UserInfo.getInstance().getToken());
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                YLog.d("失败onFailure:" + e.toString());
                YHttp.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DialogMaker.dismissProgressDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
//                            if (e.toString().contains("java.net.UnknownHostException")
//                                    ||e.toString().contains("java.io.IOException")
//                                    ||e.toString().contains("java.io")
//                                    ||e.toString().contains("java.net")
//                                    ||e.toString().conrains("SocketTimeoutException)){
//                                YToast.normal("通讯错误 请重新尝试！");
//                            }else{
//                                callBack.onFailed(e.toString());
//                            }
                            if (!url.equals(Constant.URL_REMIND)) {
                                //不是新消息提醒
                                ToastUtil.toastShortMessage("通讯错误 请重新尝试！");
                            } else {
                                //是消息提醒
                                callBack.onFailed("通讯错误 请重新尝试！");
                            }
                        } catch (Exception e1) {
                            ToastUtil.toastShortMessage("通讯错误 请重新尝试！");
                            e1.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                YLog.d("返回内容response:" + response.toString());
                if (response.isSuccessful()) {
                    final String result = response.body().string();
                    YLog.d("返回内容result:" + result);
                    YLog.json(result);
                    YHttp.handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                DialogMaker.dismissProgressDialog();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            BaseBean baseBean = null;
                            try {
                                baseBean = JSON.parseObject(result, BaseBean.class);
                            } catch (Exception e) {
                                ToastUtil.toastShortMessage("服务器异常 请稍候再试！");
                                e.printStackTrace();
                                return;
                            }
                            if (baseBean == null) {

                                return;
                            }
                            YLog.d("解析内容baseBean:" + baseBean.toString());
                            if (baseBean.getError_code() == 200) {
                                try {
                                    callBack.onSuccess(JSON.parseObject(result, YHttp.analysisClassInfo(callBack)));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (baseBean.getError_code() == 401) {
                                //token过期
//                                YToast.normal(TextUtils.isEmpty(baseBean.getError_message()) ? "您的账号在别的设备登录或登录过期，请重新登录" : baseBean.getError_message());
                                BaseBean event = new BaseBean();
                                event.setError_message(baseBean.getError_message());
                                EventBus.getDefault().post(event);
                            } else {
                                ToastUtil.toastShortMessage(baseBean.getError_message() == null ? "请求失败" : baseBean.getError_message());
                                callBack.onFailed(baseBean.getError_message() == null ? "" : baseBean.getError_message());
                            }
                        }
                    });
                } else {
                    YHttp.handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                try {
                                    DialogMaker.dismissProgressDialog();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                ToastUtil.toastShortMessage("通讯错误 请重新尝试！");
                                callBack.onFailed(response.message());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    public static String listToString(List list, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(separator);
        }
        return list.isEmpty() ? "" : sb.toString().substring(0, sb.toString().length() - 1);
    }
    private String getUrlParamsByMap(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        StringBuffer params = new StringBuffer("?");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            params.append(entry.getKey());
            params.append("=");
            params.append(entry.getValue());
            params.append("&");
        }
        String str = params.toString();
        return str.substring(0, str.length() - 1);
    }
}
