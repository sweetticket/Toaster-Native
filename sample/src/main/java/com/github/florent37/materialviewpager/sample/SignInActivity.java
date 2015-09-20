package com.github.florent37.materialviewpager.sample;

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

import com.gc.materialdesign.views.Button;

/**
 * Created by jennykim on 9/20/15.
 */
public class SignInActivity extends AppCompatActivity {

    Context mContext;
    Button signInButton;
    Button toSignUpButton;
    EditText emailField;
    EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        mContext = this;



        SharedPreferences prefs = getSharedPreferences("UserInfo", 0);
        String userId = prefs.getString("toaster_userId", null);
        if (userId != null) {
            Log.d("toaster_userId", "signed in as " + userId);
            GlobalVariables.mUserId = userId;

            Intent toMainIntent = new Intent(this, MainActivity.class);
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

                String email = emailField.getText().toString().trim().toLowerCase();
                String password = passwordField.getText().toString();

                //TODO: Call Meteor create user, return back errors or success

            }
        });

        toSignUpButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    }
