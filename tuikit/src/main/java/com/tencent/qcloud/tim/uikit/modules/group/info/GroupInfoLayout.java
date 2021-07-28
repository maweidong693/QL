   package com.tencent.qcloud.tim.uikit.modules.group.info;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.tencent.common.Constant;
import com.tencent.common.dialog.DialogMaker;
import com.tencent.common.http.BaseBean;
import com.tencent.common.http.HttpCallBack;
import com.tencent.common.http.YHttp;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMGroupInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.component.LineControllerView;
import com.tencent.qcloud.tim.uikit.component.SelectionActivity;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.dialog.TUIKitDialog;
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.GlideEngine;
import com.tencent.qcloud.tim.uikit.modules.group.apply.GroupInvitationFragment;
import com.tencent.qcloud.tim.uikit.modules.group.interfaces.IGroupMemberLayout;
import com.tencent.qcloud.tim.uikit.modules.group.member.GroupMemberInfo;
import com.tencent.qcloud.tim.uikit.modules.group.member.IGroupMemberRouter;
import com.tencent.qcloud.tim.uikit.utils.CustomInfo;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;


public class GroupInfoLayout extends LinearLayout implements IGroupMemberLayout, View.OnClickListener {

    private static final String TAG = GroupInfoLayout.class.getSimpleName();
    private TitleBarLayout mTitleBar;
    private LineControllerView mMemberView;
    private GroupInfoAdapter mMemberAdapter;
    private IGroupMemberRouter mMemberPreviewListener;
    private LineControllerView mGroupTypeView;
    private LineControllerView mGroupIDView;
    private LineControllerView mGroupNameView;
    private LineControllerView mGroupIcon;
    private LineControllerView mGroupNotice;
    private LineControllerView mGroupOwnerTransfer;
    private LineControllerView mGroupInvitation;
    private LineControllerView mGroupManager;
    private LineControllerView mGroupMuteManager;
    private LineControllerView mNickView;
    private LineControllerView mJoinTypeView;
    private LineControllerView isWithoutReview;
    private LineControllerView isAddFriendView;
    private LineControllerView isMuteView;
    private LineControllerView mTopSwitchView;
    private LineControllerView mMuteGroupMemberSwitchView;
    private Button mDissolveBtn;

    private GroupInfo mGroupInfo;
    private GroupInfoPresenter mPresenter;
    private ArrayList<String> mJoinTypes = new ArrayList<>();
    private ArrayList<String> mAddFriends = new ArrayList<>();
    private ArrayList<String> mMutes = new ArrayList<>();
    private List<GroupMemberInfo> groupMemberInfos;

    public GroupInfoLayout(Context context) {
        super(context);
        init();
    }

    public GroupInfoLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GroupInfoLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.group_info_layout, this);
        // 标题
        mTitleBar = findViewById(R.id.group_info_title_bar);
        mTitleBar.getRightGroup().setVisibility(GONE);
        mTitleBar.setTitle(getResources().getString(R.string.group_detail), TitleBarLayout.POSITION.MIDDLE);
        mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) getContext()).finish();
            }
        });
        // 成员标题
        mMemberView = findViewById(R.id.group_member_bar);
        mMemberView.setOnClickListener(this);
        mMemberView.setCanNav(true);
        // 成员列表
        GridView memberList = findViewById(R.id.group_members);
        mMemberAdapter = new GroupInfoAdapter();
        memberList.setAdapter(mMemberAdapter);
        // 群类型，只读
        mGroupTypeView = findViewById(R.id.group_type_bar);
        // 群ID，只读
        mGroupIDView = findViewById(R.id.group_account);
        // 群聊名称
        mGroupNameView = findViewById(R.id.group_name);
        mGroupNameView.setOnClickListener(this);
        mGroupNameView.setCanNav(true);
        // 群头像
        mGroupIcon = findViewById(R.id.group_icon);
        mGroupIcon.setOnClickListener(this);
        mGroupIcon.setCanNav(true);
        // 群公告
        mGroupNotice = findViewById(R.id.group_notice);
        mGroupNotice.setOnClickListener(this);
        mGroupNotice.setCanNav(true);
        //群邀请审核
        mGroupInvitation = findViewById(R.id.group_invitation);
        mGroupInvitation.setOnClickListener(this);
        mGroupInvitation.setCanNav(true);
        //群主转让
        mGroupOwnerTransfer = findViewById(R.id.group_owner_transfer);
        mGroupOwnerTransfer.setOnClickListener(this);
        mGroupOwnerTransfer.setCanNav(true);
        //群管理
        mGroupManager = findViewById(R.id.group_manager);
        mGroupManager.setOnClickListener(this);
        mGroupManager.setCanNav(true);
        //禁言管理
        mGroupMuteManager = findViewById(R.id.group_mute_manager);
        mGroupMuteManager.setOnClickListener(this);
        mGroupMuteManager.setCanNav(true);
        // 加群方式
        mJoinTypeView = findViewById(R.id.join_type_bar);
        mJoinTypeView.setOnClickListener(this);
        mJoinTypeView.setCanNav(true);
        mJoinTypes.addAll(Arrays.asList(getResources().getStringArray(R.array.group_join_type)));
        //无需审核直接进群
        isWithoutReview = findViewById(R.id.is_without_review);
        //加好友方式
        isAddFriendView = findViewById(R.id.is_add_friend);
        mAddFriends.addAll(Arrays.asList(getResources().getStringArray(R.array.add_friends)));
        //设置禁言
        isMuteView = findViewById(R.id.is_mute);
        mMutes.addAll(Arrays.asList(getResources().getStringArray(R.array.is_mute)));
        // 群昵称
        mNickView = findViewById(R.id.self_nickname_bar);
        mNickView.setOnClickListener(this);
        mNickView.setCanNav(true);
        //是否禁言
//        mMuteGroupMemberSwitchView = findViewById(R.id.mute_group_member_switch);
//        mMuteGroupMemberSwitchView.setCheckListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                TUIKitLog.i(TAG, "onCheckedChanged:" + isChecked);
//                String groupIntroduction = mGroupInfo.getGroupIntroduction();
//                HashMap<String, String> hashMap = CustomInfo.InfoStrToMap(groupIntroduction);
//                hashMap.put(CustomInfo.IS_MUTE,isChecked?"1":"0");
//                String str = CustomInfo.InfoMapToStr(hashMap);
//                mPresenter.modifyGroupCustomInfo(str, TUIKitConstants.Group.MODIFY_GROUP_INTRODUCTION_);
//
//            }
//        });
        // 是否置顶
        mTopSwitchView = findViewById(R.id.chat_to_top_switch);
        mTopSwitchView.setCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPresenter.setTopConversation(isChecked);
            }
        });
        // 退群
        mDissolveBtn = findViewById(R.id.group_dissolve_button);
        mDissolveBtn.setOnClickListener(this);

        mPresenter = new GroupInfoPresenter(this);
    }

    @Override
    public void onClick(View v) {
        if (mGroupInfo == null) {
            TUIKitLog.e(TAG, "mGroupInfo is NULL");
            return;
        }
        if (v.getId() == R.id.group_member_bar) {
            if (mMemberPreviewListener != null) {
                mMemberPreviewListener.forwardListMember(mGroupInfo);
            }
        } else if (v.getId() == R.id.group_name) {
            Bundle bundle = new Bundle();
            bundle.putString(TUIKitConstants.Selection.TITLE, getResources().getString(R.string.modify_group_name));
            bundle.putString(TUIKitConstants.Selection.INIT_CONTENT, mGroupNameView.getContent());
            bundle.putInt(TUIKitConstants.Selection.LIMIT, 20);
            SelectionActivity.startTextSelection((Activity) getContext(), bundle, new SelectionActivity.OnResultReturnListener() {
                @Override
                public void onReturn(final Object text) {
                    mPresenter.modifyGroupName(text.toString());
                    mGroupNameView.setContent(text.toString());
                }
            });
        } else if (v.getId() == R.id.group_icon) {
            choosePhoto();
//            String groupUrl = String.format("https://picsum.photos/id/%d/200/200", new Random().nextInt(1000));
        } else if (v.getId() == R.id.group_notice) {
            Bundle bundle = new Bundle();
            bundle.putString(TUIKitConstants.Selection.TITLE, getResources().getString(R.string.modify_group_notice));
            bundle.putString(TUIKitConstants.Selection.INIT_CONTENT, mGroupNotice.getContent());
            bundle.putInt(TUIKitConstants.Selection.LIMIT, 200);
            SelectionActivity.startTextSelection((Activity) getContext(), bundle, new SelectionActivity.OnResultReturnListener() {
                @Override
                public void onReturn(final Object text) {
                    mPresenter.modifyGroupNotice(text.toString());
                    mGroupNotice.setContent(text.toString());
                }
            });
        }else if (v.getId()==R.id.group_invitation){
            if (mMemberPreviewListener != null) {
                mMemberPreviewListener.forwardGroupInvitation(mGroupInfo);
            }

        } else if (v.getId() == R.id.group_owner_transfer) {
            if (mMemberPreviewListener != null) {
                mMemberPreviewListener.forwardTransFerMember(mGroupInfo);
            }
        } else if (v.getId() == R.id.group_manager) {

            if (mMemberPreviewListener != null) {
                mMemberPreviewListener.forwardGroupManager(mGroupInfo);
            }

        } else if (v.getId() == R.id.group_mute_manager) {
            if (mMemberPreviewListener != null) {
                mMemberPreviewListener.forwardGroupMuteManager(mGroupInfo);
            }
        } else if (v.getId() == R.id.self_nickname_bar) {
            Bundle bundle = new Bundle();
            bundle.putString(TUIKitConstants.Selection.TITLE, getResources().getString(R.string.modify_nick_name_in_goup));
            bundle.putString(TUIKitConstants.Selection.INIT_CONTENT, mNickView.getContent());
            bundle.putInt(TUIKitConstants.Selection.LIMIT, 20);
            SelectionActivity.startTextSelection((Activity) getContext(), bundle, new SelectionActivity.OnResultReturnListener() {
                @Override
                public void onReturn(final Object text) {
                    mPresenter.modifyMyGroupNickname(text.toString());
                    mNickView.setContent(text.toString());
                }
            });
        } else if (v.getId() == R.id.join_type_bar) {
            if (mGroupTypeView.getContent().equals("聊天室")) {
                ToastUtil.toastLongMessage("加入聊天室为自动审批，暂不支持修改");
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString(TUIKitConstants.Selection.TITLE, getResources().getString(R.string.group_join_type));
            bundle.putStringArrayList(TUIKitConstants.Selection.LIST, mJoinTypes);
            bundle.putInt(TUIKitConstants.Selection.DEFAULT_SELECT_ITEM_INDEX, mGroupInfo.getJoinType());
            SelectionActivity.startListSelection((Activity) getContext(), bundle, new SelectionActivity.OnResultReturnListener() {
                @Override
                public void onReturn(final Object text) {
                    mPresenter.modifyGroupInfo((Integer) text, TUIKitConstants.Group.MODIFY_GROUP_JOIN_TYPE);
                    mJoinTypeView.setContent(mJoinTypes.get((Integer) text));

                }
            });
        }
//        else if (v.getId() == R.id.is_add_friend) {
//            Bundle bundle = new Bundle();
//            bundle.putString(TUIKitConstants.Selection.TITLE, getResources().getString(R.string.add_friend_type));
//            bundle.putStringArrayList(TUIKitConstants.Selection.LIST, mAddFriends);
//            String groupIntroduction = mGroupInfo.getGroupIntroduction();
//            final HashMap<String, Object> hashMap = CustomInfo.InfoStrToMap(groupIntroduction);
//            String addFriend = (String) hashMap.get(CustomInfo.IS_FRIEND);
//            bundle.putInt(TUIKitConstants.Selection.DEFAULT_SELECT_ITEM_INDEX, Integer.parseInt(addFriend));
//            SelectionActivity.startListSelection((Activity) getContext(), bundle, new SelectionActivity.OnResultReturnListener() {
//                @Override
//                public void onReturn(final Object text) {
//                    TUIKitLog.i(TAG, "onCheckedChanged:" + text);
//                    hashMap.put(CustomInfo.IS_FRIEND, text.toString());
//                    hashMap.put(CustomInfo.LAST_EDIT, CustomInfo.IS_FRIEND);
//                    String s = CustomInfo.InfoMapToStr(hashMap);
//                    mPresenter.modifyGroupCustomInfo(s, TUIKitConstants.Group.MODIFY_GROUP_INTRODUCTION_);
//                    isAddFriendView.setContent(mAddFriends.get((Integer) text));
//                }
//            });
//        }
//        else if (v.getId() == R.id.is_mute) {
//            Bundle bundle = new Bundle();
//            bundle.putString(TUIKitConstants.Selection.TITLE, getResources().getString(R.string.set_mute));
//            bundle.putStringArrayList(TUIKitConstants.Selection.LIST, mMutes);
//            String groupIntroduction = mGroupInfo.getGroupIntroduction();
//            final HashMap<String, Object> hashMap = CustomInfo.InfoStrToMap(groupIntroduction);
//            String addFriend = (String) hashMap.get(CustomInfo.IS_MUTE);
//            bundle.putInt(TUIKitConstants.Selection.DEFAULT_SELECT_ITEM_INDEX, Integer.parseInt(addFriend));
//            SelectionActivity.startListSelection((Activity) getContext(), bundle, new SelectionActivity.OnResultReturnListener() {
//                @Override
//                public void onReturn(final Object text) {
//                    TUIKitLog.i(TAG, "onCheckedChanged:" + text);
//                    hashMap.put(CustomInfo.IS_MUTE, text.toString());
//                    hashMap.put(CustomInfo.LAST_EDIT, CustomInfo.IS_MUTE);
//                    String s = CustomInfo.InfoMapToStr(hashMap);
//                    mPresenter.modifyGroupCustomInfo(s, TUIKitConstants.Group.MODIFY_GROUP_INTRODUCTION_);
//                    isMuteView.setContent(mMutes.get((Integer) text));
//                }
//            });
//        }
        else if (v.getId() == R.id.group_dissolve_button) {
            if (mGroupInfo.isOwner() &&
                    mGroupInfo.getGroupType().equals(TUIKitConstants.GroupType.TYPE_PUBLIC)
            ) {
                new TUIKitDialog(getContext())
                        .builder()
                        .setCancelable(true)
                        .setCancelOutside(true)
                        .setTitle("您确认解散该群?")
                        .setDialogWidth(0.75f)
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPresenter.deleteGroup();
                            }
                        })
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show();
            } else {
                new TUIKitDialog(getContext())
                        .builder()
                        .setCancelable(true)
                        .setCancelOutside(true)
                        .setTitle("您确认退出该群？")
                        .setDialogWidth(0.75f)
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPresenter.quitGroup();
                            }
                        })
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show();
            }
        }
    }

    public void setGroupId(String groupId) {
        Map auditCount =new HashMap();
        auditCount.put("group_id",groupId);
        YHttp.obtain().post(Constant.URL_AUDIT_COUNT, auditCount, new HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (baseBean.getData() != null) {
                    try {
                        JSONObject jsonObject = JSON.parseObject(baseBean.getData().toString(), JSONObject.class);
                        int count = jsonObject.getIntValue("count");
                        if (count>0){
                            mGroupInvitation.setUnreadCount(count+"");
                        }else {
                            mGroupInvitation.setUnreadclear();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    mGroupInvitation.setUnreadclear();
                }
            }

            @Override
            public void onFailed(String error) {

            }
        });

        Map map = new HashMap();
        map.put("GroupId", groupId);
        /*YHttp.obtain().post(Constant.URL_GET_GROUP_USER_SORT, map, new HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                if (baseBean.getData() != null) {
                    groupMemberInfos = JSON.parseArray(baseBean.getData().toString(), GroupMemberInfo.class);
                    if (groupMemberInfos != null && groupMemberInfos.size() > 0) {
                        if (mGroupInfo != null && mGroupInfo.getMemberDetails() != null) {
                            mGroupInfo.setMemberDetails(groupMemberInfos);
                            mMemberAdapter.setDataSource(mGroupInfo);
                        }
                    }
                }
            }

            @Override
            public void onFailed(String error) {

            }
        });*/
        mPresenter.loadGroupInfo(groupId, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                setGroupInfo((GroupInfo) data);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

            }
        });
    }

    private void setGroupInfo(GroupInfo info) {
        if (info == null) {
            return;
        }
        this.mGroupInfo = info;
        if (groupMemberInfos != null && groupMemberInfos.size() > 0) {
            mGroupInfo.setMemberDetails(groupMemberInfos);
            mMemberAdapter.setDataSource(mGroupInfo);
        }
        mGroupNameView.setContent(info.getGroupName());
        mGroupIDView.setContent(info.getId());
        mGroupNotice.setContent(info.getNotice());
        mMemberView.setContent(info.getMemberCount() + "人");
//        String content = convertGroupText(info.getGroupType());
//        mGroupTypeView.setContent(content);
        mJoinTypeView.setContent(mJoinTypes.get(info.getJoinType()));
        mNickView.setContent(mPresenter.getNickName());
        mTopSwitchView.setChecked(mGroupInfo.isTopChat());
        String groupIntroduction = mGroupInfo.getGroupIntroduction();
        HashMap<String, Object> hashMap = CustomInfo.InfoStrToMap(groupIntroduction);
        String addFriend = (String) hashMap.get(CustomInfo.IS_FRIEND);
        String mute = (String) hashMap.get(CustomInfo.IS_MUTE);
        String addOpt = (String) hashMap.get(CustomInfo.GROUP_ADD_OPT);
//        mMuteGroupMemberSwitchView.setChecked(mute.equals("1"));
//        isAddFriendView.setContent(mAddFriends.get(Integer.parseInt(addFriend)));
        if (TextUtils.isEmpty(addOpt)){
            isWithoutReview.setChecked(false);
        }else {
            isWithoutReview.setChecked(TextUtils.equals("0",addOpt)?false:true);
        }
        isWithoutReview.setCheckListener((buttonView, isChecked) -> {
            if (!buttonView.isPressed()) {
                return;
            }
            if (mGroupInfo==null) {
                return;
            }
            HashMap<String, Object> FriendMap = CustomInfo.InfoStrToMap(mGroupInfo.getGroupIntroduction());
            FriendMap.put(CustomInfo.GROUP_ADD_OPT, isChecked?"1":"0");
            FriendMap.put(CustomInfo.LAST_EDIT, CustomInfo.GROUP_ADD_OPT);
            String s = CustomInfo.InfoMapToStr(FriendMap);
            mPresenter.modifyGroupCustomInfo(s, TUIKitConstants.Group.MODIFY_GROUP_INTRODUCTION_);

        });
        isAddFriendView.setChecked(addFriend.equals("0")?true:false);
        isAddFriendView.setCheckListener((buttonView, isChecked) -> {
            if (!buttonView.isPressed()) {
                return;
            }
            if (mGroupInfo==null) {
                return;
            }
            HashMap<String, Object> FriendMap = CustomInfo.InfoStrToMap(mGroupInfo.getGroupIntroduction());
            FriendMap.put(CustomInfo.IS_FRIEND, isChecked?"0":"1");
            FriendMap.put(CustomInfo.LAST_EDIT, CustomInfo.IS_FRIEND);
            String s = CustomInfo.InfoMapToStr(FriendMap);
            mPresenter.modifyGroupCustomInfo(s, TUIKitConstants.Group.MODIFY_GROUP_INTRODUCTION_);

        });
        isMuteView.setChecked(mute.equals("0")?false:true);
        isMuteView.setCheckListener((buttonView, isChecked) -> {
            if (!buttonView.isPressed()) {
                return;
            }
            if (mGroupInfo==null) {
                return;
            }
            HashMap<String, Object> MuteMap = CustomInfo.InfoStrToMap(mGroupInfo.getGroupIntroduction());
            MuteMap.put(CustomInfo.IS_MUTE, isChecked?"1":"0");
            MuteMap.put(CustomInfo.LAST_EDIT, CustomInfo.IS_MUTE);
            String s = CustomInfo.InfoMapToStr(MuteMap);
            mPresenter.modifyGroupCustomInfo(s, TUIKitConstants.Group.MODIFY_GROUP_INTRODUCTION_);

        });

//        isMuteView.setContent(mMutes.get(Integer.parseInt(mute)));
        mDissolveBtn.setText(R.string.dissolve);
        if (mGroupInfo.isOwner() || mGroupInfo.isManager()) {
            mGroupNotice.setVisibility(VISIBLE);
            mGroupNameView.setVisibility(VISIBLE);
            mGroupIcon.setVisibility(VISIBLE);
            isWithoutReview.setVisibility(VISIBLE);
            isAddFriendView.setVisibility(VISIBLE);
            isMuteView.setVisibility(VISIBLE);
            mGroupInvitation.setVisibility(VISIBLE);
            mGroupManager.setVisibility(mGroupInfo.isOwner() ? VISIBLE : GONE);
            mGroupOwnerTransfer.setVisibility(mGroupInfo.isOwner() ? VISIBLE : GONE);
            mGroupMuteManager.setVisibility(mGroupInfo.isOwner() ? VISIBLE : GONE);

//            if (TextUtils.equals(content, "讨论组")) {
//                mJoinTypeView.setVisibility(GONE);
//            } else {
//                mJoinTypeView.setVisibility(VISIBLE);
//            }
            if (mGroupInfo.getGroupType().equals(TUIKitConstants.GroupType.TYPE_WORK)
                    || mGroupInfo.getGroupType().equals(TUIKitConstants.GroupType.TYPE_PRIVATE)) {
                mDissolveBtn.setText(R.string.exit_group);
            }
        } else {
            mGroupNameView.setVisibility(GONE);
            mGroupManager.setVisibility(GONE);
            isAddFriendView.setVisibility(GONE);
            isWithoutReview.setVisibility(GONE);
            mGroupMuteManager.setVisibility(GONE);
            mGroupInvitation.setVisibility(GONE);
            mGroupIcon.setVisibility(GONE);
            isMuteView.setVisibility(GONE);
            mGroupNotice.setVisibility(GONE);
            mGroupNotice.setVisibility(GONE);
            mJoinTypeView.setVisibility(GONE);
            mGroupOwnerTransfer.setVisibility(GONE);
            mDissolveBtn.setText(R.string.exit_group);
        }
    }

    private String convertGroupText(String groupType) {
        String groupText = "";
        if (TextUtils.isEmpty(groupType)) {
            return groupText;
        }
        if (TextUtils.equals(groupType, TUIKitConstants.GroupType.TYPE_PRIVATE)
                || TextUtils.equals(groupType, TUIKitConstants.GroupType.TYPE_WORK)) {
            groupText = "讨论组";
        } else if (TextUtils.equals(groupType, TUIKitConstants.GroupType.TYPE_PUBLIC)) {
            groupText = "公开群";
        } else if (TextUtils.equals(groupType, TUIKitConstants.GroupType.TYPE_CHAT_ROOM)
                || TextUtils.equals(groupType, TUIKitConstants.GroupType.TYPE_MEETING)) {
            groupText = "聊天室";
        }
        return groupText;
    }

    public void onGroupInfoModified(Object value, int type) {
        switch (type) {
            case TUIKitConstants.Group.MODIFY_GROUP_NAME:
                ToastUtil.toastLongMessage(getResources().getString(R.string.modify_group_name_success));
                mGroupNameView.setContent(value.toString());
                break;
            case TUIKitConstants.Group.MODIFY_GROUP_NOTICE:
                mGroupNotice.setContent(value.toString());
                ToastUtil.toastLongMessage(getResources().getString(R.string.modify_group_notice_success));
                break;
            case TUIKitConstants.Group.MODIFY_GROUP_JOIN_TYPE:
                mJoinTypeView.setContent(mJoinTypes.get((Integer) value));
                break;
            case TUIKitConstants.Group.MODIFY_MEMBER_NAME:
                ToastUtil.toastLongMessage(getResources().getString(R.string.modify_nickname_success));
                mNickView.setContent(value.toString());
                break;
        }
    }

    public void setRouter(IGroupMemberRouter listener) {
        mMemberPreviewListener = listener;
        mMemberAdapter.setManagerCallBack(listener);
    }

    @Override
    public void setDataSource(GroupInfo dataSource) {

    }

    @Override
    public TitleBarLayout getTitleBar() {
        return mTitleBar;
    }

    @Override
    public void setParentLayout(Object parent) {

    }

    //选择图片
    private void choosePhoto() {
        PictureSelector.create((Activity) getContext())
                .openGallery(PictureMimeType.ofImage())
                .selectionMode(PictureConfig.SINGLE)
                .isGif(false)
                .isEnableCrop(true)
                .withAspectRatio(1, 1)
                .showCropFrame(true)
                .showCropGrid(false)
                .isCompress(true)
                .minimumCompressSize(1000)
                .imageEngine(GlideEngine.createGlideEngine())
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        if (result == null || result.isEmpty()) {
                            return;
                        }
                        DialogMaker.showProgressDialog(getContext(), null, "正在上传", true, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                            }
                        }).setCanceledOnTouchOutside(false);
                        // 结果回调
                        Map map = new HashMap();
                        map.put("image", new File(result.get(0).getCutPath()));
                        YHttp.obtain().post(Constant.URL_UPLOAD, map, new HttpCallBack<BaseBean>() {
                            @Override
                            public void onSuccess(BaseBean bean) {
                                if (bean.getData() == null) {
                                    return;
                                }
                                JSONObject object = JSON.parseObject(bean.getData().toString());
                                String groupUrl = object.getString("url");
                                V2TIMGroupInfo v2TIMGroupInfo = new V2TIMGroupInfo();
                                v2TIMGroupInfo.setGroupID(mGroupInfo.getId());
                                v2TIMGroupInfo.setFaceUrl(groupUrl);
                                V2TIMManager.getGroupManager().setGroupInfo(v2TIMGroupInfo, new V2TIMCallback() {
                                    @Override
                                    public void onError(int code, String desc) {
                                        TUIKitLog.e(TAG, "modify group icon failed, code:" + code + "|desc:" + desc);
                                        ToastUtil.toastLongMessage("修改群头像失败");
                                    }

                                    @Override
                                    public void onSuccess() {
                                        ToastUtil.toastLongMessage("修改群头像成功");
                                    }
                                });
                            }

                            @Override
                            public void onFailed(String error) {

                            }
                        });
                    }

                    @Override
                    public void onCancel() {
                        // 取消
                    }
                });

    }

}
