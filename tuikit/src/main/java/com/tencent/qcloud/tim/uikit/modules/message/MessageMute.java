package com.tencent.qcloud.tim.uikit.modules.message;

/**
 * @author Joseph_Yan
 * on 2020/10/19
 */
public class MessageMute {

    private String  mute ; //0 关闭禁言   1开启禁言
    private String  id ;   //

    public MessageMute(String mute) {
        this.mute = mute;
    }

    public MessageMute(String mute, String id) {
        this.mute = mute;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMute() {
        return mute;
    }

    public void setMute(String mute) {
        this.mute = mute;
    }
}
