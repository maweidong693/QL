package com.weiwu.ql.main.mine.friends;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.tencent.common.Constant;
import com.tencent.common.UserInfo;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.weiwu.ql.AppConstant;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;
import com.weiwu.ql.data.bean.FriendsData;
import com.weiwu.ql.main.contact.detail.FriendProfileActivity;
import com.weiwu.ql.main.mine.friends.adapter.MultipleItemFriendsCircleAdapter;
import com.weiwu.ql.main.mine.friends.data.RefreshBean;
import com.weiwu.ql.main.mine.friends.msg.MsgListActivity;
import com.weiwu.ql.main.mine.friends.view.DivItemDecoration;
import com.weiwu.ql.utils.IntentUtil;
import com.weiwu.ql.utils.SPUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class FriendsAndMeCircleActivity extends BaseActivity {

    private long exitTime = 0;//双击toolbar返回顶部计时使用
    protected static final String TAG = FriendsAndMeCircleActivity.class.getSimpleName();

//    private CirclePresenter presenter;
    private SuperRecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private MultipleItemFriendsCircleAdapter circleAdapter;
    public final static int TYPE_PULLREFRESH = 1;
    public final static int TYPE_UPLOADREFRESH = 2;
    private String im_id;
    private ImageView circleBg;
    private ImageView circleHead;
    private TextView circleName;

    public static void start(Context context) {
        start(context, null);
    }

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(context, FriendsAndMeCircleActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            getWindow().setStatusBarColor(getResources().getColor(R.color.tool_bar_color));
//            getWindow().setNavigationBarColor(getResources().getColor(R.color.tool_bar_color));
//            int vis = getWindow().getDecorView().getSystemUiVisibility();
//            vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
//            vis |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
//            getWindow().getDecorView().setSystemUiVisibility(vis);
        }
        setContentView(R.layout.friend_circcle_head);
        im_id = getIntent().getStringExtra("im_id");
        initUI();
//        presenter = new CirclePresenter(this);
        initView();
        recyclerView.setRefreshing(false);
    }

    private void initUI() {
//        mTitleBar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if ((System.currentTimeMillis() - exitTime) > 1200) {
//                    exitTime = System.currentTimeMillis();
//                } else {
//                    //回到顶部
//                    layoutManager.scrollToPosition(0);
//                    recyclerView.setRefreshing(true);//执行下拉刷新的动画
//                    refreshListener.onRefresh();//执行数据加载操作
//                }
//            }
//        });
    }


    @SuppressLint({"ClickableViewAccessibility", "InlinedApi"})
    private void initView() {

        recyclerView = (SuperRecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DivItemDecoration(2, true));
        recyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                presenter.loadData(TYPE_PULLREFRESH, im_id);
            }
        }, 100);

//        recyclerView.setRefreshListener(refreshListener);
        circleAdapter = new MultipleItemFriendsCircleAdapter(new ArrayList<>());
//        View headView = LayoutInflater.from(this).inflate(R.layout.friend_circle_head,null);
        AppBarLayout appBarLayout = findViewById(R.id.appBar);
        TextView title = findViewById(R.id.title);
        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(view -> {
            finish();
        });
        ImageView ivNews = findViewById(R.id.iv_news);
        ivNews.setVisibility(im_id.equals(SPUtils.getValue(AppConstant.USER,AppConstant.USER_ID)
) ? View.VISIBLE : View.GONE);
        ivNews.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean("notice", false);
            IntentUtil.redirectToNextActivity(this, MsgListActivity.class, bundle);
        });
        circleBg = findViewById(R.id.circle_bg);
        circleHead = findViewById(R.id.circle_head);
        circleName = findViewById(R.id.circle_name);
        circleHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //详细资料
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setId(im_id);
                chatInfo.setType(V2TIMConversation.V2TIM_C2C);
                Intent intent = new Intent(FriendsAndMeCircleActivity.this, FriendProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(TUIKitConstants.ProfileType.CONTENT, chatInfo);
                startActivity(intent);

            }
        });
        Toolbar toolbarView = findViewById(R.id.toolbar_view);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
//                int color = Color.argb(200,0,0,0);
//                collapsingToolbar.setCollapsedTitleTextColor(R.color.status_bar_color);
//                collapsingToolbar.setBackgroundColor(R.color.status_bar_color);
                if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) { // 折叠状态
//                    toolbar_view.setTitle("朋友圈");
//                    toolbarView.setBackgroundColor(Color.parseColor("#EDEDED"));
                    toolbarView.setBackgroundResource(R.drawable.bg_title);
                    title.setVisibility(View.VISIBLE);
                    title.setText(im_id.equals(SPUtils.getValue(AppConstant.USER,AppConstant.USER_ID)
) ? "我的相册" : circleName.getText());
//                    collapsingToolbar.setTitle("相册");
                    circleHead.setVisibility(View.GONE);
                    ivBack.setBackgroundResource(R.drawable.ic_white_back);
                    ivNews.setBackgroundResource(R.drawable.circle_white);
                } else { // 非折叠状态
                    toolbarView.setBackgroundColor(Color.parseColor("#00000000"));
//                    collapsingToolbar.setTitle("");
                    title.setVisibility(View.GONE);
                    circleHead.setVisibility(View.VISIBLE);
                    ivBack.setBackgroundResource(R.drawable.ic_white_back);
                    ivNews.setBackgroundResource(R.drawable.circle_white);
//                    back_view.setVisibility(View.VISIBLE);
//                    camera.setVisibility(View.VISIBLE);
                }
            }
        });
//        circleAdapter.addHeaderView(headView);
        recyclerView.setAdapter(circleAdapter);
        circleAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                FriendsData.DataDTO.MessageDTO circleItem = (FriendsData.DataDTO.MessageDTO) adapter.getData().get(position);
                Bundle bundle = new Bundle();
                bundle.putString("id", circleItem.getId() + "");
                IntentUtil.redirectToNextActivity(FriendsAndMeCircleActivity.this, FriendsDetailsActivity.class, bundle);
            }
        });
        ImmersionBar.with(this).titleBar(R.id.toolbar_view)
                .navigationBarColor(R.color.status_bar_color)
                .init();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBaseBean(RefreshBean event) {
        //后台拉取资料完成之后刷新
//        presenter.loadData(TYPE_PULLREFRESH, im_id);
    }

    /*@Override
    public void update2loadData(int loadType, List<CircleItem> datas, CircleHeadUser headUser) {
        if (loadType == TYPE_PULLREFRESH) {
            GlideEngine.createGlideEngine().loadImage(this, headUser.getCircleBgUrl(), circleBg);
            GlideEngine.createGlideEngine().loadCornerImage(circleHead, headUser.getHeadUrl(), null, Constant.RADIUS);
            circleName.setText(headUser.getName());
            circleAdapter.setList(datas);
        } else if (loadType == TYPE_UPLOADREFRESH) {
            circleAdapter.addData(datas);
        }
//        circleAdapter.notifyDataSetChanged();
        if (datas.size() > 19) {
            recyclerView.setupMoreListener(new OnMoreListener() {
                @Override
                public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            presenter.loadData(TYPE_UPLOADREFRESH, im_id);
                        }
                    }, 1000);
                }
            }, 1);
        } else {
            recyclerView.removeMoreListener();
            recyclerView.hideMoreProgress();
        }
    }*/

    @Override
    protected void onDestroy() {
        /*if (presenter != null) {
            presenter.recycle();
        }*/
//        if (transferee!=null){
//            transferee.clear();
//        }
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}
