package com.github.florent37.materialviewpager.sample;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

//import com.parse.Parse;
//import com.parse.ParseInstallation;

/**
 * Created by jennykim on 9/12/15.
 */
public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        SharedPreferences prefs = getSharedPreferences("UserInfo", 0);
        GlobalVariables.mUserId = prefs.getString("userId", null);
        GlobalVariables.mToken = prefs.getString("token", null);
        GlobalVariables.mTokenExp = prefs.getString("tokenExp", null);

//        Parse.initialize(this, "nGWY63hAKCyyMHS41xmjNiL4mCIqsJ0TBGWAG4vy", "w1ps0nxnPNfpJvIGnw52wCl5Og5eOLgiwiuXHn6i");
//
//        // background save wrapped in Async to prevent freezing
////        new BackgroundSave().execute(getApplicationContext());
//        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
//        installation.saveInBackground();
//        GlobalVariables.setParseObjectId(installation.getObjectId());
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
