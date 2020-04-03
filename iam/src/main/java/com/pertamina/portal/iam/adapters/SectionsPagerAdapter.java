package com.pertamina.portal.iam.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.fragments.BlankFragment;
import com.pertamina.portal.iam.fragments.ListWorklistFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    public static final int[] TAB_TITLES = new int[]{
            R.string.task_list,
            R.string.tab_pending,
            R.string.tab_approved,
            R.string.tab_rejected,
            R.string.tab_history};
    private final Context mContext;
    private final ListWorklistFragment all;
    private final ListWorklistFragment rejected, approved, pending, history;
    private final BlankFragment blankRejected;
    BlankFragment blankPending, blankHistory;
    private final BlankFragment blankDefault;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        all = ListWorklistFragment.newInstance(1,"all");
        blankPending = BlankFragment.newInstance("", "");
        blankRejected = BlankFragment.newInstance("", "");
        blankHistory = BlankFragment.newInstance("", "");
        pending = ListWorklistFragment.newInstance(1, ListWorklistFragment.LIST_PENDING);
        approved = ListWorklistFragment.newInstance(1, ListWorklistFragment.LIST_APPROVED);
        rejected = ListWorklistFragment.newInstance(1, ListWorklistFragment.LIST_REJECTED);
        history = ListWorklistFragment.newInstance(1, ListWorklistFragment.LIST_HISTORY);
        blankDefault = BlankFragment.newInstance("", "");
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                // TODO show all
                return all;
            case 1:
                // TODO show pending
                return pending;
            case 2:
                // TODO show approved
                return approved;
            case 3:
                // TODO show rejected
                return rejected;
            case 4:
                // TODO show history
                return history;
            default:
                return blankDefault;
        }

        // Sementara
//        return PlaceholderFragment.newInstance(position + 1);
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

    public ListWorklistFragment getAllWorklistFragment() {
        return all;
    }
}