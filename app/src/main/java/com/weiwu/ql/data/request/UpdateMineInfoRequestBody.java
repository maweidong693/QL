package com.weiwu.ql.data.request;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/16 09:36 
 */
public class UpdateMineInfoRequestBody {
    private String mobile;
    private int sex;
    private String name;
    private String nickName;
    private String personalSignature;
    private String avator;
    private String momentImg;

    public UpdateMineInfoRequestBody(String momentImg) {
        this.momentImg = momentImg;
    }

    public String getMomentImg() {
        return momentImg;
    }

    public void setMomentImg(String momentImg) {
        this.momentImg = momentImg;
    }

    public UpdateMineInfoRequestBody(String mobile, int sex, String name, String nickName, String personalSignature, String avator) {
        this.mobile = mobile;
        this.sex = sex;
        this.name = name;
        this.nickName = nickName;
        this.personalSignature = personalSignature;
        this.avator = avator;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPersonalSignature() {
        return personalSignature;
    }

    public void setPersonalSignature(String personalSignature) {
        this.personalSignature = personalSignature;
    }

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }
}
