package com.example.android_b19.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_b19.R;
import com.example.android_b19.model.Feed;
import com.example.android_b19.ui.feed.FeedActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prof.rssparser.Channel;
import com.prof.rssparser.OnTaskCompleted;
import com.prof.rssparser.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FavFragment extends Fragment {

    private FirebaseAuth auth;
    private DatabaseReference reference;

    private TextView tvEmpty;
    private RecyclerView rvFav;
    private FavAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Favorites").child(Objects.requireNonNull(auth.getUid()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_fav, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvEmpty = view.findViewById(R.id.tvFavEmpty);
        rvFav = view.findViewById(R.id.rvFav);
        rvFav.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new FavAdapter(getContext());
        rvFav.setAdapter(adapter);
        loadData();
    }

    private void loadData(){

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() == 0){
                    tvEmpty.setVisibility(View.VISIBLE);
                    List<Feed> feedList = new ArrayList<>();
                    adapter.setFeedList(feedList);
                } else {
                    tvEmpty.setVisibility(View.INVISIBLE);
                    List<Feed> feedList = new ArrayList<>();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        feedList.add(new Feed(UUID.fromString(dataSnapshot.getKey()), dataSnapshot.child("name").getValue(String.class), dataSnapshot.child("url").getValue(String.class)));
                    }

                    adapter.setFeedList(feedList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Cancel Loading data", Toast.LENGTH_SHORT).show();
            }
        });
    }

}