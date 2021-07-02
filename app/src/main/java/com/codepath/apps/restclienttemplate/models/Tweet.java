package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Parcel
public class Tweet {

    public String body;
    public long tweetId;
    public String createdAt;
    public User user;
    public String image;
    public String mention;
    public boolean favorited;
    public boolean retweeted;
    public boolean reply;
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    // empty constructor needed by the Parceler library
    public Tweet() {}

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        try {
            long time = sf.parse(rawJsonDate).getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " d";
            }
        } catch (ParseException e) {
            Log.i("ParseException", "getRelativeTimeAgo failed");
            e.printStackTrace();
        }

        return "";
    }

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        JSONObject entities = jsonObject.getJSONObject("entities");

        if(jsonObject.has("full_text")){
            tweet.body = jsonObject.getString("full_text");
        }else{
            tweet.body = jsonObject.getString("text");
        }

        if (jsonObject.has("in_reply_to_status_id_str") && jsonObject.getString("in_reply_to_status_id_str") != null){
            if (entities.has("user_mentions") && entities.getJSONArray("user_mentions").length() > 0){
                tweet.reply = true;
                tweet.mention = entities.getJSONArray("user_mentions").getJSONObject(0).getString("screen_name");
            } else{
                tweet.mention = "";
            }
        } else {
            tweet.reply = false;
        }
        tweet.tweetId = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));

        if (entities.has("media")) {
            tweet.image = entities.getJSONArray("media").getJSONObject(0).getString("media_url_https");
        } else {
            tweet.image = "";
        }

        tweet.favorited = jsonObject.getBoolean("favorited");
        tweet.retweeted = jsonObject.getBoolean("retweeted");
        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i<jsonArray.length(); i++){
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;

    }

}
