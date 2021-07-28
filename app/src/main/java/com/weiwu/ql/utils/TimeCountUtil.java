package com.weiwu.ql.utils;

import android.app.Activity;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.weiwu.ql.R;


/**
 * 创建时间：2019/2/11
 * 类说明：短信倒计时
 */
public class TimeCountUtil extends CountDownTimer {

    private Activity mActivity;
    private TextView btn;//按钮
    private int color;

    public TimeCountUtil(Activity mActivity, long millisInFuture, long countDownInterval, TextView btn, int color) {
        super(millisInFuture, countDownInterval);
        this.mActivity = mActivity;
        this.btn = btn;
        this.color = color;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        btn.setClickable(false);//设置不能点击
        btn.setText(millisUntilFinished / 1000 + "s后重发");//设置倒计时时间
        //设置按钮为灰色，这时是不能点击的
        btn.setTextColor(color);
    }

    @Override
    public void onFinish() {
        btn.setText(mActivity.getResources().getString(R.string.getCode));
        btn.setTextColor(color);
        btn.setClickable(true);//重新获得点击
        cancel();
    }
}