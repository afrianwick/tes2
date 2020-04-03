package com.pertamina.portal.iam.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pertamina.portal.core.activity.BackableNoActionbarActivity;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.core.utils.StringUtils;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.adapters.CountryAdditionalDestinationAdapter;
import com.pertamina.portal.iam.fragments.RequestLeaveFragment;
import com.pertamina.portal.iam.interfaces.AdditionalDestinationView;
import com.pertamina.portal.iam.utils.KeyboardUtils;

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

public class LeaveReqAddDestinationActivity extends BackableNoActionbarActivity implements AdditionalDestinationView {

    private Button saveButton;
    private TextInputEditText countryTIET, cityTIET, startDateTIET, endDateTIET, noteTIET;
    private AlertDialog alertDialog;
    private Calendar c;
    public static String CITY = "city", COUNTRY = "country", COUNTRY_ID = "country_id", START_DATE = "start_date", END_DATE = "end_date", NOTE = "note";
    public static int ADDITIONAL_DESTINATION_RESULT = 1010;
    private boolean isStartSelected = false;
    private String startDate, endDate;
    private List<String> countries, countryIDs;
    private PortalApiInterface restApi;
    private View dialogCountryView;
    private ConstraintLayout dialogCountryContainerCL;
    private AlertDialog loading;
    private String countryID;
    private DatePickerDialog datePicker;
    private String choosenDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_req_add_destination);
        super.onCreateBackable(this, R.id.ivBack);

        saveButton = findViewById(R.id.addDestinationSaveButton);
        countryTIET = findViewById(R.id.addDestinationCountryTIET);
        cityTIET = findViewById(R.id.addDestinationCityTIET);
        startDateTIET = findViewById(R.id.addDestinationStartDateTIET);
        endDateTIET = findViewById(R.id.addDestinationEndDateTIET);
        noteTIET = findViewById(R.id.addDestinationNotesTIET);
        dialogCountryView = findViewById(R.id.additionalDestinationDialogCountryView);
        dialogCountryContainerCL = dialogCountryView.findViewById(R.id.dialogCountryContainerCL);

        loading = new SpotsDialog.Builder().setContext(this).build();

        restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);

        datePickerSetup();

        startDateTIET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerSetup();
                if (RequestLeaveFragment.startDate != null) {
                    datePicker.getDatePicker().setMinDate(RequestLeaveFragment.startDate.getTime());
                }
                choosenDate = "start";
                datePicker.show();
            }
        });

        endDateTIET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosenDate = "to";
                datePicker.show();
            }
        });

        countryTIET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dialogCountrySetup();
                } catch (NullPointerException e) {
                    alertDialog.setMessage("Tidak ada pilihan country!");
                    alertDialog.show();
                }
            }
        });

        KeyboardUtils.setupUI(this, findViewById(R.id.addDestinationParentLL));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countryTIET.getText().toString().equals("") ||
                        cityTIET.getText().toString().equals("") ||
                        startDateTIET.getText().toString().equals("") ||
                        endDateTIET.getText().toString().equals("")) {
                    alertDialog.setMessage("Lengkapi data!");
                    alertDialog.show();
                    return;
                }

                try {
                    SimpleDateFormat dateBackend = new SimpleDateFormat("yyyyMMdd");
                    SimpleDateFormat dateFrontend = new SimpleDateFormat("dd MMM yyyy");

                    Date dateS = dateFrontend.parse(startDateTIET.getText().toString());
                    Date dateE = dateFrontend.parse(endDateTIET.getText().toString());

                    Intent intent = new Intent();
                    intent.putExtra(CITY, cityTIET.getText().toString());
                    intent.putExtra(COUNTRY, countryTIET.getText().toString());
                    intent.putExtra(START_DATE, startDate);
                    intent.putExtra(END_DATE, endDate);
                    intent.putExtra(NOTE, noteTIET.getText().toString());
                    intent.putExtra(COUNTRY_ID, countryID);
                    setResult(ADDITIONAL_DESTINATION_RESULT, intent);
                    finish();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        getDataCountries();
    }

    public void buildAlert() {
        alertDialog = new AlertDialog.Builder(this)
                .setNeutralButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
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
                        Toast.makeText(LeaveReqAddDestinationActivity.this,
                                "Date Start must be before Date End",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        startDateTIET.setText(sDate);
                        endDateTIET.setText("");
                        startDate = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
//                        dateFrom = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
//                        datePicker.getDatePicker().setMinDate(0);
                        datePicker.getDatePicker().setMinDate(c.getTimeInMillis());
                    }
                } else {
                    if (c.getTimeInMillis() < datePicker.getDatePicker().getMinDate()) {
                        Toast.makeText(LeaveReqAddDestinationActivity.this,
                                "Date End must be after Date Start",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        endDateTIET.setText(sDate);
                        endDate = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
//                        datePicker.getDatePicker().setMaxDate(0);
//                        datePicker.getDatePicker().setMaxDate(c.getTimeInMillis());
                    }
                }
            }
        };

        datePicker = new DatePickerDialog(LeaveReqAddDestinationActivity.this, dateSetListener, year, month, day);
    }

//    private void calendarSetup(final TextInputEditText tvDate, final boolean isStartDate) {
//        c = Calendar.getInstance();
//        int mYear = c.get(Calendar.YEAR);
//        int mMonth = c.get(Calendar.MONTH);
//        int mDay = c.get(Calendar.DAY_OF_MONTH);
//
//        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
//                new DatePickerDialog.OnDateSetListener() {
//
//                    @Override
//                    public void onDateSet(DatePicker view, int year,
//                                          int monthOfYear, int dayOfMonth) {
//                        try {
//                            Date date = new SimpleDateFormat("ddMMyyyy").parse(String.valueOf(dayOfMonth)+String.valueOf(monthOfYear+1)+String.valueOf(year));
//                            String formattedDate = new SimpleDateFormat("dd MMM yyyy").format(date);
//                            tvDate.setText(formattedDate);
//                            if (isStartDate) {
//                                isStartSelected = true;
//                                endDateTIET.setText("");
//                                startDate = new SimpleDateFormat("dd-MM-yyyy").parse(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                            }
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, mYear, mMonth, mDay);
//
//        if (isStartSelected && !isStartDate) {
//            c.setTimeInMillis(startDate.getTime());
//            c.add(Calendar.DATE, 1);
//            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis() - 1000);
//        } else {
//            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//        }
//
//        datePickerDialog.show();
//    }

    private void dialogCountrySetup() {
        ConstraintLayout dialogCountryContainer = dialogCountryView.findViewById(R.id.dialogCountryContainerCL);
        RecyclerView dialogCountryRV = dialogCountryView.findViewById(R.id.dialogCountryRV);
        EditText dialogCountrySearchET = dialogCountryView.findViewById(R.id.dialogCountrySearcET);

        dialogCountryView.setVisibility(View.VISIBLE);

        final CountryAdditionalDestinationAdapter countryAdapter = new CountryAdditionalDestinationAdapter(LeaveReqAddDestinationActivity.this, this, countries, countryIDs);
        dialogCountryRV.setLayoutManager(new LinearLayoutManager(this));
        dialogCountryRV.setAdapter(countryAdapter);

        dialogCountryContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCountryView.setVisibility(View.GONE);
            }
        });

        dialogCountrySearchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (countries == null || countries.size() == 0) {
                    return;
                }

                if (charSequence.length() == 0) {
                    countryAdapter.updateList(countries, countryIDs);
                } else {
                    List<String> filterData = new ArrayList<>();
                    List<String> filterCountryID = new ArrayList<>();
                    for (int ii = 0; ii < countries.size(); ii++) {
                        if (countries.get(ii).toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            filterData.add(countries.get(ii));
                            filterCountryID.add(countryIDs.get(ii));
                        }
                    }

                    countryAdapter.updateList(filterData, filterCountryID);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (dialogCountryView != null && dialogCountryView.isShown()) {
            dialogCountryView.setVisibility(View.GONE);
            return;
        }

        super.onBackPressed();
    }

    private void getDataCountries() {
        loading.show();
        restApi.getCountries("PersonalAdministrationServices", "GetReferenceData")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d("AdditionalDestination", "sip.. " + strResponse);
                                parseXml(strResponse);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            String message = "Could not get data. It might be you are Loged in from other device or your session was exiperd.";
                            if (response.code() == 401) {
                                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Login again", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        try {
                                            startActivity(new Intent(LeaveReqAddDestinationActivity.this,
                                                    Class.forName("com.pertamina.portal.activity.LoginActivity")));
                                            finish();
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }

                            alertDialog.setMessage(message);

                            if (!alertDialog.isShowing()) {
                                alertDialog.show();
                            }
                        }

                        loading.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        Log.d("AdditionalDestination", "onFailure..");
                        t.printStackTrace();
                    }
                });
    }

    public void parseXml(String strXml){
        DocumentBuilderFactory factory;
        DocumentBuilder builder;
        Document dom;

        try {
            InputStream is = new ByteArrayInputStream(strXml.getBytes("utf-8"));
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            dom = builder.parse(is);

            NodeList nodeListError = dom.getElementsByTagName("ReturnMessage");

            if (dom.getElementsByTagName("ReturnObject") != null) {
                NodeList nodeListSuccess = dom.getElementsByTagName("ReturnObject");

                if (nodeListSuccess.getLength() > 0) {
                    parseJsonCountries(nodeListSuccess.item(0).getTextContent());
                }
            } else {
                for (int i = 0; i < nodeListError.getLength(); i++) {
                    String strError = nodeListError.item(i).getTextContent();
                    String message = "Could not get data due to:" + strError;

                    if (strError.contains("401")) {
                        message = "Could not get data. It might be you are Loged in from other device or your session was exiperd.";
                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Login again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    startActivity(new Intent(LeaveReqAddDestinationActivity.this,
                                            Class.forName("com.pertamina.portal.activity.LoginActivity")));
                                    finish();
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    alertDialog.setMessage(message);

                    if (!alertDialog.isShowing()) {
                        alertDialog.show();
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void parseJsonCountries(String strJson) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);
        JsonArray jsonArray = jsonObject.getAsJsonArray("Table0");
        countries = new ArrayList<>();
        countryIDs = new ArrayList<>();

        if (jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jo = jsonArray.get(i).getAsJsonObject();
                countries.add(jo.get("PLANDXM").getAsString());
                countryIDs.add(jo.get("PLAND1M").getAsString());
            }

            Log.d("datanyaada", "ada");
        } else {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDestinationCountryItemClicked(String country, String countryID) {
        this.countryID = countryID;
        dialogCountryView.setVisibility(View.GONE);
        countryTIET.setText(country);
    }
}
