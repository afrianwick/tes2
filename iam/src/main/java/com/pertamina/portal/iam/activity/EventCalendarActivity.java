package com.pertamina.portal.iam.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pertamina.portal.core.activity.BackableNoActionbarActivity;
import com.pertamina.portal.core.interfaces.OnSuccessListener;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.DateUtils;
import com.pertamina.portal.core.utils.PrefUtils;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.core.utils.StringUtils;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.adapters.CalendarEventAdapter;
import com.pertamina.portal.iam.models.CalendarEvent;
import com.pertamina.portal.iam.utils.DialogUtils.DialogUtils;
import com.pertamina.portal.iam.view.DatePickerInterface;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventCalendarActivity extends BackableNoActionbarActivity {

    private static final String TAG = "EventCalendarActivity";
    private Button tvSelectedMonth;
    private TextView tvSelectedDate;
    private ArrayList<CalendarEvent> listCalendarEvent;
    private CompactCalendarView compactCalendarView;
    private RecyclerView recyclerview;
    private CalendarEventAdapter adapter;
    private List<CalendarEvent> calendarEvents = new ArrayList<>();
    private AlertDialog loading;
    private AlertDialog alertDialog;
    private Button currentDateButton;
    private TextView prevYearTV, nextYearTV;
    private Calendar calendar = Calendar.getInstance();
    private ConstraintLayout eventCalendarParentCL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_calendar);
        super.onCreateBackable(this, R.id.ivBack);

        loading = getLoading();

        prevYearTV = findViewById(R.id.calendarPrevYearTV);
        nextYearTV = findViewById(R.id.calendarNextYearTV);
        currentDateButton = findViewById(R.id.personalCalendarCurrentDateButton);
        eventCalendarParentCL = findViewById(R.id.eventCalendarParentCL);
        tvSelectedMonth = findViewById(R.id.tvSelectedMonth);

        nextYearTV.setEnabled(false);
        prevYearTV.setEnabled(false);

        currentDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                compactCalendarView.setCurrentDate(Calendar.getInstance().getTime());

                Date today = new Date();

                String strDate = StringUtils.formatDateMonth(today);
                String strFullDate = StringUtils.formatDateFull(today);

                tvSelectedMonth.setText(strDate);
                tvSelectedDate.setText(strFullDate);
            }
        });

        prevYearTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.YEAR, -1);
                compactCalendarView.setCurrentDate(calendar.getTime());

                String strDate = StringUtils.formatDateMonth(calendar.getTime());
                String strFullDate = StringUtils.formatDateFull(calendar.getTime());

                tvSelectedMonth.setText(strDate);
                tvSelectedDate.setText(strFullDate);
            }
        });

        nextYearTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.YEAR, 1);
                compactCalendarView.setCurrentDate(calendar.getTime());

                String strDate = StringUtils.formatDateMonth(calendar.getTime());
                String strFullDate = StringUtils.formatDateFull(calendar.getTime());

                tvSelectedMonth.setText(strDate);
                tvSelectedDate.setText(strFullDate);
            }
        });

        tvSelectedMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtils.datePicker(EventCalendarActivity.this, new DatePickerInterface() {
                    @Override
                    public void onDatePickerOKClicked(DatePicker datePicker) {
                        datePickerSelected(datePicker);
                    }
                });
            }
        });

        buildAlert();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getCalendarEvents();
        initializeCalendar();
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

    public void initializeCalendar() {
        tvSelectedDate = (TextView) findViewById(R.id.tvSelectedDate);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        // Set first day of week to Monday, defaults to Monday so calling setFirstDayOfWeek is not necessary
        // Use constants provided by Java Calendar class
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(llm);
        adapter = new CalendarEventAdapter(EventCalendarActivity.this, calendarEvents);
        recyclerview.setAdapter(adapter);
        Date today = new Date();

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                Log.d(TAG, "Day was clicked: " + dateClicked + " with events " + events);
                String strDate = StringUtils.formatDateFull(dateClicked);

                tvSelectedDate.setText(strDate);
                adapter.clear();
                calendarEvents = new ArrayList<>();

                for (Event event : events) {
                    CalendarEvent ce = (CalendarEvent) event.getData();
                    calendarEvents.add(ce);
                }

                adapter.addList(calendarEvents);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onMonthScroll(Date date) {
                calendar.setTime(date);
                String strDate = StringUtils.formatDateMonth(date);
                tvSelectedMonth.setText(strDate);
            }
        });

        compactCalendarView.setShouldDrawDaysHeader(true);
        compactCalendarView.setBackgroundResource(R.drawable.button_bg_rounded_corners);

        String strDate = StringUtils.formatDateMonth(today);
        String strFullDate = StringUtils.formatDateFull(today);

        tvSelectedMonth.setText(strDate);
        tvSelectedDate.setText(strFullDate);
    }

    private void getCalendarEvents() {
        String personnelNumber = PrefUtils.Build(this)
                .getPref().getString(Constants.KEY_PERSONAL_NUM, "");

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -5);
        Date dateStart = cal.getTime();

        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.MONTH, 5);
        Date dateEnd = cal2.getTime();

        String strStartDate = StringUtils.formatDateNoSpace(dateStart);
        String strEndDate = StringUtils.formatDateNoSpace(dateEnd);

        loading.show();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        restApi.getCalendarEvents("TimeManagementServices",
                "GetPersonalCalendar",
                personnelNumber, "19700101", "99991231")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d(TAG, "sip.. " + strResponse);
                                parseXml(strResponse, successListener);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            handleError(response);
                            Log.d(TAG, "false _ " + response.raw().toString());
                        }

                        loading.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        Log.d(TAG, "onFailure..");
                        t.printStackTrace();
                    }
                });
    }

    public void handleError(Response<ResponseBody> response) {
        if (response.code() == 401) {
            showError("401");
        } else if (response.code() == 404) {
            showError("404");
        } else {
            try {
                showError(response.errorBody().string());
            } catch (IOException e) {
                showError(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void showError(String strError) {
        String message = "Could not get data due to:" + strError;

        if (strError.contains("401")) {
            message = "Could not get data. It might be you are Loged in from other device or your session was exiperd.";
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Login again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        startActivity(new Intent(EventCalendarActivity.this,
                                Class.forName("com.pertamina.portal.activity.LoginActivity")));
                        finish();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        if (alertDialog == null) {
            this.alertDialog = new AlertDialog.Builder(EventCalendarActivity.this)
                    .setNeutralButton("close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    })
                    .create();
        }

        alertDialog.setMessage(message);

        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }


    private void parseJson(String strJson) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);
        JsonArray jsonArray = jsonObject.getAsJsonArray("Table0");
        listCalendarEvent = new ArrayList<>();

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jo = jsonArray.get(i).getAsJsonObject();
            String strDateStart = jo.get("StartTime").getAsString();
            String strDateEnd = jo.get("EndTime").getAsString();
            String category = jo.get("EventName").getAsString();
            String name = jo.get("Keterangan").getAsString();
            Date dateStart = StringUtils.toDate(strDateStart);

            CalendarEvent ce = new CalendarEvent();
//            ce.time = String.valueOf(dateStart.getHours()) + ":" + String.valueOf(dateStart.getMinutes());
            ce.time = "00:00";
            ce.dateStart = dateStart;
            ce.dateEnd = StringUtils.toDate(strDateEnd);
            ce.category = category;
            ce.eventName = name;

            listCalendarEvent.add(ce);
        }

        for (CalendarEvent ce : listCalendarEvent) {
            if (ce.dateStart.compareTo(ce.dateEnd) == 0) {
                int resColor = 0;

                try {
                    resColor = Constants.EventColor.getEventColor(ce.category);
                    resColor = getResources().getColor(resColor);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Event event = new Event(resColor, ce.dateStart.getTime(), ce);
                compactCalendarView.addEvent(event);
            } else {
                List<Date> dateList = DateUtils.getDatesBetween(ce.dateStart, ce.dateEnd);

                for (Date date : dateList) {
                    int resColor = 0;

                    try {
                        resColor = Constants.EventColor.getEventColor(ce.category);
                        resColor = getResources().getColor(resColor);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Event event = new Event(resColor, date.getTime(), ce);
                    compactCalendarView.addEvent(event);
                }
            }
        }
    }

    OnSuccessListener successListener = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            parseJson(strJson);
        }
    };

    private void datePickerSelected(DatePicker datePicker) {
        String day = String.valueOf(datePicker.getDayOfMonth());
        String month = String.valueOf(datePicker.getMonth() + 1);
        String year = String.valueOf(datePicker.getYear());

        if (Integer.parseInt(month) < 10) {
            month = "0"+month;
        }

//        calendar.add(Calendar.YEAR, datePicker.getYear() - calendar.getTime().getYear());
        calendar.set(Calendar.YEAR, datePicker.getYear());
        calendar.set(Calendar.MONTH, Calendar.getInstance().getTime().getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getTime().getDay());
        compactCalendarView.setCurrentDate(calendar.getTime());

        String strDate = StringUtils.formatDateMonth(calendar.getTime());
        String strFullDate = StringUtils.formatDateFull(calendar.getTime());

        tvSelectedMonth.setText(strDate);
        tvSelectedDate.setText(strFullDate);

        Log.d("tanggaldipilih", String.valueOf(datePicker.getYear() - calendar.getTime().getYear()));
        Log.d("tanggaldipilih", String.valueOf(datePicker.getYear()));
        Log.d("tanggaldipilih", String.valueOf(calendar.getTime().getYear()));

    }
}
