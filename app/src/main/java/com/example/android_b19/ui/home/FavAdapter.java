package com.example.android_b19.ui.home;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_b19.R;
import com.example.android_b19.model.Feed;
import com.example.android_b19.ui.feed.article.ItemsActivity;
import com.prof.rssparser.Article;
import com.prof.rssparser.Channel;
import com.prof.rssparser.OnTaskCompleted;
import com.prof.rssparser.Parser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.FavHolder> {

    class FavHolder extends RecyclerView.ViewHolder {

        final private TextView tvTitle;
        final private TextView tvUrl;

        FavHolder(View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvFeedTitleFav);
            tvUrl = itemView.findViewById(R.id.tvFeedUrlFav);
        }

    }

    private Context context;
    private LayoutInflater inflater;
    private List<Feed> feedList;

    public FavAdapter(Context context){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setFeedList(List<Feed> feedList){
        this.feedList = feedList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_fav, parent, false);
        return new FavHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavHolder holder, int position) {
        holder.tvTitle.setText(feedList.get(position).getName());
        holder.tvUrl.setText(feedList.get(position).getUrl());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ItemsActivity.class);
                intent.putExtra(ItemsActivity.INTENT_URL, feedList.get(holder.getAdapterPosition()).getUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return feedList == null ? 0 : feedList.size();
    }

}
