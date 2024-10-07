package com.alias.smartparty.entity;


public class Knowledge {
    private String title;
    private String content;
    private String picture;

    public Knowledge(String title, String content, String picture) {
        this.title = title;
        this.content = content;
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}


