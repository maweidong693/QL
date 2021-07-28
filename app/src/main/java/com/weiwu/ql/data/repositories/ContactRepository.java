package com.weiwu.ql.data.repositories;

import com.tencent.common.http.ContactListData;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.BaseRepository;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.bean.NewFriendListData;
import com.weiwu.ql.data.network.DataService;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.AddFriendRequestBody;
import com.weiwu.ql.data.request.NewFriendRequestBody;
import com.weiwu.ql.main.contact.ContactContract;

import okhttp3.MultipartBody;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/22 15:34 
 */
public class ContactRepository extends BaseRepository implements ContactContract.ContactSource {

    public volatile static ContactRepository mRepository;

    public static ContactRepository getInstance() {
        if (mRepository == null) {
            synchronized (ContactRepository.class) {
                if (mRepository == null) {
                    mRepository = new ContactRepository();
                }
            }
        }
        return mRepository;
    }

    @Override
    public void getContactList(LifecycleProvider provider, IBaseCallBack<ContactListData> callBack) {
        observerNoMap(provider, DataService.getApiService().getContactList(), callBack);
    }

    @Override
    public void addFriend(LifecycleProvider provider, AddFriendRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().addFriend(body), callBack);
    }

    @Override
    public void getNewFriendList(LifecycleProvider provider, NewFriendRequestBody body, IBaseCallBack<NewFriendListData> callBack) {
        observerNoMap(provider, DataService.getApiService().getNewFriendList(body), callBack);
    }

    @Override
    public void handlerNewFriendRequest(LifecycleProvider provider, NewFriendRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().handlerNewFriendRequest(body), callBack);
    }

    @Override
    public void uploadPic(LifecycleProvider lifecycleProvider, MultipartBody.Part file, IBaseCallBack<LoginData> callBack) {
        observerNoMap(lifecycleProvider, DataService.getApiService().uploadPicture(file), callBack);
    }
}