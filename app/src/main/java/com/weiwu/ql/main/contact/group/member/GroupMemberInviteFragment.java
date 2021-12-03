package com.weiwu.ql.main.contact.group.member;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.View;

import com.tencent.common.http.ContactListData;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfo;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.weiwu.ql.MyApplication;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseFragment;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.repositories.GroupRepository;
import com.weiwu.ql.data.request.InviteOrDeleteRequestBody;
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
                if (list != null && list.size() > 0) {
                    mPresenter.addMembers(new InviteOrDeleteRequestBody(mGroupInfo.getGroupId(), listToString(list, ',')));
                } else {
                    showToast("请选择要邀请的成员！");
                }
            }
        });
    }

    public String listToString(List list, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(separator);
        }
        return list.isEmpty() ? "" : sb.toString().substring(0, sb.toString().length() - 1);
    }

    @Override
    public void contactReceive(ContactListData data) {
        mInviteLayout.setDataSource(mGroupInfo, data.getData());
    }

    @Override
    public void inviteFriendsReceive(HttpResult data) {
        showToast(data.getMsg());
        getActivity().finish();
    }

    @Override
    public void deleteFriendReceive(HttpResult data) {

    }

    @Override
    public void onFail(String msg, int code) {
        showToast(msg);
        if (code == 10001) {
            MyApplication.getInstance().loginAgain();
        }
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
