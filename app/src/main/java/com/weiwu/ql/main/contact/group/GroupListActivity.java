package com.weiwu.ql.main.contact.group;

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
import com.weiwu.ql.main.contact.chat.ChatActivity;

import java.util.ArrayList;
import java.util.List;

public class GroupListActivity extends BaseActivity implements ContactContract.IContactView {

    private static final String TAG = GroupListActivity.class.getSimpleName();

    private TitleBarLayout mTitleBar;
    private ContactListView mListView;
    private ContactContract.IContactPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_list_activity);
        setPresenter(new ContactPresenter(ContactRepository.getInstance()));
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataSource();
    }

    private void init() {
        mTitleBar = findViewById(R.id.group_list_titlebar);
        mTitleBar.setTitle(getResources().getString(R.string.group), TitleBarLayout.POSITION.LEFT);
        mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        mTitleBar.setTitle(getResources().getString(R.string.add_group), TitleBarLayout.POSITION.RIGHT);
        mTitleBar.getRightIcon().setVisibility(View.GONE);
        mTitleBar.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(DemoApplication.instance(), AddMoreActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("isGroup", true);
//                startActivity(intent);
            }
        });

        mListView = findViewById(R.id.group_list);
        mListView.setOnItemClickListener(new ContactListView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, ContactItemBean contact) {

                Intent intent = new Intent(GroupListActivity.this, ChatActivity.class);
                intent.putExtra(TUIKitConstants.ProfileType.CONTENT, contact);
                intent.putExtra(AppConstant.IN, 2);
                intent.putExtra(AppConstant.CHAT_TYPE, 2);
                startActivity(intent);
            }
        });
    }

    public void loadDataSource() {
        mPresenter.getGroupList();
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
        if (data != null && data.getData() != null) {
            List<ContactListData.DataDTO.GroupsDTO> groups = new ArrayList<>();
            for (int i = 0; i < data.getData().size(); i++) {
                GroupListData.DataDTO dto = data.getData().get(i);
                ContactListData.DataDTO.GroupsDTO groupsDTO = new ContactListData.DataDTO.GroupsDTO();
                groupsDTO.setId(dto.getGroup_id());
                groupsDTO.setName(dto.getGroup_name());
                groupsDTO.setAvatar(dto.getAvatar_url());
                groups.add(groupsDTO);
            }
            ContactListData.DataDTO contactListData = new ContactListData.DataDTO();
            contactListData.setGroups(groups);
            mListView.loadDataSource(ContactListView.DataSource.GROUP_LIST, contactListData);
        }
    }

    @Override
    public void blackListReceive(BlackListData data) {

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
