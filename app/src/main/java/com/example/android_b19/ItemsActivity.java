package com.example.android_b19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.prof.rssparser.Channel;

public class ItemsActivity extends AppCompatActivity {

    public final static String INTENT_URL = "url";

    private String url;

    private Toolbar toolbar;
    private ItemsViewModel mItemsViewModel;
    private ItemsAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvArticles;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        toolbar = findViewById(R.id.toolbar_items);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(R.string.loading_article_text);

        Intent intent = getIntent();
        url = intent.getStringExtra(INTENT_URL);

        mItemsViewModel = new ViewModelProvider(this).get(ItemsViewModel.class);

        rvArticles = findViewById(R.id.rvArticles);
        rvArticles.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        progressBar = findViewById(R.id.pbItems);
        swipeRefreshLayout = findViewById(R.id.slItems);

        mItemsViewModel.getChannel().observe(
                this,
                new Observer<Channel>() {
                    @Override
                    public void onChanged(Channel channel) {
                        if(channel != null){
                            if(channel.getTitle() != null){
                                setTitle(channel.getTitle());
                            }
                        }
                        mAdapter = new ItemsAdapter(getBaseContext(), channel.getArticles());
                        rvArticles.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

        swipeRefreshLayout.setColorSchemeResources(R.color.purple_500, R.color.purple_700);
        swipeRefreshLayout.canChildScrollUp();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.getArticleList().clear();
                mAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(true);
                mItemsViewModel.fetchItems(getApplicationContext(), url);
            }
        });

        if(isNetworkAvailable()){
            mItemsViewModel.fetchItems(getApplicationContext(), url);
        } else {
            Toast.makeText(getApplicationContext(), "Internet are not connected.", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if(connectivityManager != null){
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}