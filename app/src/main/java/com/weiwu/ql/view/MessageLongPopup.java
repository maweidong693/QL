package com.weiwu.ql.view;

import android.content.Context;
import android.view.View;

import com.weiwu.ql.R;

import razerdp.basepopup.BasePopupWindow;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/15 11:27 
 */
public class MessageLongPopup extends BasePopupWindow {
    public MessageLongPopup(Context context) {
        super(context);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popu_message_long);
    }
}
