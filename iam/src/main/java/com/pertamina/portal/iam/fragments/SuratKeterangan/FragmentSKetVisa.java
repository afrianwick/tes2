package com.pertamina.portal.iam.fragments.SuratKeterangan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pertamina.portal.core.fragments.BaseFragmentApi;
import com.pertamina.portal.core.interfaces.OnSuccessListener;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.PrefUtils;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.HistorySuratKetActivity;
import com.pertamina.portal.iam.activity.MyWorklistActivity;
import com.pertamina.portal.iam.adapters.HistorySuratKeteranganAdapter;
import com.pertamina.portal.iam.interfaces.FragmentSKetVisaView;
import com.pertamina.portal.iam.models.HistorySuratKeteranganModel;
import com.pertamina.portal.iam.utils.ErrorMessage;
import com.pertamina.portal.iam.utils.SuccessMessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentSKetVisa extends BaseFragmentApi implements FragmentSKetVisaView {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_LIST_STATUS = "list-status";
    private RecyclerView sketRV;
    private String statusList;
    private int mColumnCount = 1;
    private AlertDialog loading;
    private HistorySuratKeteranganAdapter historySuratKeteranganAdapter;
    private String documentName;
    private LinearLayout historySketLL;

    public FragmentSKetVisa() {
    }

    public static FragmentSKetVisa newInstance(int columnCount, @NonNull String listStatus) {
        FragmentSKetVisa fragment = new FragmentSKetVisa();
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
        View view = inflater.inflate(R.layout.fragment_history_surat_keterangan, container, false);

        sketRV = view.findViewById(R.id.fragmentSuratKeteranganRV);
        historySketLL = view.findViewById(R.id.historySketLL);
        sketRV.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {

            loading = ((HistorySuratKetActivity) getActivity()).getLoading();

            loading.show();

            if (statusList.equalsIgnoreCase("visa")) {
                getVisa();
            } else {
                getNonVisa();
            }
        }
    }

    private void getNonVisa() {
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(getActivity(), 2000);
        restApi.getNonVisa("notificationletterservices", "GetList", PrefUtils.Build(getContext())
                .getPref().getString(Constants.KEY_PERSONAL_NUM, ""))
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

    private void getVisa() {
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(getActivity(), 2000);
        restApi.getVisa("notificationletterservices", "GetList", PrefUtils.Build(getContext())
                .getPref().getString(Constants.KEY_PERSONAL_NUM, ""))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d("Success get history", "getAllWorklist, true _ " + response.raw().toString());
                                parseXml(strResponse, successListener);
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

        JsonArray jsonArray = jsonObject.getAsJsonArray("Table0");
        List<HistorySuratKeteranganModel> historySuratKeteranganModelList = new ArrayList<>();

        if (jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jo = jsonArray.get(i).getAsJsonObject();
                HistorySuratKeteranganModel historySuratKeteranganModel = new HistorySuratKeteranganModel();
                historySuratKeteranganModel.setId("");
                historySuratKeteranganModel.setName(jo.get("DocumentName").getAsString());
                historySuratKeteranganModel.setType(jo.get("DocumentType").getAsString());
                historySuratKeteranganModel.setApprovalDate(jo.get("TMT").getAsString());
                historySuratKeteranganModel.setExpiredDate(jo.get("Endda").getAsString());
                historySuratKeteranganModel.setPersonID(jo.get("PersonID").getAsString());
                historySuratKeteranganModelList.add(historySuratKeteranganModel);
            }

            historySuratKeteranganAdapter = new HistorySuratKeteranganAdapter(getContext(), this, historySuratKeteranganModelList);
            sketRV.setAdapter(historySuratKeteranganAdapter);

            Log.d("datanyaada", "ada");
        } else {
//            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        }
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

    private void getSketPDF(String documentName, String personID) {
        loading.show();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(getActivity(), 2000);
        restApi.getPDFSket("EMPLOYEE_HTML_DOC", "A4", personID, documentName)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d("Success get history", "getAllWorklist, true _ " + response.raw().toString());
                                parseXml(strResponse, successListenerPDF);
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

    OnSuccessListener successListenerPDF = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            parseJsonPDF(strJson);
        }
    };

    private void parseJsonPDF(String strJson) {
        try {
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "pertamina-i-am-sket");
            folder.mkdir();

            File pdfFile = new File(folder, documentName + ".pdf");
            byte[] pdfAsBytes = Base64.decode(strJson, 0);
            FileOutputStream os = new FileOutputStream(pdfFile, false);
            os.write(pdfAsBytes);
            os.flush();
            os.close();
            SuccessMessage.successMessage(getContext(), historySketLL, "File Tersimpan di folder pertamina-i-am-sket pada internal storage");
//            openFolder(getContext());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void openFolder(Context context) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
                +  File.separator + "pertamina-i-am-sket" + File.separator);
        intent.setDataAndType(uri, "*/*");
        context.startActivity(Intent.createChooser(intent, "Open folder"));
    }

    @Override
    public void onSketItemClicked(String documentName, String personID) {
        this.documentName = documentName;
        getSketPDF(documentName, personID);
    }
}
