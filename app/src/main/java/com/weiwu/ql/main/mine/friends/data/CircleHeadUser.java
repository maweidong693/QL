package com.weiwu.ql.main.mine.friends.data;

/**
 * @author Joseph_Yan
 * on 2020/10/30
 * 朋友圈头布局
 */
public class CircleHeadUser {

    private String id;
    private String name = "";
    private String headUrl= "";
    private String circleBgUrl = "";

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

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getCircleBgUrl() {
        return circleBgUrl;
    }

    public void setCircleBgUrl(String circleBgUrl) {
        this.circleBgUrl = circleBgUrl;
    }
}
