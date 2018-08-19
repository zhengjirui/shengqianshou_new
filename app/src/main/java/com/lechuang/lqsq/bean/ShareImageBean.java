package com.lechuang.lqsq.bean;

import java.io.Serializable;

/**
 * Author: guoning
 * Date: 2017/10/7
 * Description:
 */

public class ShareImageBean implements Serializable {

    private String url;
    private boolean mSelected;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean ismSelected() {
        return mSelected;
    }

    public void setmSelected(boolean mSelected) {
        this.mSelected = mSelected;
    }
}
