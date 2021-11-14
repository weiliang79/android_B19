package com.example.android_b19.ui.feed;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android_b19.R;
import com.example.android_b19.model.Feed;
import com.example.android_b19.ui.feed.article.ItemsActivity;

import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.FeedViewHolder> {

    class FeedViewHolder extends RecyclerView.ViewHolder {

        final private TextView tvFeedTitle;
        final private TextView tvFeedUrl;
        final private ImageView ivFav;
        final private ImageView ivDelete;

        FeedViewHolder(View itemView){
            super(itemView);
            tvFeedTitle = itemView.findViewById(R.id.tvFeedTitle);
            tvFeedUrl = itemView.findViewById(R.id.tvFeedUrl);
            ivFav = itemView.findViewById(R.id.ivFavFeed);
            ivDelete = itemView.findViewById(R.id.ivDeleteFeed);
        }

    }

    private Context context;
    private LayoutInflater inflater;
    private ClickHandler clickHandler;
    private List<Feed> feedList;
    private boolean isHiddenManage = true;

    public FeedsAdapter(Context context, ClickHandler clickHandler){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.clickHandler = clickHandler;
    }

    public void setFeedList(List<Feed> feedList) {
        this.feedList = feedList;
        notifyDataSetChanged();
    }

    public void reverseHiddenManage(){
        this.isHiddenManage = !this.isHiddenManage;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_feed, parent, false);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.tvFeedTitle.setText(feedList.get(position).getName());

        holder.tvFeedUrl.setText(feedList.get(position).getUrl());

        if(feedList.get(position).isFav()){
            holder.ivFav.setColorFilter(context.getColor(R.color.yellow));
        } else {
            holder.ivFav.setColorFilter(context.getColor(android.R.color.tertiary_text_light));
        }

        if(isHiddenManage){
            holder.ivFav.setVisibility(View.INVISIBLE);
            holder.ivDelete.setVisibility(View.INVISIBLE);
            holder.itemView.setEnabled(true);
        } else {
            holder.ivFav.setVisibility(View.VISIBLE);
            holder.ivDelete.setVisibility(View.VISIBLE);
            holder.itemView.setEnabled(false);
        }

        holder.ivFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHandler.setFeedFav(feedList.get(position).getId(), !feedList.get(position).isFav());
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHandler.deleteFeed(feedList.get(position).getId());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ItemsActivity.class);
                intent.putExtra(ItemsActivity.INTENT_URL, feedList.get(position).getUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return feedList == null ? 0 : feedList.size();
    }

    interface ClickHandler{
        void setFeedFav(UUID feedId, boolean isFav);
        void deleteFeed(UUID feedId);
    }

}
