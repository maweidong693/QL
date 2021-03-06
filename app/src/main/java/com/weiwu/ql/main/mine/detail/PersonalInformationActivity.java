package com.weiwu.ql.main.mine.detail;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.tencent.common.Constant;
import com.tencent.common.UserInfo;
import com.tencent.common.dialog.DialogMaker;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.qcloud.tim.uikit.component.LineControllerView;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.weiwu.ql.AppConstant;
import com.weiwu.ql.MyApplication;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.bean.MessageEvent;
import com.weiwu.ql.data.bean.MineInfoData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.repositories.MineRepository;
import com.weiwu.ql.data.request.UpdateMineInfoRequestBody;
import com.weiwu.ql.main.contact.group.info.SelectionActivity;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;
import com.weiwu.ql.main.mine.MineContract;
import com.weiwu.ql.utils.IntentUtil;
import com.weiwu.ql.utils.SPUtils;
import com.weiwu.ql.view.GlideEngine;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PersonalInformationActivity extends BaseActivity implements View.OnClickListener, MineContract.IMineInfoDetailView {
    private ImageView mUserIcon;
    private TitleBarLayout mTitleBar;
    private LineControllerView mModifyNickNameView;
    private LineControllerView mModifySignatureView;
    private RelativeLayout mItemHeadView;
    private RelativeLayout mQrCodeCardView;
    private RelativeLayout mInvitationCodeView;
    private MineContract.IMineInfoDetailPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_information_fragment);
        setPresenter(new MineInfoPresenter(MineRepository.getInstance()));
        initView();
        Log.d(TAG, "onCreate: id===" + SPUtils.getValue(AppConstant.USER, AppConstant.USER_ID));
    }

    private static final String TAG = "aaaa";

    public void initView() {
        mTitleBar = findViewById(R.id.self_info_title_bar);
        mTitleBar.getLeftGroup().setOnClickListener(v -> {
            finish();
        });
        mTitleBar.getRightGroup().setVisibility(View.GONE);
        mTitleBar.setTitle(getResources().getString(R.string.personal_information), TitleBarLayout.POSITION.LEFT);
        mUserIcon = findViewById(R.id.self_icon);
        mModifyNickNameView = findViewById(R.id.modify_nick_name);
        mModifySignatureView = findViewById(R.id.modify_signature);
        mModifySignatureView.setVisibility(View.GONE);
        mModifyNickNameView.setOnClickListener(this);
        mModifySignatureView.setOnClickListener(this);
        mItemHeadView = findViewById(R.id.modify_user_icon);
        mQrCodeCardView = findViewById(R.id.qr_code_card);
        mInvitationCodeView = findViewById(R.id.invitation_code);
        mModifyNickNameView.setContent(UserInfo.getInstance().getName());
        mModifySignatureView.setContent(UserInfo.getInstance().getSelfSignature());
        mQrCodeCardView.setOnClickListener(v -> {
            IntentUtil.redirectToNextActivity(this, QrCardActivity.class, "type", "1");

        });
        mInvitationCodeView.setOnClickListener(v -> {
            IntentUtil.redirectToNextActivity(this, QrCardActivity.class, "type", "2");

        });
        mItemHeadView.setOnClickListener(v -> {
            //????????????
            choosePhoto();


        });

        mPresenter.getMineInfo();
    }

    //????????????
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
                        DialogMaker.showProgressDialog(PersonalInformationActivity.this, "????????????...");
                        File file = new File(result.get(0).getCompressPath());
                        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                        mPresenter.uploadPic(body);

                    }

                    @Override
                    public void onCancel() {
                        // ??????
                    }
                });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.modify_nick_name) {

            Bundle bundle = new Bundle();
            bundle.putString(TUIKitConstants.Selection.TITLE, getResources().getString(R.string.modify_nick_name));
            bundle.putString(TUIKitConstants.Selection.INIT_CONTENT, mModifyNickNameView.getContent());
            bundle.putInt(TUIKitConstants.Selection.LIMIT, 20);
            SelectionActivity.startTextSelection(this, bundle, new SelectionActivity.OnResultReturnListener() {
                @Override
                public void onReturn(Object text) {
                    mModifyNickNameView.setContent(text.toString());
                    updateProfile();
                }
            });


        } else if (v.getId() == R.id.modify_signature) {

            Bundle bundle = new Bundle();
            bundle.putString(TUIKitConstants.Selection.TITLE, getResources().getString(R.string.modify_signature));
            bundle.putString(TUIKitConstants.Selection.INIT_CONTENT, mModifySignatureView.getContent());
            bundle.putInt(TUIKitConstants.Selection.LIMIT, 40);
            SelectionActivity.startTextSelection(this, bundle, new SelectionActivity.OnResultReturnListener() {
                @Override
                public void onReturn(Object text) {
                    mModifySignatureView.setContent(text.toString());
                    updateProfile();
                }
            });

        }
    }

    private void updateProfile() {

        V2TIMUserFullInfo v2TIMUserFullInfo = new V2TIMUserFullInfo();
        // ??????
//        if (!TextUtils.isEmpty(mIconUrl)) {
//            v2TIMUserFullInfo.setFaceUrl(mIconUrl);
//            UserInfo.getInstance().setAvatar(mIconUrl);
//        }

        // ??????
        String nickName = mModifyNickNameView.getContent();
        v2TIMUserFullInfo.setNickname(nickName);

        // ????????????
        String signature = mModifySignatureView.getContent();
        v2TIMUserFullInfo.setSelfSignature(signature);


        V2TIMManager.getInstance().setSelfInfo(v2TIMUserFullInfo, new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                ToastUtil.toastShortMessage("Error code = " + code + ", desc = " + desc);
            }

            @Override
            public void onSuccess() {
                    /*EventBus.getDefault().post(new UpdatesBean());
                    TUIKitConfigs.getConfigs().getGeneralConfig().setUserNickname(nickName);
                    UserInfo.getInstance().setName(nickName);
                    UserInfo.getInstance().setSelfSignature(signature);*/

            }
        });
    }

    @Override
    public void onSuccess(HttpResult data) {
        DialogMaker.dismissProgressDialog();
        EventBus.getDefault().post(new MessageEvent("??????????????????", 111));
        showToast("???????????????");
    }

    @Override
    public void mineInfoReceive(MineInfoData data) {
        MineInfoData.DataDTO dto = data.getData();
        if (TextUtils.isEmpty(dto.getFace_url())) {
            Glide.with(this).load(R.drawable.ic_launcher).into(mUserIcon);
//            GlideEngine.loadImage(mUserIcon, R.drawable.ic_launcher);
        } else {
            Glide.with(this).load(dto.getFace_url()).into(mUserIcon);
//            GlideEngine.loadCornerImage(mUserIcon, dto.getAvator(), null, Constant.RADIUS);
            UserInfo.getInstance().setAvatar(dto.getFace_url());
        }
        UserInfo.getInstance().setUserId(dto.getIm_id());

        mModifyNickNameView.setContent(dto.getNick_name());
        mModifySignatureView.setContent(dto.getSing());
        SPUtils.commitValue(AppConstant.USER, AppConstant.USER_ID, dto.getIm_id());
    }

    @Override
    public void uploadReceive(LoginData data) {
        String mIconUrl = data.getData().getSrc();
        // ??????
        if (!TextUtils.isEmpty(mIconUrl)) {
            Glide.with(this).load(mIconUrl).into(mUserIcon);
            mPresenter.updateMineInfo(new UpdateMineInfoRequestBody(null, 0 + "", null, null, null, mIconUrl));
//                                    EventBus.getDefault().post(new RefreshBean());
        }
    }

    @Override
    public void onFail(String msg, int code) {
        showToast(msg);
        if (code == 10001) {
            MyApplication.getInstance().loginAgain();
        }
    }

    @Override
    public void setPresenter(MineContract.IMineInfoDetailPresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return this;
    }
}
