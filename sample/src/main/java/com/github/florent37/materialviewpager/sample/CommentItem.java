package com.github.florent37.materialviewpager.sample;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ocpsoft.pretty.time.PrettyTime;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by jennykim on 9/20/15.
 */
public class CommentItem extends RecyclerView.ViewHolder {

    private TextView mCommentBody;
    private TextView mCommentDate;
    private TextView mCommentNumVotes;
    private TextView mCommentNameTag;
    private ImageView mUpvote;
    private ImageView mDownvote;

    private String mCommentAuthorId;
    private String mCommentId;
    private String mUpvoterIds;
    private String mDownvoterIds;
    private boolean mHasUpvoted;
    private boolean mHasDownvoted;

    public CommentItem(View view) {
        super(view);

        mCommentBody = (TextView) view.findViewById(R.id.comment_body);
        mCommentDate = (TextView) view.findViewById(R.id.comment_date);
        mCommentNumVotes = (TextView) view.findViewById(R.id.num_votes);
        mCommentNameTag = (TextView) view.findViewById(R.id.comment_name_tag);
        mUpvote = (ImageView) view.findViewById(R.id.upvote);
        mDownvote = (ImageView) view.findViewById(R.id.downvote);

    }

    public void bindComment(JSONObject comment) {

        Log.d("bindComment", "binding comments!");

        try {
            String body = comment.getString("body");
            String authorId = comment.getString("userId");
            String upvoterIds = comment.getString("upvoterIds");
            mHasUpvoted = upvoterIds.contains(GlobalVariables.mUserId);
            String downvoterIds = comment.getString("downvoterIds");
            mHasDownvoted = downvoterIds.contains(GlobalVariables.mUserId);
            String numLikes = comment.getString("numLikes");
            mCommentId = comment.getString("_id");

            String nameTag = comment.getString("nameTag");

            mCommentBody.setText(body);
            mCommentNumVotes.setText(numLikes);
            mCommentNameTag.setText("by " + nameTag);

            try {
                Date createdAt = ISO8601DateParser.parse(comment.getString("createdAt"));
                PrettyTime p = new PrettyTime();
                mCommentDate.setText(p.format(createdAt));

            } catch (java.text.ParseException e){
                Log.d("bindPost", "createdAt Error: " + e.getMessage());
            }

            if (mHasUpvoted) {
                mUpvote.setImageResource(R.mipmap.upvote_active);
            } else {
                mUpvote.setImageResource((R.mipmap.upvote));
            }
            if (mHasDownvoted) {
                mDownvote.setImageResource(R.mipmap.downvote_active);
            } else {
                mDownvote.setImageResource(R.mipmap.downvote);
            }

            mUpvote.setClickable(true);
            mUpvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer numLikes = Integer.parseInt(mCommentNumVotes.getText().toString());
                    if (mHasDownvoted) {
                        mDownvote.setImageResource(R.mipmap.downvote);
                        mUpvote.setImageResource(R.mipmap.upvote_active);
                        mCommentNumVotes.setText((numLikes + 2) + "");
                        mHasDownvoted = false;
                        mHasUpvoted = true;
                    } else if (mHasUpvoted) {
                        mUpvote.setImageResource(R.mipmap.upvote);
                        mCommentNumVotes.setText((numLikes - 1) + "");
                        mHasUpvoted = false;
                    } else {
                        mUpvote.setImageResource(R.mipmap.upvote_active);
                        mCommentNumVotes.setText((numLikes + 1) + "");
                        mHasUpvoted = true;
                    }
                    VotingHelpers.sendCommentUpvoteRequest(mCommentId);
                }
            });
            mCommentNumVotes.setClickable(true);
            mCommentNumVotes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer numLikes = Integer.parseInt(mCommentNumVotes.getText().toString());
                    if (mHasDownvoted) {
                        mDownvote.setImageResource(R.mipmap.downvote);
                        mUpvote.setImageResource(R.mipmap.upvote_active);
                        mCommentNumVotes.setText((numLikes + 2) + "");
                        mHasDownvoted = false;
                        mHasUpvoted = true;
                    } else if (mHasUpvoted) {
                        mUpvote.setImageResource(R.mipmap.upvote);
                        mCommentNumVotes.setText((numLikes - 1) + "");
                        mHasUpvoted = false;
                    } else {
                        mUpvote.setImageResource(R.mipmap.upvote_active);
                        mCommentNumVotes.setText((numLikes + 1) + "");
                        mHasUpvoted = true;
                    }
                    VotingHelpers.sendCommentUpvoteRequest(mCommentId);
                }
            });

            mDownvote.setClickable(true);
            mDownvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer numLikes = Integer.parseInt(mCommentNumVotes.getText().toString());
                    if (mHasUpvoted) {
                        mUpvote.setImageResource(R.mipmap.upvote);
                        mDownvote.setImageResource(R.mipmap.downvote_active);
                        mCommentNumVotes.setText((numLikes - 2) + "");
                        mHasUpvoted = false;
                        mHasDownvoted = true;
                    } else if (mHasDownvoted) {
                        mDownvote.setImageResource(R.mipmap.downvote);
                        mCommentNumVotes.setText((numLikes + 1) + "");
                        mHasDownvoted = false;
                    } else {
                        mDownvote.setImageResource(R.mipmap.downvote_active);
                        mCommentNumVotes.setText((numLikes - 1) + "");
                        mHasDownvoted = true;
                    }
                    VotingHelpers.sendCommentDownvoteRequest(mCommentId);
                }
            });

        } catch (org.json.JSONException e) {
            Log.d("bindComment", "Error: " + e.getMessage());
        }


    }


}
