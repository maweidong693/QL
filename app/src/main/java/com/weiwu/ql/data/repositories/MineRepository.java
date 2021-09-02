package com.weiwu.ql.data.repositories;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.BaseRepository;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.bean.CoinData;
import com.weiwu.ql.data.bean.CoinListData;
import com.weiwu.ql.data.bean.HandlerData;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.bean.LoginReceive;
import com.weiwu.ql.data.bean.MineInfoData;
import com.weiwu.ql.data.bean.OrderData;
import com.weiwu.ql.data.bean.OrderDetailData;
import com.weiwu.ql.data.bean.WalletData;
import com.weiwu.ql.data.network.DataService;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.AddFriendRequestBody;
import com.weiwu.ql.data.request.DistributeRequestBody;
import com.weiwu.ql.data.request.HandlerOrderRequestBody;
import com.weiwu.ql.data.request.LoginRequestBody;
import com.weiwu.ql.data.request.OrderRequestBody;
import com.weiwu.ql.data.request.UpdateMineInfoRequestBody;
import com.weiwu.ql.data.request.WebLoginRequestBody;
import com.weiwu.ql.greendao.db.DaoSession;
import com.weiwu.ql.main.mine.MineContract;

import okhttp3.MultipartBody;
import retrofit2.http.Query;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/22 09:53 
 */
public class MineRepository extends BaseRepository implements MineContract.MineSource {

    public volatile static MineRepository mMineRepository;

    public static MineRepository getInstance() {
        if (mMineRepository == null) {
            synchronized (MineRepository.class) {
                if (mMineRepository == null) {
                    mMineRepository = new MineRepository();
                }
            }
        }
        return mMineRepository;
    }

    @Override
    public void login(LifecycleProvider provider, LoginRequestBody body, IBaseCallBack<LoginReceive> callBack) {
        observerNoMap(provider, DataService.getApiService().login(body), callBack);
    }

    @Override
    public void register(LifecycleProvider provider, LoginRequestBody body, IBaseCallBack<LoginData> callBack) {
        observerNoMap(provider, DataService.getApiService().register(body), callBack);
    }

    @Override
    public void getMineInfo(LifecycleProvider provider, IBaseCallBack<MineInfoData> callBack) {
        observerNoMap(provider, DataService.getApiService().getMineInfo(), callBack);
    }

    @Override
    public void updateMineInfo(LifecycleProvider provider, UpdateMineInfoRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().updateMineInfo(body), callBack);
    }

    @Override
    public void uploadPic(LifecycleProvider provider, MultipartBody.Part file, IBaseCallBack<LoginData> callBack) {
        observerNoMap(provider, DataService.getApiService().uploadPicture(file), callBack);
    }

    @Override
    public void webLogin(LifecycleProvider provider, String token, String code, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().webLogin(token, code), callBack);
    }

    @Override
    public void addFriend(LifecycleProvider provider, AddFriendRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().addFriend(body), callBack);
    }

    @Override
    public void getCoin(LifecycleProvider provider, IBaseCallBack<CoinData> callBack) {
        observerNoMap(provider, DataService.getApiService().getCoinList(), callBack);
    }

    @Override
    public void addWallet(LifecycleProvider provider, CoinListData body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().addCoin(body), callBack);
    }

    @Override
    public void getAllOrder(LifecycleProvider provider, OrderRequestBody body, IBaseCallBack<OrderData> callBack) {
        observerNoMap(provider, DataService.getApiService().getAllOrder(body), callBack);
    }

    @Override
    public void handlerOrder(LifecycleProvider provider, HandlerOrderRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().handlerOrder(body), callBack);
    }

    @Override
    public void receiveOrder(LifecycleProvider provider, String tradeId, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().receiveOrder(tradeId), callBack);

    }

    @Override
    public void distributeOrder(LifecycleProvider provider, DistributeRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().distributeOrder(body), callBack);

    }

    @Override
    public void getAllHandler(LifecycleProvider provider, IBaseCallBack<HandlerData> callBack) {
        observerNoMap(provider, DataService.getApiService().getAllHandler(), callBack);
    }

    @Override
    public void getOrderDetail(LifecycleProvider provider, String tradeId, IBaseCallBack<OrderDetailData> callBack) {
        observerNoMap(provider, DataService.getApiService().getOrderDetail(tradeId), callBack);
    }

    @Override
    public void getAllWallet(LifecycleProvider provider, IBaseCallBack<WalletData> callBack) {
        observerNoMap(provider, DataService.getApiService().getAllWallet(), callBack);
    }

    @Override
    public void confirmGetCoin(LifecycleProvider provider, String cTradeId, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().confirmGetCoin(cTradeId), callBack);
    }

    @Override
    public void confirmRecycleCoin(LifecycleProvider provider, String cTradeId, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().confirmRecycleCoin(cTradeId), callBack);
    }

    @Override
    public void getInviteKey(LifecycleProvider provider, IBaseCallBack<LoginData> callBack) {
        observerNoMap(provider, DataService.getApiService().getInviteKey(), callBack);
    }
}
