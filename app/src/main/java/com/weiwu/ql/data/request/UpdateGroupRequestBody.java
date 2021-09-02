package com.weiwu.ql.data.request;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/14 17:18 
 */
public class UpdateGroupRequestBody {
    private String id;
    private String name;
    private String notice;
    private String avator;
    private String isExamine;
    private String isFrd;

    public UpdateGroupRequestBody() {
    }

    public UpdateGroupRequestBody(String id, String name, String notice, String avator, String isExamine, String isFrd) {
        this.id = id;
        this.name = name;
        this.notice = notice;
        this.avator = avator;
        this.isExamine = isExamine;
        this.isFrd = isFrd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public String getIsExamine() {
        return isExamine;
    }

    public void setIsExamine(String isExamine) {
        this.isExamine = isExamine;
    }

    public String getIsFrd() {
        return isFrd;
    }

    public void setIsFrd(String isFrd) {
        this.isFrd = isFrd;
    }
}
