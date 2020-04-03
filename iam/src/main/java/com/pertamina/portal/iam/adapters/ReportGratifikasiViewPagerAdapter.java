package com.pertamina.portal.iam.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.fragments.BlankFragment;
import com.pertamina.portal.iam.fragments.FragmentReportGratifikasi;
import com.pertamina.portal.iam.fragments.SuratKeterangan.FragmentSKetVisa;

public class ReportGratifikasiViewPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    public static final int[] TAB_TITLES = new int[]{
            R.string.penerimaan,
            R.string.string_pemberian,
            R.string.string_permintaan};
    private final Context mContext;
    public static FragmentReportGratifikasi pemberian, permintaan, penerimaan;
    private final BlankFragment blankDefault;

    public ReportGratifikasiViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        pemberian = FragmentReportGratifikasi.newInstance(1,"Pemberian");
        permintaan = FragmentReportGratifikasi.newInstance(1,"Permintaan");
        penerimaan = FragmentReportGratifikasi.newInstance(1,"Penerimaan");
        blankDefault = BlankFragment.newInstance("", "");
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
                return penerimaan;
            case 1:
                // TODO show pending
                return pemberian;
            case 2:
                return permintaan;
            default:
                return blankDefault;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
