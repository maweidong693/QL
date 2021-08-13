package com.weiwu.ql.view;

import android.content.Context;
import android.view.View;

import com.weiwu.ql.R;

import razerdp.basepopup.BasePopupWindow;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/29 15:05 
 */
public class VoicePopup extends BasePopupWindow {
    public VoicePopup(Context context) {
        super(context);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.pop_voice);
    }
}
