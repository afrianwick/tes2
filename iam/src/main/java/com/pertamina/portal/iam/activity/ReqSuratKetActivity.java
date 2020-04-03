package com.pertamina.portal.iam.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pertamina.portal.core.interfaces.OnSuccessListener;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.PrefUtils;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.adapters.FamilyAdapter;
import com.pertamina.portal.iam.adapters.FamilyRVAdapter;
import com.pertamina.portal.iam.adapters.KetLeaveType;
import com.pertamina.portal.iam.adapters.TypeOfLetterDescriptionAdapter;
import com.pertamina.portal.iam.interfaces.ReqSuratKetView;
import com.pertamina.portal.iam.interfaces.ReqSuratVisiView;
import com.pertamina.portal.iam.models.FamilyModel;
import com.pertamina.portal.iam.utils.KeyboardUtils;
import com.pertamina.portal.iam.utils.SKetXML;
import com.pertamina.portal.iam.view.IamButton;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReqSuratKetActivity extends AppCompatActivity implements ReqSuratKetView {

    private IamButton submit;
    private EditText commentET;
    private CheckBox disclaimerCB;
    private AlertDialog loading;
    private AlertDialog alertDialog, alertDialogDismiss;
    private EditText etFamilyMember, etTypeOfLatter, etPurpose;
    private ImageView backIV;
    private List<FamilyModel> families;
    private TypeOfLetterDescriptionAdapter typeOfLetterDescriptionAdapter;
    private LinearLayout dialogTypeOfLatterContainerLL;
    private RelativeLayout dialogTypeOfLatterContainerRL;
    private View dialogTypeOfLatterView;
    private RecyclerView dialogTypeOfLetterRV;
    private int typeOfLetterPosition = -1;
    private FamilyModel selectedFamily;

    private View dialogFamilyView;
    private RecyclerView dialogFamilyRV;
    private RelativeLayout dialogFamilyContainerRL;
    private ConstraintLayout dialogFamilyContainerCL;
    private TextView dialogFamilyTV, tvFamilyMember, tvPurpose, tvTypeOfLetter;
    private EditText dialogFamilyET;

    private FamilyRVAdapter familyRVAdapter;

    private String PBEGDAM, PPLANSM, PBUKRSM, PBUTXTM, PDIRTXM, PDIRIDM, RoleAreaName, AreaAddress, CompanyAddress, CompanyLegalName, PERNR, ADUsername, EmailUserID;
    private String PCNAMEM, PPERNRM, PPLANSM_EMPLOYEE_HEADER, PDIRIDM_POSITION_ATTRIBUTE_OF_ROLE_OWNER, PDIRTXM_POSITION_ATTRIBUTE_OF_ROLE_OWNER, PPLSTXM;
//    private String PDIRTXM_EMPLOYEE_HEADER;
    private String PGBDATM_PERSONAL_DATA, PCNAMEM_PERSONAL_DATA, PGBORTM_PERSONAL_DATA, PGBDATM_PERSONAL_DATA_YYYYMMDD, PGBDATM_PERSONAL_DATA_DMMMMYYYY_TEXT, PPERNRM_CURRENT_ORGANIZAIONAL_ASSIGNMENT;
    private int hiringDate;

    private List<String> roleAreaNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_req_surat_ket);

        loading = new SpotsDialog.Builder().setContext(this).build();
        buildAlert();

        dialogTypeOfLatterContainerRL = findViewById(R.id.reqSuratDialogTypOfLetterContainerRL);
        dialogTypeOfLatterView = findViewById(R.id.reqSuratDialogTypOfLetterView);
        dialogTypeOfLetterRV = dialogTypeOfLatterContainerRL.findViewById(R.id.dialogTypeOfLetterRV);
        dialogTypeOfLatterContainerLL = dialogTypeOfLatterContainerRL.findViewById(R.id.dialogTypeOfLetterContainerCL);

        dialogFamilyContainerRL = findViewById(R.id.reqSuratDialogFamilyContainerRL);
        dialogFamilyView = findViewById(R.id.reqSuratDialogFamilyView);
        dialogFamilyRV = dialogFamilyContainerRL.findViewById(R.id.dialogCountryRV);
        dialogFamilyContainerCL = dialogFamilyContainerRL.findViewById(R.id.dialogCountryContainerCL);
        dialogFamilyET = dialogFamilyContainerRL.findViewById(R.id.dialogCountrySearcET);
        dialogFamilyTV = dialogFamilyContainerRL.findViewById(R.id.dialogCountryTitleTV);
        dialogFamilyET.setVisibility(View.GONE);

        dialogFamilyTV.setText("Family");

        backIV = findViewById(R.id.ivBack);
        submit = findViewById(R.id.reqSuratKetSubmitButton);
        commentET = findViewById(R.id.reqSuratKetCommentET);
        disclaimerCB = findViewById(R.id.reqSuratKetDisclaimerCB);
        etTypeOfLatter = findViewById(R.id.etTypeOfLetter);
        etFamilyMember = findViewById(R.id.etFamilyMember);
        etPurpose = findViewById(R.id.etPurpose);
        tvFamilyMember = findViewById(R.id.tvFamilyMember);
        tvPurpose = findViewById(R.id.tvPurpose);
        tvTypeOfLetter = findViewById(R.id.tvTypeOfLetter);

        roleAreaNames = new ArrayList<>();
        roleAreaNames.add("KANTOR_PUSAT_PUSAT");
        roleAreaNames.add("1010_MOR");
        roleAreaNames.add("1010_RU");
        roleAreaNames.add("'KANTOR_PUSAT_SHIPPING'");
        roleAreaNames.add("EP");
        roleAreaNames.add("PGE");
        roleAreaNames.add("PATRA_NIAGA");
        roleAreaNames.add("PEPC");
        roleAreaNames.add("LUBRICANT");
        roleAreaNames.add("PIEP");
        roleAreaNames.add("PHE_ONE");
        roleAreaNames.add("PEPC_UJTB");
        roleAreaNames.add("2021_Asset");
        roleAreaNames.add("2021_Kantor_Pusat");
        roleAreaNames.add("PDSI");
        roleAreaNames.add("PIS");
        roleAreaNames.add("PPI");

        List<String> leaveTypeKet = new ArrayList<>();

        for (int i = 0; i < getResources().getStringArray(R.array.leave_type_sket).length; i++) {
            leaveTypeKet.add(getResources().getStringArray(R.array.leave_type_sket)[i]);
        }

        typeOfLetterDescriptionAdapter = new TypeOfLetterDescriptionAdapter(this, this);
        dialogTypeOfLetterRV.setLayoutManager(new LinearLayoutManager(this));
        dialogTypeOfLetterRV.setAdapter(typeOfLetterDescriptionAdapter);


        etTypeOfLatter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogTypeOfLatterContainerRL.setVisibility(View.VISIBLE);
            }
        });

        etFamilyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogFamilyContainerRL.setVisibility(View.VISIBLE);
            }
        });

        dialogTypeOfLatterContainerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogTypeOfLatterContainerRL.setVisibility(View.GONE);
            }
        });

        dialogFamilyContainerCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogFamilyContainerRL.setVisibility(View.GONE);
            }
        });

        disclaimerCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                submit.setBtnDrawable(getResources().getDrawable(b ? R.drawable.button_bg_rounded_green : R.drawable.button_bg_rounded_gray));
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isComment = true, isTypeOfLetter = true, isFamily = true, isPurpose = true;
                if (typeOfLetterPosition == -1) {
                    etTypeOfLatter.startAnimation(AnimationUtils.loadAnimation(ReqSuratKetActivity.this, R.anim.shake));
                    etTypeOfLatter.setBackground(getResources().getDrawable(R.drawable.edittext_rounded_red));
                    tvTypeOfLetter.setTextColor(getResources().getColor(R.color.redDull));
                    isTypeOfLetter = false;
                    alertDialog.setMessage("Kolom Type of Letter tidak boleh kosong!");
                    alertDialog.show();

                    return;
                }

                if ((typeOfLetterPosition == 0 || typeOfLetterPosition == 1) && selectedFamily == null) {
                    isFamily = false;
                    etFamilyMember.startAnimation(AnimationUtils.loadAnimation(ReqSuratKetActivity.this, R.anim.shake));
                    etFamilyMember.setBackground(getResources().getDrawable(R.drawable.edittext_rounded_red));
                    tvFamilyMember.setTextColor(getResources().getColor(R.color.redDull));
                    alertDialog.setMessage("Kolom Anggota Keluarga tidak boleh kosong!");
                    alertDialog.show();
                    return;
                }

                if (etPurpose.getText().toString().isEmpty()) {
                    isPurpose = false;
                    etPurpose.startAnimation(AnimationUtils.loadAnimation(ReqSuratKetActivity.this, R.anim.shake));
                    etPurpose.setBackground(getResources().getDrawable(R.drawable.edittext_rounded_red));
                    tvPurpose.setTextColor(getResources().getColor(R.color.redDull));
                    alertDialog.setMessage("Kolom Purpose tidak boleh kosong!");
                    alertDialog.show();
                    return;
                }

                if (!disclaimerCB.isChecked()) {
                    disclaimerCB.startAnimation(AnimationUtils.loadAnimation(ReqSuratKetActivity.this, R.anim.shake));
                    alertDialog.setMessage("Mohon centang Disclaimer!");
                    alertDialog.show();
                    return;
                }

                if (commentET.getText().toString().isEmpty()) {
                    commentET.startAnimation(AnimationUtils.loadAnimation(ReqSuratKetActivity.this, R.anim.shake));
                    commentET.setBackground(getResources().getDrawable(R.drawable.edittext_rounded_red));
                    isComment = false;
                    alertDialog.setMessage("Kolom Comment tidak boleh kosong!");
                    alertDialog.show();
                    return;
                }

                if (!isComment || !isTypeOfLetter || !disclaimerCB.isChecked() || typeOfLetterPosition > 3 || !isFamily || !isPurpose) {
                    return;
                }

                String docName = getResources().getStringArray(R.array.leave_type_sket)[typeOfLetterPosition] + " " + new SimpleDateFormat("yyyyMM").format(Calendar.getInstance().getTime());
                String docType = getResources().getStringArray(R.array.leave_type_sket)[typeOfLetterPosition];
                String TemplateName = getResources().getStringArray(R.array.leave_type_sket_id)[typeOfLetterPosition];
                String pfgbdtm_text = null;
                String hiring_date_text = null;
                try {
                    pfgbdtm_text = new SimpleDateFormat("d MMMM yyyy").format(new SimpleDateFormat("yyyyMMdd").parse(selectedFamily.getPFGBDTM()));
                    hiring_date_text = new SimpleDateFormat("d MMMM yyyy").format(new SimpleDateFormat("yyyyMMdd").parse(String.valueOf(hiringDate)));
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (Exception e) {

                }

                String xmlData = "";
                String k2FieldInJson = "";
                String xmlDataSummay = SKetXML.getXMLDataSummary("SKET", docType, docType + " Request by " + PrefUtils.Build(ReqSuratKetActivity.this).getPref().getString(Constants.KEY_PCNAMEM, ""));
                if (typeOfLetterPosition == 0 || typeOfLetterPosition == 1) {
//                    [teks tipe dokumen yang dipilih] + [current YYYYMM] (tidak perlu Date) + nama keluarga [PFANAMM] dari API Get Family Member
                    docName += " " + selectedFamily.getPFANAMM();
                    if (typeOfLetterPosition == 0) {
                        xmlData = SKetXML.getXMLDataKemitraan(ReqSuratKetActivity.this, PDIRTXM_POSITION_ATTRIBUTE_OF_ROLE_OWNER, CompanyLegalName, docName,
                                docType, commentET.getText().toString(), etPurpose.getText().toString(), selectedFamily.getPFANAMM(), selectedFamily.getPFGBDTM(),
                                selectedFamily.getPFGBOTM(), pfgbdtm_text, AreaAddress, PDIRIDM, PDIRTXM, String.valueOf(hiringDate), hiring_date_text, PGBORTM_PERSONAL_DATA,
                                PGBDATM_PERSONAL_DATA_YYYYMMDD, PGBDATM_PERSONAL_DATA_DMMMMYYYY_TEXT, PCNAMEM, PPERNRM, PPLANSM_EMPLOYEE_HEADER, PPLSTXM, CompanyAddress, pfgbdtm_text, PPLSTXM, PDIRIDM_POSITION_ATTRIBUTE_OF_ROLE_OWNER, PDIRTXM_POSITION_ATTRIBUTE_OF_ROLE_OWNER);
                    } else {
                        xmlData = SKetXML.getXMLDataKesehatan(ReqSuratKetActivity.this, PDIRTXM_POSITION_ATTRIBUTE_OF_ROLE_OWNER, CompanyLegalName, docName,
                                docType, commentET.getText().toString(), etPurpose.getText().toString(), selectedFamily.getPFANAMM(), selectedFamily.getPFGBDTM(),
                                selectedFamily.getPFGBOTM(), pfgbdtm_text, AreaAddress, PDIRIDM, PDIRTXM, String.valueOf(hiringDate), hiring_date_text, PGBORTM_PERSONAL_DATA,
                                PGBDATM_PERSONAL_DATA_YYYYMMDD, PGBDATM_PERSONAL_DATA_DMMMMYYYY_TEXT, PCNAMEM, PPERNRM, PPLANSM_EMPLOYEE_HEADER, PPLSTXM, CompanyAddress, pfgbdtm_text, PPLSTXM, PDIRIDM_POSITION_ATTRIBUTE_OF_ROLE_OWNER, PDIRTXM_POSITION_ATTRIBUTE_OF_ROLE_OWNER);
                    }

                    k2FieldInJson = SKetXML.getK2FieldInJsonKesehatanDanKemitraan(ReqSuratKetActivity.this, docName, docType, TemplateName, commentET.getText().toString(), PDIRTXM, CompanyLegalName, CompanyAddress,
                            selectedFamily.getPFANAMM(), selectedFamily.getPFGBOTM(), selectedFamily.getPFGBDTM(), PCNAMEM_PERSONAL_DATA, AreaAddress, PPLSTXM, PCNAMEM, PPERNRM,
                            PDIRTXM, PDIRTXM);
                } else if (typeOfLetterPosition == 2) {
                    xmlData = SKetXML.getXMLDataTanpaUpahIndonesia(ReqSuratKetActivity.this, docName, docType, commentET.getText().toString(), etPurpose.getText().toString(), AreaAddress, PDIRTXM, CompanyLegalName,
                            PDIRIDM, PDIRTXM, String.valueOf(hiringDate), hiring_date_text, PGBORTM_PERSONAL_DATA, PGBDATM_PERSONAL_DATA, PGBDATM_PERSONAL_DATA_DMMMMYYYY_TEXT, PCNAMEM, PPERNRM, PPLANSM_EMPLOYEE_HEADER,
                            PPLSTXM, CompanyAddress, PPERNRM_CURRENT_ORGANIZAIONAL_ASSIGNMENT, PBEGDAM, PBUTXTM, PPLSTXM, PDIRIDM_POSITION_ATTRIBUTE_OF_ROLE_OWNER, PDIRTXM_POSITION_ATTRIBUTE_OF_ROLE_OWNER);

                    k2FieldInJson = SKetXML.getK2FieldInJsonTanpaUpahIndonesia(ReqSuratKetActivity.this, docName, docType, TemplateName, commentET.getText().toString(), PDIRTXM, CompanyLegalName, CompanyAddress,
                            AreaAddress, PGBORTM_PERSONAL_DATA, CompanyLegalName, PCNAMEM, PPERNRM, PGBDATM_PERSONAL_DATA, String.valueOf(hiringDate), PPLSTXM, PBEGDAM, PBUTXTM, PDIRTXM);
                } else if (typeOfLetterPosition == 3) {
                    xmlData = SKetXML.getXMLDataTanpaUpahIngris(ReqSuratKetActivity.this, docName, docType, commentET.getText().toString(), etPurpose.getText().toString(), AreaAddress, PDIRTXM, CompanyLegalName,
                            PDIRIDM, PDIRTXM, String.valueOf(hiringDate), hiring_date_text, PGBORTM_PERSONAL_DATA, PGBDATM_PERSONAL_DATA, PGBDATM_PERSONAL_DATA_DMMMMYYYY_TEXT, PCNAMEM, PPERNRM, PPLANSM_EMPLOYEE_HEADER,
                            PPLSTXM, CompanyAddress, PBEGDAM, PBUTXTM, PPLSTXM, PDIRIDM_POSITION_ATTRIBUTE_OF_ROLE_OWNER, PDIRTXM_POSITION_ATTRIBUTE_OF_ROLE_OWNER);

                    k2FieldInJson = SKetXML.getK2FieldInJsonTanpaUpahInggris(ReqSuratKetActivity.this, docName, docType, TemplateName, commentET.getText().toString(), PDIRTXM, CompanyLegalName, CompanyAddress, AreaAddress, PGBORTM_PERSONAL_DATA,
                            CompanyLegalName, PCNAMEM, PPERNRM, PGBDATM_PERSONAL_DATA, String.valueOf(hiringDate), PPLSTXM, PBEGDAM, PBUTXTM);
                }

                String comment = commentET.getText().toString();
                String requestorADUser = PrefUtils.Build(ReqSuratKetActivity.this).getPref().getString(Constants.KEY_USERNAME, "");
                String requestorPersonalNumber = PrefUtils.Build(ReqSuratKetActivity.this).getPref().getString(Constants.KEY_PERSONAL_NUM, "");
                String executorPersonalNumber = PrefUtils.Build(ReqSuratKetActivity.this).getPref().getString(Constants.KEY_PERSONAL_NUM, "");
                String url = "";
                String k2WorkFlowName = "HRISK2V3\\1010\\Personal Administration\\SuratKeteranganPekerja";

                System.out.println("k2fieldinjson "+k2FieldInJson);
                System.out.println("xmldata "+xmlData);
                System.out.println("xmldatasummary "+xmlDataSummay);
                submitSKet(k2WorkFlowName, url, executorPersonalNumber, requestorPersonalNumber, requestorADUser, xmlData, xmlDataSummay, comment, k2FieldInJson);
            }
        });

        getFamily();
        getPersonalData();

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogTypeOfLatterContainerRL.isShown()) {
                    dialogTypeOfLatterContainerRL.setVisibility(View.GONE);
                    return;
                }

                if (dialogFamilyContainerRL.isShown()) {
                    dialogFamilyContainerRL.setVisibility(View.GONE);
                }
                finish();
            }
        });

        getCurrentOrganizationalAssignment();
        getOrganizationalAssignmentHistory();
        getCompanyCodes("COMPANY_CODES", "PKTOPLM = 'PTMN'");
        getEmployeeHiringDate();
        getUserAreaByADUsername();
        getCompanies();
        getRoleOwnerForUser(PrefUtils.Build(this).getPref().getString(Constants.KEY_PBUKRSM, ""));
        getEmployeeHeaderIdentityOfRoleOwner();

        KeyboardUtils.setupUI(this, findViewById(R.id.reqSuratKetCL));

        etFamilyMember.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    etFamilyMember.setBackground(getResources().getDrawable(R.drawable.edittext_rounded_gray));
                    tvFamilyMember.setTextColor(getResources().getColor(R.color.greyAction));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etTypeOfLatter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    etTypeOfLatter.setBackground(getResources().getDrawable(R.drawable.edittext_rounded_gray));
                    tvTypeOfLetter.setTextColor(getResources().getColor(R.color.greyAction));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etPurpose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    etPurpose.setBackground(getResources().getDrawable(R.drawable.edittext_rounded_gray));
                    tvPurpose.setTextColor(getResources().getColor(R.color.greyAction));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        commentET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    commentET.setBackground(getResources().getDrawable(R.drawable.edittext_rounded_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        if (dialogTypeOfLatterContainerRL.isShown()) {
            dialogTypeOfLatterContainerRL.setVisibility(View.GONE);
            return;
        }

        if (dialogFamilyContainerRL.isShown()) {
            dialogFamilyContainerRL.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
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

        this.alertDialogDismiss = new AlertDialog.Builder(this)
                .setNeutralButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                })
                .create();
    }

    private void submitSKet(String k2WorkFlowName, String url, String executorPersonalNumber, String requestorPersonalNumber, String requestorADUser, String xmlData, String xmlDataSummay, String comment, String k2FieldInJson) {
        loading.show();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        restApi.submitRequestSKETNonVisa("K2Services", "SubmitRequest", k2WorkFlowName, url, executorPersonalNumber, requestorPersonalNumber, requestorADUser, xmlData, xmlDataSummay, comment, k2FieldInJson)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d("Success submit sket", "getAllWorklist, true _ " + response.raw().toString());
                                parseXml(strResponse, successListenerPost);
//                                parseXml(strResponse);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            handleError(response);
                            Log.d("Error submit sket", "getAllWorklist, false _ " + response.raw().toString());
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

    private void getFamily() {
        loading.show();
        Calendar calendar = Calendar.getInstance();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        restApi.getFamily("PersonalAdministrationServices", "GetInfoTypeRawData", PrefUtils.Build(this)
                .getPref().getString(Constants.KEY_PERSONAL_NUM, ""), "FAMILY_MEMBER_DEPENDENTS", new SimpleDateFormat("yyyyMMdd").format(calendar.getTime()), new SimpleDateFormat("yyyyMMdd").format(calendar.getTime()))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d("Success get history", "getAllWorklist, true _ " + response.raw().toString());
                                parseXml(strResponse, successListener);
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

    private void getPersonalData() {
        loading.show();
        Calendar calendar = Calendar.getInstance();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        restApi.getPersonalDataSKet("PersonalAdministrationServices", "GetInfoTypeRawData", PrefUtils.Build(this)
                .getPref().getString(Constants.KEY_PERSONAL_NUM, ""), new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()), new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d("Success personal data", "getAllWorklist, true _ " + response.raw().toString());
                                parseXml(strResponse, successListenerPersonalData);
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
    OnSuccessListener successListener = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            parseJson(strJson);
        }
    };

    OnSuccessListener successListenerPost = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            parseJsonPost(strJson);
        }
    };

    OnSuccessListener successListenerPersonalData = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            parseJsonPersonalData(strJson);
        }
    };

    OnSuccessListener successListenerPositionAttributeOfRoleOwner = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            parseJsonPositionAttributeOfRoleOwner(strJson);
        }
    };
    OnSuccessListener successListenerEmployeeHeaderIdentityOfRoleOwner = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            parseJsonEmployeeHeaderIdentityOfRoleOwner(strJson);
        }
    };
    OnSuccessListener successListenerRoleOwnerForUser = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            parseJsonRoleOwnerForUser(strJson);
        }
    };
    OnSuccessListener successListenerCompanies = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            parseJsonCompanies(strJson);
        }
    };
    OnSuccessListener successListenerRoleArea = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            parseJsonRoleArea(strJson);
        }
    };
    OnSuccessListener successListenerUserAreaByADUsername = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            parseJsonUserAreaByADUsername(strJson);
        }
    };
    OnSuccessListener successListenerEmployeeHiringDate = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            parseJsonEmployeeHiringDate(strJson);
        }
    };
    OnSuccessListener successListenerPositionAttribute = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            parseJsonPositionAttribute(strJson);
        }
    };
    OnSuccessListener successListenerCompanyCodes = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            parseJsonCompanyCodes(strJson);
        }
    };
    OnSuccessListener successListenerOrganizationalAssignmentHistory = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            parseJsonOrganizationalAssignmentHistory(strJson);
        }
    };
    OnSuccessListener successListenerGetCurrentOrganizationalAssignment = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            parseJsonCurrentOrganizationalAsignment(strJson);
        }
    };

    private void parseJsonPersonalData(String strJson) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);

        JsonArray jsonArray = jsonObject.getAsJsonArray("Table0");

        if (jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jo = jsonArray.get(i).getAsJsonObject();
                PGBDATM_PERSONAL_DATA = jo.get("PGBDATM").getAsString();
                PCNAMEM_PERSONAL_DATA = jo.get("PCNAMEM").getAsString();
                PGBORTM_PERSONAL_DATA = jo.get("PGBORTM").getAsString();
                PGBDATM_PERSONAL_DATA_YYYYMMDD = jo.get("PGBDATM").getAsString();
                try {
                    PGBDATM_PERSONAL_DATA_DMMMMYYYY_TEXT = new SimpleDateFormat("d MMMM yyyy").format(new SimpleDateFormat("yyyyMMdd").parse(PGBDATM_PERSONAL_DATA_YYYYMMDD));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            Log.d("datanyaadafamily", "ada");
        } else {
//            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    private void parseJson(String strJson) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);

        JsonArray jsonArray = jsonObject.getAsJsonArray("Table0");

        families = new ArrayList<>();

        if (jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                FamilyModel familyModel = new FamilyModel();
                JsonObject jo = jsonArray.get(i).getAsJsonObject();
                familyModel.setName(jo.get("PFANAMM").getAsString());
                familyModel.setType(jo.get("PFAMSAM").getAsString());
                familyModel.setPKDZUGM(jo.get("PKDZUGM").getAsString());
                familyModel.setPFGBDTM(jo.get("PFGBDTM").getAsString());
                familyModel.setPFANAMM(jo.get("PFANAMM").getAsString());
                familyModel.setPFGBOTM(jo.get("PFGBOTM").getAsString());

                families.add(familyModel);
//                PFAMSAM 1 atau 14 atau PKDZUGM y kesehatan
//                PFAMSAM 2, 6 atau 8 atau kemitraan
            }

            familyRVAdapter = new FamilyRVAdapter(this, families, this);
            dialogFamilyRV.setLayoutManager(new LinearLayoutManager(this));
            dialogFamilyRV.setAdapter(familyRVAdapter);

            Log.d("datanyaadafamily", "ada");
        } else {
//            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    private List<FamilyModel> getFamiliyFilter() {
        try {
            List<FamilyModel> result = new ArrayList<>();

            for (int i = 0; i < families.size(); i++) {
                if (typeOfLetterPosition == 1 && (families.get(i).getType().equals("1") || families.get(i).getType().equals("14") || families.get(i).getPKDZUGM().equalsIgnoreCase("Y"))) {
                    result.add(families.get(i));
                } else if (typeOfLetterPosition == 0 && (families.get(i).getType().equals("2") || families.get(i).getType().equals("6") || families.get(i).getType().equals("8"))) {
                    result.add(families.get(i));
                }
            }
            return result;
        } catch (NullPointerException e) {
            return new ArrayList<>();
        }
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
                for (int i = 0; i < nodeListError.getLength(); i++) {
                    String strError = nodeListError.item(i).getTextContent();
                    String message = "Could not get data due to:" + strError;

                    if (strError.contains("401")) {
                        message = "Could not get data. It might be you are Loged in from other device or your session was exiperd.";
                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Login again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    startActivity(new Intent(ReqSuratKetActivity.this,
                                            Class.forName("com.pertamina.portal.activity.LoginActivity")));
                                    finish();
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    alertDialog.setMessage(message);

                    if (!alertDialog.isShowing()) {
                        alertDialog.show();
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onTypeOfLetterClicked(int position) {
        if (position == 4 || position == 5) {
            alertDialog.setMessage("Untuk surat jenis ini silahkan request melalui web i-am");
            alertDialog.show();
            return;
        }
        typeOfLetterPosition = position;
        dialogTypeOfLatterContainerRL.setVisibility(View.GONE);

        if (position == 0 || position == 1) {
//            FamilyAdapter familyAdapter = new FamilyAdapter(ReqSuratKetActivity.this, android.R.layout.simple_spinner_item, getFamiliyFilter());
//            spFamilyMember.setAdapter(familyAdapter);
            selectedFamily = null;
            familyRVAdapter = new FamilyRVAdapter(this, getFamiliyFilter(), this);
            dialogFamilyRV.setAdapter(familyRVAdapter);
            tvFamilyMember.setVisibility(View.VISIBLE);
            etFamilyMember.setText("");
            etFamilyMember.setVisibility(View.VISIBLE);
        } else {
            tvFamilyMember.setVisibility(View.GONE);
            etFamilyMember.setVisibility(View.GONE);
        }
        etTypeOfLatter.setText(getResources().getStringArray(R.array.leave_type_sket)[position]);
    }

    @Override
    public void onFamilyClicked(FamilyModel familyModel) {
        etFamilyMember.setText(familyModel.getName());
        selectedFamily = familyModel;
        dialogFamilyContainerRL.setVisibility(View.GONE);
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
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Login again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        startActivity(new Intent(ReqSuratKetActivity.this,
                                Class.forName("com.pertamina.portal.activity.LoginActivity")));
                        finish();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        if (alertDialog == null) {
            this.alertDialog = new AlertDialog.Builder(ReqSuratKetActivity.this)
                    .setNeutralButton("close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    })
                    .create();
        }

        alertDialog.setMessage(message);

        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    private void getCurrentOrganizationalAssignment() {
        loading.show();
        Calendar calendar = Calendar.getInstance();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        restApi.getCurrentOrganizationalAssignment("PersonalAdministrationServices", "GetInfoTypeRawData", PrefUtils.Build(this)
                .getPref().getString(Constants.KEY_PERSONAL_NUM, ""), new SimpleDateFormat("yyyyMMdd").format(calendar.getTime()), new SimpleDateFormat("yyyyMMdd").format(calendar.getTime()))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                System.out.println("getCurrentOrganizationalAssignment success");
                                parseXml(strResponse, successListenerGetCurrentOrganizationalAssignment);
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

    private void parseJsonCurrentOrganizationalAsignment(String strJson) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);

        JsonArray jsonArray = jsonObject.getAsJsonArray("Table0");

        if (jsonArray.size() > 0) {
            JsonObject jo = jsonArray.get(0).getAsJsonObject();
            PBEGDAM = jo.get("PBEGDAM").getAsString();
            PPLANSM = jo.get("PPLANSM").getAsString();
            PPERNRM_CURRENT_ORGANIZAIONAL_ASSIGNMENT = jo.get("PPERNRM").getAsString();

            getPositionAttribute();

            Log.d("datanyaada", "ada");
        } else {
//            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    private void getOrganizationalAssignmentHistory() {
        loading.show();
        Calendar calendar = Calendar.getInstance();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        restApi.getOrganizationalAssignmentHistory("PersonalAdministrationServices", "GetInfoTypeRawData", PrefUtils.Build(this)
                .getPref().getString(Constants.KEY_PERSONAL_NUM, ""))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                System.out.println("getOrganizationalAssignmentHistory success");
                                parseXml(strResponse, successListenerOrganizationalAssignmentHistory);
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

    private void parseJsonOrganizationalAssignmentHistory(String strJson) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);

        JsonArray jsonArray = jsonObject.getAsJsonArray("Table0");

        int min = 99999999;

        if (jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jo = jsonArray.get(i).getAsJsonObject();
                if (min > jo.get("PBEGDAM").getAsInt()) {
                    min = jo.get("PBEGDAM").getAsInt();
                    PBUKRSM = jo.get("PBUKRSM").getAsString();
                }
            }

            Log.d("datanyaada", "ada");
        } else {
//            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCompanyCodes(String referenceName, String conditions) {
        loading.show();
        Calendar calendar = Calendar.getInstance();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        restApi.getCompanyCodes("PersonalAdministrationServices", "GetReferenceData", "100", referenceName, "", conditions)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                System.out.println("getCompanyCodes success");
                                parseXml(strResponse, successListenerCompanyCodes);
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

    private void parseJsonCompanyCodes(String strJson) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);

        JsonArray jsonArray = jsonObject.getAsJsonArray("Table0");

        if (jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jo = jsonArray.get(i).getAsJsonObject();
                if (jo.get("PBUKRSM").getAsString().equals(PBUKRSM)) {
                    PBUTXTM = jo.get("PBUTXTM").getAsString();
                    i = jsonArray.size();
                }
            }

            Log.d("datanyaada", "ada");
        } else {
//            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    private void getPositionAttribute() {
        loading.show();
        Calendar calendar = Calendar.getInstance();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        restApi.getPositionAttribute("OrganizationalManagementServices", "GetPositionAttribute", PPLANSM)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                System.out.println("getPositionAttribute success");
                                parseXml(strResponse, successListenerPositionAttribute);
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

    private void parseJsonPositionAttribute(String strJson) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);

        JsonArray jsonArray = jsonObject.getAsJsonArray("Table0");

        if (jsonArray.size() > 0) {

            JsonObject jo = jsonArray.get(0).getAsJsonObject();
            PDIRTXM = jo.get("PDIRTXM").getAsString();
            PDIRIDM = jo.get("PDIRIDM").getAsString();

            Log.d("datanyaada", "ada");
        } else {
//            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    private void getEmployeeHiringDate() {
        loading.show();
        Calendar calendar = Calendar.getInstance();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        restApi.getEmployeeHiringDate("PersonalAdministrationServices", "GetEmployeeHireDate", PrefUtils.Build(this)
                .getPref().getString(Constants.KEY_PERSONAL_NUM, ""))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                System.out.println("getEmployeeHiringDate success");
                                parseXml(strResponse, successListenerEmployeeHiringDate);
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

    private void parseJsonEmployeeHiringDate(String strJson) {

        hiringDate = Integer.parseInt(strJson);

    }

    private void parseJsonPost(String strJson) {
        alertDialogDismiss.setMessage(strJson);
        alertDialogDismiss.show();
        alertDialogDismiss.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                finish();
            }
        });
    }

    private void getUserAreaByADUsername() {
        loading.show();
        Calendar calendar = Calendar.getInstance();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        restApi.getUserAreaByADUsername("AuthorizationServices", "GetUserAreasByADuser", PrefUtils.Build(this)
                .getPref().getString(Constants.KEY_USERNAME, ""))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                System.out.println("getUserAreaByADUsername success");
                                parseXml(strResponse, successListenerUserAreaByADUsername);
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

    private void parseJsonUserAreaByADUsername(String strJson) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);

        JsonArray jsonArray = jsonObject.getAsJsonArray("Table0");

        List<String> userRoleAreaNames = new ArrayList<>();

        if (jsonArray.size() > 0) {
            for(int i = 0; i < jsonArray.size(); i++) {
                userRoleAreaNames.add(jsonArray.get(i).getAsJsonObject().get("RoleAreaName").getAsString());
            }

            for (int i = 0; i < roleAreaNames.size(); i++) {
                if (userRoleAreaNames.contains(roleAreaNames.get(i))) {
                    RoleAreaName = roleAreaNames.get(i);
                    i = roleAreaNames.size();
                }
            }

            getRoleArea(RoleAreaName);

            Log.d("datanyaada", "ada");
        } else {
//            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    private void getRoleArea(String roleAreaName) {
        loading.show();
        Calendar calendar = Calendar.getInstance();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        restApi.getRoleArea("AuthorizationServices", "GetRoleAreas", roleAreaName)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                System.out.println("getRoleArea success");
                                parseXml(strResponse, successListenerRoleArea);
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

    private void parseJsonRoleArea(String strJson) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);

        JsonArray jsonArray = jsonObject.getAsJsonArray("Table0");

        if (jsonArray.size() > 0) {
            AreaAddress = jsonArray.get(0).getAsJsonObject().get("AreaAddress").getAsString();

            Log.d("datanyaada", "ada");
        } else {
//            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        }
    }


    private void getCompanies() {
        loading.show();
        Calendar calendar = Calendar.getInstance();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        restApi.getCompanies("AuthenticationServices", "GetCompanies")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                System.out.println("getCompanies success");
                                parseXml(strResponse, successListenerCompanies);
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

    private void parseJsonCompanies(String strJson) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);

        JsonArray jsonArray = jsonObject.getAsJsonArray("Table0");

        if (jsonArray.size() > 0) {
            for(int i = 0; i < jsonArray.size(); i++) {
                if (PrefUtils.Build(this).getPref().getString(Constants.KEY_PBUKRSM, "").equals(jsonArray.get(i).getAsJsonObject().get("CompanyCode").getAsString())) {
                    CompanyAddress = jsonArray.get(i).getAsJsonObject().get("CompanyAddress").getAsString();
                    CompanyLegalName = jsonArray.get(i).getAsJsonObject().get("CompanyLegalName").getAsString();
                    i = jsonArray.size();
                }
            }

            Log.d("datanyaada", "ada");
        } else {
//            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    private void getRoleOwnerForUser(String roleName) {
        loading.show();
        roleName += "_SKETNONVISA_SIGNER";
        Calendar calendar = Calendar.getInstance();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        restApi.getRoleOwnerForUser("AuthorizationServices", "GetRoleOwnerForUser", roleName, PrefUtils.Build(this).getPref().getString(Constants.KEY_PERSONAL_NUM, ""))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                System.out.println("getRoleOwnerForUser success");
                                parseXml(strResponse, successListenerRoleOwnerForUser);
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

    private void parseJsonRoleOwnerForUser(String strJson) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);

        JsonArray jsonArray = jsonObject.getAsJsonArray("Table0");

        if (jsonArray.size() > 0) {
            PERNR = jsonArray.get(0).getAsJsonObject().get("PERNR").getAsString();
            ADUsername = jsonArray.get(0).getAsJsonObject().get("ADUsername").getAsString();
            EmailUserID = jsonArray.get(0).getAsJsonObject().get("EmailUserID").getAsString();

            Log.d("datanyaada", "ada");
        } else {
//            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    private void getEmployeeHeaderIdentityOfRoleOwner() {
        loading.show();
        Calendar calendar = Calendar.getInstance();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        restApi.getEmployeeHeaderIdentityOfRoleOwner("PersonalAdministrationServices", "GetEmployeeHeaderIdentity", PrefUtils.Build(this)
                .getPref().getString(Constants.KEY_PERSONAL_NUM, ""), new SimpleDateFormat("yyyyMMdd").format(calendar.getTime()), new SimpleDateFormat("yyyyMMdd").format(calendar.getTime()))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                System.out.println("getEmployeeHeaderIdentityOfRoleOwner success");
                                parseXml(strResponse, successListenerEmployeeHeaderIdentityOfRoleOwner);
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

    private void parseJsonEmployeeHeaderIdentityOfRoleOwner(String strJson) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);

        JsonArray jsonArray = jsonObject.getAsJsonArray("Table0");

        if (jsonArray.size() > 0) {
            PCNAMEM = jsonArray.get(0).getAsJsonObject().get("PCNAMEM").getAsString();
            PPERNRM = jsonArray.get(0).getAsJsonObject().get("PPERNRM").getAsString();
//            PDIRTXM_EMPLOYEE_HEADER = jsonArray.get(0).getAsJsonObject().get("PDIRTXM").getAsString();
            PPLANSM_EMPLOYEE_HEADER = jsonArray.get(0).getAsJsonObject().get("PPLANSM").getAsString();

            getPositionAttributeOfRoleOwner(PPLANSM_EMPLOYEE_HEADER);
            Log.d("datanyaada", "ada");
        } else {
//            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    private void getPositionAttributeOfRoleOwner(String positionID) {
        loading.show();
        Calendar calendar = Calendar.getInstance();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        restApi.getPositionAttributeOfRoleOwner("OrganizationalManagementServices", "GetPositionAttribute", positionID)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                System.out.println("getPositionAttributeOfRoleOwner success");
                                parseXml(strResponse, successListenerPositionAttributeOfRoleOwner);
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

    private void parseJsonPositionAttributeOfRoleOwner(String strJson) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);

        JsonArray jsonArray = jsonObject.getAsJsonArray("Table0");

        if (jsonArray.size() > 0) {
            PDIRIDM_POSITION_ATTRIBUTE_OF_ROLE_OWNER = jsonArray.get(0).getAsJsonObject().get("PDIRIDM").getAsString();
            PDIRTXM_POSITION_ATTRIBUTE_OF_ROLE_OWNER = jsonArray.get(0).getAsJsonObject().get("PDIRTXM").getAsString();
            PPLSTXM = jsonArray.get(0).getAsJsonObject().get("PPLSTXM").getAsString();

            Log.d("datanyaada", "ada");
        } else {
//            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        }
    }
}
