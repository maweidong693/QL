package com.weiwu.ql;

import androidx.fragment.app.Fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.tencent.qcloud.tim.uikit.component.UnreadCountTextView;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.weiwu.ql.base.BaseActivity;
import com.weiwu.ql.data.bean.CodeData;
import com.weiwu.ql.data.bean.MessageEvent;
import com.weiwu.ql.data.bean.MineInfoData;
import com.weiwu.ql.data.bean.NewMsgCount;
import com.weiwu.ql.data.bean.NewMsgData;
import com.weiwu.ql.data.bean.SocketData;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.repositories.MineRepository;
import com.weiwu.ql.data.request.AddFriendRequestBody;
import com.weiwu.ql.data.request.ClientIdBody;
import com.weiwu.ql.main.contact.ContactFragment;
import com.weiwu.ql.main.contact.chat.im.JWebSocketClient;
import com.weiwu.ql.main.contact.chat.im.JWebSocketClientService;
import com.weiwu.ql.main.find.MoreFragment;
import com.weiwu.ql.main.message.MessageFragment;
import com.weiwu.ql.main.mine.MineContract;
import com.weiwu.ql.main.mine.MineFragment;
import com.weiwu.ql.main.mine.MinePresenter;
import com.weiwu.ql.utils.IntentUtil;
import com.weiwu.ql.utils.Logger;
import com.weiwu.ql.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.weiwu.ql.view.Menu.REQUEST_CODE_SCAN;

public class MainActivity extends BaseActivity implements MineContract.IMineView {

    private TextView mConversationBtn;
    private TextView mContactBtn;
    private TextView mFindBtn;
    private TextView mProfileSelfBtn;
    private TextView mMsgUnread;
    private TextView mMsgCircleUnread;
    private View mLastTab;
    private String TAG = "MainActivity";
    private Context mContext;
    private MineContract.IMinePresenter mPresenter;
    private UnreadCountTextView mNewFriendCount;
    private UnreadCountTextView mNewMsgCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setPresenter(new MinePresenter(MineRepository.getInstance()));
//        mPresenter.getMineInfo();

        ImmersionBar.with(this).statusBarDarkFont(true).init();
        mContext = MainActivity.this;
        initView();
    }

    private void initView() {
//        int statusbarHeight = getStatusbarHeight(this);
//        try {
//            mBaseStatusBarView = findViewById(R.id.status_bar_view);
//            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mBaseStatusBarView.getLayoutParams();
//            layoutParams.height = statusbarHeight;
//            mBaseStatusBarView.setLayoutParams(layoutParams);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        mConversationBtn = findViewById(R.id.conversation);
        mContactBtn = findViewById(R.id.contact);
        mFindBtn = findViewById(R.id.find);
        mNewFriendCount = findViewById(R.id.tv_new_friend_count);
        mNewMsgCount = findViewById(R.id.tv_new_msg_count);
        mProfileSelfBtn = findViewById(R.id.mine);
        getSupportFragmentManager().beginTransaction().replace(R.id.empty_view, new MessageFragment()).commitAllowingStateLoss();
        if (mLastTab == null) {
            mLastTab = findViewById(R.id.conversation_btn_group);
        } else {
            // ???????????????????????????tab?????????????????????
            View tmp = mLastTab;
            mLastTab = null;
            tabClick(tmp);
            mLastTab = tmp;
        }
        //????????????
        startJWebSClientService();
        //????????????
        bindService();
        //????????????
        doRegisterReceiver();
        //????????????????????????
        checkNotification(mContext);

        mPresenter.getMineInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        jWebSClientService.onDestroy();
        client.close();
    }

    private JWebSocketClient client;
    private JWebSocketClientService.JWebSocketClientBinder binder;
    private JWebSocketClientService jWebSClientService;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e("MainActivity", "???????????????????????????");
            binder = (JWebSocketClientService.JWebSocketClientBinder) iBinder;
            jWebSClientService = binder.getService();
            client = jWebSClientService.client;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("MainActivity", "???????????????????????????");
        }
    };

    /**
     * ????????????
     */
    private void bindService() {
        Intent bindIntent = new Intent(mContext, JWebSocketClientService.class);
        bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    /**
     * ???????????????websocket??????????????????
     */
    private void startJWebSClientService() {
        Intent intent = new Intent(mContext, JWebSocketClientService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //android8.0????????????startForegroundService??????service
            startForegroundService(intent);
        } else {
            startService(intent);
        }
//        startService(intent);
    }

    @Override
    public void mineInfoReceive(MineInfoData data) {
        MineInfoData.DataDTO dto = data.getData();
        SPUtils.commitValue(AppConstant.USER, AppConstant.USER_ID, dto.getIm_id());
        SPUtils.commitValue(AppConstant.USER, AppConstant.USER_NAME, dto.getNick_name());
        SPUtils.commitValue(AppConstant.USER, AppConstant.USER_AVATAR, dto.getFace_url());
        SPUtils.commitValue(AppConstant.USER, AppConstant.USER_COUNTRY, dto.getCountry());
        SPUtils.commitValue(AppConstant.USER, AppConstant.USER_COUNTRY_CODE, dto.getNation_code());
//        SPUtils.commitValue(AppConstant.USER, AppConstant.USER_TYPE, String.valueOf(dto.getRole()));
//        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, SPUtils.getValue(AppConstant.USER, AppConstant.USER_ID) + ".db");
//        SQLiteDatabase database = helper.getWritableDatabase();
//        DaoMaster daoMaster = new DaoMaster(database);
//        MyApplication.getInstance().setDaoSession(daoMaster.newSession());


    }

    @Override
    public void newMsgCountResult(NewMsgData data) {
        NewMsgData.DataDTO o = (NewMsgData.DataDTO) data.getData();
        EventBus.getDefault().postSticky(new NewMsgCount(o.getFace_url(), o.getCount()));

    }

    @Override
    public void addFriendReceive(HttpResult data) {
        showToast("???????????????????????????");
    }

    @Override
    public void onSuccess(HttpResult data) {

    }

    @Override
    public void onFail(String msg, int code) {
        showToast(msg);
        if (code == 10001) {
            MyApplication.getInstance().loginAgain();
        }
    }

    @Override
    public void setPresenter(MineContract.IMinePresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return this;
    }

    private class ChatMessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            SocketData data = new Gson().fromJson(message, SocketData.class);
            if (data.getType().equals("login")) {
                mPresenter.init(new ClientIdBody(data.getClient_id()));
            } else if (data.getType().equals("addFriend")) {
                EventBus.getDefault().post(new MessageEvent("ss", 101));
                mNewFriendCount.setVisibility(View.VISIBLE);
                mNewFriendCount.setText(data.getNew_friend_count() + "");
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent msg) {
        Log.d(TAG, "onMessageEvent: aaaccc==" + msg.message);
        if (msg.code == 103) {
            if (msg.message.equals("0")) {
                mNewMsgCount.setVisibility(View.GONE);
            } else {
                mNewMsgCount.setVisibility(View.VISIBLE);
                mNewMsgCount.setText(msg.message);
            }
        } else if (msg.code == 166) {
            if (msg.message.equals("0")) {
                mNewFriendCount.setVisibility(View.GONE);
            } else {
                mNewFriendCount.setVisibility(View.VISIBLE);
                mNewFriendCount.setText(msg.message);
            }
        }
    }

    private ChatMessageReceiver chatMessageReceiver;


    /**
     * ??????????????????
     */
    private void doRegisterReceiver() {
        chatMessageReceiver = new ChatMessageReceiver();
        IntentFilter filter = new IntentFilter("com.xch.servicecallback.content");
        registerReceiver(chatMessageReceiver, filter);
    }

    public void tabClick(View view) {
        Logger.i(TAG, "tabClick last: " + mLastTab + " current: " + view);
        if (mLastTab != null && mLastTab.getId() == view.getId()) {
            return;
        }
        mLastTab = view;
        resetMenuState();
        Fragment current = null;
        switch (view.getId()) {
            case R.id.conversation_btn_group:
                current = new MessageFragment();
                mConversationBtn.setTextColor(getResources().getColor(R.color.tab_text_selected_color));
                mConversationBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_message), null, null);
                break;
            case R.id.contact_btn_group:
                current = new ContactFragment();
                mContactBtn.setTextColor(getResources().getColor(R.color.tab_text_selected_color));
                mContactBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_contact), null, null);
                break;
            case R.id.find_btn_group:
                current = new MoreFragment();
                mFindBtn.setTextColor(getResources().getColor(R.color.tab_text_selected_color));
                mFindBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_find), null, null);
                break;
            case R.id.myself_btn_group:
                current = new MineFragment();
                mProfileSelfBtn.setTextColor(getResources().getColor(R.color.tab_text_selected_color));
                mProfileSelfBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_mine), null, null);
                break;
            default:
                break;
        }

        if (current != null && !current.isAdded()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.empty_view, current).commitAllowingStateLoss();
            getSupportFragmentManager().executePendingTransactions();
        } else {
            Logger.w(TAG, "fragment added!");
        }
    }

    private void resetMenuState() {
        mConversationBtn.setTextColor(getResources().getColor(R.color.tab_text_normal_color));
        mConversationBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_message_not), null, null);
        mContactBtn.setTextColor(getResources().getColor(R.color.tab_text_normal_color));
        mContactBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_contact_not), null, null);
        mFindBtn.setTextColor(getResources().getColor(R.color.tab_text_normal_color));
        mFindBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_find_not), null, null);
        mProfileSelfBtn.setTextColor(getResources().getColor(R.color.tab_text_normal_color));
        mProfileSelfBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.ic_mine_not), null, null);
    }

    /**
     * ????????????????????????
     *
     * @param context
     */
    private void checkNotification(final Context context) {
        if (!isNotificationEnabled(context)) {
            new AlertDialog.Builder(context).setTitle("????????????")
                    .setMessage("???????????????????????????????????????????????????????????????????????????")
                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setNotification(context);
                        }
                    }).setNegativeButton("??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
        }
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param context
     */
    private void setNotification(Context context) {
        Intent localIntent = new Intent();
        //?????????????????????????????????????????????
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            localIntent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            localIntent.putExtra("app_package", context.getPackageName());
            localIntent.putExtra("app_uid", context.getApplicationInfo().uid);
        } else if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            localIntent.addCategory(Intent.CATEGORY_DEFAULT);
            localIntent.setData(Uri.parse("package:" + context.getPackageName()));
        } else {
            //4.4???????????????app????????????????????????????????????Action???????????????????????????????????????,
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= 9) {
                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
            } else if (Build.VERSION.SDK_INT <= 8) {
                localIntent.setAction(Intent.ACTION_VIEW);
                localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
                localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
            }
        }
        context.startActivity(localIntent);
    }

    /**
     * ??????????????????,?????????????????????????????????
     *
     * @param context
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean isNotificationEnabled(Context context) {

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // ???????????????/????????????
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(CodeUtils.RESULT_STRING);
                Log.d(TAG, "onActivityResult: " + content);
                if (content.length() > 10) {
                    CodeData codeData = new Gson().fromJson(content, CodeData.class);
                    String type = codeData.getType();
                    if (type.equals("web-login")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("type", "login");
                        bundle.putString("content", codeData.getId());
                        IntentUtil.redirectToNextActivity(this, WebLoginConfirmActivity.class, bundle);
//                        mPresenter.webLogin(new WebLoginRequestBody(SPUtils.getValue(AppConstant.USER, AppConstant.USER_TOKEN), codeData.getId()));
                    } else if (type.equals("add-friend")) {
                        mPresenter.addFriend(new AddFriendRequestBody(codeData.getId()));
                    }
                } else {
                    ToastUtil.toastLongMessage("??????????????????");
                }
                
                /*String[] split = content.split("#");
                if (split.length >= 2) {
                    String type = split[0];
                    switch (type) {
                        case "1"://?????????
//                            addFriend(split[1]);
                            break;
                        case "2"://web??????
                            Bundle bundle = new Bundle();
                            bundle.putString("type", "login");
                            bundle.putString("content", split[1]);
                            IntentUtil.redirectToNextActivity(this, WebLoginConfirmActivity.class, bundle);
                            break;

                        default:
                            ToastUtil.toastLongMessage("??????????????????");
                            break;

                    }
                } else {
                    ToastUtil.toastLongMessage("??????????????????");
//                    addFriend(split[0]);
                }*/

            }
        }
    }
}