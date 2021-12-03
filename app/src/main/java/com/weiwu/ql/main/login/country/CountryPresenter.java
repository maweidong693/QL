package com.weiwu.ql.main.login.country;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.main.mine.MineContract;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/11/25 17:01 
 */
public class CountryPresenter implements MineContract.ICountryPresenter {

    private MineContract.MineSource mSource;
    private MineContract.ICountryView mView;

    public CountryPresenter(MineContract.MineSource source) {
        mSource = source;
    }

    @Override
    public void attachView(MineContract.ICountryView view) {
        mView = view;
    }

    @Override
    public void detachView(MineContract.ICountryView view) {
        mView = null;
    }

    @Override
    public void getCountryList() {
        mSource.getCountryList((LifecycleProvider) mView, new IBaseCallBack<CountryData>() {
            @Override
            public void onSuccess(CountryData data) {
                mView.countryReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
            }
        });
    }
}
