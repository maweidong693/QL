package com.weiwu.ql.main.find;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.tencent.qcloud.tim.uikit.component.CustomLinearLayoutManager;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseFragment;
import com.weiwu.ql.data.bean.FindData;
import com.weiwu.ql.data.repositories.FindRepository;
import com.weiwu.ql.main.mine.friends.FriendsContract;
import com.weiwu.ql.utils.IntentUtil;


public class MoreFragment extends BaseFragment implements FriendsContract.IFindView {

    private TitleBarLayout mConversationTitle;
    private RecyclerView mRvFindList;
    private CustomLinearLayoutManager mManager;
    private BaseQuickAdapter<FindData.DataDTO, BaseViewHolder> mAdapter;
    private FriendsContract.IFindPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_more;
    }

    @Override
    public void onViewCreated(View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setPresenter(new FindPresenter(FindRepository.getInstance()));
        initView(view);
    }

    private void initView(View view) {
        mConversationTitle = (TitleBarLayout) view.findViewById(R.id.conversation_title);
        mRvFindList = (RecyclerView) view.findViewById(R.id.rv_find_list);

        mConversationTitle.setTitle(getResources().getString(R.string.find), TitleBarLayout.POSITION.MIDDLE);
        mConversationTitle.getLeftGroup().setVisibility(View.GONE);
        mConversationTitle.getRightGroup().setVisibility(View.GONE);

        mRvFindList.setHasFixedSize(true);
        mManager = new CustomLinearLayoutManager(getActivity());
        mRvFindList.setLayoutManager(mManager);
//        mRecyclerView.addItemDecoration(mDecoration = new SuspensionDecoration(getContext(), null));
        mRvFindList.setHasFixedSize(true);

        mAdapter = new BaseQuickAdapter<FindData.DataDTO, BaseViewHolder>(R.layout.messge_list_itemm) {
            @Override
            protected void convert(BaseViewHolder helper, FindData.DataDTO item) {
                Typeface iconfont = Typeface.createFromAsset(getActivity().getAssets(), "iconfont/iconfont.ttf");
                TextView textView = helper.getView(R.id.img_head);
                try {
                    (textView).setTypeface(iconfont);
                    (textView).setText(getResources().getIdentifier(item.getIcon(), "string", getActivity().getPackageName()));
                } catch (Exception e) {
                    (textView).setTypeface(iconfont);
                    (textView).setText(getResources().getIdentifier("tupian", "string", getActivity().getPackageName()));
                    e.printStackTrace();
                }
                helper.setText(R.id.tv_title, item.getTitle());
            }
        };
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("url", ((FindData.DataDTO) adapter.getData().get(position)).getAddress());
                bundle.putString("title", getResources().getString(R.string.announcement_title));
//                Intent intent = new Intent((Activity) getContext(), NewsActivity.class);
//                ((Activity) getContext()).startActivity(intent);
                IntentUtil.redirectToNextActivity(getActivity(), NewsActivity.class, bundle);
            }
        });
        mRvFindList.setAdapter(mAdapter);
        mPresenter.getFindList();
    }

    @Override
    public void onSuccess(FindData data) {
        if (data.getData() != null) {
            mAdapter.setList(data.getData());
        }
    }

    @Override
    public void onFail(String msg, int code) {

    }

    @Override
    public void setPresenter(FriendsContract.IFindPresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    public Activity getActivityObject() {
        return getActivity();
    }
}