package com.github.florent37.materialviewpager.sample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.sample.AppController;
import com.github.florent37.materialviewpager.sample.CustomMaterialAdapter;
import com.github.florent37.materialviewpager.sample.CustomRequest;
import com.github.florent37.materialviewpager.sample.GlobalVariables;
import com.github.florent37.materialviewpager.sample.R;
import com.github.florent37.materialviewpager.sample.PostsRecyclerViewAdapter;
import com.github.florent37.materialviewpager.sample.RecentComparator;
import com.github.florent37.materialviewpager.sample.TrendingComparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class RecyclerViewFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private CustomMaterialAdapter mAdapter;
    private List<Object> mPostObjects = new ArrayList<Object>();
    private Map<String, Integer> mCommentsCountMap = new HashMap<String, Integer>();
    private int mPosition;

    public static RecyclerViewFragment newInstance() {
        return new RecyclerViewFragment();
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public void populatePosts() {
        mPostObjects.clear();
        mCommentsCountMap.clear();

        String url;

        if (mPosition == 0) {
            // recent
            url = GlobalVariables.ROOT_URL + "/publications/recentPostsAndComments";


        } else {
            // trending
            url = GlobalVariables.ROOT_URL + "/publications/trendingPostsAndComments";
        }

        sendGetPostsRequest(url);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new CustomMaterialAdapter(new PostsRecyclerViewAdapter(mPostObjects, mCommentsCountMap));
        mRecyclerView.setAdapter(mAdapter);
        populatePosts();
        mAdapter.updateContents(mPostObjects, mCommentsCountMap);
//        Log.d("onViewCreated", mPostObjects.toString());

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);

    }

    private void sendGetPostsRequest(String url) {

        // Tag used to cancel the request
        String tag_json_obj = "get_posts_req";

        Map<String, String> params = new HashMap<String, String>();

        CustomRequest jsonObjReq = new CustomRequest(Request.Method.GET, url, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
//                        Log.d("postsandcomments", response.toString());

                        try {

                            JSONArray postsJson = response.getJSONArray("posts");

//                            Log.d("get_posts_req", postsJson.toString());
//                            Log.d("get_posts_req", "json.length() = " + postsJson.length());

                            for (int i = 0; i < postsJson.length(); i++) {

                                try {
                                    mPostObjects.add(postsJson.getJSONObject(i));
                                }
                                catch (JSONException e) {
                                    Log.d("get_posts_req", e.getMessage());

                                }

                            };



                            Log.d("get_posts_req", "mPostObjects = " + mPostObjects.toString());


                        } catch (org.json.JSONException e) {
                            Log.d("get_posts_req", e.getMessage());
                        }

                        if (mPosition == 0) {
                            Collections.sort(mPostObjects, new RecentComparator());
                        } else {
                            Collections.sort(mPostObjects, new TrendingComparator());
                        }
                        // add empty head for 'new post' card
                        mPostObjects.add(0, new Object());
                        mAdapter.notifyDataSetChanged();

                        try {
                            JSONArray commentsJson = response.getJSONArray("comments");
                            for (int i = 0; i < commentsJson.length(); i++) {

                                try {
                                    String key = commentsJson.getJSONObject(i).getString("postId");
                                    if (mCommentsCountMap.containsKey(key)) {
                                        mCommentsCountMap.put(key, mCommentsCountMap.get(key) + 1);
                                    } else {
                                        mCommentsCountMap.put(key, 1);
                                    }
                                }
                                catch (JSONException e) {
                                    Log.d("get_posts_req", e.getMessage());

                                }

                            };


                        } catch (JSONException e) {
                            Log.d("get_posts_req", e.getMessage());
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("get_posts_req", "Error: " + error.getMessage());
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
