package com.weiwu.ql.main.contact.chat.adapter;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.weiwu.ql.R;
import com.weiwu.ql.main.contact.chat.modle.ChatMessage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Adapter_ChatMessage extends BaseAdapter {
    List<ChatMessage> mChatMessageList;
    LayoutInflater inflater;
    Context context;

    public Adapter_ChatMessage(Context context, List<ChatMessage> list) {
        this.mChatMessageList = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (mChatMessageList.get(position).getIsMeSend() == 1)
            return 0;// 返回的数据位角标
        else
            return 1;
    }

    @Override
    public int getCount() {
        return mChatMessageList.size();
    }


    @Override
    public Object getItem(int i) {
        return mChatMessageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ChatMessage mChatMessage = mChatMessageList.get(i);
        String content = mChatMessage.getTextMsg();
        int isMeSend = mChatMessage.getIsMeSend();
//        int isRead = mChatMessage.getIsRead();////是否已读（0未读 1已读）
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            if (isMeSend == 0) {//对方发送
                view = inflater.inflate(R.layout.item_chat_receive_text, viewGroup, false);
                holder.rl_receive_msg = view.findViewById(R.id.rl_receive_msg);
                holder.tv_content = view.findViewById(R.id.tv_content);
                holder.tv_sendtime = view.findViewById(R.id.tv_sendtime);
                holder.tv_display_name = view.findViewById(R.id.tv_display_name);
                holder.mReceiveIv = view.findViewById(R.id.iv_receive_iv);
                holder.mPlayVideo = view.findViewById(R.id.iv_video_play);
                holder.mVoice = view.findViewById(R.id.tv_receive_voice);
            } else {
                view = inflater.inflate(R.layout.item_chat_send_text, viewGroup, false);
                holder.rl_send_msg = view.findViewById(R.id.rl_send_msg);
                holder.tv_content = view.findViewById(R.id.tv_content);
                holder.tv_sendtime = view.findViewById(R.id.tv_sendtime);
                holder.mReceiveIv = view.findViewById(R.id.iv_receive_iv);
                holder.mPlayVideo = view.findViewById(R.id.iv_video_mine_play);
                holder.mVoice = view.findViewById(R.id.tv_send_voice);

//                holder.tv_isRead = view.findViewById(R.id.tv_isRead);
            }

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


//        holder.tv_isRead.setVisibility(View.GONE);

        String msgType = mChatMessage.getMsgType();

        holder.tv_sendtime.setVisibility(View.GONE);
        if (isMeSend == 0) {
            if (msgType.equals("msg") || msgType.equals("voi")) {
                holder.rl_receive_msg.setBackground(context.getResources().getDrawable(R.drawable.jmui_msg_receive_bg));
            } else {
                holder.rl_receive_msg.setBackground(context.getResources().getDrawable(R.color.transparent));
            }
        } else {
            if (msgType.equals("msg") || msgType.equals("voi")) {
                holder.rl_send_msg.setBackground(context.getResources().getDrawable(R.drawable.jmui_msg_send_bg));
            } else {
                holder.rl_send_msg.setBackground(context.getResources().getDrawable(R.color.transparent));
            }
        }
        if (msgType.equals("msg")) {
            holder.tv_content.setVisibility(View.VISIBLE);
            holder.mReceiveIv.setVisibility(View.GONE);
            holder.mPlayVideo.setVisibility(View.GONE);
            holder.mVoice.setVisibility(View.GONE);
            holder.tv_content.setText(content);
            holder.mReceiveIv.setOnClickListener(null);
        } else if (msgType.equals("img")) {

            holder.tv_content.setVisibility(View.GONE);
            holder.mReceiveIv.setVisibility(View.VISIBLE);
            holder.mPlayVideo.setVisibility(View.GONE);
            holder.mVoice.setVisibility(View.GONE);
            Glide.with(viewGroup.getContext()).load(mChatMessage.getUrl()).into(holder.mReceiveIv);
            holder.mReceiveIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mVideoClickListener != null) {
                        mVideoClickListener.mVideClick(mChatMessage);
                    }
                }
            });
        } else if (msgType.equals("video")) {
            holder.tv_content.setVisibility(View.GONE);
            holder.mReceiveIv.setVisibility(View.VISIBLE);
            holder.mPlayVideo.setVisibility(View.VISIBLE);
            holder.mVoice.setVisibility(View.GONE);
            Glide.with(viewGroup.getContext()).load(mChatMessage.getUrl()).into(holder.mReceiveIv);
            holder.mReceiveIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mVideoClickListener != null) {
                        mVideoClickListener.mVideClick(mChatMessage);
                    }
                }
            });
        } else if (msgType.equals("voi")) {
            holder.tv_content.setVisibility(View.GONE);
            holder.mReceiveIv.setVisibility(View.GONE);
            holder.mPlayVideo.setVisibility(View.GONE);
            holder.mVoice.setVisibility(View.VISIBLE);
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
            holder.mVoice.setText(time + "“");
            holder.mVoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPlayer.start();

                }
            });
        }


        //如果是自己发送才显示未读已读
        if (isMeSend == 1) {
            /*if (isRead == 0) {
                holder.tv_isRead.setText("未读");
                holder.tv_isRead.setTextColor(context.getResources().getColor(R.color.jmui_jpush_blue));
            } else if (isRead == 1) {
                holder.tv_isRead.setText("已读");
                holder.tv_isRead.setTextColor(Color.GRAY);
            } else {
                holder.tv_isRead.setText("");
            }*/
        } else {
            holder.tv_display_name.setVisibility(View.VISIBLE);
            holder.tv_display_name.setText(mChatMessage.getMemberNickname());
        }

        return view;
    }

    private static final String TAG = "Adapter_ChatMessage";

    class ViewHolder {
        private RelativeLayout rl_send_msg, rl_receive_msg;
        private TextView tv_content, tv_sendtime, tv_display_name, mVoice;
        private ImageView mReceiveIv, mPlayVideo;
    }


    /**
     * 将毫秒数转为日期格式
     *
     * @param timeMillis
     * @return
     */
    private String formatTime(String timeMillis) {
        long timeMillisl = Long.parseLong(timeMillis);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timeMillisl);
        return simpleDateFormat.format(date);
    }

    private IVideoClickListener mVideoClickListener;

    public void setVideoClickListener(IVideoClickListener videoClickListener) {
        mVideoClickListener = videoClickListener;
    }

    public interface IVideoClickListener {
        void mVideClick(ChatMessage message);
    }
}
