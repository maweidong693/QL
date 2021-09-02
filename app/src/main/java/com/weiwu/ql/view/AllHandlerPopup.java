package com.weiwu.ql.view;

import android.content.Context;
import android.view.View;

import com.weiwu.ql.R;

import razerdp.basepopup.BasePopupWindow;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/30 14:23 
 */
public class AllHandlerPopup extends BasePopupWindow {
    public AllHandlerPopup(Context context) {
        super(context);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_all_handler);
    }
}
