package com.pertamina.portal.iam.activity.worklist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.base.BaseWorklistActivity;
import com.pertamina.portal.iam.adapters.MPPKStatusAdapter;
import com.pertamina.portal.iam.adapters.MppkAdapter;
import com.pertamina.portal.iam.fragments.PersonalDataFragment;
import com.pertamina.portal.iam.fragments.WorklistCommentFragment;
import com.pertamina.portal.iam.interfaces.LoadProfileListener;
import com.pertamina.portal.iam.models.IamComment;
import com.pertamina.portal.iam.models.MPPKModel;
import com.pertamina.portal.iam.models.MPPKStatusModel;
import com.pertamina.portal.iam.utils.ButtonApprovalBuilder;
import com.pertamina.portal.iam.utils.XMLParserUtils.XMLParserInterface;
import com.pertamina.portal.iam.utils.XMLParserUtils.XMLParserUtils;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MppkActivity extends BaseWorklistActivity {

    private String TAG = "MPPKActivity";
    private AlertDialog loading;
    private AlertDialog mAlertDialog;
    private String processInstanceId;
    private String personalNum;
    private TextView folioNameTV, lastActivityTV;
    private PortalApiInterface restApi;
    private String[] k2ActionOption;
    private EditText commentET;
    private String k2SerialNumber;
    private LinearLayout actionContainerLL;
    private RecyclerView mppkRV, mppk2RV;
    private LinearLayout additionalContainerLL, mppkContainerLL;
    private ImageView additionalExpandCollapseIV, mppkExpandCollapseIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mppk_detail);
        super.onCreateBackable(this, R.id.ivBack);

        Bundle extra = getIntent().getExtras();
        processInstanceId = extra.getString(Constants.KEY_PROCESS_INSTANCE);
        k2SerialNumber = extra.getString(Constants.KEY_K2SERIAL_NUM);
        personalNum = extra.getString(Constants.KEY_PERSONAL_NUM);
        String tmpK2ActionOption = extra.getString(Constants.KEY_K2ACTION);

        loading = new SpotsDialog.Builder().setContext(this).build();

        folioNameTV = findViewById(R.id.mppkFolioNumberTV);
        additionalExpandCollapseIV = findViewById(R.id.additionalExpandCollapseIV);
        mppkExpandCollapseIV = findViewById(R.id.mppkExpandCollapseIV);
        additionalContainerLL = findViewById(R.id.llSectionSeparator);
        mppkContainerLL = findViewById(R.id.llSectionSeparatorMPPK);
        actionContainerLL = findViewById(R.id.actionContainerLL);
        lastActivityTV = findViewById(R.id.mppkLasActivityTV);
        commentET = findViewById(R.id.commentET);
        mppkRV = findViewById(R.id.mppkRV);
        mppk2RV = findViewById(R.id.mppk2RV);

        if (tmpK2ActionOption.contains("|")) {
            k2ActionOption = tmpK2ActionOption.split("\\|");
        } else {
            k2ActionOption = new String[1];
            k2ActionOption[0] = tmpK2ActionOption;
        }
        Log.d(TAG, "tmpK2ActionOption = " + tmpK2ActionOption);
        Log.d(TAG, "k2ActionOption = " + new Gson().toJson(k2ActionOption));

        new ButtonApprovalBuilder(this)
                .setModes(k2ActionOption)
                .setModeListener(modeListener)
                .build();

        restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);

        buildAlert();

        if (getIntent().hasExtra("type")) {
            lastActivityTV.setText(getIntent().getStringExtra("type"));
            actionContainerLL.setVisibility(View.GONE);
        }

        additionalContainerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mppkRV.isShown()) {
                    mppkRV.setVisibility(View.GONE);
                    additionalExpandCollapseIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    mppkRV.setVisibility(View.VISIBLE);
                    additionalExpandCollapseIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });

        mppkContainerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mppk2RV.isShown()) {
                    mppk2RV.setVisibility(View.GONE);
                    mppkExpandCollapseIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    mppk2RV.setVisibility(View.VISIBLE);
                    mppkExpandCollapseIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });
    }

    ButtonApprovalBuilder.ApprovalModeListener modeListener = new ButtonApprovalBuilder.ApprovalModeListener() {
        @Override
        public void approve() {
            if (commentET.getText().toString().length() > 0) {
                sumbitApproval(Constants.APROVAL_APPROVE);
            } else {
                pleaseComment();
            }
        }

        @Override
        public void reject() {
            if (commentET.getText().toString().length() > 0) {
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
            if (commentET.getText().toString().length() > 0) {
                sumbitApproval(Constants.APROVAL_ASK_REVISE);
            } else {
                pleaseComment();
            }
        }

        @Override
        public void continueAction() {
            if (commentET.getText().toString().length() > 0) {
                sumbitApproval(Constants.APROVAL_CONTINUE);
            } else {
                pleaseComment();
            }
        }

        @Override
        public void cancel() {
            if (commentET.getText().toString().length() > 0) {
                sumbitApproval(Constants.APROVAL_CANCEL);
            } else {
                pleaseComment();
            }
        }

        @Override
        public void resubmit() {
            if (commentET.getText().toString().length() > 0) {
                sumbitApproval(Constants.APROVAL_RESUBMIT);
            } else {
                pleaseComment();
            }
        }

        @Override
        public void submit() {
            if (commentET.getText().toString().length() > 0) {
                sumbitApproval(Constants.APROVAL_SUBMIT);
            } else {
                pleaseComment();
            }
        }

        @Override
        public void complete() {
            if (commentET.getText().toString().length() > 0) {
                sumbitApproval(Constants.APROVAL_COMPLETE);
            } else {
                pleaseComment();
            }
        }
    };

    private void initPersonalFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        PersonalDataFragment fragment = PersonalDataFragment.newInstance(personalNum);
        fragment.setProfileListener(profileListener);
        ft.replace(R.id.fmFragmentPersonal, fragment);
        ft.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProcessInstance();
        if (getIntent().hasExtra("type")) {
            if (!getIntent().getStringExtra("type").equalsIgnoreCase("Approval History")) {
                initPersonalFragment(personalNum, profileListener);
            }
        } else {
            initPersonalFragment(personalNum, profileListener);
        }
    }

    private void getAdditional(String personalNum) {
        loading.show();
        String sqlParam = "{PersonnelNumber: '" + personalNum + "'}";
        restApi.getMPPKAdditional("DataReferenceServices", "SelectAndExecuteQuery", sqlParam, "USP_PTM_MPPK_GET_ADDITIONAL_INFO")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d(TAG, "getProcessInstance sip.. " + strResponse);
                                parseXmlAdditinal(strResponse, successListener);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                String errorResponse = response.errorBody().string();
                                Log.d(TAG, "getProcessInstance false _ " + errorResponse);
                                handleError(response);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        loading.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        Log.d(TAG, "getWorklistPending onFailure..");
                        t.printStackTrace();
                    }
                });
    }

    private void getStatus(String folioNumber) {
        loading.show();
        String sqlParam = "{FolioNumber: '" + folioNumber+ "'}";
        restApi.getMPPKStatus("DataReferenceServices", "SelectAndExecuteQuery", sqlParam)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d(TAG, "getProcessInstance sip.. " + strResponse);
                                XMLParserUtils.parseXml(strResponse, new XMLParserInterface() {
                                    @Override
                                    public void onSuccess(String result) {
                                        Gson gson = new Gson();
                                        JsonObject jsonObject = gson.fromJson(result, JsonObject.class);
                                        JsonArray jsonArray = jsonObject.getAsJsonArray("Table0");

                                        if (jsonArray.size() == 0) {
                                            mppkContainerLL.setVisibility(View.GONE);
                                            mppk2RV.setVisibility(View.GONE);
                                            return;
                                        }

                                        List<MPPKStatusModel> mppkStatusModels = new ArrayList<>();
                                        for (int i = 0; i < jsonArray.size(); i++) {
                                            MPPKStatusModel mppkStatusModel = new MPPKStatusModel();
                                            mppkStatusModel.setFolioNumber(jsonArray.get(i).getAsJsonObject().get("FolioNumber").getAsString());
                                            mppkStatusModel.setName(jsonArray.get(i).getAsJsonObject().get("Name").getAsString());
                                            mppkStatusModel.setNopek(jsonArray.get(i).getAsJsonObject().get("RequestorID").getAsString());
                                            mppkStatusModel.setStatus(jsonArray.get(i).getAsJsonObject().get("Status").getAsString());
                                            mppkStatusModels.add(mppkStatusModel);
                                        }

                                        MPPKStatusAdapter mppkStatusAdapter = new MPPKStatusAdapter(MppkActivity.this, mppkStatusModels);
                                        mppk2RV.setLayoutManager(new LinearLayoutManager(MppkActivity.this));
                                        mppk2RV.setAdapter(mppkStatusAdapter);
                                    }

                                    @Override
                                    public void onFailure(NodeList nodeListError) {

                                    }

                                    @Override
                                    public void onSuccessReturnMessage(NodeList nodeListError) {

                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                String errorResponse = response.errorBody().string();
                                Log.d(TAG, "getProcessInstance false _ " + errorResponse);
                                handleError(response);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        loading.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        Log.d(TAG, "getWorklistPending onFailure..");
                        t.printStackTrace();
                    }
                });
    }

    private void getProcessInstance() {
        loading.show();
        restApi.getMPPK("K2Services", "GetProcessInstance", processInstanceId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d(TAG, "getProcessInstance sip.. " + strResponse);
                                parseXml(strResponse, successListener);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                String errorResponse = response.errorBody().string();
                                Log.d(TAG, "getProcessInstance false _ " + errorResponse);
                                handleError(response);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        loading.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        Log.d(TAG, "getWorklistPending onFailure..");
                        t.printStackTrace();
                    }
                });
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
                folioNameTV.setText(folio);
                getStatus(folio);
            }

            getAdditional(joK2PI.get("RequesterPERNR_String_ProcDataField").getAsString());
        }

        if ((wfRequest != null) && (wfRequest.size() > 0)) {
            JsonObject joLastActivity = wfRequest.get(0).getAsJsonObject();
            String activityName = joLastActivity.get("LastActivityName").getAsString();

            if (activityName != null) {
                lastActivityTV.setText(activityName);
            }

            String requestorID = joLastActivity.get("RequestorID").getAsString();
            String createdOn = joLastActivity.get("CreatedOn").getAsString();

            if (getIntent().hasExtra("type")) {
                if (getIntent().getStringExtra("type").equalsIgnoreCase("Approval History")) {
//                    String date = Utils.formatDate(createdOn.split("T")[0], "yyyyMMdd", "yyyy-MM-dd");
//                    getEmployeeHeader(requestorID, date);
                    initPersonalFragment(requestorID, profileListener);
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

//            parseWfData(xmlData);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (wfRequestActivities != null) {
            for (JsonElement je : wfRequestActivities) {
                JsonObject jo = je.getAsJsonObject();
                IamComment comment = new IamComment();
                comment.username = jo.get("UserName").getAsString();
                comment.createdBy = jo.get("CreatedBy").getAsString();
                comment.strDate = jo.get("CreatedOn").getAsString();
                comment.message = jo.get("Comment").getAsString();
                comment.status = jo.get("STATUS").getAsString();

                list.add(comment);
            }

            loadComments(list);
        }
    }

    public void loadComments(ArrayList<IamComment> commentList) {
        Log.d(TAG, "loadComments::" + commentList.size());

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flComments, WorklistCommentFragment.newInstance(commentList));
        ft.commit();
    }

    OnSuccessListener successListener = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            parseJson(strJson);
        }
    };

    public void buildAlert() {
        mAlertDialog = new AlertDialog.Builder(this)
                .setNeutralButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                })
                .create();
    }

    LoadProfileListener profileListener = new LoadProfileListener() {
        @Override
        public void loadProfile(String username) {
            Log.d(TAG, "loadProfilePic");
            MppkActivity.this.loadProfilePic(username);
        }
    };

    public void loadProfilePic(String username) {
        super.loadProfilePic(username, R.id.ivProfile);
    }

    public void parseXmlAdditinal(String strXml, OnSuccessListener listener) {
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
                    parseJsonAdditional(nodeListSuccess.item(0).getTextContent());
                }
            } else {
                buildAlert();

                for (int i = 0; i < nodeListError.getLength(); i++) {
                    String strError = nodeListError.item(i).getTextContent();

                    showError(strError);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseJsonAdditional(String strJson) {
        Log.d(TAG, "parseJson");

        ArrayList<IamComment> list = new ArrayList<>();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);
        JsonArray jsonArray = jsonObject.getAsJsonArray("Table0");

        if (jsonArray.size() > 0) {
            List<MPPKModel> mppkModelList = new ArrayList<>();

            for (int i = 0; i < jsonArray.size(); i++) {
                MPPKModel mppkModel = new MPPKModel();
                mppkModel.setPBEGDAM(jsonArray.get(0).getAsJsonObject().get("PBEGDAM").getAsString());
                mppkModel.setPGBDATM(jsonArray.get(0).getAsJsonObject().get("PGBDATM").getAsString());
                mppkModel.setPBUKRSM(jsonArray.get(0).getAsJsonObject().get("PBUKRSM").getAsString());
                mppkModel.setPENDDAM(jsonArray.get(0).getAsJsonObject().get("PENDDAM").getAsString());
                mppkModel.setPPERNRM(jsonArray.get(0).getAsJsonObject().get("PPERNRM").getAsString());
                mppkModel.setPPKT01M(jsonArray.get(0).getAsJsonObject().get("PPKT01M").getAsString());
                mppkModelList.add(mppkModel);
            }

            MppkAdapter mppkAdapter = new MppkAdapter(this, mppkModelList);
            mppkRV.setLayoutManager(new LinearLayoutManager(this));
            mppkRV.setAdapter(mppkAdapter);

        } else {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    OnSuccessListener successApproveListener = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            Log.d(TAG, "onSuccess:strJson = " + strJson);

            if (strJson.equalsIgnoreCase("-1")) { // return value success submit worklist
                Toast.makeText(MppkActivity.this,
                        getResources().getString(R.string.submit_success), Toast.LENGTH_LONG).show();
                MppkActivity.this.finish();
            } else {
                parseJson(strJson);
            }
        }
    };

    private void sumbitApproval(String approvalValue) {
        loading.show();
        String folioNumber = folioNameTV.getText().toString();

        DataApproval dataApproval = new DataApproval();
        dataApproval.setFolioNumber(folioNumber);
        dataApproval.setK2SerialNumber(k2SerialNumber);
        dataApproval.setK2Action(approvalValue);
        dataApproval.setXmlData("");
        dataApproval.setXmlDataSummary("");
        dataApproval.setComment(commentET.getText().toString());
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
}
