package com.example.android_b19;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.FeedViewHolder> {

    class FeedViewHolder extends RecyclerView.ViewHolder {

        final private TextView tvFeedTitle;

        FeedViewHolder(View itemView){
            super(itemView);
            tvFeedTitle = itemView.findViewById(R.id.tvFeedTitle);
        }

    }

    private Context context;
    private LayoutInflater inflater;
    private List<String> feedList;

    public FeedsAdapter(Context context, List<String> feedsList){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.feedList = feedsList;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_feed, parent, false);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvFeedTitle.setText(feedList.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FeedActivity.class);
                intent.putExtra(FeedActivity.INTENT_URL, feedList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

}
