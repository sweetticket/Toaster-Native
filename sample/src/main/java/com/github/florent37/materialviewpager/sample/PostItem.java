package com.github.florent37.materialviewpager.sample;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ocpsoft.pretty.time.PrettyTime;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jennykim on 9/20/15.
 */
public class PostItem extends RecyclerView.ViewHolder {

    private int mPosition;
    private TextView mPostBody;
    private TextView mPostDate;
    private TextView mPostNumComments;
    private TextView mPostNumVotes;
    private ImageView mUpvote;
    private ImageView mDownvote;
    private String mPostAuthorId;
    private String mPostId;

    private boolean mHasUpvoted;
    private boolean mHasDownvoted;
    private int numLikes;

    private JSONObject mPostObject;

    public PostItem(View view) {
        super(view);

        mPostBody = (TextView) view.findViewById(R.id.post_body);
        mPostDate = (TextView) view.findViewById(R.id.post_date);
        mPostNumComments = (TextView) view.findViewById(R.id.num_comments);
        mPostNumVotes = (TextView) view.findViewById(R.id.num_votes);
        mUpvote = (ImageView) view.findViewById(R.id.upvote);
        mDownvote = (ImageView) view.findViewById(R.id.downvote);

        View.OnClickListener toPostDetailListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toPostDetail();
            }
        };
        mPostBody.setClickable(true);
        mPostBody.setOnClickListener(toPostDetailListener);
        view.setClickable(true);
        view.setOnClickListener(toPostDetailListener);
        mUpvote.setClickable(true);
        mUpvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mHasDownvoted) {
                    numLikes += 2;
                    mHasDownvoted = false;
                    mHasUpvoted = true;
                } else if (mHasUpvoted) {
                    numLikes -= 1;
                    mHasUpvoted = false;
                } else {
                    numLikes += 1;
                    mHasUpvoted = true;
                }
                MainActivity.getInstance().updateVotes(mPostId, mHasUpvoted, mHasDownvoted, numLikes);
                MyHistoryActivity.getInstance().updateVotes(mPostId, mHasUpvoted, mHasDownvoted, numLikes);
                VotingHelpers.sendPostUpvoteRequest(mPostId);
            }
        });
        mPostNumVotes.setClickable(true);
        mPostNumVotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mHasDownvoted) {

                    numLikes += 2;
                    mHasDownvoted = false;
                    mHasUpvoted = true;
                } else if (mHasUpvoted) {
                    numLikes -= 1;
                    mHasUpvoted = false;
                } else {
                    numLikes += 1;
                    mHasUpvoted = true;
                }
                MainActivity.getInstance().updateVotes(mPostId, mHasUpvoted, mHasDownvoted, numLikes);
                MyHistoryActivity.getInstance().updateVotes(mPostId, mHasUpvoted, mHasDownvoted, numLikes);
                VotingHelpers.sendPostUpvoteRequest(mPostId);
            }
        });

        mDownvote.setClickable(true);
        mDownvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mHasUpvoted) {
                    numLikes -= 2;
                    mHasUpvoted = false;
                    mHasDownvoted = true;
                } else if (mHasDownvoted) {
                    numLikes += 1;
                    mHasDownvoted = false;
                } else {
                    numLikes -= 1;
                    mHasDownvoted = true;
                }
                MainActivity.getInstance().updateVotes(mPostId, mHasUpvoted, mHasDownvoted, numLikes);
                MyHistoryActivity.getInstance().updateVotes(mPostId, mHasUpvoted, mHasDownvoted, numLikes);
                VotingHelpers.sendPostDownvoteRequest(mPostId);
            }
        });

    }

    public void updatePost(boolean hasUpvoted, boolean hasDownvoted, int newNumLikes) {
        mPostNumVotes.setText(newNumLikes + "");
        mHasUpvoted = hasUpvoted;
        mHasDownvoted = hasDownvoted;
        if (mHasUpvoted) {
            mUpvote.setImageResource(R.mipmap.upvote_active);
        } else {
            mUpvote.setImageResource(R.mipmap.upvote);
        }
        if (mHasDownvoted) {
            mDownvote.setImageResource(R.mipmap.downvote_active);
        } else {
            mDownvote.setImageResource(R.mipmap.downvote);
        }
        numLikes = newNumLikes;
    }

    public void updatePost(boolean hasUpvoted, boolean hasDownvoted, int newNumLikes, int numComments) {
        this.updatePost(hasUpvoted, hasDownvoted, newNumLikes);
        setNumComments(numComments);
    }



    public void bindPost(JSONObject post, Map<String, Integer> commentsCountMap) {

        try {
            mPostObject = post;
            String body = post.getString("body");
            String authorId = post.getString("userId");
            String upvoterIds = post.getString("upvoterIds");
            mHasUpvoted = upvoterIds.contains(GlobalVariables.mUserId);
//            Log.d("onBind", "upvotersIds:" + upvoterIds);
            String downvoterIds = post.getString("downvoterIds");
            mHasDownvoted = downvoterIds.contains(GlobalVariables.mUserId);
//            Log.d("onBind", "downvoterIds:" + downvoterIds);
            numLikes = post.getInt("numLikes");
            try {
                Date createdAt = ISO8601DateParser.parse(post.getString("createdAt"));
                PrettyTime p = new PrettyTime();
                mPostDate.setText(p.format(createdAt));

            } catch (java.text.ParseException e) {
                Log.d("bindPost", "createdAt Error: " + e.getMessage());
            }
            mPostId = post.getString("_id");

            mPostBody.setText(body);
            mPostNumVotes.setText(numLikes + "");

            if (mHasUpvoted) {
                mUpvote.setImageResource(R.mipmap.upvote_active);
            } else {
                mUpvote.setImageResource(R.mipmap.upvote);
            }
            if (mHasDownvoted) {
                mDownvote.setImageResource(R.mipmap.downvote_active);
            } else {
                mDownvote.setImageResource(R.mipmap.downvote);
            }

            setNumComments(commentsCountMap);

        } catch (org.json.JSONException e) {
            Log.d("bindPost", e.getMessage());
        }


    }

    public void setNumComments(int numComments) {
        if (numComments == 1) {
            mPostNumComments.setText("1 Comment");
        } else {
            mPostNumComments.setText(numComments + " Comments");
        }
    }

    public void setNumComments(Map<String, Integer> commentsCountMap) {

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

    private void toPostDetail() {

//        MainActivity.getInstance().setClickedPost(this);
        // Tag used to cancel the request
        String tag_json_obj = "get_post_detail";

        Map<String, String> params = new HashMap<String, String>();

        String url = GlobalVariables.ROOT_URL + "/get-post/" + mPostId;

        CustomRequest jsonObjReq = new CustomRequest(Request.Method.GET, url, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
//                        Log.d("get_post_detail", "response =  " + response.toString());

                        try {
                            JSONObject postDetailObj = response.getJSONArray("posts").getJSONObject(0);
                            Intent toDetailIntent = new Intent(MainActivity.getInstance(), PostDetailActivity.class);
                            toDetailIntent.putExtra("postObject", postDetailObj.toString());
                            MainActivity.getInstance().startActivity(toDetailIntent);
//                            MainActivity.getInstance().finish();
                        } catch (org.json.JSONException e) {
                            Log.d("get_post_detail", e.getMessage());
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("get_post_detail", "Error: " + error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + GlobalVariables.mToken);
                return headers;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


}
