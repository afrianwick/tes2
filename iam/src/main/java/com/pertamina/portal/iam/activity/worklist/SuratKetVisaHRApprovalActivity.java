package com.pertamina.portal.iam.activity.worklist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.pertamina.portal.core.interfaces.OnSuccessListener;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.models.paramsapi.DataApproval;
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.RmjDetailActivity;
import com.pertamina.portal.iam.activity.base.BaseWorklistActivity;
import com.pertamina.portal.iam.adapters.worklist.RmjAdapter;
import com.pertamina.portal.iam.fragments.PersonalDataFragment;
import com.pertamina.portal.iam.fragments.WorklistCommentFragment;
import com.pertamina.portal.iam.interfaces.LoadProfileListener;
import com.pertamina.portal.iam.models.IamComment;
import com.pertamina.portal.iam.models.worklist.RmjData;
import com.pertamina.portal.iam.utils.ButtonApprovalBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.jakewharton.picasso.OkHttp3Downloader;
//import com.squareup.picasso.Picasso;

public class SuratKetVisaHRApprovalActivity extends BaseWorklistActivity {

    public static final String TAG = "RmjDetailAct";
    private AlertDialog loading;
    private AlertDialog alertDialog;
    private String processInstanceId;
    private String personalNum;
    private CircularImageView ivProfile;
    private RecyclerView recyclerview;
    private TextView tvConsulate;
    private TextView tvDestCountry;
    private TextView tvDestCity;
    private TextView tvPurpose;
    private TextView tvDate;
    private TextView tvDescription;
    private TextView titleTV;
    private TextView folioNumberTV;
    private TextView tvLastActivity;
    private LinearLayout actionContainerLL;
    private ImageView backIV;
    private TextInputEditText typeOfLetterET, etPurpose, familyMemberET;
    private String sketType = "";
    private TextInputLayout tilFamilyMember, tilPurpose, tilTypeOfLetter;
    private LinearLayout headerSuratKeteranganExpandCollapseLL;
    private LinearLayout headerCommentSuratKeteranganExpandCollapseLL;
    private LinearLayout headerCommentSuratKeteranganContainerLL;
    private LinearLayout headerSuratKeteranganContainerLL;
    private ImageView sketExpandCollapseIV;
    private EditText etComment;
    private String[] k2ActionOption;
    private String k2SerialNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surat_ket_visa_hr_approval);

        tvConsulate = findViewById(R.id.tvConsulate);
        etPurpose = findViewById(R.id.etPurpose);
        etComment = findViewById(R.id.etComment);
        tilTypeOfLetter = findViewById(R.id.tilTypeOfLetter);
        tilFamilyMember = findViewById(R.id.tilFamilyMember);
        tilPurpose = findViewById(R.id.tilPurpose);
        familyMemberET = findViewById(R.id.etFamilyMember);
        folioNumberTV = findViewById(R.id.tvFolioNumber);
        tvLastActivity = findViewById(R.id.tvLastActivity);
        tvDestCountry = findViewById(R.id.tvDestCountry);
        tvDestCity = findViewById(R.id.tvDestCity);
        tvPurpose = findViewById(R.id.tvPurpose);
        tvDate = findViewById(R.id.tvDate);
        tvDescription = findViewById(R.id.tvDescription);
        titleTV = findViewById(R.id.suratKetDetailTitleTV);
        actionContainerLL = findViewById(R.id.linearLayout3);
        headerSuratKeteranganExpandCollapseLL = findViewById(R.id.llSectionSeparator);
        headerSuratKeteranganContainerLL = findViewById(R.id.surateKetVisaContainerDetailLL);
        sketExpandCollapseIV = findViewById(R.id.sketExpandCollapseIV);

        headerCommentSuratKeteranganExpandCollapseLL = findViewById(R.id.llSectionSeparator);
        headerCommentSuratKeteranganContainerLL = findViewById(R.id.surateKetVisaContainerDetailLL);
        backIV = findViewById(R.id.ivBack);
        typeOfLetterET = findViewById(R.id.etTypeOfLetter);

        headerSuratKeteranganExpandCollapseLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (headerSuratKeteranganContainerLL.isShown()) {
                    headerSuratKeteranganContainerLL.setVisibility(View.GONE);
                    sketExpandCollapseIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    headerSuratKeteranganContainerLL.setVisibility(View.VISIBLE);
                    sketExpandCollapseIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (getIntent().hasExtra("type")) {
            actionContainerLL.setVisibility(View.GONE);
            tvLastActivity.setText(getIntent().getStringExtra("type"));
        }

//        View cvPersonal1 = findViewById(R.id.cvPersonal1);
//        View cvPersonal2 = findViewById(R.id.cvPersonal2);
        ivProfile = (CircularImageView) findViewById(R.id.ivProfile);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDetail();
            }
        };

//        cvPersonal1.setOnClickListener(listener);
//        cvPersonal2.setOnClickListener(listener);

        // sudah didefine di superclass
        loading = new SpotsDialog.Builder().setContext(this).build();
        buildAlert();

        Bundle extra = getIntent().getExtras();
        processInstanceId = extra.getString(Constants.KEY_PROCESS_INSTANCE);
        personalNum = extra.getString(Constants.KEY_PERSONAL_NUM);

        String tmpK2ActionOption = extra.getString(Constants.KEY_K2ACTION);
        if (tmpK2ActionOption.contains("|")) {
            k2ActionOption = tmpK2ActionOption.split("\\|");
        } else {
            k2ActionOption = new String[1];
            k2ActionOption[0] = tmpK2ActionOption;
        }
        k2ActionOption = tmpK2ActionOption.split("\\|");
        k2SerialNumber = extra.getString(Constants.KEY_K2SERIAL_NUM);
        loading = new SpotsDialog.Builder().setContext(this).build();

        Log.d(TAG, "tmpK2ActionOption = " + tmpK2ActionOption);
        Log.d(TAG, "k2ActionOption = " + new Gson().toJson(k2ActionOption));

        new ButtonApprovalBuilder(this)
                .setModes(k2ActionOption)
                .setModeListener(modeListener)
                .build();
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

    @Override
    protected void onStart() {
        super.onStart();
        getProcessInstance();
//        initPersonalFragment();
        if (getIntent().hasExtra("type")) {
            if (!getIntent().getStringExtra("type").equalsIgnoreCase("Approval History")) {
                super.initPersonalFragment(personalNum, profileListener);
            }
        } else {
            super.initPersonalFragment(personalNum, profileListener);
        }
    }

    private void initPersonalFragment() {
        Log.d("berapa", personalNum);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        PersonalDataFragment fragment = PersonalDataFragment.newInstance(personalNum);
        fragment.setProfileListener(profileListener);
        ft.replace(R.id.fmFragmentPersonal, fragment);
        ft.commit();
    }

//    private void loadComments(ArrayList<IamComment> commentList) {
//        Log.d(TAG, "loadComments: " + commentList.size());
//
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.flComments, WorklistCommentFragment.newInstance(commentList));
//        ft.commit();
//    }

    private void goToDetail() {
        startActivity(new Intent(this, RmjDetailActivity.class));
    }

    private void getProcessInstance() {
        loading.show();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        restApi.getSuratKetVisa("K2Services", "GetProcessInstance", processInstanceId)
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
                            Log.d(TAG, "getProcessInstance false _ " + response.raw().toString());
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

    private void sumbitApproval(String approvalValue) {
        loading.show();
        String folioNumber = folioNumberTV.getText().toString();

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

    private void parseJson(String strJson) {
        ArrayList<IamComment> list = new ArrayList<>();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);
        JsonArray processInstance = jsonObject.getAsJsonArray("K2ProcessInstance");
        JsonArray wfRequest = jsonObject.getAsJsonArray("WF_REQUEST");
        JsonArray wfRequestData = jsonObject.getAsJsonArray("WF_REQUEST_DATA");
        JsonArray wfRequestDataSumary = jsonObject.getAsJsonArray("WF_REQUEST_DATA_SUMMARY");
        JsonArray wfRequestActivities = jsonObject.getAsJsonArray("WF_REQUEST_ACTIVITIES");

        String xmlData = "";

        if (wfRequestData != null && wfRequestData.size() > 0) {
            JsonObject joWf = wfRequestData.get(0).getAsJsonObject();
            xmlData = joWf.get("XMLData").getAsString();
        }

        if ((processInstance != null) && (processInstance.size() > 0)) {
            JsonObject joK2PI = processInstance.get(0).getAsJsonObject();
            String folio = joK2PI.get("Folio").getAsString();

            if (folio != null) {
                folioNumberTV.setText(folio);
            }
        }

        if ((processInstance != null) && (processInstance.size() > 0)) {
            JsonObject joK2PI = processInstance.get(0).getAsJsonObject();
            String folio = joK2PI.get("DocType_String_ProcDataField").getAsString();

            if (folio != null) {
                titleTV.setText(folio);
                typeOfLetterET.setText(folio);
            }
        }

//        if ((wfRequest != null) && (wfRequest.size() > 0)) {
//            JsonObject joLastActivity = wfRequest.get(0).getAsJsonObject();
//            String activityName = joLastActivity.get("LastActivityName").getAsString();
//
//            if (activityName != null) {
//                tvLastActivity.setText(activityName);
//            }
//        }

        if (getIntent().hasExtra("type")) {
            if (getIntent().getStringExtra("type").equalsIgnoreCase("Approval History")) {
                if ((wfRequest != null) && (wfRequest.size() > 0)) {
                    JsonObject joLastActivity = wfRequest.get(0).getAsJsonObject();
                    String requestorID = joLastActivity.get("RequestorID").getAsString();
                    String createdOn = joLastActivity.get("CreatedOn").getAsString();

                    if (getIntent().getStringExtra("type").equalsIgnoreCase("Approval History")) {
//                    String date = Utils.formatDate(createdOn.split("T")[0], "yyyyMMdd", "yyyy-MM-dd");
//                    getEmployeeHeader(requestorID, date);
//                        super.initPersonalFragment(requestorID, profileListener);
                        personalNum = requestorID;
                        initPersonalFragment();
                    }
                }
            }
        }

        if ((processInstance != null) && (processInstance.size() > 0)) {
            JsonObject joLastActivity = processInstance.get(0).getAsJsonObject();
            sketType = joLastActivity.get("TemplateName_String_ProcDataField").getAsString();
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

    public void loadProfilePic(String username) {
        super.loadProfilePic(username, R.id.ivProfile);
    }

    OnSuccessListener successListener = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            if (strJson.equalsIgnoreCase("-1")) { // return value success submit worklist
                Toast.makeText(SuratKetVisaHRApprovalActivity.this,
                        getResources().getString(R.string.submit_success), Toast.LENGTH_LONG).show();
                SuratKetVisaHRApprovalActivity.this.finish();
            } else {
                parseJson(strJson);
            }
        }
    };

    public void parseWfData(String strXml) {
        DocumentBuilderFactory factory;
        DocumentBuilder builder;
        Document dom;

        try {
            InputStream is = new ByteArrayInputStream(strXml.getBytes("utf-8"));
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            dom = builder.parse(is);

            String templateName = dom.getElementsByTagName("TemplateName").item(0).getTextContent();

            if (templateName.equalsIgnoreCase(getResources().getStringArray(R.array.leave_type_sket_id)[0]) || templateName.equalsIgnoreCase(getResources().getStringArray(R.array.leave_type_sket_id)[1])) {
                // TODO handle SKET NON VISA
                String purpose = dom.getElementsByTagName("Purpose").item(0).getTextContent();
                String familyMemberName = dom.getElementsByTagName("FamilyMember").item(0).getTextContent();

                Log.d("nama surat", sketType);

                tilPurpose.setVisibility(View.GONE);
                tilTypeOfLetter.setVisibility(View.GONE);
                tilFamilyMember.setVisibility(View.GONE);
                if (sketType.equalsIgnoreCase(getResources().getStringArray(R.array.leave_type_sket_id)[0]) || sketType.equalsIgnoreCase(getResources().getStringArray(R.array.leave_type_sket_id)[1])) {
                    tilPurpose.setVisibility(View.VISIBLE);
                    tilFamilyMember.setVisibility(View.VISIBLE);
                    tilTypeOfLetter.setVisibility(View.VISIBLE);
                    etPurpose.setText(purpose);
                    familyMemberET.setText(familyMemberName);
                }
            }
            else if (templateName.equalsIgnoreCase("SKET_VISA_BUSINESS_EN")) {
                // TODO handle SKET VISA
                String embassyConsulate = dom.getElementsByTagName("EmbassyConsulate").item(0).getTextContent();
                String destinationCountry = dom.getElementsByTagName("DestinationCountry").item(0).getTextContent();
                String destinationCity = dom.getElementsByTagName("DestinationCity").item(0).getTextContent();
                String keperluan = dom.getElementsByTagName("DocType").item(0).getTextContent();
                String startDateText = dom.getElementsByTagName("StartDate_Text").item(0).getTextContent();
                String endDateText = dom.getElementsByTagName("EndDate_Text").item(0).getTextContent();
                String description = dom.getElementsByTagName("Description").item(0).getTextContent();
                String purpose = dom.getElementsByTagName("Purpose").item(0).getTextContent();
                String familyMemberName = dom.getElementsByTagName("FamilyMember").item(0).getTextContent();

                Log.d(TAG, "embassyConsulate = " + embassyConsulate);

                tvConsulate.setText(embassyConsulate);
                tvDestCountry.setText(destinationCountry);
                tvDestCity.setText(destinationCity);
                tvPurpose.setText(keperluan);
                tvDate.setText(startDateText + " to " + endDateText);
                tvDescription.setText(description);

                Log.d("nama surat", sketType);

                tilPurpose.setVisibility(View.GONE);
                tilTypeOfLetter.setVisibility(View.GONE);
                tilFamilyMember.setVisibility(View.GONE);
                if (sketType.equalsIgnoreCase(getResources().getStringArray(R.array.leave_type_sket_id)[0]) || sketType.equalsIgnoreCase(getResources().getStringArray(R.array.leave_type_sket_id)[1])) {
                    tilPurpose.setVisibility(View.VISIBLE);
                    tilFamilyMember.setVisibility(View.VISIBLE);
                    tilTypeOfLetter.setVisibility(View.VISIBLE);
                    etPurpose.setText(purpose);
                    familyMemberET.setText(familyMemberName);
                }

                tvConsulate.setVisibility(View.VISIBLE);
                tvDestCountry.setVisibility(View.VISIBLE);
                tvDestCity.setVisibility(View.VISIBLE);
                tvPurpose.setVisibility(View.VISIBLE);
                tvDate.setVisibility(View.VISIBLE);
                tvDescription.setVisibility(View.VISIBLE);


            } else {
                // TODO handle non VISA
                String purpose = dom.getElementsByTagName("Purpose").item(0).getTextContent();

                tilPurpose.setVisibility(View.VISIBLE);
                tilTypeOfLetter.setVisibility(View.VISIBLE);
                etPurpose.setText(purpose);
            }
        } catch (Exception e) {
            // TODO handle SKET NON VISA
        }
    }

    LoadProfileListener profileListener = new LoadProfileListener() {
        @Override
        public void loadProfile(String username) {
            Log.d(TAG, "loadProfilePic");
            SuratKetVisaHRApprovalActivity.this.loadProfilePic(username);
        }
    };
}
