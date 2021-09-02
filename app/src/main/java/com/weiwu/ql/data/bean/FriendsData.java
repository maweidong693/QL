package com.weiwu.ql.data.bean;

import android.text.TextUtils;

import com.weiwu.ql.main.mine.friends.data.FavortItem;

import java.util.List;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/18 13:59 
 */
public class FriendsData {

    private int code;
    private String message;
    private List<DataDTO> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public static class DataDTO {
        private String id;
        private String imgUrlJson;
        private List<String> imgUrl;
        private String videoUrl;
        private String article;
        private String memberId;
        private MemberInfoResultVODTO memberInfoResultVO;
        private String createdTime;
        private String updatedTime;
        private String createdBy;
        private String updatedBy;
        private int type;
        private List<ThumbListDTO> thumbList;
        private List<CommentAndReplyListDTO> commentAndReplyList;
        private boolean isExpand;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getCurUserFavortId(String curUserId) {
            String favortid = "";
            if (!TextUtils.isEmpty(curUserId) && hasFavort()) {
                for (ThumbListDTO item : thumbList) {
                    if (curUserId.equals(item.getFromMemberInfo().getId())) {
                        favortid = item.getId();
                        return favortid;
                    }
                }
            }
            return favortid;
        }

        public boolean hasFavort() {
            if (thumbList != null && thumbList.size() > 0) {
                return true;
            }
            return false;
        }

        public boolean hasComment() {
            if (commentAndReplyList != null && commentAndReplyList.size() > 0) {
                return true;
            }
            return false;
        }

        public boolean isExpand() {
            return isExpand;
        }

        public void setExpand(boolean expand) {
            isExpand = expand;
        }

        public String getArticle() {
            return article;
        }

        public void setArticle(String article) {
            this.article = article;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImgUrlJson() {
            return imgUrlJson;
        }

        public void setImgUrlJson(String imgUrlJson) {
            this.imgUrlJson = imgUrlJson;
        }

        public List<String> getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(List<String> imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public MemberInfoResultVODTO getMemberInfoResultVO() {
            return memberInfoResultVO;
        }

        public void setMemberInfoResultVO(MemberInfoResultVODTO memberInfoResultVO) {
            this.memberInfoResultVO = memberInfoResultVO;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

        public String getUpdatedTime() {
            return updatedTime;
        }

        public void setUpdatedTime(String updatedTime) {
            this.updatedTime = updatedTime;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getUpdatedBy() {
            return updatedBy;
        }

        public void setUpdatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
        }

        public List<ThumbListDTO> getThumbList() {
            return thumbList;
        }

        public void setThumbList(List<ThumbListDTO> thumbList) {
            this.thumbList = thumbList;
        }

        public List<CommentAndReplyListDTO> getCommentAndReplyList() {
            return commentAndReplyList;
        }

        public void setCommentAndReplyList(List<CommentAndReplyListDTO> commentAndReplyList) {
            this.commentAndReplyList = commentAndReplyList;
        }

        public static class MemberInfoResultVODTO {
            private String id;
            private String nickName;
            private String mobile;
            private String lastLoginTime;
            private String createdTime;
            private String updatedTime;
            private String avator;
            private int sex;

            public String getAvator() {
                return avator;
            }

            public void setAvator(String avator) {
                this.avator = avator;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getLastLoginTime() {
                return lastLoginTime;
            }

            public void setLastLoginTime(String lastLoginTime) {
                this.lastLoginTime = lastLoginTime;
            }

            public String getCreatedTime() {
                return createdTime;
            }

            public void setCreatedTime(String createdTime) {
                this.createdTime = createdTime;
            }

            public String getUpdatedTime() {
                return updatedTime;
            }

            public void setUpdatedTime(String updatedTime) {
                this.updatedTime = updatedTime;
            }

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }
        }

        public static class ThumbListDTO {
            private String id;
            private String momentsId;
            private String memberId;
            private FromMemberInfoDTO fromMemberInfo;
            private int type;
            private String createdTime;
            private String createdBy;
            private String updatedTime;
            private String updatedBy;
            private int del;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getMomentsId() {
                return momentsId;
            }

            public void setMomentsId(String momentsId) {
                this.momentsId = momentsId;
            }

            public String getMemberId() {
                return memberId;
            }

            public void setMemberId(String memberId) {
                this.memberId = memberId;
            }

            public FromMemberInfoDTO getFromMemberInfo() {
                return fromMemberInfo;
            }

            public void setFromMemberInfo(FromMemberInfoDTO fromMemberInfo) {
                this.fromMemberInfo = fromMemberInfo;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getCreatedTime() {
                return createdTime;
            }

            public void setCreatedTime(String createdTime) {
                this.createdTime = createdTime;
            }

            public String getCreatedBy() {
                return createdBy;
            }

            public void setCreatedBy(String createdBy) {
                this.createdBy = createdBy;
            }

            public String getUpdatedTime() {
                return updatedTime;
            }

            public void setUpdatedTime(String updatedTime) {
                this.updatedTime = updatedTime;
            }

            public String getUpdatedBy() {
                return updatedBy;
            }

            public void setUpdatedBy(String updatedBy) {
                this.updatedBy = updatedBy;
            }

            public int getDel() {
                return del;
            }

            public void setDel(int del) {
                this.del = del;
            }

            public static class FromMemberInfoDTO {
                private String id;
                private String nickName;
                private String avator;
                private String mobile;
                private String momentImg;
                private String personalSignature;
                private String lastLoginTime;
                private String createdTime;
                private String updatedTime;
                private int sex;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getNickName() {
                    return nickName;
                }

                public void setNickName(String nickName) {
                    this.nickName = nickName;
                }

                public String getAvator() {
                    return avator;
                }

                public void setAvator(String avator) {
                    this.avator = avator;
                }

                public String getMobile() {
                    return mobile;
                }

                public void setMobile(String mobile) {
                    this.mobile = mobile;
                }

                public String getMomentImg() {
                    return momentImg;
                }

                public void setMomentImg(String momentImg) {
                    this.momentImg = momentImg;
                }

                public String getPersonalSignature() {
                    return personalSignature;
                }

                public void setPersonalSignature(String personalSignature) {
                    this.personalSignature = personalSignature;
                }

                public String getLastLoginTime() {
                    return lastLoginTime;
                }

                public void setLastLoginTime(String lastLoginTime) {
                    this.lastLoginTime = lastLoginTime;
                }

                public String getCreatedTime() {
                    return createdTime;
                }

                public void setCreatedTime(String createdTime) {
                    this.createdTime = createdTime;
                }

                public String getUpdatedTime() {
                    return updatedTime;
                }

                public void setUpdatedTime(String updatedTime) {
                    this.updatedTime = updatedTime;
                }

                public int getSex() {
                    return sex;
                }

                public void setSex(int sex) {
                    this.sex = sex;
                }
            }
        }

        public static class CommentAndReplyListDTO {
            private String id;
            private String momentsId;
            private String memberId;
            private ThumbListDTO.FromMemberInfoDTO fromMemberInfo;
            private ThumbListDTO.FromMemberInfoDTO toMemberInfo;
            private int type;
            private String content;
            private String createdTime;
            private String createdBy;
            private String updatedTime;
            private String updatedBy;
            private int del;

            public ThumbListDTO.FromMemberInfoDTO getToMemberInfo() {
                return toMemberInfo;
            }

            public void setToMemberInfo(ThumbListDTO.FromMemberInfoDTO toMemberInfo) {
                this.toMemberInfo = toMemberInfo;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getMomentsId() {
                return momentsId;
            }

            public void setMomentsId(String momentsId) {
                this.momentsId = momentsId;
            }

            public String getMemberId() {
                return memberId;
            }

            public void setMemberId(String memberId) {
                this.memberId = memberId;
            }

            public ThumbListDTO.FromMemberInfoDTO getFromMemberInfo() {
                return fromMemberInfo;
            }

            public void setFromMemberInfo(ThumbListDTO.FromMemberInfoDTO fromMemberInfo) {
                this.fromMemberInfo = fromMemberInfo;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getCreatedTime() {
                return createdTime;
            }

            public void setCreatedTime(String createdTime) {
                this.createdTime = createdTime;
            }

            public String getCreatedBy() {
                return createdBy;
            }

            public void setCreatedBy(String createdBy) {
                this.createdBy = createdBy;
            }

            public String getUpdatedTime() {
                return updatedTime;
            }

            public void setUpdatedTime(String updatedTime) {
                this.updatedTime = updatedTime;
            }

            public String getUpdatedBy() {
                return updatedBy;
            }

            public void setUpdatedBy(String updatedBy) {
                this.updatedBy = updatedBy;
            }

            public int getDel() {
                return del;
            }

            public void setDel(int del) {
                this.del = del;
            }
        }
    }
}
