package com.github.florent37.materialviewpager.sample;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by jennykim on 10/2/15.
 */
public class AboutDetailActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private AboutDetailActivity mInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInstance = this;

        setContentView(R.layout.about_detail);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);

        if (getIntent().hasExtra("page")) {
            String page = getIntent().getStringExtra("page");

            switch (page) {
                case ("about"):
                    mToolbar.setTitle("ABOUT YOLK");
                    findViewById(R.id.about).setVisibility(View.VISIBLE);
                    findViewById(R.id.privacy).setVisibility(View.GONE);
                    findViewById(R.id.terms).setVisibility(View.GONE);
                    break;
                case ("privacy"):
                    mToolbar.setTitle("PRIVACY POLICY");
                    findViewById(R.id.about).setVisibility(View.GONE);
                    findViewById(R.id.privacy).setVisibility(View.VISIBLE);
                    findViewById(R.id.terms).setVisibility(View.GONE);
                    break;
                case ("terms"):
                    mToolbar.setTitle("TERMS OF SERVICE");
                    findViewById(R.id.about).setVisibility(View.GONE);
                    findViewById(R.id.privacy).setVisibility(View.GONE);
                    findViewById(R.id.terms).setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }

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
    }


}
