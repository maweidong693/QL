package com.weiwu.ql.main.login.country;

import java.util.List;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/11/25 16:57 
 */
public class CountryData {

    private int code;
    private String msg;
    private List<CountryEntity> data;

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

    public List<CountryEntity> getData() {
        return data;
    }

    public void setData(List<CountryEntity> data) {
        this.data = data;
    }
}
