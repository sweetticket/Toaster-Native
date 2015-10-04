package com.github.florent37.materialviewpager.sample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends Activity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = this.getWindow();
//            // clear FLAG_TRANSLUCENT_STATUS flag:
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            // finally change the color
//
////            //ORANGE
//            int statusbar_color = Color.rgb(255, 94, 58);
//
//            window.setStatusBarColor(statusbar_color);
//        }

        /* New Handler to start the Menu-Activity
         * and close this SplashActivity-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                Intent intent;

                if (GlobalVariables.mUserId != null && GlobalVariables.mTokenExp != null && GlobalVariables.mToken != null && GlobalVariables.mIsVerified) {
                    Log.d("mUserId", "signed in as " + GlobalVariables.mUserId + ", token: " + GlobalVariables.mToken);
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                } else {
                /* Create an Intent that will start the Menu-Activity. */
                   intent = new Intent(SplashActivity.this, MainActivity.class);
                }
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
