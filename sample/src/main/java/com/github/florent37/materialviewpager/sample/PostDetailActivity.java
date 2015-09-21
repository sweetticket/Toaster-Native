package com.github.florent37.materialviewpager.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gc.materialdesign.views.Button;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jennykim on 9/20/15.
 */
public class PostDetailActivity extends AppCompatActivity {

    private static PostDetailActivity mInstance;
    Button mCommentBtn;
    EditText mCommentEditText;
    RecyclerView mRecyclerView;
    private CommentsRecyclerViewAdapter mAdapter;
    private List<Object> mCommentObjects = new ArrayList<Object>();
    private JSONObject mPostObject;
    private String mPostId;

    private TextView mPostBody;
    private TextView mPostDate;
    private TextView mPostNumComments;
    private TextView mPostNumVotes;
    private ImageView mUpvote;
    private ImageView mDownvote;

    public static synchronized PostDetailActivity getInstance() {
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_detail);
        mInstance = this;

        mPostBody = (TextView) findViewById(R.id.post_body);
        mPostDate = (TextView) findViewById(R.id.post_date);
        mUpvote = (ImageView) findViewById(R.id.upvote);
        mDownvote = (ImageView) findViewById(R.id.downvote);
        mPostNumVotes = (TextView) findViewById(R.id.num_votes);
        mPostNumComments = (TextView) findViewById(R.id.num_comments);

        String post_obj_str = getIntent().getStringExtra("postObject");

        if (post_obj_str != null) {
            Log.d("PostDetail", "postObject: " + post_obj_str);
            try {
                JSONObject mPostObject = new JSONObject(post_obj_str);
                mPostId = mPostObject.get("_id").toString();
                mPostBody.setText(mPostObject.get("body").toString());
                mPostNumVotes.setText(mPostObject.get("numLikes").toString());

            } catch (org.json.JSONException e) {
                Log.d("PostDetail", "onCreate JSONException: " + e.getMessage());
            }
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new CommentsRecyclerViewAdapter(mCommentObjects);
        mRecyclerView.setAdapter(mAdapter);
        populateComments();
        mAdapter.updateContents(mCommentObjects);
        Log.d("PostDetail", "onCreate: " + mCommentObjects.toString());

        MaterialViewPagerHelper.registerRecyclerView(this, mRecyclerView, null);

    }

    private void populateComments() {

            mCommentObjects.clear();

            Log.d("populateComments", "Just emptied mCommentObjects, length = " + mCommentObjects.size());

            String url = GlobalVariables.ROOT_URL + "/get-comments-for-post/" + mPostId;




    }

    private void sendCommentRequest() {
        // Tag used to cancel the request
        String tag_json_obj = "get_comments_req";

        Map<String, String> params = new HashMap<String, String>();

        String url = ""; //TODO


        CustomRequest jsonObjReq = new CustomRequest(Request.Method.GET, url, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("get_comments_req", "response =  " + response.toString());

                        try {

                            JSONArray commentsJson = response.getJSONArray("comments");

                            Log.d("get_comments_req", "comments JSONarray = " + commentsJson.toString());
                            Log.d("get_comments_req", "commentsjsonarray.length() = " + commentsJson.length());

                            for (int i = 0; i < commentsJson.length(); i++) {

                                try {
                                    mCommentObjects.add(commentsJson.getJSONObject(i));
                                }
                                catch (JSONException e) {
                                    Log.d("get_comments_req", e.getMessage());

                                }

                            };

//                            Log.d("get_posts_req", "mPostObjects = " + mPostObjects.toString());


                        } catch (org.json.JSONException e) {
                            Log.d("get_comments_req", e.getMessage());
                        }

                        Collections.reverse(mCommentObjects);
                        mAdapter.notifyDataSetChanged();


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("get_comments_req", "Error: " + error.getMessage());
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
