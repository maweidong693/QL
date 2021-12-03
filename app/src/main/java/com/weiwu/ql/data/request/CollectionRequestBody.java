package com.weiwu.ql.data.request;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/11/24 10:20 
 */
public class CollectionRequestBody {
    private String nick;
    private String content;
    private String file_url;
    private String thumb_image;
    private String type;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CollectionRequestBody(String nick, String content, String file_url, String thumb_image, String type) {
        this.nick = nick;
        this.content = content;
        this.file_url = file_url;
        this.thumb_image = thumb_image;
        this.type = type;
    }
}
