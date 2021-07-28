package com.weiwu.ql.base;

public interface IBaseCallBack<T> {
    void onSuccess(T data);

    void onFail(String msg, int statusCode);
}
