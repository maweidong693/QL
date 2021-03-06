package com.weiwu.ql.main.contact.group.info;

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
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.weiwu.ql.MyApplication;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;
import com.weiwu.ql.data.bean.MessageEvent;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.repositories.GroupRepository;
import com.weiwu.ql.data.request.SetFriendRequestBody;
import com.weiwu.ql.data.request.UpdateGroupRequestBody;
import com.weiwu.ql.data.request.UpdateMineInfoRequestBody;
import com.weiwu.ql.main.contact.group.GroupContract;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectionActivity extends BaseActivity implements GroupContract.IUpdateGroupInfoView {

    private static OnResultReturnListener sOnResultReturnListener;

    private RadioGroup radioGroup;
    private EditText input;
    private int mSelectionType;
    private GroupContract.IUpdateGroupInfoPresenter mPresenter;
    private String id;
    private String mName;
    private int limit;

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
        setPresenter(new UpdateGroupPresenter(GroupRepository.getInstance()));
        final TitleBarLayout titleBar = findViewById(R.id.edit_title_bar);
        radioGroup = findViewById(R.id.content_list_rg);
        input = findViewById(R.id.edit_content_et);

        Bundle bundle = getIntent().getBundleExtra(TUIKitConstants.Selection.CONTENT);
        id = bundle.getString(TUIKitConstants.Selection.ID);
        final String title = bundle.getString(TUIKitConstants.Selection.TITLE);
        switch (bundle.getInt(TUIKitConstants.Selection.TYPE)) {
            case TUIKitConstants.Selection.TYPE_TEXT:
                radioGroup.setVisibility(View.GONE);
                String defaultString = bundle.getString(TUIKitConstants.Selection.INIT_CONTENT);
                limit = bundle.getInt(TUIKitConstants.Selection.LIMIT);
                if (!TextUtils.isEmpty(defaultString)) {
                    input.setText(defaultString);
                    input.setSelection(defaultString.length());
                }
                if (limit > 0) {
                    input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(limit)});
                }
                if (title.equals(getResources().getString(R.string.modify_nick_name)) || title.equals(getResources().getString(R.string.modify_nick_name_in_goup))) {
                    input.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            String editable = input.getText().toString();
//                            String regEx = "[^a-zA-Z0-9]";  //???????????????????????????
                            String regEx = "[^0-9a-zA-Z\u4e00-\u9fa5]";
                            Pattern p = Pattern.compile(regEx);
                            Matcher m = p.matcher(editable);
                            String str = m.replaceAll("").trim();    //????????????????????????????????????
                            if (!editable.equals(str)) {
                                input.setText(str);  //??????EditText?????????
                                input.setSelection(str.length()); //???????????????????????????????????????????????????????????????
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
                    radioButton.setPadding(0, 5, 0, 5);
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
        String inputText = input.getText().toString();
        switch (mSelectionType) {
            case TUIKitConstants.Selection.TYPE_TEXT:
                if (TextUtils.isEmpty(inputText) && title.equals(getResources().getString(R.string.modify_group_name))) {
                    ToastUtil.toastLongMessage("????????????????????????????????????");
                    return;
                }
                if (title.equals(getResources().getString(R.string.modify_nick_name)) || title.equals(getResources().getString(R.string.modify_nick_name_in_goup))) {
                    if (TextUtils.isEmpty(inputText.trim())) {
                        ToastUtil.toastLongMessage("????????????????????????????????????");
                        return;
                    }
                    /*if (stringFilter(input.getText().toString())) {
                        ToastUtil.toastLongMessage("??????????????????????????????");
                        return;
                    }*/
                }
                if (TextUtils.isEmpty(inputText) && title.equals("???????????????")) {
                    ToastUtil.toastLongMessage("???????????????????????????????????????");
                    return;
                }
                if (TextUtils.isEmpty(inputText) && title.equals("???????????????")) {
                    ToastUtil.toastLongMessage("???????????????????????????????????????");
                    return;
                }
                if (TextUtils.isEmpty(inputText) && title.equals("????????????")) {
                    ToastUtil.toastLongMessage("????????????????????????????????????");
                    return;
                }

                if (sOnResultReturnListener != null) {
                    mName = inputText;
                    if (limit == 200) {
                        mPresenter.updateGroupInfo(new UpdateGroupRequestBody(id, mName, null, null, null, null, null, null, null));
                    } else if (limit == 300) {
                        mPresenter.updateGroupInfo(new UpdateGroupRequestBody(id, null, null, inputText, null, null, null, null, null));
                    } else if (limit == 400) {
                        mPresenter.updateGroupInfo(new UpdateGroupRequestBody(id, null, inputText, null, null, null, null, null, null));
                    } else if (limit == 266) {
                        mPresenter.updateFriendInfo(new SetFriendRequestBody(id, null, null, null, inputText));
                    } else {
                        mPresenter.updateMineInfo(new UpdateMineInfoRequestBody(null, null, null, inputText, null, null));
                    }
                }
                break;
            case TUIKitConstants.Selection.TYPE_LIST:
                if (sOnResultReturnListener != null) {
                    mPresenter.updateGroupInfo(new UpdateGroupRequestBody(id, inputText, null, null, null, null, null, null, null));
                    sOnResultReturnListener.onReturn(radioGroup.getCheckedRadioButtonId());
                }
                break;
        }
        finish();
    }

    public boolean stringFilter(String str) {
        String userId = UserInfo.getInstance().getUserId();
        String s = str.toLowerCase();
        if (s.contains(userId)) {
            return true;
        }
        return false;


    }

    @Override
    protected void onStop() {
        super.onStop();
        sOnResultReturnListener = null;
    }

    @Override
    public void onSuccess(HttpResult data) {
        EventBus.getDefault().post(new MessageEvent("??????????????????", 111));
        sOnResultReturnListener.onReturn(mName);
    }

    @Override
    public void onFail(String msg, int code) {
        showToast(msg);
        if (code == 10001) {
            MyApplication.getInstance().loginAgain();
        }
    }

    @Override
    public void setPresenter(GroupContract.IUpdateGroupInfoPresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return this;
    }

    public interface OnResultReturnListener {
        void onReturn(Object res);
    }
}
