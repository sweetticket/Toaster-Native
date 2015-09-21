package com.github.florent37.materialviewpager.sample;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.Arrays;

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
    private String mAPostAuthorId;


    public PostItem(View view) {
        super(view);

        mPostBody = (TextView) view.findViewById(R.id.post_body);
        mPostDate = (TextView) view.findViewById(R.id.new_post_input);
        mPostNumComments = (TextView) view.findViewById(R.id.new_post_input);
        mPostNumVotes = (TextView) view.findViewById(R.id.num_votes);
        mUpvote = (ImageView) view.findViewById(R.id.upvote);
        mDownvote = (ImageView) view.findViewById(R.id.downvote);

    }

    public void bindPost(JSONObject post) {

        try {
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
            mPostBody.setText(body);
            mPostNumVotes.setText(numLikes);
//            mPostDate.setText(createdAt);
//            mPostNumComments.setText(); //TODO

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

        } catch (org.json.JSONException e) {
            Log.d("onBind", e.getMessage());
        }



    }


}
