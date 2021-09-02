package com.weiwu.ql.main.mine.setting;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
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
import com.tencent.qcloud.tim.uikit.component.LineControllerView;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;
import com.weiwu.ql.utils.GlideEngine;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设置聊天背景
 */
public class SetChatbackGroundActivity extends BaseActivity {

    private TitleBarLayout mTitleBar;
    private LineControllerView mSelectFromAlbum;
    private LineControllerView mDefaultBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_background_layout);
        initView();
    }

    private void initView() {
        mSelectFromAlbum = findViewById(R.id.select_from_album);
        mDefaultBackground = findViewById(R.id.default_background);
        mSelectFromAlbum.setCanNav(true);
        mSelectFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChatbackgroundImage();
            }
        });
        mDefaultBackground.setCanNav(true);
        mDefaultBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogMaker.showProgressDialog(SetChatbackGroundActivity.this, getString(R.string.request), true);
                /*Map map = new HashMap();
                map.put("backgroundImg", "");
                YHttp.obtain().post(Constant.URL_SET_CHAT_IMAGE, map, new HttpCallBack<BaseBean>() {
                    @Override
                    public void onSuccess(BaseBean bean) {
                        ToastUtil.toastShortMessage("设置成功");
                        YPreferencesUtils.put(Constant.sp_chat_background,"");
                    }

                    @Override
                    public void onFailed(String error) {

                    }
                });*/

            }
        });
        mTitleBar = findViewById(R.id.title);
        mTitleBar.setTitle(getResources().getString(R.string.chat_background), TitleBarLayout.POSITION.LEFT);
        mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleBar.getRightIcon().setVisibility(View.GONE);

    }

    //设置聊天背景
    private void setChatbackgroundImage() {
        PictureSelector.create(SetChatbackGroundActivity.this)
                .openGallery(PictureMimeType.ofImage())
                .selectionMode( PictureConfig.SINGLE)
                .isGif(false)
                .isEnableCrop(true)
                .withAspectRatio(1,2)
                .showCropFrame(true)
                .showCropGrid(false)
                .isCompress(true)
                .minimumCompressSize(1000)
                .imageEngine(GlideEngine.createGlideEngine())
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        if (result==null||result.isEmpty()){
                            return;
                        }
                        DialogMaker.showProgressDialog(SetChatbackGroundActivity.this, getString(R.string.request), true);
                        // 结果回调
                        /*Map map = new HashMap();
                        map.put("image", new File(result.get(0).getCompressPath()));
                        YHttp.obtain().post(Constant.URL_UPLOAD, map, new HttpCallBack<BaseBean>() {
                            @Override
                            public void onSuccess(BaseBean bean) {
                                if (bean.getData() == null) {
                                    return;
                                }
                                FileUploadBean uploadBean = JSON.parseObject(bean.getData().toString(), FileUploadBean.class);
                                String mIconUrl = uploadBean.getUrl();
                                if (TextUtils.isEmpty(mIconUrl)){
                                    return;
                                }
                                Map map = new HashMap();
                                map.put("backgroundImg", mIconUrl);
                                YHttp.obtain().post(Constant.URL_SET_CHAT_IMAGE, map, new HttpCallBack<BaseBean>() {
                                    @Override
                                    public void onSuccess(BaseBean bean) {
                                        ToastUtil.toastShortMessage(bean.getMsg());
                                        YPreferencesUtils.put(Constant.sp_chat_background,mIconUrl);
                                    }

                                    @Override
                                    public void onFailed(String error) {

                                    }
                                });


                            }

                            @Override
                            public void onFailed(String error) {

                            }
                        });*/



                    }

                    @Override
                    public void onCancel() {
                        // 取消
                    }
                });

    }

}