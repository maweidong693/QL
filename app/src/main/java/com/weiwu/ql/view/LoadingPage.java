package com.weiwu.ql.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;

import com.cunoraz.gifview.library.GifView;
import com.weiwu.ql.R;
import com.weiwu.ql.utils.SystemFacade;


public class LoadingPage extends ConstraintLayout {

    public static final int MODE_1 = 1; // 透明背景

    public static final int MODE_2 = 2;// 背景不透明

    private RelativeLayout mLoadingLayout;

    private Group mErrorGroup;

    private GifView mGifView;

    private TextView mTVMsg;

    private Button mReload;

    private int mMode;
    private Context mContext;

    public LoadingPage(Context context) {
        super(context);
        mContext = context;
    }

    public LoadingPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public LoadingPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }


    private void initView() {
        mLoadingLayout = findViewById(R.id.loading_layout);
        mErrorGroup = findViewById(R.id.loading_group_error);
        mGifView = findViewById(R.id.loading_gif_view);
        mTVMsg = findViewById(R.id.loading_tv_msg);
        mReload = findViewById(R.id.loading_btn_reload);
    }


    public void show(int mode) {
        if (mode == MODE_1) {
            setBackgroundColor(Color.parseColor("#00000000"));

        } else {
            setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }
        mErrorGroup.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.VISIBLE);

        mGifView.play();
    }


    public void onError(String msg) {
        mErrorGroup.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        mReload.setVisibility(GONE);
        mReload.setOnClickListener(null);
        mTVMsg.setText(msg);
    }

    public void onError(String msg, final OnReloadListener listener) {
        mErrorGroup.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        mReload.setVisibility(VISIBLE);
        mReload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    if (SystemFacade.isOnInternet(mContext)) {
                        show(MODE_2);
                        listener.reload();
                    } else {
                        Toast.makeText(mContext, "当前无网络！前检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        mReload.requestFocus();
        mTVMsg.setText(msg);
    }

    public void dismiss() {
        mGifView.pause();
        mReload.setOnClickListener(null);

        ViewGroup parent = (ViewGroup) getParent();

        if (parent != null) {
            parent.removeView(this);
        }

    }

    public interface OnReloadListener {
        void reload();
    }


}
