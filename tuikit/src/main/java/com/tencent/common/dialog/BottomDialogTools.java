package com.tencent.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.qcloud.tim.uikit.R;

import java.lang.reflect.Method;

import androidx.core.content.ContextCompat;

/**
 * 底部弹出菜单Utils
 * Created by hui.feng on 2018/1/19.
 */

public class BottomDialogTools {

    private static final int DES_ITEM_HEIGHT = 67;
    private static final int ITEM_HEIGHT = 55;
    private static Dialog mCameraDialog;

    private static long lastClickTime = 0;

    // 判断是不是快速点击，返回true表示不是
    public static boolean isNotFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime >= 400) {
            lastClickTime = time;
            return true;
        } else {
            return false;
        }
    }


    /**
     * 底部弹出菜单
     *
     * @param context     context
     * @param isTouchHide 点击外面是否消失
     * @param listener    item监听
     * @param beans       模型类
     */
    public static Dialog showBottomDialogText(Context context, boolean isTouchHide, BottomDialogAdapter.OnPopupClickListener listener, BottomDialogBean... beans) {
        if (!isNotFastClick()) {
            return mCameraDialog;
        }
        mCameraDialog = new Dialog(context, R.style.BottomDialog);
        View root = LayoutInflater.from(context).inflate(
                R.layout.view_bottom_dialog_text, null);
        mCameraDialog.setContentView(root);
        ListView mLvContent = root.findViewById(R.id.lv_contentView);
        int height = 0;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLvContent.getLayoutParams();
        for (BottomDialogBean bean : beans) {
            height = TextUtils.isEmpty(bean.getDialogDescribe()) ? (ITEM_HEIGHT + height) : (DES_ITEM_HEIGHT + height);
        }
        params.height = dip2px(context, height + 2);
        BottomDialogAdapter adapter = new BottomDialogAdapter(beans, context);
        adapter.setOnPopupClickListener(listener);
        mLvContent.setAdapter(adapter);
        mCameraDialog.setCanceledOnTouchOutside(isTouchHide);
        Window dialogWindow = mCameraDialog.getWindow();
        if (null == dialogWindow) {
            return mCameraDialog;
        }
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = context.getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
        return mCameraDialog;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 底部弹出菜单Adapter
     */
    public static class BottomDialogAdapter extends BaseAdapter {

        private BottomDialogBean[] mBeans;
        private Context mContext;
        private ViewHolder holder;
        private OnPopupClickListener mListener;

        BottomDialogAdapter(BottomDialogBean[] beans, Context context) {
            this.mBeans = beans;
            this.mContext = context;
        }

        public interface OnPopupClickListener {
            void onClick(int position, Dialog dialog);
        }

        void setOnPopupClickListener(OnPopupClickListener listener) {
            this.mListener = listener;
        }

        @Override
        public int getCount() {
            return mBeans.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }


        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (null == view) {
                view = View.inflate(mContext, R.layout.adapter_bottom_dialog, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            BottomDialogBean mBean = mBeans[i];
//            R.drawable.common_dialog_cancel_selector   common_dialog_selector
            holder.mRlItem.setBackgroundResource(i == 0 ? R.drawable.common_dialog_round_selector
                    :R.drawable.common_dialog_cancel_selector);
//            holder.mTvDialogText.setTextColor(i == (mBeans.length - 1) ? ContextCompat.getColor(mContext, R.color.common_color_c1_337cff)
//                    : ContextCompat.getColor(mContext, R.color.common_color_c5_45494e));
            holder.mTvDialogText.setTextColor(ContextCompat.getColor(mContext, R.color.common_color_c5_45494e));
            holder.mTvLine.setVisibility(i == (mBeans.length - 2) ? View.GONE : View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.mRlItem.getLayoutParams();
            params.height = TextUtils.isEmpty(mBean.getDialogDescribe()) ? dip2px(mContext, ITEM_HEIGHT) : dip2px(mContext, DES_ITEM_HEIGHT);
            holder.mTvDialogDescText.setVisibility(TextUtils.isEmpty(mBean.getDialogDescribe()) ? View.GONE : View.VISIBLE);
            holder.mTvDialogDescText.setText(TextUtils.isEmpty(mBean.getDialogDescribe()) ? "" : mBean.getDialogDescribe());
            holder.mTvDialogText.setText(TextUtils.isEmpty(mBean.getDialogText()) ? "" : mBean.getDialogText());
            holder.mRlItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onClick(i, mCameraDialog);
                    }
                }
            });
            return view;
        }

        class ViewHolder {

            private RelativeLayout mRlItem;
            private TextView mTvDialogText;
            private TextView mTvDialogDescText;
            private TextView mTvLine;

            ViewHolder(View view) {
                mRlItem = view.findViewById(R.id.rl_des);
                mTvDialogText = view.findViewById(R.id.tv_dialog_text);
                mTvDialogDescText = view.findViewById(R.id.tv_desc_text);
                mTvLine = view.findViewById(R.id.tv_line);
            }
        }
    }


    /**
     * 获取是否存在NavigationBar
     *
     * @param context 上下文
     * @return boolean
     */
    @SuppressWarnings("unchecked")
    private static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNavigationBar;
    }

    /**
     * 获取虚拟功能键高度
     *
     * @param context 上下文
     * @return 高度
     */
    private static int getVirtualBarHeigh(Context context) {
        int vh = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - windowManager.getDefaultDisplay().getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vh;
    }

    //    public static void showBottomDialogText(Context context, boolean isTouchHide, BottomDialogBean... beans) {
//        final Dialog mCameraDialog = new Dialog(context, R.style.BottomDialog);
//        View root = LayoutInflater.from(context).inflate(
//                R.layout.view_easy_popupwindow_test, null);
//        mCameraDialog.setContentView(root);
//        RelativeLayout mRlDes1 = root.findViewById(R.id.rl_des_1);
//        RelativeLayout mRlDes2 = root.findViewById(R.id.rl_des_2);
//        RelativeLayout mRlDes3 = root.findViewById(R.id.rl_des_3);
//        RelativeLayout mRlDes4 = root.findViewById(R.id.rl_des_4);
//        TextView mTvDes1Text = root.findViewById(R.id.tv_des_1_text);
//        TextView mTvDes1TextDesc = root.findViewById(R.id.tv_des_1_text_desc);
//        TextView mTvDes2Text = root.findViewById(R.id.tv_des_2_text);
//        TextView mTvDes2TextDesc = root.findViewById(R.id.tv_des_2_text_desc);
//        TextView mTvDes3Text = root.findViewById(R.id.tv_des_3_text);
//        TextView mTvDes3TextDesc = root.findViewById(R.id.tv_des_3_text_desc);
//        TextView mTvDes4Text = root.findViewById(R.id.tv_des_4_text);
//        TextView mTvDes4TextDesc = root.findViewById(R.id.tv_des_4_text_desc);
//        switch (beans.length) {
//            case 1:
//                BottomDialogBean bean = beans[0];
//                mRlDes1.setVisibility(View.VISIBLE);
//                mRlDes2.setVisibility(View.GONE);
//                mRlDes3.setVisibility(View.GONE);
//                mRlDes4.setVisibility(View.GONE);
//                mTvDes1Text.setText(bean.getDialogText());
//                mRlDes1.setBackgroundResource(R.drawable.common_dialog_cancel_selector);
//                if(!TextUtils.isEmpty(bean.getDialogDescribe())){
//                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRlDes1.getLayoutParams();
//                    params.height = ConvertUtils.dip2px(context, 80);
//                }
//                break;
//            case 2:
//                mRlDes1.setVisibility(View.VISIBLE);
//                mRlDes2.setVisibility(View.VISIBLE);
//                mRlDes3.setVisibility(View.GONE);
//                mRlDes4.setVisibility(View.GONE);
//                mTvDes1Text.setText(beans[0].getDialogText());
//                mTvDes2Text.setText(beans[1].getDialogText());
//                mRlDes1.setBackgroundResource(R.drawable.common_dialog_cancel_selector);
//                mRlDes2.setBackgroundResource(R.drawable.common_dialog_round_selector);
//                if(!TextUtils.isEmpty(beans[0].getDialogDescribe())){
//                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRlDes1.getLayoutParams();
//                    params.height = ConvertUtils.dip2px(context, 80);
//                }
//                if(!TextUtils.isEmpty(beans[1].getDialogDescribe())){
//                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRlDes2.getLayoutParams();
//                    params.height = ConvertUtils.dip2px(context, 80);
//                }
//                break;
//            case 3:
//                mRlDes1.setVisibility(View.VISIBLE);
//                mRlDes2.setVisibility(View.VISIBLE);
//                mRlDes3.setVisibility(View.VISIBLE);
//                mRlDes4.setVisibility(View.GONE);
//                mTvDes1Text.setText(beans[0].getDialogText());
//                mTvDes2Text.setText(beans[1].getDialogText());
//                mTvDes3Text.setText(beans[2].getDialogText());
//                mRlDes1.setBackgroundResource(R.drawable.common_dialog_cancel_selector);
//                mRlDes2.setBackgroundResource(R.drawable.common_dialog_selector);
//                mRlDes3.setBackgroundResource(R.drawable.common_dialog_round_selector);
//                if(!TextUtils.isEmpty(beans[0].getDialogDescribe())){
//                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRlDes1.getLayoutParams();
//                    params.height = ConvertUtils.dip2px(context, 80);
//                }
//                if(!TextUtils.isEmpty(beans[1].getDialogDescribe())){
//                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRlDes2.getLayoutParams();
//                    params.height = ConvertUtils.dip2px(context, 80);
//                }
//                if(!TextUtils.isEmpty(beans[2].getDialogDescribe())){
//                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRlDes3.getLayoutParams();
//                    params.height = ConvertUtils.dip2px(context, 80);
//                }
//                break;
//            case 4:
//                mRlDes1.setVisibility(View.VISIBLE);
//                mRlDes2.setVisibility(View.VISIBLE);
//                mRlDes3.setVisibility(View.VISIBLE);
//                mRlDes4.setVisibility(View.VISIBLE);
//                mTvDes1Text.setText(beans[0].getDialogText());
//                mTvDes2Text.setText(beans[1].getDialogText());
//                mTvDes3Text.setText(beans[2].getDialogText());
//                mTvDes4Text.setText(beans[3].getDialogText());
//                mRlDes1.setBackgroundResource(R.drawable.common_dialog_cancel_selector);
//                mRlDes2.setBackgroundResource(R.drawable.common_dialog_selector);
//                mRlDes3.setBackgroundResource(R.drawable.common_dialog_selector);
//                mRlDes4.setBackgroundResource(R.drawable.common_dialog_round_selector);
//                if(!TextUtils.isEmpty(beans[0].getDialogDescribe())){
//                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRlDes1.getLayoutParams();
//                    params.height = ConvertUtils.dip2px(context, 80);
//                }
//                if(!TextUtils.isEmpty(beans[1].getDialogDescribe())){
//                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRlDes2.getLayoutParams();
//                    params.height = ConvertUtils.dip2px(context, 80);
//                }
//                if(!TextUtils.isEmpty(beans[2].getDialogDescribe())){
//                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRlDes3.getLayoutParams();
//                    params.height = ConvertUtils.dip2px(context, 80);
//                }
//                if(!TextUtils.isEmpty(beans[3].getDialogDescribe())){
//                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRlDes4.getLayoutParams();
//                    params.height = ConvertUtils.dip2px(context, 80);
//                }
//                break;
//        }
//        mCameraDialog.setCanceledOnTouchOutside(isTouchHide);
//        Window dialogWindow = mCameraDialog.getWindow();
//        if (null == dialogWindow) {
//            return_img;
//        }
//        dialogWindow.setWindowAnimations(R.style.DialogAnimation);
//        dialogWindow.setGravity(Gravity.BOTTOM);
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        if(checkDeviceHasNavigationBar(context)){
////            lp.verticalMargin = getVirtualBarHeigh(context);
//        }else{
//
//        }
//        lp.x = 0; // 新位置X坐标
//        lp.y = 0; // 新位置Y坐标
//        lp.width = context.getResources().getDisplayMetrics().widthPixels; // 宽度
//        root.measure(0, 0);
//        lp.height = root.getMeasuredHeight();
//        lp.alpha = 9f; // 透明度
//        dialogWindow.setAttributes(lp);
//        mCameraDialog.show();
//    }
}
