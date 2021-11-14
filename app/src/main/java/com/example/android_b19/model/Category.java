package com.example.android_b19.model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Category {

    @Exclude
    private UUID id;

    private String name;

    @Exclude
    private List<Feed> feedList;

    public Category(){
        id = null;
        name = "";
        feedList = new ArrayList<>();
    }

    public Category(UUID id, String name){
        this.id = id;
        this.name = name;
        feedList = new ArrayList<>();
    }

    public UUID getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Feed> getFeedList() {
        return feedList;
    }

    public void setId(UUID id){
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFeedList(List<Feed> feedList) {
        this.feedList = feedList;
    }

}
