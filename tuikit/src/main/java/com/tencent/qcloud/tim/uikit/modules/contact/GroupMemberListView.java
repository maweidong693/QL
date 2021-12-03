package com.tencent.qcloud.tim.uikit.modules.contact;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tencent.imsdk.v2.V2TIMFriendInfo;
import com.tencent.imsdk.v2.V2TIMGroupMemberFullInfo;
import com.tencent.imsdk.v2.V2TIMGroupMemberInfoResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.component.CustomLinearLayoutManager;
import com.tencent.qcloud.tim.uikit.component.indexlib.IndexBar.widget.IndexBar;
import com.tencent.qcloud.tim.uikit.component.indexlib.suspension.SuspensionDecoration;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfo;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupMemberInfo;
import com.tencent.qcloud.tim.uikit.utils.BackgroundTasks;
import com.tencent.qcloud.tim.uikit.utils.CustomInfo;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.tencent.qcloud.tim.uikit.utils.ThreadHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


public class GroupMemberListView extends LinearLayout {

    private static final String TAG = GroupMemberListView.class.getSimpleName();

    private static final String INDEX_STRING_TOP = "↑";
    private RecyclerView mRv;
    private GrouoMemberAdapter mAdapter;
    private CustomLinearLayoutManager mManager;
    private List<ContactItemBean> mData = new ArrayList<>();
    private SuspensionDecoration mDecoration;
    private ProgressBar mContactLoadingBar;
    private GroupInfo mGroupInfo;

    /**
     * 右侧边栏导航区域
     */
    private IndexBar mIndexBar;

    /**
     * 显示指示器DialogText
     */
    private TextView mTvSideBarHint;
    private int type; //1选择新群主   2管理员    3选择联系人发送名片

    public GroupMemberListView(Context context) {
        super(context);
        init();
    }

    public GroupMemberListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GroupMemberListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.contact_list, this);
        mRv = findViewById(R.id.contact_member_list);
        mManager = new CustomLinearLayoutManager(getContext());
        mRv.setLayoutManager(mManager);

        mAdapter = new GrouoMemberAdapter(mData);
        mRv.setAdapter(mAdapter);
        mRv.addItemDecoration(mDecoration = new SuspensionDecoration(getContext(), mData));
        mTvSideBarHint = findViewById(R.id.contact_tvSideBarHint);
        mIndexBar = findViewById(R.id.contact_indexBar);
        mIndexBar.setPressedShowTextView(mTvSideBarHint)
                .setNeedRealIndex(false)
                .setLayoutManager(mManager);
        mContactLoadingBar = findViewById(R.id.contact_loading_bar);
    }

    public GrouoMemberAdapter getAdapter() {
        return mAdapter;
    }

    public void setDataSource(List<ContactItemBean> data) {
        mContactLoadingBar.setVisibility(GONE);
        this.mData = data;
        mAdapter.setDataSource(mData);
        mIndexBar.setSourceDatas(mData).invalidate();
        mDecoration.setDatas(mData);
    }

    public void setSingleSelectMode(boolean mode) {
        mAdapter.setSingleSelectMode(mode);
    }

    public void setType(int type) {
        this.type = type;
        mAdapter.setType(type);
    }

    public void setOnSelectChangeListener(OnSelectChangedListener selectChangeListener) {
        mAdapter.setOnSelectChangedListener(selectChangeListener);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mAdapter.setOnItemClickListener(listener);
    }

    public void loadDataSource(int dataSource, GroupInfo data) {
        mContactLoadingBar.setVisibility(VISIBLE);
        mData.clear();
        switch (dataSource) {
            case DataSource.FRIEND_LIST:
                loadFriendListDataAsync();
                break;
            case DataSource.BLACK_LIST:
                loadBlackListData();
                break;
            case DataSource.GROUP_LIST:
                loadGroupListData();
                break;
            case DataSource.CONTACT_LIST:
                mData.add((ContactItemBean) new ContactItemBean(getResources().getString(R.string.new_friend),0)
                        .setTop(true).setBaseIndexTag(ContactItemBean.INDEX_STRING_TOP));
                mData.add((ContactItemBean) new ContactItemBean(getResources().getString(R.string.group),0).
                        setTop(true).setBaseIndexTag(ContactItemBean.INDEX_STRING_TOP));
                mData.add((ContactItemBean) new ContactItemBean(getResources().getString(R.string.blacklist),0).
                        setTop(true).setBaseIndexTag(ContactItemBean.INDEX_STRING_TOP));
                loadFriendListDataAsync();
                break;
            case DataSource.GROUP_MEMBER_LIST:
                mGroupInfo = data;
                loadGroupMembers();
                break;
            default:
                break;
        }
        mAdapter.notifyDataSetChanged();
    }

    public void setGroupInfo(GroupInfo groupInfo) {
        mGroupInfo = groupInfo;
    }

    private void updateStatus(List<ContactItemBean> beanList) {
        if (mGroupInfo == null) {
            return;
        }
        List<GroupMemberInfo> list = mGroupInfo.getMemberDetails();
        boolean needFresh = false;
        if (list.size() > 0) {
            for (GroupMemberInfo info : list) {
                for (ContactItemBean bean : beanList) {
                    if (info.getAccount().equals(bean.getId())) {
                        bean.setSelected(true);
                        bean.setEnable(false);
                        needFresh = true;
                    }
                }
            }
        }
        if (needFresh) {
            BackgroundTasks.getInstance().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void updateStatus(List<ContactItemBean> mData, List<String> beanList) {
        for (ContactItemBean info : mData) {
            for (String id : beanList) {
                if (info.getId().equals(id)) {
                    info.setSelected(true);
                }
            }
        }
    }

    private void loadFriendListDataAsync() {
        TUIKitLog.i(TAG, "loadFriendListDataAsync");
        ThreadHelper.INST.execute(new Runnable() {
            @Override
            public void run() {
                // 压测时数据量比较大，query耗时比较久，所以这里使用新线程来处理
                V2TIMManager.getFriendshipManager().getFriendList(new V2TIMValueCallback<List<V2TIMFriendInfo>>() {
                    @Override
                    public void onError(int code, String desc) {
                        TUIKitLog.e(TAG, "loadFriendListDataAsync err code:" + code + ", desc:" + desc);
//                        ToastUtil.toastShortMessage("loadFriendList error code = " + code + ", desc = " + desc);
                    }

                    @Override
                    public void onSuccess(List<V2TIMFriendInfo> v2TIMFriendInfos) {
                        if (v2TIMFriendInfos == null) {
                            v2TIMFriendInfos = new ArrayList<>();
                        }
                        TUIKitLog.i(TAG, "loadFriendListDataAsync->getFriendList:" + v2TIMFriendInfos.size());
                        assembleFriendListData(v2TIMFriendInfos);
                    }
                });
            }
        });
    }

//    private void fillFriendListData(final List<V2TIMFriendInfo> timFriendInfoList) {
//        // 外部调用是在其他线程里面，但是更新数据同时会刷新UI，所以需要放在主线程做。
//        BackgroundTasks.getInstance().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (timFriendInfoList.size() == 0) {
//                    TIMFriendshipManager.getInstance().getFriendList(new TIMValueCallBack<List<TIMFriend>>() {
//                        @Override
//                        public void onError(int code, String desc) {
//                            TUIKitLog.e(TAG, "getFriendList err code = " + code);
//                            mContactLoadingBar.setVisibility(GONE);
//                        }
//
//                        @Override
//                        public void onSuccess(List<TIMFriend> timFriends) {
//                            if (timFriends == null) {
//                                timFriends = new ArrayList<>();
//                            }
//                            TUIKitLog.i(TAG, "getFriendList success result = " + timFriends.size());
//                            assembleFriendListData(timFriends);
//                        }
//                    });
//                } else {
//                    assembleFriendListData(timFriendInfoList);
//                }
//            }
//        });
//    }

    private void assembleFriendListData(final List<V2TIMFriendInfo> timFriendInfoList) {
        /*for (V2TIMFriendInfo timFriendInfo : timFriendInfoList) {
            ContactItemBean info = new ContactItemBean();
            info.covertTIMFriend(timFriendInfo);
            mData.add(info);
        }*/
        updateStatus(mData);
        setDataSource(mData);
    }

    private void loadBlackListData() {
        TUIKitLog.i(TAG, "loadBlackListData");

        /*V2TIMManager.getFriendshipManager().getBlackList(new V2TIMValueCallback<List<V2TIMFriendInfo>>() {
            @Override
            public void onError(int code, String desc) {
                TUIKitLog.e(TAG, "getBlackList err code = " + code + ", desc = " + desc);
                ToastUtil.toastShortMessage("Error code = " + code + ", desc = " + desc);
                mContactLoadingBar.setVisibility(GONE);
            }

            @Override
            public void onSuccess(List<V2TIMFriendInfo> v2TIMFriendInfos) {
                TUIKitLog.i(TAG, "getBlackList success: " + v2TIMFriendInfos.size());
                if (v2TIMFriendInfos.size() == 0) {
                    TUIKitLog.i(TAG, "getBlackList success but no data");
                }
                mData.clear();
                for (V2TIMFriendInfo timFriendInfo : v2TIMFriendInfos) {
                    ContactItemBean info = new ContactItemBean();
                    info.covertTIMFriend(timFriendInfo).setBlackList(true);
                    mData.add(info);
                }
                setDataSource(mData);
            }
        });*/

//        TIMFriendshipManager.getInstance().getBlackList(new TIMValueCallBack<List<TIMFriend>>() {
//            @Override
//            public void onError(int i, String s) {
//                TUIKitLog.e(TAG, "getBlackList err code = " + i + ", desc = " + s);
//                ToastUtil.toastShortMessage("Error code = " + i + ", desc = " + s);
//                mContactLoadingBar.setVisibility(GONE);
//            }
//
//            @Override
//            public void onSuccess(List<TIMFriend> timFriends) {
//                TUIKitLog.i(TAG, "getBlackList success: " + timFriends.size());
//                if (timFriends.size() == 0) {
//                    TUIKitLog.i(TAG, "getBlackList success but no data");
//                }
//                mData.clear();
//                for (TIMFriend timFriend : timFriends) {
//                    ContactItemBean info = new ContactItemBean();
//                    info.covertTIMFriend(timFriend).setBlackList(true);
//                    mData.add(info);
//                }
//                setDataSource(mData);
//            }
//        });
    }

    private void loadGroupListData() {
        TUIKitLog.i(TAG, "loadGroupListData");

        /*V2TIMManager.getGroupManager().getJoinedGroupList(new V2TIMValueCallback<List<V2TIMGroupInfo>>() {
            @Override
            public void onError(int code, String desc) {
                TUIKitLog.e(TAG, "getGroupList err code = " + code + ", desc = " + desc);
                ToastUtil.toastShortMessage("Error code = " + code + ", desc = " + desc);
                mContactLoadingBar.setVisibility(GONE);
            }

            @Override
            public void onSuccess(List<V2TIMGroupInfo> v2TIMGroupInfos) {
                TUIKitLog.i(TAG, "getGroupList success: " + v2TIMGroupInfos.size());
                if (v2TIMGroupInfos.size() == 0) {
                    TUIKitLog.i(TAG, "getGroupList success but no data");
                }
                mData.clear();
                for (V2TIMGroupInfo info : v2TIMGroupInfos) {
                    ContactItemBean bean = new ContactItemBean();
                    mData.add(bean.covertTIMGroupBaseInfo(info));
                }
                setDataSource(mData);
            }
        });*/
    }

    private void loadGroupMembers() {
        List<GroupMemberInfo> memberDetails = mGroupInfo.getMemberDetails();
        List<GroupMemberInfo> members = new ArrayList<>();
        for (int i = 0; i < memberDetails.size(); i++) {
            members.add(memberDetails.get(i));

        }
        mData.clear();
        for (GroupMemberInfo info : members) {
            ContactItemBean bean = new ContactItemBean();
            mData.add(bean.covertTIMGroupMemberFullInfo(info));
        }
        if (type == 2) {
//            String introduction = mGroupInfo.getGroupIntroduction();
//            HashMap<String, Object> hashMap = CustomInfo.InfoStrToMap(introduction);
//            List<String >  list = (List<String>) hashMap.get(CustomInfo.GROUP_MANAGEMENT);
//            if (list!=null&&list.size()>0){
            List<String> list = new ArrayList<>();
            for (int i = 0; i < memberDetails.size(); i++) {
                GroupMemberInfo memberInfo = memberDetails.get(i);
                if (memberInfo.getMemberType() == 110) {
                    list.add(memberInfo.getAccount());
                }
            }
            updateStatus(mData, list);
//            }
        }
        setDataSource(mData);
       /* V2TIMManager.getGroupManager().getGroupMemberList(mGroupInfo.getId(), V2TIMGroupMemberFullInfo.V2TIM_GROUP_MEMBER_FILTER_ALL, 0, new V2TIMValueCallback<V2TIMGroupMemberInfoResult>() {
            @Override
            public void onError(int code, String desc) {
                TUIKitLog.e(TAG, "loadGroupMembers failed, code: " + code + "|desc: " + desc);
            }

            @Override
            public void onSuccess(V2TIMGroupMemberInfoResult v2TIMGroupMemberInfoResult) {
                List<V2TIMGroupMemberFullInfo> members = new ArrayList<>();
                for (int i = 0; i < v2TIMGroupMemberInfoResult.getMemberInfoList().size(); i++) {
                    //remove self
                    if (v2TIMGroupMemberInfoResult.getMemberInfoList().get(i).getUserID().equals(V2TIMManager.getInstance().getLoginUser())){
                        continue;
                    }
                    members.add(v2TIMGroupMemberInfoResult.getMemberInfoList().get(i));
                }
                mData.clear();
                for (V2TIMGroupMemberFullInfo info : members) {
                    ContactItemBean bean = new ContactItemBean();
                    mData.add(bean.covertTIMGroupMemberFullInfo(info));
                }
                if (type==2){
                    String introduction = mGroupInfo.getGroupIntroduction();
                    HashMap<String, Object> hashMap = CustomInfo.InfoStrToMap(introduction);
                    List<String >  list = (List<String>) hashMap.get(CustomInfo.GROUP_MANAGEMENT);
                    if (list!=null&&list.size()>0){
                        updateStatus(mData,list);
                    }
                }
                setDataSource(mData);
            }
        });*/
    }

    public List<ContactItemBean> getGroupData() {
        return mData;
    }

    public interface OnSelectChangedListener {
        void onSelectChanged(ContactItemBean contact, boolean selected);
    }

    public interface OnItemClickListener {
        void onItemClick(int position, ContactItemBean contact);
    }

    public static class DataSource {
        public static final int UNKNOWN = -1;
        public static final int FRIEND_LIST = 1;
        public static final int BLACK_LIST = 2;
        public static final int GROUP_LIST = 3;
        public static final int CONTACT_LIST = 4;
        public static final int GROUP_MEMBER_LIST = 5;
    }
}
