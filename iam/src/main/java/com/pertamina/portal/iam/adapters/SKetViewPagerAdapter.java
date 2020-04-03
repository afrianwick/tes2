package com.pertamina.portal.iam.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.fragments.BlankFragment;
import com.pertamina.portal.iam.fragments.SuratKeterangan.FragmentSKetVisa;

public class SKetViewPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    public static final int[] TAB_TITLES = new int[]{
            R.string.non_visa,
            R.string.visa};
    private final Context mContext;
    private final FragmentSKetVisa visa, nonVisa;
    private final BlankFragment blankDefault;

    public SKetViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        visa = FragmentSKetVisa.newInstance(1,"visa");
        blankDefault = BlankFragment.newInstance("", "");
        nonVisa = FragmentSKetVisa.newInstance(1, "non-visa");
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                // TODO show all
                return nonVisa;
            case 1:
                // TODO show pending
                return visa;
            default:
                return blankDefault;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
