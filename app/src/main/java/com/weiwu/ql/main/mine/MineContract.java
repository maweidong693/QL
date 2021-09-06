package com.weiwu.ql.main.mine;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.base.IBasePresenter;
import com.weiwu.ql.base.IBaseView;
import com.weiwu.ql.data.bean.CoinData;
import com.weiwu.ql.data.bean.CoinListData;
import com.weiwu.ql.data.bean.FriendInfoData;
import com.weiwu.ql.data.bean.HandlerData;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.bean.LoginReceive;
import com.weiwu.ql.data.bean.MineInfoData;
import com.weiwu.ql.data.bean.OrderData;
import com.weiwu.ql.data.bean.OrderDetailData;
import com.weiwu.ql.data.bean.WalletData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.AddFriendRequestBody;
import com.weiwu.ql.data.request.DistributeRequestBody;
import com.weiwu.ql.data.request.HandlerOrderRequestBody;
import com.weiwu.ql.data.request.LoginRequestBody;
import com.weiwu.ql.data.request.OrderRequestBody;
import com.weiwu.ql.data.request.UpdateMineInfoRequestBody;
import com.weiwu.ql.data.request.WebLoginRequestBody;

import okhttp3.MultipartBody;
import retrofit2.http.Query;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/22 09:47 
 */
public interface MineContract {
    public interface ILoginView extends IBaseView<ILoginPresenter> {
        void loginResult(LoginReceive data);

        void registerResult(LoginData data);

        void onFail(String msg, int code);
    }

    public interface ILoginPresenter extends IBasePresenter<ILoginView> {
        void login(LoginRequestBody body);

        void register(LoginRequestBody body);
    }

    public interface IMineView extends IBaseView<IMinePresenter> {
        void mineInfoReceive(MineInfoData data);

        void addFriendReceive(HttpResult data);

        void onFail(String msg, int code);
    }

    public interface IMinePresenter extends IBasePresenter<IMineView> {
        void getMineInfo();

        void addFriend(AddFriendRequestBody body);
    }

    public interface IWebLoginView extends IBaseView<IWebLoginPresenter> {
        void webLoginResult(HttpResult data);

        void onFail(String msg, int code);
    }

    public interface IWebLoginPresenter extends IBasePresenter<IWebLoginView> {
        void webLogin(String token, String code);

    }

    public interface IMineInfoDetailView extends IBaseView<IMineInfoDetailPresenter> {
        void onSuccess(HttpResult data);

        void mineInfoReceive(MineInfoData data);

        void uploadReceive(LoginData data);

        void onFail(String msg, int code);
    }

    public interface IMineInfoDetailPresenter extends IBasePresenter<IMineInfoDetailView> {
        void updateMineInfo(UpdateMineInfoRequestBody body);

        void uploadPic(MultipartBody.Part file);

        void getMineInfo();
    }

    public interface IAddWalletView extends IBaseView<IAddWalletPresenter> {
        void coinListReceive(CoinData data);

        void onSuccess(HttpResult data);

        void onFail(String msg, int code);
    }

    public interface IAddWalletPresenter extends IBasePresenter<IAddWalletView> {
        void getCoinList();

        void addWallet(CoinListData body);
    }

    public interface IOrderView extends IBaseView<IOrderPresenter> {
        void detailReceive(OrderDetailData data);

        void orderReceive(OrderData data);

        void walletReceive(WalletData data);

        void handlerReceive(HandlerData data);

        void onSuccess(HttpResult data);

        void onFail(String msg, int code);
    }

    public interface IOrderPresenter extends IBasePresenter<IOrderView> {
        void getAllOrder(OrderRequestBody body);

        void getOrderDetail(String id);

        void getAllWallet(String coinType);

        void handlerOrder(HandlerOrderRequestBody body);

        void receiveOrder(String tradeId);

        void confirmGetCoin(String cTradeId);

        void confirmRecycleCoin(String cTradeId);

        void getAllHandler();
    }

    public interface IDistributeView extends IBaseView<IDistributePresenter> {
        void handlerReceive(HandlerData data);

        void onSuccess(HttpResult data);

        void onFail(String msg, int code);

    }

    public interface IDistributePresenter extends IBasePresenter<IDistributeView> {
        void getAllHandler();

        void distributeOrder(DistributeRequestBody body);
    }

    public interface IWalletView extends IBaseView<IWalletPresenter> {
        void walletReceive(WalletData data);

        void onFail(String msg, int code);
    }

    public interface IWalletPresenter extends IBasePresenter<IWalletView> {
        void getAllWallet(String coinType);
    }

    public interface IInviteView extends IBaseView<IInvitePresenter> {
        void inviteKeyReceive(LoginData data);

        void onFail(String msg, int code);
    }

    public interface IInvitePresenter extends IBasePresenter<IInviteView> {
        void getInviteKey();
    }

    public interface MineSource {
        void login(LifecycleProvider provider, LoginRequestBody body, IBaseCallBack<LoginReceive> callBack);

        void register(LifecycleProvider provider, LoginRequestBody body, IBaseCallBack<LoginData> callBack);

        void getMineInfo(LifecycleProvider provider, IBaseCallBack<MineInfoData> callBack);

        void updateMineInfo(LifecycleProvider provider, UpdateMineInfoRequestBody body, IBaseCallBack<HttpResult> callBack);

        void uploadPic(LifecycleProvider provider, MultipartBody.Part file, IBaseCallBack<LoginData> callBack);

        void webLogin(LifecycleProvider provider, String token, String code, IBaseCallBack<HttpResult> callBack);

        void addFriend(LifecycleProvider provider, AddFriendRequestBody body, IBaseCallBack<HttpResult> callBack);

        void getCoin(LifecycleProvider provider, IBaseCallBack<CoinData> callBack);

        void addWallet(LifecycleProvider provider, CoinListData body, IBaseCallBack<HttpResult> callBack);

        void getAllOrder(LifecycleProvider provider, OrderRequestBody body, IBaseCallBack<OrderData> callBack);

        void handlerOrder(LifecycleProvider provider, HandlerOrderRequestBody body, IBaseCallBack<HttpResult> callBack);

        void receiveOrder(LifecycleProvider provider, String tradeId, IBaseCallBack<HttpResult> callBack);

        void distributeOrder(LifecycleProvider provider, DistributeRequestBody body, IBaseCallBack<HttpResult> callBack);

        void getAllHandler(LifecycleProvider provider, IBaseCallBack<HandlerData> callBack);

        void getOrderDetail(LifecycleProvider provider, String tradeId, IBaseCallBack<OrderDetailData> callBack);

        void getAllWallet(LifecycleProvider provider, String coinType, IBaseCallBack<WalletData> callBack);

        void confirmGetCoin(LifecycleProvider provider, String cTradeId, IBaseCallBack<HttpResult> callBack);

        void confirmRecycleCoin(LifecycleProvider provider, String cTradeId, IBaseCallBack<HttpResult> callBack);

        void getInviteKey(LifecycleProvider provider, IBaseCallBack<LoginData> callBack);

    }
}
