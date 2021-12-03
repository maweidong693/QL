package com.weiwu.ql.data.request;

import java.util.List;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/17 15:46 
 */
public class IssueMessageRequestBody {

    private String textContent;
    private String mediaUrl;
    private String mediaPreview;
    private String pics;

    public IssueMessageRequestBody(String textContent, String mediaUrl, String mediaPreview) {
        this.textContent = textContent;
        this.mediaUrl = mediaUrl;
        this.mediaPreview = mediaPreview;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getMediaPreview() {
        return mediaPreview;
    }

    public void setMediaPreview(String mediaPreview) {
        this.mediaPreview = mediaPreview;
    }

    public IssueMessageRequestBody(String article) {
        this.textContent = article;
    }

    public IssueMessageRequestBody(String article, String imgUrl) {
        this.textContent = article;
        this.pics = imgUrl;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }
}
