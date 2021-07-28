package com.tencent.qcloud.tim.uikit.modules.chat.base;

/**
 * 自定义消息的bean实体，用来与json的相互转化
 * 自定义群震动
 */
public class CustomMessage {
    public String mark = "custom";
    public int type;
    public String id = "";     //发送者id
    public String text = "";   //内容
    public String remind = ""; //界面可能需要展示的文字
    public String link = "";   //扩展链接

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRemind() {
        return remind;
    }

    public void setRemind(String remind) {
        this.remind = remind;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
