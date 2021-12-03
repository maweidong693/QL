package com.weiwu.ql.main.mine;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.base.IBasePresenter;
import com.weiwu.ql.base.IBaseView;
import com.weiwu.ql.data.bean.CoinData;
import com.weiwu.ql.data.bean.CoinListData;
import com.weiwu.ql.data.bean.HandlerData;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.bean.LoginReceive;
import com.weiwu.ql.data.bean.MineInfoData;
import com.weiwu.ql.data.bean.NewMsgData;
import com.weiwu.ql.data.bean.OrderData;
import com.weiwu.ql.data.bean.OrderDetailData;
import com.weiwu.ql.data.bean.RangeBean;
import com.weiwu.ql.data.bean.SocketDataBean;
import com.weiwu.ql.data.bean.WalletData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.AddFriendRequestBody;
import com.weiwu.ql.data.request.ClientIdBody;
import com.weiwu.ql.data.request.DistributeRequestBody;
import com.weiwu.ql.data.request.GroupInfoRequestBody;
import com.weiwu.ql.data.request.HandlerOrderRequestBody;
import com.weiwu.ql.data.request.LoginRequestBody;
import com.weiwu.ql.data.request.OrderRequestBody;
import com.weiwu.ql.data.request.RegisterRequestBody;
import com.weiwu.ql.data.request.SendPhoneMsgRequestBody;
import com.weiwu.ql.data.request.SetRangeRequestBody;
import com.weiwu.ql.data.request.UpdateMineInfoRequestBody;
import com.weiwu.ql.data.request.VerifyCodeRequestBody;
import com.weiwu.ql.main.login.country.CountryData;
import com.weiwu.ql.main.mine.collection.FavoritesData;
import com.weiwu.ql.main.mine.friends.data.MsgData;

import okhttp3.MultipartBody;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/22 09:47 
 */
public interface MineContract {
    public interface ILoginView extends IBaseView<ILoginPresenter> {

        void updateSuccess(HttpResult data);

        void verifySuccess(HttpResult data);

        void registerSuccess(HttpResult data);

        void loginResult(LoginReceive data);

        void socketUrl(SocketDataBean dataBean);

        void onSuccess(HttpResult data);

        void onFail(String msg, int code);
    }

    public interface ICountryView extends IBaseView<ICountryPresenter> {
        void countryReceive(CountryData data);
    }

    public interface ICountryPresenter extends IBasePresenter<ICountryView> {
        void getCountryList();
    }

    public interface ICollectionView extends IBaseView<ICollectionPresenter> {
        void collectionListReceive(FavoritesData data);

        void onSuccess(HttpResult data);

        void onFail(String msg, int code);
    }

    public interface ICollectionPresenter extends IBasePresenter<ICollectionView> {
        void collectionList();

        void delCollection(GroupInfoRequestBody body);
    }

    public interface ILoginPresenter extends IBasePresenter<ILoginView> {

        void forgetPassword(RegisterRequestBody body);

        void updatePassword(RegisterRequestBody body);

        void register(RegisterRequestBody body);

        void verify(VerifyCodeRequestBody body);

        void login(LoginRequestBody body);

        void sendPhone(SendPhoneMsgRequestBody body);

        void getSocketUrl();

    }

    public interface IMineView extends IBaseView<IMinePresenter> {
        void mineInfoReceive(MineInfoData data);

        void newMsgCountResult(NewMsgData data);

        void addFriendReceive(HttpResult data);

        void onSuccess(HttpResult data);

        void onFail(String msg, int code);
    }

    public interface IMinePresenter extends IBasePresenter<IMineView> {
        void getMineInfo();

        void getNewMsgCount();

        void init(ClientIdBody body);

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

    public interface IPrivateSetView extends IBaseView<IPrivateSetPresenter> {

        void mineInfoReceive(MineInfoData data);

        void rangeReceive(RangeBean data);

        void onSuccess(HttpResult data);

        void onFail(String msg, int code);

    }

    public interface IPrivateSetPresenter extends IBasePresenter<IPrivateSetView> {
        void getRange();

        void setCheckFriend(UpdateMineInfoRequestBody body);

        void updateMineInfo(UpdateMineInfoRequestBody body);

        void getMineInfo();

        void setRange(SetRangeRequestBody body);
    }

    public interface MineSource {
        void login(LifecycleProvider provider, LoginRequestBody body, IBaseCallBack<LoginReceive> callBack);

        void getMineInfo(LifecycleProvider provider, IBaseCallBack<MineInfoData> callBack);

        void updateMineInfo(LifecycleProvider provider, UpdateMineInfoRequestBody body, IBaseCallBack<HttpResult> callBack);

        void uploadPic(LifecycleProvider provider, MultipartBody.Part file, IBaseCallBack<LoginData> callBack);

        void webLogin(LifecycleProvider provider, String token, String code, IBaseCallBack<HttpResult> callBack);

        void addFriend(LifecycleProvider provider, AddFriendRequestBody body, IBaseCallBack<HttpResult> callBack);

        void getInviteKey(LifecycleProvider provider, IBaseCallBack<LoginData> callBack);

        void getSocketUrl(LifecycleProvider provider, IBaseCallBack<SocketDataBean> callBack);

        void sendPhoneMsg(LifecycleProvider provider, SendPhoneMsgRequestBody body, IBaseCallBack<HttpResult> callBack);

        void verify(LifecycleProvider provider, VerifyCodeRequestBody body, IBaseCallBack<HttpResult> callBack);

        void register(LifecycleProvider provider, RegisterRequestBody body, IBaseCallBack<HttpResult> callBack);

        void init(LifecycleProvider provider, ClientIdBody body, IBaseCallBack<HttpResult> callBack);

        void collectionList(LifecycleProvider provider, IBaseCallBack<FavoritesData> callBack);

        void delCollection(LifecycleProvider provider, GroupInfoRequestBody body, IBaseCallBack<HttpResult> callBack);

        void getRange(LifecycleProvider provider, IBaseCallBack<RangeBean> callBack);

        void setRange(LifecycleProvider provider, SetRangeRequestBody body, IBaseCallBack<HttpResult> callBack);

        void getNewMsgCount(LifecycleProvider provider, IBaseCallBack<NewMsgData> callBack);

        void getMsgList(LifecycleProvider provider, IBaseCallBack<MsgData> callBack);

        void readMsg(LifecycleProvider provider, GroupInfoRequestBody body, IBaseCallBack<HttpResult> callBack);

        void getCountryList(LifecycleProvider provider, IBaseCallBack<CountryData> callBack);

        void updatePassword(LifecycleProvider provider, RegisterRequestBody body, IBaseCallBack<HttpResult> callBack);

        void forgetPassword(LifecycleProvider provider, RegisterRequestBody body, IBaseCallBack<HttpResult> callBack);
    }
}
