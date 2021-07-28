package com.weiwu.ql.main.contact;

import com.tencent.common.http.ContactListData;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.base.IBasePresenter;
import com.weiwu.ql.base.IBaseView;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.bean.NewFriendListData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.AddFriendRequestBody;
import com.weiwu.ql.data.request.NewFriendRequestBody;

import okhttp3.MultipartBody;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/22 15:30 
 */
public interface ContactContract {
    interface IContactView extends IBaseView<IContactPresenter> {
        void contactReceive(ContactListData data);

        void onFail(String msg, int code);
    }

    interface IContactPresenter extends IBasePresenter<IContactView> {
        void getContactList();
    }

    interface IAddFriendView extends IBaseView<IAddFriendPresenter> {
        void addFriendReceive(HttpResult data);

        void onFail(String msg, int code);
    }

    interface IAddFriendPresenter extends IBasePresenter<IAddFriendView> {
        void addFriend(AddFriendRequestBody body);
    }

    interface INewFriendView extends IBaseView<INewFriendPresenter> {
        void getNewFriendListReceive(NewFriendListData data);

        void handlerFriendRequestReceive(HttpResult data);

        void onFail(String msg, int code);
    }

    interface INewFriendPresenter extends IBasePresenter<INewFriendView> {
        void getNewFriendList(NewFriendRequestBody body);

        void handlerNewFriendRequest(NewFriendRequestBody body);
    }

    interface IChatView extends IBaseView<IChatPresenter> {
        void uploadPicResult(LoginData data);

        void onFail(String msg, int code);
    }

    interface IChatPresenter extends IBasePresenter<IChatView> {
        void uploadPic(MultipartBody.Part file);
    }

    interface ContactSource {
        void getContactList(LifecycleProvider provider, IBaseCallBack<ContactListData> callBack);

        void addFriend(LifecycleProvider provider, AddFriendRequestBody body, IBaseCallBack<HttpResult> callBack);

        void getNewFriendList(LifecycleProvider provider, NewFriendRequestBody body, IBaseCallBack<NewFriendListData> callBack);

        void handlerNewFriendRequest(LifecycleProvider provider, NewFriendRequestBody body, IBaseCallBack<HttpResult> callBack);

        void uploadPic(LifecycleProvider lifecycleProvider, MultipartBody.Part file, IBaseCallBack<LoginData> callBack);
    }
}
