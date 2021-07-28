package com.tencent.common.http;

/**
 * @author Joseph_Yan
 * on 2019/6/21
 */
public class BaseBean {
    int code ;
    String msg;
    int error_code;
    String error_message="";
    Object data;

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

    public int getError_code() {
        return getCode() ;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_message() {
        return getMsg();
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
        this.msg = error_message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
