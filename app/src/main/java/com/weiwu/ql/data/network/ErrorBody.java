package com.weiwu.ql.data.network;

public class ErrorBody {

    /**
     * code : 000
     * data : {}
     * message : 用户不存在
     * success : false
     */

    private int code;
    private String msg;

    @Override
    public String toString() {
        return "ErrorBody{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", success=" + success +
                '}';
    }

    private boolean success;

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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ErrorBody() {
    }

    public ErrorBody(int code, String msg, boolean success) {
        this.code = code;
        this.msg = msg;
        this.success = success;
    }
}
