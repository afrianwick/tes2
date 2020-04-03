package com.pertamina.portal.iam.activity.worklist;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.pertamina.portal.iam.activity.base.BaseWorklistActivity;
import com.pertamina.portal.iam.adapters.worklist.DestinationAdapter;
import com.pertamina.portal.iam.adapters.worklist.ParticipantAdapter;
import com.pertamina.portal.iam.adapters.worklist.RevLevAdapter;
import com.pertamina.portal.iam.adapters.worklist.SupportingDocAdapter;
import com.pertamina.portal.iam.interfaces.LoadProfileListener;
import com.pertamina.portal.iam.models.IamComment;
import com.pertamina.portal.iam.models.worklist.Destination;
import com.pertamina.portal.iam.models.worklist.Participant;
import com.pertamina.portal.iam.models.worklist.ReviewerLev;
import com.pertamina.portal.iam.models.worklist.SupportingDoc;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClaimTypeDescActivity extends BaseWorklistActivity {

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
    private TextView tvForecastQuota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lev_detail);
        super.onCreateBackable(this, R.id.ivBack);

    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    private void getData() {
//        TODO implement claim type desc
//        loading.show();
//        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
//        restApi.getLEV("K2Services", "GetProcessInstance", processInstanceId)
//                .enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        if (response.isSuccessful()) {
//                            try {
//                                String strResponse = response.body().string();
//                                Log.d(TAG, "getProcessInstance sip.. " + strResponse);
//                                parseXml(strResponse, successApproveListener);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            ClaimTypeDescActivity.super.handleError(response);
//                            Log.d(TAG, "getProcessInstance false _ " + response.raw().toString());
//                        }
//
//                        loading.dismiss();
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        loading.dismiss();
//                        Log.d(TAG, "getWorklistPending onFailure..");
//                        ClaimTypeDescActivity.super.showError("Failed connect to server");
//                        t.printStackTrace();
//                    }
//                });
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

        if ((wfRequest != null) && (wfRequest.size() > 0)) {
            JsonObject joLastActivity = wfRequest.get(0).getAsJsonObject();
            String activityName = joLastActivity.get("LastActivityName").getAsString();

            if (activityName != null) {
                tvLastActivity.setText(activityName);
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
}
