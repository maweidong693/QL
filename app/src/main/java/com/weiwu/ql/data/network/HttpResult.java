package com.weiwu.ql.data.network;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/4/30 11:17 
 */
public class HttpResult<T> {
    public int code = -1;
    public String msg;
    public T data;

    @Override
    public String toString() {
        return "HttpResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public HttpResult() {
    }

    public HttpResult(int code, String message, T data) {
        this.code = code;
        this.msg = message;
        this.data = data;
    }
}
