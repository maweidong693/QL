package com.weiwu.ql.main.find;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.weiwu.ql.R;
import com.weiwu.ql.data.bean.HandlerData;
import com.weiwu.ql.data.request.DistributeRequestBody;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/31 09:52 
 */
public class DistributeAdapter extends RecyclerView.Adapter<DistributeAdapter.DistributeViewHolder> {

    private List<HandlerData.DataDTO> mList = new ArrayList<>();

    public List<HandlerData.DataDTO> getList() {
        return mList;
    }

    public void setList(List<HandlerData.DataDTO> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public DistributeViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new DistributeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_distribute, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DistributeViewHolder holder, int position) {
        HandlerData.DataDTO dto = mList.get(position);
        holder.setData(position, dto);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class DistributeViewHolder extends RecyclerView.ViewHolder {

        private ImageView mAvator;
        private TextView mName;
        private EditText mMoney;

//        private TxtWatcher mTxtWatcher;

        public DistributeViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            mAvator = itemView.findViewById(R.id.iv_distribute_avator);
            mName = itemView.findViewById(R.id.tv_distribute_name);
            mMoney = itemView.findViewById(R.id.et_distribute_money);

//            mTxtWatcher = new TxtWatcher();
        }

        public void setData(int position, HandlerData.DataDTO data) {
            if (TextUtils.isEmpty(data.getMomentImg())) {
                Glide.with(itemView.getContext()).load(R.drawable.icon).into(mAvator);
            } else {
                Glide.with(itemView.getContext()).load(data.getMomentImg()).into(mAvator);
            }
            mName.setText(data.getNickName());

//            mTxtWatcher.buildWatcher(position);

            mMoney.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String text = mMoney.getText().toString();
                    if (text.length() > 0) {
                        if (mListener != null) {
                            mList.get(position).setMoney(Integer.parseInt(text));
                            mListener.onEditTextChanged(position, text);
//                    mTvTotalPrice.setText(String.valueOf(mList.get(mPosition).getMoney()));
                        }
                    } else {
                        if (mListener != null) {
                            mList.get(position).setMoney(0);
                            mListener.onEditTextChanged(position, "0");
//                    mTvTotalPrice.setText("");
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            /*mMoney.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
//                        mEditTextUnFocusListener.onEditTextUnFocus(true, TextUtils.isEmpty(mMoney.getText().toString()) ? "0" : mMoney.getText().toString());
                        mMoney.addTextChangedListener(mTxtWatcher);
                    } else {
//                        mEditTextUnFocusListener.onEditTextUnFocus(false, TextUtils.isEmpty(mMoney.getText().toString()) ? "0" : mMoney.getText().toString());
                        mMoney.removeTextChangedListener(mTxtWatcher);
                    }
                }
            });*/
        }
    }

    /*public interface IEditTextUnFocusListener {
        void onEditTextUnFocus(boolean type, String value);
    }

    private IEditTextUnFocusListener mEditTextUnFocusListener;

    public void setEditTextUnFocusListener(IEditTextUnFocusListener editTextUnFocusListener) {
        mEditTextUnFocusListener = editTextUnFocusListener;
    }*/

    public interface EditAbleListAdapterListener {
        public void onEditTextChanged(int position, String value);
    }

    private EditAbleListAdapterListener mListener;

    public void setListener(EditAbleListAdapterListener listener) {
        mListener = listener;
    }

    public class TxtWatcher implements TextWatcher {

        private int mPosition;
//        private TextView mTvTotalPrice;

        public void buildWatcher(int position) {
            this.mPosition = position;
//            this.mTvTotalPrice = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                if (mListener != null) {
                    mListener.onEditTextChanged(mPosition, s.toString());
                    mList.get(mPosition).setMoney(Integer.parseInt(s.toString()));
//                    mTvTotalPrice.setText(String.valueOf(mList.get(mPosition).getMoney()));
                }
            } else {
                if (mListener != null) {
                    mListener.onEditTextChanged(mPosition, "0");
                    mList.get(mPosition).setMoney(0);
//                    mTvTotalPrice.setText("");
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
