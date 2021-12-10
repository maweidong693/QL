package com.weiwu.ql.main.contact.group;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.tencent.common.dialog.DialogMaker;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfo;
import com.weiwu.ql.AppConstant;
import com.weiwu.ql.MyApplication;
import com.weiwu.ql.base.BaseFragment;
import com.tencent.qcloud.tim.uikit.modules.group.info.InfoData;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupMemberInfo;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.ForbiddenRequestBody;
import com.weiwu.ql.data.request.GroupInfoRequestBody;
import com.weiwu.ql.data.request.UpdateGroupRequestBody;
import com.weiwu.ql.main.contact.group.info.invites.GroupInvitationFragment;
import com.weiwu.ql.main.contact.group.member.GroupMemberDeleteFragment;
import com.weiwu.ql.main.contact.group.member.GroupMemberInviteFragment;
import com.weiwu.ql.main.contact.group.member.GroupMemberManagerFragment;
import com.weiwu.ql.main.contact.group.member.GroupMemberMuteFragment;
import com.weiwu.ql.main.contact.group.member.GroupMemberTransferFragment;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.weiwu.ql.R;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfoData;
import com.weiwu.ql.data.repositories.GroupRepository;
import com.weiwu.ql.main.mine.detail.PersonalInformationActivity;
import com.weiwu.ql.utils.SPUtils;
import com.weiwu.ql.view.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class GroupInfoFragment extends BaseFragment implements GroupContract.IGroupView {

    private GroupInfoLayout mGroupInfoLayout;
    private GroupContract.IGroupPresenter mPresenter;
    private GroupInfo groupInfo;

    /*@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.group_info_fragment, container, false);
        setPresenter(new GroupPresenter(GroupRepository.getInstance()));
        initView();
        return mBaseView;
    }*/

    @Override
    protected int getLayoutId() {
        return R.layout.group_info_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setPresenter(new GroupPresenter(GroupRepository.getInstance()));
        initView(view);
    }

    private static final String TAG = "GroupInfoFragment";

    private void initView(View view) {
        mGroupInfoLayout = view.findViewById(R.id.group_info_layout);
        mGroupInfoLayout.setGroupId(getArguments().getInt(TUIKitConstants.Group.GROUP_ID));
        mPresenter.getGroupInfo(new GroupInfoRequestBody(getArguments().getInt(TUIKitConstants.Group.GROUP_ID, 0)));
        mGroupInfoLayout.setRouter(new IGroupMemberRouter() {
            @Override
            public void forwardListMember(GroupInfo info) {
                GroupMemberManagerFragment fragment = new GroupMemberManagerFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(TUIKitConstants.Group.GROUP_INFO, info);
                fragment.setArguments(bundle);
                forward(fragment, false);
            }

            @Override
            public void forwardAddMember(GroupInfo info) {
                GroupMemberInviteFragment fragment = new GroupMemberInviteFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(TUIKitConstants.Group.GROUP_INFO, info);
                fragment.setArguments(bundle);
                forward(fragment, false);
            }

            @Override
            public void forwardDeleteMember(GroupInfo info) {
                GroupMemberDeleteFragment fragment = new GroupMemberDeleteFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(TUIKitConstants.Group.GROUP_INFO, info);
                fragment.setArguments(bundle);
                forward(fragment, false);
            }

            @Override
            public void forwardTransFerMember(GroupInfo info) {
//                Log.d(TAG, "forwardTransFerMember: ccc===" + info.getMemberDetails().size());
                GroupMemberTransferFragment fragment = new GroupMemberTransferFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(TUIKitConstants.Group.GROUP_INFO, info);
                bundle.putSerializable("info", new InfoData("选择新群主", 1));
                fragment.setArguments(bundle);
                forward(fragment, false);
            }

            @Override
            public void forwardGroupManager(GroupInfo info) {
                GroupMemberMuteFragment fragment = new GroupMemberMuteFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(TUIKitConstants.Group.GROUP_INFO, info);
                bundle.putSerializable("info", new InfoData("群管理员", 1));
                fragment.setArguments(bundle);
                forward(fragment, false);
                /*GroupMemberTransferFragment fragment = new GroupMemberTransferFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(TUIKitConstants.Group.GROUP_INFO, info);
                bundle.putSerializable("info", new InfoData("群管理员", 2));
                fragment.setArguments(bundle);
                forward(fragment, false);*/

            }

            @Override
            public void forwardGroupMuteManager(GroupInfo info) {
                GroupMemberMuteFragment fragment = new GroupMemberMuteFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(TUIKitConstants.Group.GROUP_INFO, info);
                bundle.putSerializable("info", new InfoData("禁言管理", 3));
                fragment.setArguments(bundle);
                forward(fragment, false);

            }

            @Override
            public void forwardGroupInvitation(GroupInfo info) {
                GroupInvitationFragment fragment = new GroupInvitationFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(TUIKitConstants.Group.GROUP_INFO, info);
                fragment.setArguments(bundle);
                forward(fragment, false);
            }

            @Override
            public void allMute(GroupInfo info, boolean isMute) {
                List<GroupMemberInfo> memberDetails = info.getMemberDetails();
                List<String> membersId = new ArrayList<>();
                for (int i = 0; i < memberDetails.size(); i++) {
                    String account = memberDetails.get(i).getAccount();
                    if (!SPUtils.getValue(AppConstant.USER, AppConstant.USER_ID).equals(account)) {
                        membersId.add(account);
                    }
                }
                if (isMute) {
                    mPresenter.updateGroupInfo(new UpdateGroupRequestBody(info.getId(), "1"));

                } else {
                    mPresenter.updateGroupInfo(new UpdateGroupRequestBody(info.getId(), "0"));

                }
            }

            @Override
            public void setExamine(GroupInfo info, boolean isExamine) {
                UpdateGroupRequestBody body = new UpdateGroupRequestBody();
                body.setId(info.getId());
                if (isExamine) {
                    body.setIs_join_check("1");
                } else {
                    body.setIs_join_check("0");
                }
                mPresenter.updateGroupInfo(body);
            }

            @Override
            public void setGroupIcon(GroupInfo info) {
                choosePhoto();
            }

            @Override
            public void setAddFriend(GroupInfo info, int isAddFriend) {
                mPresenter.updateGroupInfo(new UpdateGroupRequestBody(groupInfo.getId(), null, null, null, null, null, isAddFriend + "", null, null));
            }

            @Override
            public void setIsTop(GroupInfo info, boolean isTop) {
                mPresenter.updateGroupInfo(new UpdateGroupRequestBody(groupInfo.getId(), null, null, null, null, null, null, null, isTop + ""));

            }
        });

        mGroupInfoLayout.setQuitGroupListener(new GroupInfoLayout.IQuitGroupListener() {
            @Override
            public void quitGroup(int type, String groupId) {
                if (type == 1) {
                    mPresenter.dissolveGroup(new GroupInfoRequestBody(Integer.parseInt(groupId)));
                } else if (type == 2) {
                    mPresenter.signGroup(new GroupInfoRequestBody(Integer.parseInt(groupId)));
                }
            }
        });

    }

    //选择图片
    private void choosePhoto() {
        PictureSelector.create(this)
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
                        DialogMaker.showProgressDialog(getContext(), "正在上传...");
                        File file = new File(result.get(0).getCompressPath());
                        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                        mPresenter.uploadPic(body);

                    }

                    @Override
                    public void onCancel() {
                        // 取消
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
    public void groupInfoReceive(GroupInfoData data) {
        if (data != null) {
            groupInfo = new GroupInfo();
//            List<GroupMemberInfo> member = new ArrayList<>();
            /*List<GroupMemberData.DataDTO> list = data.getData().getMemberGroupResultVOList();
            for (int i = 0; i < list.size(); i++) {
                GroupMemberInfo groupMemberInfo = new GroupMemberInfo();
                member.add(groupMemberInfo.covertTIMGroupMemberInfo(list.get(i)));
            }*/
            groupInfo.covertTIMGroupDetailInfo(data);
//            groupInfo.setMemberDetails(member);
            mGroupInfoLayout.setGroupInfo(groupInfo);
        }
    }

    @Override
    public void uploadReceive(LoginData data) {
        if (data != null && data.getData() != null) {
            DialogMaker.dismissProgressDialog();
            mPresenter.updateGroupInfo(new UpdateGroupRequestBody(groupInfo.getId(), null, null, null, data.getData().getSrc(), null, null, null, null));
        }
    }

    @Override
    public void allMuteReceive(HttpResult data) {
        showToast(data.getMsg());
    }

    @Override
    public void onSuccess(HttpResult data) {
        mPresenter.getGroupInfo(new GroupInfoRequestBody(getArguments().getInt(TUIKitConstants.Group.GROUP_ID, 0)));
        showToast(data.getMsg());
        getActivity().setResult(1002);
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
    public void setPresenter(GroupContract.IGroupPresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return getActivity();
    }
}
