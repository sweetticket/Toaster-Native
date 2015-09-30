package com.github.florent37.materialviewpager.sample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.github.florent37.materialviewpager.sample.fragment.RecyclerViewFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    private MaterialViewPager mViewPager;
    private static MainActivity mInstance;
    private DrawerLayout mDrawer;
    private String[] mDrawerArray;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private RecyclerViewFragment mRecentFragment;
    private RecyclerViewFragment mTrendingFragment;
    private TextView mBadge;

    static boolean active = false;

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInstance = this;

        setContentView(R.layout.activity_main);

        if (!BuildConfig.DEBUG)
            Fabric.with(this, new Crashlytics());

        setTitle("");

        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);

        toolbar = mViewPager.getToolbar();
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (toolbar != null) {
            setSupportActionBar(toolbar);

            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setHomeButtonEnabled(true);
            }
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, 0, 0);
        mDrawer.setDrawerListener(mDrawerToggle);
        mDrawerArray = getResources().getStringArray(R.array.drawer_array);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);


        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mDrawerArray));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position % 2) {
                    case 0:
                        mRecentFragment =  RecyclerViewFragment.newInstance();
                        mRecentFragment.setPosition(0);
                        return mRecentFragment;
                    case 1:
                        mTrendingFragment =  RecyclerViewFragment.newInstance();
                        mTrendingFragment.setPosition(1);
                        return mTrendingFragment;
                    //case 2:
                    //    return WebViewFragment.newInstance();
                    default:
                        RecyclerViewFragment fragment = RecyclerViewFragment.newInstance();
                        return fragment;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % 2) {
                    case 0:
                        return "Recent";
                    case 1:
                        return "Trending";

                }
                return "";
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);
            }

        });

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.ColorPrimary,
                                "https://fs01.androidpit.info/a/63/0e/android-l-wallpapers-630ea6-h900.jpg");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.ColorSecondary,
                                "http://cdn1.tnwcdn.com/wp-content/blogs.dir/1/files/2014/06/wallpaper_51.jpg");
                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());


        View logo = findViewById(R.id.logo_white);
        if (logo != null)
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.notifyHeaderChanged();
                    Toast.makeText(getApplicationContext(), "Yes, the title is clickable", Toast.LENGTH_SHORT).show();
                }
            });

        sendGetNumUnreadRequest();


    }

    public static synchronized MainActivity getInstance() {
        return mInstance;
    }

    public void updateVotes(String postId, boolean hasUpvoted, boolean hasDownvoted, int numLikes) {
        PostItem recentPostItem = getFragment(0).getAdapter().getOriginalAdapter().getPostItem(postId);
        PostItem trendingPostItem = getFragment(1).getAdapter().getOriginalAdapter().getPostItem(postId);
        if (recentPostItem != null) {
            recentPostItem.updatePost(hasUpvoted, hasDownvoted, numLikes);
        }
        if (trendingPostItem != null) {
            trendingPostItem.updatePost(hasUpvoted, hasDownvoted, numLikes);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getIntent().hasExtra("postId")){
            String postId = getIntent().getStringExtra("postId");
            boolean hasUpvoted = getIntent().getBooleanExtra("hasUpvoted", true);
            boolean hasDownvoted = getIntent().getBooleanExtra("hasDownvoted", true);
            int numLikes = getIntent().getIntExtra("numLikes", 0);
            int numComments = getIntent().getIntExtra("numComments", 0);
            PostItem recentPostItem = getFragment(0).getAdapter().getOriginalAdapter().getPostItem(postId);
            PostItem trendingPostItem = getFragment(1).getAdapter().getOriginalAdapter().getPostItem(postId);
            if (recentPostItem != null) {
                recentPostItem.updatePost(hasUpvoted, hasDownvoted, numLikes, numComments);
            }
            if (trendingPostItem != null) {
                trendingPostItem.updatePost(hasUpvoted, hasDownvoted, numLikes, numComments);
            }
        }

//        if (mClickedPost != null) {
//            boolean hasUpvoted = getIntent().getBooleanExtra("hasUpvoted", true);
//            boolean hasDownvoted = getIntent().getBooleanExtra("hasDownvoted", true);
//            int numLikes = getIntent().getIntExtra("numLikes", 0);
//            int numComments = getIntent().getIntExtra("numComments", 0);
//            mClickedPost.updatePost(hasUpvoted, hasDownvoted, numLikes, numComments);
//            mClickedPost = null;
//            getFragment(0).getAdapter().getItem()
//        }
    }

    public MaterialViewPager getViewPager() {
        return mViewPager;
    }

//    public void setClickedPost(PostItem postitem) {
//        mClickedPost = postitem;
//    }

    public RecyclerViewFragment getFragment(int position) {
        if (position == 0) {
            return mRecentFragment;
        } else {
            return mTrendingFragment;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);

        View noticeActionView = menu.findItem(R.id.notice).getActionView();
        mBadge = (TextView) noticeActionView.findViewById(R.id.actionbar_notifcation_textview);
        noticeActionView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.getInstance(), NotificationsActivity.class);
                MainActivity.getInstance().startActivity(intent);
//                MainActivity.getInstance().finish();
            }
        });

        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) ||
                super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {

        switch(position) {
            case 0:
                //TODO: intent for profile activity
                break;
            case 2:
                //TODO: logout, go to signup page
                break;
            default:
                break;
        }

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
//        setTitle(mDrawerArray[position]);
        mDrawer.closeDrawer(mDrawerList);
    }

    public void setBadgeCount(String numUnread) {
        if (numUnread == "0") {
            mBadge.setVisibility(View.GONE);
        } else {
            mBadge.setVisibility(View.VISIBLE);
            mBadge.setText(numUnread);
        }
    }

    public void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void sendGetNumUnreadRequest() {
        // Tag used to cancel the request
        String tag_json_obj = "get_num_unread_req";

        String url = GlobalVariables.ROOT_URL + "/api/notifications/getnumunread";

        Map<String, String> params = new HashMap<String, String>();


        CustomRequest jsonObjReq = new CustomRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("get_num_unread_req", response.toString());
                        try {
                            String numUnread = response.getString("numUnread");
                            setBadgeCount(numUnread);
                        } catch (org.json.JSONException e) {
                            Log.d("get_num_unread_req", "onResponse Error: " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("get_num_unread_req", "Error: " + error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + GlobalVariables.mToken);
                return headers;
            }
        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }
}
