package com.weiwu.ql.main.message;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.weiwu.ql.AppConstant;
import com.weiwu.ql.MyApplication;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseFragment;
import com.weiwu.ql.data.bean.MessageEvent;
import com.weiwu.ql.data.network.HttpResult;
import com.weiwu.ql.data.request.DelChatBody;
import com.weiwu.ql.main.contact.chat.modle.MessageListData;
import com.weiwu.ql.data.repositories.ContactRepository;
import com.weiwu.ql.main.contact.ContactContract;
import com.weiwu.ql.main.contact.chat.ChatActivity;
import com.weiwu.ql.main.contact.chat.im.JWebSocketClient;
import com.weiwu.ql.main.contact.chat.im.JWebSocketClientService;
import com.weiwu.ql.view.Menu;

import org.greenrobot.eventbus.EventBus;

import java.util.Random;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/22 13:35 
 */
public class MessageFragment extends BaseFragment implements ContactContract.IMessageView {

    private JWebSocketClient client;
    private JWebSocketClientService.JWebSocketClientBinder binder;
    private JWebSocketClientService jWebSClientService;
    private ChatMessageReceiver chatMessageReceiver;

    private TitleBarLayout mConversationTitle;
    private RecyclerView mRvMessageList;
    private MessageAdapter mMessageAdapter;
    private Menu mMenu;
    private ContactContract.IMessagePresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragemtn_message;
    }

    @Override
    public void onViewCreated(View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setPresenter(new MessagePresenter(ContactRepository.getInstance()));
        initView(view);
        //绑定服务
        bindService();
        //注册广播
        doRegisterReceiver();
    }

    private static final String TAG = "MessageFragment";

    @Override
    public void messageListReceive(MessageListData data) {
        if (data != null && data.getData() != null) {
            mMessageAdapter.setList(data.getData().getList());
            EventBus.getDefault().post(new MessageEvent(data.getData().getUnread_count() + "", 103));
        }
    }

    @Override
    public void onSuccess(HttpResult data) {
        mPresenter.getMessageList();
    }

    @Override
    public void onFail(String msg, int code) {
        showToast(msg);
        if (code == 10001) {
            MyApplication.getInstance().loginAgain();
        }
    }

    @Override
    public void setPresenter(ContactContract.IMessagePresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return getActivity();
    }


    private class ChatMessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            mPresenter.getMessageList();
        }
    }

    public static int getNum(int endNum) {
        if (endNum > 0) {
            Random random = new Random();
            return random.nextInt(endNum);
        }
        return 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getMessageList();
        Log.d(TAG, "onResume: aaa");
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e("MainActivity", "服务与活动成功绑定");
            binder = (JWebSocketClientService.JWebSocketClientBinder) iBinder;
            jWebSClientService = binder.getService();
            client = jWebSClientService.client;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("MainActivity", "服务与活动成功断开");
        }
    };

    /**
     * 绑定服务
     */
    private void bindService() {
        Intent bindIntent = new Intent(getContext(), JWebSocketClientService.class);
        getActivity().bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    /**
     * 动态注册广播
     */
    private void doRegisterReceiver() {
        chatMessageReceiver = new ChatMessageReceiver();
        IntentFilter filter = new IntentFilter("com.xch.servicecallback.content");
        getActivity().registerReceiver(chatMessageReceiver, filter);
    }

    private void initView(View view) {
        mConversationTitle = (TitleBarLayout) view.findViewById(R.id.conversation_title);
        mRvMessageList = (RecyclerView) view.findViewById(R.id.rv_message_list);

        mConversationTitle.setTitle(getResources().getString(com.tencent.qcloud.tim.uikit.R.string.conversation_title), TitleBarLayout.POSITION.MIDDLE);
        mConversationTitle.getLeftGroup().setVisibility(View.GONE);
        mConversationTitle.setRightIcon(com.tencent.qcloud.tim.uikit.R.drawable.conversation_more);
        mMenu = new Menu(getActivity(), mConversationTitle, Menu.MENU_TYPE_CONVERSATION);

        mRvMessageList.setLayoutManager(new LinearLayoutManager(getContext()));
        mMessageAdapter = new MessageAdapter();
        mRvMessageList.setAdapter(mMessageAdapter);

        mPresenter.getMessageList();

        mMessageAdapter.setChatClickListener(new MessageAdapter.IChatClickListener() {
            @Override
            public void mClick(MessageListData.DataDTO.ListDTO data) {
                ContactItemBean bean = new ContactItemBean(data.getToid(), data.getName());
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra(TUIKitConstants.ProfileType.CONTENT, bean);
                intent.putExtra(AppConstant.IN, 1);
                intent.putExtra(AppConstant.CHAT_TYPE, data.getChat_type());
                intent.putExtra(AppConstant.CHAT_ID, data.getId());
                startActivity(intent);
//                getActivity().finish();
            }
        });

        mMessageAdapter.setMessageLongClickListener(new MessageAdapter.IMessageLongClickListener() {
            @Override
            public void mLongClick(MessageListData.DataDTO.ListDTO data,View view) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_message, popupMenu.getMenu());
                android.view.Menu menu = popupMenu.getMenu();
                MenuItem withdrawMenuItem = menu.getItem(1);
                MenuItem mQuote = menu.getItem(0);
                MenuItem mCollection = menu.getItem(2);
                withdrawMenuItem.setVisible(false);
                mQuote.setVisible(false);
                mCollection.setVisible(false);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.tv_message_delete:
                                mPresenter.delChat(new DelChatBody(data.getId()));
                                break;
                        }
                        return false;
                    }
                });
            }
        });
        initTitleAction();
    }

    private void initTitleAction() {
        mConversationTitle.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMenu.isShowing()) {
                    mMenu.hide();
                } else {
                    mMenu.show();
                }
            }
        });
    }
}
