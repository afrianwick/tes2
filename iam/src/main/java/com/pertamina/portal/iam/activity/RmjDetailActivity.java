package com.pertamina.portal.iam.activity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.pertamina.portal.core.activity.BackableActivity;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.adapters.RmjDetailAdapter;
import com.pertamina.portal.iam.models.worklist.RmjData;

public class RmjDetailActivity extends AppCompatActivity {

    public static RmjData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rmj_detail);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        RmjDetailAdapter sectionsPagerAdapter = new RmjDetailAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        data = (RmjData) getIntent().getSerializableExtra("rmjData");
        Log.d("datarmj", new Gson().toJson(data));


    }
}