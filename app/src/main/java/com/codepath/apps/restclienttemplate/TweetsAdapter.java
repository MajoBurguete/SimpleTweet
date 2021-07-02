package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TweetsAdapter extends  RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    Context context;
    List<Tweet> tweets;
    OnClickListener interactionClickListener;

    public interface OnClickListener{
        void onItemClicked(int position);
        void onFavClick(int position);
        void onRetweetClick(int position);
        void onReplyClick(int position);

    }

    // Pass in the context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets, OnClickListener clickListener) {
        this.context = context;
        this.tweets = tweets;
        this.interactionClickListener = clickListener;
    }

    // For each row, inflate the layout
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet,parent, false);
        return new ViewHolder(view);
    }

    // Bind values based on the position of the element

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        // Get the data at the position
        Tweet tweet = tweets.get(position);

        // Bind the tweet with the viewholder
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public void clear(){
        tweets.clear();
    }

    public void addAll(List<Tweet> list){
        tweets.addAll(list);
        notifyDataSetChanged();
    }


    // Define a view holder
    public class ViewHolder extends RecyclerView.ViewHolder{

        MaterialCardView cvTweet;
        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        TextView tvName;
        TextView tvRelative;
        ImageView ivTweetImage;
        ImageButton ibReply;
        ImageButton ibRetweet;
        ImageButton ibLike;
        TextView tvReply;
        TextView tvRetweetC;
        TextView tvLikeC;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvName = itemView.findViewById(R.id.tvName);
            tvRelative = itemView.findViewById(R.id.tvRelative);
            ivTweetImage = itemView.findViewById(R.id.ivTweetImage);
            cvTweet = itemView.findViewById(R.id.cvTweet);
            ibReply = itemView.findViewById(R.id.ibReply);
            ibRetweet = itemView.findViewById(R.id.ibRetweet);
            ibLike = itemView.findViewById(R.id.ibLike);
            tvReply = itemView.findViewById(R.id.tvReply);
            tvRetweetC = itemView.findViewById(R.id.tvRetweetC);
            tvLikeC = itemView.findViewById(R.id.tvLikeC);
        }

        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            tvScreenName.setText("@" + tweet.user.screenName);
            Glide.with(context).load(tweet.user.profileImageUrl).circleCrop().into(ivProfileImage);
            Glide.with(context).load(tweet.image).transform(new RoundedCorners(40)).into(ivTweetImage);
            tvName.setText(tweet.user.name);
            tvRetweetC.setText(tweet.retweetC);
            tvLikeC.setText(tweet.favoriteC);
            if (tweet.favorited){
                ibLike.setImageResource(R.drawable.ic_vector_heart);
            } else{
                ibLike.setImageResource(R.drawable.ic_vector_heart_stroke);
            }

            if (tweet.reply){
                tvReply.setVisibility(View.VISIBLE);
                tvReply.setText("On reply to @" + tweet.mention + ":");
            } else {
                tvReply.setVisibility(View.GONE);
            }
            String time = tweet.getRelativeTimeAgo(tweet.createdAt);
            tvRelative.setText(time);
            cvTweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    interactionClickListener.onItemClicked(getAdapterPosition());
                }
            });
            ibReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    interactionClickListener.onReplyClick(getAdapterPosition());
                }
            });
            ibRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    interactionClickListener.onRetweetClick(getAdapterPosition());
                }
            });
            ibLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    interactionClickListener.onFavClick(getAdapterPosition());
                }
            });


        }
    }
}
