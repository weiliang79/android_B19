package com.example.android_b19.model;

import com.google.firebase.database.Exclude;
import com.prof.rssparser.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Feed {

    @Exclude
    private UUID id;

    private String name;
    private String url;
    private boolean isFav;

    @Exclude
    private List<Article> articleList;

    public Feed(){
        id = null;
        name = "";
        url = "";
        articleList = new ArrayList<>();
    }

    public Feed(UUID id, String name, String url){
        this.id = id;
        this.name = name;
        this.url = url;
        this.isFav = true;
        articleList = new ArrayList<>();
    }

    public Feed(UUID id, String name, String url, boolean isFav){
        this.id = id;
        this.name = name;
        this.url = url;
        this.isFav = isFav;
        articleList = new ArrayList<>();
    }

    public Feed(UUID id, String name, String url, List<Article> articleList){
        this.id = id;
        this.name = name;
        this.url = url;
        this.isFav = true;
        this.articleList = articleList;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl(){
        return url;
    }

    public boolean isFav(){
        return isFav;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }
}
