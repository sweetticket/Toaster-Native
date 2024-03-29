package com.github.florent37.materialviewpager.sample;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
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
public class NotificationsActivity extends AppCompatActivity {

    private static NotificationsActivity mInstance;
    RecyclerView mRecyclerView;
    private NotiRecyclerViewAdapter mAdapter;
    private List<Object> mNotiObjects = new ArrayList<Object>();
    private Toolbar mToolbar;
    private ProgressBarCircularIndeterminate mWheel;
    static boolean alive = false;

    @Override
    public void onStart() {
        super.onStart();
        alive = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        alive = false;
    }

    public static synchronized NotificationsActivity getInstance() {
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);
        mInstance = this;

        mWheel = (ProgressBarCircularIndeterminate) findViewById(R.id.wheel);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("NOTIFICATIONS");
        mToolbar.setNavigationIcon(R.mipmap.back);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            int statusbar_color = getResources().getColor(R.color.YellowBrown);
            window.setStatusBarColor(statusbar_color);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new NotiRecyclerViewAdapter(mNotiObjects);
        mRecyclerView.setAdapter(mAdapter);
        populateNoti();
        Log.d("Notifications", "mCommentObjects = " + mNotiObjects.toString());

        MaterialViewPagerHelper.registerRecyclerView(this, mRecyclerView, null);

    }

    @Override
    public void onBackPressed() {

        sendReadNotiRequest();

        if (getIntent().hasExtra("postId")) {

            String postId = getIntent().getStringExtra("postId");
            boolean hasUpvoted = getIntent().getBooleanExtra("hasUpvoted", true);
            boolean hasDownvoted = getIntent().getBooleanExtra("hasDownvoted", true);
            int numLikes = getIntent().getIntExtra("numLikes", 0);
            int numComments = getIntent().getIntExtra("numComments", 0);

            if (MyHistoryActivity.alive) {
                Intent toHistoryIntent = new Intent(this, MyHistoryActivity.class);
                toHistoryIntent.putExtra("postId", postId);
                toHistoryIntent.putExtra("hasUpvoted", hasUpvoted);
                toHistoryIntent.putExtra("hasDownvoted", hasDownvoted);
                toHistoryIntent.putExtra("numLikes", numLikes);
                toHistoryIntent.putExtra("numComments", numComments);
                toHistoryIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(toHistoryIntent);
            } else if (MainActivity.alive) {
                Intent toMainIntent = new Intent(this, MainActivity.class);
                toMainIntent.putExtra("postId", postId);
                toMainIntent.putExtra("hasUpvoted", hasUpvoted);
                toMainIntent.putExtra("hasDownvoted", hasDownvoted);
                toMainIntent.putExtra("numLikes", numLikes);
                toMainIntent.putExtra("numComments", numComments);
                toMainIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(toMainIntent);
            }
        }
        finish();
    }

    private void populateNoti() {
        mWheel.setVisibility(View.VISIBLE);
        mNotiObjects.clear();
        sendGetNotiRequest();
    }

    private void sendGetNotiRequest() {
        // Tag used to cancel the request
        String tag_json_obj = "get_noti_req";

        Map<String, String> params = new HashMap<String, String>();

        String url = GlobalVariables.ROOT_URL + "/api/notifications/50/0";

        Log.d("get_noti_req", "url = " + url);


        CustomRequest jsonObjReq = new CustomRequest(Request.Method.GET, url, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("get_noti_req", "response =  " + response.toString());

                        try {

                            JSONArray notiJson = response.getJSONArray("notifications");


                            for (int i = 0; i < notiJson.length(); i++) {

                                try {
                                    mNotiObjects.add(notiJson.getJSONObject(i));
                                }
                                catch (JSONException e) {
                                    Log.d("get_noti_req", e.getMessage());
                                }

                            };

                            Log.d("get_noti_req", "mNotiObjects = " + mNotiObjects.toString());


                        } catch (JSONException e) {
                            Log.d("get_noti_req", e.getMessage());
                        }

                        Collections.sort(mNotiObjects, new CommentsComparator());
                        mAdapter.updateContents(mNotiObjects);
                        mAdapter.notifyDataSetChanged();
                        mWheel.setVisibility(View.GONE);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("get_noti_req", "Error: " + error.getMessage());
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

    private void sendReadNotiRequest() {
        // Tag used to cancel the request
        String tag_json_obj = "read_noti_req";

        String url = GlobalVariables.ROOT_URL + "/api/notifications/readall";

        Map<String, String> params = new HashMap<String, String>();

        Log.d("read_noti_req", "starting sendReadNotiRequest");

        CustomRequest jsonObjReq = new CustomRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("read_noti_req", "response: " + response.toString());
                        Intent toMainIntent = new Intent(MainActivity.getInstance(), MainActivity.class);
                        MainActivity.getInstance().startActivity(toMainIntent);
                        finish();
                    }
                }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("read_noti_req", "Error: " + error.getMessage());
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
