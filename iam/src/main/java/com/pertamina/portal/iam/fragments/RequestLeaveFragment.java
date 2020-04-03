package com.pertamina.portal.iam.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pertamina.portal.core.interfaces.OnSuccessListener;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.PrefUtils;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.core.utils.StringUtils;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.LeaveRequestActivity;
import com.pertamina.portal.iam.adapters.CountriesAdapters;
import com.pertamina.portal.iam.adapters.LeaveTypeAdapters;
import com.pertamina.portal.iam.interfaces.LeaveRequestView;
import com.pertamina.portal.iam.models.Country;
import com.pertamina.portal.iam.models.LeaveType;
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

public class RequestLeaveFragment extends Fragment {

    private static final String TAG = "RequestLeaveFragment";
    private static final int ACT_TYP_REQ_LEAVETYPES = 1;
    private static final int ACT_TYP_REQ_COUNTRIES = 2;
    private static final int ACT_TYP_REQ_SIMULATE_LEAVE = 3;
    private DatePickerDialog datePicker;
    private EditText etDateFrom;
    private EditText etDateTo;
    public static EditText etCity;
    private String choosenDate;
    private Context context;
    private PortalApiInterface restApi;
    private AlertDialog loading;
    private AlertDialog alertDialog;
    private Spinner spLeaveType;
    private EditText etLeaveType;
    private LeaveType selectedLeaveType;
    private Spinner spCountry;
    public static EditText etCountry;
    private Country selectedCountry;
    private List<String> countries, countryIDs;
    private Button simulateButton;
    public static String attendanceorAbsenceType, attendanceorAbsenceName;
    public static CheckBox checkBox;
    private AlertDialog mAlertDialog;
    public static String dateFrom = "";
    public static String dateTo = "";
    private LinearLayout infoContainerLL;
    public static TextView leaveQuotaTV, leaveTotalTV, remainingQuotaTV;
    private boolean isStartSelected = false;
    public static Date startDate = null;
    public static EditText reasonET;
    public static String leaveQuota, leaveTotal, outOfTownLeave, remainingQuota, leaveOnProcess, forecastQuota, returnType, returnMessage;
    public static String totalNumberOfDays, superiorName, superiorPosition, nextAttendance, deductionFrom, deductionTo;
    public static String selectedCountryName, selectedCountryID;
    private ScrollView requestLeaveFragmentSV;
    private List<LeaveType> leaveTypes;
    private TextView simulateLeaveMessageTV, forecastTV, leaveOnProcessTV;
    private ConstraintLayout additionalContainer1CL;
    private LinearLayout additionalContainer1LL, additionalContainer2LL;

    private TextView reasonLabelTV, leaveTypeLabelTV, destinationCityLabelTV, destinationCountryLabelTV;

    public RequestLeaveFragment() {
        // Required empty public constructor
    }

    public static RequestLeaveFragment newInstance(String param1, String param2) {
        RequestLeaveFragment fragment = new RequestLeaveFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //         Testing purpose only!!!
//        PrefUtils.Build(getActivity())
//                .getPref()
//                .edit().putString(Constants.KEY_ACCES_TOKEN, "cerDbTjygKUswnFJ936QiNNVkPEKwua4x0nfHasLWhkWwEJBx-1Iqn0zOcjQNZT_2lHsvu3I7-uEvcKpsXPLivYMTGU8zPkNvX1tBzN5PoNk_D94Wo2QATgNEI4rh_Xmu7NQo-p9ON3rg6RPyLOpaIUB7RookQrp4KcyXC5NfFWfvrzHlVoZKFMGTcE7goqj2lCnOYpUZBfIt5jGcRheP0EueHci420XFqi0u1HffQA-2JBJn2DYQ-0Fd2iF5U_i8gZjgKtHeablddBJY2cXSgIpCthWFcAxKjrxmMnxvnX-p5vwCLeTG02AxOlAz2Fpmkc_Qs9mVF6jXt5012PMRuI4DYeSvaBeClbETT9kSeFdXtmIBgW9gvBcVFiBpToIzOIgTAPFjsiOwc4-O1GXIs45OkNa7tDfvPq4qjqQsOo5vRijKPq8qIqups_moDOp4-MN0lXhzHC9ul-uMgX0VlNw45gEgJ6_9YKEOQKXfk8MJfyWpOmWA30FEc2FYuFyfP7kDMHkQyA0Hb8jtrxJfeIiMWf_o1NwQfiuO-zOMDkmoIwxEzyXYecyecn72Ga-kFhy7KxiF-7J6fZNldiaT7_2ZRoCt-6yAr2DUtwiozxQWfnbinREp5XQ74YQYCCtwMuWdeEg_ReOpFAdmbx5x_TrrxJmHw4bsN2kfe1aFv_3vrjJVNwiB0QalqkYhGDXKm4Rp3X-waUbG1Ed4F-FS89Bv4Yz_zAyr7utP0gplk1MbZe1J4prD57lFNhznU9827L5L9x-LXvRJ_rl9yNGZyhCL1cWxpxK7VeJhh-AMohkbaFqD6oStNfDlKZsaZxs8a-iPcOmnjote4jYEn2euiC8Y0Da2uBgfU0KWripsfr-bKDxM-C9dUy_jqTuIjl7vnBaiuEZZC73QcCIwUnZyuciP-OyhkoZlueLKQkI57mCDakIkEEqYjrga6vzuuq4LrlRpzVN7mhtbs9-4b6Xdu-4h_H0Hnq87383UEuIRV5YwDhx9w7y4m-LWO2ycQH3lwjgCZ8bARN9nk2mxyg8nTCrxjz5zxPAJnlj_Xjy0XraP7IJqPQLvdCTTQnHXoPdBo191bSjkJp6ksybVWBvRezC2uqt19QJBcYqpQIFXcS2JRlxz9X3JwRuB5NKCorz52X7EzhAhCBVN7RdNMtwI7F4sthVqex5HPcJqFdU6CMfiWC7r9bUoPZ2qpcq7LE6z0RCiMTuNwYiKYC_MOKgug")
//        .apply();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_leave_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final LeaveRequestView leaveRequestView = (LeaveRequestActivity) getActivity();

        etDateFrom = view.findViewById(R.id.etDateFrom);
        additionalContainer1CL = view.findViewById(R.id.additionalContainerTV);
        additionalContainer1LL = view.findViewById(R.id.fragmentRequestLeaveDetailsAdditionalDaysContainerLL);
        additionalContainer2LL = view.findViewById(R.id.fragmentRequestLeaveDetailsAdditionalDays2ContainerLL);
        etDateTo = view.findViewById(R.id.etDateTo);
        simulateLeaveMessageTV = view.findViewById(R.id.fragmentRequestLeaveDetailsMessageTV);
        leaveOnProcessTV = view.findViewById(R.id.requestLeaveDetailLeaveLeaveOnProcessTV);
        forecastTV = view.findViewById(R.id.requestLeaveDetailLeaveForecastQuotaTV);
        context = RequestLeaveFragment.this.getContext();
        spLeaveType = (Spinner) view.findViewById(R.id.spLeaveType);
        etLeaveType = view.findViewById(R.id.etLeaveType);
        etCountry = view.findViewById(R.id.etCountry);
        spCountry = (Spinner) view.findViewById(R.id.spCountry);
        etCity = view.findViewById(R.id.etCity);
        simulateButton = view.findViewById(R.id.requestLeaveDetailsSimulateButton);
        checkBox = view.findViewById(R.id.checkBox);
        infoContainerLL = view.findViewById(R.id.requestLeaveDetailInfoContainerLL);
        leaveQuotaTV = view.findViewById(R.id.requestLeaveDetailLeaveQuotaTV);
        leaveTotalTV = view.findViewById(R.id.requestLeaveDetailLeaveTotalTV);
        remainingQuotaTV = view.findViewById(R.id.requestLeaveDetailRemainingQuotaTV);
        reasonET = view.findViewById(R.id.requestLeaveDetailReasonET);
        requestLeaveFragmentSV = view.findViewById(R.id.requestLeaveFragmentSV);
        leaveTypeLabelTV = view.findViewById(R.id.fragmentLeaveDetailLeaveTypeLabelTV);
        reasonLabelTV = view.findViewById(R.id.fragmentLeaveDetailReasonLabelTV);
        destinationCityLabelTV = view.findViewById(R.id.fragmentLeaveDetailDestinationCityLabelTV);
        destinationCountryLabelTV = view.findViewById(R.id.fragmentLeaveDetailDestinationCountryLabelTV);

        setupDatePicker();
        etDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupDatePicker();
                choosenDate = "start";
                datePicker.show();
            }
        });

        etDateTo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                choosenDate = "to";
                datePicker.show();
                return false;
            }
        });

        etLeaveType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    leaveRequestView.onLeaveTypeClicked(leaveTypes);
                } catch (NullPointerException e){

                }
            }
        });

        etCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    leaveRequestView.onDestinationCountryClicked(countries, countryIDs);
                } catch (NullPointerException e) {
                    getDataCountries();
                }
            }
        });

        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCountry = (Country) adapterView.getItemAtPosition(i);
                String str = selectedCountry.name;
                etCountry.setText(str);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        buildAlert();

        restApi = RestClient.getRetrofitAuthenticatedXML(getActivity(), 2000);
        loading = new SpotsDialog.Builder().setContext(getActivity()).build();

        getDataLeaveType();
        getDataCountries();

        simulateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etDateFrom.getText().toString().equals("") || etDateTo.getText().toString().equals("") ||
                        spLeaveType.getSelectedItemPosition() == 0 ||
                        etCountry.getText().toString().equals("") ||
                        etCity.getText().toString().equals("")) {
                    alertDialog.setMessage("Lengkapi data!");
                    alertDialog.show();

                    if (etLeaveType.getText().toString().isEmpty()) {
                        leaveTypeLabelTV.setTextColor(getResources().getColor(R.color.redDull));
                        etLeaveType.setBackground(getResources().getDrawable(R.drawable.edittext_rounded_red));
                    }

                    if (etCountry.getText().toString().isEmpty()) {
                        destinationCountryLabelTV.setTextColor(getResources().getColor(R.color.redDull));
                        etCountry.setBackground(getResources().getDrawable(R.drawable.edittext_rounded_red));
                    }

                    if (etCity.getText().toString().isEmpty()) {
                        destinationCityLabelTV.setTextColor(getResources().getColor(R.color.redDull));
                        etCity.setBackground(getResources().getDrawable(R.drawable.edittext_rounded_red));
                    }

                    if (reasonET.getText().toString().isEmpty()) {
                        reasonLabelTV.setTextColor(getResources().getColor(R.color.redDull));
                        reasonET.setBackground(getResources().getDrawable(R.drawable.edittext_rounded_red));
                    }

                    if (etDateTo.getText().toString().isEmpty()) {
                        etDateTo.setBackground(getResources().getDrawable(R.drawable.edittext_rounded_red));
                    }

                    if (etDateFrom.getText().toString().isEmpty()) {
                        etDateFrom.setBackground(getResources().getDrawable(R.drawable.edittext_rounded_red));
                    }
                } else {
                    getSimulateLeave();
                }
            }
        });

        etLeaveType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    leaveTypeLabelTV.setTextColor(getResources().getColor(R.color.greyAction));
                    etLeaveType.setBackground(getResources().getDrawable(R.drawable.edittext_rounded_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etCountry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    destinationCountryLabelTV.setTextColor(getResources().getColor(R.color.greyAction));
                    etCountry.setBackground(getResources().getDrawable(R.drawable.edittext_rounded_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    destinationCityLabelTV.setTextColor(getResources().getColor(R.color.greyAction));
                    etCity.setBackground(getResources().getDrawable(R.drawable.edittext_rounded_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        reasonET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    reasonLabelTV.setTextColor(getResources().getColor(R.color.greyAction));
                    reasonET.setBackground(getResources().getDrawable(R.drawable.edittext_rounded_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etDateFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    etDateFrom.setBackground(getResources().getDrawable(R.drawable.edittext_rounded_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etDateTo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    etDateTo.setBackground(getResources().getDrawable(R.drawable.edittext_rounded_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        KeyboardUtils.setupUI(getContext(), view.findViewById(R.id.fragmentRequestLeaveDetailsParentLL));
    }

    private void setupDatePicker() {
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
                        Toast.makeText(context,
                                "Date Start must be before Date End",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        etDateFrom.setText(sDate);
                        dateFrom = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
                        etDateTo.setText("");
                        datePicker.getDatePicker().setMinDate(c.getTimeInMillis());
                        try {
                            startDate = new SimpleDateFormat("yyyyMMdd").parse(dateFrom);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    if (c.getTimeInMillis() < datePicker.getDatePicker().getMinDate()) {
                        Toast.makeText(context,
                                "Date End must be after Date Start",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        etDateTo.setText(sDate);
                        dateTo = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
                    }
                }
            }
        };

        datePicker = new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
    }

    private void getSimulateLeave() {
        loading.show();
        restApi.getSimulateLeave("TimeManagementServices", "ValidateAbsence",
                PrefUtils.Build(context) .getPref().getString(Constants.KEY_PERSONAL_NUM, ""),
                dateFrom,
                dateTo,
                attendanceorAbsenceType,
                String.valueOf(checkBox.isChecked())).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String strResponse = response.body().string();
                        Log.d(TAG, "sip.. " + strResponse);
                        parseXml(strResponse, ACT_TYP_REQ_SIMULATE_LEAVE);
//                                parseXml(strResponse);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, "false _ " + response.raw().toString());
                }

                loading.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading.dismiss();
            }
        });
    }

    private OnSuccessListener successListener = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            Log.d(TAG, "onSuccess:strJson = " + strJson);
            parseJson(strJson);
        }
    };

    private void parseJson(String strJson) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);
        JsonArray jsoArray = jsonObject.getAsJsonArray("records");

        for (int i = 0; i < jsoArray.size(); i++) {
            JsonObject jo = jsoArray.get(i).getAsJsonObject();

//            String strRecId = jo.get("recid").getAsString();
//            int recid = Integer.parseInt(strRecId);
//
//            JsonElement joPSNAMEM = jo.get("PSNAMEM");
//            JsonElement joPENAMEM = jo.get("PENAMEM");
//            JsonElement joADUserName = jo.get("ADUserName");
//            JsonElement joPSTEXTM = jo.get("PSTEXTM");
//            JsonElement joPPERNRM = jo.get("PPERNRM");
//
//            String jeProcessPSName = "";
//            String jeProcessPEName = "";
//            String adUserName = "";
//            String userPosition = "";
//            String personelNumber = "";
//
//            if (!joPSNAMEM.toString().equals("null")) {
//                jeProcessPSName = jo.get("PSNAMEM").getAsString();
//            }
//
//            if (!joPENAMEM.toString().equals("null")) {
//                jeProcessPEName = jo.get("PENAMEM").getAsString();
//            }
//
//            if (!joADUserName.toString().equals("null")) {
//                adUserName = jo.get("ADUserName").getAsString();
//            }
//
//            if (!joPSTEXTM.toString().equals("null")) {
//                Log.d("naonieu", joPSTEXTM.toString());
//                userPosition = jo.get("PSTEXTM").getAsString();
//            }
//
//            if (!joPPERNRM.toString().equals("null")) {
//                personelNumber = jo.get("PPERNRM").getAsString();
//            }
        }
    }

    public void setDestinationCountry(String country, String countryID) {
        selectedCountryName = country;
        selectedCountryID = countryID;
        etCountry.setText(country);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void getDataLeaveType() {
        String personelNum = PrefUtils.Build(getActivity()).getPref().getString(Constants.KEY_PERSONAL_NUM, "");

        loading.show();
        restApi.getLeaveTypeCalendar("TimeManagementServices",
                "GetAttendanceAndAbsenceTypeByPersonnelNumber",
                personelNum)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d(TAG, "sip.. " + strResponse);
                                parseXml(strResponse, ACT_TYP_REQ_LEAVETYPES);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
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

    private void getDataCountries() {
        loading.show();
        restApi.getCountries("PersonalAdministrationServices", "GetReferenceData")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d(TAG, "sip.. " + strResponse);
                                parseXml(strResponse, ACT_TYP_REQ_COUNTRIES);
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
                                            startActivity(new Intent(getContext(),
                                                    Class.forName("com.pertamina.portal.activity.LoginActivity")));
                                            getActivity().finish();
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
                        Log.d(TAG, "onFailure..");
                        t.printStackTrace();
                    }
                });
    }

    public void parseXml(String strXml, int actionType){
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
                    if (actionType == ACT_TYP_REQ_LEAVETYPES) {
                        parseJsonLeave(nodeListSuccess.item(0).getTextContent());
                    } else if (actionType == ACT_TYP_REQ_COUNTRIES) {
                        parseJsonCountries(nodeListSuccess.item(0).getTextContent());
                    } else {
                        parseJsonSimulateLeave(nodeListSuccess.item(0).getTextContent());
                    }
                }
            } else {
                for (int i = 0; i < nodeListError.getLength(); i++) {
                    String strError = nodeListError.item(i).getTextContent();
                    String message = "Could not get data due to:" + strError;

                    if (strError.contains("401")) {
                        message = "Could not get data. It might be you are Loged in from other device or your session was exiperd.";
                        mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Login again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    startActivity(new Intent(getActivity(),
                                            Class.forName("com.pertamina.portal.activity.LoginActivity")));
                                    getActivity().finish();
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    mAlertDialog.setMessage(message);

                    if (!mAlertDialog.isShowing()) {
                        mAlertDialog.show();
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void parseJsonLeave(String strJson) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);
        JsonArray jsonArray = jsonObject.getAsJsonArray("Table0");
        leaveTypes = new ArrayList<>();

        if (jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jo = jsonArray.get(i).getAsJsonObject();
                LeaveType lt = new LeaveType(jo.get("PATEXTM").getAsString(), jo.get("PAWARTM").getAsString());
                leaveTypes.add(lt);
            }

        } else {
            Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
        }

    }

    private void parseJsonCountries(String strJson) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);
        JsonArray jsonArray = jsonObject.getAsJsonArray("Table0");
        List<Country> list = new ArrayList<>();
        countries = new ArrayList<>();
        countryIDs = new ArrayList<>();

        String countryIndonesia = "";
        String countryIDsIndonesia = "";

        if (jsonArray.size() > 0) {
            Country countryTmp = new Country("Please select", "");
            list.add(countryTmp);

            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jo = jsonArray.get(i).getAsJsonObject();
                if (jo.get("PLANDXM").getAsString().equalsIgnoreCase("Indonesia")) {
                    countryIndonesia = jo.get("PLANDXM").getAsString();
                    countryIDsIndonesia = jo.get("PLAND1M").getAsString();
                } else {
                    Country country = new Country(jo.get("PLANDXM").getAsString(), jo.get("PLAND1M").getAsString());
                    list.add(country);
                    countries.add(jo.get("PLANDXM").getAsString());
                    countryIDs.add(jo.get("PLAND1M").getAsString());
                }
            }

            Country country = new Country(countryIndonesia, countryIDsIndonesia);
            list.add(0, country);
            countries.add(0, countryIndonesia);
            countryIDs.add(0, countryIDsIndonesia);


            CountriesAdapters lva = new CountriesAdapters(context, android.R.layout.simple_spinner_item, list);
            spCountry.setAdapter(lva);
        } else {
            Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
        }

    }

    private void parseJsonSimulateLeave(String strJson) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);
        JsonArray jsonArray = jsonObject.getAsJsonArray("Table0");

        if (jsonArray.size() > 0) {
            Country countryTmp = new Country("Please select", "");

            JsonObject jo = jsonArray.get(0).getAsJsonObject();
            totalNumberOfDays = jo.get("TotalNumberOfDays").getAsString();
            leaveQuota = jo.get("CurrentQuota").getAsString();
            leaveTotal = jo.get("TotalNumberOfWorkingDays").getAsString();
            outOfTownLeave = jo.get("OutOfTownLeave").getAsString();
            remainingQuota = jo.get("RemainingQuota").getAsString();
            superiorName = jo.get("SuperiorName").getAsString();
            superiorPosition = jo.get("SuperiorPosition").getAsString();
            nextAttendance = jo.get("NextAttendance").getAsString();
            deductionFrom = jo.get("DeductionFrom").getAsString();
            deductionTo = jo.get("DeductionTo").getAsString();
            leaveOnProcess = jo.get("LeaveOnProcessDays").getAsString();
            forecastQuota = String.valueOf(Integer.parseInt(remainingQuota) - Integer.parseInt(leaveOnProcess));
            returnType = jo.get("ReturnType").getAsString();
            returnMessage = jo.get("ReturnMessage").getAsString();

            infoContainerLL.setVisibility(View.VISIBLE);
            leaveQuotaTV.setText(leaveQuota);
            leaveTotalTV.setText(leaveTotal);
            leaveOnProcessTV.setText(leaveOnProcess);
            forecastTV.setText(String.valueOf(Integer.parseInt(remainingQuota) - Integer.parseInt(leaveOnProcess)));
            remainingQuotaTV.setText(remainingQuota);
            additionalContainer1CL.setVisibility(View.GONE);
            additionalContainer1LL.setVisibility(View.GONE);
            FinalLeaveDetailsFragment.additionalContainer1CL.setVisibility(View.GONE);
            FinalLeaveDetailsFragment.additionalContainer1LL.setVisibility(View.GONE);
            if (checkBox.isChecked()) {
                additionalContainer1CL.setVisibility(View.VISIBLE);
                additionalContainer1LL.setVisibility(View.VISIBLE);
                FinalLeaveDetailsFragment.additionalContainer1CL.setVisibility(View.VISIBLE);
                FinalLeaveDetailsFragment.additionalContainer1LL.setVisibility(View.VISIBLE);
            }

            if (returnType.equalsIgnoreCase("E")) {
                simulateLeaveMessageTV.setBackground(getResources().getDrawable(R.drawable.button_bg_rounded_red));
                FinalLeaveDetailsFragment.messageTV.setBackground(getResources().getDrawable(R.drawable.button_bg_rounded_red));
            } else if (returnType.equalsIgnoreCase("W")) {
                simulateLeaveMessageTV.setBackground(getResources().getDrawable(R.drawable.button_bg_rounded_corners_blue));
                FinalLeaveDetailsFragment.messageTV.setBackground(getResources().getDrawable(R.drawable.button_bg_rounded_corners_blue));
            } else {
                simulateLeaveMessageTV.setBackground(getResources().getDrawable(R.drawable.button_bg_rounded_green));
                FinalLeaveDetailsFragment.messageTV.setBackground(getResources().getDrawable(R.drawable.button_bg_rounded_green));
                returnMessage = "Success! validation succeed";
            }

            simulateLeaveMessageTV.setText(returnMessage);

            FinalLeaveDetailsFragment.finalLeaveDetailsInfoContainerLL.setVisibility(View.VISIBLE);
            FinalLeaveDetailsFragment.leaveQuotaTV.setText(leaveQuota);
            FinalLeaveDetailsFragment.leaveTotalTV.setText(leaveTotal);
            FinalLeaveDetailsFragment.leaveOnProcessTV.setText(leaveOnProcess);
            FinalLeaveDetailsFragment.forecastTV.setText(forecastTV.getText().toString());
            FinalLeaveDetailsFragment.messageTV.setText(returnMessage);
            FinalLeaveDetailsFragment.remainingQuotaTV.setText(remainingQuota);
            FinalLeaveDetailsFragment.leaveTypeTV.setText(etLeaveType.getText().toString());
            FinalLeaveDetailsFragment.reasonTV.setText(reasonET.getText().toString());
            FinalLeaveDetailsFragment.leaveDateTV.setText(etDateFrom.getText().toString() + " to " + etDateTo.getText().toString());
            FinalLeaveDetailsFragment.outOfTownTV.setText(checkBox.isChecked() ? "Yes" : "No");
            FinalLeaveDetailsFragment.cityTV.setText(etCity.getText().toString());
            FinalLeaveDetailsFragment.countryTV.setText(etCountry.getText().toString());

            requestLeaveFragmentSV.post(new Runnable() {
                @Override
                public void run() {
                    requestLeaveFragmentSV.fullScroll(View.FOCUS_DOWN);
                }
            });
        } else {
            Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
        }

    }

    public void buildAlert() {
        this.alertDialog = new AlertDialog.Builder(getActivity())
                .setNeutralButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
    }

    public void setLeaveType(LeaveType leaveType) {
        selectedLeaveType = leaveType;
        attendanceorAbsenceType = selectedLeaveType.value;
        attendanceorAbsenceName = selectedLeaveType.label;
        etLeaveType.setText(leaveType.label);
    }
}
