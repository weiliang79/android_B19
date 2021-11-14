package com.example.android_b19.ui.feed.article;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.android_b19.R;

public class WebViewActivity extends AppCompatActivity {

    public final static String INTENT_ARTICLE_URL = "article_url";

    private Toolbar toolbar;
    private WebView webView;
    private String articleUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        toolbar = findViewById(R.id.toolbar_web_view);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.loading_webview_text);

        Intent intent = getIntent();
        articleUrl = intent.getStringExtra(INTENT_ARTICLE_URL);

        webView = findViewById(R.id.web_view);
        webView.loadUrl(articleUrl);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                WebViewActivity.this.setTitle(view.getTitle());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }

    }
}