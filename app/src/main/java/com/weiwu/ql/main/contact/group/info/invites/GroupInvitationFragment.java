package com.weiwu.ql.main.contact.group.info.invites;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.tencent.common.Constant;
import com.tencent.common.dialog.DialogMaker;
import com.tencent.common.http.BaseBean;
import com.tencent.common.http.HttpCallBack;
import com.tencent.common.http.YHttp;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.GlideEngine;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfo;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.weiwu.ql.MyApplication;
import com.weiwu.ql.base.BaseFragment;
import com.weiwu.ql.data.bean.CheckInvitesData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.repositories.GroupRepository;
import com.weiwu.ql.data.request.CheckInvitesRequestBody;
import com.weiwu.ql.main.contact.group.GroupContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupInvitationFragment extends BaseFragment implements GroupContract.ICheckInvitesView {

    private GroupInfo mGroupInfo;
    private TitleBarLayout mTitleBar;
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter<GroupInvitInfo, BaseViewHolder> mAdapter;
    private GroupContract.ICheckInvitesPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_group_invitation;
    }

    @Override
    public void onViewCreated(View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setPresenter(new GroupInvitesPresenter(GroupRepository.getInstance()));
        mGroupInfo = (GroupInfo) getArguments().getSerializable(TUIKitConstants.Group.GROUP_INFO);
        mTitleBar = view.findViewById(R.id.self_info_title_bar);
        mTitleBar.getRightGroup().setVisibility(View.GONE);
        mTitleBar.setTitle(getResources().getString(R.string.group_apply_members), TitleBarLayout.POSITION.MIDDLE);
        mTitleBar.setOnLeftClickListener(v -> backward());
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new BaseQuickAdapter<GroupInvitInfo, BaseViewHolder>(R.layout.group_member_apply_adpater) {
            @Override
            protected void convert(BaseViewHolder holder, GroupInvitInfo item) {
                // 0 待审核 1拒绝 2通过
                holder.setText(R.id.group_apply_member_name, item.getTo_nick())
                        .setText(R.id.group_apply_reason, item.getStatus() == 0 ? item.getCreate_time() : item.getUpdate_time());
                GlideEngine.loadImage(holder.getView(R.id.group_apply_member_icon), item.getFace_url(), null);

                if (item.getStatus() == 0) {
                    holder.getView(R.id.group_apply_accept).setVisibility(View.VISIBLE);
                    holder.setText(R.id.group_apply_accept, R.string.accept);
                    holder.getView(R.id.group_apply_accept).setBackground(TUIKit.getAppContext().getResources().getDrawable(R.color.bg_positive_btn));
                    holder.getView(R.id.group_apply_accept).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            acceptOrRefuse(item, 1);
                        }
                    });
                    holder.getView(R.id.group_apply_refuse).setVisibility(View.VISIBLE);
                    holder.setText(R.id.group_apply_refuse, R.string.refuse);
                    holder.getView(R.id.group_apply_refuse).setBackground(TUIKit.getAppContext().getResources().getDrawable(R.color.bg_negative_btn));
                    holder.getView(R.id.group_apply_refuse).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            acceptOrRefuse(item, 0);
                        }
                    });
                } else if (item.getStatus() == 2) {
                    holder.getView(R.id.group_apply_accept).setVisibility(View.VISIBLE);
                    holder.getView(R.id.group_apply_accept).setClickable(false);
                    holder.setText(R.id.group_apply_accept, R.string.accepted);
                    holder.getView(R.id.group_apply_accept).setBackground(TUIKit.getAppContext().getResources().getDrawable(R.drawable.gray_btn_bg));
                    holder.getView(R.id.group_apply_refuse).setVisibility(View.GONE);
                } else if (item.getStatus() == 1) {
                    holder.getView(R.id.group_apply_refuse).setVisibility(View.VISIBLE);
                    holder.getView(R.id.group_apply_refuse).setClickable(false);
                    holder.setText(R.id.group_apply_refuse, R.string.refused);
                    holder.getView(R.id.group_apply_refuse).setBackground(TUIKit.getAppContext().getResources().getDrawable(R.drawable.gray_btn_bg));
                    holder.getView(R.id.group_apply_accept).setVisibility(View.GONE);
                }


            }
        };
        mRecyclerView.setAdapter(mAdapter);
        getData();
    }

    private void acceptOrRefuse(GroupInvitInfo info, int status) {
        DialogMaker.showProgressDialog(getActivity(), "请稍后…");

        mPresenter.checkInvites(info.getId(), String.valueOf(status));
        /*Map map = new HashMap();
        map.put("id", info.getId());
        map.put("status", status);
        YHttp.obtain().post(Constant.URL_AUDIT_USER, map, new HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean bean) {
                getData();
            }

            @Override
            public void onFailed(String error) {

            }
        });*/

    }

    private void getData() {
        mPresenter.getAllInvites(mGroupInfo.getId());
        /*Map paramsMap = new HashMap();
        paramsMap.put("page", 1);
        paramsMap.put("size", 2000);
        paramsMap.put("group_id", mGroupInfo.getId());
        YHttp.obtain().post(Constant.URL_INVITATION_LIST, paramsMap, new HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean bean) {
                if (bean.getData() == null) {
                    return;
                }
                List<GroupInvitInfo> data = JSON.parseArray(bean.getData().toString(), GroupInvitInfo.class);
//                mAdapter.setNewInstance(data);
                if (data != null && !data.isEmpty()) {
                    mAdapter.setList(data);
                } else {
                    mAdapter.setEmptyView(R.layout.adapter_item_empty);
                }
            }

            @Override
            public void onFailed(String error) {

            }
        });*/

    }


    @Override
    public void onInvitesReceive(CheckInvitesData data) {
        if (data.getData() == null) {
            return;
        }
        List<CheckInvitesData.DataDTO> list = data.getData();
        List<GroupInvitInfo> invites = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            GroupInvitInfo inviteInfo = new GroupInvitInfo();
            inviteInfo.covertGroupInvites(list.get(i));
            invites.add(inviteInfo);
        }
//                mAdapter.setNewInstance(data);
        if (invites != null && !invites.isEmpty()) {
            mAdapter.setList(invites);
        } else {
            mAdapter.setEmptyView(R.layout.adapter_item_empty);
        }
    }

    @Override
    public void onSuccess(HttpResult data) {
        DialogMaker.dismissProgressDialog();
        getData();
    }

    @Override
    public void onFail(String msg, int code) {
        showToast(msg);
        if (code == 10001) {
            MyApplication.getInstance().loginAgain();
        }
    }

    @Override
    public void setPresenter(GroupContract.ICheckInvitesPresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return getActivity();
    }
}
