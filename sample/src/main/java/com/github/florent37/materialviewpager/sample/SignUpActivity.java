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
public class SignUpActivity extends AppCompatActivity {

    Context mContext;
    Button signUpButton;
    Button toSignInButton;
    EditText emailField;
    EditText passwordField;
    EditText passwordConfirmField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        mContext = this;



        SharedPreferences prefs = getSharedPreferences("UserInfo", 0);
        String userId = prefs.getString("toaster_userId", null);
        if (userId != null) {
            Log.d("toaster_userId", "signed in as " + userId);
            GlobalVariables.mUserId = userId;

            Intent toMainIntent = new Intent(this, MainActivity.class);
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
                EditText emailField = (EditText) findViewById(R.id.signup_email);
                EditText passwordField = (EditText) findViewById(R.id.signup_password);
                EditText passwordConfirmField = (EditText) findViewById(R.id.signup_password2);
                String email = emailField.getText().toString().trim().toLowerCase();
                String password = passwordField.getText().toString();
                String password2 = passwordConfirmField.getText().toString();

                //TODO: Call Meteor create user, return back errors or success

            }
        });

        toSignInButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent toSignInIntent = new Intent(mContext, SignInActivity.class);
                startActivity(toSignInIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    }
