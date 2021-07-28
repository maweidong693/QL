package com.tencent.qcloud.tim.uikit.modules.contact;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tencent.common.Constant;
import com.tencent.common.http.BaseBean;
import com.tencent.common.http.HttpCallBack;
import com.tencent.common.http.YHttp;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMFriendApplication;
import com.tencent.imsdk.v2.V2TIMFriendInfo;
import com.tencent.imsdk.v2.V2TIMFriendOperationResult;
import com.tencent.imsdk.v2.V2TIMGroupApplication;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.uikit.NewFriendBean;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.component.CircleImageView;
import com.tencent.qcloud.tim.uikit.component.LineControllerView;
import com.tencent.qcloud.tim.uikit.component.SelectionActivity;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.dialog.TUIKitDialog;
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.GlideEngine;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.tencent.qcloud.tim.uikit.modules.chat.GroupChatManagerKit;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;
import com.tencent.qcloud.tim.uikit.modules.group.apply.GroupApplyInfo;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfo;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfoProvider;
import com.tencent.qcloud.tim.uikit.utils.CustomInfo;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendProfileLayout extends LinearLayout implements View.OnClickListener {

    private static final String TAG = FriendProfileLayout.class.getSimpleName();

    private final int CHANGE_REMARK_CODE = 200;

    private TitleBarLayout mTitleBar;
    private CircleImageView mHeadImageView;
    private TextView mNickNameView;
    private LineControllerView mIDView;
    private LineControllerView mAddWordingView;
    private LineControllerView mRemarkView;
    private LineControllerView mAuthorityView;
    private LineControllerView mAddBlackView;
    private LineControllerView mChatTopView;
    private LinearLayout llCircleOfFriends;
    private TextView mDeleteView;
    private TextView mChatView;

    private ContactItemBean mContactInfo;
    private ChatInfo mChatInfo;
    private NewFriendBean.DataDTO mFriendApplication;
    private OnButtonClickListener mListener;
    private String mId;
    private String mNickname;
    private String mRemark;
    private String mAddWords;

    public FriendProfileLayout(Context context) {
        super(context);
        init();
    }

    public FriendProfileLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FriendProfileLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.contact_friend_profile_layout, this);

        mHeadImageView = findViewById(R.id.avatar);
        mNickNameView = findViewById(R.id.name);
        mIDView = findViewById(R.id.id);
        mAddWordingView = findViewById(R.id.add_wording);
        mAddWordingView.setCanNav(false);
        mAddWordingView.setSingleLine(false);
        mRemarkView = findViewById(R.id.remark);
        mRemarkView.setOnClickListener(this);
        mAuthorityView = findViewById(R.id.authority);
        mChatTopView = findViewById(R.id.chat_to_top);
        mAddBlackView = findViewById(R.id.blackList);
        llCircleOfFriends = findViewById(R.id.llCircleOfFriends);
        mDeleteView = findViewById(R.id.btnDel);
        mDeleteView.setOnClickListener(this);
        mChatView = findViewById(R.id.btnChat);
        mChatView.setOnClickListener(this);
        mTitleBar = findViewById(R.id.friend_titlebar);
        mTitleBar.setTitle(getResources().getString(R.string.profile_detail), TitleBarLayout.POSITION.MIDDLE);
        mTitleBar.getRightGroup().setVisibility(View.GONE);
        mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) getContext()).finish();
            }
        });
        mChatView.setVisibility(GONE);
        mRemarkView.setVisibility(GONE);
        mAddBlackView.setVisibility(GONE);
    }

    public void initData(Object data) {
        if (data instanceof ChatInfo) {
            mChatInfo = (ChatInfo) data;
            if (mChatInfo.getType() == V2TIMConversation.V2TIM_GROUP) {
                //聊天列表右上角图标，群头像
                mId = mChatInfo.getId();
                mChatTopView.setVisibility(GONE);
                String groupId = mChatInfo.getGroupId();
                GroupInfoProvider mProvider = new GroupInfoProvider();
                //查询群
                mProvider.loadGroupInfo(groupId, new IUIKitCallBack() {
                    @Override
                    public void onSuccess(Object data) {
                        GroupInfo groupInfo = (GroupInfo) data;
                        String groupIntroduction = groupInfo.getGroupIntroduction();
                        HashMap<String, Object> hashMap = CustomInfo.InfoStrToMap(groupIntroduction);
                        String addFriend = (String) hashMap.get(CustomInfo.IS_FRIEND);
                        if (addFriend.equals("1")) {
                            //禁止加好友 1
                            loadUserProfile(1);

                        } else {
                            //允许加好友 0
                            loadUserProfile(0);
                        }


                    }

                    @Override
                    public void onError(String module, int errCode, String errMsg) {

                    }
                });
            } else {
                //聊天列表右上角图标(私聊图标)
                mId = mChatInfo.getId();
                mChatTopView.setVisibility(TUIKitConfigs.getConfigs().getGeneralConfig().getUserId().equals(mId) ? View.GONE : View.VISIBLE);
                mChatTopView.setChecked(ConversationManagerKit.getInstance().isTopConversation(mId));
                mChatTopView.setCheckListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        ConversationManagerKit.getInstance().setConversationTop(mId, isChecked);
                    }
                });
                loadUserProfile(0);
            }
            return;
        } else if (data instanceof ContactItemBean) {
            //好友列表
            mChatView.setVisibility(VISIBLE);
            mRemarkView.setVisibility(VISIBLE);
            mAddBlackView.setVisibility(VISIBLE);
            llCircleOfFriends.setVisibility(VISIBLE);
            mAuthorityView.setVisibility(VISIBLE);
            mContactInfo = (ContactItemBean) data;
            mId = mContactInfo.getId();
            mNickname = mContactInfo.getNickname();
            mRemarkView.setContent(mContactInfo.getRemark());
            mAddBlackView.setChecked(mContactInfo.isBlackList());
            mAddBlackView.setCheckListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        addBlack();
                    } else {
                        deleteBlack();
                    }
                }
            });
            if (!TextUtils.isEmpty(mContactInfo.getAvatarurl())) {
                GlideEngine.loadCornerImage(mHeadImageView, mContactInfo.getAvatarurl(), null, 10);
//                GlideEngine.loadImage(mHeadImageView, Uri.parse(mContactInfo.getAvatarurl()));
            }
        } else if (data instanceof NewFriendBean.DataDTO) {
            mFriendApplication = (NewFriendBean.DataDTO) data;
            int type = mFriendApplication.getStatus();
            switch (type) {
                case 0:
                    mChatView.setVisibility(VISIBLE);
                    mDeleteView.setVisibility(VISIBLE);
                    break;
                case V2TIMFriendApplication.V2TIM_FRIEND_APPLICATION_SEND_OUT:
                case V2TIMFriendApplication.V2TIM_FRIEND_APPLICATION_BOTH:
                    mChatView.setVisibility(GONE);
                    mDeleteView.setVisibility(GONE);
                    break;
                default:
                    mChatView.setVisibility(GONE);
                    mDeleteView.setVisibility(GONE);
                    break;
            }
            mId = mFriendApplication.getUser_id();
            mNickname = mFriendApplication.getUser_nick();
            mAddWordingView.setVisibility(View.VISIBLE);
            mAddWordingView.setContent(mFriendApplication.getRemark());
            mRemarkView.setVisibility(GONE);
            if (TextUtils.isEmpty(mFriendApplication.getFace_url())) {
                mHeadImageView.setImageResource(R.drawable.ic_personal_member);
            } else {
                Glide.with(getContext()).load(mFriendApplication.getFace_url()).into(mHeadImageView);
            }
            mAddBlackView.setVisibility(GONE);
            mDeleteView.setText(R.string.refuse);
            mDeleteView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    refuse(mFriendApplication.getId());
                }
            });
            mChatView.setText(R.string.accept);
            mChatView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    accept(mFriendApplication.getId());
                }
            });
        } else if (data instanceof GroupApplyInfo) {
            final GroupApplyInfo info = (GroupApplyInfo) data;
            V2TIMGroupApplication item = ((GroupApplyInfo) data).getGroupApplication();
            mId = item.getFromUser();
            mNickname = item.getFromUserNickName();
            mAddWordingView.setVisibility(View.VISIBLE);
            mAddWordingView.setContent(item.getRequestMsg());
            mRemarkView.setVisibility(GONE);
            mAddBlackView.setVisibility(GONE);
            mDeleteView.setText(R.string.refuse);
            mDeleteView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    refuseApply(info);
                }
            });
            mChatView.setText(R.string.accept);
            mChatView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptApply(info);
                }
            });
        }

        if (!TextUtils.isEmpty(mNickname)) {
            mNickNameView.setText(mNickname);
        } else {
            mNickNameView.setText(mId);
        }
        mIDView.setContent(mId);
    }

    private void updateViews(ContactItemBean bean, int allow) {
        //1 禁止
        mContactInfo = bean;
//        mChatTopView.setVisibility(allow==1?View.GONE:View.VISIBLE);
        mChatTopView.setVisibility(allow == 1 ? View.GONE : TUIKitConfigs.getConfigs().getGeneralConfig().getUserId().equals(mId) ? View.GONE : View.VISIBLE);
        boolean top = ConversationManagerKit.getInstance().isTopConversation(mId);
        if (mChatTopView.isChecked() != top) {
            mChatTopView.setChecked(top);
        }
        mChatTopView.setCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ConversationManagerKit.getInstance().setConversationTop(mId, isChecked);
            }
        });
        mId = bean.getId();
        mNickname = bean.getNickname();
        if (bean.isFriend()) {
            llCircleOfFriends.setVisibility(VISIBLE);
            mAuthorityView.setVisibility(VISIBLE);
            mChatView.setVisibility(VISIBLE);
            mRemarkView.setVisibility(VISIBLE);
            mRemarkView.setContent(bean.getRemark());
            mAddBlackView.setVisibility(VISIBLE);
            mAddBlackView.setChecked(bean.isBlackList());
            mAddBlackView.setCheckListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        addBlack();
                    } else {
                        deleteBlack();
                    }
                }
            });
            mDeleteView.setVisibility(VISIBLE);

            if (!TextUtils.isEmpty(mNickname)) {
                mNickNameView.setText(mNickname);
            } else {
                mNickNameView.setText(mId);
            }
            mIDView.setContent(mId);
        } else {
            //这里包括自己本人,不是好友设置朋友权限隐藏，不然报错
//            mAuthorityView.setVisibility(TUIKitConfigs.getConfigs().getGeneralConfig().getUserId().equals(mId)?View.GONE:allow==0?View.VISIBLE:View.GONE);
            mAuthorityView.setVisibility(GONE);
            mChatView.setVisibility(GONE);
            mRemarkView.setVisibility(GONE);
            mAddBlackView.setVisibility(GONE);
            mDeleteView.setVisibility(GONE);

            if (allow == 0) {
                //不是我的好友，但是设置允许加好友
                if (!TextUtils.isEmpty(mNickname)) {
                    mNickNameView.setText(mNickname);
                } else {
                    mNickNameView.setText(mId);
                }
                llCircleOfFriends.setVisibility(VISIBLE);
                mIDView.setContent(mId);
            } else {
                //信息不展示
                mChatView.setVisibility(GONE);

            }
        }

        if (!TextUtils.isEmpty(bean.getAvatarurl())) {
            GlideEngine.loadCornerImage(mHeadImageView, bean.getAvatarurl(), null, 10);
//            GlideEngine.loadImage(mHeadImageView, Uri.parse(bean.getAvatarurl()));
        }
    }

    private void loadUserProfile(final int allow) {
        ArrayList<String> list = new ArrayList<>();
        list.add(mId);
        final ContactItemBean bean = new ContactItemBean();
        bean.setFriend(false);

        V2TIMManager.getInstance().getUsersInfo(list, new V2TIMValueCallback<List<V2TIMUserFullInfo>>() {
            @Override
            public void onError(int code, String desc) {
                TUIKitLog.e(TAG, "loadUserProfile err code = " + code + ", desc = " + desc);
                ToastUtil.toastShortMessage("Error code = " + code + ", desc = " + desc);
            }

            @Override
            public void onSuccess(List<V2TIMUserFullInfo> v2TIMUserFullInfos) {
                if (v2TIMUserFullInfos == null || v2TIMUserFullInfos.size() != 1) {
                    return;
                }
                final V2TIMUserFullInfo timUserFullInfo = v2TIMUserFullInfos.get(0);
                bean.setNickname(timUserFullInfo.getNickName());
                bean.setId(timUserFullInfo.getUserID());
                bean.setAvatarurl(timUserFullInfo.getFaceUrl());
                updateViews(bean, allow);
            }
        });

        V2TIMManager.getFriendshipManager().getBlackList(new V2TIMValueCallback<List<V2TIMFriendInfo>>() {
            @Override
            public void onError(int code, String desc) {
                TUIKitLog.e(TAG, "getBlackList err code = " + code + ", desc = " + desc);
                ToastUtil.toastShortMessage("Error code = " + code + ", desc = " + desc);
            }

            @Override
            public void onSuccess(List<V2TIMFriendInfo> v2TIMFriendInfos) {
                if (v2TIMFriendInfos != null && v2TIMFriendInfos.size() > 0) {
                    for (V2TIMFriendInfo friendInfo : v2TIMFriendInfos) {
                        if (TextUtils.equals(friendInfo.getUserID(), mId)) {
                            bean.setBlackList(true);
                            updateViews(bean, allow);
                            break;
                        }
                    }
                }
            }
        });

        V2TIMManager.getFriendshipManager().getFriendList(new V2TIMValueCallback<List<V2TIMFriendInfo>>() {
            @Override
            public void onError(int code, String desc) {
                TUIKitLog.e(TAG, "getFriendList err code = " + code + ", desc = " + desc);
                ToastUtil.toastShortMessage("Error code = " + code + ", desc = " + desc);
            }

            @Override
            public void onSuccess(List<V2TIMFriendInfo> v2TIMFriendInfos) {
                if (v2TIMFriendInfos != null && v2TIMFriendInfos.size() > 0) {
                    for (V2TIMFriendInfo friendInfo : v2TIMFriendInfos) {
                        if (TextUtils.equals(friendInfo.getUserID(), mId)) {
                            bean.setFriend(true);
                            bean.setRemark(friendInfo.getFriendRemark());
                            bean.setAvatarurl(friendInfo.getUserProfile().getFaceUrl());
                            break;
                        }
                    }
                }
                updateViews(bean, allow);
            }
        });
    }

    private void accept(int id) {
        Map map = new HashMap();
        map.put("id", id);
        map.put("status", 1);
        YHttp.obtain().post(Constant.URL_ADD_FRIEND_STATUS, map, new HttpCallBack<BaseBean>() {

            @Override
            public void onSuccess(BaseBean baseBean) {
                TUIKitLog.i(TAG, "accept success");
                mChatView.setText(R.string.accepted);
                ((Activity) getContext()).finish();
            }

            @Override
            public void onFailed(String error) {

            }
        });

    }

    private void refuse(int id) {

        Map map = new HashMap();
        map.put("id", id);
        map.put("status", 2);
        YHttp.obtain().post(Constant.URL_ADD_FRIEND_STATUS, map, new HttpCallBack<BaseBean>() {

            @Override
            public void onSuccess(BaseBean baseBean) {
                TUIKitLog.i(TAG, "refuse success");
                mDeleteView.setText(R.string.refused);
                ((Activity) getContext()).finish();
            }

            @Override
            public void onFailed(String error) {

            }
        });


    }

    public void acceptApply(final GroupApplyInfo item) {
        GroupChatManagerKit.getInstance().getProvider().acceptApply(item, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                Intent intent = new Intent();
                intent.putExtra(TUIKitConstants.Group.MEMBER_APPLY, item);
                ((Activity) getContext()).setResult(Activity.RESULT_OK, intent);
                ((Activity) getContext()).finish();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ToastUtil.toastLongMessage(errMsg);
            }
        });
    }

    public void refuseApply(final GroupApplyInfo item) {
        GroupChatManagerKit.getInstance().getProvider().refuseApply(item, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                Intent intent = new Intent();
                intent.putExtra(TUIKitConstants.Group.MEMBER_APPLY, item);
                ((Activity) getContext()).setResult(Activity.RESULT_OK, intent);
                ((Activity) getContext()).finish();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ToastUtil.toastLongMessage(errMsg);
            }
        });
    }

    private void delete() {
        List<String> identifiers = new ArrayList<>();
        identifiers.add(mId);

        V2TIMManager.getFriendshipManager().deleteFromFriendList(identifiers, V2TIMFriendInfo.V2TIM_FRIEND_TYPE_BOTH, new V2TIMValueCallback<List<V2TIMFriendOperationResult>>() {
            @Override
            public void onError(int code, String desc) {
                TUIKitLog.e(TAG, "deleteFriends err code = " + code + ", desc = " + desc);
                ToastUtil.toastShortMessage("Error code = " + code + ", desc = " + desc);
            }

            @Override
            public void onSuccess(List<V2TIMFriendOperationResult> v2TIMFriendOperationResults) {
                TUIKitLog.i(TAG, "deleteFriends success");
                ConversationManagerKit.getInstance().deleteConversation(mId, false);
                if (mListener != null) {
                    mListener.onDeleteFriendClick(mId);
                }
                ((Activity) getContext()).finish();
            }
        });
    }

    private void chat() {
        if (mListener != null || mContactInfo != null) {
            mListener.onStartConversationClick(mContactInfo);
        }
        ((Activity) getContext()).finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnChat) {
            chat();
        } else if (v.getId() == R.id.btnDel) {
            new TUIKitDialog(getContext())
                    .builder()
                    .setCancelable(true)
                    .setCancelOutside(true)
                    .setTitle("删除联系人")
                    .setDialogWidth(0.75f)
                    .setSureButtonColor(getResources().getColor(R.color.text_negative))
                    .setPositiveButton("删除", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            delete();
                        }
                    })
                    .setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .show();

        } else if (v.getId() == R.id.remark) {
            Bundle bundle = new Bundle();
            bundle.putString(TUIKitConstants.Selection.TITLE, getResources().getString(R.string.profile_remark_edit));
            bundle.putString(TUIKitConstants.Selection.INIT_CONTENT, mRemarkView.getContent());
            bundle.putInt(TUIKitConstants.Selection.LIMIT, 20);
            SelectionActivity.startTextSelection(TUIKit.getAppContext(), bundle, new SelectionActivity.OnResultReturnListener() {
                @Override
                public void onReturn(Object text) {
                    mRemarkView.setContent(text.toString());
                    if (TextUtils.isEmpty(text.toString())) {
                        text = "";
                    }
                    modifyRemark(text.toString());
                }
            });
        }
    }

    public String getmId() {
        return mId;
    }

    private void modifyRemark(final String txt) {
        V2TIMFriendInfo v2TIMFriendInfo = new V2TIMFriendInfo();
        v2TIMFriendInfo.setUserID(mId);
        v2TIMFriendInfo.setFriendRemark(txt);

        V2TIMManager.getFriendshipManager().setFriendInfo(v2TIMFriendInfo, new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                TUIKitLog.e(TAG, "modifyRemark err code = " + code + ", desc = " + desc);
            }

            @Override
            public void onSuccess() {
                mContactInfo.setRemark(txt);
                TUIKitLog.i(TAG, "modifyRemark success");
                Map data = new HashMap();
                data.put("friend_id", mId);
                data.put("alias", txt);
                YHttp.obtain().post(Constant.URL_SET_ALIAS, data, null);
            }
        });
    }

    private void addBlack() {
        String[] idStringList = mId.split(",");

        List<String> idList = new ArrayList<>();
        for (String id : idStringList) {
            idList.add(id);
        }

        V2TIMManager.getFriendshipManager().addToBlackList(idList, new V2TIMValueCallback<List<V2TIMFriendOperationResult>>() {
            @Override
            public void onError(int code, String desc) {
                TUIKitLog.e(TAG, "addBlackList err code = " + code + ", desc = " + desc);
                ToastUtil.toastShortMessage("Error code = " + code + ", desc = " + desc);
            }

            @Override
            public void onSuccess(List<V2TIMFriendOperationResult> v2TIMFriendOperationResults) {
                TUIKitLog.v(TAG, "addBlackList success");
            }
        });
    }

    private void deleteBlack() {
        String[] idStringList = mId.split(",");

        List<String> idList = new ArrayList<>();
        for (String id : idStringList) {
            idList.add(id);
        }

        V2TIMManager.getFriendshipManager().deleteFromBlackList(idList, new V2TIMValueCallback<List<V2TIMFriendOperationResult>>() {
            @Override
            public void onError(int code, String desc) {
                TUIKitLog.e(TAG, "deleteBlackList err code = " + code + ", desc = " + desc);
                ToastUtil.toastShortMessage("Error code = " + code + ", desc = " + desc);
            }

            @Override
            public void onSuccess(List<V2TIMFriendOperationResult> v2TIMFriendOperationResults) {
                TUIKitLog.i(TAG, "deleteBlackList success");
            }
        });
    }

    public void setOnButtonClickListener(OnButtonClickListener l) {
        mListener = l;
    }

    public interface OnButtonClickListener {
        void onStartConversationClick(ContactItemBean info);

        void onDeleteFriendClick(String id);
    }

}
