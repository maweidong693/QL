package com.weiwu.ql.main.find;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.bean.HandlerData;
import com.weiwu.ql.data.bean.OrderData;
import com.weiwu.ql.data.bean.OrderDetailData;
import com.weiwu.ql.data.bean.WalletData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.HandlerOrderRequestBody;
import com.weiwu.ql.data.request.OrderRequestBody;
import com.weiwu.ql.main.mine.MineContract;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/28 17:20 
 */
public class OrderPresenter implements MineContract.IOrderPresenter {

    private MineContract.IOrderView mView;
    private MineContract.MineSource mSource;

    public OrderPresenter(MineContract.MineSource source) {
        mSource = source;
    }

    @Override
    public void getAllOrder(OrderRequestBody body) {
        mSource.getAllOrder((LifecycleProvider) mView, body, new IBaseCallBack<OrderData>() {
            @Override
            public void onSuccess(OrderData data) {
                mView.orderReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void getOrderDetail(String id) {
        mSource.getOrderDetail((LifecycleProvider) mView, id, new IBaseCallBack<OrderDetailData>() {
            @Override
            public void onSuccess(OrderDetailData data) {
                mView.detailReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void getAllWallet(String coinType) {
        mSource.getAllWallet((LifecycleProvider) mView, coinType, new IBaseCallBack<WalletData>() {
            @Override
            public void onSuccess(WalletData data) {
                mView.walletReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void handlerOrder(HandlerOrderRequestBody body) {
        mSource.handlerOrder((LifecycleProvider) mView, body, new IBaseCallBack<HttpResult>() {
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
    public void receiveOrder(String tradeId) {
        mSource.receiveOrder((LifecycleProvider) mView, tradeId, new IBaseCallBack<HttpResult>() {
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
    public void confirmGetCoin(String cTradeId) {
        mSource.confirmGetCoin((LifecycleProvider) mView, cTradeId, new IBaseCallBack<HttpResult>() {
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
    public void confirmRecycleCoin(String cTradeId) {
        mSource.confirmRecycleCoin((LifecycleProvider) mView, cTradeId, new IBaseCallBack<HttpResult>() {
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
    public void getAllHandler() {
        mSource.getAllHandler((LifecycleProvider) mView, new IBaseCallBack<HandlerData>() {
            @Override
            public void onSuccess(HandlerData data) {
                mView.handlerReceive(data);
            }

            @Override
            public void onFail(String msg, int statusCode) {
                mView.onFail(msg, statusCode);
            }
        });
    }

    @Override
    public void attachView(MineContract.IOrderView view) {
        mView = view;
    }

    @Override
    public void detachView(MineContract.IOrderView view) {
        mView = null;
    }
}
