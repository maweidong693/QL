package com.tencent.qcloud.tim.uikit.modules.group.member;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.tencent.common.http.ContactListData;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.base.BaseFragment;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.modules.contact.MessageForwardListView;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfo;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupMemberInfo;
import com.tencent.qcloud.tim.uikit.modules.group.interfaces.IGroupMemberLayout;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.utils.CustomInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageForwardLayout extends LinearLayout implements IGroupMemberLayout {

    private static final String TAG = MessageForwardLayout.class.getSimpleName();

    private TitleBarLayout mTitleBar;
    private MessageForwardListView mContactListView;
    private List<String> mInviteMembers = new ArrayList<>();
    private ContactItemBean contactItemBean ;
    private Object mParentLayout;
    private GroupInfo mGroupInfo;
    private MessageInfo msg ;
    private int mType;

    public MessageForwardLayout(Context context) {
        super(context);
        init();
    }

    public MessageForwardLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MessageForwardLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.message_forward_layout, this);
        mTitleBar = findViewById(R.id.group_invite_title_bar);
        mTitleBar.setTitle("确定", TitleBarLayout.POSITION.RIGHT);
        mTitleBar.setTitle("选择", TitleBarLayout.POSITION.LEFT);
//        mTitleBar.getRightTitle().setTextColor(Color.BLUE);
        mTitleBar.getRightIcon().setVisibility(View.GONE);
        mTitleBar.setOnRightClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInviteMembers.size()==0){
                    return;
                }
                finish();
                EventBus.getDefault().post(contactItemBean);

//                V2TIMManager.getInstance().getUsersInfo(mInviteMembers, new V2TIMValueCallback<List<V2TIMUserFullInfo>>() {
//                    @Override
//                    public void onError(int code, String desc) {
//
//                    }
//
//                    @Override
//                    public void onSuccess(List<V2TIMUserFullInfo> v2TIMUserFullInfos) {
//                        if (v2TIMUserFullInfos == null || v2TIMUserFullInfos.size() == 0) {
//                            TUIKitLog.e(TAG, "getUserInfoByUserId failed, v2TIMUserFullInfos is empty");
//                            return;
//                        }
//                        V2TIMUserFullInfo userFullInfo = v2TIMUserFullInfos.get(0);
//                        UserModel userModel = new UserModel();
//                        userModel.userId = userFullInfo.getUserID();
//                        userModel.userName = userFullInfo.getNickName();
//                        userModel.userAvatar = userFullInfo.getFaceUrl();
//                        finish();
//                        EventBus.getDefault().post(userModel);
//                    }
//                });
            }
        });
        mContactListView = findViewById(R.id.group_invite_member_list);
//        mContactListView.loadDataSource(GroupMemberListView.DataSource.GROUP_MEMBER_LIST);
        mContactListView.setSingleSelectMode(true);
        mContactListView.setOnSelectChangeListener(new MessageForwardListView.OnSelectChangedListener() {
            @Override
            public void onSelectChanged(ContactItemBean contact, boolean selected) {
                if (selected) {
                    if (!mInviteMembers.contains(contact.getId())){
                        contactItemBean = contact ;
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
            mContactListView.loadDataSource(MessageForwardListView.DataSource.FRIEND_LIST);
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

    public void setMsgSource(MessageInfo msg) {
        this.msg = msg ;
    }

    @Override
    public void setDataSource(GroupInfo dataSource, ContactListData.DataDTO list) {

    }
}
