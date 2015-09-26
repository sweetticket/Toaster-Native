package com.github.florent37.materialviewpager.sample;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jennykim on 9/25/15.
 */
public class VotingHelpers {

    public static void sendPostUpvoteRequest(String postId) {
        // Tag used to cancel the request
        String tag_json_obj = "post_upvote_req";

        String url = GlobalVariables.ROOT_URL + "/api/posts/upvote";



        Map<String, String> params = new HashMap<String, String>();
        params.put("postId", postId);


        CustomRequest jsonObjReq = new CustomRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("post_upvote_req", "upvote success");
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                    Log.d("post_downvote_req", "Error: " + error.getMessage());
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

    public static void sendPostDownvoteRequest(String postId) {
        // Tag used to cancel the request
        String tag_json_obj = "post_downvote_req";

        String url = GlobalVariables.ROOT_URL + "/api/posts/downvote";



        Map<String, String> params = new HashMap<String, String>();
        params.put("postId", postId);


        CustomRequest jsonObjReq = new CustomRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("post_downvote_req", "downvote success");
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.d("post_downvote_req", "Error: " + error.getMessage());
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

    public static void sendCommentUpvoteRequest(String commentId) {
        // Tag used to cancel the request
        String tag_json_obj = "comment_upvote_req";

        String url = GlobalVariables.ROOT_URL + "/api/comments/upvote";



        Map<String, String> params = new HashMap<String, String>();
        params.put("commentId", commentId);


        CustomRequest jsonObjReq = new CustomRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("comment_upvote_req", "upvote success");
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                    Log.d("comment_upvote_req", "Error: " + error.getMessage());
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

    public static void sendCommentDownvoteRequest(String commentId) {
        // Tag used to cancel the request
        String tag_json_obj = "comment_downvote_req";

        String url = GlobalVariables.ROOT_URL + "/api/comments/downvote";



        Map<String, String> params = new HashMap<String, String>();
        params.put("commentId", commentId);


        CustomRequest jsonObjReq = new CustomRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("comment_downvote_req", "downvote success");
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.d("comment_downvote_req", "Error: " + error.getMessage());
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
