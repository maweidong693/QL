package com.weiwu.ql.main.mine.friends;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gyf.immersionbar.ImmersionBar;
import com.hitomi.tilibrary.transfer.TransferConfig;
import com.hitomi.tilibrary.transfer.Transferee;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.tencent.qcloud.tim.uikit.base.ITitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.weiwu.ql.R;
import com.weiwu.ql.base.BaseActivity;
import com.weiwu.ql.data.bean.FriendsData;
import com.weiwu.ql.main.mine.friends.adapter.CircleDetailsAdapter;
import com.weiwu.ql.main.mine.friends.data.CommentConfig;
import com.weiwu.ql.main.mine.friends.data.RefreshBean;
import com.weiwu.ql.main.mine.friends.view.CommentListView;
import com.weiwu.ql.main.mine.friends.view.DivItemDecoration;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class FriendsDetailsActivity extends BaseActivity {

    private long exitTime = 0;//双击toolbar返回顶部计时使用
    protected static final String TAG = FriendsDetailsActivity.class.getSimpleName();
    private CircleDetailsAdapter circleAdapter;
    private LinearLayout edittextbody;
    private TitleBarLayout mTitleBar;
    private EditText editText;
    private ImageView sendIv;
    private int screenHeight;
    private int editTextBodyHeight;
    private int currentKeyboardH;
    private int selectCircleItemH;
    private int selectCommentItemOffset;
    //    private CirclePresenter presenter;
    private SuperRecyclerView recyclerView;
    private RelativeLayout bodyLayout;
    private LinearLayoutManager layoutManager;
    //    private TitleBar titleBar;
    public final static int TYPE_PULLREFRESH = 1;
    public final static int TYPE_UPLOADREFRESH = 2;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;

    boolean mFull = false;

    //图片浏览器
    public Transferee transferee;
    public TransferConfig config;
    private String msgId;

    public static void start(Context context) {
        start(context, null);
    }

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(context, FriendsDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().setStatusBarColor(getResources().getColor(R.color.tool_bar_color));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.tool_bar_color));
            int vis = getWindow().getDecorView().getSystemUiVisibility();
            vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            vis |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            getWindow().getDecorView().setSystemUiVisibility(vis);
        }
        setContentView(R.layout.activity_friends);
        msgId = getIntent().getStringExtra("id");
        initUI();
        ImmersionBar.with(this)
                .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题
                .init();
//        presenter = new CirclePresenter(this);
        initView();
        //实现自动下拉刷新功能
        recyclerView.getSwipeToRefresh().post(new Runnable() {
            @Override
            public void run() {
                recyclerView.setRefreshing(true);//执行下拉刷新的动画
                refreshListener.onRefresh();//执行数据加载操作
            }
        });
//        getRemind();
        initTransferee();

    }

    /**
     * 获取新消息提醒
     */
    private void getRemind() {
        ScheduledExecutorService scheduExec = Executors.newScheduledThreadPool(1);
        scheduExec.scheduleWithFixedDelay(new Runnable() {
            public void run() {


            }
        }, 0, 3, TimeUnit.SECONDS);

    }

    private void initTransferee() {
        transferee = Transferee.getDefault(this);
        transferee.setOnTransfereeStateChangeListener(new Transferee.OnTransfereeStateChangeListener() {
            @Override
            public void onShow() {

            }

            @Override
            public void onDismiss() {
                ImmersionBar.with(FriendsDetailsActivity.this)
                        .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题
                        .init();

            }
        });
    }

    private void initUI() {
        mTitleBar = findViewById(R.id.friend_title_bar);
        mTitleBar.getRightGroup().setVisibility(View.GONE);
//        mTitleBar.setBackgroundColor(getResources().getColor(R.color.tool_bar_color));
        mTitleBar.setTitle("详情", ITitleBarLayout.POSITION.LEFT);
        mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mTitleBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((System.currentTimeMillis() - exitTime) > 1200) {
                    exitTime = System.currentTimeMillis();
                } else {
                    //回到顶部
                    layoutManager.scrollToPosition(0);
                    recyclerView.setRefreshing(true);//执行下拉刷新的动画
                    refreshListener.onRefresh();//执行数据加载操作
                }
            }
        });
    }


    @SuppressLint({"ClickableViewAccessibility", "InlinedApi"})
    private void initView() {

        recyclerView = (SuperRecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DivItemDecoration(2, true));
        recyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (edittextbody.getVisibility() == View.VISIBLE) {
                    //隐藏键盘
//                    updateEditTextBodyVisible(View.GONE, null);
                    return true;
                }
                return false;
            }
        });

        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        presenter.loadData(msgId);
                    }
                }, 100);
            }
        };
        recyclerView.setRefreshListener(refreshListener);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            int firstVisibleItem, lastVisibleItem;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    Glide.with(FriendsActivity.this).resumeRequests();
//                } else {
//                    Glide.with(FriendsActivity.this).pauseRequests();
//                }

                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                //大于0说明有播放
                if (GSYVideoManager.instance().getPlayPosition() >= 0) {
                    //当前播放的位置
                    int position = GSYVideoManager.instance().getPlayPosition();
                    //对应的播放列表TAG
                    //对应的播放列表TAG
                    if (GSYVideoManager.instance().getPlayTag().equals("PlayTag")
                            && (position < firstVisibleItem || position > lastVisibleItem)) {
                        if (GSYVideoManager.isFullState(FriendsDetailsActivity.this)) {
                            return;
                        }
                        //如果滑出去了上面和下面就是否，和今日头条一样
                        GSYVideoManager.releaseAllVideos();
//                        circleAdapter.notifyDataSetChanged();
                    }

//                    if (GSYVideoManager.instance().getPlayTag().equals("PlayTag")
//                            && (position < firstVisibleItem || position > lastVisibleItem)) {
//
//                        //如果滑出去了上面和下面就是否，和今日头条一样
//                        //是否全屏
//                        if (!mFull) {
//                            GSYVideoManager.releaseAllVideos();
////                            circleAdapter.notifyDataSetChanged();
////                            circleAdapter.notifyItemChanged(position);
//                        }
//                    }
                }

            }
        });

        edittextbody = (LinearLayout) findViewById(R.id.editTextBodyLl);
        edittextbody.setVisibility(View.VISIBLE);
        editText = (EditText) findViewById(R.id.circleEt);
        sendIv = (ImageView) findViewById(R.id.sendIv);
        sendIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (presenter != null) {
                    //发布评论
                    String content = editText.getText().toString().trim();
                    if (TextUtils.isEmpty(content)) {
                        Toast.makeText(FriendsDetailsActivity.this, "评论内容不能为空...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    /*if (commentConfig.msgId == 0) {
                        FriendsData.DataDTO.MessageDTO item = (FriendsData.DataDTO.MessageDTO) circleAdapter.getDatas().get(commentConfig.circlePosition);
                        commentConfig.msgId = item.getId();
                    }*/
//                    presenter.addComment(content, commentConfig);
//                }
//                updateEditTextBodyVisible(View.GONE, null);
            }
        });

        setViewTreeObserver();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBaseBean(RefreshBean event) {
        //后台拉取资料完成之后刷新
//        presenter.loadData(TYPE_PULLREFRESH);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        mFull = newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_USER;
    }


    private void setViewTreeObserver() {
        bodyLayout = (RelativeLayout) findViewById(R.id.bodyLayout);
        final ViewTreeObserver swipeRefreshLayoutVTO = bodyLayout.getViewTreeObserver();
        swipeRefreshLayoutVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                bodyLayout.getWindowVisibleDisplayFrame(r);
                int statusBarH = getStatusBarHeight();//状态栏高度
                int screenH = bodyLayout.getRootView().getHeight();
                if (r.top != statusBarH) {
                    //在这个demo中r.top代表的是状态栏高度，在沉浸式状态栏时r.top＝0，通过getStatusBarHeight获取状态栏高度
                    r.top = statusBarH;
                }
                int keyboardH = screenH - (r.bottom - r.top);
//                Log.d(TAG, "screenH＝ " + screenH + " &keyboardH = " + keyboardH + " &r.bottom=" + r.bottom + " &top=" + r.top + " &statusBarH=" + statusBarH);

                if (keyboardH == currentKeyboardH) {//有变化时才处理，否则会陷入死循环
                    return;
                }

                currentKeyboardH = keyboardH;
                screenHeight = screenH;//应用屏幕的高度
                editTextBodyHeight = edittextbody.getHeight();

                if (keyboardH < 150) {//说明是隐藏键盘的情况
//                    updateEditTextBodyVisible(View.GONE, null);
                    return;
                }
                //偏移listview
                /*if (layoutManager != null && commentConfig != null) {
                    layoutManager.scrollToPositionWithOffset(commentConfig.circlePosition + CircleDetailsAdapter.HEADVIEW_SIZE, getListviewOffset(commentConfig));
                }*/
            }
        });
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (edittextbody != null && edittextbody.getVisibility() == View.VISIBLE) {
                //edittextbody.setVisibility(View.GONE);
//                updateEditTextBodyVisible(View.GONE, null);
//                CommonUtils.hideSoftInput(editText.getContext(), editText);
                return true;
            }
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (GSYVideoManager.backFromWindowFull(this)) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /*@Override
    public void update2DeleteCircle(String circleId) {
        List<FriendsData.DataDTO.MessageDTO> circleItems = circleAdapter.getDatas();
        for (int i = 0; i < circleItems.size(); i++) {
            if (circleId.equals(circleItems.get(i).getId())) {
                circleItems.remove(i);
                circleAdapter.notifyDataSetChanged();
                //circleAdapter.notifyItemRemoved(i+1);
                return;
            }
        }
    }*/

    /*@Override
    public void update2AddFavorite(int circlePosition, FriendsData.DataDTO.MessageDTO.LikesDTO addItem) {
        if (addItem != null) {
            FriendsData.DataDTO.MessageDTO item = (FriendsData.DataDTO.MessageDTO) circleAdapter.getDatas().get(circlePosition);
            item.getLikes().add(addItem);
            circleAdapter.notifyDataSetChanged();
            //circleAdapter.notifyItemChanged(circlePosition+1);
        }
    }*/

    /*@Override
    public void update2DeleteFavort(int circlePosition, String favortId) {
        FriendsData.DataDTO.MessageDTO item = (FriendsData.DataDTO.MessageDTO) circleAdapter.getDatas().get(circlePosition);
        List<FriendsData.DataDTO.MessageDTO.LikesDTO> items = item.getLikes();
        for (int i = 0; i < items.size(); i++) {
            if (favortId.equals(items.get(i).getLikerId())) {
                items.remove(i);
                circleAdapter.notifyDataSetChanged();
                //circleAdapter.notifyItemChanged(circlePosition+1);
                return;
            }
        }
    }*/

   /* @Override
    public void update2AddComment(int circlePosition, CommentItem addItem) {
        if (addItem != null) {
            CircleItem item = (CircleItem) circleAdapter.getDatas().get(circlePosition);
            item.getComments().add(addItem);
            circleAdapter.notifyDataSetChanged();
            //circleAdapter.notifyItemChanged(circlePosition+1);
        }
        //清空评论文本
        editText.setText("");
    }*/

   /* @Override
    public void update2DeleteComment(int circlePosition, String commentId) {
        CircleItem item = (CircleItem) circleAdapter.getDatas().get(circlePosition);
        List<CommentItem> items = item.getComments();
        for (int i = 0; i < items.size(); i++) {
            if (commentId.equals(items.get(i).getId())) {
                items.remove(i);
                circleAdapter.notifyDataSetChanged();
                //circleAdapter.notifyItemChanged(circlePosition+1);
                return;
            }
        }
    }*/

   /* @Override
    public void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig) {
        this.commentConfig = commentConfig;
        edittextbody.setVisibility(visibility);

        measureCircleItemHighAndCommentItemOffset(commentConfig);

        if (View.VISIBLE == visibility) {
            editText.requestFocus();
            //弹出键盘
            new YHandler().post(new Runnable() {
                @Override
                public void run() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //弹出键盘
                            CommonUtils.showSoftInput(editText.getContext(), editText);
                        }
                    }, 120);
                }
            });
        } else if (View.GONE == visibility) {
            //隐藏键盘
            CommonUtils.hideSoftInput(editText.getContext(), editText);
//            editText.clearFocus();
        }
    }*/

    /*@Override
    public void update2loadData(int loadType, List<CircleItem> datas, CircleHeadUser headUser) {
        recyclerView.setRefreshing(false);
        if (circleAdapter == null) {
            circleAdapter = new CircleDetailsAdapter(FriendsDetailsActivity.this);
            circleAdapter.setCirclePresenter(presenter);
            recyclerView.setAdapter(circleAdapter);
        }
        circleAdapter.setDatas(datas, headUser);
        circleAdapter.notifyDataSetChanged();
        recyclerView.removeMoreListener();
        recyclerView.hideMoreProgress();

    }*/


    /**
     * 测量偏移量
     *
     * @param commentConfig
     * @return
     */
    private int getListviewOffset(CommentConfig commentConfig) {
        if (commentConfig == null)
            return 0;
        //这里如果你的listview上面还有其它占高度的控件，则需要减去该控件高度，listview的headview除外。
        //int listviewOffset = mScreenHeight - mSelectCircleItemH - mCurrentKeyboardH - mEditTextBodyHeight;
//        int listviewOffset = screenHeight - selectCircleItemH - currentKeyboardH - editTextBodyHeight - titleBar.getHeight();
        int listviewOffset = screenHeight - selectCircleItemH - currentKeyboardH - editTextBodyHeight - mTitleBar.getHeight() + ImmersionBar.getStatusBarHeight(this);
        if (commentConfig.commentType == CommentConfig.Type.REPLY) {
            //回复评论的情况
            listviewOffset = listviewOffset + selectCommentItemOffset;
        }
//        Log.i(TAG, "listviewOffset : " + listviewOffset);
        return listviewOffset;
    }

    private void measureCircleItemHighAndCommentItemOffset(CommentConfig commentConfig) {
        if (commentConfig == null)
            return;

        int firstPosition = layoutManager.findFirstVisibleItemPosition();
        //只能返回当前可见区域（列表可滚动）的子项
        View selectCircleItem = layoutManager.getChildAt(commentConfig.circlePosition + CircleDetailsAdapter.HEADVIEW_SIZE - firstPosition);

        if (selectCircleItem != null) {
            selectCircleItemH = selectCircleItem.getHeight();
        }

        if (commentConfig.commentType == CommentConfig.Type.REPLY) {
            //回复评论的情况
            CommentListView commentLv = (CommentListView) selectCircleItem.findViewById(R.id.commentList);
            if (commentLv != null) {
                //找到要回复的评论view,计算出该view距离所属动态底部的距离
                View selectCommentItem = commentLv.getChildAt(commentConfig.commentPosition);
                if (selectCommentItem != null) {
                    //选择的commentItem距选择的CircleItem底部的距离
                    selectCommentItemOffset = 0;
                    View parentView = selectCommentItem;
                    do {
                        int subItemBottom = parentView.getBottom();
                        parentView = (View) parentView.getParent();
                        if (parentView != null) {
                            selectCommentItemOffset += (parentView.getHeight() - subItemBottom);
                        }
                    } while (parentView != null && parentView != selectCircleItem);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //此处可以根据两个Code进行判断，本页面和结果页面跳过来的值
        if (requestCode == 1 && resultCode == RESULT_OK) {
            //刷新
//            presenter.loadData(TYPE_PULLREFRESH);
        }
    }

    @Override
    protected void onDestroy() {
        /*if (presenter != null) {
            presenter.recycle();
        }*/
        if (circleAdapter != null) {
            circleAdapter.stopRind();
        }
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }


}
