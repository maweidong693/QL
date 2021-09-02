package com.weiwu.ql.main.find;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.weiwu.ql.R;
import com.weiwu.ql.data.bean.HandlerData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/30 14:25 
 */
public class AllHandlerAdapter extends RecyclerView.Adapter<AllHandlerAdapter.AllHandlerViewHolder> {

    private List<HandlerData.DataDTO> mList = new ArrayList<>();

    public void setList(List<HandlerData.DataDTO> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public AllHandlerViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new AllHandlerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_handler, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AllHandlerViewHolder holder, int position) {
        HandlerData.DataDTO dataDTO = mList.get(position);
        holder.setData(dataDTO);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataDTO.setCheck(isChecked);
                if (mHandlerCheckListener != null) {
                    mHandlerCheckListener.onCheck(dataDTO, isChecked);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class AllHandlerViewHolder extends RecyclerView.ViewHolder {

        private CheckBox mCheckBox;
        private ImageView mAvator;
        private TextView mName;

        public AllHandlerViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.cb_handler);
            mAvator = itemView.findViewById(R.id.iv_handler_avator);
            mName = itemView.findViewById(R.id.tv_handler_name);
        }

        public void setData(HandlerData.DataDTO data) {
            mCheckBox.setChecked(data.isCheck());
            if (TextUtils.isEmpty(data.getMomentImg())) {
                Glide.with(itemView.getContext()).load(R.drawable.icon).into(mAvator);
            } else {
                Glide.with(itemView.getContext()).load(data.getMomentImg()).into(mAvator);
            }
            mName.setText(data.getNickName());
        }
    }

    private IHandlerCheckListener mHandlerCheckListener;

    public void setHandlerCheckListener(IHandlerCheckListener handlerCheckListener) {
        mHandlerCheckListener = handlerCheckListener;
    }

    public interface IHandlerCheckListener {
        void onCheck(HandlerData.DataDTO data, boolean isCheck);
    }
}
