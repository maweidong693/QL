package com.weiwu.ql.main.message;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.weiwu.ql.R;
import com.weiwu.ql.data.bean.MessageListData;
import com.weiwu.ql.utils.SystemFacade;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/7/22 14:35 
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<MessageListData> mList = new ArrayList<>();

    public void setList(List<MessageListData> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member_message, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MessageViewHolder holder, int position) {
        MessageListData data = mList.get(position);
        holder.setData(data);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChatClickListener != null) {
                    mChatClickListener.mClick(data);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        private ImageView mAvator;
        private TextView mLastContent, mNickName, mTime;

        public MessageViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            mAvator = itemView.findViewById(R.id.iv_message_head);
            mLastContent = itemView.findViewById(R.id.tv_message_info);
            mNickName = itemView.findViewById(R.id.tv_message_nick_name);
            mTime = itemView.findViewById(R.id.tv_message_time);
        }

        public void setData(MessageListData data) {
            if (TextUtils.isEmpty(data.getAvator())) {
                Glide.with(itemView.getContext()).load(itemView.getContext().getResources().getDrawable(R.drawable.icon)).into(mAvator);
            } else {
                Glide.with(itemView.getContext()).load(data.getAvator()).into(mAvator);
            }
            mLastContent.setText(SystemFacade.base64ToString(data.getLastContent()));
            mNickName.setText(data.getNickName());
            if (!TextUtils.isEmpty(data.getTime())) {
                mTime.setText(getDate(data.getTime()));
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
    }


    private IChatClickListener mChatClickListener;

    public void setChatClickListener(IChatClickListener chatClickListener) {
        mChatClickListener = chatClickListener;
    }

    public interface IChatClickListener {
        void mClick(MessageListData data);
    }
}
