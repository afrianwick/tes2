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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.models.paramsapi.DataApproval;
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.JsonXmlUtils;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.base.BaseWorklistActivity;
import com.pertamina.portal.iam.adapters.MCRAdapter;
import com.pertamina.portal.iam.fragments.PersonalDataFragment;
import com.pertamina.portal.iam.fragments.WorklistCommentFragment;
import com.pertamina.portal.iam.interfaces.LoadProfileListener;
import com.pertamina.portal.iam.models.IamComment;
import com.pertamina.portal.iam.models.MCRDetailModel;
import com.pertamina.portal.iam.models.MCRModel;
import com.pertamina.portal.iam.models.PANModel.AddressModel;
import com.pertamina.portal.iam.utils.ButtonApprovalBuilder;
import com.pertamina.portal.iam.utils.DateUtils.DateUtils;
import com.pertamina.portal.iam.utils.XMLParserUtils.XMLParserInterface;
import com.pertamina.portal.iam.utils.XMLParserUtils.XMLParserUtils;

import org.json.JSONException;
import org.json.JSONObject;
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
import javax.xml.transform.TransformerException;

import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MCRActivity extends BaseWorklistActivity {

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
    private RecyclerView mcrRV;
    private LinearLayout mcrContainerLL;
    private ImageView mcrExpandCollapseIV, backIV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcr);

        Bundle extra = getIntent().getExtras();
        processInstanceId = extra.getString(Constants.KEY_PROCESS_INSTANCE);
        k2SerialNumber = extra.getString(Constants.KEY_K2SERIAL_NUM);
        personalNum = extra.getString(Constants.KEY_PERSONAL_NUM);
        String tmpK2ActionOption = extra.getString(Constants.KEY_K2ACTION);

        loading = new SpotsDialog.Builder().setContext(this).build();

        backIV = findViewById(R.id.ivBack);
        folioNameTV = findViewById(R.id.mcrFolioNumberTV);
        mcrExpandCollapseIV = findViewById(R.id.expandCollapseIV);
        actionContainerLL = findViewById(R.id.actionContainerLL);
        lastActivityTV = findViewById(R.id.mcrLasActivityTV);
        commentET = findViewById(R.id.commentET);
        mcrRV = findViewById(R.id.mcrRV);
        mcrContainerLL = findViewById(R.id.llSectionSeparator);

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

        mcrContainerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mcrRV.isShown()) {
                    mcrRV.setVisibility(View.GONE);
                    mcrExpandCollapseIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    mcrRV.setVisibility(View.VISIBLE);
                    mcrExpandCollapseIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
                                XMLParserUtils.parseXml(strResponse, new XMLParserInterface() {
                                    @Override
                                    public void onSuccess(String result) {
                                        parseJson(result);
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

    public void pleaseComment() {
        Toast.makeText(this,
                getResources().getString(R.string.please_comment), Toast.LENGTH_LONG).show();
    }

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
                                XMLParserUtils.parseXml(strResponse, new XMLParserInterface() {
                                    @Override
                                    public void onSuccess(String result) {
                                        Log.d(TAG, "onSuccess:result = " + result);

                                        if (result.equalsIgnoreCase("-1")) { // return value success submit worklist
                                            Toast.makeText(MCRActivity.this,
                                                    getResources().getString(R.string.submit_success), Toast.LENGTH_LONG).show();
                                            MCRActivity.this.finish();
                                        } else {
                                            parseJson(result);
                                        }
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
            }
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
            String data = xmlData;
            data = data.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
            data = data.replaceAll("\\+", "%2B");
            xmlData = URLDecoder.decode(data, "UTF-8");

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

            if (getMCRModel(dom) == null) {
                Toast.makeText(this, "Something when wrong to the response or data does not exist", Toast.LENGTH_LONG).show();
                return;
            }

            Log.d("mcrdetail", new Gson().toJson(getMCRDetailModel(dom)));
            MCRAdapter mcrAdapter = new MCRAdapter(this, getMCRModel(dom), getMCRDetailModel(dom));
            mcrRV.setLayoutManager(new LinearLayoutManager(this));
            mcrRV.setAdapter(mcrAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initPersonalFragment(String personalNum, LoadProfileListener profileListener) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        PersonalDataFragment fragment = PersonalDataFragment.newInstance(personalNum);
        fragment.setProfileListener(profileListener);
        ft.replace(R.id.fmFragmentPersonal, fragment);
        ft.commit();
    }

    LoadProfileListener profileListener = new LoadProfileListener() {
        @Override
        public void loadProfile(String username) {
            Log.d(TAG, "loadProfilePic");
            MCRActivity.this.loadProfilePic(username);
        }
    };

    public void loadProfilePic(String username) {
        super.loadProfilePic(username, R.id.ivProfile);
    }

    public static List<MCRModel> getMCRModel(Document dom) {
        try {
            List<MCRModel> mcrModels = new ArrayList<>();
            NodeList nAddress = dom.getElementsByTagName("PTM_MS_IT_PISA_IDENTITY");
            for (int i = 0; i < nAddress.getLength(); i++) {
                JSONObject jo = JsonXmlUtils.xmlNodeToJsonNode(nAddress.item(i));

                MCRModel mcrModel = new MCRModel();
//                Action: PRESE1M ("IN" = New, "UP" = Update, "DE" = Delete)

                mcrModel.setName(jo.getString("PCONAMM"));
                mcrModel.setBloodType(jo.getString("PBLOODM"));
//                mcrModel.set(jo.getString("PNOPISAM"));
                mcrModel.setClinicCode(jo.getString("PCLINICM"));
                mcrModel.setClinicName(jo.getString("PCLINICM_TEXT"));
                mcrModel.setStartDate(jo.getString("PBEGDAM"));
                mcrModel.setEndDate(jo.getString("PENDDAM"));
                mcrModel.setStatus(getStatus(jo.getString("PRESE1M")));

                mcrModels.add(mcrModel);
            }

            return mcrModels;
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String getStatus(String status) {
        switch (status) {
            case "IN":
                return "New";
            case "UP":
                return "Update";
            case "DE":
                return "Delete";
            default:
                return "";
        }
    }

    public static List<MCRDetailModel> getMCRDetailModel(Document dom) {
        try {
            List<MCRDetailModel> mcrDetailModels = new ArrayList<>();
            NodeList nAddress = dom.getElementsByTagName("PTM_MS_IT_PISA_IDENTITY");
            for (int i = 0; i < nAddress.getLength(); i++) {
                JSONObject jo = JsonXmlUtils.xmlNodeToJsonNode(nAddress.item(i));

                MCRDetailModel mcrDetailModel = new MCRDetailModel();

                DateUtils dateUtils = new DateUtils();

                mcrDetailModel.setStartDate(dateUtils.setInputDate(jo.getString("PBEGDAM")).setInputPattern("yyyyMMdd").setOutputPattern("dd-MM-yyyy").build());
                mcrDetailModel.setEndDate(dateUtils.setInputDate(jo.getString("PENDDAM")).setInputPattern("yyyyMMdd").setOutputPattern("dd-MM-yyyy").build());
//              mcrModel.set(jo.getString("PNOPISAM"));

                if (jo.isNull("PFAMSAM_TEXT")) {
                    mcrDetailModel.setFamilyMember("");
                } else {
                    mcrDetailModel.setFamilyMember(jo.getString("PFAMSAM_TEXT"));
                }

                if (jo.isNull("FamilyTypeText")) {
                    mcrDetailModel.setFamilyType("");
                } else {
                    mcrDetailModel.setFamilyType(jo.getString("FamilyTypeText"));
                }

                if (jo.isNull("AGEOFYEAR")) {
                    mcrDetailModel.setAgeYear("0");
                } else {
                    mcrDetailModel.setAgeYear(jo.getString("AGEOFYEAR"));
                }

                if (jo.isNull("AGEOFMONTH")) {
                    mcrDetailModel.setAgeMonth("0");
                } else {
                    mcrDetailModel.setAgeMonth(jo.getString("AGEOFMONTH"));
                }

                if (jo.isNull("PNOPISAM")) {
                    mcrDetailModel.setPisaNumber("");
                } else {
                    mcrDetailModel.setPisaNumber(jo.getString("PNOPISAM"));
                }

                if (jo.isNull("PPOLIM")) {
                    mcrDetailModel.setClinicName("");
                } else {
                    mcrDetailModel.setClinicName(jo.getString("PPOLIM"));
                }

                if (jo.isNull("PBLOODM")) {
                    mcrDetailModel.setBloodType("");
                } else {
                    mcrDetailModel.setBloodType(jo.getString("PBLOODM"));
                }

                mcrDetailModels.add(mcrDetailModel);
            }

            return mcrDetailModels;
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
