package com.pertamina.portal.iam.activity.worklist;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pertamina.portal.core.interfaces.OnSuccessListener;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.models.paramsapi.DataApproval;
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.JsonXmlUtils;
import com.pertamina.portal.core.utils.PrefUtils;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.core.utils.StringUtils;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.PayslipActivity;
import com.pertamina.portal.iam.activity.base.BaseWorklistActivity;
import com.pertamina.portal.iam.adapters.MCLAdapter;
import com.pertamina.portal.iam.adapters.MCLDocumentAdapter;
import com.pertamina.portal.iam.adapters.TypeOfLetterDescriptionAdapter;
import com.pertamina.portal.iam.adapters.worklist.ClaimReqAdapter;
import com.pertamina.portal.iam.adapters.worklist.MCLHistoryAdapter;
import com.pertamina.portal.iam.fragments.PersonalDataFragment;
import com.pertamina.portal.iam.interfaces.LoadProfileListener;
import com.pertamina.portal.iam.interfaces.MCLView;
import com.pertamina.portal.iam.models.ClaimRequest;
import com.pertamina.portal.iam.models.CostCenter;
import com.pertamina.portal.iam.models.IamComment;
import com.pertamina.portal.iam.models.MCLDocumentModel;
import com.pertamina.portal.iam.utils.ButtonApprovalBuilder;
import com.pertamina.portal.iam.utils.FileDownloader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MclDetailActivity extends BaseWorklistActivity implements MCLView {

    private static final String TAG = "MclDetailAct";
    public static final String CLAIM_REQUEST = "claim-request";
    public static final int CLAIM_REQ_CODE = 1;
    private EditText etComment;
    private String processInstanceId;
    private String personalNum;
    private String k2SerialNumber;
    private String[] k2ActionOption;
    private AlertDialog loading;
    private TextView tvFolioNumber;
    private TextView tvLastActivity;
    private TextView mclTitleTV;
    private RecyclerView rvMcl, mclHistoryRV;
//    private Button btClaimForm;
    private ClaimRequest claimRequest;
    private MCLDocumentAdapter mclDocumentAdapter;
    private RecyclerView documentRV, mclClaimTypeRV;
    private ImageView mclClaimTypeIV, mclHistoryIV, mclClaimFormIV;
    private static final int REQUEST_CODE_WES = 1;
    private LinearLayout llSectionSeparatorTypeDesc, llMclClaimForm, llActionContainer, llMCLHistory;
    private RelativeLayout llClaimForm;
    private String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcl_detail);
        super.onCreateBackable(this, R.id.ivBack);
        super.buildAlert();

        etComment = (EditText) findViewById(R.id.etComment);
        tvFolioNumber = (TextView) findViewById(R.id.tvFolioNumber);
        tvLastActivity = (TextView) findViewById(R.id.tvLastActivity);
        rvMcl = (RecyclerView) findViewById(R.id.rvMcl);
//        btClaimForm = (Button) findViewById(R.id.btClaimForm);
        documentRV = findViewById(R.id.mclDetailRV);
        mclClaimTypeRV = findViewById(R.id.mclClaimTypeRV);
        mclClaimTypeIV = findViewById(R.id.mclMedicalClaimTypeDescIV);
        llSectionSeparatorTypeDesc = findViewById(R.id.llSectionSeparatorTypeDesc);
        llMclClaimForm = findViewById(R.id.llSectionSeparatorClaimForm);
        mclClaimFormIV = findViewById(R.id.mclClaimFormIV);
        llClaimForm = findViewById(R.id.llClaimForm);
        mclHistoryIV = findViewById(R.id.mclHistoryIV);
        llActionContainer = findViewById(R.id.llActionContainer);
        mclTitleTV = findViewById(R.id.mclTitleTV);
        mclHistoryRV = findViewById(R.id.mclHistoryRV);
        llMCLHistory = findViewById(R.id.mclHistoryLL);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);

        rvMcl.setLayoutManager(llm);

        final MCLAdapter mclAdapter = new MCLAdapter(this);
        mclClaimTypeRV.setLayoutManager(new LinearLayoutManager(this));
        mclClaimTypeRV.setAdapter(mclAdapter);

        if (getIntent().hasExtra("type")) {
            llActionContainer.setVisibility(View.GONE);
            tvLastActivity.setText(getIntent().getStringExtra("type"));
            type = getIntent().getStringExtra("type");
        }



        llSectionSeparatorTypeDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mclClaimTypeRV.isShown()) {
                    mclClaimTypeRV.setVisibility(View.GONE);
                    mclClaimTypeIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    mclClaimTypeRV.setVisibility(View.VISIBLE);
                    mclClaimTypeIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });

        llMclClaimForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rvMcl.isShown()) {
//                    btClaimForm.setVisibility(View.GONE);
                    llClaimForm.setVisibility(View.GONE);
                    rvMcl.setVisibility(View.GONE);
                    mclClaimFormIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    rvMcl.setVisibility(View.VISIBLE);
//                    btClaimForm.setVisibility(View.VISIBLE);
                    llClaimForm.setVisibility(View.VISIBLE);
                    mclClaimFormIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });

        llMCLHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mclHistoryRV.isShown()) {
                    mclHistoryRV.setVisibility(View.GONE);
                    mclHistoryIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    mclHistoryRV.setVisibility(View.VISIBLE);
                    mclHistoryIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });

        Bundle extra = getIntent().getExtras();
        processInstanceId = extra.getString(Constants.KEY_PROCESS_INSTANCE);
        personalNum = extra.getString(Constants.KEY_PERSONAL_NUM);
        k2SerialNumber = extra.getString(Constants.KEY_K2SERIAL_NUM);
        String tmpK2ActionOption = extra.getString(Constants.KEY_K2ACTION);
        if (tmpK2ActionOption.contains("|")) {
            k2ActionOption = tmpK2ActionOption.split("\\|");
        } else {
            k2ActionOption = new String[1];
            k2ActionOption[0] = tmpK2ActionOption;
        }
        loading = new SpotsDialog.Builder().setContext(this).build();

        Log.d(TAG, "tmpK2ActionOption = " + tmpK2ActionOption);
        Log.d(TAG, "k2ActionOption = " + new Gson().toJson(k2ActionOption));

        new ButtonApprovalBuilder(this)
                .setModes(k2ActionOption)
                .setModeListener(modeListener)
                .build();

//        btClaimForm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MclDetailActivity.this, MclClaimFormActivity.class);
//
//                intent.putExtra(CLAIM_REQUEST, claimRequest);
//                startActivityForResult(intent, CLAIM_REQ_CODE);
//            }
//        });

        getProcessInstance();

        getFCLHistory();
    }

    @Override
    protected void onStart() {
        super.onStart();

        int writeExternalDisk = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int redExternalDisk = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if ((writeExternalDisk == PackageManager.PERMISSION_GRANTED) &&
                (redExternalDisk == PackageManager.PERMISSION_GRANTED)
        ) {
            Log.v(TAG,"Permission is granted");
        } else {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Permission Required")
                    .setMessage("This applicaiton need WRITE & READ External Storage Permission")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MclDetailActivity.this,
                                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WES);

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            Toast.makeText(MclDetailActivity.this, "Payslip can't be downloaded", Toast.LENGTH_LONG).show();
                        }
                    })
                    .create();

            dialog.show();
        }

        if (getIntent().hasExtra("type")) {
            if (!getIntent().getStringExtra("type").equalsIgnoreCase("Approval History")) {
                super.initPersonalFragment(personalNum, profileListener);
            }
        } else {
            super.initPersonalFragment(personalNum, profileListener);
        }
    }

    public void initPersonalFragment(String personalNum, LoadProfileListener profileListener) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        PersonalDataFragment fragment = PersonalDataFragment.newInstance(personalNum);
        fragment.setProfileListener(profileListener);
        ft.replace(R.id.fmFragmentPersonal, fragment);
        ft.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_WES) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                claimRequest = (ClaimRequest) data.getSerializableExtra(CLAIM_REQUEST);
                Log.d(TAG, "claimRequest.grantedAmount = " + claimRequest.grantedAmount);
                List<ClaimRequest> list = new ArrayList<>();
                list.add(claimRequest);
                ClaimReqAdapter adapter = new ClaimReqAdapter(this, list, claimRequest, type);
                rvMcl.setAdapter(adapter);
            } if (resultCode == Activity.RESULT_CANCELED) {
                Log.d(TAG, "onActivityResult = RESULT_CANCELED");
            }
        }
    }

    private void getProcessInstance() {
        loading.show();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        restApi.getMclFlcOpc("K2Services", "GetProcessInstance", processInstanceId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d(TAG, "getProcessInstance sip.. " + strResponse);
                                parseXml(strResponse, successApproveListener);
                                getDocuments();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            MclDetailActivity.super.handleError(response);
                            Log.d(TAG, "getProcessInstance false _ " + response.raw().toString());
                        }

                        loading.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        Log.d(TAG, "getWorklistPending onFailure..");
                        MclDetailActivity.super.showError("Failed connect to server");
                        t.printStackTrace();
                    }
                });
    }

    private void getFCLHistory() {
        loading.show();
        Calendar calendar = Calendar.getInstance();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        restApi.getFCLHistory("BenefitServices", "GetAllClaimRequest", PrefUtils.Build(this)
                .getPref().getString(Constants.KEY_PERSONAL_NUM, ""),
                new SimpleDateFormat("yyyyMMdd").format(calendar.getTime()),
                PrefUtils.Build(this)
                        .getPref().getString(Constants.KEY_PBUKRSM, ""))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                System.out.println("getCurrentOrganizationalAssignment success");
                                parseXml(strResponse, successListenerHistory);
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

    private void getDocuments() {
        loading.show();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        String folder = "ROOT/3rdPartyApps/IAM/RequestAttachment/" + tvFolioNumber.getText().toString();
        restApi.getMclDocuments("FileManagementServices", "GetList", folder, "1000", "0", "", "")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d(TAG, "getProcessInstance sip.. " + strResponse);
                                parseXml(strResponse, successGetDocumentListener);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            MclDetailActivity.super.handleError(response);
                            Log.d(TAG, "getProcessInstance false _ " + response.raw().toString());
                        }

                        loading.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        Log.d(TAG, "getWorklistPending onFailure..");
                        MclDetailActivity.super.showError("Failed connect to server");
                        t.printStackTrace();
                    }
                });
    }

    private void sumbitApproval(String approvalValue) {
        loading.show();
        String folioNumber = tvFolioNumber.getText().toString();

        DataApproval dataApproval = new DataApproval();
        dataApproval.setFolioNumber(folioNumber);
        dataApproval.setK2SerialNumber(k2SerialNumber);
        dataApproval.setK2Action(approvalValue);
        dataApproval.setXmlData(getNewXmlData());
        dataApproval.setXmlDataSummary("");
        dataApproval.setComment(etComment.getText().toString());
        dataApproval.setK2DataFieldsInJSON("");

        Gson gson = new Gson();

        Log.d(TAG, "dataApproval = " + gson.toJson(dataApproval));

        PortalApiInterface restApi = RestClient.getRetrofitAuthenticated(this, 2000);
        restApi.submitApprovalPart("K2Services", "SetDecision",
                dataApproval.getFolioNumber(),
                dataApproval.getK2SerialNumber(),
                dataApproval.getK2Action(),
                dataApproval.getXmlData(),
                dataApproval.getXmlDataSummary(),
                dataApproval.getComment(),
                dataApproval.getK2DataFieldsInJSON())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d(TAG, "sumbitApproval sip.. " + strResponse);
                                parseXml(strResponse, successApproveListener);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d(TAG, "sumbitApproval false _ " + response.raw().toString());
                        }

                        loading.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        Log.d(TAG, "sumbitApproval onFailure..");
                        t.printStackTrace();
                    }
                });
    }

    private void parseJsonHistory(String strJson) {
        try {
            List<ClaimRequest> list = new ArrayList<>();
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);

            JsonArray jsonArray = jsonObject.getAsJsonArray("Table0");

            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jo = jsonArray.get(i).getAsJsonObject();
                ClaimRequest data = new ClaimRequest();
                data.folioNumber = jo.get("FolioNumber").getAsString();
                data.claimType = jo.get("ClaimTypeText").getAsString();
                data.claimType = URLDecoder.decode(data.claimType, "UTF-8");
                data.patient = jo.get("FI_DEPENDENT").getAsString();
//                data.patientAge = jo.get("AGEOFYEAR").isJsonNull() ? "" : jo.get("AGEOFYEAR").getAsString();
                data.strStatus = jo.get("PISA").getAsString();
                data.claimAmount = jo.get("FI_USERCLAIMAMOUNT").getAsString();
//                data.claimAmountTxt = jo.get("FI_USERCLAIMAMOUNTTXT").getAsString();
                // TODO make sure field reference
                data.grantedAmount = jo.get("FI_CLAIMAMOUNT").getAsString();
//                data.grantedAmountText = jo.get("FI_CLAIMAMOUNTTXT").getAsString();
                data.billDate = jo.get("FI_BILLDATE").getAsString();
                data.billNumber = jo.get("FI_BILNR").getAsString();
//                data.diagnose4 = String.valueOf(jo.get("Diagnose_4")).equalsIgnoreCase("null") ? "" : jo.getAsJsonObject("Diagnose_4").getAsString();
//                data.costCenter = jo.get("FI_KOSTL").getAsString();

                Log.d(TAG, "data.claimType = " + data.claimType);

                list.add(data);
            }

            if (list != null && list.size() != 0) {
                llMCLHistory.setVisibility(View.VISIBLE);
                mclHistoryRV.setVisibility(View.VISIBLE);
                MCLHistoryAdapter adapter = new MCLHistoryAdapter(this, list, type);
                mclHistoryRV.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
                mclHistoryRV.setAdapter(adapter);
            } else {
                llMCLHistory.setVisibility(View.GONE);
                mclHistoryRV.setVisibility(View.GONE);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void parseJson(String strJson) {
        Log.d(TAG, "parseJson");

        ArrayList<IamComment> list = new ArrayList<>();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);
        JsonArray processInstance = jsonObject.getAsJsonArray("K2ProcessInstance");
        JsonArray wfRequest = jsonObject.getAsJsonArray("WF_REQUEST");
        JsonArray wfRequestData = jsonObject.getAsJsonArray("WF_REQUEST_DATA");
        JsonArray wfRequestDataSumary = jsonObject.getAsJsonArray("WF_REQUEST_DATA_SUMMARY");
        JsonArray wfRequestActivities = jsonObject.getAsJsonArray("WF_REQUEST_ACTIVITIES");

        if ((processInstance != null) && (processInstance.size() > 0)) {
            JsonObject joK2PI = processInstance.get(0).getAsJsonObject();
            String folio = joK2PI.get("Folio").getAsString();

            if (folio != null) {
                tvFolioNumber.setText(folio);
                if (folio.startsWith("MCL")) {
                    mclTitleTV.setText("Medical Claim ");
                } else if (folio.startsWith("FLC")) {
                    mclTitleTV.setText("Frame Lense Claim");
                } else if (folio.startsWith("OPC")) {
                    mclTitleTV.setText("Orthodonthi Prosthodonthi Claim");
                }
            }
        }

        if (!getIntent().hasExtra("type")) {
            if ((wfRequest != null) && (wfRequest.size() > 0)) {
                JsonObject joLastActivity = wfRequest.get(0).getAsJsonObject();
                String activityName = joLastActivity.get("LastActivityName").getAsString();
                String requestorID = joLastActivity.get("RequestorID").getAsString();

                if (activityName != null) {
                    tvLastActivity.setText(activityName);
                }
            }
        } else {
            if ((wfRequest != null) && (wfRequest.size() > 0)) {
                JsonObject joLastActivity = wfRequest.get(0).getAsJsonObject();
                String requestorID = joLastActivity.get("RequestorID").getAsString();
                String createdOn = joLastActivity.get("CreatedOn").getAsString();

                if (getIntent().getStringExtra("type").equalsIgnoreCase("Approval History")) {
//                    String date = Utils.formatDate(createdOn.split("T")[0], "yyyyMMdd", "yyyy-MM-dd");
//                    getEmployeeHeader(requestorID, date);
                    super.initPersonalFragment(requestorID, profileListener);
                }
            }
        }

        String xmlData = "";

        if (wfRequestData != null && wfRequestData.size() > 0) {
            JsonObject joWf = wfRequestData.get(0).getAsJsonObject();
            xmlData = joWf.get("XMLData").getAsString();
        }

        try {
            xmlData = URLDecoder.decode(xmlData, "UTF-8");

            parseWfData(xmlData);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (wfRequestActivities != null) {
            for (JsonElement je : wfRequestActivities) {
                JsonObject jo = je.getAsJsonObject();
                IamComment comment = new IamComment();

                try {
                    comment.username = jo.get("UserName") != null ? jo.get("UserName").getAsString() : "null";
                    comment.createdBy = jo.get("CreatedBy") != null ? jo.get("CreatedBy").getAsString() : "null";
                    comment.strDate = jo.get("CreatedOn") != null ? jo.get("CreatedOn").getAsString() : "null";
                    comment.message = jo.get("Comment") != null ? jo.get("Comment").getAsString() : "null";
                    comment.status = jo.get("STATUS") != null ? jo.get("STATUS").getAsString() : "null";

                    list.add(comment);
                } catch (UnsupportedOperationException ex) {
                    ex.printStackTrace();
                }
            }

            super.loadComments(list);
        }
    }

    private void parseJsonDocument(String strJson) {
        Log.d(TAG, "parseJson");

        Gson gson = new Gson();
        JsonObject jo = gson.fromJson(strJson, JsonObject.class);
        JsonArray jArr = jo.getAsJsonArray("Table0");

        List<MCLDocumentModel> mclDocumentModels = new ArrayList<>();
        for (int i = 0; i < jArr.size(); i++) {
            MCLDocumentModel mclDocumentModel = new MCLDocumentModel();
            mclDocumentModel.setName(jArr.get(i).getAsJsonObject().get("ObjectName").getAsString());
            mclDocumentModel.setUrl(jArr.get(i).getAsJsonObject().get("FullName").getAsString());
            mclDocumentModels.add(mclDocumentModel);
        }

        mclDocumentAdapter = new MCLDocumentAdapter(this, mclDocumentModels, this);
        documentRV.setLayoutManager(new LinearLayoutManager(this));
        documentRV.setAdapter(mclDocumentAdapter);


    }

    public void parseWfData(String strXml) {
        Log.d(TAG, "parseWfData");
        Log.d(TAG, "strXml=" + strXml);

        DocumentBuilderFactory factory;
        DocumentBuilder builder;
        Document dom;

        try {
            InputStream is = new ByteArrayInputStream(strXml.getBytes("utf-8"));
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            dom = builder.parse(is);

            NodeList nClaimMedReq = dom.getElementsByTagName("ClaimMedicalRequest");

            /**
             MCL Claim Form
             */
            try {
                if (nClaimMedReq.getLength() > 0) {
                    JSONObject jo = JsonXmlUtils.xmlNodeToJsonNode(nClaimMedReq.item(0));

                    Log.d(TAG, "ClaimRequest obj = " + jo.toString());

                    claimRequest = new ClaimRequest();
                    claimRequest.claimType = jo.getString("ClaimTypeText");
                    claimRequest.claimType = URLDecoder.decode(claimRequest.claimType, "UTF-8");
                    claimRequest.patient = jo.getString("FI_DEPENDENT");
                    claimRequest.patientAge = jo.getString("AGEOFYEAR");
                    claimRequest.strStatus = jo.getString("PISA");
                    claimRequest.claimAmount = jo.getString("FI_USERCLAIMAMOUNT");
                    claimRequest.claimAmountTxt = jo.getString("FI_USERCLAIMAMOUNTTXT");
                    // TODO make sure field reference
                    claimRequest.grantedAmount = jo.getString("FI_CLAIMAMOUNT");
                    claimRequest.grantedAmountText = jo.getString("FI_CLAIMAMOUNTTXT");
                    claimRequest.billDate = jo.getString("FI_BILLDATE");
                    claimRequest.billNumber = jo.getString("FI_BILNR");
                    claimRequest.diagnoseId1 = "";
                    claimRequest.diagnoseId2 = "";
                    claimRequest.diagnoseId3 = "";
                    claimRequest.diagnose4 = "";
                    claimRequest.diagnoseTxt1 = "";
                    claimRequest.diagnoseTxt2 = "";
                    claimRequest.diagnoseTxt3 = "";

                    if (!jo.isNull("Diagnose")) {
                        claimRequest.diagnoseId1 = jo.getJSONObject("Diagnose").isNull("Diagnose_1_ID") ? "" : jo.getJSONObject("Diagnose").getString("Diagnose_1_ID");
                        claimRequest.diagnoseId2 = jo.getJSONObject("Diagnose").isNull("Diagnose_2_ID") ? "" : jo.getJSONObject("Diagnose").getString("Diagnose_2_ID");
                        claimRequest.diagnoseId3 = jo.getJSONObject("Diagnose").isNull("Diagnose_3_ID") ? "" : jo.getJSONObject("Diagnose").getString("Diagnose_3_ID");
                        claimRequest.diagnoseTxt1 = jo.getJSONObject("Diagnose").isNull("Diagnose_1_TXT") ? "" : jo.getJSONObject("Diagnose").getString("Diagnose_1_TXT");
                        claimRequest.diagnoseTxt2 = jo.getJSONObject("Diagnose").isNull("Diagnose_2_TXT") ? "" : jo.getJSONObject("Diagnose").getString("Diagnose_2_TXT");
                        claimRequest.diagnoseTxt3 = jo.getJSONObject("Diagnose").isNull("Diagnose_3_TXT") ? "" : jo.getJSONObject("Diagnose").getString("Diagnose_3_TXT");
                        claimRequest.diagnose4 = jo.getJSONObject("Diagnose").isNull("Diagnose_4") ? "" : jo.getJSONObject("Diagnose").getString("Diagnose_4");
                    }
                    claimRequest.costCenter = jo.getString("FI_KOSTL");

                    Log.d(TAG, "data.claimType = " + claimRequest.claimType);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            /**
              MCL History
              TODO ganti nClaimMedReq dengan Element Hostory
             */
            try {
                ClaimRequest data;
                List<ClaimRequest> list = new ArrayList<>();

                if (nClaimMedReq.getLength() > 0) {
                    for (int i = 0; i < nClaimMedReq.getLength(); i++) {
                        JSONObject jo = JsonXmlUtils.xmlNodeToJsonNode(nClaimMedReq.item(i));

                        Log.d(TAG, "ClaimRequest obj = " + jo.toString());

                        data = new ClaimRequest();
//                        data.folioNumber = jo.getString("FolioNumber");
                        data.claimType = jo.getString("ClaimTypeText");
                        data.claimType = URLDecoder.decode(data.claimType, "UTF-8");
                        data.patient = jo.getString("FI_DEPENDENT");
                        data.patientAge = jo.getString("AGEOFYEAR");
                        data.strStatus = jo.getString("PISA");
                        data.claimAmount = jo.getString("FI_USERCLAIMAMOUNT");
                        data.claimAmountTxt = jo.getString("FI_USERCLAIMAMOUNTTXT");
                        // TODO make sure field reference
                        data.grantedAmount = jo.getString("FI_CLAIMAMOUNT");
                        data.grantedAmountText = jo.getString("FI_CLAIMAMOUNTTXT");
                        data.billDate = jo.getString("FI_BILLDATE");
                        data.billNumber = jo.getString("FI_BILNR");

                        data.diagnoseId1 = "";
                        data.diagnoseId2 = "";
                        data.diagnoseId3 = "";
                        data.diagnose4 = "";
                        data.diagnoseTxt1 = "";
                        data.diagnoseTxt2 = "";
                        data.diagnoseTxt3 = "";

                        if (!jo.isNull("Diagnose")) {
                            data.diagnoseId1 = jo.getJSONObject("Diagnose").isNull("Diagnose_1_ID") ? "" : jo.getJSONObject("Diagnose").getString("Diagnose_1_ID");
                            data.diagnoseId2 = jo.getJSONObject("Diagnose").isNull("Diagnose_2_ID") ? "" : jo.getJSONObject("Diagnose").getString("Diagnose_2_ID");
                            data.diagnoseId3 = jo.getJSONObject("Diagnose").isNull("Diagnose_3_ID") ? "" : jo.getJSONObject("Diagnose").getString("Diagnose_3_ID");
                            data.diagnoseTxt1 = jo.getJSONObject("Diagnose").isNull("Diagnose_1_TXT") ? "" : jo.getJSONObject("Diagnose").getString("Diagnose_1_TXT");
                            data.diagnoseTxt2 = jo.getJSONObject("Diagnose").isNull("Diagnose_2_TXT") ? "" : jo.getJSONObject("Diagnose").getString("Diagnose_2_TXT");
                            data.diagnoseTxt3 = jo.getJSONObject("Diagnose").isNull("Diagnose_3_TXT") ? "" : jo.getJSONObject("Diagnose").getString("Diagnose_3_TXT");
                            data.diagnose4 = jo.getJSONObject("Diagnose").isNull("Diagnose_4") ? "" : jo.getJSONObject("Diagnose").getString("Diagnose_4");
                        }
                        data.costCenter = jo.getString("FI_KOSTL");

                        Log.d(TAG, "data.claimType = " + data.claimType);
                        Log.d(TAG, "data.diagnoseId1 = " + data.diagnoseId1);

                        list.add(data);
                    }

                    ClaimReqAdapter adapter = new ClaimReqAdapter(this, list, claimRequest, type);
                    rvMcl.setAdapter(adapter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getNewXmlData() {
        String existingClaimamount = "\t\t<FI_CLAIMAMOUNT>10000000</FI_CLAIMAMOUNT>\n";
        String newClaimAmount = "\t\t<FI_CLAIMAMOUNT>" + claimRequest.grantedAmount + "</FI_CLAIMAMOUNT>\n";

        // Diagnose ID
        String existingDiagnose1ID = "\t\t\t<Diagnose_1_ID />\n";
        String newDiagnose1ID = "\t\t\t<Diagnose_1_ID>" + claimRequest.diagnoseId1 + "</Diagnose_1_ID>\n";

        String existingDiagnose2ID = "\t\t\t<Diagnose_2_ID />\n";
        String newDiagnose2ID = "\t\t\t<Diagnose_2_ID>" + claimRequest.diagnoseId2 + "</Diagnose_2_ID>\n";

        String existingDiagnose3ID = "\t\t\t<Diagnose_3_ID />\n";
        String newDiagnose3ID = "\t\t\t<Diagnose_3_ID>" + claimRequest.diagnoseId3 + "</Diagnose_3_ID>\n";

        // Diagnose Txt
        String existingDiagnose1Txt = "\t\t\t<Diagnose_1_TXT />\n";
        String newDiagnose1Txt = "\t\t\t<Diagnose_1_TXT>" + claimRequest.diagnoseTxt1 + "</Diagnose_1_TXT>\n";

        String existingDiagnose2Txt = "\t\t\t<Diagnose_2_TXT />\n";
        String newDiagnose2Txt = "\t\t\t<Diagnose_2_TXT>" + claimRequest.diagnoseTxt2 + "</Diagnose_2_TXT>\n";

        String existingDiagnose3Txt = "\t\t\t<Diagnose_3_TXT />\n";
        String newDiagnose3Txt = "\t\t\t<Diagnose_3_TXT>" + claimRequest.diagnoseTxt3 + "</Diagnose_3_TXT>\n";

        String existingDiagnose4 = "\t\t\t<Diagnose_4 />\n";
        String newDiagnose4 = "\t\t\t<Diagnose_4>" + claimRequest.diagnose4 + "</Diagnose_4>\n";

        String existingCostCenter = "\t\t\t<FI_KOSTL />\n";
        String newCostCenter = "\t\t\t<FI_KOSTL>" + claimRequest.costCenter + "</FI_KOSTL>\n";


        XML_SAMPLE = XML_SAMPLE.replace(existingClaimamount, newClaimAmount);
        XML_SAMPLE = XML_SAMPLE.replace(existingDiagnose1ID, newDiagnose1ID);
        XML_SAMPLE = XML_SAMPLE.replace(existingDiagnose2ID, newDiagnose2ID);
        XML_SAMPLE = XML_SAMPLE.replace(existingDiagnose3ID, newDiagnose3ID);
        XML_SAMPLE = XML_SAMPLE.replace(existingDiagnose1Txt, newDiagnose1Txt);
        XML_SAMPLE = XML_SAMPLE.replace(existingDiagnose2Txt, newDiagnose2Txt);
        XML_SAMPLE = XML_SAMPLE.replace(existingDiagnose3Txt, newDiagnose3Txt);
        XML_SAMPLE = XML_SAMPLE.replace(existingDiagnose4, newDiagnose4);
        XML_SAMPLE = XML_SAMPLE.replace(existingCostCenter, newCostCenter);

        // DST....

        return XML_SAMPLE;
    }

    ButtonApprovalBuilder.ApprovalModeListener modeListener = new ButtonApprovalBuilder.ApprovalModeListener() {
        @Override
        public void approve() {
            if (etComment.getText().toString().length() > 0) {
                sumbitApproval(Constants.APROVAL_APPROVE);
            } else {
                pleaseComment();
            }
        }

        @Override
        public void reject() {
            if (etComment.getText().toString().length() > 0) {
                sumbitApproval(Constants.APROVAL_REJECT);
            } else {
                pleaseComment();
            }
        }

        @Override
        public void retry() {

        }

        @Override
        public void askRevise() {
            if (etComment.getText().toString().length() > 0) {
                sumbitApproval(Constants.APROVAL_ASK_REVISE);
            } else {
                pleaseComment();
            }
        }

        @Override
        public void continueAction() {
            if (etComment.getText().toString().length() > 0) {
                sumbitApproval(Constants.APROVAL_CONTINUE);
            } else {
                pleaseComment();
            }
        }

        @Override
        public void cancel() {
            if (etComment.getText().toString().length() > 0) {
                sumbitApproval(Constants.APROVAL_CANCEL);
            } else {
                pleaseComment();
            }
        }

        @Override
        public void resubmit() {
            if (etComment.getText().toString().length() > 0) {
                sumbitApproval(Constants.APROVAL_RESUBMIT);
            } else {
                pleaseComment();
            }
        }

        @Override
        public void submit() {
            if (etComment.getText().toString().length() > 0) {
                sumbitApproval(Constants.APROVAL_SUBMIT);
            } else {
                pleaseComment();
            }
        }

        @Override
        public void complete() {
            if (etComment.getText().toString().length() > 0) {
                sumbitApproval(Constants.APROVAL_COMPLETE);
            } else {
                pleaseComment();
            }
        }
    };

    LoadProfileListener profileListener = new LoadProfileListener() {
        @Override
        public void loadProfile(String username) {
            Log.d(TAG, "loadProfilePic");
            MclDetailActivity.this.loadProfilePic(username, R.id.ivProfile);
        }
    };

    OnSuccessListener successApproveListener = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            Log.d(TAG, "onSuccess:strJson = " + strJson);

            if (strJson.equalsIgnoreCase("-1")) { // return value success submit worklist
                Toast.makeText(MclDetailActivity.this,
                        getResources().getString(R.string.submit_success), Toast.LENGTH_LONG).show();
                MclDetailActivity.this.finish();
            } else {
                parseJson(strJson);
            }
        }
    };

    OnSuccessListener successListenerHistory = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            Log.d(TAG, "onSuccess:strJson = " + strJson);
            parseJsonHistory(strJson);
        }
    };

    OnSuccessListener successGetDocumentListener = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            Log.d(TAG, "onSuccess:strJson = " + strJson);
            parseJsonDocument(strJson);
        }
    };

    private static String XML_SAMPLE = "<ClaimMedical>\n" +
            "\t<ClaimMedicalRequest>\n" +
            "\t\t<AGEOFYEAR>34</AGEOFYEAR>\n" +
            "\t\t<AGEOFMONTH>6</AGEOFMONTH>\n" +
            "\t\t<PSUBTYM />\n" +
            "\t\t<POBJPSM />\n" +
            "\t\t<PISA>P</PISA>\n" +
            "\t\t<LastClaim>20190819</LastClaim>\n" +
            "\t\t<RequesterName>Trainee 8</RequesterName>\n" +
            "\t\t<RequesterADUser>PERTAMINA\\\\traineeptm.6008</RequesterADUser>\n" +
            "\t\t<RequesterNPK>760010</RequesterNPK>\n" +
            "\t\t<RequesterDate>20190819</RequesterDate>\n" +
            "\t\t<CreatedBy>PERTAMINA\\\\traineeptm.6008</CreatedBy>\n" +
            "\t\t<ClaimTypeText>Lasik</ClaimTypeText>\n" +
            "\t\t<Diagnose>\n" +
            "\t\t\t<Diagnose_1_ID />\n" +
            "\t\t\t<Diagnose_2_ID />\n" +
            "\t\t\t<Diagnose_3_ID />\n" +
            "\t\t\t<Diagnose_1_TXT />\n" +
            "\t\t\t<Diagnose_2_TXT />\n" +
            "\t\t\t<Diagnose_3_TXT />\n" +
            "\t\t\t<Diagnose_4 />\n" +
            "\t\t</Diagnose>\n" +
            "\t\t<FI_BEN_PLAN>MEDI</FI_BEN_PLAN>\n" +
            "\t\t<FI_BILLDATE>20190819</FI_BILLDATE>\n" +
            "\t\t<FI_BILNR>1321019</FI_BILNR>\n" +
            "\t\t<FI_CLAIMAMOUNT>10000000</FI_CLAIMAMOUNT>\n" +
            "\t\t<FI_CLAIMAMOUNTTXT>Sepuluh  Juta Rupiah</FI_CLAIMAMOUNTTXT>\n" +
            "\t\t<FI_DEPENDENT>Trainee 8</FI_DEPENDENT>\n" +
            "\t\t<FI_DIAGNOSE />\n" +
            "\t\t<FI_KOSTL />\n" +
            "\t\t<FI_LGART>8854</FI_LGART>\n" +
            "\t\t<FI_PERNR>760010</FI_PERNR>\n" +
            "\t\t<FI_USERCLAIMAMOUNT>10000000</FI_USERCLAIMAMOUNT>\n" +
            "\t\t<FI_USERCLAIMAMOUNTTXT>Sepuluh  Juta Rupiah</FI_USERCLAIMAMOUNTTXT>\n" +
            "\t\t<PBTRTLM>Z000</PBTRTLM>\n" +
            "\t\t<PBTRTLM_TEXT>Kantor Pusat</PBTRTLM_TEXT>\n" +
            "\t</ClaimMedicalRequest>\n" +
            "</ClaimMedical>";

    @Override
    public void onDownloadButtonClicked(String url, String name) {
        downloadPDF(url, name);
    }

    private void downloadPDF(String file, String name) {
        loading.show();
        String baseUrl = "https://apps.pertamina.com/PTM.HRIS.Service.WebService.Internet.DEV/GetFM.ashx?URL=";
        new DownloadFile().execute(baseUrl+file, name);
    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "pertamina-i-am-mcl");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            FileDownloader.downloadFile(MclDetailActivity.this, fileUrl, pdfFile);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loading.dismiss();
            Toast.makeText(MclDetailActivity.this, "File telah di-Download ke folder pertamina-i-am-mcl di internal storage", Toast.LENGTH_LONG).show();
        }
    }
}
