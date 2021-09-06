package com.weiwu.ql.main.contact.chat.adapter;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.weiwu.ql.R;
import com.weiwu.ql.main.contact.chat.modle.ChatMessage;
import com.weiwu.ql.main.contact.chat.modle.NoticeData;
import com.weiwu.ql.main.contact.chat.modle.NoticeOrderData;
import com.weiwu.ql.utils.SystemFacade;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/16 15:43 
 */
public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "ChatMessageAdapter";

    private List<ChatMessage> mList = new ArrayList<>();

    public void setList(List<ChatMessage> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new ReceiveMessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_receive_text, parent, false));
        } else {
            return new SendMessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_send_text, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage mChatMessage = mList.get(position);
//        int isMeSend = mChatMessage.getIsMeSend();
        Context context = holder.itemView.getContext();
        if (holder instanceof ReceiveMessageViewHolder) {
            ReceiveMessageViewHolder receiveHolder = (ReceiveMessageViewHolder) holder;
            if (mChatMessage.getType().equals("message")) {
                String msgType = mChatMessage.getMsgType();

                if (msgType.equals("msg") || msgType.equals("voi") || msgType.equals("quote")) {
                    receiveHolder.mReceiveContent.setBackground(context.getResources().getDrawable(R.drawable.jmui_msg_receive_bg));
                } else {
                    receiveHolder.mReceiveContent.setBackground(context.getResources().getDrawable(R.color.transparent));
                }
                receiveHolder.mReceiveNickName.setText(mChatMessage.getMemberNickname());
                receiveHolder.mReceiveMsg.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (mMessageLongClickListener != null) {
                            mMessageLongClickListener.mLongClick(mChatMessage, receiveHolder.mReceiveContent);
                        }
                        return true;
                    }
                });
                receiveHolder.mReceiveIv.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (mMessageLongClickListener != null) {
                            mMessageLongClickListener.mLongClick(mChatMessage, receiveHolder.mReceiveContent);
                        }
                        return true;
                    }
                });
                if (msgType.equals("msg")) {
                    String content = mChatMessage.getTextMsg();
                    receiveHolder.mReceiveMsg.setVisibility(View.VISIBLE);
                    receiveHolder.mReceiveIv.setVisibility(View.GONE);
                    receiveHolder.mReceiveVideoPlay.setVisibility(View.GONE);
                    receiveHolder.mReceiveVoice.setVisibility(View.GONE);
                    receiveHolder.mReceiveMsg.setText(SystemFacade.base64ToString(content));
                    receiveHolder.mReceiveQuote.setVisibility(View.GONE);
                    receiveHolder.mReceiveIv.setOnClickListener(null);

                } else if (msgType.equals("quote")) {
                    String content = mChatMessage.getTextMsg();
                    receiveHolder.mReceiveMsg.setVisibility(View.VISIBLE);
                    receiveHolder.mReceiveIv.setVisibility(View.GONE);
                    receiveHolder.mReceiveVideoPlay.setVisibility(View.GONE);
                    receiveHolder.mReceiveVoice.setVisibility(View.GONE);
                    receiveHolder.mReceiveMsg.setText(SystemFacade.base64ToString(content));
                    receiveHolder.mReceiveIv.setOnClickListener(null);
                    receiveHolder.mReceiveQuote.setVisibility(View.VISIBLE);
                    String quoteMessageInfo = mChatMessage.getQuoteMessageInfo();
                    ChatMessage chatMessage = new Gson().fromJson(quoteMessageInfo, ChatMessage.class);
                    if (chatMessage != null) {
                        String quoteContent = "";
                        if (chatMessage.getMsgType().equals("img")) {
                            quoteContent = "[图片]";
                            receiveHolder.mReceiveQuote.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mVideoClickListener != null) {
                                        mVideoClickListener.mVideClick(chatMessage);
                                    }
                                }
                            });
                        } else if (chatMessage.getMsgType().equals("video")) {
                            quoteContent = "[视频]";
                            receiveHolder.mReceiveQuote.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mVideoClickListener != null) {
                                        mVideoClickListener.mVideClick(chatMessage);
                                    }
                                }
                            });

                        } else {
                            quoteContent = SystemFacade.base64ToString(chatMessage.getTextMsg());
                        }
                        receiveHolder.mReceiveQuote.setText(chatMessage.getMemberNickname() + ":" + quoteContent);
                    }

                } else if (msgType.equals("img")) {

                    receiveHolder.mReceiveMsg.setVisibility(View.GONE);
                    receiveHolder.mReceiveIv.setVisibility(View.VISIBLE);
                    receiveHolder.mReceiveVideoPlay.setVisibility(View.GONE);
                    receiveHolder.mReceiveVoice.setVisibility(View.GONE);
                    receiveHolder.mReceiveQuote.setVisibility(View.GONE);

                    Glide.with(context).load(mChatMessage.getUrl()).into(receiveHolder.mReceiveIv);
                    receiveHolder.mReceiveIv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mVideoClickListener != null) {
                                mVideoClickListener.mVideClick(mChatMessage);
                            }
                        }
                    });
//                holder.rl_receive_msg.setOnLongClickListener(null);
//                holder.rl_send_msg.setOnLongClickListener(null);
                } else if (msgType.equals("video")) {
                    receiveHolder.mReceiveMsg.setVisibility(View.GONE);
                    receiveHolder.mReceiveIv.setVisibility(View.VISIBLE);
                    receiveHolder.mReceiveVideoPlay.setVisibility(View.VISIBLE);
                    receiveHolder.mReceiveVoice.setVisibility(View.GONE);
                    receiveHolder.mReceiveQuote.setVisibility(View.GONE);

                    Glide.with(context).load(mChatMessage.getUrl()).into(receiveHolder.mReceiveIv);
                    receiveHolder.mReceiveIv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mVideoClickListener != null) {
                                mVideoClickListener.mVideClick(mChatMessage);
                            }
                        }
                    });
//                holder.rl_receive_msg.setOnLongClickListener(null);
//                holder.rl_send_msg.setOnLongClickListener(null);

                } else if (msgType.equals("voi")) {
                    receiveHolder.mReceiveMsg.setVisibility(View.GONE);
                    receiveHolder.mReceiveIv.setVisibility(View.GONE);
                    receiveHolder.mReceiveVideoPlay.setVisibility(View.GONE);
                    receiveHolder.mReceiveVoice.setVisibility(View.VISIBLE);
                    receiveHolder.mReceiveQuote.setVisibility(View.GONE);

                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    try {
                        mediaPlayer.setDataSource(mChatMessage.getUrl());
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int duration = mediaPlayer.getDuration();
                    int time = Math.max(duration / 1000, 1);
                    receiveHolder.mReceiveVoice.setText(time + "“");
                    receiveHolder.mReceiveVoice.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mediaPlayer.start();

                        }
                    });
//                holder.rl_receive_msg.setOnLongClickListener(null);
//                holder.rl_send_msg.setOnLongClickListener(null);

                }
            }

        } else if (holder instanceof SendMessageViewHolder) {
            SendMessageViewHolder sendHolder = (SendMessageViewHolder) holder;
            if (mChatMessage.getType().equals("message")) {
                sendHolder.mSendMsg.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (mMessageLongClickListener != null) {
                            mMessageLongClickListener.mLongClick(mChatMessage, sendHolder.mSendContent);
                        }
                        return false;
                    }
                });
                sendHolder.mSendIv.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (mMessageLongClickListener != null) {
                            mMessageLongClickListener.mLongClick(mChatMessage, sendHolder.mSendContent);
                        }
                        return false;
                    }
                });
                String msgType = mChatMessage.getMsgType();

                sendHolder.mContent.setVisibility(View.VISIBLE);
                sendHolder.mNotice.setVisibility(View.GONE);
                sendHolder.mOrderNotice.setVisibility(View.GONE);


                if (msgType.equals("msg") || msgType.equals("voi") || msgType.equals("quote")) {
                    sendHolder.mSendContent.setBackground(context.getResources().getDrawable(R.drawable.jmui_msg_send_bg));
                } else {
                    sendHolder.mSendContent.setBackground(context.getResources().getDrawable(R.color.transparent));
                }

                if (msgType.equals("msg")) {
                    String content = mChatMessage.getTextMsg();
                    sendHolder.mSendMsg.setVisibility(View.VISIBLE);
                    sendHolder.mSendIv.setVisibility(View.GONE);
                    sendHolder.mSendVideoPlay.setVisibility(View.GONE);
                    sendHolder.mSendVoice.setVisibility(View.GONE);
                    sendHolder.mSendMsg.setText(SystemFacade.base64ToString(content));
                    sendHolder.mSendIv.setOnClickListener(null);
                    sendHolder.mSendQuote.setVisibility(View.GONE);


                } else if (msgType.equals("quote")) {
                    String content = mChatMessage.getTextMsg();
                    sendHolder.mSendMsg.setVisibility(View.VISIBLE);
                    sendHolder.mSendIv.setVisibility(View.GONE);
                    sendHolder.mSendVideoPlay.setVisibility(View.GONE);
                    sendHolder.mSendVoice.setVisibility(View.GONE);
                    sendHolder.mSendMsg.setText(content);
                    sendHolder.mSendIv.setOnClickListener(null);
                    sendHolder.mSendQuote.setVisibility(View.VISIBLE);
                    String quoteMessageInfo = mChatMessage.getQuoteMessageInfo();
                    ChatMessage chatMessage = new Gson().fromJson(quoteMessageInfo, ChatMessage.class);
                    if (chatMessage != null) {
                        String quoteContent = "";
                        if (chatMessage.getMsgType().equals("img")) {
                            quoteContent = "[图片]";
                            sendHolder.mSendQuote.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mVideoClickListener != null) {
                                        mVideoClickListener.mVideClick(chatMessage);
                                    }
                                }
                            });
                        } else if (chatMessage.getMsgType().equals("video")) {
                            quoteContent = "[视频]";
                            sendHolder.mSendQuote.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mVideoClickListener != null) {
                                        mVideoClickListener.mVideClick(chatMessage);
                                    }
                                }
                            });

                        } else {
                            quoteContent = SystemFacade.base64ToString(chatMessage.getTextMsg());
                        }
                        sendHolder.mSendQuote.setText(chatMessage.getMemberNickname() + ":" + quoteContent);
                    }

                } else if (msgType.equals("img")) {

                    sendHolder.mSendMsg.setVisibility(View.GONE);
                    sendHolder.mSendIv.setVisibility(View.VISIBLE);
                    sendHolder.mSendVideoPlay.setVisibility(View.GONE);
                    sendHolder.mSendVoice.setVisibility(View.GONE);
                    sendHolder.mSendQuote.setVisibility(View.GONE);

                    Glide.with(context).load(mChatMessage.getUrl()).into(sendHolder.mSendIv);
                    sendHolder.mSendIv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mVideoClickListener != null) {
                                mVideoClickListener.mVideClick(mChatMessage);
                            }
                        }
                    });
//                holder.rl_receive_msg.setOnLongClickListener(null);
//                holder.rl_send_msg.setOnLongClickListener(null);
                } else if (msgType.equals("video")) {
                    sendHolder.mSendMsg.setVisibility(View.GONE);
                    sendHolder.mSendIv.setVisibility(View.VISIBLE);
                    sendHolder.mSendVideoPlay.setVisibility(View.VISIBLE);
                    sendHolder.mSendVoice.setVisibility(View.GONE);
                    sendHolder.mSendQuote.setVisibility(View.GONE);

                    Glide.with(context).load(mChatMessage.getUrl()).into(sendHolder.mSendIv);
                    sendHolder.mSendIv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mVideoClickListener != null) {
                                mVideoClickListener.mVideClick(mChatMessage);
                            }
                        }
                    });
//                holder.rl_receive_msg.setOnLongClickListener(null);
//                holder.rl_send_msg.setOnLongClickListener(null);

                } else if (msgType.equals("voi")) {
                    sendHolder.mSendMsg.setVisibility(View.GONE);
                    sendHolder.mSendIv.setVisibility(View.GONE);
                    sendHolder.mSendVideoPlay.setVisibility(View.GONE);
                    sendHolder.mSendVoice.setVisibility(View.VISIBLE);
                    sendHolder.mSendQuote.setVisibility(View.GONE);

                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    try {
                        mediaPlayer.setDataSource(mChatMessage.getUrl());
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int duration = mediaPlayer.getDuration();
                    int time = Math.max(duration / 1000, 1);
                    sendHolder.mSendVoice.setText(time + "“");
                    sendHolder.mSendVoice.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mediaPlayer.start();

                        }
                    });
//                holder.rl_receive_msg.setOnLongClickListener(null);
//                holder.rl_send_msg.setOnLongClickListener(null);

                }
            } else if (mChatMessage.getType().equals("notice")) {

                String noticeInfo = mChatMessage.getNoticeInfo();
                NoticeData noticeData = new Gson().fromJson(noticeInfo, NoticeData.class);
                int noticeDataType = noticeData.getType();
                if (noticeDataType == 601) {
                    NoticeOrderData tradeInfo = noticeData.getTradeInfo();
                    sendHolder.mContent.setVisibility(View.GONE);
                    sendHolder.mNotice.setVisibility(View.GONE);
                    sendHolder.mOrderNotice.setVisibility(View.VISIBLE);
                    sendHolder.mOrderNotice.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOrderNoticeClickListener != null) {
                                mOrderNoticeClickListener.mOrderClick(tradeInfo);
                            }
                        }
                    });

                    sendHolder.mOrderNo.setText(tradeInfo.getOrderNo());
                    NoticeOrderData.CreatedTimeDTO createdTime = tradeInfo.getCreatedTime();
                    NoticeOrderData.ReleaseTimeDTO.DateDTO date = createdTime.getDate();
                    NoticeOrderData.ReleaseTimeDTO.TimeDTO time = createdTime.getTime();
                    sendHolder.mOrderTime.setText(date.getYear() + " -" + date.getMonth() + " -" + date.getDay() + " " + time.getHour() + ":" + time.getMinute());
                    sendHolder.mOrderMoney.setText(String.valueOf(tradeInfo.getMoney() / 100));
                    sendHolder.mOrderPerson.setText(tradeInfo.getDispatchMemberId());
                    sendHolder.mOrderCoin.setText(tradeInfo.getReleaseCoinName());

                } else {
                    String noticeContent = "";
                    switch (noticeDataType) {
                        case 101:
                        case 103:
                            noticeContent = noticeData.getFromMemberNickName() + "禁言了成员";
                            break;
                        case 102:
                        case 104:
                            noticeContent = noticeData.getFromMemberNickName() + "解除了禁言";
                            break;

                        case 110:
                            noticeContent = noticeData.getFromMemberNickName() + "设置了管理员";
                            break;
                        case 111:
                            noticeContent = noticeData.getFromMemberNickName() + "转让了群主";
                            break;
                        case 112:
                            noticeContent = noticeData.getFromMemberNickName() + "修改了群公告为：" + noticeData.getRelationInfo();

                            break;
                        case 113:
                            noticeContent = noticeData.getFromMemberNickName() + "修改了群名称";

                            break;
                        case 114:
                            noticeContent = noticeData.getFromMemberNickName() + "修改了加群方式";

                            break;
                        case 115:
                            noticeContent = noticeData.getFromMemberNickName() + "设置了群内加好友方式";

                            break;
                        case 116:
                            noticeContent = noticeData.getFromMemberNickName() + "退出了群聊";

                            break;
                        case 117:
                            noticeContent = noticeData.getFromMemberNickName() + "更换了群头像";

                            break;
                        case 118:
                            noticeContent = noticeData.getFromMemberNickName() + "新建群聊";
                            break;
                        case 119:
                            noticeContent = noticeData.getFromMemberNickName() + "加入了群聊";

                            break;
                        case 400:
                            noticeContent = "该群组已解散！";

                            break;
                        case 120:
                            noticeContent = "对方以将你删除！";

                            break;
                        case 121:
                            noticeContent = "你们已经成为好友啦！";

                            break;
                        case 122:
                            noticeContent = "您有新的好友申请！";

                            break;

                    }
                    sendHolder.mOrderNotice.setVisibility(View.GONE);
                    sendHolder.mContent.setVisibility(View.GONE);
                    sendHolder.mNotice.setVisibility(View.VISIBLE);
                    sendHolder.mNotice.setText(noticeContent);
                }

            }
        }

    }

    private String getDate(String milliSeconds) {
        // Create a DateFormatter object for displaying date in specified
        // format.
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Create a calendar object that will convert the date and time value in
        // milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(milliSeconds));
        return formatter.format(calendar.getTime());
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = mList.get(position);
        if (chatMessage.getIsMeSend() == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ReceiveMessageViewHolder extends RecyclerView.ViewHolder {

        private ImageView mReceiveIcon, mReceiveIv, mReceiveVideoPlay;
        private TextView mReceiveNickName, mReceiveMsg, mReceiveVoice, mReceiveQuote;
        private RelativeLayout mReceiveContent;

        public ReceiveMessageViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            mReceiveIcon = itemView.findViewById(R.id.jmui_avatar_iv);
            mReceiveNickName = itemView.findViewById(R.id.tv_display_name);
            mReceiveContent = itemView.findViewById(R.id.rl_receive_msg);
            mReceiveIv = itemView.findViewById(R.id.iv_receive_iv);
            mReceiveVideoPlay = itemView.findViewById(R.id.iv_video_play);
            mReceiveMsg = itemView.findViewById(R.id.tv_content);
            mReceiveVoice = itemView.findViewById(R.id.tv_receive_voice);
            mReceiveQuote = itemView.findViewById(R.id.tv_receive_quote);
        }
    }

    public static class SendMessageViewHolder extends RecyclerView.ViewHolder {

        private ImageView mSendIcon, mSendIv, mSendVideoPlay;
        private TextView mSendNickName, mSendMsg, mSendVoice, mNotice, mSendQuote, mOrderNo, mOrderTime, mOrderMoney, mOrderPerson, mOrderCoin;
        private RelativeLayout mSendContent, mContent, mOrderNotice;

        public SendMessageViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            mContent = itemView.findViewById(R.id.ll_me);
            mSendContent = itemView.findViewById(R.id.rl_send_msg);
            mSendIv = itemView.findViewById(R.id.iv_receive_iv);
            mSendVideoPlay = itemView.findViewById(R.id.iv_video_mine_play);
            mSendMsg = itemView.findViewById(R.id.tv_content);
            mSendVoice = itemView.findViewById(R.id.tv_send_voice);
            mSendIcon = itemView.findViewById(R.id.jmui_avatar_iv);
            mNotice = itemView.findViewById(R.id.tv_notice_content);
            mSendQuote = itemView.findViewById(R.id.tv_send_quote);
            mOrderNotice = itemView.findViewById(R.id.rl_get_order_notice);
            mOrderNo = itemView.findViewById(R.id.tv_chat_order_no);
            mOrderTime = itemView.findViewById(R.id.tv_chat_order_time);
            mOrderMoney = itemView.findViewById(R.id.tv_chat_order_money);
            mOrderPerson = itemView.findViewById(R.id.tv_chat_order_person);
            mOrderCoin = itemView.findViewById(R.id.tv_chat_order_coin);

        }
    }

    private Adapter_ChatMessage.IVideoClickListener mVideoClickListener;

    public void setVideoClickListener(Adapter_ChatMessage.IVideoClickListener videoClickListener) {
        mVideoClickListener = videoClickListener;
    }

    public interface IVideoClickListener {
        void mVideClick(ChatMessage message);
    }

    private IMessageLongClickListener mMessageLongClickListener;

    public void setMessageLongClickListener(IMessageLongClickListener messageLongClickListener) {
        mMessageLongClickListener = messageLongClickListener;
    }

    public interface IMessageLongClickListener {
        void mLongClick(ChatMessage message, View view);
    }

    private IOrderNoticeClickListener mOrderNoticeClickListener;

    public void setOrderNoticeClickListener(IOrderNoticeClickListener orderNoticeClickListener) {
        mOrderNoticeClickListener = orderNoticeClickListener;
    }

    public interface IOrderNoticeClickListener {
        void mOrderClick(NoticeOrderData data);
    }
}
