package com.weiwu.ql.data.request;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/22 09:49 
 */
public class LoginRequestBody {
    private String mobile;
    private String nickName;
    private String password;
    private int sex;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public LoginRequestBody(String mobile, String nickName, String password, int sex) {
        this.mobile = mobile;
        this.nickName = nickName;
        this.password = password;
        this.sex = sex;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoginRequestBody(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }
}
