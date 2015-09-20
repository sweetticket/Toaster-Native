package com.github.florent37.materialviewpager.sample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.gc.materialdesign.views.Button;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jennykim on 9/20/15.
 */
public class SignInActivity extends AppCompatActivity {

    private static SignInActivity mInstance;
    Button signInButton;
    Button toSignUpButton;
    EditText emailField;
    EditText passwordField;

    String mEmail;
    String mPassword;

    public static synchronized SignInActivity getInstance() {
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        mInstance = this;

        if (GlobalVariables.mUserId != null && GlobalVariables.mTokenExp != null && GlobalVariables.mToken != null) {
            Log.d("mUserId", "signed in as " + GlobalVariables.mUserId);

            Intent toMainIntent = new Intent(this, MainActivity.class);
            SignUpActivity.getInstance().finish();
            finish();
            startActivity(toMainIntent);
        }

        TextView appTitle = (TextView) findViewById(R.id.app_title);

        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/oduda.ttf");
        appTitle.setTypeface(face);

        signInButton = (Button) findViewById(R.id.signin_btn);
        toSignUpButton = (Button) findViewById(R.id.to_signup_btn);
        emailField = (EditText) findViewById(R.id.signin_email);
        passwordField = (EditText) findViewById(R.id.signin_password);

        emailField.setHintTextColor(getResources().getColor(R.color.White));
        passwordField.setHintTextColor(getResources().getColor(R.color.White));

        signInButton.getTextView().setTextColor(getResources().getColor(R.color.ColorPrimary));


        signInButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                mEmail = emailField.getText().toString().trim().toLowerCase();
                mPassword = passwordField.getText().toString();

                //TODO: Call Meteor create user, return back errors or success
                sendSignInRequest();

                Intent toMainIntent = new Intent(mInstance, MainActivity.class);
                finish();
                startActivity(toMainIntent);

            }
        });

        toSignUpButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void sendSignInRequest() {
        // Tag used to cancel the request
        String tag_json_obj = "sign_in_req";

        String url = GlobalVariables.ROOT_URL + "/users/login";

        Map<String, String> params = new HashMap<String, String>();
        params.put("email", mEmail);
        params.put("password", mPassword);


        CustomRequest jsonObjReq = new CustomRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("sign_in_req", response.toString());

                        try {
                            GlobalVariables.mUserId = response.getString("id");
                            GlobalVariables.mToken = response.getString("token");
                            GlobalVariables.mTokenExp = response.getString("tokenExpires");

                            SharedPreferences prefs = getSharedPreferences("UserInfo", 0);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("userId", GlobalVariables.mUserId);
                            editor.putString("token", GlobalVariables.mToken);
                            editor.putString("tokenExp", GlobalVariables.mTokenExp);
                            editor.commit();

                        } catch (org.json.JSONException e) {
                            Log.d("sign_in_req", e.getMessage());
                        }

                        Intent toMainIntent = new Intent(mInstance, MainActivity.class);
                        SignUpActivity.getInstance().finish();
                        finish();
                        startActivity(toMainIntent);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("sign_in_req", "Error: " + error.getMessage());
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


    }
