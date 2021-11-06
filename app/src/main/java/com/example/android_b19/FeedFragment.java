package com.example.android_b19;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FeedFragment extends Fragment {

    private RecyclerView rvFeeds;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_feeds, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<String> feedsList = new ArrayList<>();
        feedsList.add("https://www.androidauthority.com/feed");
        feedsList.add("http://rss.cnn.com/rss/edition.rss");
        feedsList.add("https://www.thestar.com.my/rss/News");

        rvFeeds = view.findViewById(R.id.rvFeeds);
        rvFeeds.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvFeeds.setAdapter(new FeedsAdapter(getContext(), feedsList));
    }

}
