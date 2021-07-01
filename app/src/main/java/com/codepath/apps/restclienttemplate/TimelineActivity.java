package com.codepath.apps.restclienttemplate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity implements TweetsAdapter.OnClickListener{

    private final int REQUEST_CODE = 20;

    public static final String TAG = "TimelineActivity";
    TwitterClient client;
    RecyclerView rvTweets;
    List<Tweet> tweets;
    TweetsAdapter adapter;
    SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        swipeContainer = findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateHomeTimeline();
                swipeContainer.setRefreshing(false);
            }
        });

        client = TwitterApp.getRestClient(this);
        // Find the recycler view
        rvTweets = findViewById(R.id.rvTweets);
        // Initialize the list of tweets and adpater
        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(this, tweets, this);
        //Recycler view setup : layout manager and adapter
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(adapter);
        populateHomeTimeline();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.logout){
            client.clearAccessToken();
            finish();
        } else if (item.getItemId() == R.id.New){
            // Compose icon has been selected
            Intent intentC = new Intent(this, ComposeActivity.class);

            startActivityForResult(intentC, REQUEST_CODE);
            // Navigate to the compose activity
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            // Get data from the intent (tweet)
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
            // Update the recycler view with this tweet
            // Modify data source of tweets
            tweets.add(0, tweet);
            // Notify the adapter
            adapter.notifyItemInserted(0);
            rvTweets.smoothScrollToPosition(0);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void populateHomeTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess: OnSuccess!" +json.toString());
                JSONArray jsonArray = json.jsonArray;
                adapter.clear();
                try {
                    adapter.addAll(Tweet.fromJsonArray(jsonArray));
                    swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    Log.e(TAG, "JSON Exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i(TAG, "onFailure: OnFailure!" + response, throwable);
            }
        });
    }

    @Override
    public void onItemClicked(int position) {
        Log.i(TAG, "onItemClicked: yesss");
        Intent t = new Intent(TimelineActivity.this, DetailActivity.class);
        t.putExtra("tweet", Parcels.wrap( tweets.get(position)));
        startActivity(t);
    }

    @Override
    public void onFavClick(int position) {

    }

    @Override
    public void onRetweetClick(int position) {

    }

    @Override
    public void onReplyClick(int position) {

    }
}