package com.github.florent37.materialviewpager.sample;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gc.materialdesign.views.Button;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jennykim on 9/20/15.
 */
public class NewPostItem extends RecyclerView.ViewHolder {

    private TextView mCharCount;
    private EditText mNewPost;
    private Button mSubmitBtn;
    private RecyclerView.Adapter mAdapter;


    public NewPostItem(View view) {
        super(view);

        mNewPost = (EditText) view.findViewById(R.id.new_post_input);
        mNewPost.setFocusable(true);
        mNewPost.setFocusableInTouchMode(true);

        mCharCount = (TextView) view.findViewById(R.id.char_count);
        mNewPost.addTextChangedListener(mNewPostWatcher);

        mSubmitBtn = (Button) view.findViewById(R.id.submit);
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNewPostRequest();
            }
        });

    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
    }

    private final TextWatcher mNewPostWatcher= new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            mCharCount.setText(String.valueOf(140 - s.length()));
        }

        public void afterTextChanged(Editable s) {
        }
    };

    private void sendNewPostRequest() {
        // Tag used to cancel the request
        String tag_json_obj = "new_post_req";

        String url = GlobalVariables.ROOT_URL + "/api/posts/new";
        String postbody = mNewPost.getText().toString().trim();

        if (postbody != "") {

            Map<String, String> params = new HashMap<String, String>();
            params.put("postBody", postbody);


            CustomRequest jsonObjReq = new CustomRequest(Request.Method.POST, url, params,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("new_post_req", response.toString());
                            mNewPost.setText("");
                            mCharCount.setText("140");
                            MainActivity.getInstance().getFragment(0).populatePosts();
                            MainActivity.getInstance().getFragment(1).populatePosts();
                            MainActivity.getInstance().closeKeyboard();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("new_post_req", "Error: " + error.getMessage());
                }
            }){

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
}
