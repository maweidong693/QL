package com.weiwu.ql.data.request;

import java.util.List;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/8/17 15:46 
 */
public class IssueMessageRequestBody {

    private String article;
    private List<String> imgUrl;

    public IssueMessageRequestBody(String article) {
        this.article = article;
    }

    public IssueMessageRequestBody(String article, List<String> imgUrl) {
        this.article = article;
        this.imgUrl = imgUrl;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public List<String> getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(List<String> imgUrl) {
        this.imgUrl = imgUrl;
    }
}
