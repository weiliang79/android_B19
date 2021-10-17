package com.example.android_b19;

import android.content.Context;
import android.widget.Toast;

import com.prof.rssparser.Article;
import com.prof.rssparser.Channel;
import com.prof.rssparser.OnTaskCompleted;
import com.prof.rssparser.Parser;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ItemsViewModel extends ViewModel {

    private MutableLiveData<Channel> mArticleListLive = null;

    public MutableLiveData<Channel> getChannel(){
        if(mArticleListLive == null){
            mArticleListLive = new MutableLiveData<>();
        }
        return mArticleListLive;
    }

    public void fetchItems(Context context, String url){

        Parser parser = new Parser.Builder()
                .build();

        parser.onFinish(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(@NonNull Channel channel) {
                setChannel(channel);
            }

            @Override
            public void onError(@NonNull Exception e) {
                setChannel(new Channel(null, null, null, null, null, null, new ArrayList<Article>()));
                e.printStackTrace();
                Toast.makeText(context, "Error has occurred, please try again", Toast.LENGTH_SHORT).show();
            }
        });

        parser.execute(url);

    }

    private void setChannel(Channel channel) {
        this.mArticleListLive.postValue(channel);
    }

}
