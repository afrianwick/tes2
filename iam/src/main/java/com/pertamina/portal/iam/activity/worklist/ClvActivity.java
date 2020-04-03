package com.pertamina.portal.iam.activity.worklist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.pertamina.portal.core.interfaces.OnSuccessListener;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.models.paramsapi.DataApproval;
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.ImageUtils.ImageUtils;
import com.pertamina.portal.core.utils.JsonXmlUtils;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.adapters.LeaveDetailAdapter;
import com.pertamina.portal.iam.fragments.PersonalDataFragment;
import com.pertamina.portal.iam.fragments.WorklistCommentFragment;
import com.pertamina.portal.iam.interfaces.LoadProfileListener;
import com.pertamina.portal.iam.models.IamComment;
import com.pertamina.portal.iam.models.LeaveDetailModel;
import com.pertamina.portal.iam.utils.ButtonApprovalBuilder;

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

public class ClvActivity extends AppCompatActivity {

    private static final String TAG = "CLVActivity";

    private LinearLayout leaveDetailContainer, actionContainerLL;
    private ImageView leaveDetailIV;
    private RecyclerView leaveDetailRV;
    private AlertDialog loading;
    private AlertDialog mAlertDialog;
    private TextView tvLastActivity, tvFolioNumber;
    private String processInstanceId, personalNum, k2SerialNumber;
    private String[] k2ActionOption;
    private EditText etComment;
    private LeaveDetailAdapter leaveDetailAdapter;
    private ImageView backIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clv_detail);

        loading = new SpotsDialog.Builder().setContext(this).build();
        buildAlert();

        leaveDetailContainer = findViewById(R.id.llSectionSeparator);
        leaveDetailIV = findViewById(R.id.clvDetailLeaveDetailIV);
        leaveDetailRV = findViewById(R.id.clvDetailRV);
        tvLastActivity = findViewById(R.id.tvLastActivity);
        tvFolioNumber = findViewById(R.id.tvFolioNumber);
        actionContainerLL = findViewById(R.id.action_container);
        etComment = findViewById(R.id.etComment);
        backIV = findViewById(R.id.ivBack);

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        leaveDetailContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (leaveDetailRV.isShown()) {
                    leaveDetailRV.setVisibility(View.GONE);
                    leaveDetailIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    leaveDetailRV.setVisibility(View.VISIBLE);
                    leaveDetailIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
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

        Log.d(TAG, "tmpK2ActionOption = " + tmpK2ActionOption);
        Log.d(TAG, "k2ActionOption = " + new Gson().toJson(k2ActionOption));

        if (getIntent().hasExtra("type")) {
            tvLastActivity.setText(getIntent().getStringExtra("type"));
            actionContainerLL.setVisibility(View.GONE);
        }

        new ButtonApprovalBuilder(this)
                .setModes(k2ActionOption)
                .setModeListener(modeListener)
                .build();
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
                            handleError(response);
                            Log.d(TAG, "getProcessInstance false _ " + response.raw().toString());
                        }

                        loading.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        Log.d(TAG, "getWorklistPending onFailure..");
                        showError("Failed connect to server");
                        t.printStackTrace();
                    }
                });
    }

    OnSuccessListener successListener = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            Log.d(TAG, "onSuccess:strJson = " + strJson);

            if (strJson.equalsIgnoreCase("-1")) { // return value success submit worklist
                Toast.makeText(ClvActivity.this,
                        getResources().getString(R.string.submit_success), Toast.LENGTH_LONG).show();
                ClvActivity.this.finish();
            } else {
                parseJson(strJson);
            }
        }
    };

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

                if (getIntent().hasExtra("type")) {
                    if (getIntent().getStringExtra("type").equalsIgnoreCase("Approval History")) {
//                    String date = Utils.formatDate(createdOn.split("T")[0], "yyyyMMdd", "yyyy-MM-dd");
//                    getEmployeeHeader(requestorID, date);
                        initPersonalFragment(requestorID, profileListener);
                    }
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

            loadComments(list);
        }
    }

    public void loadComments(ArrayList<IamComment> commentList) {
        Log.d(TAG, "loadComments::" + commentList.size());

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flComments, WorklistCommentFragment.newInstance(commentList));
        ft.commit();
    }

    public void buildAlert() {
        this.mAlertDialog = new AlertDialog.Builder(this)
                .setNeutralButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                })
                .create();
    }

    public void initPersonalFragment(String personalNum, LoadProfileListener profileListener) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        PersonalDataFragment fragment = PersonalDataFragment.newInstance(personalNum);
        fragment.setProfileListener(profileListener);
        ft.replace(R.id.fmFragmentPersonal, fragment);
        ft.commit();
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

    public void pleaseComment() {
        Toast.makeText(this,
                getResources().getString(R.string.please_comment), Toast.LENGTH_LONG).show();
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
                buildAlert();

                for (int i = 0; i < nodeListError.getLength(); i++) {
                    String strError = nodeListError.item(i).getTextContent();

                    showError(strError);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
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

            try {
                List<LeaveDetailModel> leaveDetailModels = new ArrayList<>();
                NodeList nAddress = dom.getElementsByTagName("PTM_MS_IT_ABSENCES");
                for (int i = 0; i < nAddress.getLength(); i++) {
                    JSONObject jo = JsonXmlUtils.xmlNodeToJsonNode(nAddress.item(i));

                    LeaveDetailModel leaveDetailModel = new LeaveDetailModel();

                    leaveDetailModel.setStartDate(jo.getString("PBEGDAM"));
                    leaveDetailModel.setEndDate(jo.getString("PENDDAM"));
                    leaveDetailModel.setStatus(jo.getString("PRESE1M"));
                    leaveDetailModel.setType(jo.getString("PATEXTM"));

                    leaveDetailModels.add(leaveDetailModel);
                }

                leaveDetailAdapter = new LeaveDetailAdapter(this, leaveDetailModels);
                leaveDetailRV.setLayoutManager(new LinearLayoutManager(this));
                leaveDetailRV.setAdapter(leaveDetailAdapter);
            } catch (TransformerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    LoadProfileListener profileListener = new LoadProfileListener() {
        @Override
        public void loadProfile(String username) {
            Log.d(TAG, "loadProfilePic");
            loadProfilePic(username, R.id.ivProfile);
        }
    };

    protected void loadProfilePic(String username, int resProfileImage) {
        final CircularImageView civ = (CircularImageView) findViewById(resProfileImage);
        username = username.replace("\\", "%5C");
        String url = "ROOT/Public/Images/ProfilePictures/" + username + ".jpg";

        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        restApi.loadImage(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                        civ.setImageBitmap(ImageUtils.scaleDown(bmp, 100, false));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.d(TAG, "load image not success" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "load image onFailure");
                t.printStackTrace();
            }
        });
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
            mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Login again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        startActivity(new Intent(ClvActivity.this,
                                Class.forName("com.pertamina.portal.activity.LoginActivity")));
                        finish();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (strError.contains("404")) {
            message = "Could not get data. File Not Found.";
        }

        if (mAlertDialog == null) {
            this.mAlertDialog = new AlertDialog.Builder(this)
                    .setNeutralButton("close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    })
                    .create();
        }

        mAlertDialog.setMessage(message);

        if (!mAlertDialog.isShowing()) {
            mAlertDialog.show();
        }
    }

}
