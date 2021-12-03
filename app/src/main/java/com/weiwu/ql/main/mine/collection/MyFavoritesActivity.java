package com.weiwu.ql.main.mine.collection;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.tencent.common.Constant;
import com.tencent.common.http.BaseBean;
import com.tencent.common.http.HttpCallBack;
import com.tencent.common.http.YHttp;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.repositories.MineRepository;
import com.weiwu.ql.data.request.GroupInfoRequestBody;
import com.weiwu.ql.main.mine.MineContract;
import com.weiwu.ql.utils.IntentUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收藏
 */
public class MyFavoritesActivity extends BaseActivity implements OnLoadMoreListener, MineContract.ICollectionView {

    private TitleBarLayout mTitleBar;
    private RecyclerView mRecyclerView;
    private MultipleItemMyFavoritesAdapter myFavoritesAdapter;
    private int mCurrentCounter;
    private int TOTAL_COUNTER;
    private boolean isErr;
    private MineContract.ICollectionPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorites_layout);
        setPresenter(new CollectionPresenter(MineRepository.getInstance()));
        initView();
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myFavoritesAdapter = new MultipleItemMyFavoritesAdapter(new ArrayList<>());
        mRecyclerView.setAdapter(myFavoritesAdapter);
//        myFavoritesAdapter.getLoadMoreModule().setOnLoadMoreListener(this);
        mTitleBar = findViewById(R.id.title);
        mTitleBar.setTitle(getResources().getString(R.string.view_favorites), TitleBarLayout.POSITION.LEFT);
        mTitleBar.getRightGroup().setVisibility(View.GONE);
//        mTitleBar.setRightIcon(R.drawable.home_search_white);
        mTitleBar.setOnRightClickListener(v -> {
//            IntentUtil.redirectToNextActivity(this, MyFavoritesSearchActivity.class);

        });
        mTitleBar.setOnLeftClickListener(v -> finish());
        getData();
        myFavoritesAdapter.setCollectionDelListener(new MultipleItemMyFavoritesAdapter.ICollectionDelListener() {
            @Override
            public void delCollection(FavoritesData.FavoritesItem data) {
                mPresenter.delCollection(new GroupInfoRequestBody(Integer.parseInt(data.getId())));
            }
        });
    }

    private void getData() {
        mPresenter.collectionList();
        /*Map map = new HashMap();
//        map.put("page",1);
//        map.put("size",3);
        YHttp.obtain().post(Constant.URL_COLLECTION_LIST, map, new HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (baseBean.getData() == null) {
                    myFavoritesAdapter.setUseEmpty(true);
                    return;
                }
                List<FavoritesData.FavoritesItem> favoritesItems = JSON.parseArray(baseBean.getData().toString(), FavoritesData.FavoritesItem.class);
                myFavoritesAdapter.setList(favoritesItems);
            }

            @Override
            public void onFailed(String error) {

            }
        });*/
    }

    @Override
    public void onLoadMore() {
        //加载数据
        if (mCurrentCounter >= TOTAL_COUNTER) {
            //Data are all loaded.
            myFavoritesAdapter.getLoadMoreModule().loadMoreEnd();
        } else {
            if (isErr) {
                //Successfully get more data
//                        myFavoritesAdapter.addData();
                mCurrentCounter = myFavoritesAdapter.getData().size();
                myFavoritesAdapter.getLoadMoreModule().loadMoreComplete();
            } else {
                //Get more data failed
                isErr = true;
                myFavoritesAdapter.getLoadMoreModule().loadMoreFail();

            }
        }
    }

    @Override
    public void setPresenter(MineContract.ICollectionPresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return this;
    }

    @Override
    public void collectionListReceive(FavoritesData data) {
        if (data.getData() == null) {
            myFavoritesAdapter.setUseEmpty(true);
            return;
        }
        myFavoritesAdapter.setList(data.getData());
    }

    @Override
    public void onSuccess(HttpResult data) {
        mPresenter.collectionList();
        showToast(data.getMsg());
    }

    @Override
    public void onFail(String msg, int code) {
        showToast(msg);
    }
}