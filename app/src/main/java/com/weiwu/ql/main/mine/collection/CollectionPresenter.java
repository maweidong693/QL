package com.weiwu.ql.main.mine.collection;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.GroupInfoRequestBody;
import com.weiwu.ql.main.mine.MineContract;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/11/24 13:17 
 */
public class CollectionPresenter implements MineContract.ICollectionPresenter {

    private MineContract.MineSource mSource;
    private MineContract.ICollectionView mView;

    public CollectionPresenter(MineContract.MineSource source) {
        mSource = source;
    }

    @Override
    public void attachView(MineContract.ICollectionView view) {
        mView = view;
    }

    @Override
    public void detachView(MineContract.ICollectionView view) {
        mView = null;
    }

    @Override
    public void collectionList() {
        mSource.collectionList((LifecycleProvider) mView, new IBaseCallBack<FavoritesData>() {
            @Override
            public void onSuccess(FavoritesData data) {
                mView.collectionListReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void delCollection(GroupInfoRequestBody body) {
        mSource.delCollection((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
            @Override
            public void onSuccess(HttpResult data) {
                mView.onSuccess(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);

            }
        });
    }
}
