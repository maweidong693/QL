package com.weiwu.ql.main.find;

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
import com.weiwu.ql.data.bean.OrderDetailData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/9/1 10:08 
 */
public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {

    private List<OrderDetailData.DataDTO.ChildTradeVOListDTO.StatusInfoListDTO> mList = new ArrayList<>();

    public void setList(List<OrderDetailData.DataDTO.ChildTradeVOListDTO.StatusInfoListDTO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    private boolean isExp = false;

    public boolean isExp() {
        return isExp;
    }

    public void setExp(boolean exp) {
        isExp = exp;
    }

    @NonNull
    @NotNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new OrderDetailViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OrderDetailViewHolder holder, int position) {
        OrderDetailData.DataDTO.ChildTradeVOListDTO.StatusInfoListDTO statusInfoListDTO = mList.get(position);
        holder.setData(statusInfoListDTO);
        if (position == 0) {
            holder.mV2.setVisibility(View.GONE);
            holder.mV1.setVisibility(View.VISIBLE);
        } else if (position == mList.size() - 1) {
            holder.mV1.setVisibility(View.GONE);
            holder.mV2.setVisibility(View.VISIBLE);
        } else {
            holder.mV1.setVisibility(View.VISIBLE);
            holder.mV2.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        if (mList.size() < 3) {
            return mList.size();
        } else {
            if (isExp) {
                return mList.size();
            } else {
                return 3;
            }
        }
    }

    public class OrderDetailViewHolder extends RecyclerView.ViewHolder {

        private View mV1, mV2;
        private ImageView mRecord;
        private TextView mTitle, mTime;

        public OrderDetailViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            mV1 = itemView.findViewById(R.id.v_order_b);
            mV2 = itemView.findViewById(R.id.v_order_t);
            mRecord = itemView.findViewById(R.id.iv_order_record);
            mTime = itemView.findViewById(R.id.tv_order_detail_time);
            mTitle = itemView.findViewById(R.id.tv_order_detail_title);
        }

        public void setData(OrderDetailData.DataDTO.ChildTradeVOListDTO.StatusInfoListDTO data) {
            mTitle.setText(data.getAfterStatusInfo());
            mTime.setText(data.getChangeTime());
            if (TextUtils.isEmpty(data.getChangeTime())) {
                Glide.with(itemView.getContext()).load(R.drawable.ic_order_detail_no).into(mRecord);
            } else {
                Glide.with(itemView.getContext()).load(R.mipmap.ic_order_detail_record).into(mRecord);

            }
        }
    }
}
