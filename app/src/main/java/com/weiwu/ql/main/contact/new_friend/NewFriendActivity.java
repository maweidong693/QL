package com.weiwu.ql.main.contact.new_friend;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.tencent.common.Constant;
import com.tencent.common.http.HttpCallBack;
import com.tencent.common.http.YHttp;
import com.tencent.imsdk.v2.V2TIMFriendApplication;
import com.tencent.qcloud.tim.uikit.NewFriendBean;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.weiwu.ql.MyApplication;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;
import com.weiwu.ql.data.bean.NewFriendListData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.repositories.ContactRepository;
import com.weiwu.ql.data.request.NewFriendRequestBody;
import com.weiwu.ql.main.contact.ContactContract;
import com.weiwu.ql.main.contact.add.AddMoreActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewFriendActivity extends BaseActivity implements ContactContract.INewFriendView {

    private static final String TAG = NewFriendActivity.class.getSimpleName();

    private TitleBarLayout mTitleBar;
    private ListView mNewFriendLv;
    private TextView mEmptyView;
    private List<NewFriendListData.DataDTO> mProcessedList = new ArrayList<>();
    private NewFriendAdapter mProcessedAdapter;
    private ListView mProcessedFriendLv;
    private ContactContract.INewFriendPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);
        setPresenter(new NewFriendPresenter(ContactRepository.getInstance()));
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPendency();
    }

    private void init() {
        mTitleBar = findViewById(R.id.new_friend_titlebar);
        mTitleBar.setTitle(getResources().getString(R.string.new_friend), TitleBarLayout.POSITION.LEFT);
        mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleBar.setTitle(getResources().getString(R.string.add_friend), TitleBarLayout.POSITION.RIGHT);
        mTitleBar.getRightIcon().setVisibility(View.GONE);
        mTitleBar.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewFriendActivity.this, AddMoreActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("isGroup", false);
                startActivity(intent);
            }
        });

        mNewFriendLv = findViewById(R.id.new_friend_list);
        mProcessedFriendLv = findViewById(R.id.processed_friend_list);
        mEmptyView = findViewById(R.id.empty_text);
    }

    private void initPendency() {
        mPresenter.getNewFriendList(new NewFriendRequestBody(""));
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void getNewFriendListReceive(NewFriendListData data) {
        if (data.getData() == null) {
//            if (data.getData().size() == 0) {
                mEmptyView.setText(getResources().getString(R.string.no_friend_apply));
                mNewFriendLv.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.VISIBLE);
                return;
//            }
        }
        mNewFriendLv.setVisibility(View.VISIBLE);
        mProcessedList.clear();

        mProcessedList.addAll(data.getData());
        mProcessedAdapter = new NewFriendAdapter(NewFriendActivity.this, R.layout.contact_new_friend_item, mProcessedList);
        mProcessedFriendLv.setAdapter(mProcessedAdapter);
        mProcessedAdapter.notifyDataSetChanged();

        mProcessedAdapter.setNewFriendRequestItemClickListener(new NewFriendAdapter.INewFriendRequestItemClickListener() {
            @Override
            public void mItemClick(NewFriendListData.DataDTO dataDTO) {
                mPresenter.handlerNewFriendRequest(new NewFriendRequestBody("200", dataDTO.getId(), ""));
            }
        });
    }

    @Override
    public void handlerFriendRequestReceive(HttpResult data) {
        if (data.getCode() == 200) {
            showToast(data.getMessage());
        } else {
            showToast(data.getMessage());
        }
        initPendency();
    }

    @Override
    public void onFail(String msg, int code) {
        showToast(msg);
        if (code == 10000) {
            MyApplication.loginAgain();
        }
    }

    @Override
    public void setPresenter(ContactContract.INewFriendPresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return this;
    }
}