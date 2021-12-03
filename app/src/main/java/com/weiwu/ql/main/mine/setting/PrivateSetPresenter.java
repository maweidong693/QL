package com.weiwu.ql.main.mine.setting;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.bean.MineInfoData;
import com.weiwu.ql.data.bean.RangeBean;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.SetRangeRequestBody;
import com.weiwu.ql.data.request.UpdateMineInfoRequestBody;
import com.weiwu.ql.main.mine.MineContract;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/11/24 13:54 
 */
public class PrivateSetPresenter implements MineContract.IPrivateSetPresenter {

    private MineContract.IPrivateSetView mView;
    private MineContract.MineSource mSource;

    public PrivateSetPresenter(MineContract.MineSource source) {
        mSource = source;
    }

    @Override
    public void attachView(MineContract.IPrivateSetView view) {
        mView = view;
    }

    @Override
    public void detachView(MineContract.IPrivateSetView view) {
        mView = null;
    }

    @Override
    public void getRange() {
        mSource.getRange((LifecycleProvider) mView, new IBaseCallBack<RangeBean>() {
            @Override
            public void onSuccess(RangeBean data) {
                mView.rangeReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void setCheckFriend(UpdateMineInfoRequestBody body) {
        mSource.updateMineInfo((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
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

    @Override
    public void updateMineInfo(UpdateMineInfoRequestBody body) {
        mSource.updateMineInfo((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
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

    @Override
    public void getMineInfo() {
        mSource.getMineInfo((LifecycleProvider) mView, new IBaseCallBack<MineInfoData>() {
            @Override
            public void onSuccess(MineInfoData data) {
                mView.mineInfoReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);

            }
        });
    }

    @Override
    public void setRange(SetRangeRequestBody body) {
        mSource.setRange((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
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
