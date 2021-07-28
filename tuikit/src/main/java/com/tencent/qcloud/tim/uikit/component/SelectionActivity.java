package com.tencent.qcloud.tim.uikit.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tencent.common.UserInfo;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectionActivity extends Activity {

    private static OnResultReturnListener sOnResultReturnListener;

    private RadioGroup radioGroup;
    private EditText input;
    private int mSelectionType;

    public static void startTextSelection(Context context, Bundle bundle, OnResultReturnListener listener) {
        bundle.putInt(TUIKitConstants.Selection.TYPE, TUIKitConstants.Selection.TYPE_TEXT);
        startSelection(context, bundle, listener);
    }

    public static void startListSelection(Context context, Bundle bundle, OnResultReturnListener listener) {
        bundle.putInt(TUIKitConstants.Selection.TYPE, TUIKitConstants.Selection.TYPE_LIST);
        startSelection(context, bundle, listener);
    }

    private static void startSelection(Context context, Bundle bundle, OnResultReturnListener listener) {
        Intent intent = new Intent(context, SelectionActivity.class);
        intent.putExtra(TUIKitConstants.Selection.CONTENT, bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        sOnResultReturnListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().setStatusBarColor(getResources().getColor(R.color.status_bar_color));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.navigation_bar_color));
            int vis = getWindow().getDecorView().getSystemUiVisibility();
            vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            vis |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            getWindow().getDecorView().setSystemUiVisibility(vis);
        }
        setContentView(R.layout.selection_activity);
        final TitleBarLayout titleBar = findViewById(R.id.edit_title_bar);
        radioGroup = findViewById(R.id.content_list_rg);
        input = findViewById(R.id.edit_content_et);

        Bundle bundle = getIntent().getBundleExtra(TUIKitConstants.Selection.CONTENT);
        final String title = bundle.getString(TUIKitConstants.Selection.TITLE);
        switch (bundle.getInt(TUIKitConstants.Selection.TYPE)) {
            case TUIKitConstants.Selection.TYPE_TEXT:
                radioGroup.setVisibility(View.GONE);
                String defaultString = bundle.getString(TUIKitConstants.Selection.INIT_CONTENT);
                int limit = bundle.getInt(TUIKitConstants.Selection.LIMIT);
                if (!TextUtils.isEmpty(defaultString)) {
                    input.setText(defaultString);
                    input.setSelection(defaultString.length());
                }
                if (limit > 0) {
                    input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(limit)});
                }
                if (title.equals(getResources().getString(R.string.modify_nick_name))||title.equals(getResources().getString(R.string.modify_nick_name_in_goup))) {
                    input.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            String editable = input.getText().toString();
//                            String regEx = "[^a-zA-Z0-9]";  //只能输入字母或数字
                            String regEx = "[^0-9a-zA-Z\u4e00-\u9fa5]";
                            Pattern p = Pattern.compile(regEx);
                            Matcher m = p.matcher(editable);
                            String str = m.replaceAll("").trim();    //删掉不是字母或数字的字符
                            if(!editable.equals(str)){
                                input.setText(str);  //设置EditText的字符
                                input.setSelection(str.length()); //因为删除了字符，要重写设置新的光标所在位置
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                }
                break;
            case TUIKitConstants.Selection.TYPE_LIST:
                input.setVisibility(View.GONE);
                ArrayList<String> list = bundle.getStringArrayList(TUIKitConstants.Selection.LIST);
                if (list == null || list.size() == 0) {
                    return;
                }
                for (int i = 0; i < list.size(); i++) {
                    RadioButton radioButton = new RadioButton(this);
                    radioButton.setText(list.get(i));
                    radioButton.setId(i);
                    radioButton.setTextSize(18);
                    radioButton.setTextColor(getResources().getColor(R.color.black));
                    radioButton.setPadding(0,5,0,5);
                    radioButton.setLayoutDirection(android.view.View.LAYOUT_DIRECTION_RTL);
                    radioButton.setTextDirection(android.view.View.TEXT_DIRECTION_LTR);
                    radioGroup.addView(radioButton, i, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
                int checked = bundle.getInt(TUIKitConstants.Selection.DEFAULT_SELECT_ITEM_INDEX);
                radioGroup.check(checked);
                radioGroup.invalidate();
                break;
            default:
                finish();
                return;
        }
        mSelectionType = bundle.getInt(TUIKitConstants.Selection.TYPE);

        titleBar.setTitle(title, TitleBarLayout.POSITION.LEFT);
        titleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBar.getRightIcon().setVisibility(View.GONE);
        titleBar.getRightTitle().setText(getResources().getString(R.string.sure));
        titleBar.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                echoClick(title);
            }
        });
    }

    private void echoClick(String title) {
        switch (mSelectionType) {
            case TUIKitConstants.Selection.TYPE_TEXT:
                if (TextUtils.isEmpty(input.getText().toString()) && title.equals(getResources().getString(R.string.modify_group_name))) {
                    ToastUtil.toastLongMessage("没有输入昵称，请重新填写");
                    return;
                }
                if (title.equals(getResources().getString(R.string.modify_nick_name))||title.equals(getResources().getString(R.string.modify_nick_name_in_goup))) {
                    if (TextUtils.isEmpty(input.getText().toString().trim())){
                        ToastUtil.toastLongMessage("没有输入昵称，请重新填写");
                        return;
                    }
                    if (stringFilter(input.getText().toString())){
                        ToastUtil.toastLongMessage("昵称非法，请重新填写");
                        return;
                    }
                }

                if (sOnResultReturnListener != null) {
                    sOnResultReturnListener.onReturn(input.getText().toString());
                }
                break;
            case TUIKitConstants.Selection.TYPE_LIST:
                if (sOnResultReturnListener != null) {
                    sOnResultReturnListener.onReturn(radioGroup.getCheckedRadioButtonId());
                }
                break;
        }
        finish();
    }
    public  boolean stringFilter(String str) {
        String userId = UserInfo.getInstance().getUserId();
        String s  = str.toLowerCase();
        if (s.contains(userId)){
               return true;
           }
        return false;


        }
    @Override
    protected void onStop() {
        super.onStop();
        sOnResultReturnListener = null;
    }

    public interface OnResultReturnListener {
        void onReturn(Object res);
    }
}
