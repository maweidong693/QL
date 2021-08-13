package com.weiwu.ql.main.contact.group.member;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.View;

import com.tencent.common.http.ContactListData;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfo;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseFragment;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.repositories.ContactRepository;
import com.weiwu.ql.data.repositories.GroupRepository;
import com.weiwu.ql.data.request.InviteOrDeleteRequestBody;
import com.weiwu.ql.main.contact.ContactContract;
import com.weiwu.ql.main.contact.ContactPresenter;
import com.weiwu.ql.main.contact.group.GroupContract;

import java.util.List;


public class GroupMemberInviteFragment extends BaseFragment implements GroupContract.IGroupAddView {

    private GroupMemberInviteLayout mInviteLayout;
    private GroupContract.IGroupAddPresenter mPresenter;
    private GroupInfo mGroupInfo;

    /*@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.group_fragment_invite_members, container, false);
        mInviteLayout = mBaseView.findViewById(R.id.group_member_invite_layout);
        mInviteLayout.setParentLayout(this);
        init();
        return mBaseView;
    }*/

    @Override
    protected int getLayoutId() {
        return R.layout.group_fragment_invite_members;
    }

    @Override
    public void onViewCreated(View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGroupInfo = (GroupInfo) getArguments().getSerializable(TUIKitConstants.Group.GROUP_INFO);
        setPresenter(new InviteMemberPresenter(GroupRepository.getInstance()));
        mInviteLayout = view.findViewById(R.id.group_member_invite_layout);
        mInviteLayout.setParentLayout(this);
        init();
    }

    private void init() {
        mPresenter.getContactList();
        mInviteLayout.getTitleBar().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backward();
            }
        });
        mInviteLayout.setAddSelectItemListener(new GroupMemberInviteLayout.IAddSelectItemListener() {
            @Override
            public void select(List<String> list) {
                mPresenter.addMembers(new InviteOrDeleteRequestBody(mGroupInfo.getId(), list));
            }
        });
    }

    @Override
    public void contactReceive(ContactListData data) {
        mInviteLayout.setDataSource(mGroupInfo, data.getData());
    }

    @Override
    public void inviteFriendsReceive(HttpResult data) {
        showToast(data.getMessage());
        getActivity().finish();
    }

    @Override
    public void deleteFriendReceive(HttpResult data) {

    }

    @Override
    public void onFail(String msg, int code) {
        showToast(msg);
    }

    @Override
    public void setPresenter(GroupContract.IGroupAddPresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return getActivity();
    }
}