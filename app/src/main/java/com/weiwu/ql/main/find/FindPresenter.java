package com.weiwu.ql.main.find;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.bean.FindData;
import com.weiwu.ql.main.mine.friends.FriendsContract;

import okhttp3.Cache;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/10/28 14:32 
 */
public class FindPresenter implements FriendsContract.IFindPresenter {

    private FriendsContract.FindSource mSource;
    private FriendsContract.IFindView mView;

    public FindPresenter(FriendsContract.FindSource source) {
        mSource = source;
    }

    @Override
    public void getFindList() {
        mSource.getFindList((LifecycleProvider) mView, new IBaseCallBack<FindData>() {
            @Override
            public void onSuccess(FindData data) {
                mView.onSuccess(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void attachView(FriendsContract.IFindView view) {
        mView = view;
    }

    @Override
    public void detachView(FriendsContract.IFindView view) {
        mView = null;
    }
}
