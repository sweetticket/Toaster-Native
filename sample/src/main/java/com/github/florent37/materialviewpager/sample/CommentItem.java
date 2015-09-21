package com.github.florent37.materialviewpager.sample;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by jennykim on 9/20/15.
 */
public class CommentItem extends RecyclerView.ViewHolder {

    private TextView mCommentBody;
    private TextView mCommentDate;
    private TextView mCommentNumVotes;
    private ImageView mUpvote;
    private ImageView mDownvote;

    private String mCommentAuthorId;
    private String mCommentId;

    public CommentItem(View view) {
        super(view);

        mCommentBody = (TextView) view.findViewById(R.id.comment_body);
        mCommentDate = (TextView) view.findViewById(R.id.comment_date);
        mCommentNumVotes = (TextView) view.findViewById(R.id.num_votes);
        mUpvote = (ImageView) view.findViewById(R.id.upvote);
        mDownvote = (ImageView) view.findViewById(R.id.downvote);

    }

    public void bindComment(JSONObject comment) {

        Log.d("bindComment", "binding comments!");

        try {
            String body = comment.getString("body");
            Log.d("bindComment", "body: " + body);
            String authorId = comment.getString("userId");
            Log.d("bindComment", "author: " + authorId);
//            String[] upvoterIds = (String[]) post.get("upvoterIds");
//            Log.d("onBind", "upvotersIds:" + upvoterIds);
//            String[] downvoterIds = (String[]) post.get("downvoterIds");
//            Log.d("onBind", "downvoterIds:" + downvoterIds);
            String numLikes = comment.getString("numLikes");
            Log.d("bindComment", "numLikes:" + numLikes);
//            String createdAt = post.getString("createdAt");
            mCommentId = comment.getString("_id");
            Log.d("bindComment", "commentId: " + mCommentId);

            mCommentBody.setText(body);
            mCommentNumVotes.setText(numLikes);
//            mCommentDate.setText(createdAt);

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
            Log.d("bindComment", "Error: " + e.getMessage());
        }


    }


}
