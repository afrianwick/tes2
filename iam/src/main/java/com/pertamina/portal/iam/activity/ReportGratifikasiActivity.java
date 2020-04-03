package com.pertamina.portal.iam.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.pertamina.portal.core.utils.StringUtils;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.adapters.ReportGratifikasiViewPagerAdapter;
import com.pertamina.portal.iam.utils.DateUtils.DateUtils;
import com.pertamina.portal.iam.utils.DialogUtils.DialogUtils;
import com.pertamina.portal.iam.view.ReportGratifikasiView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import dmax.dialog.SpotsDialog;

public class ReportGratifikasiActivity extends AppCompatActivity implements ReportGratifikasiView {

    private AppCompatImageButton backIB;
    private AlertDialog loading;
    private ViewPager viewPager;
    private ReportGratifikasiViewPagerAdapter reportGratifikasiViewPagerAdapter;
    private TabLayout tabLayout;
    private String choosenDate;
    private DatePickerDialog datePicker;
    private EditText startDateTIET, endDateTIET;
    private Button applyIB;
    private String dateFrom, dateTo;
    private AlertDialog alertDialog;
    private ReportGratifikasiView reportGratifikasiView;
    private boolean isStartDate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_gratification);

        loading = new SpotsDialog.Builder().setContext(this).build();

        reportGratifikasiView = this;

        endDateTIET = findViewById(R.id.field_range_end);
        startDateTIET = findViewById(R.id.field_range_start);
        applyIB = findViewById(R.id.btn_apply);

        datePickerSetup();

        buildAlert();

        startDateTIET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                datePickerSetup();
//                choosenDate = "start";
//                datePicker.show();
                isStartDate = true;
                DialogUtils.datePicker(ReportGratifikasiActivity.this, reportGratifikasiView);
            }
        });

        endDateTIET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                choosenDate = "to";
//                datePicker.show();
                isStartDate = false;
                DialogUtils.datePicker(ReportGratifikasiActivity.this, reportGratifikasiView);
            }
        });

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.view_pager);

        reportGratifikasiViewPagerAdapter = new ReportGratifikasiViewPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(reportGratifikasiViewPagerAdapter);
        viewPager.setOffscreenPageLimit(ReportGratifikasiViewPagerAdapter.TAB_TITLES.length);
        tabLayout.setupWithViewPager(viewPager);

        applyIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startDateTIET.getText().toString().equalsIgnoreCase("") || endDateTIET.getText().toString().equalsIgnoreCase("")) {
                    alertDialog.setMessage("Pilih tanggal!");
                    alertDialog.show();
                    return;
                }
                ReportGratifikasiViewPagerAdapter.permintaan.getReportGratifikasi("Permintaan", dateFrom, dateTo);
                ReportGratifikasiViewPagerAdapter.pemberian.getReportGratifikasi("Pemberian", dateFrom, dateTo);
                ReportGratifikasiViewPagerAdapter.penerimaan.getReportGratifikasi("Penerimaan", dateFrom, dateTo);
            }
        });

        tabHandler();

        backIB = findViewById(R.id.ic_back);
        backIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void tabHandler() {
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

    private void datePickerSetup() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker dp, int i, int i1, int i2) {
                final Calendar c = Calendar.getInstance();
                c.set(i, i1, i2);

                String sDate = StringUtils.formatDateSimple(c.getTime());

                if (choosenDate.equalsIgnoreCase("start")) {
                    if (c.getTimeInMillis() > datePicker.getDatePicker().getMaxDate()) {
                        Toast.makeText(ReportGratifikasiActivity.this,
                                "Date Start must be before Date End",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        startDateTIET.setText(sDate);
                        endDateTIET.setText("");
                        dateFrom = new SimpleDateFormat("dd-MM-yyyy").format(c.getTime());
//                        datePicker.getDatePicker().setMinDate(0);
                        datePicker.getDatePicker().setMinDate(c.getTimeInMillis());
                    }
                } else {
                    if (c.getTimeInMillis() < datePicker.getDatePicker().getMinDate()) {
                        Toast.makeText(ReportGratifikasiActivity.this,
                                "Date End must be after Date Start",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        endDateTIET.setText(sDate);
                        dateTo = new SimpleDateFormat("dd-MM-yyyy").format(c.getTime());
//                        datePicker.getDatePicker().setMaxDate(0);
//                        datePicker.getDatePicker().setMaxDate(c.getTimeInMillis());
                    }
                }
            }
        };

        datePicker = new DatePickerDialog(ReportGratifikasiActivity.this, dateSetListener, year, month, day);
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

    @Override
    public void onDatePickerOKClicked(DatePicker datePicker) {
        String day = String.valueOf(datePicker.getDayOfMonth());
        String month = String.valueOf(datePicker.getMonth() + 1);
        String year = String.valueOf(datePicker.getYear());

        if (Integer.parseInt(month) < 10) {
            month = "0"+month;
        }

        DateUtils dateUtils = new DateUtils();
        if (isStartDate) {
            startDateTIET.setText(dateUtils.setInputDate("01"+month+year).setInputPattern("ddMMyyyy").setOutputPattern("dd MMM yyyy").build());
            dateFrom = dateUtils.setInputDate("01"+month+year).setInputPattern("ddMMyyyy").setOutputPattern("dd-MM-yyyy").build();
        } else {
            try {
                String date = "01/"+month+"/"+year;
                Calendar cal = Calendar.getInstance();
                cal.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(date));
                int res = cal.getActualMaximum(Calendar.DATE);
                System.out.println("Today's Date = " + cal.getTime());
                System.out.println("Last Date of the current month = " + res);
                endDateTIET.setText(dateUtils.setInputDate(res+month+year).setInputPattern("ddMMyyyy").setOutputPattern("dd MMM yyyy").build());
                dateTo = dateUtils.setInputDate(res+month+year).setInputPattern("ddMMyyyy").setOutputPattern("dd-MM-yyyy").build();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Log.d("tanggaldipilih", "01"+month+year);
    }
}
