package com.weiwu.ql.main.login.country;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weiwu.ql.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class ChoiceCountryAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CountryEntity> listDatas;

    public ChoiceCountryAdapter(Context context, ArrayList<CountryEntity> listDatas) {
        this.context = context;
        this.listDatas = listDatas;
    }

    @Override
    public int getCount() {
        return listDatas == null ? 0 : listDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return listDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_text_point, parent, false);
            holder = new MyViewHolder(convertView);
            //设置标记
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        final CountryEntity bean = listDatas.get(position);


        if (bean.isTitle()) {
            holder.rl_index_container.setVisibility(View.VISIBLE);
            holder.rl_content_container.setVisibility(View.GONE);

            holder.tv_index_title.setText(bean.getTitle());
        } else {
            holder.rl_index_container.setVisibility(View.GONE);
            holder.rl_content_container.setVisibility(View.VISIBLE);

            holder.tv_title.setText(bean.getCountry());
            holder.tv_num.setText(bean.getCode());
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!bean.isTitle()) {
                    EventBus.getDefault().post(bean);
                    ((Activity) context).finish();
                }
            }
        });
        return convertView;
    }

    /**
     * 内容
     */
    public static class MyViewHolder {
        public View rootView;
        public RelativeLayout rl_index_container;
        public RelativeLayout rl_content_container;
        public TextView tv_index_title;
        public TextView tv_title;
        public TextView tv_num;
        public View view_line;

        public MyViewHolder(View rootView) {
            this.rootView = rootView;
            rl_index_container = (RelativeLayout) rootView.findViewById(R.id.rl_index_container);
            rl_content_container = (RelativeLayout) rootView.findViewById(R.id.rl_content_container);
            tv_index_title = (TextView) rootView.findViewById(R.id.tv_index_title);
            tv_title = (TextView) rootView.findViewById(R.id.tv_title);
            tv_num = (TextView) rootView.findViewById(R.id.tv_num);
            view_line = (View) rootView.findViewById(R.id.view_line);
        }
    }
}
