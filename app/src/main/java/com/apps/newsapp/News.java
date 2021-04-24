package com.apps.newsapp;

public class News {
    private String title;
    private String author;
    private String url;
    private String desc;
    private String imageUrl;

    public News(String title, String author, String url, String desc, String imageUrl) {
        this.title = title;
        this.author = author;
        this.url = url;
        this.desc = desc;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDesc() {
        return desc;
    }
}
