package com.weiwu.ql.main.mine;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.base.IBasePresenter;
import com.weiwu.ql.base.IBaseView;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.bean.MineInfoData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.LoginRequestBody;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/22 09:47 
 */
public interface MineContract {
    public interface ILoginView extends IBaseView<ILoginPresenter> {
        void loginResult(LoginData data);

        void registerResult(LoginData data);

        void onFail(String msg, int code);
    }

    public interface ILoginPresenter extends IBasePresenter<ILoginView> {
        void login(LoginRequestBody body);

        void register(LoginRequestBody body);
    }

    public interface IMineView extends IBaseView<IMinePresenter> {
        void mineInfoReceive(MineInfoData data);

        void onFail(String msg, int code);
    }

    public interface IMinePresenter extends IBasePresenter<IMineView> {
        void getMineInfo();
    }

    public interface MineSource {
        void login(LifecycleProvider provider, LoginRequestBody body, IBaseCallBack<LoginData> callBack);

        void register(LifecycleProvider provider, LoginRequestBody body, IBaseCallBack<LoginData> callBack);

        void getMineInfo(LifecycleProvider provider, IBaseCallBack<MineInfoData> callBack);
    }
}
