package com.tencent.qcloud.tim.uikit.modules.chat.base;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.luck.picture.lib.config.PictureMimeType;
import com.tencent.common.Constant;
import com.tencent.common.http.HttpCallBack;
import com.tencent.common.http.YHttp;
import com.tencent.imsdk.v2.V2TIMFaceElem;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMTextElem;
import com.tencent.imsdk.v2.V2TIMVideoElem;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.component.AudioPlayer;
import com.tencent.qcloud.tim.uikit.modules.chat.ChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.interfaces.IChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.interfaces.IChatProvider;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.input.InputLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.MessageLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.MessageListAdapter;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfo;
import com.tencent.qcloud.tim.uikit.modules.group.info.MessageForwardActivity;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageMute;
import com.tencent.qcloud.tim.uikit.modules.message.MessageQuoteInfo;
import com.tencent.qcloud.tim.uikit.utils.BackgroundTasks;
import com.tencent.qcloud.tim.uikit.utils.FileUtil;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import static com.tencent.qcloud.tim.uikit.modules.message.MessageInfoUtil.checkMsgIsQuote;


public abstract class AbsChatLayout extends ChatLayoutUI implements IChatLayout {

    public static final String TAG= AbsChatLayout.class.getName();
    private ChatLayout.OnMessageItemClickListener  messageItemClickListener ;
    protected MessageListAdapter mAdapter;

    private AnimationDrawable mVolumeAnim;
    private Runnable mTypingRunnable = null;
    private ChatProvider.TypingListener mTypingListener = new ChatProvider.TypingListener() {
        @Override
        public void onTyping() {
            final String oldTitle = getTitleBar().getMiddleTitle().getText().toString();
            getTitleBar().getMiddleTitle().setText(R.string.typing);
            if (mTypingRunnable == null) {
                mTypingRunnable = new Runnable() {
                    @Override
                    public void run() {
                        getTitleBar().getMiddleTitle().setText(oldTitle);
                    }
                };
            }
            getTitleBar().getMiddleTitle().removeCallbacks(mTypingRunnable);
            getTitleBar().getMiddleTitle().postDelayed(mTypingRunnable, 3000);
        }
    };
    public AbsChatLayout(Context context) {
        super(context);
    }

    public AbsChatLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AbsChatLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initListener() {
        getMessageLayout().setPopActionClickListener(new MessageLayout.OnPopActionClickListener() {
            @Override
            public void onCopyClick(int position, MessageInfo msg) {
                Log.d(TAG, "onCopyClick: "+position+"   "+msg);
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboard == null || msg == null) {
                    return;
                }
                if (msg.getMsgType() == MessageInfo.MSG_TYPE_TEXT) {
//                    V2TIMTextElem txtEle = msg.getTimMessage().getTextElem();
//                    if (checkMsgIsQuote(msg.getQuoteMsg())){
//                        Gson gson = new Gson();
//                        MessageQuoteInfo quoteInfo = gson.fromJson(msg.getQuoteMsg(), MessageQuoteInfo.class);
//                        ClipData clip = ClipData.newPlainText("message", quoteInfo.getMessage());
//                        clipboard.setPrimaryClip(clip);
//                    }else {
//                        ClipData clip = ClipData.newPlainText("message", msg.getExtra().toString());
//                        clipboard.setPrimaryClip(clip);
//                    }
                    ClipData clip = ClipData.newPlainText("message", msg.getExtra().toString());
                    clipboard.setPrimaryClip(clip);

                }
            }

            @Override
            public void onSendMessageClick(MessageInfo msg, boolean retry) {
                sendMessage(msg, retry);
            }

            @Override
            public void onDeleteMessageClick(int position, MessageInfo msg) {
                deleteMessage(position, msg);
            }

            @Override
            public void onRevokeMessageClick(int position, MessageInfo msg, GroupInfo groupInfo) {
                if (msg.isSelf()){
                    revokeMessage(position, msg);
                }else {
                    Map map = new HashMap();
                    map.put("GroupId",groupInfo.getId());
                    map.put("MsgSeq",msg.getTimMessage().getSeq());
                    YHttp.obtain().post(Constant.URL_GROUP_MSG_RECALL, map, new HttpCallBack() {
                        @Override
                        public void onSuccess(Object o) {

                        }

                        @Override
                        public void onFailed(String error) {

                        }
                    });

                }
            }

            @Override
            public void onMessageForwardClick(int position, MessageInfo msg, GroupInfo groupInfo) {
                Log.d(TAG, "onMessageForwardClick: "+position+msg+groupInfo);
                if (messageItemClickListener!=null){
                    messageItemClickListener.onMessageForwardClick(msg);
                }
            }

            @Override
            public void onMessageFavoritesClick(int position, MessageInfo msg, GroupInfo groupInfo) {
                if (messageItemClickListener!=null){
                    messageItemClickListener.onMessageFavoritesClick(msg);
                }
            }

            @Override
            public void onMessageQuoteClick(int position, MessageInfo msg, GroupInfo groupInfo) {

                if (messageItemClickListener!=null){
                    messageItemClickListener.onMessageQuoteClick(msg);
                }
            }
        });
        getMessageLayout().setLoadMoreMessageHandler(new MessageLayout.OnLoadMoreHandler() {
            @Override
            public void loadMore() {
                loadMessages();
            }
        });
        getMessageLayout().setEmptySpaceClickListener(new MessageLayout.OnEmptySpaceClickListener() {
            @Override
            public void onClick() {
                getInputLayout().hideSoftInput();
            }
        });

        /**
         * 设置消息列表空白处点击处理
         */
        getMessageLayout().addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_UP) {
                    View child = rv.findChildViewUnder(e.getX(), e.getY());
                    if (child == null) {
                        getInputLayout().hideSoftInput();
                    } else if (child instanceof ViewGroup) {
                        ViewGroup group = (ViewGroup) child;
                        final int count = group.getChildCount();
                        float x = e.getRawX();
                        float y = e.getRawY();
                        View touchChild = null;
                        for (int i = count - 1; i >= 0; i--) {
                            final View innerChild = group.getChildAt(i);
                            int position[] = new int[2];
                            innerChild.getLocationOnScreen(position);
                            if (x >= position[0]
                                    && x <= position[0] + innerChild.getMeasuredWidth()
                                    && y >= position[1]
                                    && y <= position[1] + innerChild.getMeasuredHeight()) {
                                touchChild = innerChild;
                                break;
                            }
                        }
                        if (touchChild == null) {
                            getInputLayout().hideSoftInput();
                        }
                    }
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        getInputLayout().setChatInputHandler(new InputLayout.ChatInputHandler() {
            @Override
            public void onInputAreaClick() {
                post(new Runnable() {
                    @Override
                    public void run() {
                        scrollToEnd();
                    }
                });
            }

            @Override
            public void onRecordStatusChanged(int status) {
                switch (status) {
                    case RECORD_START:
                        startRecording();
                        break;
                    case RECORD_STOP:
                        stopRecording();
                        break;
                    case RECORD_CANCEL:
                        cancelRecording();
                        break;
                    case RECORD_TOO_SHORT:
                    case RECORD_FAILED:
                        stopAbnormally(status);
                        break;
                    default:
                        break;
                }
            }

            private void startRecording() {
                post(new Runnable() {
                    @Override
                    public void run() {
                        AudioPlayer.getInstance().stopPlay();
                        mRecordingGroup.setVisibility(View.VISIBLE);
                        mRecordingIcon.setImageResource(R.drawable.recording_volume);
                        mVolumeAnim = (AnimationDrawable) mRecordingIcon.getDrawable();
                        mVolumeAnim.start();
                        mRecordingTips.setTextColor(Color.WHITE);
                        mRecordingTips.setText("手指上滑，取消发送");
                    }
                });
            }

            private void stopRecording() {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mVolumeAnim.stop();
                        mRecordingGroup.setVisibility(View.GONE);
                    }
                }, 500);
            }

            private void stopAbnormally(final int status) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        mVolumeAnim.stop();
                        mRecordingIcon.setImageResource(R.drawable.ic_volume_dialog_length_short);
                        mRecordingTips.setTextColor(Color.WHITE);
                        if (status == RECORD_TOO_SHORT) {
                            mRecordingTips.setText("说话时间太短");
                        } else {
                            mRecordingTips.setText("录音失败");
                        }
                    }
                });
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecordingGroup.setVisibility(View.GONE);
                    }
                }, 1000);
            }

            private void cancelRecording() {
                post(new Runnable() {
                    @Override
                    public void run() {
                        mRecordingIcon.setImageResource(R.drawable.ic_volume_dialog_cancel);
                        mRecordingTips.setText("松开手指，取消发送");
                    }
                });
            }
        });
    }

    @Override
    public void initDefault() {
        getTitleBar().getLeftGroup().setVisibility(View.VISIBLE);
        getTitleBar().setOnLeftClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).finish();
                }
            }
        });
        getInputLayout().setMessageHandler(new InputLayout.MessageHandler() {
            @Override
            public void sendMessage(MessageInfo msg) {
                AbsChatLayout.this.sendMessage(msg, false);
            }
        });
        getInputLayout().clearCustomActionList();
        if (getMessageLayout().getAdapter() == null) {
            mAdapter = new MessageListAdapter();
            getMessageLayout().setAdapter(mAdapter);
        }
        initListener();
    }
    @Override
    public void setParentLayout(Object parentContainer) {

    }

    public void scrollToEnd() {
        getMessageLayout().scrollToEnd();
    }

    public void setDataProvider(IChatProvider provider) {
        if (provider != null) {
            ((ChatProvider) provider).setTypingListener(mTypingListener);
        }
        if (mAdapter != null) {
            mAdapter.setDataSource(provider);
            getChatManager().setLastMessageInfo(mAdapter.getItemCount() > 0 ? mAdapter.getItem(1) : null);
        }
    }

    public abstract ChatManagerKit getChatManager();

    @Override
    public void loadMessages() {
        loadChatMessages(mAdapter.getItemCount() > 0 ? mAdapter.getItem(1) : null);
    }

    public void loadChatMessages(final MessageInfo lastMessage) {
        getChatManager().loadChatMessages(lastMessage, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                if (lastMessage == null && data != null) {
                    setDataProvider((ChatProvider) data);
                }
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ToastUtil.toastLongMessage(errMsg);
                if (lastMessage == null) {
                    setDataProvider(null);
                }
            }
        });
    }

    protected void deleteMessage(int position, MessageInfo msg) {
        getChatManager().deleteMessage(position, msg);
    }

    protected void revokeMessage(int position, MessageInfo msg) {
        getChatManager().revokeMessage(position, msg);
    }

    @Override
    public void sendMessage(MessageInfo msg, boolean retry) {
        getChatManager().sendMessage(msg, retry, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                BackgroundTasks.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scrollToEnd();
                    }
                });
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ToastUtil.toastLongMessage(errMsg);
            }
        });
    }


    @Override
    public void exitChat() {
        getTitleBar().getMiddleTitle().removeCallbacks(mTypingRunnable);
        AudioPlayer.getInstance().stopRecord();
        AudioPlayer.getInstance().stopPlay();
        if (getChatManager() != null) {
            getChatManager().destroyChat();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        exitChat();
    }

    public void setMessageItemClickListener(ChatLayout.OnMessageItemClickListener messageItemClickListener) {
        this.messageItemClickListener = messageItemClickListener;
    }
}