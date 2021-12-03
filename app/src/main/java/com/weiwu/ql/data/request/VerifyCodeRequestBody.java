package com.weiwu.ql.data.request;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/11/22 15:00 
 */
public class VerifyCodeRequestBody {
    private String tel;
    private String yzm;

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getYzmCode() {
        return yzm;
    }

    public void setNationCode(String yzm) {
        this.yzm = yzm;
    }

    public VerifyCodeRequestBody(String tel, String yzm) {
        this.tel = tel;
        this.yzm = yzm;
    }
}
