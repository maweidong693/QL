package com.weiwu.ql.main.mine.friends;

import com.weiwu.ql.main.mine.friends.data.CircleHeadUser;
import com.weiwu.ql.main.mine.friends.data.CircleItem;
import com.weiwu.ql.main.mine.friends.data.CommentConfig;
import com.weiwu.ql.main.mine.friends.data.CommentItem;
import com.weiwu.ql.main.mine.friends.data.FavortItem;

import java.util.List;

/**
 * Created by suneee on 2016/7/15.
 */
public interface CircleContract {

    interface View extends BaseView{
        void update2DeleteCircle(String circleId);
        void update2AddFavorite(int circlePosition, FavortItem addItem);
        void update2DeleteFavort(int circlePosition, String favortId);
        void update2AddComment(int circlePosition, CommentItem addItem);
        void update2DeleteComment(int circlePosition, String commentId);
        void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig);
        void update2loadData(int loadType, List<CircleItem> datas, CircleHeadUser headUser);
    }

    interface Presenter extends BasePresenter{
        void loadData(int loadType);
        void loadData(String msgId);
        void deleteCircle(final String circleId);
        void addFavort(final int circlePosition,String  msgID);
        void deleteFavort(final int circlePosition, final String favortId,String  msgID);
        void deleteComment(final int circlePosition, final String commentId,String type);

    }
}
