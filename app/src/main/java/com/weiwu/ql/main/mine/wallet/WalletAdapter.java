package com.weiwu.ql.main.mine.wallet;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.weiwu.ql.R;
import com.weiwu.ql.data.bean.WalletData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/9/1 11:12 
 */
public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.WalletViewHolder> {

    private List<WalletData.DataDTO> mList = new ArrayList<>();

    public void setList(List<WalletData.DataDTO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public WalletViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new WalletViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wallet, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull WalletViewHolder holder, int position) {
        WalletData.DataDTO dataDTO = mList.get(position);
        holder.setData(dataDTO);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWalletClickListener != null) {
                    mWalletClickListener.onClick(dataDTO);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class WalletViewHolder extends RecyclerView.ViewHolder {

        private ImageView mBg;
        private TextView mTitle, mAddress, mRemark;

        public WalletViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            mBg = itemView.findViewById(R.id.iv_wallet_bg);
            mTitle = itemView.findViewById(R.id.tv_wallet_coin_name);
            mAddress = itemView.findViewById(R.id.tv_wallet_coin_address);
            mRemark = itemView.findViewById(R.id.tv_wallet_remark);
        }

        public void setData(WalletData.DataDTO data) {
            Random random = new Random();
            int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
            mBg.setBackgroundColor(color);
            mTitle.setText(data.getCoinName());
            mAddress.setText(data.getCoinAddress());

            if (data.getRemark() != null) {
                mRemark.setVisibility(View.VISIBLE);
                mRemark.setText(data.getRemark());
            } else {
                mRemark.setVisibility(View.GONE);
            }
        }
    }

    private IWalletClickListener mWalletClickListener;

    public void setWalletClickListener(IWalletClickListener walletClickListener) {
        mWalletClickListener = walletClickListener;
    }

    public interface IWalletClickListener {
        void onClick(WalletData.DataDTO data);
    }
}
