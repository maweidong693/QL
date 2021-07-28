package com.tencent.common.dialog;

/**
 * 底部弹出菜单Bean
 * Created by hui.feng on 2018/1/22.
 */

public class BottomDialogBean<T> {

    private String dialogText;
    private String dialogDescribe;
    private T t;

    public BottomDialogBean() {
    }

    public BottomDialogBean(String dialogText, String dialogDescribe) {
        this.dialogText = dialogText;
        this.dialogDescribe = dialogDescribe;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public BottomDialogBean(String dialogText) {
        this.dialogText = dialogText;
    }

    public String getDialogText() {
        return dialogText;
    }

    public void setDialogText(String dialogText) {
        this.dialogText = dialogText;
    }

    public String getDialogDescribe() {
        return dialogDescribe;
    }

    public void setDialogDescribe(String dialogDescribe) {
        this.dialogDescribe = dialogDescribe;
    }
}
