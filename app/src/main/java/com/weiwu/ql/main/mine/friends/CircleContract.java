package com.weiwu.ql.main.mine.friends;

import com.weiwu.ql.data.bean.FriendsData;
import com.weiwu.ql.main.mine.friends.data.CircleHeadUser;

import java.util.List;

/**
 * Created by suneee on 2016/7/15.
 */
public interface CircleContract {

    interface View extends BaseView{
        void update2DeleteCircle(String circleId);
        void update2AddFavorite(int circlePosition, FriendsData.DataDTO.MessageDTO.LikesDTO addItem);
        void update2DeleteFavort(int circlePosition, String favortId);
        void update2AddComment(int circlePosition, FriendsData.DataDTO.MessageDTO.CommentAndRepliesDTO addItem);
        void update2DeleteComment(int circlePosition, String commentId);
        void updateEditTextBodyVisible(int visibility);
        void update2loadData(int loadType, List<FriendsData.DataDTO.MessageDTO> datas, CircleHeadUser headUser);
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
