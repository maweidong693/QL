package com.weiwu.ql.main.mine.setting;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSON;
import com.tencent.common.Constant;
import com.tencent.common.http.BaseBean;
import com.tencent.common.http.HttpCallBack;
import com.tencent.common.http.YHttp;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.uikit.component.LineControllerView;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;
import com.weiwu.ql.data.bean.MineInfoData;
import com.weiwu.ql.data.bean.RangeBean;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.repositories.MineRepository;
import com.weiwu.ql.data.request.SetRangeRequestBody;
import com.weiwu.ql.data.request.UpdateMineInfoRequestBody;
import com.weiwu.ql.main.mine.MineContract;
import com.weiwu.ql.utils.IntentUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Joseph_Yan
 * on 2020/12/23
 */
public class PrivacySettingActivity extends BaseActivity implements MineContract.IPrivateSetView {

    private TitleBarLayout mTitleBar;
    private LineControllerView viewRange;
    private LineControllerView mModifyAllowTypeView;
    private LineControllerView mMessageReminder;
    private String mIndex;  //默认的值 ，并不是传的index
    private int mCurrentIndex; //当前要传的索引
    private ArrayList<String> rangeValue = new ArrayList<>();
    private ArrayList<String> rangeKey = new ArrayList<>();
    private Dialog dialog;
    private RadioGroup radioGroup;
    private MineContract.IPrivateSetPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_priva_set);
        setPresenter(new PrivateSetPresenter(MineRepository.getInstance()));
        initView();
    }

    private void initView() {
        mTitleBar = findViewById(R.id.title);
        mTitleBar.setTitle("隐私", TitleBarLayout.POSITION.LEFT);
        mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleBar.getRightIcon().setVisibility(View.GONE);
        viewRange = findViewById(R.id.view_range);
        mMessageReminder = findViewById(R.id.new_message_reminder);
        mModifyAllowTypeView = findViewById(R.id.modify_allow_type);
//        mModifyAllowTypeView.setChecked(true);
        mPresenter.getMineInfo();
        viewRange.setOnClickListener(v -> {
            Dialog();
        });
        mMessageReminder.setOnClickListener(view -> {
//            IntentUtil.redirectToNextActivity(this, NewMessageActivity.class);
        });
        mModifyAllowTypeView.setCheckListener((buttonView, isChecked) -> {
            if (!buttonView.isPressed()) {
                return;
            }
            mPresenter.updateMineInfo(new UpdateMineInfoRequestBody(isChecked ? "1" : "0"));


        });
//        getAllowType();
        getRange();
    }

    private void Dialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.my_allow_friend_dialog);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = getResources().getDisplayMetrics().widthPixels * 4 / 5;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        radioGroup = dialog.findViewById(R.id.content_list_rg);
        for (int i = 0; i < rangeValue.size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(rangeValue.get(i));
            radioButton.setId(i);
            radioButton.setTextSize(18);
            radioButton.setTextColor(getResources().getColor(com.tencent.qcloud.tim.uikit.R.color.black));
            radioButton.setPadding(25, 45, 0, 45);
            radioButton.setLayoutDirection(android.view.View.LAYOUT_DIRECTION_LTR);
            radioButton.setTextDirection(android.view.View.TEXT_DIRECTION_LTR);
            radioGroup.addView(radioButton, i, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        radioGroup.check(mCurrentIndex);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            viewRange.setContent(rangeValue.get(checkedId));
            mIndex = rangeKey.get(checkedId);
            mCurrentIndex = checkedId;
            mPresenter.setRange(new SetRangeRequestBody(Integer.parseInt(mIndex)));
            dialog.dismiss();

            /*Map map = new HashMap();
            map.put("key", mIndex);
            dialog.dismiss();
            YHttp.obtain().post(Constant.URL_SHOW_RANGE, map, new HttpCallBack<BaseBean>() {
                @Override
                public void onSuccess(BaseBean bean) {

                }

                @Override
                public void onFailed(String error) {

                }
            });*/

        });
        radioGroup.invalidate();
        dialog.findViewById(R.id.btn_pos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();

    }

    private void getRange() {
        mPresenter.getRange();
        /*YHttp.obtain().post(Constant.URL_RANGE, null, new HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean bean) {
                if (bean.getData() != null) {
                    *//*RangeBean rangeBean = JSON.parseObject(bean.getData().toString(), RangeBean.class);
                    int defaultX = rangeBean.getDefault();
                    mIndex = defaultX+"";
                    List<RangeBean.ListBean> list = rangeBean.getList();
                    if (list!=null&&list.size()>0){
                        rangeKey.clear();
                        rangeValue.clear();
                        for (int i = 0; i < list.size(); i++) {
                            if (mIndex.equals(list.get(i).getKey()+"")){
                                mCurrentIndex = i;
                                viewRange.setContent(list.get(i).getValue());
                            }
                            rangeKey.add(list.get(i).getKey()+"");
                            rangeValue.add(list.get(i).getValue());
                        }
                    }*//*
                }
            }

            @Override
            public void onFailed(String error) {

            }
        });*/

    }

    @Override
    public void setPresenter(MineContract.IPrivateSetPresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return this;
    }

    @Override
    public void mineInfoReceive(MineInfoData data) {
        mModifyAllowTypeView.setChecked(data.getData().getIs_check_friend() == 1);
    }

    @Override
    public void rangeReceive(RangeBean data) {
        RangeBean.DataDTO rangeBean = data.getData();
        int defaultX = rangeBean.getDefaultX();
        mIndex = defaultX + "";
        List<RangeBean.DataDTO.ListDTO> list = rangeBean.getList();
        if (list != null && list.size() > 0) {
            rangeKey.clear();
            rangeValue.clear();
            for (int i = 0; i < list.size(); i++) {
                if (mIndex.equals(list.get(i).getKey() + "")) {
                    mCurrentIndex = i;
                    viewRange.setContent(list.get(i).getValue());
                }
                rangeKey.add(list.get(i).getKey() + "");
                rangeValue.add(list.get(i).getValue());
            }
        }
    }

    @Override
    public void onSuccess(HttpResult data) {

    }

    @Override
    public void onFail(String msg, int code) {
        showToast(msg);
    }
}
