package com.github.florent37.materialviewpager.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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
public class VerificationActivity extends AppCompatActivity {

    private static VerificationActivity mInstance;
    Button alreadyVerifiedButton;
    Button emailAgainButton;
    Button toSignInButton;

    String mEmail;
    String mPassword;

    public static synchronized VerificationActivity getInstance() {
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_password);
        mInstance = this;

        mEmail = getIntent().getStringExtra("email");

        alreadyVerifiedButton = (Button) findViewById(R.id.already_verified);
        emailAgainButton = (Button) findViewById(R.id.email_again);
        toSignInButton = (Button) findViewById(R.id.to_signin_btn);


        alreadyVerifiedButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                sendCheckIfVerifiedRequest();
            }
        });

        toSignInButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent toSignInIntent = new Intent(mInstance, SignInActivity.class);
                finish();
                SignUpEmailActivity.getInstance().finish();
                SignUpPasswordActivity.getInstance().finish();
                startActivity(toSignInIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    //TODO: CHECK IF VERIFIED
    private void sendCheckIfVerifiedRequest() {
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
                        VerificationActivity.getInstance().finish();
                        finish();
                        startActivity(toMainIntent);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("sign_up_req", "Error: " + error.getMessage());

                //TODO: SEND BACK TO SIGNUPEMAILACTIVITY IF EMAIL ALREADY EXISTS
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }
}
