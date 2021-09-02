package com.weiwu.ql.main.contact.group.member;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.View;

import com.tencent.common.http.ContactListData;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfo;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupMemberInfo;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.weiwu.ql.MyApplication;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseFragment;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.repositories.GroupRepository;
import com.weiwu.ql.data.request.InviteOrDeleteRequestBody;
import com.weiwu.ql.main.contact.group.GroupContract;

import java.util.ArrayList;
import java.util.List;


public class GroupMemberDeleteFragment extends BaseFragment implements GroupContract.IGroupAddView {

    private GroupMemberDeleteLayout mMemberDelLayout;
    private GroupContract.IGroupAddPresenter mPresenter;
    private GroupInfo mGroupInfo;

    /*@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.group_fragment_del_members, container, false);
        mMemberDelLayout = mBaseView.findViewById(R.id.group_member_del_layout);
        init();
        return mBaseView;
    }*/

    @Override
    protected int getLayoutId() {
        return R.layout.group_fragment_del_members;
    }

    @Override
    public void onViewCreated(View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGroupInfo = (GroupInfo) getArguments().getSerializable(TUIKitConstants.Group.GROUP_INFO);
        setPresenter(new InviteMemberPresenter(GroupRepository.getInstance()));
        mMemberDelLayout = view.findViewById(R.id.group_member_del_layout);
        init();
    }

    private void init() {
        mMemberDelLayout.setDataSource((GroupInfo) getArguments().getSerializable(TUIKitConstants.Group.GROUP_INFO), null);
        mMemberDelLayout.getTitleBar().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backward();
            }
        });
        mMemberDelLayout.setDeleteSelectItemListener(new GroupMemberDeleteLayout.IDeleteSelectItemListener() {
            @Override
            public void select(List<GroupMemberInfo> list) {
                if (list != null && list.size() > 0) {
                    List<String> members = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        members.add(list.get(i).getAccount());
                    }
                    mPresenter.deleteMembers(new InviteOrDeleteRequestBody(mGroupInfo.getId(), members));
                }else {
                    showToast("请选择成员！");
                }
            }
        });
    }

    @Override
    public void contactReceive(ContactListData data) {

    }

    @Override
    public void inviteFriendsReceive(HttpResult data) {

    }

    @Override
    public void deleteFriendReceive(HttpResult data) {
        showToast(data.getMessage());
        getActivity().finish();
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
