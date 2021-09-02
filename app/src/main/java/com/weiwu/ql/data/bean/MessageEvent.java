package com.weiwu.ql.data.bean;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/6/18 14:31 
 */
public class MessageEvent {

    public final String message;
    public final int code;

    public MessageEvent(String message, int code) {
        this.message = message;
        this.code = code;
    }
}
