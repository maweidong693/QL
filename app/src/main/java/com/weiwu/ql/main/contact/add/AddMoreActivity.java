package com.weiwu.ql.main.contact.add;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.tencent.common.UserInfo;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.utils.SoftKeyBoardUtil;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.weiwu.ql.MyApplication;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.repositories.ContactRepository;
import com.weiwu.ql.data.request.AddFriendRequestBody;
import com.weiwu.ql.main.contact.ContactContract;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddMoreActivity extends BaseActivity implements ContactContract.IAddFriendView {

    private static final String TAG = AddMoreActivity.class.getSimpleName();

    private TitleBarLayout mTitleBar;
    private EditText mUserID;
    private EditText mAddWording;
    private boolean mIsGroup;
    private ContactContract.IAddFriendPresenter mPresenter;
    private Button mAdd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            mIsGroup = getIntent().getExtras().getBoolean(TUIKitConstants.GroupType.GROUP);
        }
        setContentView(R.layout.contact_add_activity);
        setPresenter(new AddFriendPresenter(ContactRepository.getInstance()));

        mTitleBar = findViewById(R.id.add_friend_titlebar);
        mTitleBar.setTitle(mIsGroup ? getResources().getString(R.string.add_group) : getResources().getString(R.string.add_friend), TitleBarLayout.POSITION.LEFT);
        mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleBar.getRightGroup().setVisibility(View.GONE);

        mUserID = findViewById(R.id.user_id);
        mUserID.setHint(mIsGroup ? getResources().getString(R.string.hint_add_user_id) : getResources().getString(R.string.hint_add_user_id_or_phone));
        mAddWording = findViewById(R.id.add_wording);
        mAdd = findViewById(R.id.bt_add);
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
    }

    public void add() {
        if (mIsGroup) {
            addGroup();
        } else {
            addFriend();
        }
    }

    public void addFriend() {
        String id = mUserID.getText().toString();
        if (TextUtils.isEmpty(id)) {
            return;
        }
        if (TextUtils.equals(id, UserInfo.getInstance().getUserId())) {
            ToastUtil.toastShortMessage("不能添加自己");
            return;
        }
        /*if (isNumeric(id)) {
            //全是数字可能是手机,去服务器查
            Map map = new HashMap();
            map.put("tel", id);
            YHttp.obtain().post(Constant.URL_GET_IMID, map, new HttpCallBack<BaseBean>() {
                @Override
                public void onSuccess(BaseBean baseBean) {
                    if (baseBean.getData() == null) {
                        return;
                    }
                    JSONObject object = JSON.parseObject(baseBean.getData().toString());
                    if (object != null) {
                        String uid = object.getString("im_id");
                        if (!TextUtils.isEmpty(uid)) {
                            if (TextUtils.equals(uid, UserInfo.getInstance().getUserId())) {
                                ToastUtil.toastShortMessage("不能添加自己");
                                return;
                            }
                            addFriend(uid);
                        } else {
                            ToastUtil.toastShortMessage(baseBean.getMsg());
                        }
                    } else {
                        ToastUtil.toastShortMessage(baseBean.getMsg());
                    }
                }

                @Override
                public void onFailed(String error) {

                }
            });
        } else {*/
        addFriend(id);
//        }
    }

    private void addFriend(String id) {
        mPresenter.addFriend(new AddFriendRequestBody(id, mAddWording.getText().toString()));
    }

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public void addGroup() {
        String id = mUserID.getText().toString();
        if (TextUtils.isEmpty(id)) {
            return;
        }

    }

    @Override
    public void finish() {
        super.finish();
        SoftKeyBoardUtil.hideKeyBoard(mUserID.getWindowToken());
    }

    @Override
    public void addFriendReceive(HttpResult data) {
        if (data != null) {
            showToast(data.getMessage());
            finish();
        }
    }

    @Override
    public void onFail(String msg, int code) {
        showToast(msg);
        if (code == 10000) {
            MyApplication.loginAgain();
        }
    }

    @Override
    public void setPresenter(ContactContract.IAddFriendPresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return this;
    }
}
