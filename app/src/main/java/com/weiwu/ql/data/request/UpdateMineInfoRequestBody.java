package com.weiwu.ql.data.request;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/16 09:36 
 */
public class UpdateMineInfoRequestBody {
    private String mobile;
    private String sex;
    private String name;
    private String nick_name;
    private String sing;
    private String face_url;
    private String momentImg;
    private String is_check_friend;

    public UpdateMineInfoRequestBody(String is_check_friend) {
        this.is_check_friend = is_check_friend;
    }

    public String getIs_check_friend() {
        return is_check_friend;
    }

    public void setIs_check_friend(String is_check_friend) {
        this.is_check_friend = is_check_friend;
    }

    public String getMomentImg() {
        return momentImg;
    }

    public void setMomentImg(String momentImg) {
        this.momentImg = momentImg;
    }

    public UpdateMineInfoRequestBody(String mobile, String sex, String name, String nickName, String personalSignature, String avator) {
        this.mobile = mobile;
        this.sex = sex;
        this.name = name;
        this.nick_name = nickName;
        this.sing = personalSignature;
        this.face_url = avator;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getSing() {
        return sing;
    }

    public void setSing(String sing) {
        this.sing = sing;
    }

    public String getFace_url() {
        return face_url;
    }

    public void setFace_url(String face_url) {
        this.face_url = face_url;
    }
}
