package com.weiwu.ql.main.contact.black;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.tencent.common.http.BlackListData;
import com.tencent.common.http.ContactListData;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactListView;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.weiwu.ql.AppConstant;
import com.weiwu.ql.MyApplication;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;
import com.weiwu.ql.data.bean.GroupListData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.repositories.ContactRepository;
import com.weiwu.ql.main.contact.ContactContract;
import com.weiwu.ql.main.contact.ContactPresenter;
import com.weiwu.ql.main.contact.detail.FriendProfileActivity;

import java.util.ArrayList;
import java.util.List;

public class BlackListActivity extends BaseActivity implements ContactContract.IContactView {

    private TitleBarLayout mTitleBar;
    private ContactListView mListView;
    private ContactContract.IContactPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_blacklist_activity);
        setPresenter(new ContactPresenter(ContactRepository.getInstance()));
        init();
    }

    private void init() {
        mTitleBar = findViewById(R.id.black_list_titlebar);
        mTitleBar.setTitle(getResources().getString(R.string.blacklist), TitleBarLayout.POSITION.LEFT);
        mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleBar.getRightGroup().setVisibility(View.GONE);

        mListView = findViewById(R.id.black_list);
        mListView.setOnItemClickListener(new ContactListView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, ContactItemBean contact) {
                Intent intent = new Intent(BlackListActivity.this, FriendProfileActivity.class);
                intent.putExtra(TUIKitConstants.ProfileType.CONTENT, contact);
                intent.putExtra(AppConstant.FRIEND_TYPE, 121);
                startActivity(intent);
            }
        });

        mPresenter.getBlackList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getBlackList();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void contactReceive(ContactListData data) {

    }

    @Override
    public void groupListReceive(GroupListData data) {

    }

    @Override
    public void blackListReceive(BlackListData data) {
        ContactListData.DataDTO dataDTO = new ContactListData.DataDTO();
        List<ContactListData.DataDTO.FriendsDTO> list = new ArrayList<>();
        for (int i = 0; i < data.getData().size(); i++) {
            ContactListData.DataDTO.FriendsDTO friendsDTO = new ContactListData.DataDTO.FriendsDTO();
            ContactListData.DataDTO.FriendsDTO friend = friendsDTO.covertTIMFriend(data.getData().get(i));
            list.add(friend);
        }
        dataDTO.setBlockFriends(list);
        mListView.loadDataSource(ContactListView.DataSource.BLACK_LIST, dataDTO);
    }

    @Override
    public void createGroupResult(HttpResult result) {

    }

    @Override
    public void onFail(String msg, int code) {
        showToast(msg);
        if (code == 10001) {
            MyApplication.getInstance().loginAgain();
        }
    }

    @Override
    public void setPresenter(ContactContract.IContactPresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return this;
    }
}
