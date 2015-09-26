package com.github.florent37.materialviewpager.sample;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ocpsoft.pretty.time.PrettyTime;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jennykim on 9/20/15.
 */
public class NotificationItem extends RecyclerView.ViewHolder {

    private TextView mNotiBody;
    private TextView mNotiPreview;
    private TextView mNotiDate;
    private TextView mNotiCount;
    private ImageView mNotiIcon;
    private String mPostId;
    private LinearLayout mCardContent;

    private String mNotiId;

    private JSONObject mNotiObject;

    public NotificationItem(View view) {
        super(view);

        mNotiBody = (TextView) view.findViewById(R.id.noti_body);
        mNotiDate = (TextView) view.findViewById(R.id.noti_date);
        mNotiPreview = (TextView) view.findViewById(R.id.noti_preview);
        mNotiCount = (TextView) view.findViewById(R.id.noti_count);
        mNotiIcon = (ImageView) view.findViewById(R.id.noti_icon);
        mCardContent = (LinearLayout) view.findViewById(R.id.card_content);
        View.OnClickListener toPostDetailListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toPostDetail();
            }
        };
        view.setOnClickListener(toPostDetailListener);

    }

    public void bindNoti(JSONObject noti) {

        try {
            mNotiObject = noti;
            mPostId = noti.getString("postId");
            String body = noti.getString("body");
            int count = noti.getInt("countUnread");
            String type = noti.getString("type");
            boolean isRead = noti.getBoolean("isRead");
            try {
                Date createdAt = ISO8601DateParser.parse(noti.getString("createdAt"));
                PrettyTime p = new PrettyTime();
                mNotiDate.setText(p.format(createdAt));

            } catch (java.text.ParseException e) {
                Log.d("bindPost", "createdAt Error: " + e.getMessage());
            }
            mNotiId = noti.getString("_id");
            mNotiBody.setText(body);
            mNotiCount.setText(count + "");

            if (type == "upvote") {
                mNotiIcon.setImageResource(R.mipmap.thumbsup);
            } else if (type == "downvote") {
                mNotiIcon.setImageResource(R.mipmap.thumbsdown);
            } else {
                mNotiIcon.setImageResource(R.mipmap.chatbubble);
            }

            if (isRead) {
                mCardContent.setBackgroundColor(NotificationsActivity.getInstance().getResources().getColor(R.color.White));
            } else {
                mCardContent.setBackgroundColor(NotificationsActivity.getInstance().getResources().getColor(R.color.Highlight));
            }

        } catch (JSONException e) {
            Log.d("bindPost", e.getMessage());
        }


    }

    private void setNotiPreview() {

    }

    private void toPostDetail() {
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
                            MainActivity.getInstance().finish();
                        } catch (JSONException e) {
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
