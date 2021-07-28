package com.weiwu.ql.main.contact;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.tencent.common.http.ContactListData;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactLayout;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactListView;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.weiwu.ql.MyApplication;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseFragment;
import com.weiwu.ql.data.repositories.ContactRepository;
import com.weiwu.ql.main.contact.detail.FriendProfileActivity;
import com.weiwu.ql.main.contact.new_friend.NewFriendActivity;
import com.weiwu.ql.view.Menu;

import java.util.ArrayList;
import java.util.List;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/22 14:01 
 */
public class ContactFragment extends BaseFragment implements ContactContract.IContactView {

    private RecyclerView mRvContact;
    private ContactLayout mContactLayout;
    private Menu mMenu;
    private ContactContract.IContactPresenter mPresenter;
    private List<ContactListData.DataDTO.FriendsDTO> friends = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contact;
    }

    @Override
    public void onViewCreated(View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setPresenter(new ContactPresenter(ContactRepository.getInstance()));
        initView(view);
    }

    private void initView(View view) {
        // 从布局文件中获取通讯录面板
        mContactLayout = view.findViewById(R.id.contact_layout);
        mMenu = new Menu(getActivity(), mContactLayout.getTitleBar(), Menu.MENU_TYPE_CONTACT);
        mContactLayout.getTitleBar().setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMenu.isShowing()) {
                    mMenu.hide();
                } else {
                    mMenu.show();
                }
            }
        });
        mPresenter.getContactList();
        mContactLayout.getContactListView().setOnItemClickListener(new ContactListView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, ContactItemBean contact) {
                if (position == 0) {
                    Intent intent = new Intent(getContext(), NewFriendActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent);
                } else if (position == 1) {
                    /*Intent intent = new Intent(getContext(), GroupListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent);*/
                } else if (position == 2) {
                    /*Intent intent = new Intent(getContext(), BlackListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent);*/
                } else {
                    Intent intent = new Intent(getContext(), FriendProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(TUIKitConstants.ProfileType.CONTENT, contact);
                    getActivity().startActivity(intent);

                }
            }
        });
    }

    private void refreshData() {
        // 通讯录面板的默认UI和交互初始化
        mContactLayout.initDefault(friends);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    public void contactReceive(ContactListData data) {
        if (data.getData() != null) {
            friends = data.getData().getFriends();
            mContactLayout.initDefault(friends);
        }
    }

    @Override
    public void onFail(String msg, int code) {
        showToast(msg);
        if (code == 10000) {
            MyApplication.loginAgain();
        }
    }

    @Override
    public void setPresenter(ContactContract.IContactPresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return getActivity();
    }
}
