package com.pertamina.portal.iam.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pertamina.portal.core.activity.BackableNoActionbarActivity;
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.worklist.AHEActivity;
import com.pertamina.portal.iam.activity.worklist.ClvActivity;
import com.pertamina.portal.iam.activity.worklist.LevDetailActivity;
import com.pertamina.portal.iam.activity.worklist.MCRActivity;
import com.pertamina.portal.iam.activity.worklist.MclDetailActivity;
import com.pertamina.portal.iam.activity.worklist.MppkActivity;
import com.pertamina.portal.iam.activity.worklist.PanDetailActivity;
import com.pertamina.portal.iam.activity.worklist.RmjDetailApprovalActivity;
import com.pertamina.portal.iam.activity.worklist.SuratKetVisaHRApprovalActivity;
import com.pertamina.portal.iam.adapters.SectionsPagerAdapter;
import com.pertamina.portal.iam.adapters.WorkListFilterAdapter;
import com.pertamina.portal.iam.adapters.WorkListFilterRVAdapter;
import com.pertamina.portal.iam.fragments.ListWorklistFragment;
import com.pertamina.portal.iam.interfaces.MyWorkListView;
import com.pertamina.portal.iam.models.Task;
import com.pertamina.portal.iam.models.TaskApproval;
import com.pertamina.portal.iam.models.TaskHistory;
import com.pertamina.portal.iam.models.TaskPending;
import com.pertamina.portal.iam.models.TaskRejected;
import com.pertamina.portal.iam.utils.WorklistCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class MyWorklistActivity extends BackableNoActionbarActivity
        implements ListWorklistFragment.OnListFragmentInteractionListener, MyWorkListView {

    private static final String TAG = "MyWorklistActivity";
    private ImageView ivAddFilter, searchIV, sortIV;
//    private CheckBox cbLev, cbPan, cbMcl, cbClv, cbSkmj, cbMppk, cbFlc, cbSsc, cbDpkp, cbMccr;
//    private View llLev;
//    private View llPan;
//    private View llMcl;
//    private View llSkmj;
//    private View llClv;
    private RecyclerView filterListRV;
    private WorkListFilterAdapter workListFilterAdapter;
    private List<String> filters;
    private List<Integer> filtersIndex;
    private boolean[] isFilterChecked;
    private AlertDialog alertDialog;
    private AlertDialog loading;
    private SectionsPagerAdapter pagerAdapter;
    private EditText etSearch;
    private ViewPager viewPager;
    private TabLayout tabs;
    private boolean isFiltered = false;
    public static HashMap<String, String> filterNew;
    public static String[] filter;
    private WorkListFilterRVAdapter workListFilterRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_worklist);
        super.onCreateBackable(this, R.id.ivBack);
        ButterKnife.bind(this);

        sortIV = findViewById(R.id.sortIV);
        etSearch = (EditText) findViewById(R.id.etSearch);
        ivAddFilter = findViewById(R.id.ivAddFilter);
        filterListRV = findViewById(R.id.workListFilterListRV);
        searchIV = findViewById(R.id.workListSearchIV);

        pagerSetup();

        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        filterValueSetupNew();

        ivAddFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterDialog();
            }
        });
//        llLev.setVisibility(View.GONE);
//        llPan.setVisibility(View.GONE);
//        llMcl.setVisibility(View.GONE);
//        llSkmj.setVisibility(View.GONE);
//        llClv.setVisibility(View.GONE);

        filterListSetup(new ArrayList<String>(), new ArrayList<Integer>());
        loading = new SpotsDialog.Builder().setContext(this).build();

        buildAlert();

        sortIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ListWorklistFragment.sortAsc) {
                    ListWorklistFragment.sortAsc = false;
                } else {
                    ListWorklistFragment.sortAsc = true;
                }

                search();
            }
        });
    }

    private void filterValueSetupNew() {
        filterNew = new HashMap<>();
    }

    private void pagerSetup() {
        pagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        tabs = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(SectionsPagerAdapter.TAB_TITLES.length);
        tabs.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(4);

        tabHandler();
    }

    private void tabHandler() {
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                search();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void filterValueSetup() {
        if (filter == null) {
            filter = new String[filterNew.size()];
        }

        int i = 0;
        for (Map.Entry<String,String> entry : filterNew.entrySet()) {
            System.out.println("Key = " + entry.getKey() +
                    ", Value = " + entry.getValue());
            filter[i] = entry.getKey();
            i++;
        }

//        filter[0] = "lev";
//        filter[1] = "pan";
//        filter[2] = "mcl";
//        filter[3] = "clv";
//        filter[4] = "skmj";
//        filter[5] = "mppk";
//        filter[6] = "flc";
//        filter[7] = "ssc";
//        filter[8] = "dkpk";
//        filter[9] = "mccr";
    }

    private void filterListSetup(List<String> filters, List<Integer> filterForIndexRV) {
        this.filters = filters;
        this.filtersIndex = filterForIndexRV;
        workListFilterAdapter = new WorkListFilterAdapter(this, this, this.filters, this.filtersIndex);
        filterListRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        filterListRV.setAdapter(workListFilterAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void search() {
        Log.d(TAG, "click drawable Right");
        int currentPos = viewPager.getCurrentItem();
        ListWorklistFragment currentFragment = (ListWorklistFragment) pagerAdapter.getItem(currentPos);

        if (currentFragment != null) {
            String keyword = etSearch.getText().toString();
            String filter[] = procesFilter();

            currentFragment.searchAll(keyword, filter, isFiltered);
        } else {
            Log.d(TAG, "fragment null");
        }
    }

    private String[] procesFilter() {
        String result[] = new String[1];
        try {
            result = new String[filterNew.size()];
            for (int i = 0; i < filterNew.size(); i++) {
                String value = null;
                if (isFiltered) {
                    if (isFilterChecked[i]) {
                        value = filter[i].toUpperCase();
                    }
                } else {
                    if (filter == null) {
                        filterValueSetup();
                    }
                    value = filter[i].toUpperCase();
                }
                result[i] = value;
            }

            String debugArray = new Gson().toJson(result);
            Log.d(TAG, "debugArray: " + debugArray);
            return result;
        } catch (Exception e) {
            return result;
        }

    }

    @Override
    public void onListFragmentInteraction(Task item, int position) {
        Log.d(TAG, "onListFragmentInteraction:" + item.folioNumber);

        // Misal item.code ini isinya: CLV,
        // maka utk mengarahkan ke Activity-nya bsa dg,
        // cara sbb, agar tdk hardcode

//        try {
//            // Asumsikan nama Activitynya adl WorklistCLVActivity
//            // jadi ketika dipanggil scr dinamis akn mjd sbb
//            String activityName = "Worklist" + item.code + "Activity";
//            Intent intent = new Intent(getApplicationContext(),
//                    Class.forName(activityName));
//            startActivity(intent);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        if (item.folioNumber.startsWith(WorklistCode.LEV)) {
            Intent intent = new Intent(this, LevDetailActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            startActivity(intent);
        } else if (item.folioNumber.startsWith(WorklistCode.CLV)) {
//            Toast.makeText(this, "Jenis dokumen ini dalam proses pengembangan.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ClvActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            startActivity(intent);
        } else if (item.folioNumber.startsWith(WorklistCode.MCL)
            || item.folioNumber.startsWith(WorklistCode.FLC) || item.folioNumber.startsWith(WorklistCode.OPC)) {
//            Toast.makeText(this, "Jenis dokumen ini dalam proses pengembangan.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MclDetailActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            startActivity(intent);
        } else if (item.folioNumber.startsWith(WorklistCode.MPPK)) {
//            Toast.makeText(this, "Jenis dokumen ini dalam proses pengembangan.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MppkActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            startActivity(intent);
//        } else if (item.folioNumber.startsWith(WorklistCode.SKMJ)) {
//            startActivity(new Intent(this, SkmjDetailReviewApprovalActivity.class));
        } else if (item.folioNumber.startsWith(WorklistCode.RMJ) || item.folioNumber.startsWith(WorklistCode.SKMJ)) { //RMJ
//             || item.folioNumber.startsWith(WorklistCode.SKMJ
            if (item.karyawan.personalNum == null) {
                Toast.makeText(this, "Personal Number NULL. Please contact Admin.",
                        Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(this, RmjDetailApprovalActivity.class);
                intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
                intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
                intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
                intent.putExtra(Constants.KEY_K2ACTION, item.action);
                startActivity(intent);
            }
        } else if (item.folioNumber.startsWith(WorklistCode.SKET)) {
            Intent intent = new Intent(this, SuratKetVisaHRApprovalActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            startActivity(intent);
        } else if (item.folioNumber.startsWith(WorklistCode.PAN)) {
//            Toast.makeText(this, "Jenis dokumen ini dalam proses pengembangan.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, PanDetailActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            startActivity(intent);
        } else if (item.folioNumber.startsWith(WorklistCode.MCR)) {
            Intent intent = new Intent(this, MCRActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            startActivity(intent);
        } else if (item.folioNumber.startsWith(WorklistCode.AHE)) {
            Intent intent = new Intent(this, AHEActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Jenis dokumen ini dalam proses pengembangan.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onListFragmentInteraction(TaskPending item, int position) {
        Log.d(TAG, "onListFragmentInteraction:" + item.folioNumber);

        // Misal item.code ini isinya: CLV,
        // maka utk mengarahkan ke Activity-nya bsa dg,
        // cara sbb, agar tdk hardcode

//        try {
//            // Asumsikan nama Activitynya adl WorklistCLVActivity
//            // jadi ketika dipanggil scr dinamis akn mjd sbb
//            String activityName = "Worklist" + item.code + "Activity";
//            Intent intent = new Intent(getApplicationContext(),
//                    Class.forName(activityName));
//            startActivity(intent);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        if (item.folioNumber.startsWith(WorklistCode.LEV)) {
            Intent intent = new Intent(this, LevDetailActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Pending");
            startActivity(intent);
        } else if (item.folioNumber.startsWith(WorklistCode.CLV)) {
//            Toast.makeText(this, "Jenis dokumen ini dalam proses pengembangan.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ClvActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Pending");
            startActivity(intent);
        } else if (item.folioNumber.startsWith(WorklistCode.MCL)
                || item.folioNumber.startsWith(WorklistCode.FLC) || item.folioNumber.startsWith(WorklistCode.OPC)) {
//            Toast.makeText(this, "Jenis dokumen ini dalam proses pengembangan.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MclDetailActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Pending");
            startActivity(intent);
        } else if (item.folioNumber.startsWith(WorklistCode.MPPK)) {
//            Toast.makeText(this, "Jenis dokumen ini dalam proses pengembangan.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MppkActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Pending");
            startActivity(intent);
//        } else if (item.folioNumber.startsWith(WorklistCode.SKMJ)) {
//            startActivity(new Intent(this, SkmjDetailReviewApprovalActivity.class));
        } else if (item.folioNumber.startsWith(WorklistCode.RMJ) || item.folioNumber.startsWith(WorklistCode.SKMJ)) { //RMJ
//             || item.folioNumber.startsWith(WorklistCode.SKMJ
            if (item.karyawan.personalNum == null) {
                Toast.makeText(this, "Personal Number NULL. Please contact Admin.",
                        Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(this, RmjDetailApprovalActivity.class);
                intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
                intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
                intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
                intent.putExtra(Constants.KEY_K2ACTION, item.action);
                intent.putExtra("type", "Pending");
                startActivity(intent);
            }
        } else if (item.folioNumber.startsWith(WorklistCode.SKET)) {
            Intent intent = new Intent(this, SuratKetVisaHRApprovalActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Pending");
            startActivity(intent);
        } else if (item.folioNumber.startsWith(WorklistCode.PAN)) {
//            Toast.makeText(this, "Jenis dokumen ini dalam proses pengembangan.", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(this, PanDetailGenericActivity.class);
            Intent intent = new Intent(this, PanDetailActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Pending");
            startActivity(intent);
        } else if (item.folioNumber.startsWith(WorklistCode.MCR)) {
            Intent intent = new Intent(this, MCRActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Pending");
            startActivity(intent);
        } else if (item.folioNumber.startsWith(WorklistCode.AHE)) {
            Intent intent = new Intent(this, AHEActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Pending");
            startActivity(intent);
        } else {
            Toast.makeText(this, "Jenis dokumen ini dalam proses pengembangan.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onListFragmentInteraction(TaskRejected item, int position) {
        Log.d(TAG, "onListFragmentInteraction:" + item.folioNumber);

        // Misal item.code ini isinya: CLV,
        // maka utk mengarahkan ke Activity-nya bsa dg,
        // cara sbb, agar tdk hardcode

//        try {
//            // Asumsikan nama Activitynya adl WorklistCLVActivity
//            // jadi ketika dipanggil scr dinamis akn mjd sbb
//            String activityName = "Worklist" + item.code + "Activity";
//            Intent intent = new Intent(getApplicationContext(),
//                    Class.forName(activityName));
//            startActivity(intent);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        if (item.folioNumber.startsWith(WorklistCode.LEV)) {
            Intent intent = new Intent(this, LevDetailActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Rejected");
            startActivity(intent);
        } else if (item.folioNumber.startsWith(WorklistCode.CLV)) {
//            Toast.makeText(this, "Jenis dokumen ini dalam proses pengembangan.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ClvActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Rejected");
            startActivity(intent);
        } else if (item.folioNumber.startsWith(WorklistCode.MCL)
                || item.folioNumber.startsWith(WorklistCode.FLC) || item.folioNumber.startsWith(WorklistCode.OPC)) {
//            Toast.makeText(this, "Jenis dokumen ini dalam proses pengembangan.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MclDetailActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Rejected");
            startActivity(intent);
        } else if (item.folioNumber.startsWith(WorklistCode.MPPK)) {
//            Toast.makeText(this, "Jenis dokumen ini dalam proses pengembangan.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MppkActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Rejected");
            startActivity(intent);
//        } else if (item.folioNumber.startsWith(WorklistCode.SKMJ)) {
//            startActivity(new Intent(this, SkmjDetailReviewApprovalActivity.class));
        } else if (item.folioNumber.startsWith(WorklistCode.RMJ) || item.folioNumber.startsWith(WorklistCode.SKMJ)) { //RMJ
//             || item.folioNumber.startsWith(WorklistCode.SKMJ
            if (item.karyawan.personalNum == null) {
                Toast.makeText(this, "Personal Number NULL. Please contact Admin.",
                        Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(this, RmjDetailApprovalActivity.class);
                intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
                intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
                intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
                intent.putExtra(Constants.KEY_K2ACTION, item.action);
                intent.putExtra("type", "Rejected");
                startActivity(intent);
            }
        } else if (item.folioNumber.startsWith(WorklistCode.SKET)) {
            Intent intent = new Intent(this, SuratKetVisaHRApprovalActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Rejected");
            startActivity(intent);
        } else if (item.folioNumber.startsWith(WorklistCode.PAN)) {
//            Toast.makeText(this, "Jenis dokumen ini dalam proses pengembangan.", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(this, PanDetailGenericActivity.class);
            Intent intent = new Intent(this, PanDetailActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Rejected");
            startActivity(intent);
        } else if (item.folioNumber.startsWith(WorklistCode.MCR)) {
            Intent intent = new Intent(this, MCRActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Rejected");
            startActivity(intent);
        } else if (item.folioNumber.startsWith(WorklistCode.AHE)) {
            Intent intent = new Intent(this, AHEActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Rejected");
            startActivity(intent);
        } else {
            Toast.makeText(this, "Jenis dokumen ini dalam proses pengembangan.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onListFragmentInteraction(TaskApproval item, int position) {
        Log.d(TAG, "onListFragmentInteraction:" + item.folioNumber);

        // Misal item.code ini isinya: CLV,
        // maka utk mengarahkan ke Activity-nya bsa dg,
        // cara sbb, agar tdk hardcode

//        try {
//            // Asumsikan nama Activitynya adl WorklistCLVActivity
//            // jadi ketika dipanggil scr dinamis akn mjd sbb
//            String activityName = "Worklist" + item.code + "Activity";
//            Intent intent = new Intent(getApplicationContext(),
//                    Class.forName(activityName));
//            startActivity(intent);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        if (item.folioNumber.startsWith(WorklistCode.LEV)) {
            Intent intent = new Intent(this, LevDetailActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Approved");
            startActivity(intent);
        } else if (item.folioNumber.startsWith(WorklistCode.CLV)) {
//            Toast.makeText(this, "Jenis dokumen ini dalam proses pengembangan.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ClvActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Approved");
            startActivity(intent);
        } else if (item.folioNumber.startsWith(WorklistCode.MCL)
                || item.folioNumber.startsWith(WorklistCode.FLC) || item.folioNumber.startsWith(WorklistCode.OPC)) {
//            Toast.makeText(this, "Jenis dokumen ini dalam proses pengembangan.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MclDetailActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Approved");
            startActivity(intent);
        } else if (item.folioNumber.startsWith(WorklistCode.MPPK)) {
//            Toast.makeText(this, "Jenis dokumen ini dalam proses pengembangan.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MppkActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Approved");
            startActivity(intent);
//        } else if (item.folioNumber.startsWith(WorklistCode.SKMJ)) {
//            startActivity(new Intent(this, SkmjDetailReviewApprovalActivity.class));
        } else if (item.folioNumber.startsWith(WorklistCode.RMJ) || item.folioNumber.startsWith(WorklistCode.SKMJ)) { //RMJ
//             || item.folioNumber.startsWith(WorklistCode.SKMJ
            if (item.karyawan.personalNum == null) {
                Toast.makeText(this, "Personal Number NULL. Please contact Admin.",
                        Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(this, RmjDetailApprovalActivity.class);
                intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
                intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
                intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
                intent.putExtra(Constants.KEY_K2ACTION, item.action);
                intent.putExtra("type", "Approved");
                startActivity(intent);
            }
        } else if (item.folioNumber.startsWith(WorklistCode.SKET)) {
            Intent intent = new Intent(this, SuratKetVisaHRApprovalActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Approved");
            startActivity(intent);
        } else if (item.folioNumber.startsWith(WorklistCode.PAN)) {
//            Toast.makeText(this, "Jenis dokumen ini dalam proses pengembangan.", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(this, PanDetailGenericActivity.class);
            Intent intent = new Intent(this, PanDetailActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Approved");
            startActivity(intent);
        } else if (item.folioNumber.startsWith(WorklistCode.MCR)) {
            Intent intent = new Intent(this, MCRActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Approved");
            startActivity(intent);
        } else if (item.folioNumber.startsWith(WorklistCode.AHE)) {
            Intent intent = new Intent(this, AHEActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Approved");
            startActivity(intent);
        } else {
            Toast.makeText(this, "Jenis dokumen ini dalam proses pengembangan.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onListFragmentInteraction(TaskHistory item, int position) {
        Log.d(TAG, "onListFragmentInteraction:" + item.folioNumber);

        // Misal item.code ini isinya: CLV,
        // maka utk mengarahkan ke Activity-nya bsa dg,
        // cara sbb, agar tdk hardcode

//        try {
//            // Asumsikan nama Activitynya adl WorklistCLVActivity
//            // jadi ketika dipanggil scr dinamis akn mjd sbb
//            String activityName = "Worklist" + item.code + "Activity";
//            Intent intent = new Intent(getApplicationContext(),
//                    Class.forName(activityName));
//            startActivity(intent);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        if (item.folioNumber.startsWith(WorklistCode.LEV)) {
            Intent intent = new Intent(this, LevDetailActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Approval History");
            startActivity(intent);
        } else if (item.folioNumber.startsWith(WorklistCode.CLV)) {
//            Toast.makeText(this, "Jenis dokumen ini dalam proses pengembangan.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ClvActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Approval History");
            startActivity(intent);
        } else if (item.folioNumber.startsWith(WorklistCode.MCL)
                || item.folioNumber.startsWith(WorklistCode.FLC) || item.folioNumber.startsWith(WorklistCode.OPC)) {
//            Toast.makeText(this, "Jenis dokumen ini dalam proses pengembangan.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MclDetailActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Approval History");
            startActivity(intent);
        } else if (item.folioNumber.startsWith(WorklistCode.MPPK)) {
//            Toast.makeText(this, "Jenis dokumen ini dalam proses pengembangan.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MppkActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Approval History");
            startActivity(intent);
//        } else if (item.folioNumber.startsWith(WorklistCode.SKMJ)) {
//            startActivity(new Intent(this, SkmjDetailReviewApprovalActivity.class));
        } else if (item.folioNumber.startsWith(WorklistCode.RMJ) || item.folioNumber.startsWith(WorklistCode.SKMJ)) { //RMJ
//             || item.folioNumber.startsWith(WorklistCode.SKMJ
            if (item.karyawan.personalNum == null) {
                Toast.makeText(this, "Personal Number NULL. Please contact Admin.",
                        Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(this, RmjDetailApprovalActivity.class);
                intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
                intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
                intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
                intent.putExtra(Constants.KEY_K2ACTION, item.action);
                intent.putExtra("type", "Approval History");
                startActivity(intent);
            }
        } else if (item.folioNumber.startsWith(WorklistCode.SKET)) {
            Intent intent = new Intent(this, SuratKetVisaHRApprovalActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Approval History");
            startActivity(intent);
        } else if (item.folioNumber.startsWith(WorklistCode.PAN)) {
//            Toast.makeText(this, "Jenis dokumen ini dalam proses pengembangan.", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(this, PanDetailGenericActivity.class);
            Intent intent = new Intent(this, PanDetailActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Approval History");
            startActivity(intent);
        } else if (item.folioNumber.startsWith(WorklistCode.MCR)) {
            Intent intent = new Intent(this, MCRActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Approval History");
            startActivity(intent);
        } else if (item.folioNumber.startsWith(WorklistCode.AHE)) {
            Intent intent = new Intent(this, AHEActivity.class);
            intent.putExtra(Constants.KEY_PROCESS_INSTANCE, item.processInstanceID);
            intent.putExtra(Constants.KEY_PERSONAL_NUM, item.karyawan.personalNum);
            intent.putExtra(Constants.KEY_K2SERIAL_NUM, item.k2SerailNumber);
            intent.putExtra(Constants.KEY_K2ACTION, item.action);
            intent.putExtra("type", "Approval History");
            startActivity(intent);
        } else {
            Toast.makeText(this, "Jenis dokumen ini dalam proses pengembangan.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showFilterDialog() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_apply_filter);
        dialog.getWindow().setLayout((6 * width)/7, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();

        View tvApply = dialog.findViewById(R.id.tvApply);
        View tvCancel = dialog.findViewById(R.id.tvCancel);
        RecyclerView rvFilter = dialog.findViewById(R.id.filterRV);

        if (filter == null) {
            filterValueSetup();
        }

        if (isFilterChecked == null) {
            isFilterChecked = new boolean[filterNew.size()];
        }

        workListFilterRVAdapter = new WorkListFilterRVAdapter(this, filter, this);
        rvFilter.setLayoutManager(new LinearLayoutManager(this));
        rvFilter.setAdapter(workListFilterRVAdapter);

//        cbLev = (CheckBox) dialog.findViewById(R.id.cbLev);
//        cbPan = (CheckBox) dialog.findViewById(R.id.cbPan);
//        cbMcl = (CheckBox) dialog.findViewById(R.id.cbMcl);
//        cbClv = (CheckBox) dialog.findViewById(R.id.cbClv);
//        cbSkmj = (CheckBox) dialog.findViewById(R.id.cbSkmj);
//
//        cbMppk = (CheckBox) dialog.findViewById(R.id.cbMppk);
//        cbFlc = (CheckBox) dialog.findViewById(R.id.cbFlc);
//        cbSsc = (CheckBox) dialog.findViewById(R.id.cbSsc);
//        cbDpkp = (CheckBox) dialog.findViewById(R.id.cbDpkp);
//        cbMccr = (CheckBox) dialog.findViewById(R.id.cbMCCR);
//
//        cbLev.setChecked(isFilterChecked[0]);
//        cbPan.setChecked(isFilterChecked[1]);
//        cbMcl.setChecked(isFilterChecked[2]);
//        cbClv.setChecked(isFilterChecked[3]);
//        cbSkmj.setChecked(isFilterChecked[4]);
//
//        cbMppk.setChecked(isFilterChecked[5]);
//        cbFlc.setChecked(isFilterChecked[6]);
//        cbSsc.setChecked(isFilterChecked[7]);
//        cbDpkp.setChecked(isFilterChecked[8]);
//        cbMccr.setChecked(isFilterChecked[9]);

        tvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                llLev.setVisibility(cbLev.isChecked() ? View.VISIBLE : View.GONE);
//                llPan.setVisibility(cbPan.isChecked() ? View.VISIBLE : View.GONE);
//                llMcl.setVisibility(cbMcl.isChecked() ? View.VISIBLE : View.GONE);
//                llSkmj.setVisibility(cbSkmj.isChecked() ? View.VISIBLE : View.GONE);
//                llClv.setVisibility(cbClv.isChecked() ? View.VISIBLE : View.GONE);

//                isFilterChecked[0] = cbLev.isChecked();
//                isFilterChecked[1] = cbPan.isChecked();
//                isFilterChecked[2] = cbMcl.isChecked();
//                isFilterChecked[3] = cbClv.isChecked();
//                isFilterChecked[4] = cbSkmj.isChecked();
//
//                isFilterChecked[5] = cbMppk.isChecked();
//                isFilterChecked[6] = cbFlc.isChecked();
//                isFilterChecked[7] = cbSsc.isChecked();
//                isFilterChecked[8] = cbDpkp.isChecked();
//                isFilterChecked[9] = cbMccr.isChecked();

                List<String> filterForRV = new ArrayList<>();
                List<Integer> filterForIndexRV = new ArrayList<>();
                isFiltered = false;
                for (int i = 0; i < filter.length; i++) {
                    if (isFilterChecked[i]) {
                        filterForRV.add(filter[i].toUpperCase());
                        filterForIndexRV.add(i);
                        isFiltered = true;
                    }
                }

                filterListSetup(filterForRV, filterForIndexRV);

                search();
                dialog.dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void buildAlert() {
        this.alertDialog = new AlertDialog.Builder(this)
                .setNeutralButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
    }

    public AlertDialog getAlertDialog() {
        return alertDialog;
    }

    public AlertDialog getLoading() {
        return loading;
    }

    @Override
    public void onDeleteFilter(int position) {
        isFilterChecked[position] = false;
    }

    @Override
    public void onRefilter() {
        if (filterListRV.getAdapter().getItemCount() == 0) {
            isFiltered = false;
        }
        search();
    }

    @Override
    public void filterChecked(int position, boolean isChecked) {
        isFilterChecked[position] = isChecked;
    }
}