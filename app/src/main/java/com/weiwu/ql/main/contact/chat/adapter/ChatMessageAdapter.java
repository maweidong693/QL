package com.weiwu.ql.main.contact.chat.adapter;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cunoraz.gifview.library.GifView;
import com.google.gson.Gson;
import com.tencent.common.Constant;
import com.weiwu.ql.AppConstant;
import com.weiwu.ql.R;
import com.weiwu.ql.main.contact.chat.modle.HistoryMessageData;
import com.weiwu.ql.main.contact.chat.modle.NoticeOrderData;
import com.weiwu.ql.main.mine.friends.utils.GlideCircleTransform;
import com.weiwu.ql.main.mine.setting.YPreferencesUtils;
import com.weiwu.ql.utils.DensityUtils;
import com.weiwu.ql.utils.SPUtils;
import com.weiwu.ql.utils.SystemFacade;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/16 15:43 
 */
public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "ChatMessageAdapter";

    private int type;

    private List<HistoryMessageData.DataDTO.ListDTO> mList = new ArrayList<>();

    public List<HistoryMessageData.DataDTO.ListDTO> getList() {
        return mList;
    }

    public void setList(List<HistoryMessageData.DataDTO.ListDTO> list, int type) {
        mList.addAll(0, list);
        this.type = type;
        notifyDataSetChanged();
    }


    public void clearList() {
        mList.clear();
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

    private IFriendClickListener mFriendClickListener;

    public void setFriendClickListener(IFriendClickListener friendClickListener) {
        mFriendClickListener = friendClickListener;
    }

    public interface IFriendClickListener {
        void click(HistoryMessageData.DataDTO.ListDTO mChatMessage);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        HistoryMessageData.DataDTO.ListDTO mChatMessage = mList.get(position);
//        int isMeSend = mChatMessage.getIsMeSend();
        Context context = holder.itemView.getContext();
        if (holder instanceof ReceiveMessageViewHolder) {
            ReceiveMessageViewHolder receiveHolder = (ReceiveMessageViewHolder) holder;
            receiveHolder.setIsRecyclable(false);
            if (mChatMessage.getLast_time().equals("0000-00-00 00:00:00")) {
                receiveHolder.mReceiveTime.setVisibility(View.GONE);
            } else {
                receiveHolder.mReceiveTime.setVisibility(View.VISIBLE);
                receiveHolder.mReceiveTime.setText(SystemFacade.getNewChatTime(mChatMessage.getLast_time()));
            }
            if (mChatMessage.getType() != 1) {
                int msgType = mChatMessage.getCate();
                receiveHolder.mReceiveIcon.setVisibility(View.VISIBLE);
                receiveHolder.mReceiveIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mFriendClickListener != null) {
                            mFriendClickListener.click(mChatMessage);
                        }
                    }
                });
                if (type == 1) {
                    receiveHolder.mReceiveNickName.setVisibility(View.GONE);
                } else {
                    receiveHolder.mReceiveNickName.setVisibility(View.VISIBLE);

                }
                if (!TextUtils.isEmpty(mChatMessage.getFace_url())) {
                    Glide.with(context)
                            .load(mChatMessage.getFace_url())
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .placeholder(R.drawable.icon)
                            .transform(new GlideCircleTransform())
                            .into(receiveHolder.mReceiveIcon);
//                    Glide.with(receiveHolder.itemView.getContext()).load(mChatMessage.getFace_url()).into(receiveHolder.mReceiveIcon);
                } else {
                    Glide.with(context)
                            .load(R.drawable.icon)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .placeholder(R.drawable.icon)
                            .transform(new GlideCircleTransform())
                            .into(receiveHolder.mReceiveIcon);
//                    Glide.with(receiveHolder.itemView.getContext()).load(R.drawable.ic_launcher).into(receiveHolder.mReceiveIcon);
                }
                if (msgType == 1 || msgType == 3 || msgType == 5) {
                    receiveHolder.mReceiveContent.setBackground(context.getResources().getDrawable(R.drawable.jmui_msg_receive_bg));
                } else {
                    receiveHolder.mReceiveContent.setBackground(context.getResources().getDrawable(R.color.transparent));
                }
                receiveHolder.mReceiveNickName.setText(mChatMessage.getNick_name());
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
                if (msgType == 1) {
                    String content = mChatMessage.getContent();
                    receiveHolder.mReceiveMsg.setVisibility(View.VISIBLE);
                    receiveHolder.mReceiveIv.setVisibility(View.GONE);
                    receiveHolder.mReceiveVideoPlay.setVisibility(View.GONE);
                    receiveHolder.mReceiveVoice.setVisibility(View.GONE);
                    receiveHolder.mReceiveMsg.setText(content);
                    int dimension = receiveHolder.itemView.getContext().getResources().getDimensionPixelSize(R.dimen.sp_stander);
                    float scale = (float) YPreferencesUtils.get(Constant.SP_FontScale, 0.0f);
                    if(scale!=0.0){
                        double v = scale * (int) DensityUtils.px2sp(receiveHolder.itemView.getContext(), dimension);
                        receiveHolder.mReceiveMsg.setTextSize((int) v);
                    }
                    receiveHolder.mReceiveQuote.setVisibility(View.GONE);
                    receiveHolder.mReceiveIv.setOnClickListener(null);

                } else if (msgType == 5) {
                    String content = mChatMessage.getContent();
                    receiveHolder.mReceiveMsg.setVisibility(View.VISIBLE);
                    receiveHolder.mReceiveIv.setVisibility(View.GONE);
                    receiveHolder.mReceiveVideoPlay.setVisibility(View.GONE);
                    receiveHolder.mReceiveVoice.setVisibility(View.GONE);
                    receiveHolder.mReceiveMsg.setText(content);
                    receiveHolder.mReceiveIv.setOnClickListener(null);
                    receiveHolder.mReceiveQuote.setVisibility(View.VISIBLE);
                    String quoteMessageInfo = mChatMessage.getCite();
                    HistoryMessageData.DataDTO.ListDTO chatMessage = new Gson().fromJson(StringEscapeUtils.unescapeJava(quoteMessageInfo), HistoryMessageData.DataDTO.ListDTO.class);
                    if (chatMessage != null) {
                        String quoteContent = "";
                        if (chatMessage.getCate() == 2) {
                            quoteContent = "[图片]";
                            receiveHolder.mReceiveQuote.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mVideoClickListener != null) {
                                        mVideoClickListener.mVideClick(chatMessage);
                                    }
                                }
                            });
                        } else if (chatMessage.getCate() == 4) {
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
                            quoteContent = chatMessage.getContent();
                        }
                        receiveHolder.mReceiveQuote.setText(chatMessage.getNick_name() + ":" + quoteContent);
                    }

                } else if (msgType == 2) {

                    receiveHolder.mReceiveMsg.setVisibility(View.GONE);
                    receiveHolder.mReceiveIv.setVisibility(View.VISIBLE);
                    receiveHolder.mReceiveVideoPlay.setVisibility(View.GONE);
                    receiveHolder.mReceiveVoice.setVisibility(View.GONE);
                    receiveHolder.mReceiveQuote.setVisibility(View.GONE);

                    Glide.with(context).load(mChatMessage.getContent()).into(receiveHolder.mReceiveIv);
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
                } else if (msgType == 4) {
                    receiveHolder.mReceiveMsg.setVisibility(View.GONE);
                    receiveHolder.mReceiveIv.setVisibility(View.VISIBLE);
                    receiveHolder.mReceiveVideoPlay.setVisibility(View.VISIBLE);
                    receiveHolder.mReceiveVoice.setVisibility(View.GONE);
                    receiveHolder.mReceiveQuote.setVisibility(View.GONE);

                    Glide.with(context).load(mChatMessage.getContent()).into(receiveHolder.mReceiveIv);
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

                } else if (msgType == 3) {
                    receiveHolder.mReceiveMsg.setVisibility(View.GONE);
                    receiveHolder.mReceiveIv.setVisibility(View.GONE);
                    receiveHolder.mReceiveVideoPlay.setVisibility(View.GONE);
                    receiveHolder.mReceiveVoice.setVisibility(View.VISIBLE);
                    receiveHolder.mReceiveQuote.setVisibility(View.GONE);

                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    try {
                        mediaPlayer.setDataSource(mChatMessage.getContent());
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
            } else if (mChatMessage.getType() == 1) {
                String noticeInfo = mChatMessage.getContent();
                receiveHolder.mReceiveContent.setVisibility(View.GONE);
                receiveHolder.mReceiveNickName.setVisibility(View.GONE);
                receiveHolder.mReceiveIcon.setVisibility(View.GONE);
                receiveHolder.mReceiveIcon.setOnClickListener(null);
                receiveHolder.mNotice.setVisibility(View.VISIBLE);
                receiveHolder.mNotice.setText(noticeInfo);
            }
        } else if (holder instanceof SendMessageViewHolder) {
            SendMessageViewHolder sendHolder = (SendMessageViewHolder) holder;
            if (mChatMessage.getLast_time().equals("0000-00-00 00:00:00")) {
                sendHolder.mSendTime.setVisibility(View.GONE);
            } else {
                sendHolder.mSendTime.setVisibility(View.VISIBLE);
                sendHolder.mSendTime.setText(SystemFacade.getNewChatTime(mChatMessage.getLast_time()));
            }
            if (mChatMessage.getType() != 1) {
                sendHolder.mSendIcon.setVisibility(View.VISIBLE);
                sendHolder.mSendIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mFriendClickListener != null) {
                            mFriendClickListener.click(mChatMessage);
                        }
                    }
                });
                if (!TextUtils.isEmpty(mChatMessage.getFace_url())) {
                    Glide.with(sendHolder.itemView.getContext()).load(mChatMessage.getFace_url()).into(sendHolder.mSendIcon);
                } else {
                    Glide.with(sendHolder.itemView.getContext()).load(R.drawable.ic_launcher).into(sendHolder.mSendIcon);
                }
                sendHolder.mSendMsg.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (mMessageLongClickListener != null) {
                            mMessageLongClickListener.mLongClick(mChatMessage, sendHolder.mSendContent);
                        }
                        return false;
                    }
                });
                int msgType = mChatMessage.getCate();
                sendHolder.mSendIv.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (mMessageLongClickListener != null) {

                            mMessageLongClickListener.mLongClick(mChatMessage, sendHolder.mSendContent);
                        }
                        return false;
                    }
                });

                sendHolder.mContent.setVisibility(View.VISIBLE);
                sendHolder.mNotice.setVisibility(View.GONE);


                if (msgType == 1 || msgType == 3 || msgType == 5) {
                    sendHolder.mSendContent.setBackground(context.getResources().getDrawable(R.drawable.jmui_msg_send_bg));
                } else {
                    sendHolder.mSendContent.setBackground(context.getResources().getDrawable(R.color.transparent));
                }

                if (msgType == 1) {
                    String content = mChatMessage.getContent();
                    sendHolder.mSendMsg.setVisibility(View.VISIBLE);
                    sendHolder.mSendIv.setVisibility(View.GONE);
                    sendHolder.mSendVideoPlay.setVisibility(View.GONE);
                    sendHolder.mSendVoice.setVisibility(View.GONE);
                    sendHolder.mSendMsg.setText(content);
                    int dimension = sendHolder.itemView.getContext().getResources().getDimensionPixelSize(R.dimen.sp_stander);
                    float scale = (float) YPreferencesUtils.get(Constant.SP_FontScale, 0.0f);
                    if(scale!=0.0) {
                        double v = scale * (int) DensityUtils.px2sp(sendHolder.itemView.getContext(), dimension);
                        sendHolder.mSendMsg.setTextSize((int) v);
                    }
                    sendHolder.mSendIv.setOnClickListener(null);
                    sendHolder.mSendQuote.setVisibility(View.GONE);


                } else if (msgType == 5) {
                    String content = mChatMessage.getContent();
                    sendHolder.mSendMsg.setVisibility(View.VISIBLE);
                    sendHolder.mSendIv.setVisibility(View.GONE);
                    sendHolder.mSendVideoPlay.setVisibility(View.GONE);
                    sendHolder.mSendVoice.setVisibility(View.GONE);
                    sendHolder.mSendMsg.setText(content);
                    sendHolder.mSendIv.setOnClickListener(null);
                    sendHolder.mSendQuote.setVisibility(View.VISIBLE);
                    String quoteMessageInfo = mChatMessage.getCite();

                    String s = StringEscapeUtils.unescapeJava(quoteMessageInfo);
                    HistoryMessageData.DataDTO.ListDTO chatMessage = new Gson().fromJson(s, HistoryMessageData.DataDTO.ListDTO.class);
                    if (chatMessage != null) {
                        String quoteContent = "";
                        if (chatMessage.getCate() == 2) {
                            quoteContent = "[图片]";
                            sendHolder.mSendQuote.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mVideoClickListener != null) {
                                        mVideoClickListener.mVideClick(chatMessage);
                                    }
                                }
                            });
                        } else if (chatMessage.getCate() == 4) {
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
                            quoteContent = chatMessage.getContent();
                        }
                        sendHolder.mSendQuote.setText(chatMessage.getNick_name() + ":" + quoteContent);
                    }

                } else if (msgType == 2) {

                    sendHolder.mSendMsg.setVisibility(View.GONE);
                    sendHolder.mSendIv.setVisibility(View.VISIBLE);
                    sendHolder.mSendVideoPlay.setVisibility(View.GONE);
                    sendHolder.mSendVoice.setVisibility(View.GONE);
                    sendHolder.mSendQuote.setVisibility(View.GONE);

                    Glide.with(context).load(mChatMessage.getContent()).into(sendHolder.mSendIv);
                    sendHolder.mSendIv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mVideoClickListener != null) {
                                mVideoClickListener.mVideClick(mChatMessage);
                            }
                        }
                    });
                    if (mChatMessage.getSending() == 0) {
                        sendHolder.mSending.setVisibility(View.GONE);
                    } else {
                        sendHolder.mSending.setVisibility(View.VISIBLE);
                    }
//                holder.rl_receive_msg.setOnLongClickListener(null);
//                holder.rl_send_msg.setOnLongClickListener(null);
                } else if (msgType == 4) {
                    sendHolder.mSendMsg.setVisibility(View.GONE);
                    sendHolder.mSendIv.setVisibility(View.VISIBLE);
                    sendHolder.mSendVideoPlay.setVisibility(View.VISIBLE);
                    sendHolder.mSendVoice.setVisibility(View.GONE);
                    sendHolder.mSendQuote.setVisibility(View.GONE);

                    Glide.with(context).load(mChatMessage.getContent()).into(sendHolder.mSendIv);
                    sendHolder.mSendIv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mVideoClickListener != null) {
                                mVideoClickListener.mVideClick(mChatMessage);
                            }
                        }
                    });

                    if (mChatMessage.getSending() == 0) {
                        sendHolder.mSending.setVisibility(View.GONE);
                    } else {
                        sendHolder.mSending.setVisibility(View.VISIBLE);
                    }
//                holder.rl_receive_msg.setOnLongClickListener(null);
//                holder.rl_send_msg.setOnLongClickListener(null);

                } else if (msgType == 3) {
                    sendHolder.mSendMsg.setVisibility(View.GONE);
                    sendHolder.mSendIv.setVisibility(View.GONE);
                    sendHolder.mSendVideoPlay.setVisibility(View.GONE);
                    sendHolder.mSendVoice.setVisibility(View.VISIBLE);
                    sendHolder.mSendQuote.setVisibility(View.GONE);

                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    try {
                        mediaPlayer.setDataSource(mChatMessage.getContent());
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

                    /*if (mChatMessage.getSending() == 0) {
                        sendHolder.mSending.setVisibility(View.GONE);
                    } else {
                        sendHolder.mSending.setVisibility(View.VISIBLE);
                    }*/
//                holder.rl_receive_msg.setOnLongClickListener(null);
//                holder.rl_send_msg.setOnLongClickListener(null);

                }
            } else if (mChatMessage.getType() == 1) {

                String noticeInfo = mChatMessage.getContent();
                sendHolder.mContent.setVisibility(View.GONE);
                sendHolder.mSendIcon.setVisibility(View.GONE);
                sendHolder.mSendIcon.setOnClickListener(null);
                sendHolder.mNotice.setVisibility(View.VISIBLE);
                sendHolder.mNotice.setText(noticeInfo);
                sendHolder.mSending.setVisibility(View.GONE);

            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        HistoryMessageData.DataDTO.ListDTO chatMessage = mList.get(position);
        if (chatMessage.getFid().equals(SPUtils.getValue(AppConstant.USER, AppConstant.USER_ID))) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ReceiveMessageViewHolder extends RecyclerView.ViewHolder {

        private ImageView mReceiveIcon, mReceiveIv, mReceiveVideoPlay;
        private TextView mReceiveNickName, mReceiveMsg, mReceiveVoice, mReceiveQuote, mNotice, mReceiveTime;
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
            mNotice = itemView.findViewById(R.id.tv_notice_content);
            mReceiveTime = itemView.findViewById(R.id.tv_receive_time);

        }
    }

    public static class SendMessageViewHolder extends RecyclerView.ViewHolder {

        private ImageView mSendIcon, mSendIv, mSendVideoPlay;
        private TextView mSendNickName, mSendMsg, mSendVoice, mNotice, mSendQuote, mSendTime;
        private RelativeLayout mSendContent, mContent;
        private GifView mSending;

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
            mSendTime = itemView.findViewById(R.id.tv_send_time);
            mSending = itemView.findViewById(R.id.jmui_sending_iv);

        }
    }

    private IVideoClickListener mVideoClickListener;

    public void setVideoClickListener(IVideoClickListener videoClickListener) {
        mVideoClickListener = videoClickListener;
    }

    public interface IVideoClickListener {
        void mVideClick(HistoryMessageData.DataDTO.ListDTO message);
    }

    private IMessageLongClickListener mMessageLongClickListener;

    public void setMessageLongClickListener(IMessageLongClickListener messageLongClickListener) {
        mMessageLongClickListener = messageLongClickListener;
    }

    public interface IMessageLongClickListener {
        void mLongClick(HistoryMessageData.DataDTO.ListDTO message, View view);
    }

    private IOrderNoticeClickListener mOrderNoticeClickListener;

    public void setOrderNoticeClickListener(IOrderNoticeClickListener orderNoticeClickListener) {
        mOrderNoticeClickListener = orderNoticeClickListener;
    }

    public interface IOrderNoticeClickListener {
        void mOrderClick(NoticeOrderData data);
    }
}
