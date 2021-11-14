package com.example.android_b19.model;

import com.google.firebase.database.Exclude;

import java.util.UUID;

public class Feed {

    @Exclude
    private UUID id;

    private String name;
    private String url;
    private boolean isFav;

    public Feed(){
        id = null;
        name = "";
        url = "";
    }

    public Feed(UUID id, String name, String url, boolean isFav){
        this.id = id;
        this.name = name;
        this.url = url;
        this.isFav = isFav;
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
}
