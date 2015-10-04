package com.github.florent37.materialviewpager.sample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.github.florent37.materialviewpager.sample.fragment.MyHistoryRecyclerViewFragment;

import io.fabric.sdk.android.Fabric;

/**
 * Created by jennykim on 10/2/15.
 */
public class AboutActivity extends AppCompatActivity {

    private static AboutActivity mInstance;
    private DrawerLayout mDrawer;
    private String[] mDrawerArray;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private MyHistoryRecyclerViewFragment mMyPostsFragment;
    private MyHistoryRecyclerViewFragment mMyRepliesFragment;
    private TextView mBadge;
    private Toolbar mToolbar;

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

        setContentView(R.layout.about_main);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbar.setTitle("ABOUT");
        mToolbar.setNavigationIcon(R.mipmap.back);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            int statusbar_color = Color.rgb(255, 177, 30);
            window.setStatusBarColor(statusbar_color);
        }

        if (!BuildConfig.DEBUG)
            Fabric.with(this, new Crashlytics());

        findViewById(R.id.about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutActivity.this, AboutDetailActivity.class);
                intent.putExtra("page", "about");
                startActivity(intent);
            }
        });
        findViewById(R.id.privacy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutActivity.this, AboutDetailActivity.class);
                intent.putExtra("page", "privacy");
                startActivity(intent);
            }
        });
        findViewById(R.id.terms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutActivity.this, AboutDetailActivity.class);
                intent.putExtra("page", "terms");
                startActivity(intent);
            }
        });
//
//        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//
//        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, 0, 0);
//        mDrawer.setDrawerListener(mDrawerToggle);
//        mDrawerArray = getResources().getStringArray(R.array.drawer_array);
//        mDrawerList = (ListView) findViewById(R.id.left_drawer);
//
//
//        // Set the adapter for the list view
//        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
//                R.layout.drawer_list_item, mDrawerArray));
//        // Set the list's click listener
//        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

    }

    public static synchronized AboutActivity getInstance() {
        return mInstance;
    }



//    private class DrawerItemClickListener implements ListView.OnItemClickListener {
//        @Override
//        public void onItemClick(AdapterView parent, View view, int position, long id) {
//            selectItem(position);
//        }
//    }
//
//    /** Swaps fragments in the main content view */
//    private void selectItem(int position) {
//
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            // Highlight the selected item, update the title, and close the drawer
//            mDrawerList.setItemChecked(position, true);
////          setTitle(mDrawerArray[position]);
//            mDrawer.closeDrawer(mDrawerList);
//
//            switch (position) {
//                case 0:
//                    Intent toMainIntent = new Intent(this, MainActivity.class);
//                    finish();
//                    startActivity(toMainIntent);
//                    break;
//                case 1:
//                    Intent toMyStuffIntent = new Intent(this, MyHistoryActivity.class);
//                    finish();
//                    startActivity(toMyStuffIntent);
//                    break;
//                case 2:
//                    break;
//                case 3:
//                    logout();
//                    break;
//                default:
//                    break;
//            }
//        }

    private void logout() {
        GlobalVariables.mToken = null;
        GlobalVariables.mUserId = null;
        GlobalVariables.mTokenExp = null;
        SharedPreferences prefs = getSharedPreferences("UserInfo", 0);
        prefs.edit().clear().commit();
        Intent toSignupIntent = new Intent(this, SignUpEmailActivity.class);
        finish();
        if (MainActivity.alive) {
            MainActivity.getInstance().finish();
        }
        startActivity(toSignupIntent);
    }

}
