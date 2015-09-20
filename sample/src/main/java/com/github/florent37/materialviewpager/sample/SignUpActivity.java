package com.github.florent37.materialviewpager.sample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gc.materialdesign.views.Button;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jennykim on 9/20/15.
 */
public class SignUpActivity extends AppCompatActivity {

    private static SignUpActivity mInstance;
    Button signUpButton;
    Button toSignInButton;
    EditText emailField;
    EditText passwordField;
    EditText passwordConfirmField;

    String mEmail;
    String mPassword;
    String mPassword2;

    public static synchronized SignUpActivity getInstance() {
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        mInstance = this;


        if (GlobalVariables.mUserId != null && GlobalVariables.mTokenExp != null && GlobalVariables.mToken != null) {
            Log.d("mUserId", "signed in as " + GlobalVariables.mUserId + ", token: " + GlobalVariables.mToken);

            Intent toMainIntent = new Intent(this, MainActivity.class);
            finish();
            startActivity(toMainIntent);
        }


        TextView appTitle = (TextView) findViewById(R.id.app_title);

        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/oduda.ttf");
        appTitle.setTypeface(face);

        signUpButton = (Button) findViewById(R.id.signup_btn);
        toSignInButton = (Button) findViewById(R.id.to_signin_btn);
        emailField = (EditText) findViewById(R.id.signup_email);
        passwordField = (EditText) findViewById(R.id.signup_password);
        passwordConfirmField = (EditText) findViewById(R.id.signup_password2);

        emailField.setHintTextColor(getResources().getColor(R.color.White));
        passwordField.setHintTextColor(getResources().getColor(R.color.White));
        passwordConfirmField.setHintTextColor(getResources().getColor(R.color.White));

        signUpButton.getTextView().setTextColor(getResources().getColor(R.color.ColorPrimary));


        signUpButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                mEmail = emailField.getText().toString().trim().toLowerCase();
                mPassword = passwordField.getText().toString();
                mPassword2 = passwordConfirmField.getText().toString();

                if (mEmail != "" && mPassword != "" && mPassword2 != "") {
                    sendSignUpRequest();

                } else if (mPassword != mPassword2) {
                    //TODO: Display error msg
                }


            }
        });

        toSignInButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent toSignInIntent = new Intent(mInstance, SignInActivity.class);
                startActivity(toSignInIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    private void sendSignUpRequest() {
        // Tag used to cancel the request
        String tag_json_obj = "sign_up_req";

        String url = GlobalVariables.ROOT_URL + "/users/register";

        Map<String, String> params = new HashMap<String, String>();
        params.put("email", mEmail);
        params.put("password", mPassword);


        CustomRequest jsonObjReq = new CustomRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("sign_up_req", response.toString());

                        try {
                            GlobalVariables.mUserId = response.getString("id");
                            GlobalVariables.mToken = response.getString("token");
                            GlobalVariables.mTokenExp = response.getString("tokenExpires");
                        } catch (org.json.JSONException e) {
                            Log.d("sign_up_req", e.getMessage());
                        }

                        Intent toMainIntent = new Intent(mInstance, MainActivity.class);
                        SignUpActivity.getInstance().finish();
                        finish();
                        startActivity(toMainIntent);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("sign_up_req", "Error: " + error.getMessage());
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }
}
