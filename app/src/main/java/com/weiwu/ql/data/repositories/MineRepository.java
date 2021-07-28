package com.weiwu.ql.data.repositories;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.weiwu.ql.base.BaseRepository;
import com.weiwu.ql.base.IBaseCallBack;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.bean.MineInfoData;
import com.weiwu.ql.data.network.DataService;
import com.weiwu.ql.data.request.LoginRequestBody;
import com.weiwu.ql.main.mine.MineContract;

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
    public void login(LifecycleProvider provider, LoginRequestBody body, IBaseCallBack<LoginData> callBack) {
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
}
