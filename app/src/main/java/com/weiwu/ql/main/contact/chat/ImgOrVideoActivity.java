package com.weiwu.ql.main.contact.chat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.GSYSampleADVideoPlayer;
import com.weiwu.ql.AppConstant;
import com.weiwu.ql.R;

public class ImgOrVideoActivity extends AppCompatActivity {

    private GSYSampleADVideoPlayer mVpChatVideo;
    private String mUrl;
    private String mType;
    private ImageView mIvChatImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_or_video);
        ImmersionBar.with(this).transparentNavigationBar().hideBar(BarHide.FLAG_HIDE_BAR).init();
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra(AppConstant.IMG_OR_VIDEO);
            mUrl = intent.getStringExtra(AppConstant.VIDEO_URL);
        }
        initView();
    }

    private void initView() {
        mVpChatVideo = (GSYSampleADVideoPlayer) findViewById(R.id.vp_chat_video);
        mIvChatImg = (ImageView) findViewById(R.id.iv_chat_img);
        if (mType.equals("img")) {
            mVpChatVideo.setVisibility(View.GONE);
            mIvChatImg.setVisibility(View.VISIBLE);
            Glide.with(this).load(mUrl).into(mIvChatImg);
        } else {
            mVpChatVideo.setVisibility(View.VISIBLE);
            mIvChatImg.setVisibility(View.GONE);
            mVpChatVideo.setUp(mUrl, true," ");
            //设置返回键
            mVpChatVideo.getBackButton().setVisibility(View.VISIBLE);

            //是否可以滑动调整
            mVpChatVideo.setIsTouchWiget(true);
            mVpChatVideo.getFullscreenButton().setVisibility(View.GONE);
            //设置返回按键功能
            mVpChatVideo.getBackButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    mVpChatVideo.setVideoAllCallBack(null);
                }
            });
            mVpChatVideo.startPlayLogic();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mVpChatVideo.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVpChatVideo.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        //释放所有
        super.onBackPressed();
    }
}