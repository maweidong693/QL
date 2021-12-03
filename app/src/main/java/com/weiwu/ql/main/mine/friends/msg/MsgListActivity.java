package com.weiwu.ql.main.mine.friends.msg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.tencent.common.Constant;
import com.tencent.common.http.BaseBean;
import com.tencent.common.http.HttpCallBack;
import com.tencent.common.http.YHttp;
import com.tencent.qcloud.tim.uikit.base.ITitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.dialog.TUIKitDialog;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;
import com.weiwu.ql.data.bean.NewMsgCount;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.repositories.FindRepository;
import com.weiwu.ql.data.request.GroupInfoRequestBody;
import com.weiwu.ql.data.request.RemindRequestBody;
import com.weiwu.ql.main.mine.friends.FriendsContract;
import com.weiwu.ql.main.mine.friends.adapter.NotRedMsgsAdapter;
import com.weiwu.ql.main.mine.friends.data.MsgData;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息列表
 */
public class MsgListActivity extends BaseActivity implements FriendsContract.IMsgView {

    private RecyclerView mRecyclerView;
    private TextView empyView;
    private NotRedMsgsAdapter mAdapter;
    private TitleBarLayout mTitleBar;
    private boolean notice; //true  点通知来的 false 点我的朋友圈顶部
    private FriendsContract.IMsgPresenter mPresenter;
    private List<MsgData.MsgEntity> msgEntities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setPresenter(new NewMsgPresenter(FindRepository.getInstance()));
        Intent intent = getIntent();
        if (intent != null) {
            notice = intent.getBooleanExtra("notice", false);
        }
        initView();
        getData();
    }

    public void initView() {
        mTitleBar = findViewById(R.id.friend_title_bar);
        mTitleBar.getRightIcon().setVisibility(View.GONE);
        mTitleBar.setOnLeftClickListener(v -> finish());
        mTitleBar.setTitle("消息", ITitleBarLayout.POSITION.LEFT);
        mTitleBar.setTitle("清空", ITitleBarLayout.POSITION.RIGHT);
        mRecyclerView = findViewById(R.id.mXRecyclerView);
        empyView = findViewById(R.id.empy_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new NotRedMsgsAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
//            List<Integer> list = new ArrayList<>();
//            list.add(mAdapter.getData().get(position).getId());
//            clearNotRedMsg(list, false);
            Bundle bundle = new Bundle();
            bundle.putString("id", mAdapter.getData().get(position).getMsgId() + "");
//            IntentUtil.redirectToNextActivity(this, FriendsDetailsActivity.class, bundle);
        });
        mTitleBar.setOnRightClickListener(v ->
                {
                    //清空
                    if (mAdapter.getData() != null && mAdapter.getData().size() == 0) {
                        return;
                    }
                    new TUIKitDialog(this)
                            .builder()
                            .setCancelable(true)
                            .setCancelOutside(true)
                            .setTitle("清空所有消息？")
                            .setDialogWidth(0.75f)
                            .setPositiveButton("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    List<String> list = new ArrayList<>();
                                    for (MsgData.MsgEntity data : mAdapter.getData()) {
                                        list.add(data.getId() + "");
                                    }
                                    clearNotRedMsg(list, true);
                                }
                            })
                            .setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .show();
                }
        );
    }

    private void getData() {
        mPresenter.getMsgList();
        /*YHttp.obtain().post(Constant.URL_NOTREAD, null, new HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean bean) {
                if (bean.getData() == null) {
                    empyView.setVisibility(View.VISIBLE);
                    return;
                }
                *//*List<MsgEntity> msgEntities = JSON.parseArray(bean.getData().toString(), MsgEntity.class);
                if (msgEntities!=null&&msgEntities.size()>0){
                    if (notice){
                        List<Integer>  readnotice = new ArrayList<>();
                        for (int i = 0; i < msgEntities.size(); i++) {
                            readnotice.add(msgEntities.get(i).getId());
                        }
                        readnotice(readnotice);
                    }
                    empyView.setVisibility(View.GONE);
                    mAdapter.setList(msgEntities);
                }else {
                    empyView.setVisibility(View.VISIBLE);
                    mAdapter.setList(new ArrayList<>());
                }*//*

            }

            @Override
            public void onFailed(String error) {

            }
        });*/

    }

    /**
     * 标记未读消息不通知
     */
    private void readnotice(List<Integer> list) {
        Map map = new HashMap();
        map.put("id", list);
        YHttp.obtain().post(Constant.URL_READ_NOTICE, map, new HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean bean) {


            }

            @Override
            public void onFailed(String error) {

            }
        });

    }

    public String listToString(List list, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(separator);
        }
        return list.isEmpty() ? "" : sb.toString().substring(0, sb.toString().length() - 1);
    }

    private void clearNotRedMsg(List<String> list, Boolean isClear) {
        mPresenter.readMsg(new RemindRequestBody(listToString(list, ',')));
    }

    @Override
    public void setPresenter(FriendsContract.IMsgPresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return this;
    }

    @Override
    public void msgReceive(MsgData data) {
        msgEntities = data.getData();
        if (data.getData() != null && data.getData().size() > 0) {
            if (notice) {
                List<Integer> readnotice = new ArrayList<>();
                for (int i = 0; i < msgEntities.size(); i++) {
                    readnotice.add(msgEntities.get(i).getId());
                }
//                readnotice(readnotice);
            }
            empyView.setVisibility(View.GONE);
            mAdapter.setList(msgEntities);
        } else {
            empyView.setVisibility(View.VISIBLE);
            mAdapter.setList(new ArrayList<>());
        }
    }

    @Override
    public void onSuccess(HttpResult data) {
        EventBus.getDefault().post(new NewMsgCount(null, 0));
        finish();
    }

    @Override
    public void onFail(String msg, int code) {
        showToast(msg);
    }
}
