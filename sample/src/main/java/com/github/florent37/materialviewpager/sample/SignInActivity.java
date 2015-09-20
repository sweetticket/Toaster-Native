package com.github.florent37.materialviewpager.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.gc.materialdesign.views.Button;

/**
 * Created by jennykim on 9/20/15.
 */
public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        Button signInButton = (Button) findViewById(R.id.signin_btn);

        signInButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                EditText emailField = (EditText) findViewById(R.id.signin_email);
                EditText passwordField = (EditText) findViewById(R.id.signin_password);
                String email = emailField.getText().toString().trim().toLowerCase();
                String password = passwordField.getText().toString();

                //TODO: Call Meteor sign-in, return back errors or success

            }
        });

    }

    }
