package com.weiwu.ql.main.mine.collection;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

public class FavoritesData extends BaseBean implements Serializable {
    private int code;
    private String msg;
    private List<FavoritesItem> data;

    public List<FavoritesItem> getData() {
        return data;
    }

    public void setData(List<FavoritesItem> data) {
        this.data = data;
    }

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

    public static class FavoritesItem extends BaseBean implements MultiItemEntity {

        public final static int TYPE_TEXT = 1;
        public final static int TYPE_IMG = 2;
        public final static int TYPE_SOUND = 3;
        public final static int TYPE_VIDEO = 4;

        private String id;
        private String type;
        private String content;
        private String create_time;
        private String file_url;
        private String nick;
        private String user_id;
        private String thumb_image;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getFile_url() {
            return file_url;
        }

        public void setFile_url(String file_url) {
            this.file_url = file_url;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getThumb_image() {
            return thumb_image;
        }

        public void setThumb_image(String thumb_image) {
            this.thumb_image = thumb_image;
        }

        @Override
        public int getItemType() {
            return Integer.parseInt(type);
        }
    }

}
