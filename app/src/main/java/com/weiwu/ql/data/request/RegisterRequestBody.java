package com.weiwu.ql.data.request;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/11/22 15:13 
 */
public class RegisterRequestBody {
    private String mobile;
    private String tel;
    private String password;
    private String nation_code;
    private String inv_code;

    public RegisterRequestBody(String password) {
        this.password = password;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public RegisterRequestBody(String tel, String password) {
        this.tel = tel;
        this.password = password;
    }

    public RegisterRequestBody(String mobile, String password, String nation_code, String inv_code) {
        this.mobile = mobile;
        this.password = password;
        this.nation_code = nation_code;
        this.inv_code = inv_code;
    }

    public RegisterRequestBody(String mobile, String password, String nation_code) {
        this.mobile = mobile;
        this.password = password;
        this.nation_code = nation_code;
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

    public String getNation_code() {
        return nation_code;
    }

    public void setNation_code(String nation_code) {
        this.nation_code = nation_code;
    }

    public String getInv_code() {
        return inv_code;
    }

    public void setInv_code(String inv_code) {
        this.inv_code = inv_code;
    }
}
