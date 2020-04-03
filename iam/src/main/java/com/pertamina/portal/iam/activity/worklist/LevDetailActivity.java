package com.pertamina.portal.iam.activity.worklist;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.core.utils.StringUtils;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.MyWorklistActivity;
import com.pertamina.portal.iam.activity.base.BaseWorklistActivity;
import com.pertamina.portal.iam.adapters.worklist.DestinationAdapter;
import com.pertamina.portal.iam.adapters.worklist.ParticipantAdapter;
import com.pertamina.portal.iam.adapters.worklist.RevLevAdapter;
import com.pertamina.portal.iam.adapters.worklist.SupportingDocAdapter;
import com.pertamina.portal.iam.fragments.PersonalDataFragment;
import com.pertamina.portal.iam.interfaces.LoadProfileListener;
import com.pertamina.portal.iam.models.worklist.Destination;
import com.pertamina.portal.iam.models.IamComment;
import com.pertamina.portal.iam.models.worklist.Participant;
import com.pertamina.portal.iam.models.worklist.ReviewerLev;
import com.pertamina.portal.iam.models.worklist.SupportingDoc;
import com.pertamina.portal.iam.utils.ButtonApprovalBuilder;
import com.pertamina.portal.iam.utils.Utils;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LevDetailActivity extends BaseWorklistActivity {

    private static final String TAG = "LevDetailActivity";
    private AlertDialog loading;
    private String processInstanceId;
    private String personalNum;
    private RecyclerView rvAdditionalDestination, rvAdditionalParticipant,
            rvAdditionalReviewer, rvSupportingDocument;
    private TextView tvFolioNumber;
    private TextView tvLastActivity;
    private TextView tvLeaveType;
    private TextView tvReason;
    private TextView tvLeaveDate;
    private TextView tvOutOfTown;
    private TextView tvDestCity;
    private TextView tvDestCountry;
    private String k2SerialNumber;
    private String[] k2ActionOption;
    private EditText etComment;
    private View btApprove, btReject;
    private TextView tvLeaveQuota;
    private TextView tvLeaveTotal;
    private TextView tvRemainingQuota;
    private TextView tvLeaveOnProcess;
    private TextView levNameTV;
    private TextView tvForecastQuota;
    private LinearLayout additionalParticipant, additionalReviewer, additionalDestination, supportingDocument, detail, levDetailContainerLL, levActionContainerLL;
    private ImageView additionalParticipantIV, additionalReviewerIV, additionalDestinationIV, supportingDocumentIV, detailIV;
    private ConstraintLayout additionalDayCL;
    private TextView additionalDay1TV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lev_detail);
        super.onCreateBackable(this, R.id.ivBack);

        rvAdditionalDestination = (RecyclerView) findViewById(R.id.rvAdditionalDestination);
        rvAdditionalParticipant = (RecyclerView) findViewById(R.id.rvAdditionalParticipant);
        rvAdditionalReviewer = (RecyclerView) findViewById(R.id.rvAdditionalReviewer);
        rvSupportingDocument = (RecyclerView) findViewById(R.id.rvSupportingDocument);


        additionalDayCL = findViewById(R.id.additionalContainerTV);
        additionalDay1TV = findViewById(R.id.additionalDay1TV);
        additionalDestination = findViewById(R.id.levAdditionalDestinationHaederConainerLL);
        additionalParticipant = findViewById(R.id.levAdditionalParticipantHaederConainerLL);
        additionalReviewer = findViewById(R.id.levAdditionalReviewerHaederConainerLL);
        levDetailContainerLL = findViewById(R.id.levDetailContainerLL);
        supportingDocument = findViewById(R.id.levAdditionalSupportingDocumentHaederConainerLL);
        supportingDocumentIV = findViewById(R.id.levAdditionalSupportingDocumentIV);
        detail = findViewById(R.id.llSectionSeparator);
        additionalDestinationIV = findViewById(R.id.levAdditionalDestinationIV);
        additionalParticipantIV = findViewById(R.id.levAdditionalParticipantIV);
        additionalReviewerIV = findViewById(R.id.levAdditionalReviewerIV);
        detailIV = findViewById(R.id.levAdditionalDetailIV);

        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (levDetailContainerLL.isShown()) {
                    levDetailContainerLL.setVisibility(View.GONE);
                    detailIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    levDetailContainerLL.setVisibility(View.VISIBLE);
                    detailIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });

        additionalReviewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rvAdditionalReviewer.isShown()) {
                    rvAdditionalReviewer.setVisibility(View.GONE);
                    additionalReviewerIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    rvAdditionalReviewer.setVisibility(View.VISIBLE);
                    additionalReviewerIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });

        additionalParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rvAdditionalParticipant.isShown()) {
                    rvAdditionalParticipant.setVisibility(View.GONE);
                    additionalParticipantIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    rvAdditionalParticipant.setVisibility(View.VISIBLE);
                    additionalParticipantIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });

        additionalDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rvAdditionalDestination.isShown()) {
                    rvAdditionalDestination.setVisibility(View.GONE);
                    additionalDestinationIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    rvAdditionalDestination.setVisibility(View.VISIBLE);
                    additionalDestinationIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });

        supportingDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rvSupportingDocument.isShown()) {
                    rvSupportingDocument.setVisibility(View.GONE);
                    supportingDocumentIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    rvSupportingDocument.setVisibility(View.VISIBLE);
                    supportingDocumentIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });

        tvFolioNumber = findViewById(R.id.tvFolioNumber);
        levActionContainerLL = findViewById(R.id.levActionContainerLL);
        levNameTV = findViewById(R.id.levNameTV);
        tvLastActivity = findViewById(R.id.tvLastActivity);
        tvLeaveType = findViewById(R.id.tvLeaveType);
        tvReason = findViewById(R.id.tvReason);
        tvLeaveDate = findViewById(R.id.tvLeaveDate);
        tvOutOfTown = findViewById(R.id.tvOutOfTown);
        tvDestCity = findViewById(R.id.tvDestCity);
        tvDestCountry = findViewById(R.id.tvDestCountry);
        etComment = (EditText) findViewById(R.id.etComment);
        btApprove = findViewById(R.id.btApprove);
        btReject = findViewById(R.id.btReject);
        tvLeaveQuota = findViewById(R.id.tvLeaveQuota);
        tvLeaveTotal = findViewById(R.id.tvLeaveTotal);
        tvRemainingQuota = findViewById(R.id.tvRemainingQuota);
        tvLeaveOnProcess = findViewById(R.id.tvLeaveOnProcess);
        tvForecastQuota = findViewById(R.id.tvForecastQuota);

        if (getIntent().hasExtra("type")) {
            tvLastActivity.setText(getIntent().getStringExtra("type"));
            levActionContainerLL.setVisibility(View.GONE);
        }

        loading = new SpotsDialog.Builder().setContext(this).build();
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        LinearLayoutManager llm2 = new LinearLayoutManager(this);
        llm2.setOrientation(LinearLayoutManager.VERTICAL);

        LinearLayoutManager llm3 = new LinearLayoutManager(this);
        llm3.setOrientation(LinearLayoutManager.VERTICAL);

        LinearLayoutManager llm4 = new LinearLayoutManager(this);
        llm4.setOrientation(LinearLayoutManager.VERTICAL);

        LinearLayoutManager llm5 = new LinearLayoutManager(this);
        llm5.setOrientation(LinearLayoutManager.VERTICAL);

        rvAdditionalDestination.setLayoutManager(llm);
        rvAdditionalReviewer.setLayoutManager(llm2);
        rvAdditionalParticipant.setLayoutManager(llm3);
        rvAdditionalDestination.setLayoutManager(llm4);
        rvSupportingDocument.setLayoutManager(llm5);

        buildAlert();

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

        Log.d(TAG, "tmpK2ActionOption = " + tmpK2ActionOption);
        Log.d(TAG, "k2ActionOption = " + new Gson().toJson(k2ActionOption));

        new ButtonApprovalBuilder(this)
                .setModes(k2ActionOption)
                .setModeListener(modeListener)
                .build();

//        btApprove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (etComment.getText().toString().length() > 0) {
//                    sumbitApproval(Constants.APROVAL_APPROVE);
//                } else {
//                    LevDetailActivity.super.pleaseComment();
//                }
//            }
//        });
//
//        btReject.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (etComment.getText().toString().length() > 0) {
//                    sumbitApproval(Constants.APROVAL_REJECT);
//                } else {
//                    LevDetailActivity.super.pleaseComment();
//                }
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getProcessInstance();
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

    private void getProcessInstance() {
        loading.show();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        restApi.getLEV("K2Services", "GetProcessInstance", processInstanceId)
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
                            LevDetailActivity.super.handleError(response);
                            Log.d(TAG, "getProcessInstance false _ " + response.raw().toString());
                        }

                        loading.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        Log.d(TAG, "getWorklistPending onFailure..");
                        LevDetailActivity.super.showError("Failed connect to server");
                        t.printStackTrace();
                    }
                });
    }

    private void parseJsonRequestor(String strJson) {
        Log.d(TAG, "parseJson");

        ArrayList<IamComment> list = new ArrayList<>();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);
        JsonArray processInstance = jsonObject.getAsJsonArray("K2ProcessInstance");
        JsonArray wfRequest = jsonObject.getAsJsonArray("WF_REQUEST");
        JsonArray wfRequestData = jsonObject.getAsJsonArray("WF_REQUEST_DATA");
        JsonArray wfRequestDataSumary = jsonObject.getAsJsonArray("WF_REQUEST_DATA_SUMMARY");
        JsonArray wfRequestActivities = jsonObject.getAsJsonArray("WF_REQUEST_ACTIVITIES");

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
            }
        }

        if (!getIntent().hasExtra("type")) {
            if ((wfRequest != null) && (wfRequest.size() > 0)) {
                JsonObject joLastActivity = wfRequest.get(0).getAsJsonObject();
                String activityName = joLastActivity.get("LastActivityName").getAsString();
                String requestorID = joLastActivity.get("RequestorID").getAsString();
                String createdOn = joLastActivity.get("CreatedOn").getAsString();

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

    private void getEmployeeHeader(String requestorID, String date) {
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticated(this, 2000);
        restApi.getEmployeeHeaderIdentityOfRoleOwner("PersonalAdministrationServices", "GetEmployeeHeaderIdentity", requestorID, date, date)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d(TAG, "sumbitApproval sip.. " + strResponse);
                                parseXml(strResponse, successListener);
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

    LoadProfileListener profileListener = new LoadProfileListener() {
        @Override
        public void loadProfile(String username) {
            Log.d(TAG, "loadProfilePic");
            LevDetailActivity.this.loadProfilePic(username, R.id.ivProfile);
        }
    };

    OnSuccessListener successListener = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            Log.d(TAG, "onSuccess:strJson = " + strJson);

            if (strJson.equalsIgnoreCase("-1")) { // return value success submit worklist
                Toast.makeText(LevDetailActivity.this,
                        getResources().getString(R.string.submit_success), Toast.LENGTH_LONG).show();
                LevDetailActivity.this.finish();
            } else {
                parseJson(strJson);
            }
        }
    };

    OnSuccessListener successListenerRequestor = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            Log.d(TAG, "onSuccess:strJson = " + strJson);

            parseJsonRequestor(strJson);
        }
    };

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

            NodeList nAbsence = dom.getElementsByTagName("PTM_MS_IT_ABSENCES");
            NodeList nAdditionalInfo = dom.getElementsByTagName("AdditionalInfo");
            NodeList reviewer = dom.getElementsByTagName("Reviewer");
            NodeList absenceCalculation = dom.getElementsByTagName("AbsenceCalculation");
            NodeList nParticipant = dom.getElementsByTagName("Participant");
            NodeList nDestination = dom.getElementsByTagName("Destination");
            NodeList nSupportingDocument = dom.getElementsByTagName("SupportingDocument");

            Document docAbsence             = nAbsence.item(0).getOwnerDocument();
            Document docAdditionalInfo      = nAdditionalInfo.item(0).getOwnerDocument();
            Document docAbsenceCalculation  = absenceCalculation.item(0).getOwnerDocument();

            String strStartDate = docAbsence.getElementsByTagName("PBEGDAM").item(0).getTextContent();
            String strEndDate = docAbsence.getElementsByTagName("PENDDAM").item(0).getTextContent();

            Date startDate = StringUtils.toDateYyyyMmDd(strStartDate);
            Date endDate = StringUtils.toDateYyyyMmDd(strEndDate);
            strStartDate = StringUtils.formatDateSimple(startDate);
            strEndDate = StringUtils.formatDateSimple(endDate);

            String leaveType = docAdditionalInfo.getElementsByTagName("LeaveTypeText")
                    .item(0).getTextContent();
            String reason = docAdditionalInfo.getElementsByTagName("Reason")
                    .item(0).getTextContent();
            String leaveDate = strStartDate + " to " + strEndDate;
            String outOfTown =  docAdditionalInfo.getElementsByTagName("TambahanHari")
                    .item(0).getTextContent();
            String destCity =  docAdditionalInfo.getElementsByTagName("DestinationCity")
                    .item(0).getTextContent();
            String destCountry =  docAdditionalInfo.getElementsByTagName("DestinationCountry")
                    .item(0).getTextContent();

            levNameTV.setText(leaveType);
            tvLeaveType.setText(leaveType);
            tvReason.setText(reason);
            tvLeaveDate.setText(leaveDate);
            tvOutOfTown.setText(outOfTown);
            tvDestCity.setText(destCity);
            tvDestCountry.setText(destCountry);

            if (!outOfTown.equalsIgnoreCase("Yes")) {
                additionalDayCL.setVisibility(View.INVISIBLE);
                additionalDay1TV.setVisibility(View.GONE);
            }

            /**
             Field LeaveTotal & TotalLeaveonProcess mengambil dari AdditionalInfo
             Bagian data Sumary Quota, fieldnya nyampur AdditionalInfo dengan AbsenceCalculation
             */

            try {
                String currentQuota = docAbsenceCalculation.getElementsByTagName("CurrentQuota")
                        .item(0).getTextContent();
                String leaveTotal = docAbsenceCalculation.getElementsByTagName("TotalNumberOfWorkingDays")
                        .item(0).getTextContent();
                String remainingQuota = docAbsenceCalculation.getElementsByTagName("RemainingQuota")
                        .item(0).getTextContent();
                String totalLeaveonProcess = docAbsenceCalculation.getElementsByTagName("LeaveOnProcessDays")
                        .item(0).getTextContent();
                int tmpRemainingQuota = Integer.parseInt(remainingQuota);
                int tmpLeaveOnProcess = Integer.parseInt(totalLeaveonProcess.replace(" ", ""));
                int tmpForecastQuota = tmpRemainingQuota - tmpLeaveOnProcess;
                String strForecastQuota = String.valueOf(tmpForecastQuota);

                tvLeaveQuota.setText(currentQuota);
                tvLeaveTotal.setText(leaveTotal);
                tvRemainingQuota.setText(remainingQuota);
                tvLeaveOnProcess.setText(totalLeaveonProcess);
                tvForecastQuota.setText(strForecastQuota);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            try {
                ReviewerLev revLev;
                List<ReviewerLev> revLevList = new ArrayList<>();

                if (reviewer.getLength() > 0) {
                    for (int i = 0; i < reviewer.getLength(); i++) {
                        JSONObject jo = JsonXmlUtils.xmlNodeToJsonNode(reviewer.item(i));

                        revLev = new ReviewerLev();
                        revLev.revPan = jo.getString("ReviewPersonalNumber");
                        revLev.revName = jo.getString("Name");
                        revLev.revPosition = jo.getString("Position");
                        revLev.revADUserName = jo.getString("ADUserName");

                        revLevList.add(revLev);
                    }

                    RevLevAdapter revAdapter = new RevLevAdapter(this, revLevList);
                    rvAdditionalReviewer.setAdapter(revAdapter);

                }
                if (revLevList.size() == 0) {
                    additionalReviewer.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Participant
            try {
                List<Participant> list = new ArrayList<>();

                if (nParticipant.getLength() > 0) {
                    for (int i = 0; i < nParticipant.getLength(); i++) {
                        JSONObject jo = JsonXmlUtils.xmlNodeToJsonNode(nParticipant.item(i));

                        Participant participant = new Participant();
                        participant.name = jo.getString("Name");
                        participant.note= jo.getString("Note");

                        list.add(participant);
                    }

                    Log.d(TAG, "lllist.size() = " + list.size());

                    ParticipantAdapter revAdapter = new ParticipantAdapter(this, list);
                    rvAdditionalParticipant.setAdapter(revAdapter);

                }
                if (list.size() == 0) {
                    additionalParticipant.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Additional Destination
            try {
                List<Destination> list = new ArrayList<>();

                if (nDestination.getLength() > 0) {
                    for (int i = 0; i < nDestination.getLength(); i++) {
                        JSONObject jo = JsonXmlUtils.xmlNodeToJsonNode(nDestination.item(i));

                        Destination data = new Destination();
                        data.from = jo.getString("From");
                        data.to = jo.getString("To");
                        data.countryId = jo.getString("CountryID");
                        data.startDate = jo.getString("SDate");
                        data.endDate = jo.getString("EDate");
                        data.startDate = StringUtils.reformatDateYyyyMmDd(data.startDate);
                        data.endDate = StringUtils.reformatDateYyyyMmDd(data.endDate);
                        data.description = jo.getString("Description");

                        list.add(data);
                    }

                    Log.d(TAG, "lllist.size() = " + list.size());

                    DestinationAdapter adapter = new DestinationAdapter(this, list);
                    rvAdditionalDestination.setAdapter(adapter);
                }
                if (list.size() == 0) {
                    additionalDestination.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // SupportingDoc
            try {
                List<SupportingDoc> list = new ArrayList<>();

                if (nSupportingDocument.getLength() > 0) {
                    for (int i = 0; i < nSupportingDocument.getLength(); i++) {
                        JSONObject jo = JsonXmlUtils.xmlNodeToJsonNode(nSupportingDocument.item(i));

                        SupportingDoc data = new SupportingDoc();
                        data.docId = jo.getString("DocumentID");
                        data.docTypeName = jo.getString("DocumentTypeName");
                        data.docName = jo.getString("DocumentName");
                        data.docDesc = jo.getString("DocumentDescription");
                        data.docIssuer = jo.getString("DocumentIssuer");
                        data.dateOfIssue = jo.getString("DateOfIssue");
                        data.dateOfIssue = StringUtils.reformatDateYyyyMmDd(data.dateOfIssue);
                        data.docUrl = jo.getString("DocumentURL");

                        list.add(data);
                    }

                    SupportingDocAdapter adapter = new SupportingDocAdapter(this, list);
                    rvSupportingDocument.setAdapter(adapter);

                }
                if (list.size() == 0) {
                    supportingDocument.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sumbitApproval(String approvalValue) {
        loading.show();
        String folioNumber = tvFolioNumber.getText().toString();

        DataApproval dataApproval = new DataApproval();
        dataApproval.setFolioNumber(folioNumber);
        dataApproval.setK2SerialNumber(k2SerialNumber);
        dataApproval.setK2Action(approvalValue);
        dataApproval.setXmlData("");
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
                                parseXml(strResponse, successListener);
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
}
