package com.weiwu.ql.main.mine.friends.adapter;

import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.weiwu.ql.R;

import moe.codeest.enviews.ENPlayView;

/**
 * Created by WZTENG on 2018/1/24 0024.
 */

public class VideoPlayerViewHolder extends CircleViewHolder {

    public final static String TAG = "VideoPlayerViewHolder";

    public StandardGSYVideoPlayer videoView;
    public ImageView ThumbImage;
    public ENPlayView videoStart;

    public VideoPlayerViewHolder(View itemView) {
        super(itemView, TYPE_VIDEO);
    }

    @Override
    public void initSubView(int viewType, ViewStub viewStub) {
        if (viewStub == null) {
            throw new IllegalArgumentException("viewStub is null...");
        }

        viewStub.setLayoutResource(R.layout.friends_viewstub_videoplayerbody);
        View subView = viewStub.inflate();

        StandardGSYVideoPlayer videoBody = subView.findViewById(R.id.videoView);
        ImageView  ThumbImage = subView.findViewById(R.id.ThumbImage);
        ENPlayView  videoStart = subView.findViewById(R.id.video_start);
        if (videoBody != null) {
            this.videoView = videoBody;
            this.ThumbImage = ThumbImage;
            this.videoStart = videoStart;
        }

    }
}
