package com.weiwu.ql.data.repositories;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.BaseRepository;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.bean.FindData;
import com.weiwu.ql.data.bean.FriendsData;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.bean.MineInfoData;
import com.weiwu.ql.data.network.DataService;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.AddCommentRequestBody;
import com.weiwu.ql.data.request.IssueMessageRequestBody;
import com.weiwu.ql.data.request.UpdateMineInfoRequestBody;
import com.weiwu.ql.main.mine.friends.FriendsContract;

import okhttp3.MultipartBody;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/17 16:35 
 */
public class FindRepository extends BaseRepository implements FriendsContract.FindSource {

    public volatile static FindRepository mRepository;

    public static FindRepository getInstance() {
        if (mRepository == null) {
            synchronized (FindRepository.class) {
                if (mRepository == null) {
                    mRepository = new FindRepository();
                }
            }
        }
        return mRepository;
    }

    @Override
    public void issueMessage(LifecycleProvider provider, IssueMessageRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().issueMessage(body), callBack);
    }

    @Override
    public void uploadPic(LifecycleProvider provider, MultipartBody.Part file, IBaseCallBack<LoginData> callBack) {
        observerNoMap(provider, DataService.getApiService().uploadPicture(file), callBack);
    }

    @Override
    public void getAllFriends(LifecycleProvider provider, String pageSize, String pageNum, IBaseCallBack<FriendsData> callBack) {
        observerNoMap(provider, DataService.getApiService().getAllFriends(pageSize, pageNum), callBack);
    }

    @Override
    public void deleteFriends(LifecycleProvider provider, String momentId, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().deleteFriends(momentId), callBack);
    }

    @Override
    public void addComment(LifecycleProvider provider, AddCommentRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().addComment(body), callBack);
    }

    @Override
    public void deleteComment(LifecycleProvider provider, String commentId, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().removeComment(commentId), callBack);
    }

    @Override
    public void updateBg(LifecycleProvider provider, UpdateMineInfoRequestBody body, IBaseCallBack<HttpResult> callBack) {
        observerNoMap(provider, DataService.getApiService().updateMineInfo(body), callBack);
    }

    @Override
    public void getMineInfo(LifecycleProvider provider, IBaseCallBack<MineInfoData> callBack) {
        observerNoMap(provider, DataService.getApiService().getMineInfo(), callBack);
    }

    @Override
    public void getFindList(LifecycleProvider provider, IBaseCallBack<FindData> callBack) {
        observerNoMap(provider,DataService.getApiService().getFindList(), callBack);
    }
}
