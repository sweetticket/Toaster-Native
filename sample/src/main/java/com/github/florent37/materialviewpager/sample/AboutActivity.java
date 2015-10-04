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
            int statusbar_color = getResources().getColor(R.color.YellowBrown);
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
    }

    public static synchronized AboutActivity getInstance() {
        return mInstance;
    }

}
