package com.weiwu.ql.main.contact.group.member;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfo;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupMemberData;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupMemberInfo;
import com.tencent.qcloud.tim.uikit.modules.group.info.InfoData;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.weiwu.ql.base.BaseFragment;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.repositories.GroupRepository;
import com.weiwu.ql.data.request.InviteOrDeleteRequestBody;
import com.weiwu.ql.main.contact.group.GroupContract;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class GroupMemberMuteFragment extends BaseFragment implements GroupContract.IGroupForbiddenView {

    private GroupMemberMuteLayout mInviteLayout;
    private GroupContract.IGroupForbiddenPresenter mPresenter;
    private GroupInfo mGroupInfo;

    /*@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.group_fragment_mute_members, container, false);
        mInviteLayout = mBaseView.findViewById(R.id.group_member_invite_layout);
        mInviteLayout.setParentLayout(this);
        init();
        return mBaseView;
    }*/

    @Override
    protected int getLayoutId() {
        return R.layout.group_fragment_mute_members;
    }

    @Override
    public void onViewCreated(View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGroupInfo = (GroupInfo) getArguments().getSerializable(TUIKitConstants.Group.GROUP_INFO);
        setPresenter(new GroupForbiddenPresenter(GroupRepository.getInstance()));
        mInviteLayout = view.findViewById(R.id.group_member_invite_layout);
        mInviteLayout.setParentLayout(this);
        init();
    }

    private void init() {
        mInviteLayout.initTitle((InfoData) getArguments().getSerializable("info"));
        mPresenter.getGroupMember(mGroupInfo.getId());
        mInviteLayout.getTitleBar().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backward();
            }
        });
        mInviteLayout.setMuteListener(new GroupMemberMuteLayout.IMuteListener() {
            @Override
            public void mute(ContactItemBean contact, boolean selected) {
                List<String> list = new ArrayList<>();
                list.add(contact.getId());
                if (selected) {
                    mPresenter.forbiddenMember(new InviteOrDeleteRequestBody(mGroupInfo.getId(), list));
                } else {
                    mPresenter.cancelForbiddenMember(new InviteOrDeleteRequestBody(mGroupInfo.getId(), list));
                }
            }
        });
    }

    @Override
    public void groupMemberReceive(GroupMemberData data) {
        if (data.getData() != null) {
            List<GroupMemberInfo> members = new ArrayList<>();
            for (int i = 0; i < data.getData().size(); i++) {
                GroupMemberInfo memberInfo = new GroupMemberInfo();
                GroupMemberData.DataDTO dataDTO = data.getData().get(i);
                memberInfo.covertTIMGroupMemberInfo(dataDTO);
                members.add(memberInfo);
            }
            mGroupInfo.setMemberDetails(members);
            mInviteLayout.setDataSource(mGroupInfo);
        }
    }

    @Override
    public void forbiddenReceive(HttpResult data) {
        showToast(data.getMessage());
    }

    @Override
    public void onFail(String msg, int code) {
        showToast(msg);
    }

    @Override
    public void setPresenter(GroupContract.IGroupForbiddenPresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return getActivity();
    }
}
