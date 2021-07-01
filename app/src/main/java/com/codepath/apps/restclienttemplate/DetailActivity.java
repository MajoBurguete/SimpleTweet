package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.databinding.ActivityDetailBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 21;
    TwitterClient client;
    Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDetailBinding binding = ActivityDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Toolbar toolbar = findViewById(R.id.toolbarDet);
        setSupportActionBar(toolbar);
        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        Glide.with(this).load(tweet.user.profileImageUrl).circleCrop().into(binding.ivProfPic);
        binding.tvUserN.setText(tweet.user.name);
        binding.tvUserS.setText("@"+tweet.user.screenName);
        binding.tvTweetB.setText(tweet.body);
        Glide.with(this).load(tweet.image).transform(new RoundedCorners(40)).into(binding.ivImageA);

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