package com.github.florent37.materialviewpager.sample;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
public class SignUpPasswordActivity extends AppCompatActivity {

    private static SignUpPasswordActivity mInstance;
    Button continueButton;
    Button toSignInButton;
    EditText emailField;
    EditText passwordField;
    EditText passwordConfirmField;

    String mEmail;
    String mPassword;

    public static synchronized SignUpPasswordActivity getInstance() {
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_password);
        mInstance = this;

        mEmail = getIntent().getStringExtra("email");

        continueButton = (Button) findViewById(R.id.continue_btn);
        toSignInButton = (Button) findViewById(R.id.to_signin_btn);
        passwordField = (EditText) findViewById(R.id.signup_password);

//        passwordField.setHintTextColor(getResources().getColor(R.color.White));

//        continueButton.getTextView().setTextColor(getResources().getColor(R.color.ColorPrimary));


        continueButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                mPassword = passwordField.getText().toString();

                if (mPassword != "") {
                    if (mPassword.length() > 5) {
                        sendSignUpRequest();
                    } else {
                        findViewById(R.id.not_valid).setVisibility(View.VISIBLE);
                    }

                }


            }
        });

        toSignInButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent toSignInIntent = new Intent(mInstance, SignInActivity.class);
                finish();
                SignUpEmailActivity.getInstance().finish();
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

                        Intent toVerificationIntent = new Intent(mInstance, VerificationActivity.class);
                        SignUpEmailActivity.getInstance().finish();
                        finish();
                        startActivity(toVerificationIntent);
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
