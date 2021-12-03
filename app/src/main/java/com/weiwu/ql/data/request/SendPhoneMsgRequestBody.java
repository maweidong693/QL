package com.weiwu.ql.data.request;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/11/22 15:00 
 */
public class SendPhoneMsgRequestBody {
    private String tel;
    private String nationCode;

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getNationCode() {
        return nationCode;
    }

    public void setNationCode(String nationCode) {
        this.nationCode = nationCode;
    }

    public SendPhoneMsgRequestBody(String tel, String nationCode) {
        this.tel = tel;
        this.nationCode = nationCode;
    }
}
