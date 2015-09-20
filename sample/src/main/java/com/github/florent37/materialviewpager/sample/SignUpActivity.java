package com.github.florent37.materialviewpager.sample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gc.materialdesign.views.Button;

/**
 * Created by jennykim on 9/20/15.
 */
public class SignUpActivity extends AppCompatActivity {

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        mContext = this;

        TextView appTitle = (TextView) findViewById(R.id.app_title);

        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/oduda.ttf");
        appTitle.setTypeface(face);

        Button signUpButton = (Button) findViewById(R.id.signup_btn);
        Button toSignInButton = (Button) findViewById(R.id.to_signin_btn);

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
