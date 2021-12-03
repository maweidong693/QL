package com.weiwu.ql.data.bean;

import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/18 13:59 
 */
public class FriendsData {


    private int code;
    private String msg;
    private DataDTO data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public static class DataDTO {
        private String create_time;
        private int show_range;
        private String backgroundImg;
        private String ownerName;
        private String ownerFaceUrl;
        private List<MessageDTO> message;

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public int getShow_range() {
            return show_range;
        }

        public void setShow_range(int show_range) {
            this.show_range = show_range;
        }

        public String getBackgroundImg() {
            return backgroundImg;
        }

        public void setBackgroundImg(String backgroundImg) {
            this.backgroundImg = backgroundImg;
        }

        public String getOwnerName() {
            return ownerName;
        }

        public void setOwnerName(String ownerName) {
            this.ownerName = ownerName;
        }

        public String getOwnerFaceUrl() {
            return ownerFaceUrl;
        }

        public void setOwnerFaceUrl(String ownerFaceUrl) {
            this.ownerFaceUrl = ownerFaceUrl;
        }

        public List<MessageDTO> getMessage() {
            return message;
        }

        public void setMessage(List<MessageDTO> message) {
            this.message = message;
        }

        public static class MessageDTO implements MultiItemEntity {
            public final static int TYPE_CIRCLE_TEXT = 1;
            public final static int TYPE_CIRCLE_IMG = 2;
            public final static int TYPE_CIRCLE_VIDEO =3;

            private int id;
            private String im_id;
            private String nick_name;
            private int create_id;
            private String face_url;
            private String create_time;
            private String location;
            private String text_content;
            private String mediaPreview;
            private String mediaUrl;
            private List<String> pics;
            private boolean like;
            private List<LikesDTO> likes;
            private List<CommentAndRepliesDTO> commentAndReplies;

            private boolean isExpand;

            public boolean isExpand() {
                return isExpand;
            }

            public void setExpand(boolean expand) {
                isExpand = expand;
            }

            public boolean hasFavort() {
                if (likes != null && likes.size() > 0) {
                    return true;
                }
                return false;
            }

            public boolean hasComment() {
                if (commentAndReplies != null && commentAndReplies.size() > 0) {
                    return true;
                }
                return false;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getIm_id() {
                return im_id;
            }

            public void setIm_id(String im_id) {
                this.im_id = im_id;
            }

            public String getNick_name() {
                return nick_name;
            }

            public void setNick_name(String nick_name) {
                this.nick_name = nick_name;
            }

            public int getCreate_id() {
                return create_id;
            }

            public void setCreate_id(int create_id) {
                this.create_id = create_id;
            }

            public String getFace_url() {
                return face_url;
            }

            public void setFace_url(String face_url) {
                this.face_url = face_url;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public String getText_content() {
                return text_content;
            }

            public void setText_content(String text_content) {
                this.text_content = text_content;
            }

            public String getMediaPreview() {
                return mediaPreview;
            }

            public void setMediaPreview(String mediaPreview) {
                this.mediaPreview = mediaPreview;
            }

            public String getMediaUrl() {
                return mediaUrl;
            }

            public void setMediaUrl(String mediaUrl) {
                this.mediaUrl = mediaUrl;
            }

            public List<String> getPics() {
                return pics;
            }

            public void setPics(List<String> pics) {
                this.pics = pics;
            }

            public boolean isLike() {
                return like;
            }

            public void setLike(boolean like) {
                this.like = like;
            }

            public List<LikesDTO> getLikes() {
                return likes;
            }

            public void setLikes(List<LikesDTO> likes) {
                this.likes = likes;
            }

            public List<CommentAndRepliesDTO> getCommentAndReplies() {
                return commentAndReplies;
            }

            public void setCommentAndReplies(List<CommentAndRepliesDTO> commentAndReplies) {
                this.commentAndReplies = commentAndReplies;
            }

            public int getCurUserFavortId(String curUserId) {
                int favortid = 0;
                if (!TextUtils.isEmpty(curUserId) && hasFavort()) {
                    for (LikesDTO item : likes) {
                        if (curUserId.equals(item.getIm_id())) {
                            favortid = item.getLikerId();
                            return favortid;
                        }
                    }
                }
                return favortid;
            }

            @Override
            public int getItemType() {
                return 0;
            }

            public static class LikesDTO {
                private String im_id;
                private int likerId;
                private String likernick_name;

                public String getIm_id() {
                    return im_id;
                }

                public void setIm_id(String im_id) {
                    this.im_id = im_id;
                }

                public int getLikerId() {
                    return likerId;
                }

                public void setLikerId(int likerId) {
                    this.likerId = likerId;
                }

                public String getLikernick_name() {
                    return likernick_name;
                }

                public void setLikernick_name(String likernick_name) {
                    this.likernick_name = likernick_name;
                }
            }

            public static class CommentAndRepliesDTO {
                private int id;
                private String im_id;
                private String commentatornick_name;
                private int commentatorId;
                private String content;
                private String commentatorIcon;
                private int commentId;
                private int type;
                private String replyImId;
                private String replyAlias;
                private String replyIcon;
                private String replyId;
                private String replyName;
                private String replyToImId;
                private String replyToAlias;
                private String replyToIcon;
                private String replyToId;
                private String replyToName;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getIm_id() {
                    return im_id;
                }

                public void setIm_id(String im_id) {
                    this.im_id = im_id;
                }

                public String getCommentatornick_name() {
                    return commentatornick_name;
                }

                public void setCommentatornick_name(String commentatornick_name) {
                    this.commentatornick_name = commentatornick_name;
                }

                public int getCommentatorId() {
                    return commentatorId;
                }

                public void setCommentatorId(int commentatorId) {
                    this.commentatorId = commentatorId;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getCommentatorIcon() {
                    return commentatorIcon;
                }

                public void setCommentatorIcon(String commentatorIcon) {
                    this.commentatorIcon = commentatorIcon;
                }

                public int getCommentId() {
                    return commentId;
                }

                public void setCommentId(int commentId) {
                    this.commentId = commentId;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public String getReplyImId() {
                    return replyImId;
                }

                public void setReplyImId(String replyImId) {
                    this.replyImId = replyImId;
                }

                public String getReplyAlias() {
                    return replyAlias;
                }

                public void setReplyAlias(String replyAlias) {
                    this.replyAlias = replyAlias;
                }

                public String getReplyIcon() {
                    return replyIcon;
                }

                public void setReplyIcon(String replyIcon) {
                    this.replyIcon = replyIcon;
                }

                public String getReplyId() {
                    return replyId;
                }

                public void setReplyId(String replyId) {
                    this.replyId = replyId;
                }

                public String getReplyName() {
                    return replyName;
                }

                public void setReplyName(String replyName) {
                    this.replyName = replyName;
                }

                public String getReplyToImId() {
                    return replyToImId;
                }

                public void setReplyToImId(String replyToImId) {
                    this.replyToImId = replyToImId;
                }

                public String getReplyToAlias() {
                    return replyToAlias;
                }

                public void setReplyToAlias(String replyToAlias) {
                    this.replyToAlias = replyToAlias;
                }

                public String getReplyToIcon() {
                    return replyToIcon;
                }

                public void setReplyToIcon(String replyToIcon) {
                    this.replyToIcon = replyToIcon;
                }

                public String getReplyToId() {
                    return replyToId;
                }

                public void setReplyToId(String replyToId) {
                    this.replyToId = replyToId;
                }

                public String getReplyToName() {
                    return replyToName;
                }

                public void setReplyToName(String replyToName) {
                    this.replyToName = replyToName;
                }
            }
        }
    }
}
