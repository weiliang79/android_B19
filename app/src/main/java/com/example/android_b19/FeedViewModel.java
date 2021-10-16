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

public class FeedViewModel extends ViewModel {

    private Context context;
    private MutableLiveData<Channel> articleListLive = null;

    public MutableLiveData<Channel> getChannel(){
        if(articleListLive == null){
            articleListLive = new MutableLiveData<>();
        }
        return articleListLive;
    }

    private void setChannel(Channel channel) {
        this.articleListLive.postValue(channel);
    }

    public void fetchFeed(Context context, String url){

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

}
