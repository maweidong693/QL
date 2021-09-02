package com.weiwu.ql.main.mine.detail;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.bean.MineInfoData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.UpdateMineInfoRequestBody;
import com.weiwu.ql.main.mine.MineContract;

import okhttp3.MultipartBody;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/16 09:44 
 */
public class MineInfoPresenter implements MineContract.IMineInfoDetailPresenter {

    private MineContract.IMineInfoDetailView mView;
    private MineContract.MineSource mSource;

    public MineInfoPresenter(MineContract.MineSource source) {
        mSource = source;
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
    public void uploadPic(MultipartBody.Part file) {
        mSource.uploadPic((LifecycleProvider) mView, file, new IBaseCallBack<LoginData>() {
            @Override
            public void onSuccess(LoginData data) {
                mView.uploadReceive(data);
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
    public void attachView(MineContract.IMineInfoDetailView view) {
        mView = view;
    }

    @Override
    public void detachView(MineContract.IMineInfoDetailView view) {
        mView = null;
    }
}
