package com.weiwu.ql.main.find;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.weiwu.ql.R;
import com.weiwu.ql.data.bean.OrderData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/28 17:01 
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<OrderData.DataDTO.ListDTO> mList = new ArrayList<>();

    public void setList(List<OrderData.DataDTO.ListDTO> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearList() {
        mList.clear();
    }

    @NonNull
    @NotNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new OrderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OrderViewHolder holder, int position) {
        OrderData.DataDTO.ListDTO dto = mList.get(position);
        holder.setData(dto);
        int status = dto.getStatus();
        if (status == 100 || status == 101 || status == 200 || status == 202 || status == 203) {
            holder.mStatus.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.bg_order_status));
            holder.mStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOrderClickListener != null) {
                        mOrderClickListener.onClick(dto);
                    }
                }
            });
        }
        if (status == 102 || status == 103 || status == 104 || status == 201 || status == 204 || status == 205) {
            holder.mStatus.setBackground(holder.itemView.getContext().getResources().getDrawable(R.drawable.bg_quote));
            holder.mStatus.setOnClickListener(null);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOrderDetailClickListener != null) {
                    mOrderDetailClickListener.onClick(dto);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        private TextView mNumber, mPushCoin, mComeCoin, mMoney, mStatus;

        public OrderViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            mNumber = itemView.findViewById(R.id.tv_order_number);
            mPushCoin = itemView.findViewById(R.id.tv_order_push_coin);
            mComeCoin = itemView.findViewById(R.id.tv_order_come_coin);
            mMoney = itemView.findViewById(R.id.tv_order_money);
            mStatus = itemView.findViewById(R.id.tv_order_status);
        }

        public void setData(OrderData.DataDTO.ListDTO data) {
            mNumber.setText(data.getOrderNo());
            mPushCoin.setText(data.getReleaseCoinName());
            mComeCoin.setText(data.getHandlerReturnCoinName());
            mMoney.setText("¥" + data.getMoney() / 100);
            int status = data.getStatus();
            /*
            * 总单状态：
                100待接单(派单人接单)                       app----接单（按钮）
                101已接单(此时分单)                         app----待派单（按钮）
                102已派单                                   app----已派单（置灰）
                103待发币                                   app----待发币（置灰）
                104已完成(所有子单完成时，总单完成)         app----已完成（置灰）


              子单状态
                200待接单(处理人接单)                       app----接单（按钮）
                201已接单(待发单人发币)       app----待发币（置灰）
                202已发币待确认(待处理人确认)               app----确认收币（按钮）
                203处理人确认收币，待回币(待处理人回币)     app----确认回币（按钮）
                204已回币待确认(待发单人确认)               app----待确认（置灰）
                205确认收到回币(发单人确认收币)             app----已完成
            * */
            String s = "";
            switch (status) {
                case 100:
                    s = "接单";
                    break;
                case 101:
                    s = "待派单";
                    break;
                case 102:
                    s = "已派单";

                    break;
                case 103:
                    s = "待发币";

                    break;
                case 104:
                    s = "已完成";

                    break;
                case 200:
                    s = "接单";

                    break;
                case 201:
                    s = "待发币";

                    break;
                case 202:
                    s = "确认收币";

                    break;
                case 203:
                    s = "确认回币";

                    break;
                case 204:
                    s = "待确认";

                    break;
                case 205:
                    s = "已完成";

                    break;

            }
            mStatus.setText(s);
        }
    }

    private IOrderClickListener mOrderClickListener;

    public void setOrderClickListener(IOrderClickListener orderClickListener) {
        mOrderClickListener = orderClickListener;
    }

    public interface IOrderClickListener {
        void onClick(OrderData.DataDTO.ListDTO data);
    }

    private IOrderDetailClickListener mOrderDetailClickListener;

    public void setOrderDetailClickListener(IOrderDetailClickListener orderDetailClickListener) {
        mOrderDetailClickListener = orderDetailClickListener;
    }

    public interface IOrderDetailClickListener {
        void onClick(OrderData.DataDTO.ListDTO data);
    }
}
