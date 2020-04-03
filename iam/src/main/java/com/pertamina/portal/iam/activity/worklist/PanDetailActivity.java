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
import com.pertamina.portal.core.utils.StringUtils;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.adapters.PANAdapter.FamilyAdapter;
import com.pertamina.portal.iam.adapters.PANAdapter.AddressAdapter;
import com.pertamina.portal.iam.adapters.PANAdapter.BankAdapter;
import com.pertamina.portal.iam.adapters.PANAdapter.CommunicationAdapter;
import com.pertamina.portal.iam.adapters.PANAdapter.FormalEducationAdapter;
import com.pertamina.portal.iam.adapters.PANAdapter.NPWPAdapter;
import com.pertamina.portal.iam.adapters.PANAdapter.PersonalIDAdapter;
import com.pertamina.portal.iam.adapters.PANAdapter.UniformAdapter;
import com.pertamina.portal.iam.adapters.worklist.PersonalIdAdapter;
import com.pertamina.portal.iam.fragments.PersonalDataFragment;
import com.pertamina.portal.iam.fragments.WorklistCommentFragment;
import com.pertamina.portal.iam.interfaces.LoadProfileListener;
import com.pertamina.portal.iam.models.IamComment;
import com.pertamina.portal.iam.models.PANModel.AddressModel;
import com.pertamina.portal.iam.models.PANModel.BankModel;
import com.pertamina.portal.iam.models.PANModel.CommunicationModel;
import com.pertamina.portal.iam.models.PANModel.FamilyModel;
import com.pertamina.portal.iam.models.PANModel.FormalEducation;
import com.pertamina.portal.iam.models.PANModel.NPWPModel;
import com.pertamina.portal.iam.models.PANModel.PersonalData;
import com.pertamina.portal.iam.models.PANModel.PersonalIDModel;
import com.pertamina.portal.iam.models.worklist.Destination;
import com.pertamina.portal.iam.models.worklist.Participant;
import com.pertamina.portal.iam.models.worklist.ReviewerLev;
import com.pertamina.portal.iam.models.worklist.SupportingDoc;
import com.pertamina.portal.iam.utils.ButtonApprovalBuilder;
import com.pertamina.portal.iam.utils.PANHelper.PANDataUtils;

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

/** Will not be used **/
public class PanDetailActivity extends AppCompatActivity {

    private ImageView ivBack;

    private LinearLayout addressSectionLL, bankAccountSectionLL, communicationSectionLL, formalEducationSectionLL;
    private LinearLayout npwpSectionLL, personalIDSectionLL, employeeDetailSectionLL;

    private LinearLayout maritalStatusContainerForPersonalDataLL;
    private LinearLayout personalDataSectionLL, birthDataSectionLL, familyReligionSectionLL, academicTitleSectionLL, familySectionLL;

    private ImageView addressIV, bankAccountIV, communicationIV, formalEducationIV;
    private ImageView npwpIV, personalIDIV, employeeDetailIV;

    private ImageView personalDataIV, birthDataIV, familyReligionIV, academicTitleIV, familyIV;

    private static final String TAG = "PANActivity";
    private AlertDialog loading, mAlertDialog;

    private TextView tvFolioNumber, tvLastActivity;

    private String processInstanceId, personalNum, k2SerialNumber;
    private String[] k2ActionOption;

    private EditText etComment;

    private LinearLayout actionContainerLL;

    private RecyclerView addressRV, communicationRV, bankRV, npwpRV;
    private RecyclerView uniformRV, personalIDRV, familyRV, formalEducationRV;

    private LinearLayout personalDataContentLL;
    private LinearLayout birthDataContentLL;
    private LinearLayout familyReligionContentLL;
    private LinearLayout academicTitleContent;

    private TextView personalDataNameTV, personalDataTitleTV, personalDataLanguageTV;
    private TextView personalDataGenderTV, personalDataBirthDateTV, personalDataBirthPlaceTV;
    private TextView personalDataCountryTV, personalDataNationalityTV;
    private TextView personalDataReligionTV, personalDataStatusTV, personalDataSinceTV;
    private TextView personalDataAcademicTitleTV, personalDataAcademicTitle2TV, personalDataAcademicTitle3TV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pan_detail);

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvFolioNumber = findViewById(R.id.tvFolioNumber);
        tvLastActivity = findViewById(R.id.tvLastActivity);
        etComment = findViewById(R.id.etComment);
        actionContainerLL = findViewById(R.id.actionContainerLL);

        addressRV = findViewById(R.id.panAddressRV);
        communicationRV = findViewById(R.id.panCommunicationRV);
        bankRV = findViewById(R.id.panBankRV);
        npwpRV = findViewById(R.id.panNPWPRV);
        uniformRV = findViewById(R.id.panUniformRV);
        personalIDRV = findViewById(R.id.panPersonalIDRV);
        familyRV = findViewById(R.id.panFamilyRV);
        formalEducationRV = findViewById(R.id.panFormalEducationRV);

        personalDataContentLL = findViewById(R.id.panPersonalDataContent);
        birthDataContentLL = findViewById(R.id.panBirthDataContent);
        familyReligionContentLL = findViewById(R.id.panFamilyReligionContent);
        academicTitleContent = findViewById(R.id.panAcademicTitleContent);

        personalDataNameTV = findViewById(R.id.personalDataNameTV);
        personalDataTitleTV = findViewById(R.id.personalDataTitleTV);
        personalDataLanguageTV = findViewById(R.id.personalDataLanguageTV);

        personalDataAcademicTitleTV = findViewById(R.id.personalDataAcademicTitleTV);
        personalDataAcademicTitle2TV = findViewById(R.id.personalDataAcademicTitle2TV);
        personalDataAcademicTitle3TV = findViewById(R.id.personalDataAcademicTitle3TV);

        personalDataGenderTV = findViewById(R.id.personalDataGenderTV);
        personalDataBirthDateTV = findViewById(R.id.personalDataBirthDateTV);
        personalDataBirthPlaceTV = findViewById(R.id.personalDataBirthPlaceTV);
        personalDataCountryTV = findViewById(R.id.personalDataCountryTV);
        personalDataNationalityTV = findViewById(R.id.personalDataNationalityTV);

        personalDataReligionTV = findViewById(R.id.personalDataReligionTV);
        personalDataStatusTV = findViewById(R.id.personalDataStatusTV);
        personalDataSinceTV = findViewById(R.id.personalDataSinceTV);

        loading = new SpotsDialog.Builder().setContext(this).build();
        buildAlert();

        sectionSetup();
        sectionExpandCollapseIVSetup();
        sectionClickedSetup();

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
    protected void onStart() {
        super.onStart();
        getProcessInstance();
        if (getIntent().hasExtra("type")) {
            if (!getIntent().getStringExtra("type").equalsIgnoreCase("Approval History")) {
                initPersonalFragment(personalNum, profileListener);
            }
        } else {
            initPersonalFragment(personalNum, profileListener);
        }
    }

    LoadProfileListener profileListener = new LoadProfileListener() {
        @Override
        public void loadProfile(String username) {
            Log.d(TAG, "loadProfilePic");
            loadProfilePic(username, R.id.ivProfile);
        }
    };

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
        restApi.getPAN("K2Services", "GetProcessInstance", processInstanceId)
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

    private void sectionClickedSetup() {
        addressSectionLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addressRV.isShown()) {
                    addressRV.setVisibility(View.GONE);
                    addressIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    addressRV.setVisibility(View.VISIBLE);
                    addressIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });

        bankAccountSectionLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bankRV.isShown()) {
                    bankRV.setVisibility(View.GONE);
                    bankAccountIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    bankRV.setVisibility(View.VISIBLE);
                    bankAccountIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });

        communicationSectionLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (communicationRV.isShown()) {
                    communicationRV.setVisibility(View.GONE);
                    communicationIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    communicationRV.setVisibility(View.VISIBLE);
                    communicationIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });

        formalEducationSectionLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (formalEducationRV.isShown()) {
                    formalEducationRV.setVisibility(View.GONE);
                    formalEducationIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    formalEducationRV.setVisibility(View.VISIBLE);
                    formalEducationIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });

        npwpSectionLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (npwpRV.isShown()) {
                    npwpRV.setVisibility(View.GONE);
                    npwpIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    npwpRV.setVisibility(View.VISIBLE);
                    npwpIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });

        personalIDSectionLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (personalIDRV.isShown()) {
                    personalIDRV.setVisibility(View.GONE);
                    personalIDIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    personalIDRV.setVisibility(View.VISIBLE);
                    personalIDIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });

        employeeDetailSectionLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uniformRV.isShown()) {
                    uniformRV.setVisibility(View.GONE);
                    employeeDetailIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    uniformRV.setVisibility(View.VISIBLE);
                    employeeDetailIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });

        personalDataSectionLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (personalDataContentLL.isShown()) {
                    personalDataContentLL.setVisibility(View.GONE);
                    personalDataIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    personalDataContentLL.setVisibility(View.VISIBLE);
                    personalDataIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });

        birthDataSectionLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (birthDataContentLL.isShown()) {
                    birthDataContentLL.setVisibility(View.GONE);
                    birthDataIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    birthDataContentLL.setVisibility(View.VISIBLE);
                    birthDataIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });

        familyReligionSectionLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (familyReligionContentLL.isShown()) {
                    familyReligionContentLL.setVisibility(View.GONE);
                    familyReligionIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    familyReligionContentLL.setVisibility(View.VISIBLE);
                    familyReligionIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });

        academicTitleSectionLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (academicTitleContent.isShown()) {
                    academicTitleContent.setVisibility(View.GONE);
                    academicTitleIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    academicTitleContent.setVisibility(View.VISIBLE);
                    academicTitleIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });

        familySectionLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (familyRV.isShown()) {
                    familyRV.setVisibility(View.GONE);
                    familyIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    familyRV.setVisibility(View.VISIBLE);
                    familyIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });
    }

    private void sectionExpandCollapseIVSetup() {
        addressIV = findViewById(R.id.addressIV);
        bankAccountIV = findViewById(R.id.bankAcountIV);
        communicationIV = findViewById(R.id.communicationIV);
        formalEducationIV = findViewById(R.id.formalEducationIV);

        npwpIV = findViewById(R.id.npwpIV);
        personalIDIV = findViewById(R.id.personalIDIV);
        employeeDetailIV = findViewById(R.id.employeeDetailIV);

        personalDataIV = findViewById(R.id.panPersonalDataIV);
        birthDataIV = findViewById(R.id.panBirthDataIV);
        familyReligionIV = findViewById(R.id.panFamilyReligionIV);
        academicTitleIV = findViewById(R.id.panAcademicTitleIV);
        familyIV = findViewById(R.id.familyIV);
    }

    private void sectionSetup() {
        addressSectionLL = findViewById(R.id.panSectionAddress);
        bankAccountSectionLL = findViewById(R.id.panSectionBankAccount);
        communicationSectionLL = findViewById(R.id.panSectionComunication);
        formalEducationSectionLL = findViewById(R.id.panSectionFormalEducation);
        npwpSectionLL = findViewById(R.id.panSectionNPWP);
        personalIDSectionLL = findViewById(R.id.panSectionPersonalID);
        employeeDetailSectionLL = findViewById(R.id.panSectionEmployeeDetail);

        maritalStatusContainerForPersonalDataLL = findViewById(R.id.panMaritalStatusContainerLL);
        personalDataSectionLL = findViewById(R.id.panPersonalData);
        birthDataSectionLL = findViewById(R.id.panBirthData);
        familyReligionSectionLL = findViewById(R.id.panFamilyReligion);
        academicTitleSectionLL = findViewById(R.id.panAcademicTitle);
        familySectionLL = findViewById(R.id.panSectionFamily);
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
                        startActivity(new Intent(PanDetailActivity.this,
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

    OnSuccessListener successListener = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            Log.d(TAG, "onSuccess:strJson = " + strJson);

            if (strJson.equalsIgnoreCase("-1")) { // return value success submit worklist
                Toast.makeText(PanDetailActivity.this,
                        getResources().getString(R.string.submit_success), Toast.LENGTH_LONG).show();
                PanDetailActivity.this.finish();
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
                List<AddressModel> addressModels = PANDataUtils.getAddressData(dom);

                if (addressModels == null || addressModels.size() == 0) {
                    addressSectionLL.setVisibility(View.GONE);
                    addressRV.setVisibility(View.GONE);
                } else {
                    AddressAdapter addressAdapter = new AddressAdapter(this, addressModels);
                    addressRV.setLayoutManager(new LinearLayoutManager(this));
                    addressRV.setAdapter(addressAdapter);
                    addressRV.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                addressSectionLL.setVisibility(View.GONE);
                addressRV.setVisibility(View.GONE);
            }

            try {
                List<CommunicationModel> communicationModels = PANDataUtils.getCommunicationData(dom);

                if (communicationModels == null || communicationModels.size() == 0) {
                    communicationSectionLL.setVisibility(View.GONE);
                    communicationRV.setVisibility(View.GONE);
                } else {
                    CommunicationAdapter addressAdapter = new CommunicationAdapter(this, communicationModels);
                    communicationRV.setLayoutManager(new LinearLayoutManager(this));
                    communicationRV.setAdapter(addressAdapter);
                    communicationRV.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                communicationSectionLL.setVisibility(View.GONE);
                communicationRV.setVisibility(View.GONE);
            }

            try {
                List<BankModel> bankModels = PANDataUtils.getBankData(dom);

                if (bankModels == null || bankModels.size() == 0) {
                    bankAccountSectionLL.setVisibility(View.GONE);
                    bankRV.setVisibility(View.GONE);
                } else {
                    BankAdapter addressAdapter = new BankAdapter(this, bankModels);
                    bankRV.setLayoutManager(new LinearLayoutManager(this));
                    bankRV.setAdapter(addressAdapter);
                    bankRV.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                bankAccountSectionLL.setVisibility(View.GONE);
                bankRV.setVisibility(View.GONE);
            }

            try {
                List<NPWPModel> npwpModels = PANDataUtils.getNPWPData(dom);

                if (npwpModels == null || npwpModels.size() == 0) {
                    npwpSectionLL.setVisibility(View.GONE);
                    npwpRV.setVisibility(View.GONE);
                } else {
                    NPWPAdapter addressAdapter = new NPWPAdapter(this, npwpModels);
                    npwpRV.setLayoutManager(new LinearLayoutManager(this));
                    npwpRV.setAdapter(addressAdapter);
                    npwpRV.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                npwpSectionLL.setVisibility(View.GONE);
                npwpRV.setVisibility(View.GONE);
            }

            try {
                List<PersonalIDModel> personalIDModels = PANDataUtils.getPersonalIDData(dom);

                if (personalIDModels == null || personalIDModels.size() == 0) {
                    personalIDSectionLL.setVisibility(View.GONE);
                    personalIDRV.setVisibility(View.GONE);
                } else {
                    PersonalIDAdapter addressAdapter = new PersonalIDAdapter(this, personalIDModels);
                    personalIDRV.setLayoutManager(new LinearLayoutManager(this));
                    personalIDRV.setAdapter(addressAdapter);
                    personalIDRV.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                personalIDSectionLL.setVisibility(View.GONE);
                personalIDRV.setVisibility(View.GONE);
            }

            try {
                List<PersonalIDModel> personalIDModels = PANDataUtils.getUniformData(dom);

                if (personalIDModels == null || personalIDModels.size() == 0) {
                    employeeDetailSectionLL.setVisibility(View.GONE);
                    uniformRV.setVisibility(View.GONE);
                } else {
                    UniformAdapter addressAdapter = new UniformAdapter(this, personalIDModels);
                    uniformRV.setLayoutManager(new LinearLayoutManager(this));
                    uniformRV.setAdapter(addressAdapter);
                    uniformRV.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                employeeDetailSectionLL.setVisibility(View.GONE);
                uniformRV.setVisibility(View.GONE);
            }

            try {
                PersonalData personalData = PANDataUtils.getPersonalData(dom);

                personalDataTitleTV.setText(": " + personalData.getTitle());
                personalDataNameTV.setText(": " + personalData.getName());
                personalDataLanguageTV.setText(": " + personalData.getLanguage());

                personalDataContentLL.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                personalDataSectionLL.setVisibility(View.GONE);
                personalDataContentLL.setVisibility(View.GONE);
            }

            try {
                PersonalData personalData = PANDataUtils.getPersonalData(dom);

                personalDataGenderTV.setText(": " + personalData.getGender());
                personalDataBirthDateTV.setText(": " + personalData.getBirthDate());
                personalDataBirthPlaceTV.setText(": " + personalData.getBirthPlace());
                personalDataCountryTV.setText(": " + personalData.getCountry());
                personalDataNationalityTV.setText(": " + personalData.getNationality());

                birthDataContentLL.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                birthDataSectionLL.setVisibility(View.GONE);
                birthDataContentLL.setVisibility(View.GONE);
            }

            try {
                PersonalData personalData = PANDataUtils.getPersonalData(dom);

                personalDataReligionTV.setText(": " + personalData.getReligion());
                personalDataStatusTV.setText(": " + personalData.getStatus());
                personalDataSinceTV.setText(": " + personalData.getSince());

                familyReligionContentLL.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                familyReligionSectionLL.setVisibility(View.GONE);
                familyReligionContentLL.setVisibility(View.GONE);
            }

            try {
                List<FamilyModel> familyModels = PANDataUtils.getFamilyData(dom);

                if (familyModels == null || familyModels.size() == 0) {
                    familySectionLL.setVisibility(View.GONE);
                    familyRV.setVisibility(View.GONE);
                } else {
                    FamilyAdapter addressAdapter = new FamilyAdapter(this, familyModels);
                    familyRV.setLayoutManager(new LinearLayoutManager(this));
                    familyRV.setAdapter(addressAdapter);
                    familyRV.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                familySectionLL.setVisibility(View.GONE);
                familyRV.setVisibility(View.GONE);
            }

            try {
                PersonalData personalData = PANDataUtils.getPersonalData(dom);

                personalDataAcademicTitleTV.setText(": " + personalData.getTitlePTITELM_TEXT());
                personalDataAcademicTitle2TV.setText(": " + personalData.getSecondTitlePTITL2M_TEXT());
                personalDataAcademicTitle3TV.setText(": " + personalData.getAdditionalTitle());

                academicTitleContent.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                academicTitleSectionLL.setVisibility(View.GONE);
                academicTitleContent.setVisibility(View.GONE);
            }

            try {
                List<FormalEducation> formalEducations = PANDataUtils.getFormalEducation(dom);

                if (formalEducations == null || formalEducations.size() == 0) {
                    formalEducationSectionLL.setVisibility(View.GONE);
                    formalEducationRV.setVisibility(View.GONE);
                } else {
                    FormalEducationAdapter addressAdapter = new FormalEducationAdapter(this, formalEducations);
                    formalEducationRV.setLayoutManager(new LinearLayoutManager(this));
                    formalEducationRV.setAdapter(addressAdapter);
                    formalEducationRV.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                formalEducationSectionLL.setVisibility(View.GONE);
                formalEducationRV.setVisibility(View.GONE);
            }

            // TODO for formal education
//            try {
//                List<> bankModels = PANDataUtils.getBankData(dom);
//
//                if (bankModels == null || bankModels.size() == 0) {
//                    bankAccountSectionLL.setVisibility(View.GONE);
//                    bankRV.setVisibility(View.GONE);
//                } else {
//                    BankAdapter addressAdapter = new BankAdapter(this, bankModels);
//                    bankRV.setLayoutManager(new LinearLayoutManager(this));
//                    bankRV.setAdapter(addressAdapter);
//                }
//            } catch (Exception e) {
//                bankAccountSectionLL.setVisibility(View.GONE);
//                bankRV.setVisibility(View.GONE);
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void pleaseCheckDisclaimer() {
        Toast.makeText(this,
                "Checklis disclaimer", Toast.LENGTH_LONG).show();
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

    public void loadComments(ArrayList<IamComment> commentList) {
        Log.d(TAG, "loadComments::" + commentList.size());

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flComments, WorklistCommentFragment.newInstance(commentList));
        ft.commit();
    }
}
