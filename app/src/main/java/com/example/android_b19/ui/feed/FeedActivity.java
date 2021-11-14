package com.example.android_b19.ui.feed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_b19.R;
import com.example.android_b19.model.Feed;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class FeedActivity extends AppCompatActivity implements FeedsAdapter.ClickHandler {

    public static final String CATEGORY_ID_KEY = "category_id";

    private FirebaseAuth auth;
    private DatabaseReference reference;

    private TextView tvEmpty;
    private RecyclerView rvFeeds;
    private FeedsAdapter feedsAdapter;

    private UUID categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        Intent intent = getIntent();
        categoryId = UUID.fromString(intent.getStringExtra(CATEGORY_ID_KEY));

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Feeds").child(auth.getUid()).child("Category").child(categoryId.toString()).child("Feed");


        reference.getParent().child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    setTitle(task.getResult().getValue(String.class));
                } else {
                    Toast.makeText(FeedActivity.this, "Unable to fetch category title", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvEmpty = findViewById(R.id.tv_feed_empty);
        rvFeeds = findViewById(R.id.rvFeeds);
        rvFeeds.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        feedsAdapter = new FeedsAdapter(this, this);
        rvFeeds.setAdapter(feedsAdapter);
        loadData();
    }

    private void loadData(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() == 0){
                    tvEmpty.setVisibility(View.VISIBLE);
                    List<Feed> feedList = new ArrayList<>();
                    feedsAdapter.setFeedList(feedList);
                } else {
                    tvEmpty.setVisibility(View.GONE);
                    List<Feed> feedList = new ArrayList<>();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        feedList.add(new Feed(UUID.fromString(dataSnapshot.getKey()), dataSnapshot.child("name").getValue(String.class), dataSnapshot.child("url").getValue(String.class), dataSnapshot.child("isFav").getValue(Boolean.class) != null && dataSnapshot.child("isFav").getValue(Boolean.class)));
                    }
                    feedsAdapter.setFeedList(feedList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FeedActivity.this, "Cancel Loading data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        } else if(item.getItemId() == R.id.menu_add_feed){

            AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_feed, null);

            EditText etLink = dialogView.findViewById(R.id.et_feed_link);
            TextView tvCancel = dialogView.findViewById(R.id.btn_cancel_add_feed);
            TextView tvSave = dialogView.findViewById(R.id.btn_save_add_feed);

            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.dismiss();
                }
            });

            tvSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String urlTitle = "";
                    ValidateRssUrl validateRssUrl = new ValidateRssUrl(etLink.getText().toString());
                    Thread thread = new Thread(validateRssUrl);
                    thread.start();

                    try {
                        thread.join();
                        boolean isRssUrl = validateRssUrl.getIsRssUrl();
                        urlTitle = validateRssUrl.getUrlTitle();
                        if(!isRssUrl){
                            Toast.makeText(FeedActivity.this, R.string.new_feed_error, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (Exception e) {
                        Toast.makeText(FeedActivity.this, R.string.new_feed_error, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    reference.child(UUID.randomUUID().toString()).setValue(new Feed(null, urlTitle, etLink.getText().toString(), false));

                    dialogBuilder.dismiss();
                }
            });

            dialogBuilder.setView(dialogView);
            dialogBuilder.show();

            return true;
        } else if(item.getItemId() == R.id.menu_show_manage_feed){
            feedsAdapter.reverseHiddenManage();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    class ValidateRssUrl implements Runnable {

        private String url;
        private boolean isRssLink = false;
        private String urlTitle = "";

        ValidateRssUrl(String url){
            this.url = url;
        }

        @Override
        public void run() {
            try {
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document document = builder.parse(url);
                if(document.getDocumentElement().getNodeName().equalsIgnoreCase("rss")){
                    isRssLink = true;
                } else {
                    isRssLink = false;
                }

                Node titleNode = document.getElementsByTagName("title").item(0);
                Log.e("xml title", "" + titleNode.getTextContent());
                urlTitle = titleNode.getTextContent();
            } catch (NullPointerException e) {
                urlTitle = "";
            } catch (Exception e) {
                isRssLink = false;
            }
        }

        public boolean getIsRssUrl(){
            return isRssLink;
        }

        public String getUrlTitle(){
            return urlTitle;
        }
    }

    @Override
    public void setFeedFav(UUID feedId, boolean isFav){
        reference.child(feedId.toString()).child("isFav").setValue(isFav);
    }

    @Override
    public void deleteFeed(UUID feedId){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder
                .setTitle(getResources().getString(R.string.delete_warning_title))
                .setMessage(getResources().getString(R.string.delete_warning_msg))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reference.child(feedId.toString()).removeValue();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = dialogBuilder.create();

        dialog.show();
    }
}