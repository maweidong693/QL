package com.weiwu.ql.main.mine.detail;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.tencent.common.Constant;
import com.tencent.common.UserInfo;
import com.tencent.common.dialog.BottomDialogBean;
import com.tencent.common.dialog.BottomDialogTools;
import com.tencent.common.dialog.DialogMaker;
import com.tencent.common.http.BaseBean;
import com.tencent.common.http.HttpCallBack;
import com.tencent.common.http.YHttp;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.GlideEngine;
import com.tencent.qcloud.tim.uikit.utils.MD5Utils;
import com.weiwu.ql.AppConstant;
import com.weiwu.ql.MyApplication;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;
import com.weiwu.ql.data.bean.CodeData;
import com.weiwu.ql.data.bean.LoginData;
import com.weiwu.ql.data.repositories.MineRepository;
import com.weiwu.ql.main.mine.MineContract;
import com.weiwu.ql.utils.RxQRCode;
import com.weiwu.ql.utils.SPUtils;
import com.weiwu.ql.utils.ToastHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Joseph_Yan
 * on 2020/12/22
 */
public class QrCardActivity extends BaseActivity implements MineContract.IInviteView {

    private TitleBarLayout mTitleBar;

    private ImageView mUserIcon;
    private ImageView mCodeImg;
    private TextView mAccountView;
    private TextView mPhoneView;
    private TextView mDescription;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String picFile = (String) msg.obj;
            String[] split = picFile.split("/");
            String fileName = split[split.length - 1];
            try {
                MediaStore.Images.Media.insertImage(getContentResolver(), picFile, fileName, null);
            } catch (Exception e) {
                try {
                    MediaStore.Images.Media.insertImage(TUIKit.getAppContext()
                            .getContentResolver(), picFile, fileName, null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            }
            // 最后通知图库更新
            sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"
                    + picFile)));
            ToastHelper.showToast(QrCardActivity.this, "保存成功");
        }
    };
    private String mType;
    private MineContract.IInvitePresenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_card_activity);
        setPresenter(new InvitePresenter(MineRepository.getInstance()));
        initView();
    }

    private void initView() {
        mTitleBar = findViewById(R.id.self_info_title_bar);
        mDescription = findViewById(R.id.description);
        mUserIcon = findViewById(R.id.self_icon);
        mCodeImg = findViewById(R.id.code_img);
        mAccountView = findViewById(R.id.self_account);
        mPhoneView = findViewById(R.id.self_phone);
//        String selfUserID = V2TIMManager.getInstance().getLoginUser();
        String selfUserID = SPUtils.getValue(AppConstant.USER, AppConstant.USER_ID);
        mAccountView.setText(String.format(getResources().getString(R.string.id), selfUserID));
        DialogMaker.showProgressDialog(this, "正在生成二维码...");
        String type = getIntent().getStringExtra("type");
        if (TextUtils.equals(type, "1")) {
            mPhoneView.setText(UserInfo.getInstance().getPhone());
            mPhoneView.setVisibility(View.VISIBLE);
            mDescription.setText(getString(R.string.description_business_card));
            mTitleBar.setTitle(getResources().getString(R.string.qr_code_business_card), TitleBarLayout.POSITION.LEFT);
            CodeData codeData = new CodeData(SPUtils.getValue(AppConstant.USER, AppConstant.USER_ID), "add-friend");
            String codeContent = new Gson().toJson(codeData);
            if (TextUtils.isEmpty(UserInfo.getInstance().getAvatar())) {
                RxQRCode.builder(codeContent).
                        backColor(0xFFFFFFFF).
                        codeColor(0xFF000000).
                        codeSide(600).
                        codeLogo(getResources().getDrawable(R.drawable.ic_launcher)).
                        codeBorder(1).
                        into(mCodeImg);

            }
            GlideEngine.loadCornerImage(mUserIcon, UserInfo.getInstance().getAvatar(), new RequestListener<Drawable>() {
                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    DialogMaker.dismissProgressDialog();
                    RxQRCode.builder(codeContent).
                            backColor(0xFFFFFFFF).
                            codeColor(0xFF000000).
                            codeSide(600).
                            codeLogo(resource).
                            codeBorder(1).
                            into(mCodeImg);
                    return false;
                }

                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                    DialogMaker.dismissProgressDialog();
                    RxQRCode.builder(codeContent).
                            backColor(0xFFFFFFFF).
                            codeColor(0xFF000000).
                            codeSide(600).
                            codeLogo(getResources().getDrawable(R.drawable.ic_launcher)).
                            codeBorder(1).
                            into(mCodeImg);
                    return false;
                }

            }, Constant.RADIUS);

        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mAccountView.getLayoutParams();
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            mAccountView.setLayoutParams(params);
            mDescription.setText(getResources().getString(R.string.description_invitation_code));
            mTitleBar.setTitle(getResources().getString(R.string.invitation_code), TitleBarLayout.POSITION.LEFT);

            mPresenter.getInviteKey();
        }
        mTitleBar.getLeftGroup().setOnClickListener(v -> {
            finish();
        });
        mTitleBar.setRightIcon(R.drawable.more);
        mTitleBar.setOnRightClickListener(v -> {
            BottomDialogTools.showBottomDialogText(this, true, new BottomDialogTools.BottomDialogAdapter.OnPopupClickListener() {
                        @Override
                        public void onClick(int position, Dialog dialog) {
                            switch (position) {
                                case 0:
                                    //保存图片
                                    saveMyBitmap("code_img", createViewBitmap(mCodeImg));

                                    dialog.dismiss();
                                    break;
                                case 1:
                                    dialog.dismiss();
                                    break;
                            }


                        }
                    }, new BottomDialogBean("保存到手机"),
                    new BottomDialogBean("取消")).show();


        });
    }

    //使用IO流将bitmap对象存到本地指定文件夹
    public void saveMyBitmap(final String bitName, final Bitmap bitmap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File downloadDir = new File(Environment.getExternalStorageDirectory(), "PromoterDownload");
                if (!downloadDir.exists()) {
                    downloadDir.mkdir();
                }
                File file = new File(downloadDir, String.valueOf(System.currentTimeMillis()) + MD5Utils.md5Password(UserInfo.getInstance().getUserId()) + ".png");
//                if (file.exists()) {
//                    YHttp.handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                ToastHelper.showToast(getActivity(), "保存成功");
//                                if (dialog != null) {
//                                    dialog.dismiss();
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                    return;
//                }
//                String filePath = Environment.getExternalStorageDirectory().getPath();
//                File file = new File(filePath + "/DCIM/Camera/" + bitName + ".png");
                try {
                    file.createNewFile();
                    FileOutputStream fOut = null;
                    fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);


                    Message msg = Message.obtain();
                    msg.obj = file.getPath();
                    handler.sendMessage(msg);
                    //Toast.makeText(PayCodeActivity.this, "保存成功", Toast.LENGTH_LONG).show();
                    fOut.flush();
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public Bitmap createViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;

    }

    @Override
    public void inviteKeyReceive(LoginData data) {
        if (data.getData() == null) {
            return;
        }
        String url = data.getData();
        if (!TextUtils.isEmpty(url)) {
            DialogMaker.showProgressDialog(QrCardActivity.this, "正在生成二维码...");
            RxQRCode.builder(url).
                    backColor(0xFFFFFFFF).
                    codeColor(0xFF000000).
                    codeSide(600).
                    codeLogo(getResources().getDrawable(R.drawable.ic_launcher)).
                    codeBorder(1).
                    into(mCodeImg);

            GlideEngine.loadCornerImage(mUserIcon, UserInfo.getInstance().getAvatar(), new RequestListener<Drawable>() {
                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    DialogMaker.dismissProgressDialog();
                    RxQRCode.builder(url).
                            backColor(0xFFFFFFFF).
                            codeColor(0xFF000000).
                            codeSide(600).
                            codeLogo(resource).
                            codeBorder(1).
                            into(mCodeImg);
                    return false;
                }

                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                    DialogMaker.dismissProgressDialog();
                    RxQRCode.builder(url).
                            backColor(0xFFFFFFFF).
                            codeColor(0xFF000000).
                            codeSide(600).
                            codeLogo(getResources().getDrawable(R.drawable.ic_launcher)).
                            codeBorder(1).
                            into(mCodeImg);
                    return false;
                }

            }, Constant.RADIUS);

            DialogMaker.dismissProgressDialog();
        }
    }

    @Override
    public void onFail(String msg, int code) {
        showToast(msg);
        if (code == 10001) {
            MyApplication.loginAgain();
        }
    }

    @Override
    public void setPresenter(MineContract.IInvitePresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return this;
    }
}
