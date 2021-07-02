package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.databinding.ActivityDetailBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class DetailActivity extends AppCompatActivity {

    private final int REQUEST_CODE_REPLY = 21;
    public static final int MAX_TWEET_LENGTH = 280;
    TwitterClient client;
    Tweet tweet;
    User superUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDetailBinding binding = ActivityDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Toolbar toolbar = findViewById(R.id.toolbarDet);
        setSupportActionBar(toolbar);
        client = TwitterApp.getRestClient(this);
        superUser = Parcels.unwrap(getIntent().getParcelableExtra("superUser"));
        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        binding.tiReply.setCounterMaxLength(MAX_TWEET_LENGTH);
        Glide.with(this).load(tweet.user.profileImageUrl).circleCrop().into(binding.ivProfPic);
        binding.tvUserN.setText(tweet.user.name);
        binding.tvUserS.setText("@"+tweet.user.screenName);
        binding.tvTweetB.setText(tweet.body);
        Glide.with(this).load(tweet.image).transform(new RoundedCorners(40)).into(binding.ivImageA);
        Glide.with(this).load(superUser.profileImageUrl).circleCrop().into(binding.ivReplyPP);
        binding.reply.setText("@"+tweet.user.screenName);

        binding.btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetCont = binding.reply.getText().toString();
                if (tweetCont.isEmpty()){
                    Toast.makeText(DetailActivity.this, "Sorry your tweet cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (tweetCont.length() > MAX_TWEET_LENGTH){
                    Toast.makeText(DetailActivity.this, "Sorry, your tweet is too long", Toast.LENGTH_LONG).show();
                    return;
                }
                client.postReply(tweetCont, tweet.tweetId, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i("DetailActivity", "onSuccess to reply tweet" + json.jsonObject);
                        try {
                            tweet = Tweet.fromJson(json.jsonObject);
                            Intent intent = new Intent();
                            // Set the result code and bundle data from response
                            intent.putExtra("tweet", Parcels.wrap(tweet));
                            // closes activity, pass data to parent
                            setResult(RESULT_OK, intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e("DetailActivity", "onFailure to reply tweet", throwable );
                    }
                });
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.btnBack){
            finish();
        }
        return true;
    }
}