package com.weiwu.ql.data.request;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/11/24 17:17 
 */
public class UpdateBgBody {
    private String background_img;

    public String getBackground_img() {
        return background_img;
    }

    public void setBackground_img(String background_img) {
        this.background_img = background_img;
    }

    public UpdateBgBody(String background_img) {
        this.background_img = background_img;
    }
}
