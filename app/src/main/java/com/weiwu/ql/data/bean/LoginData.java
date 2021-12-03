package com.weiwu.ql.data.bean;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/22 09:51 
 */
public class LoginData {
    private int code;
    private String msg;
    private DataDto data;

    public LoginData(int code, String msg, DataDto data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public DataDto getData() {
        return data;
    }

    public void setData(DataDto data) {
        this.data = data;
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



    public class DataDto {
        private String src;
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }
    }
}
