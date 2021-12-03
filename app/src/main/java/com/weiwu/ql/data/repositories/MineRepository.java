package com.weiwu.ql.data.repositories;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.BaseRepository;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.bean.LoginReceive;
import com.weiwu.ql.data.bean.MineInfoData;
import com.weiwu.ql.data.bean.NewMsgData;
import com.weiwu.ql.data.bean.RangeBean;
import com.weiwu.ql.data.bean.SocketDataBean;
import com.weiwu.ql.data.network.DataService;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.AddFriendRequestBody;
import com.weiwu.ql.data.request.ClientIdBody;
import com.weiwu.ql.data.request.GroupInfoRequestBody;
import com.weiwu.ql.data.request.LoginRequestBody;
import com.weiwu.ql.data.request.RegisterRequestBody;
import com.weiwu.ql.data.request.SendPhoneMsgRequestBody;
import com.weiwu.ql.data.request.SetRangeRequestBody;
import com.weiwu.ql.data.request.UpdateMineInfoRequestBody;
import com.weiwu.ql.data.request.VerifyCodeRequestBody;
import com.weiwu.ql.main.login.country.CountryData;
import com.weiwu.ql.main.mine.MineContract;
import com.weiwu.ql.main.mine.collection.FavoritesData;
import com.weiwu.ql.main.mine.friends.data.MsgData;

import okhttp3.MultipartBody;

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
    public void getInviteKey(LifecycleProvider provider, IBaseCallBack<LoginData> callBack) {
        observerNoMap(provider, DataService.getApiService().getInviteKey(), callBack);
    }

    @Override
    public void getSocketUrl(LifecycleProvider provider, IBaseCallBack<SocketDataBean> callBack) {
        observerNoMap(provider, DataService.getApiService().getSocketUrl(), callBack);
    }

    @Override
    public void sendPhoneMsg(LifecycleProvider provider, SendPhoneMsgRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().sendMsg(body), callBack);
    }

    @Override
    public void verify(LifecycleProvider provider, VerifyCodeRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().verify(body), callBack);
    }

    @Override
    public void register(LifecycleProvider provider, RegisterRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().register(body), callBack);
    }

    @Override
    public void init(LifecycleProvider provider, ClientIdBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().init(body), callBack);
    }

    @Override
    public void collectionList(LifecycleProvider provider, IBaseCallBack<FavoritesData> callBack) {
        observerNoMap(provider, DataService.getApiService().getCollectionList(), callBack);
    }

    @Override
    public void delCollection(LifecycleProvider provider, GroupInfoRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().delCollection(body), callBack);
    }

    @Override
    public void getRange(LifecycleProvider provider, IBaseCallBack<RangeBean> callBack) {
        observerNoMap(provider, DataService.getApiService().getRange(), callBack);
    }

    @Override
    public void setRange(LifecycleProvider provider, SetRangeRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().setRange(body), callBack);
    }

    @Override
    public void getNewMsgCount(LifecycleProvider provider, IBaseCallBack<NewMsgData> callBack) {
        observerNoMap(provider, DataService.getApiService().getNewMsgCount(), callBack);
    }

    @Override
    public void getMsgList(LifecycleProvider provider, IBaseCallBack<MsgData> callBack) {

    }

    @Override
    public void readMsg(LifecycleProvider provider, GroupInfoRequestBody body, IBaseCallBack<HttpResult> callBack) {

    }

    @Override
    public void getCountryList(LifecycleProvider provider, IBaseCallBack<CountryData> callBack) {
        observerNoMap(provider, DataService.getApiService().getCountryList(), callBack);
    }

    @Override
    public void updatePassword(LifecycleProvider provider, RegisterRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().updatePassword(body), callBack);
    }

    @Override
    public void forgetPassword(LifecycleProvider provider, RegisterRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().forgetPassword(body), callBack);
    }
}
