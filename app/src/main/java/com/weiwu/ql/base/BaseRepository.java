package com.weiwu.ql.base;

import android.util.Log;

import com.google.gson.Gson;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.BuildConfig;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.weiwu.ql.data.network.ErrorBody;
import com.weiwu.ql.utils.Logger;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class BaseRepository {
    public <T, R> void observer(LifecycleProvider provider, Observable<T> observable, Function<T, ObservableSource<R>> function, final IBaseCallBack<R> callBack) {

        observable.flatMap(function)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(provider instanceof RxAppCompatActivity ? ((RxAppCompatActivity) provider).<R>bindUntilEvent(ActivityEvent.DESTROY)
                        : ((RxFragment) provider).<R>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new Observer<R>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(R r) {

                        if (BuildConfig.DEBUG) {
                            Logger.d("%s onNext = %s ", getClass().getSimpleName(), r.toString());
                        }
                        callBack.onSuccess(r);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (BuildConfig.DEBUG) {
                            e.printStackTrace();
                            Logger.d("%s onError =  %s", getClass().getSimpleName(), e.getMessage());
                        }
                        if (e instanceof HttpException) {
                            HttpException httpException = (HttpException) e;
                            ResponseBody errorBody = httpException.response().errorBody();
                            if (errorBody != null) {
                                try {
                                    String errorBodyString = errorBody.string();
                                    Gson gson = new Gson();
                                    ErrorBody body = gson.fromJson(errorBodyString, ErrorBody.class);
                                    callBack.onFail(body.getMsg(), body.getCode());
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }

                            }
                        }

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public <T, R> void observerNoMap(LifecycleProvider provider, Observable<R> observable, final IBaseCallBack<R> callBack) {

        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).compose(provider instanceof RxAppCompatActivity ? ((RxAppCompatActivity) provider).<R>bindUntilEvent(ActivityEvent.DESTROY)
                : ((RxFragment) provider).<R>bindUntilEvent(FragmentEvent.DESTROY_VIEW)).subscribe(new Observer<R>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(R r) {
                Log.d(TAG, "onNext: ne");
                if (BuildConfig.DEBUG) {
                    Logger.d("%s onNext = %s ", getClass().getSimpleName(), r.toString());
                }
                callBack.onSuccess(r);
            }

            @Override
            public void onError(Throwable e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                    Logger.d("%s onError =  %s", getClass().getSimpleName(), e.getMessage());
                }
                Logger.d("%s onError =  %s", getClass().getSimpleName(), e.getMessage());
                Log.d(TAG, "onError: " + e.getMessage());
                if (e instanceof ServerException) {
                    ServerException httpException = (ServerException) e;
                    callBack.onFail(httpException.getErrMsg(), httpException.getErrCode());
                }

            }

            @Override
            public void onComplete() {

            }
        });

    }

    private static final String TAG = "BaseRepository";
}