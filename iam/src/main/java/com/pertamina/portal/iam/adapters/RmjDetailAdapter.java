package com.pertamina.portal.iam.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.fragments.JabatanBaruFragment;
import com.pertamina.portal.iam.fragments.JabatanLamaFragment;
import com.pertamina.portal.iam.fragments.ListWorklistFragment;
import com.pertamina.portal.iam.fragments.PlaceholderFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class RmjDetailAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[] {
            R.string.tab_jabatan_lama,
            R.string.tab_jabatan_baru};
    private final Context mContext;

    public RmjDetailAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                // TODO show all
                return new JabatanBaruFragment();
            case 1:
                // TODO show pending
                return new JabatanLamaFragment();
        }

        // Sementara
        return PlaceholderFragment.newInstance(position + 1);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return TAB_TITLES.length;
    }
}