package com.pertamina.portal.iam.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pertamina.portal.core.interfaces.OnSuccessListener;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.PrefUtils;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.core.utils.StringUtils;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.ReportGratifikasiActivity;
import com.pertamina.portal.iam.activity.ReqSuratVisiActivity;
import com.pertamina.portal.iam.adapters.ReportGratifikasiAdapter;
import com.pertamina.portal.iam.models.ReportGratifikasiModel;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentReportGratifikasi extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_LIST_STATUS = "list-status";
    private RecyclerView reportRV;
    private String statusList;
    private int mColumnCount = 1;
    private AlertDialog loading;
    private ReportGratifikasiAdapter reportGratifikasiAdapter;
    private AlertDialog alertDialog;

    public FragmentReportGratifikasi() {
    }

    public static FragmentReportGratifikasi newInstance(int columnCount, @NonNull String listStatus) {
        FragmentReportGratifikasi fragment = new FragmentReportGratifikasi();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putString(ARG_LIST_STATUS, listStatus);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            statusList = getArguments().getString(ARG_LIST_STATUS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_gratifikasi, container, false);

        reportRV = view.findViewById(R.id.fragmentReportGratifikasiRV);
        reportRV.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {

            loading = ((ReportGratifikasiActivity) getActivity()).getLoading();

            buildAlert();

            Calendar calendar = Calendar.getInstance();
            String startDate = new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime());
            String endDate = new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime());
            getReportGratifikasi(statusList, startDate, endDate);
        }
    }

    private String getType() {
        switch (statusList) {
            case "Pemberian":
                return "Penerima";
            case "Permintaan":
                return "Peminta";
            case "Penerimaan":
                return "Pemberi";
            default:
                return "";
        }
    }

    public void getReportGratifikasi(String type, String startDate, String endDate) {
        loading.show();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(getActivity(), 2000);
        restApi.getReportGratifikasi(type, startDate, endDate)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d("Success get history", "getAllWorklist, true _ " + response.raw().toString());
                                parseXml(strResponse, successListener);
//                                parseXml(strResponse);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            handleError(response);
                            Log.d("Error get history", "getAllWorklist, false _ " + response.raw().toString());
                        }

                        loading.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        Log.d("Failure get history", "getAllWorklist, onFailure..");
                        t.printStackTrace();
                    }
                });
    }

    OnSuccessListener successListener = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            parseJson(strJson);
        }
    };

    private void parseJson(String strJson) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);

        JsonArray jsonArray = jsonObject.getAsJsonArray("Table");
        List<ReportGratifikasiModel> reportGratifikasiModels = new ArrayList<>();

        if (jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jo = jsonArray.get(i).getAsJsonObject();
                ReportGratifikasiModel reportGratifikasiModel = new ReportGratifikasiModel();
                reportGratifikasiModel.setId(jo.get("Id").getAsString());
                reportGratifikasiModel.setDate(jo.get("Tgl_Submit").getAsString());
                reportGratifikasiModel.setValue(jo.get("Gratifikasi").getAsString());
                reportGratifikasiModel.setTglGratifikasi(jo.get("TglTransaksi").getAsString());
                reportGratifikasiModel.setAlasan(jo.get("Keterangan").getAsString());
                reportGratifikasiModel.setJenis(jo.get("Jenis").getAsString());
                reportGratifikasiModel.setNilaiGratifikasi(jo.get("Nilai").getAsString());
                reportGratifikasiModel.setJumlah(jo.get("Jumlah").getAsString());
                reportGratifikasiModel.setPeminta("");
                reportGratifikasiModels.add(reportGratifikasiModel);
            }

            reportGratifikasiAdapter = new ReportGratifikasiAdapter(getContext(), reportGratifikasiModels, getType());
            reportRV.setAdapter(reportGratifikasiAdapter);

            Log.d("datanyaada", "ada");
        } else {
//            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    public void parseXml(String strXml, OnSuccessListener listener){
        DocumentBuilderFactory factory;
        DocumentBuilder builder;
        Document dom;

        try {
            InputStream is = new ByteArrayInputStream(strXml.getBytes("utf-8"));
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            dom = builder.parse(is);

            NodeList nodeListError = dom.getElementsByTagName("ReturnMessage");
            NodeList returnType = dom.getElementsByTagName("ReturnType");
            String strReturnType = returnType.item(0).getTextContent();

            if (strReturnType.equalsIgnoreCase("S")) {
                NodeList nodeListSuccess = dom.getElementsByTagName("ReturnObject");

                if (nodeListSuccess.getLength() > 0) {
                    listener.onSuccess(nodeListSuccess.item(0).getTextContent());
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
                                    startActivity(new Intent(getActivity(),
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
            }
        } catch(Exception e){
            e.printStackTrace();
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
                        startActivity(new Intent(getActivity(),
                                Class.forName("com.pertamina.portal.activity.LoginActivity")));
                        getActivity().finish();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        if (alertDialog == null) {
            this.alertDialog = new AlertDialog.Builder(getActivity())
                    .setNeutralButton("close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            getActivity().finish();
                        }
                    })
                    .create();
        }

        alertDialog.setMessage(message);

        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }
}
