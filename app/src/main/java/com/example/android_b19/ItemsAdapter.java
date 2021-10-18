package com.example.android_b19;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.prof.rssparser.Article;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder> {

    class ItemsViewHolder extends RecyclerView.ViewHolder {

        final TextView title;
        final TextView pubDate;
        final ImageView image;

        ItemsViewHolder(View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.tvArticleTitle);
            pubDate = itemView.findViewById(R.id.tvArticlePubDate);
            image = itemView.findViewById(R.id.ivArticleImage);
        }

    }

    private Context context;
    private LayoutInflater inflater;
    private List<Article> mArticleList;

    public ItemsAdapter(Context context, List<Article> articleList){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.mArticleList = articleList;
    }

    public List<Article> getArticleList(){
        return mArticleList;
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_article, parent, false);
        return new ItemsViewHolder(view);
    }

    @SuppressLint("SetJavascriptEnabled")
    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {

        Article currentArticle = mArticleList.get(position);
        String pubDateString = "";

        try{
            String sourceString = currentArticle.getPubDate();

            if(sourceString != null){
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
                Date date = dateFormat.parse(sourceString);
                if(date != null){
                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                    pubDateString = dateFormat1.format(date);
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.title.setText(Html.fromHtml(currentArticle.getTitle()).toString());

        holder.pubDate.setText(pubDateString);

        Glide.with(context)
                .load(currentArticle.getImage())
                .into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(WebViewActivity.INTENT_ARTICLE_URL, currentArticle.getLink());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mArticleList.size();
    }

}
