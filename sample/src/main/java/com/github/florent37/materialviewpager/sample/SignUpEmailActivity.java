package com.github.florent37.materialviewpager.sample;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
public class SignUpEmailActivity extends AppCompatActivity {

    private static SignUpEmailActivity mInstance;
    Button continueButton;
    TextView toSignInButton;
    EditText emailField;

    String mEmail;
    private Toolbar mToolbar;

    public static synchronized SignUpEmailActivity getInstance() {
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_email);
        mInstance = this;


        if (GlobalVariables.mUserId != null && GlobalVariables.mTokenExp != null && GlobalVariables.mToken != null) {
            Log.d("mUserId", "signed in as " + GlobalVariables.mUserId + ", token: " + GlobalVariables.mToken);

            Intent toMainIntent = new Intent(this, MainActivity.class);
            finish();
            startActivity(toMainIntent);
        }

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("SIGN UP");
        setSupportActionBar(mToolbar);

        continueButton = (Button) findViewById(R.id.continue_btn);
        toSignInButton = (TextView) findViewById(R.id.to_signin_btn);
        emailField = (EditText) findViewById(R.id.signup_email);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            int statusbar_color = getResources().getColor(R.color.YellowBrown);
            window.setStatusBarColor(statusbar_color);
        }

        emailField.setHintTextColor(getResources().getColor(R.color.ColorMediumGray));

//        continueButton.getTextView().setTextColor(getResources().getColor(R.color.ColorPrimary));


        continueButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                mEmail = emailField.getText().toString().trim().toLowerCase();

                if (mEmail != "") {
                    EmailValidator validator = new EmailValidator();
                    if (validator.validate(mEmail)) {
                        Intent toSignUpPasswordIntent = new Intent(mInstance, SignUpPasswordActivity.class);
                        toSignUpPasswordIntent.putExtra("email", mEmail);
                        startActivity(toSignUpPasswordIntent);
                    } else {
                        findViewById(R.id.already_exists).setVisibility(View.GONE);
                        findViewById(R.id.not_valid).setVisibility(View.VISIBLE);
                    }
                }


            }
        });

        toSignInButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent toSignInIntent = new Intent(mInstance, SignInActivity.class);
                finish();
                startActivity(toSignInIntent);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getIntent().hasExtra("emailNotNew")) {
            findViewById(R.id.not_valid).setVisibility(View.GONE);
            findViewById(R.id.already_exists).setVisibility(View.VISIBLE);
            emailField.setText("");
        }
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }


}
