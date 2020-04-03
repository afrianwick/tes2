package com.pertamina.portal.iam.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.adapters.HistorySuratKeteranganAdapter;
import com.pertamina.portal.iam.adapters.SKetViewPagerAdapter;
import com.pertamina.portal.iam.adapters.SectionsPagerAdapter;

import dmax.dialog.SpotsDialog;

public class HistorySuratKetActivity extends AppCompatActivity {

    private ImageButton backIB;
    private TabItem tabVisa, tabNonVisa;
    private TabLayout tabLayout;
    private HistorySuratKeteranganAdapter historySuratKeteranganAdapter;
    private ViewPager viewPager;
    private SKetViewPagerAdapter sKetViewPagerAdapter;
    private AlertDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_surat_keterangan);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.view_pager);

        sKetViewPagerAdapter = new SKetViewPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(sKetViewPagerAdapter);
        viewPager.setOffscreenPageLimit(SKetViewPagerAdapter.TAB_TITLES.length);
        tabLayout.setupWithViewPager(viewPager);

        backIB = findViewById(R.id.btn_back);
        backIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loading = new SpotsDialog.Builder().setContext(this).build();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public AlertDialog getLoading() {
        return loading;
    }
}
