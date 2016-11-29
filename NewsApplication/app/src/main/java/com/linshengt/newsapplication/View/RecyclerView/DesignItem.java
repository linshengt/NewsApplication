package com.linshengt.newsapplication.View.RecyclerView;

/**
 * Created by sunwei on 2015/11/4.
 * Email: lx_sunwei@163.com.
 * Description:
 */
public class DesignItem {

    public String author;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String title;
    public String imageUrl;

    public DesignItem() {}

    public DesignItem(String author, String title, String imageUrl) {
        this.author = author;
        this.title = title;
        this.imageUrl = imageUrl;
    }
}
