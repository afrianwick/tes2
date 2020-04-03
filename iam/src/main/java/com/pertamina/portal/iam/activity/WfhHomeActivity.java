package com.pertamina.portal.iam.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.fragments.WfhMenuFragment;
import com.pertamina.portal.iam.fragments.RecentTaskFragment;
import com.pertamina.portal.iam.fragments.SettingsFragment;
import com.pertamina.portal.iam.models.Task;

public class WfhHomeActivity extends AppCompatActivity implements WfhMenuFragment.OnFragmentInteractionListener,
        RecentTaskFragment.OnListFragmentInteractionListener {

    private final FragmentManager fm = getSupportFragmentManager();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.navigation_home) {
                fm.beginTransaction()
                        .hide(fragmentActive)
                        .show(menuFragment)
                        .commit();

                fragmentActive = menuFragment;

                return true;
            } else if (id == R.id.navigation_notifications) {
                fm.beginTransaction()
                        .hide(fragmentActive)
                        .show(settingFragment)
                        .commit();

                fragmentActive = settingFragment;

                return true;
            } else {
                return false;
            }
        }
    };

    private Fragment fragmentActive;
    private WfhMenuFragment menuFragment;
    private SettingsFragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wfh_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        menuFragment = WfhMenuFragment.newInstance(null, null);
        settingFragment = SettingsFragment.newInstance();
        fragmentActive = menuFragment;

        fm.beginTransaction()
                .add(R.id.main_container, settingFragment, "settings")
                .add(R.id.main_container, menuFragment, "menu")
                .hide(settingFragment)
                .show(menuFragment)
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(Task item) {

    }
}
