package com.github.florent37.materialviewpager.sample;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by jennykim on 9/20/15.
 */
public class PostItem extends RecyclerView.ViewHolder {

    private TextView mPostBody;
    private TextView mPostDate;
    private TextView mPostNumComments;
    private TextView mPostNumVotes;
    private ImageView mUpvote;
    private ImageView mDownvote;
    private LinearLayout mToPostDetailClickable;

    private String mPostAuthorId;
    private String mPostId;

    private JSONObject mPostObject;

    public PostItem(View view) {
        super(view);

        mPostBody = (TextView) view.findViewById(R.id.post_body);
        mPostDate = (TextView) view.findViewById(R.id.post_date);
        mPostNumComments = (TextView) view.findViewById(R.id.num_comments);
        mPostNumVotes = (TextView) view.findViewById(R.id.num_votes);
        mUpvote = (ImageView) view.findViewById(R.id.upvote);
        mDownvote = (ImageView) view.findViewById(R.id.downvote);
        mToPostDetailClickable = (LinearLayout) view.findViewById(R.id.to_post_detail);

        Log.d("onClick", "has click listeners = " + mPostBody.hasOnClickListeners());

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("onClick", "I've been clicked!");
                switch(view.getId())
                {
                    case R.id.upvote:
                    {
                        //TODO
                        break;
                    }
                    case R.id.downvote:
                    {
                        //TODO
                        break;
                    }
                    default:
                    {
                        Intent toDetailIntent = new Intent(MainActivity.getInstance(), PostDetailActivity.class);
                        toDetailIntent.putExtra("postObject", mPostObject.toString());
                        MainActivity.getInstance().startActivity(toDetailIntent);
                        break;
                    }

                }
            }
        };

        view.setOnClickListener(onClickListener);
        mToPostDetailClickable.setClickable(true);
        mToPostDetailClickable.setOnClickListener(onClickListener);
        mPostBody.setClickable(true);
        mPostBody.setOnClickListener(onClickListener);

        Log.d("onClick", "has click listeners (after assign) = " + mPostBody.hasOnClickListeners());

    }

    public void bindPost(JSONObject post, Map<String, Integer> commentsCountMap) {

        try {
            mPostObject = post;
            String body = post.getString("body");
            Log.d("onBind", "body: " + body);
            String authorId = post.getString("userId");
            Log.d("onBind", "author: " + authorId);
//            String[] upvoterIds = (String[]) post.get("upvoterIds");
//            Log.d("onBind", "upvotersIds:" + upvoterIds);
//            String[] downvoterIds = (String[]) post.get("downvoterIds");
//            Log.d("onBind", "downvoterIds:" + downvoterIds);
            String numLikes = post.getString("numLikes");
            Log.d("onBind", "numLikes:" + numLikes);
//            String createdAt = post.getString("createdAt");
            mPostId = post.getString("_id");
            Log.d("onBind", "postId: " + mPostId);

            mPostBody.setText(body);
            mPostNumVotes.setText(numLikes);
//            mPostDate.setText(createdAt);

//            if (Arrays.asList(upvoterIds).contains(GlobalVariables.mUserId)) {
//                mUpvote.setImageResource(R.mipmap.upvote_active);
//
//            } else {
//                mUpvote.setImageResource(R.mipmap.upvote);
//            }
//
//            if (Arrays.asList(downvoterIds).contains(GlobalVariables.mUserId)){
//                mDownvote.setImageResource(R.mipmap.downvote_active);
//            } else {
//                mDownvote.setImageResource(R.mipmap.downvote);
//            }
            setNumComments(commentsCountMap);
//
//            mToPostDetailClickable.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent toDetailIntent = new Intent(MainActivity.getInstance(), PostDetailActivity.class);
//                    toDetailIntent.putExtra("postObject", mPostObject.toString());
//                }
//            });
//
//            mToPostDetailClickable.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//                    Intent toDetailIntent = new Intent(MainActivity.getInstance(), PostDetailActivity.class);
//                    toDetailIntent.putExtra("postObject", mPostObject.toString());
//                    return false;
//                }
//            });

        } catch (org.json.JSONException e) {
            Log.d("onBind", e.getMessage());
        }


    }

    public void setNumComments(Map<String, Integer> commentsCountMap) {

        Log.d("setNumComments", "commentsCountMap: " + commentsCountMap.toString());

        if (commentsCountMap.containsKey(mPostId)) {
            Integer numComments = commentsCountMap.get(mPostId);

            if (numComments == 1) {
                mPostNumComments.setText("1 Comment");
            } else {
                mPostNumComments.setText(numComments.toString() + " Comments");
            }
        } else {
            mPostNumComments.setText("0 Comments");
        }

    }


}
