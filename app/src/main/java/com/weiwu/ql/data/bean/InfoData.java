package com.weiwu.ql.data.bean;

import java.io.Serializable;

/**
 *
 */
public class InfoData implements Serializable {

    String   title ;
    int    type ;

    public InfoData(String title, int type) {
        this.title = title;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
