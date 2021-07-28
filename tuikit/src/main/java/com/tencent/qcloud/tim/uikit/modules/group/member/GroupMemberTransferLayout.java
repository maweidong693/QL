package com.tencent.qcloud.tim.uikit.modules.group.member;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMGroupInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.liteav.login.UserModel;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.base.BaseFragment;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.modules.contact.GroupMemberListView;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfo;
import com.tencent.qcloud.tim.uikit.modules.group.info.InfoData;
import com.tencent.qcloud.tim.uikit.modules.group.interfaces.IGroupMemberLayout;
import com.tencent.qcloud.tim.uikit.utils.CustomInfo;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;

public class GroupMemberTransferLayout extends LinearLayout implements IGroupMemberLayout {

    private static final String TAG = GroupMemberTransferLayout.class.getSimpleName();

    private TitleBarLayout mTitleBar;
    private GroupMemberListView mContactListView;
    private List<String> mInviteMembers = new ArrayList<>();
    private Object mParentLayout;
    private GroupInfo mGroupInfo;
    private int mType;  //1选择新群主   2管理员  3选择联系人(发送名片)

    public GroupMemberTransferLayout(Context context) {
        super(context);
        init();
    }

    public GroupMemberTransferLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GroupMemberTransferLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.group_member_transfer_layout, this);
        mTitleBar = findViewById(R.id.group_invite_title_bar);
        mTitleBar.setTitle("确定", TitleBarLayout.POSITION.RIGHT);
//        mTitleBar.setTitle("选择新群主", TitleBarLayout.POSITION.MIDDLE);
//        mTitleBar.getRightTitle().setTextColor(Color.BLUE);
        mTitleBar.getRightIcon().setVisibility(View.GONE);
        mTitleBar.setOnRightClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mType==1){
                    if (mInviteMembers.size()==0){
                        return;
                    }
                    String introduction = mGroupInfo.getGroupIntroduction();
                    HashMap<String, Object> hashMap = CustomInfo.InfoStrToMap(introduction);
                    List<String> list = (List<String>) hashMap.get(CustomInfo.GROUP_MANAGEMENT);
                    if ((list!=null&&list.size()>0)&&list.contains(mInviteMembers.get(0))){
                            list.remove(mInviteMembers.get(0));
                            hashMap.put(CustomInfo.GROUP_MANAGEMENT,list);
                            hashMap.put(CustomInfo.LAST_EDIT,CustomInfo.GROUP_MANAGEMENT);
                            String s = CustomInfo.InfoMapToStr(hashMap);
                            V2TIMGroupInfo v2TIMGroupInfo = new V2TIMGroupInfo();
                            v2TIMGroupInfo.setGroupID(mGroupInfo.getId());
                            v2TIMGroupInfo.setIntroduction(s);
                            V2TIMManager.getGroupManager().setGroupInfo(v2TIMGroupInfo, new V2TIMCallback() {
                                @Override
                                public void onError(int code, String desc) {

                                }

                                @Override
                                public void onSuccess() {
                                    V2TIMManager.getGroupManager().transferGroupOwner(mGroupInfo.getId(), mInviteMembers.get(0), new V2TIMCallback() {
                                        @Override
                                        public void onError(int i, String s) {
                                            ToastUtil.toastLongMessage("转让群主失败" );
                                        }

                                        @Override
                                        public void onSuccess() {
                                            ToastUtil.toastLongMessage("转让群主成功");
                                            mInviteMembers.clear();
                                            finish();
                                        }
                                    });
                                }
                            });

                    }else {
                        V2TIMManager.getGroupManager().transferGroupOwner(mGroupInfo.getId(), mInviteMembers.get(0), new V2TIMCallback() {
                            @Override
                            public void onError(int i, String s) {
                                ToastUtil.toastLongMessage("转让群主失败" );
                            }

                            @Override
                            public void onSuccess() {
                                ToastUtil.toastLongMessage("转让群主成功");
                                mInviteMembers.clear();
                                finish();
                            }
                        });
                    }
                }else if (mType==2){
                    String introduction = mGroupInfo.getGroupIntroduction();
                    HashMap<String, Object> hashMap = CustomInfo.InfoStrToMap(introduction);
                    hashMap.put(CustomInfo.GROUP_MANAGEMENT,mInviteMembers);
                    hashMap.put(CustomInfo.LAST_EDIT,CustomInfo.GROUP_MANAGEMENT);
                    String s = CustomInfo.InfoMapToStr(hashMap);
                    V2TIMGroupInfo v2TIMGroupInfo = new V2TIMGroupInfo();
                    v2TIMGroupInfo.setGroupID(mGroupInfo.getId());
                    v2TIMGroupInfo.setIntroduction(s);
                    V2TIMManager.getGroupManager().setGroupInfo(v2TIMGroupInfo, new V2TIMCallback() {
                        @Override
                        public void onError(int code, String desc) {
                            ToastUtil.toastLongMessage("设置管理员失败" );
                            TUIKitLog.e(TAG, "modify group icon failed, code:" + code + "|desc:" + desc);
                        }

                        @Override
                        public void onSuccess() {
                            ToastUtil.toastLongMessage(mInviteMembers.size()==0?"取消管理员":"设置管理员成功");
                            mInviteMembers.clear();
                            finish();
                        }
                    });
                }else if (mType==3){
                    if (mInviteMembers.size()==0){
                        return;
                    }
                    V2TIMManager.getInstance().getUsersInfo(mInviteMembers, new V2TIMValueCallback<List<V2TIMUserFullInfo>>() {
                        @Override
                        public void onError(int code, String desc) {

                        }

                        @Override
                        public void onSuccess(List<V2TIMUserFullInfo> v2TIMUserFullInfos) {
                            if (v2TIMUserFullInfos == null || v2TIMUserFullInfos.size() == 0) {
                                TUIKitLog.e(TAG, "getUserInfoByUserId failed, v2TIMUserFullInfos is empty");
                                return;
                            }
                            V2TIMUserFullInfo userFullInfo = v2TIMUserFullInfos.get(0);
                            UserModel userModel = new UserModel();
                            userModel.userId = userFullInfo.getUserID();
                            userModel.userName = userFullInfo.getNickName();
                            userModel.userAvatar = userFullInfo.getFaceUrl();
                            finish();
                            EventBus.getDefault().post(userModel);
                        }
                    });
                }
            }
        });
        mContactListView = findViewById(R.id.group_invite_member_list);
//        mContactListView.loadDataSource(GroupMemberListView.DataSource.GROUP_MEMBER_LIST);

        mContactListView.setOnSelectChangeListener(new GroupMemberListView.OnSelectChangedListener() {
            @Override
            public void onSelectChanged(ContactItemBean contact, boolean selected) {
                if (selected) {
                    if (!mInviteMembers.contains(contact.getId())){
                        mInviteMembers.add(contact.getId());
                    }
                } else {
                    mInviteMembers.remove(contact.getId());
                }
                if (mContactListView.getAdapter().getSingleSelectMode()){
                    mTitleBar.setTitle("确定", TitleBarLayout.POSITION.RIGHT);
                }else {
                    if (mInviteMembers.size() > 0) {
                        mTitleBar.setTitle("确定（" + mInviteMembers.size() + "）", TitleBarLayout.POSITION.RIGHT);
                    } else {
                        mTitleBar.setTitle("确定", TitleBarLayout.POSITION.RIGHT);
                    }
                }
            }
        });
    }

    public void setDataSource(GroupInfo groupInfo) {
        mGroupInfo = groupInfo;
        if (mType==2){
            String introduction = mGroupInfo.getGroupIntroduction();
            HashMap<String, Object> hashMap = CustomInfo.InfoStrToMap(introduction);
            List<String> list = (List<String>) hashMap.get(CustomInfo.GROUP_MANAGEMENT);
            if (list!=null&&list.size()>0){
                List<GroupMemberInfo> memberDetails = groupInfo.getMemberDetails();
                for (GroupMemberInfo memberDetail : memberDetails) {
                    for (String s : list) {
                        if (s.equals(memberDetail.getAccount())){
                            mInviteMembers.add(s);
                        }
                    }
                }
            }
        }
        if (mContactListView != null) {
            mContactListView.setGroupInfo(mGroupInfo);
            mContactListView.loadDataSource(mType==3?GroupMemberListView.DataSource.FRIEND_LIST:GroupMemberListView.DataSource.GROUP_MEMBER_LIST);
        }
    }

    public TitleBarLayout getTitleBar() {
        return mTitleBar;
    }

    @Override
    public void setParentLayout(Object parent) {
        mParentLayout = parent;
    }

    private void finish() {
        if (mParentLayout instanceof Activity) {
            ((Activity) mParentLayout).finish();
        } else if (mParentLayout instanceof BaseFragment) {
            ((BaseFragment) mParentLayout).backward();
        }
    }

    public void initTitle(InfoData infoData) {
        if (infoData!=null){
            String title = infoData.getTitle();
            mType = infoData.getType();
            mContactListView.setSingleSelectMode(mType!=2);
            mContactListView.setType(mType);
            mTitleBar.setTitle(title, TitleBarLayout.POSITION.MIDDLE);
        }

    }
}
