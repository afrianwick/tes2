package com.pertamina.portal.iam.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pertamina.portal.core.activity.BackableNoActionbarActivity;
import com.pertamina.portal.core.interfaces.OnSuccessListener;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.PrefUtils;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.adapters.SearchReviewerAdapter;
import com.pertamina.portal.iam.interfaces.SearchEmployeeView;
import com.pertamina.portal.iam.models.Karyawan;
import com.pertamina.portal.iam.models.ReviewerModel;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveReqAddReviewerActivity extends BackableNoActionbarActivity implements SearchEmployeeView {

    AppCompatAutoCompleteTextView textView;
    private SearchReviewerAdapter searchReviewerAdapter;
    private String TAG = "searchEmployee";
    private AlertDialog loading, alertDialog;
    private RecyclerView reviewerRV;
    private ImageView searchIV, searchDeleteIV;
    private Button saveDataButton;
    private ReviewerModel reviewerModel;
    public static String REVIEWER_NAME = "name", REVIEWER_POSITION = "position", REVIEWER_AD_USERNAME = "ad_username", REVIEWER_PERSONAL_NUMBER = "personal_number";
    public static int REVIEWER_RESULT = 3030;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_reviewer);
        super.onCreateBackable(this, R.id.ivBack);

        loading = new SpotsDialog.Builder().setContext(this).build();

        textView = (AppCompatAutoCompleteTextView) findViewById(R.id.etSearch);
        reviewerRV = findViewById(R.id.searchReviewerRV);
        searchIV = findViewById(R.id.searchReviewerIV);
        saveDataButton = findViewById(R.id.searchReviewerSaveDataButton);
        searchDeleteIV = findViewById(R.id.searchReviewerDeleteIV);

        List<Karyawan> items = new ArrayList<>();

        Karyawan karyawan = new Karyawan();
        karyawan.fullName = "Budi";

        Karyawan karyawan2 = new Karyawan();
        karyawan2.fullName = "Andi";

        items.add(karyawan);
        items.add(karyawan2);

//        SearchEmployeeAdapter adapter = new SearchEmployeeAdapter(this, R.layout.item_search_employee, items);
//
//        textView.setThreshold(3);
//        textView.setAdapter(adapter);


        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textView.getText().toString().length() > 2) {
                    getSearchEmployee();
                } else {
                    alertDialog.setMessage("Ketik minimal 3 karakter!");
                    alertDialog.show();
                }
            }
        });

        saveDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reviewerModel != null) {
                    Intent intent = new Intent();
                    intent.putExtra(REVIEWER_NAME, reviewerModel.getName());
                    intent.putExtra(REVIEWER_AD_USERNAME, reviewerModel.getAdUserName());
                    intent.putExtra(REVIEWER_PERSONAL_NUMBER, reviewerModel.getPersonelNumber());
                    intent.putExtra(REVIEWER_POSITION, reviewerModel.getPosition());
                    setResult(REVIEWER_RESULT, intent);
                    finish();
                } else {
                    alertDialog.setMessage("Pilih reviewer terlebih dahulu!");
                    alertDialog.show();
                }
            }
        });

        searchDeleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("");
            }
        });
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

    private void getSearchEmployee() {
        Log.d(TAG, "get reviewer search");

        loading.show();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);

        String tableName = "dbo.UFN_PTM_CT_SEARCH_EMPLOYEE('"+ textView.getText().toString() +"', '"+ PrefUtils.Build(this)
                .getPref().getString(Constants.KEY_USERNAME, "") +"')";

        restApi.getSearchEmployee("DataManagementServices", "GetDataFromTable", tableName)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d(TAG, "sip.. " + strResponse);
                                parseXml(strResponse, successListener);
//                                parseXml(strResponse);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d(TAG, "false _ " + response.raw().toString());
                        }

                        loading.dismiss();
//                        swiperefresh.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        Log.d(TAG, "onFailure..");
                        t.printStackTrace();
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
            NodeList returnType = dom.getElementsByTagName("ReturnType");
            String strReturnType = returnType.item(0).getTextContent();

            if (strReturnType.equalsIgnoreCase("S")) {
                NodeList nodeListSuccess = dom.getElementsByTagName("ReturnObject");

                Log.d("parsexml", "success0");
                if (nodeListSuccess.getLength() > 0) {
                    Log.d("parsexml", "success");
                    parseJson(nodeListSuccess.item(0).getTextContent());
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
                                    startActivity(new Intent(LeaveReqAddReviewerActivity.this,
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
            Log.d("errorsearchemployee", e.getMessage());
            e.printStackTrace();
        }
    }

    private void parseJson(String strJson) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);
        JsonArray jsoArray = jsonObject.getAsJsonArray("records");
        List<ReviewerModel> reviewers = new ArrayList<>();

        for (int i = 0; i < jsoArray.size(); i++) {
            JsonObject jo = jsoArray.get(i).getAsJsonObject();

            String strRecId = jo.get("recid").getAsString();
            int recid = Integer.parseInt(strRecId);

            JsonElement joPSNAMEM = jo.get("PSNAMEM");
            JsonElement joPENAMEM = jo.get("PENAMEM");
            JsonElement joADUserName = jo.get("ADUserName");
            JsonElement joPSTEXTM = jo.get("PSTEXTM");
            JsonElement joPPERNRM = jo.get("PPERNRM");

            String jeProcessPSName = "";
            String jeProcessPEName = "";
            String adUserName = "";
            String userPosition = "";
            String personelNumber = "";

            if (!joPSNAMEM.toString().equals("null")) {
                jeProcessPSName = jo.get("PSNAMEM").getAsString();
            }

            if (!joPENAMEM.toString().equals("null")) {
                jeProcessPEName = jo.get("PENAMEM").getAsString();
            }

            if (!joADUserName.toString().equals("null")) {
                adUserName = jo.get("ADUserName").getAsString();
            }

            if (!joPSTEXTM.toString().equals("null")) {
                Log.d("naonieu", joPSTEXTM.toString());
                userPosition = jo.get("PSTEXTM").getAsString();
            }

            if (!joPPERNRM.toString().equals("null")) {
                personelNumber = jo.get("PPERNRM").getAsString();
            }

            ReviewerModel reviewerModel = new ReviewerModel(
                    jeProcessPEName,
                    userPosition,
                    adUserName,
                    personelNumber
            );

            reviewers.add(reviewerModel);
        }

        searchReviewerAdapter = new SearchReviewerAdapter(this, reviewers, this);

        reviewerRV.setLayoutManager(new LinearLayoutManager(this));
        reviewerRV.setAdapter(searchReviewerAdapter);
    }

    @Override
    public void onEmployeeClicked(ReviewerModel reviewerModel, int lastPosition) {
        this.reviewerModel = reviewerModel;
        if (lastPosition != -1) {
            RadioButton radioButton = reviewerRV.findViewHolderForAdapterPosition(lastPosition).itemView.findViewById(R.id.listItemSearchReviewerRB);
            radioButton.setChecked(false);
        }
    }
}
