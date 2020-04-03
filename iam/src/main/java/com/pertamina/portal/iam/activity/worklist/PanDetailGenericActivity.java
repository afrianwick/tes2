package com.pertamina.portal.iam.activity.worklist;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.core.utils.StringUtils;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.RmjDetailActivity;
import com.pertamina.portal.iam.activity.base.BaseWorklistActivity;
import com.pertamina.portal.iam.adapters.worklist.BpjsAdapter;
import com.pertamina.portal.iam.adapters.worklist.CommunicationAdapter;
import com.pertamina.portal.iam.adapters.worklist.EducationAdapter;
import com.pertamina.portal.iam.adapters.worklist.PersonalIdAdapter;
import com.pertamina.portal.iam.adapters.worklist.TaxAdapter;
import com.pertamina.portal.iam.adapters.worklist.UniformAdapter;
import com.pertamina.portal.iam.fragments.PersonalDataFragment;
import com.pertamina.portal.iam.fragments.WorklistCommentFragment;
import com.pertamina.portal.iam.interfaces.LoadProfileListener;
import com.pertamina.portal.iam.models.IamComment;
import com.pertamina.portal.iam.models.worklist.ContactData;
import com.pertamina.portal.iam.models.worklist.EducationData;
import com.pertamina.portal.iam.models.worklist.PersonalIdData;
import com.pertamina.portal.iam.models.worklist.TaxData;
import com.pertamina.portal.iam.models.worklist.UniformData;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.jakewharton.picasso.OkHttp3Downloader;
//import com.squareup.picasso.Picasso;

public class PanDetailGenericActivity extends BaseWorklistActivity {

    public static final String TAG = "PanDetail";
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
    private TextView tvFolioNumber;
    private TextView tvLastActivity;
    private TextView tvDataType;
    private LinearLayout action_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pan_detail_generic);
        super.onCreateBackable(this, R.id.ivBack);

        tvFolioNumber = (TextView) findViewById(R.id.tvFolioNumber);
        tvLastActivity = (TextView) findViewById(R.id.tvLastActivity);

        if (getIntent().hasExtra("type")) {
            tvLastActivity.setText(getIntent().getStringExtra("type"));
            action_container.setVisibility(View.GONE);
        }

        tvConsulate = (TextView) findViewById(R.id.tvConsulate);
        action_container = findViewById(R.id.action_container);
        tvDestCountry = (TextView) findViewById(R.id.tvDestCountry);
        tvDestCity = (TextView) findViewById(R.id.tvDestCity);
        tvPurpose = (TextView) findViewById(R.id.tvPurpose);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvDataType = (TextView) findViewById(R.id.tvDataType);

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

        LinearLayoutManager llm = new LinearLayoutManager(this);

        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview.setLayoutManager(llm);
        buildAlert();

        Bundle extra = getIntent().getExtras();
        processInstanceId = extra.getString(Constants.KEY_PROCESS_INSTANCE);
        personalNum = extra.getString(Constants.KEY_PERSONAL_NUM);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getProcessInstance();
        initPersonalFragment();
    }

    private void initPersonalFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        PersonalDataFragment fragment = PersonalDataFragment.newInstance(personalNum);
        fragment.setProfileListener(profileListener);
        ft.replace(R.id.fmFragmentPersonal, fragment);
        ft.commit();
    }

//    private void loadComments(ArrayList<IamComment> commentList) {
//        Log.d(TAG, "loadComments::" + commentList.size());
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
                        PanDetailGenericActivity.super.showError("Failed to connect to Server");
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
                tvFolioNumber.setText(folio);
            }
        }

        if (!getIntent().hasExtra("type")) {
            if ((wfRequest != null) && (wfRequest.size() > 0)) {
                JsonObject joLastActivity = wfRequest.get(0).getAsJsonObject();
                String activityName = joLastActivity.get("LastActivityName").getAsString();

                if (activityName != null) {
                    tvLastActivity.setText(activityName);
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

//    private void loadMainDataList(ArrayList<BpjsData> dataList) {
//        Log.d(TAG, "loadMainDataList");
//
//        LinearLayoutManager llm = new LinearLayoutManager(this);
//
//        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerview.setLayoutManager(llm);
//
//        BpjsAdapter adapter = new BpjsAdapter(this, dataList);
//        recyclerview.setAdapter(adapter);
//    }

    public void loadProfilePic(String username) {
        super.loadProfilePic(username, R.id.ivProfile);
    }

    OnSuccessListener successListener = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            parseJson(strJson);
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

            NodeList nPanRoot = dom.getElementsByTagName("PersonalAdministrationDataSet");
            NodeList nJamsostek = dom.getElementsByTagName("PTM_MS_IT_JAMSOSTEK_INSURANCE");
            //sepaket sama ini PTM_MS_IT_BANK_DETAILS

            NodeList nBankDetail = dom.getElementsByTagName("PTM_MS_IT_BANK_DETAILS");
            NodeList nCommunication = dom.getElementsByTagName("PTM_MS_IT_COMMUNICATION");
            NodeList nFamilyMember = dom.getElementsByTagName("PTM_MS_IT_FAMILY_MEMBER_DEPENDENTS");
            NodeList nPersonalData = dom.getElementsByTagName("PTM_MS_IT_PERSONAL_DATA");
            NodeList nUkuranSeragam = dom.getElementsByTagName("PTM_MS_IT_INFOTYPE_UKURAN_SERAGAM");
            NodeList nTaxData = dom.getElementsByTagName("PTM_MS_IT_INDONESIAN_TAX_DATA");
            NodeList nEducation = dom.getElementsByTagName("PTM_MS_IT_EDUCATION");
            NodeList nPersonalID = dom.getElementsByTagName("PTM_MS_IT_PERSONAL_IDS");
            NodeList nAddress = dom.getElementsByTagName("PTM_MS_IT_ADDRESSES");

            int chilNum = nPanRoot.item(0).getChildNodes().getLength();
            Log.d(TAG, "nPanRoot.child list = " + chilNum);

            /**
             * Ini hanya digunakan jika jenis datanya banyak
             */
            if (chilNum == 1) {
                // load single
                if (nJamsostek.getLength() > 0) {
                    tvDataType.setText("BPJS");
                    loadJamsostek(nJamsostek);
                } else if (nCommunication.getLength() > 0) {
                    tvDataType.setText("Communication");
                    loadCommunication(nCommunication);
                } else if (nFamilyMember.getLength() > 0) {
                    tvDataType.setText("Family Member");
//                loadFamilyMember(nFamilyMember);
                } else if (nPersonalData.getLength() > 0) {
                    tvDataType.setText("Personal Data");
//                loadPersonalData(nPersonalData);
                } else if (nTaxData.getLength() > 0) {
                    tvDataType.setText("Tax Data");
                    loadTax(nTaxData);
                } else if (nBankDetail.getLength() > 0) {
                    tvDataType.setText("Bank Detail");
//                loadBankDetail(nBankDetail);
                } else if (nUkuranSeragam.getLength() > 0) {
                    tvDataType.setText("Uniform");
                    loadUniform(nUkuranSeragam);
                } else if (nPersonalID.getLength() > 0) {
                    tvDataType.setText("Personal ID");
                    //TODO loadPersonalId()
                    loadPersonalId(nPersonalID);
                } else if (nAddress.getLength() > 0) {
                    tvDataType.setText("Address");
                    //TODO loadAddress
                    loadPersonalId(nPersonalID);
                } else {
                    tvDataType.setText("Education");
                    loadEducation(nEducation);
                }
            } else {
                // load multiple
                tvDataType.setText("Multiple Data");

                NodeList nodeList = nPanRoot.item(0).getChildNodes();

                for (int i = 0; i < chilNum; i++) {
                    String nodeName = nodeList.item(i).getNodeName();
                    Log.d(TAG, "nodeName = " + nodeName);

                    if (nodeName.equalsIgnoreCase("PTM_MS_IT_BANK_DETAILS")) {
                        NodeList nl = dom.getElementsByTagName("PTM_MS_IT_BANK_DETAILS");
//                        loadBankDetail(nl);
                    } else if (nodeName.equalsIgnoreCase("PTM_MS_IT_COMMUNICATION")) {
                        NodeList nl = dom.getElementsByTagName("PTM_MS_IT_COMMUNICATION");
                        loadCommunication(nl);
                    } else if (nodeName.equalsIgnoreCase("PTM_MS_IT_FAMILY_MEMBER_DEPENDENTS")) {
                        NodeList nl = dom.getElementsByTagName("PTM_MS_IT_FAMILY_MEMBER_DEPENDENTS");
//                        loadFamilyMember(nl);
                    } else if (nodeName.equalsIgnoreCase("PTM_MS_IT_PERSONAL_DATA")) {
                        NodeList nl = dom.getElementsByTagName("PTM_MS_IT_PERSONAL_DATA");
//                        loadPersonalData(nl);
                    } else if (nodeName.equalsIgnoreCase("PTM_MS_IT_INFOTYPE_UKURAN_SERAGAM")) {
                        NodeList nl = dom.getElementsByTagName("PTM_MS_IT_INFOTYPE_UKURAN_SERAGAM");
                        loadUniform(nl);
                    } else if (nodeName.equalsIgnoreCase("PTM_MS_IT_INDONESIAN_TAX_DATA")) {
                        NodeList nl = dom.getElementsByTagName("PTM_MS_IT_INDONESIAN_TAX_DATA");
                        loadTax(nl);
                    } else if (nodeName.equalsIgnoreCase("PTM_MS_IT_EDUCATION")) {
                        NodeList nl = dom.getElementsByTagName("PTM_MS_IT_EDUCATION");
                        loadEducation(nl);
                    } else if (nodeName.equalsIgnoreCase("PTM_MS_IT_PERSONAL_IDS")) {
                        NodeList nl = dom.getElementsByTagName("PTM_MS_IT_PERSONAL_IDS");
                        loadPersonalId(nl);
                    } else if (nodeName.equalsIgnoreCase("PTM_MS_IT_ADDRESSES")) {
                        NodeList nl = dom.getElementsByTagName("PTM_MS_IT_ADDRESSES");
//                        loadAddress(nl);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadJamsostek(NodeList datas) {
        if ((datas != null) && (datas.getLength() > 0)) {
            ArrayList<BpjsData> list = new ArrayList<>();

            for (int i = 0; i < datas.getLength(); i++) {
                Document doc = datas.item(0).getOwnerDocument();

                String sStartDate = doc.getElementsByTagName("PBEGDAM").item(0).getTextContent();
                String sEndDate = doc.getElementsByTagName("PENDDAM").item(0).getTextContent();
                String jamsostekNum = doc.getElementsByTagName("PJAMIDM").item(0).getTextContent();
                String strMarriedStatus = doc.getElementsByTagName("PMARSTM").item(0).getTextContent();
                strMarriedStatus = strMarriedStatus.equalsIgnoreCase("x") ? "Married" : "Single";

                Date startDate = StringUtils.toDateYyyyMmDd(sStartDate);
                Date endDate = StringUtils.toDateYyyyMmDd(sEndDate);
                sStartDate = StringUtils.formatDateSimple(startDate);
                sEndDate = StringUtils.formatDateSimple(endDate);

                BpjsData bpjsData = new BpjsData();
                bpjsData.startDate = sStartDate;
                bpjsData.endDate = sEndDate;
                bpjsData.jamsostekNumber = jamsostekNum;
                bpjsData.marritalStatus = strMarriedStatus;

                list.add(bpjsData);
            }

            Log.d(TAG, "loadJamsostek,  = list.size()" + list.size());

            BpjsAdapter adapter = new BpjsAdapter(this, list);
            recyclerview.setAdapter(adapter);
        }
    }

    private void loadCommunication(NodeList datas) {
        if ((datas != null) && (datas.getLength() > 0)) {
            ArrayList<ContactData> list = new ArrayList<>();

            for (int i = 0; i < datas.getLength(); i++) {
                Document doc = datas.item(0).getOwnerDocument();

                String contactLabel = doc.getElementsByTagName("PSUBTYM_TEXT").item(0).getTextContent();
                String contactValue = doc.getElementsByTagName("VALUE").item(0).getTextContent();
                String type = doc.getElementsByTagName("modifyStatus").item(0).getTextContent();
                String sStartDate = doc.getElementsByTagName("PBEGDAM").item(0).getTextContent();
                String sEndDate = doc.getElementsByTagName("PENDDAM").item(0).getTextContent();;

                Date startDate = StringUtils.toDateYyyyMmDd(sStartDate);
                Date endDate = StringUtils.toDateYyyyMmDd(sEndDate);
                sStartDate = StringUtils.formatDateSimple(startDate);
                sEndDate = StringUtils.formatDateSimple(endDate);

                ContactData data = new ContactData();
                data.contactLabel = contactLabel;
                data.contactValue = contactValue;
                data.typeStr = type;
                data.sstrStartDate = sStartDate;
                data.strEndDate = sEndDate;

                list.add(data);
            }

            CommunicationAdapter adapter = new CommunicationAdapter(this, list);
            recyclerview.setAdapter(adapter);
        }
    }

    private void loadEducation(NodeList datas) {
        if ((datas != null) && (datas.getLength() > 0)) {
            ArrayList<EducationData> list = new ArrayList<>();

            for (int i = 0; i < datas.getLength(); i++) {
                Document doc = datas.item(0).getOwnerDocument();

                String educationType = doc.getElementsByTagName("PAUSBIM_TEXT").item(0).getTextContent();
                String institutionAndLocation = doc.getElementsByTagName("PINSTIM_TEXT").item(0).getTextContent();
                String branchOfStudy = doc.getElementsByTagName("PSLTP1M_TEXT").item(0).getTextContent();
                String establishment = doc.getElementsByTagName("PSLARTM_TEXT").item(0).getTextContent();
                String sStartDate = doc.getElementsByTagName("PBEGDAM").item(0).getTextContent();
                String sEndDate = doc.getElementsByTagName("PENDDAM").item(0).getTextContent();
                String actionType = doc.getElementsByTagName("PRESE1M").item(0).getTextContent();

                Date startDate = StringUtils.toDateYyyyMmDd(sStartDate);
                Date endDate = StringUtils.toDateYyyyMmDd(sEndDate);
                sStartDate = StringUtils.formatDateSimple(startDate);
                sEndDate = StringUtils.formatDateSimple(endDate);

                EducationData data = new EducationData();
                data.institutionAndLocation = institutionAndLocation;
                data.branchOfStudy = branchOfStudy;
                data.establishment = establishment;
                data.strStartDate = sStartDate;
                data.strEndDate = sEndDate;
                data.actionType = actionType;

                tvDataType.setText(educationType.trim() + " " + tvDataType.getText());

                list.add(data);
            }

            EducationAdapter adapter = new EducationAdapter(this, list);
            recyclerview.setAdapter(adapter);
        }
    }

    private void loadUniform(NodeList datas) {
        if ((datas != null) && (datas.getLength() > 0)) {
            ArrayList<UniformData> list = new ArrayList<>();

            for (int i = 0; i < datas.getLength(); i++) {
                Document doc = datas.item(0).getOwnerDocument();

                String ukuranBaju = doc.getElementsByTagName("PUK_BAJUM_TEXT").item(0).getTextContent();
                String ukuranCelana = doc.getElementsByTagName("PUK_CELANAM_TEXT").item(0).getTextContent();
                String ukuranSepatu = doc.getElementsByTagName("PUK_SEPATUM_TEXT").item(0).getTextContent();
                Node nodeStartDate = doc.getElementsByTagName("PBEGDAM").item(0);

                // Response API tidak konsisten, padahal sama2 data uniform
                if (nodeStartDate == null) {
                    nodeStartDate = doc.getElementsByTagName("PENDDAM").item(0);
                }
//                PBEGDAM
                String sStartDate = nodeStartDate.getTextContent();
                String sEndDate = doc.getElementsByTagName("PBEGDAM").item(0).getTextContent();
                String actionType = doc.getElementsByTagName("PRESE1M").item(0).getTextContent();

                Date startDate = StringUtils.toDateYyyyMmDd(sStartDate);
                Date endDate = StringUtils.toDateYyyyMmDd(sEndDate);
                sStartDate = StringUtils.formatDateSimple(startDate);
                sEndDate = StringUtils.formatDateSimple(endDate);

                UniformData data = new UniformData();
                data.ukuranBaju = ukuranBaju;
                data.ukuranCelana = ukuranCelana;
                data.ukuranSepatu = ukuranSepatu;
                data.strStartDate = sStartDate;
                data.strEndDate = sEndDate;
                data.actionType = actionType;

                list.add(data);
            }

            UniformAdapter adapter = new UniformAdapter(this, list);
            recyclerview.setAdapter(adapter);
        }
    }

    private void loadTax(NodeList datas) {
        if ((datas != null) && (datas.getLength() > 0)) {
            ArrayList<TaxData> list = new ArrayList<>();

            for (int i = 0; i < datas.getLength(); i++) {
                Document doc = datas.item(0).getOwnerDocument();

                String status = doc.getElementsByTagName("PRESE1M").item(0).getTextContent();
                String npwp = doc.getElementsByTagName("PTAXIDM").item(0).getTextContent();
                String spouseBenefit = doc.getElementsByTagName("STATUS_PAJAK").item(0).getTextContent();
                Node nodeStartDate = doc.getElementsByTagName("PBEGDAM").item(0);

                // Response API tidak konsisten, padahal sama2 data uniform
//                if (nodeStartDate == null) {
//                    nodeStartDate = doc.getElementsByTagName("PBEGDAM").item(0);
//                }
                //PAEDTMM
                String sStartDate = nodeStartDate.getTextContent();
                String sEndDate = doc.getElementsByTagName("PENDDAM").item(0).getTextContent();
                String actionType = doc.getElementsByTagName("PRESE1M").item(0).getTextContent();

                Date startDate = StringUtils.toDateYyyyMmDd(sStartDate);
                Date endDate = StringUtils.toDateYyyyMmDd(sEndDate);
                sStartDate = StringUtils.formatDateSimple(startDate);
                sEndDate = StringUtils.formatDateSimple(endDate);

                TaxData data = new TaxData();
                data.npwp = npwp;
                data.taxStatus = spouseBenefit;
                data.actionType = status;
                data.strStartDate = sStartDate;
                data.strEndDate = sEndDate;
                data.actionType = actionType;

                list.add(data);
            }

            TaxAdapter adapter = new TaxAdapter(this, list);
            recyclerview.setAdapter(adapter);
        }
    }

    private void loadPersonalId(NodeList datas) {
        if ((datas != null) && (datas.getLength() > 0)) {
            ArrayList<PersonalIdData> list = new ArrayList<>();

            for (int i = 0; i < datas.getLength(); i++) {
                Document doc = datas.item(0).getOwnerDocument();

                String actionType = doc.getElementsByTagName("PRESE1M").item(0).getTextContent();
                String idType = doc.getElementsByTagName("PICTYPM_TEXT").item(0).getTextContent();
                String idValue = doc.getElementsByTagName("PICNUMM").item(0).getTextContent();
                Node nodeStartDate = doc.getElementsByTagName("PBEGDAM").item(0);

                String sStartDate = nodeStartDate.getTextContent();
                String sEndDate = doc.getElementsByTagName("PENDDAM").item(0).getTextContent();

                Date startDate = StringUtils.toDateYyyyMmDd(sStartDate);
                Date endDate = StringUtils.toDateYyyyMmDd(sEndDate);
                sStartDate = StringUtils.formatDateSimple(startDate);
                sEndDate = StringUtils.formatDateSimple(endDate);

                PersonalIdData data = new PersonalIdData();
                data.idType = idType;
                data.idValue = idValue;
                data.actionType = actionType;
                data.strStartDate = sStartDate;
                data.strEndDate = sEndDate;

                list.add(data);
            }

            PersonalIdAdapter adapter = new PersonalIdAdapter(this, list);
            recyclerview.setAdapter(adapter);
        }
    }

//    private void loadFamilyMember(NodeList datas) {
//        if ((datas != null) && (datas.getLength() > 0)) {
//            ArrayList<PersonalIdData> list = new ArrayList<>();
//
//            for (int i = 0; i < datas.getLength(); i++) {
//                Document doc = datas.item(0).getOwnerDocument();
//
//                // TODO Update field, Adapter, Layout item
//                String actionType = doc.getElementsByTagName("PRESE1M").item(0).getTextContent();
//                String idType = doc.getElementsByTagName("PICTYPM_TEXT").item(0).getTextContent();
//                String idValue = doc.getElementsByTagName("PICNUMM").item(0).getTextContent();
//                Node nodeStartDate = doc.getElementsByTagName("PBEGDAM").item(0);
//
//                String sStartDate = nodeStartDate.getTextContent();
//                String sEndDate = doc.getElementsByTagName("PENDDAM").item(0).getTextContent();
//
//                Date startDate = StringUtils.toDateYyyyMmDd(sStartDate);
//                Date endDate = StringUtils.toDateYyyyMmDd(sEndDate);
//                sStartDate = StringUtils.formatDateSimple(startDate);
//                sEndDate = StringUtils.formatDateSimple(endDate);
//
//                PersonalIdData data = new PersonalIdData();
//                data.idType = idType;
//                data.idValue = idValue;
//                data.actionType = actionType;
//                data.strStartDate = sStartDate;
//                data.strEndDate = sEndDate;
//
//                list.add(data);
//            }
//
//            PersonalIdAdapter adapter = new PersonalIdAdapter(this, list);
//            recyclerview.setAdapter(adapter);
//        }
//    }

    LoadProfileListener profileListener = new LoadProfileListener() {
        @Override
        public void loadProfile(String username) {
            Log.d(TAG, "loadProfilePic");
            PanDetailGenericActivity.this.loadProfilePic(username);
        }
    };

    public class BpjsData {
        public String startDate, endDate, jamsostekNumber, marritalStatus;
    }
}
