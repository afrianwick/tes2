package com.pertamina.portal.iam.activity.worklist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class SkmjDetailApprovalActivity extends BaseWorklistActivity {

    public static final String TAG = "RmjDetailAct";
    private AlertDialog loading;
    private AlertDialog alertDialog;
    private String processInstanceId;
    private String personalNum;
    private CircularImageView ivProfile;
    private RecyclerView recyclerview;
    private TextView tvFolioNumber;
    private TextView tvLastActivity;
    private String k2SerialNumber;
    private String[] k2ActionOption;
    private EditText etComment;
    private ImageView transferRPL21BelowExpandCollapseIV;
    private LinearLayout llSectionSeparator, action_container, skmjRequestContainerLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_approval_skmj);
        super.onCreateBackable(this, R.id.ivBack);
        super.buildAlert();

//        View cvPersonal1 = findViewById(R.id.cvPersonal1);
//        View cvPersonal2 = findViewById(R.id.cvPersonal2);
        tvFolioNumber = (TextView) findViewById(R.id.tvFolioNumber);
        action_container = findViewById(R.id.action_container);
        skmjRequestContainerLL = findViewById(R.id.skmjRequestContainerLL);
        llSectionSeparator = findViewById(R.id.llSectionSeparator);
        tvLastActivity = (TextView) findViewById(R.id.tvLastActivity);
        ivProfile = (CircularImageView) findViewById(R.id.ivProfile);
        recyclerview = (RecyclerView) findViewById(R.id.skmjItemRV);
        transferRPL21BelowExpandCollapseIV = findViewById(R.id.transferRPL21BelowExpandCollapseIV);
        etComment = findViewById(R.id.etComment);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDetail();
            }
        };

//        cvPersonal1.setOnClickListener(listener);
//        cvPersonal2.setOnClickListener(listener);

        if (getIntent().hasExtra("type")) {
            tvLastActivity.setText(getIntent().getStringExtra("type"));
            action_container.setVisibility(View.GONE);
        }

        llSectionSeparator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (skmjRequestContainerLL.isShown()) {
                    skmjRequestContainerLL.setVisibility(View.GONE);
                    transferRPL21BelowExpandCollapseIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    skmjRequestContainerLL.setVisibility(View.VISIBLE);
                    transferRPL21BelowExpandCollapseIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });

        loading = new SpotsDialog.Builder().setContext(this).build();
        buildAlert();

        Bundle extra = getIntent().getExtras();
        processInstanceId = extra.getString(Constants.KEY_PROCESS_INSTANCE);
        personalNum = extra.getString(Constants.KEY_PERSONAL_NUM);
        k2SerialNumber = extra.getString(Constants.KEY_K2SERIAL_NUM);

        String tmpK2ActionOption = extra.getString(Constants.KEY_K2ACTION);
        k2ActionOption = tmpK2ActionOption.split("\\|");
        loading = new SpotsDialog.Builder().setContext(this).build();

        Log.d(TAG, "tmpK2ActionOption = " + tmpK2ActionOption);
        Log.d(TAG, "k2ActionOption = " + new Gson().toJson(k2ActionOption));

        new ButtonApprovalBuilder(this)
                .setModes(k2ActionOption)
                .setModeListener(modeListener)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getProcessInstance();
        initPersonalFragment(personalNum);
    }

    private void initPersonalFragment(String personalNum) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        PersonalDataFragment fragment = PersonalDataFragment.newInstance(personalNum);
        fragment.setProfileListener(profileListener);
        ft.replace(R.id.fmFragmentPersonal, fragment);
        ft.commit();
    }

//    private void loadComments(ArrayList<IamComment> commentList) {
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.flComments, WorklistCommentFragment.newInstance(commentList));
//        ft.commit();
//    }

    private void loadMainDataList(ArrayList<RmjData> dataList) {
        LinearLayoutManager llm = new LinearLayoutManager(this);

        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(llm);

        RmjAdapter adapter = new RmjAdapter(this, dataList);
        recyclerview.setAdapter(adapter);
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

    private void goToDetail() {
        startActivity(new Intent(this, RmjDetailActivity.class));
    }

    private void getProcessInstance() {
        loading.show();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        restApi.getSkmjData(processInstanceId, "K2Services", "GetProcessInstance")
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

    private void parseJson(String strJson) {
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

        try {
            xmlData = URLDecoder.decode(xmlData, "UTF-8");

            parseWfData(xmlData);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
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
                Toast.makeText(SkmjDetailApprovalActivity.this,
                        getResources().getString(R.string.submit_success), Toast.LENGTH_LONG).show();
                SkmjDetailApprovalActivity.this.finish();
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
            NodeList nodeParticipant = dom.getElementsByTagName("Participant");

            if ((nodeParticipant != null) && (nodeParticipant.getLength() > 0)) {
                ArrayList<RmjData> list = new ArrayList<>();

                for (int i = 0; i < nodeParticipant.getLength(); i++) {
                    Document doc = nodeParticipant.item(i).getOwnerDocument();

                    System.out.println("disinixml " + doc.getTextContent());

                    String panNum = "";
                    try {
                        panNum = doc.getElementsByTagName("NOPEK").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }
                    String personID = "";
                    try {
                        personID = doc.getElementsByTagName("PersonID").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }
                    String PGBDATM = "";
                    try {
                        PGBDATM = doc.getElementsByTagName("PGBDATM").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }
                    String TMT_JABATAN = "";
                    try {
                        TMT_JABATAN = doc.getElementsByTagName("TMT_JABATAN").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }
                    String TMT_PRL_BS_OLD = "";
                    try {
                        TMT_PRL_BS_OLD = doc.getElementsByTagName("TMT_PRL_BS_OLD").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }
                    String HIRING_DATE = "";
                    try {
                        HIRING_DATE = doc.getElementsByTagName("HIRING_DATE").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }
                    String TMT_PENSIUN = "";
                    try {
                        TMT_PENSIUN = doc.getElementsByTagName("TMT_PENSIUN").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }
                    String FIRSTSCORE = "";
                    try {
                        FIRSTSCORE = doc.getElementsByTagName("FIRSTSCORE").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }
                    String AGE = "";
                    try {
                        AGE = doc.getElementsByTagName("AGE").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }
                    String SECONDSCORE = "";
                    try {
                        SECONDSCORE = doc.getElementsByTagName("SECONDSCORE").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }
                    String namaPekerja = "";
                    try {
                        namaPekerja = doc.getElementsByTagName("PCNAMEM").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }
                    String kodeJabatanLma = "";
                    try {
                        kodeJabatanLma = doc.getElementsByTagName("PPLANSM").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }
                    String namaJabatan = "";
                    try {
                        namaJabatan = doc.getElementsByTagName("PPLTXTM").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }
                    String tipeJabatanMutasi = "";
                    try {
                        tipeJabatanMutasi = doc.getElementsByTagName("PRL").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }
                    String prlJabatanLama = "";
                    try {
                        prlJabatanLama = doc.getElementsByTagName("TYPE_JABATAN_MUTASI").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }

                    String TMT_PRL_BS = "";
                    try {
                        TMT_PRL_BS = doc.getElementsByTagName("TMT_PRL_BS").item(0).getTextContent();
                    } catch (Exception e) {

                    }

                    int prlBB = Integer.parseInt(doc.getElementsByTagName("PRL_BS_BARU").item(0).getTextContent());
                    int prlJB = Integer.parseInt(doc.getElementsByTagName("PRL_NEW").item(0).getTextContent());

                    RmjData data = new RmjData();
                    data.panNumber = panNum;
                    data.namaPekerja = namaPekerja;
                    data.kodeJabatan = kodeJabatanLma;
                    data.namaJabatan = namaJabatan;
                    data.jabatanLama = prlJabatanLama;
                    data.jenisMutasi = tipeJabatanMutasi;
                    data.prlBsBaru = prlBB;
                    data.prlJabatanBaru = prlJB;

                    data.PersonID = personID;
                    data.NOPEK = panNum;
                    data.PCNAMEM = namaPekerja;
                    data.PGBDATM = PGBDATM;
                    data.TMT_JABATAN = TMT_JABATAN;
                    data.TMT_PRL_BS_OLD = TMT_PRL_BS_OLD;
                    data.HIRING_DATE = HIRING_DATE;
                    data.TMT_PENSIUN = TMT_PENSIUN;
                    data.FIRSTSCORE = FIRSTSCORE;
                    data.AGE = AGE;
                    data.SECONDSCORE = SECONDSCORE;

                    String PPLANSM = "";
                    try {
                        PPLANSM = doc.getElementsByTagName("PPLANSM").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }
                    String PPLTXTM = "";
                    try {
                        PPLTXTM = doc.getElementsByTagName("PPLTXTM").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }
                    String PRL = "";
                    try {
                        PRL = doc.getElementsByTagName("PRL").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }
                    String PPERSAM = "";
                    try {
                        PPERSAM = doc.getElementsByTagName("PPERSAM").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }
                    String PPERTXM = "";
                    try {
                        PPERTXM = doc.getElementsByTagName("PPERTXM").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }
                    String PBTRTLM = "";
                    try {
                        PBTRTLM = doc.getElementsByTagName("PBTRTLM").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }
                    String PBTEXTM = "";
                    try {
                        PBTEXTM = doc.getElementsByTagName("PBTEXTM").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }
                    String PDEPTXM = "";
                    try {
                        PDEPTXM = doc.getElementsByTagName("PDEPTXM").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }
                    String PDIVTXM = "";
                    try {
                        PDIVTXM = doc.getElementsByTagName("PDIVTXM").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }
                    String PFUNTXM = "";
                    try {
                        PFUNTXM = doc.getElementsByTagName("PFUNTXM").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }
                    String PDIRTXM = "";
                    try {
                        PDIRTXM = doc.getElementsByTagName("PDIRTXM").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }
                    String PBUKRSM = "";
                    try {
                        PBUKRSM = doc.getElementsByTagName("PBUKRSM").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }
                    String PBUTXTM = "";
                    try {
                        PBUTXTM = doc.getElementsByTagName("PBUTXTM_NEW").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }

                    String TMT_ADMIN_BEGDA = "";
                    try {
                        TMT_ADMIN_BEGDA = doc.getElementsByTagName("TMT_ADMIN_BEGDA").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }

                    String TYPE_MUTASI = "";
                    try {
                        TYPE_MUTASI = doc.getElementsByTagName("TYPE_MUTASI").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }

                    String KETERANGAN_MUTASI = "";
                    try {
                        KETERANGAN_MUTASI = doc.getElementsByTagName("KETERANGAN_MUTASI").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }

                    String ID_POSISI_TUJUAN = "";
                    try {
                        ID_POSISI_TUJUAN = doc.getElementsByTagName("ID_POSISI_TUJUAN").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }

                    String PPLTXTM_NEW = "";
                    try {
                        PPLTXTM_NEW = doc.getElementsByTagName("PPLTXTM_NEW").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }

                    String PRL_NEW = "";
                    try {
                        PRL_NEW = doc.getElementsByTagName("PRL_NEW").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }

                    String PPERSAM_NEW = "";
                    try {
                        PPERSAM_NEW = doc.getElementsByTagName("PPERSAM_NEW").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }

                    String PBTRTLM_NEW = "";
                    try {
                        PBTRTLM_NEW = doc.getElementsByTagName("PBTRTLM_NEW").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }

                    String PDEPTXM_NEW = "";
                    try {
                        PDEPTXM_NEW = doc.getElementsByTagName("PDEPTXM_NEW").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }

                    String PDIVTXM_NEW = "";
                    try {
                        PDIVTXM_NEW = doc.getElementsByTagName("PDIVTXM_NEW").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }

                    String PFUNTXM_NEW = "";
                    try {
                        PFUNTXM_NEW = doc.getElementsByTagName("PFUNTXM_NEW").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }

                    String PDIRTXM_NEW = "";
                    try {
                        PDIRTXM_NEW = doc.getElementsByTagName("PDIRTXM_NEW").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }

                    String PBUKRSM_NEW = "";
                    try {
                        PBUKRSM_NEW = doc.getElementsByTagName("PBUKRSM_NEW").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }

                    String PBUTXTM_NEW = "";
                    try {
                        PBUTXTM_NEW = doc.getElementsByTagName("PBUTXTM_NEW").item(0).getTextContent();
                    } catch (NullPointerException e) {

                    }


                    data.TMT_PRL_BS = TMT_PRL_BS;
                    data.TMT_ADMIN_BEGDA = TMT_ADMIN_BEGDA;
                    data.TYPE_MUTASI = TYPE_MUTASI;
                    data.KETERANGAN_MUTASI = KETERANGAN_MUTASI;
                    data.ID_POSISI_TUJUAN = ID_POSISI_TUJUAN;
                    data.PPLTXTM_NEW = PPLTXTM_NEW;
                    data.PRL_NEW = PRL_NEW;
                    data.PPERSAM_NEW = PPERSAM_NEW;
                    data.PBTRTLM_NEW = PBTRTLM_NEW;
                    data.PDEPTXM_NEW = PDEPTXM_NEW;
                    data.PDIVTXM_NEW = PDIVTXM_NEW;
                    data.PFUNTXM_NEW = PFUNTXM_NEW;
                    data.PDIRTXM_NEW = PDIRTXM_NEW;
                    data.PBUKRSM_NEW = PBUKRSM_NEW;
                    data.PBUTXTM_NEW = PBUTXTM_NEW;
                    data.PPLANSM = PPLANSM;
                    data.PPLTXTM = PPLTXTM;
                    data.PRL = PRL;
                    data.PPERSAM = PPERSAM;
                    data.PPERTXM = PPERTXM;
                    data.PBTRTLM = PBTRTLM;
                    data.PBTEXTM = PBTEXTM;
                    data.PDEPTXM = PDEPTXM;
                    data.PDIVTXM = PDIVTXM;
                    data.PFUNTXM = PFUNTXM;
                    data.PDIRTXM = PDIRTXM;
                    data.PBUKRSM = PBUKRSM;
                    data.PBUTXTM = PBUTXTM;

                    list.add(data);
                }

                loadMainDataList(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    LoadProfileListener profileListener = new LoadProfileListener() {
        @Override
        public void loadProfile(String username) {
            loadProfilePic(username);
        }
    };

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
}
