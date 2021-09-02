package com.weiwu.ql.main.mine.friends.adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.weiwu.ql.main.mine.friends.data.CircleHeadUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiwei on 16/4/9.
 */
public abstract class BaseRecycleViewAdapter<T,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected RecycleViewItemListener itemListener;
    protected List<T> datas = new ArrayList<T>();
    protected CircleHeadUser headUser  = new CircleHeadUser();

    public List<T> getDatas() {
        if (datas==null)
            datas = new ArrayList<T>();
        return datas;
    }

    public void setDatas(List<T> datas, CircleHeadUser headUser) {
        this.datas = datas;
        this.headUser = headUser;
    }

    public void setItemListener(RecycleViewItemListener listener){
        this.itemListener = listener;
    }

}
