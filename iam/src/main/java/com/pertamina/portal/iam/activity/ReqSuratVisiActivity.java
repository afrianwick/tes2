package com.pertamina.portal.iam.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pertamina.portal.core.activity.BackableNoActionbarActivity;
import com.pertamina.portal.core.interfaces.OnSuccessListener;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.PrefUtils;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.core.utils.StringUtils;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.adapters.AdditionalFieldAdapter;
import com.pertamina.portal.iam.adapters.AdditionalFieldVisaAdapter;
import com.pertamina.portal.iam.adapters.EmbassyAdapter;
import com.pertamina.portal.iam.adapters.PurposeRVAdapter;
import com.pertamina.portal.iam.interfaces.ReqSuratVisiView;
import com.pertamina.portal.iam.models.AdditionalFieldVisaModel;
import com.pertamina.portal.iam.models.EmbassyModel;
import com.pertamina.portal.iam.models.PurposeModel;
import com.pertamina.portal.iam.utils.BackgroundService.SketVisaBackground;
import com.pertamina.portal.iam.utils.DateUtils.DateUtils;
import com.pertamina.portal.iam.utils.FileUtils.FileUtils;
import com.pertamina.portal.iam.utils.SKetXML;
import com.pertamina.portal.iam.utils.Utils;
import com.pertamina.portal.iam.view.IamButton;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReqSuratVisiActivity extends BackableNoActionbarActivity implements ReqSuratVisiView {

    private TextInputEditText etPurpose;
    private Spinner spAdditionalField;
    private List<PurposeModel> purposeModels;
    private AlertDialog alertDialog;
    private AlertDialog loading;
    private List<EmbassyModel> embassyModels;
    private TextInputEditText embassyET, destinationET, startDateTIET, endDateTIET, passportTIET, descriptionTIET;
    private TextInputLayout embassyTIL, destinationTIL, cityTIL, startDateTIL, endDateTIL, passportTIL, descriptionTIL, purposeTIL;
    private View embassyDialog;
    private ConstraintLayout embassyDialogContainer;
    private RecyclerView embassyRV;
    private RelativeLayout documentButtonContainerRL;
    private EditText embassyRVET, commentET;
    private EmbassyAdapter embassyAdapter;
    private String choosenDate;
    private DatePickerDialog datePicker;
    private CheckBox disclaimerCB;
    private IamButton submit, documentButton;
    private RecyclerView additionalFieldRV;
    private AdditionalFieldVisaAdapter additionalFieldVisaAdapter;
    private List<AdditionalFieldVisaModel> additionalFieldVisaModels;
    private List<String> additionalFieldVisaType;
    private AdditionalFieldAdapter additionalFieldAdapter;
    private int additionalFieldType = 0, DOCUMENT_REQUEST = 1010;
    private Uri fileUri;
    private PurposeRVAdapter purposeAdapter;
    private EditText etDestination, etCity, etConsulate;
    private TextView fileSizeTV, fileNameTV, fileInfoTV;
    private LinearLayout fileInfoContainerLL;

    private View dialogPurposeView;
    private RecyclerView dialogPurposeRV;
    private RelativeLayout dialogPurposeContainerRL;
    private ConstraintLayout dialogPurposeContainerCL;
    private TextView dialogPurposeTV, dialogEmbassyTV;
    private EditText dialogPurposeET;
    private String filePath;
    private int purposeID = 0;
    private String PDIRTXM_POSITION_ATTRIBUTE_OF_ROLE_OWNER, PDIRIDM_POSITION_ATTRIBUTE_OF_ROLE_OWNER, PPLSTXM, ADDRESS, AREA, OFFICIAL_NAME, COUNTRY;
    private String CompanyLegalName, CompanyAddress, PCNAMEM_EMPLOYEE_HEADER, PPERNRM_EMPLOYEE_HEADER, PPLANSM_EMPLOYEE_HEADER, EMBASSY_ID;
    private AlertDialog alertDialogDismiss;
    private String[] additionalName = new String[5], additionalRelationship = new String[5], additionalPassport = new String[5];
    private byte[] bytes;
    private String sket;
    private long fileSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_req_surat_visa);
        super.onCreateBackable(this, R.id.ivBack);

        loading = new SpotsDialog.Builder().setContext(this).build();
        buildAlert();

        Arrays.fill(additionalName, "");
        Arrays.fill(additionalPassport, "");
        Arrays.fill(additionalRelationship, "");

        dialogPurposeContainerRL = findViewById(R.id.reqSuratKetVisaPurposeContainerRL);
        dialogPurposeView = findViewById(R.id.reqSuratKetVisaPurposeView);
        etDestination = findViewById(R.id.etDestination);
        etCity = findViewById(R.id.etCity);
        etConsulate = findViewById(R.id.etConsulate);
        dialogPurposeRV = dialogPurposeContainerRL.findViewById(R.id.dialogCountryRV);
        dialogPurposeContainerCL = dialogPurposeContainerRL.findViewById(R.id.dialogCountryContainerCL);
        dialogPurposeET = dialogPurposeContainerRL.findViewById(R.id.dialogCountrySearcET);
        dialogPurposeTV = dialogPurposeContainerRL.findViewById(R.id.dialogCountryTitleTV);
        dialogPurposeET.setVisibility(View.GONE);

        fileSizeTV = findViewById(R.id.fileSizeTV);
        fileInfoTV = findViewById(R.id.fileInfoTV);
        fileNameTV = findViewById(R.id.fileNameTV);
        fileInfoContainerLL = findViewById(R.id.fileInfoContainerLL);

        dialogPurposeTV.setText("Purpose");

        dialogPurposeContainerCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPurposeContainerRL.setVisibility(View.GONE);
            }
        });

        etPurpose = findViewById(R.id.etPurpose);
        commentET = findViewById(R.id.commentET);
        destinationET = findViewById(R.id.etDestination);
        embassyET = findViewById(R.id.etConsulate);
        embassyTIL = findViewById(R.id.reqSuratKetVisaEmbassyTIL);
        descriptionTIL = findViewById(R.id.reqSuratKetVisaDescriptionTIL);
        destinationTIL = findViewById(R.id.reqSuratKetVisaDestinationTIL);
        cityTIL = findViewById(R.id.reqSuratKetVisaCityTIL);
        startDateTIL = findViewById(R.id.reqSuratKetVisaStartDateTIL);
        endDateTIL = findViewById(R.id.reqSuratKetVisaEndDateTIL);
        passportTIL = findViewById(R.id.reqSuratKetVisaPassportTIL);
        purposeTIL = findViewById(R.id.reqSuratKetVisaPurposeTIL);
        embassyDialog = findViewById(R.id.reqSuratvisaEmbassyDialog);
        embassyRV = embassyDialog.findViewById(R.id.dialogCountryRV);
        embassyRVET = embassyDialog.findViewById(R.id.dialogCountrySearcET);
        embassyDialogContainer = embassyDialog.findViewById(R.id.dialogCountryContainerCL);
        dialogEmbassyTV = embassyDialog.findViewById(R.id.dialogCountryTitleTV);
        startDateTIET = findViewById(R.id.etDateFrom);
        endDateTIET = findViewById(R.id.etDateTo);
        spAdditionalField = findViewById(R.id.spAdditionalField);
        passportTIET = findViewById(R.id.etPassport);
        descriptionTIET = findViewById(R.id.etDesc);
        disclaimerCB = findViewById(R.id.reqSuratKetVisaDisclaimerCB);
        additionalFieldRV = findViewById(R.id.reqSuratKetVisaAdditionalRV);
        submit = findViewById(R.id.reqSuratKetVisaSubmitButton);
        documentButton = findViewById(R.id.reqSuratKetVisaDocumentButton);
        documentButtonContainerRL = findViewById(R.id.reqSuratKetVisaDocumentButtonContainerRL);

        dialogEmbassyTV.setText("Embassy/Consulate");

        additionalFieldVisaModels = new ArrayList<>();
        AdditionalFieldVisaModel additionalFieldVisaModel = new AdditionalFieldVisaModel();
        additionalFieldVisaModel.setName("");
        additionalFieldVisaModel.setType("Spouse");
        additionalFieldVisaModels.add(additionalFieldVisaModel);
        additionalFieldVisaAdapter = new AdditionalFieldVisaAdapter(this, additionalFieldVisaModels, this);
        additionalFieldRV.setLayoutManager(new LinearLayoutManager(this));
        additionalFieldRV.setAdapter(additionalFieldVisaAdapter);

        disclaimerCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                submit.setBtnDrawable(getResources().getDrawable(b ? R.drawable.button_bg_rounded_green : R.drawable.button_bg_rounded_gray));
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isPurpose = true, isDestination = true, isCity = true, isEmbassy = true, isStartDate = true, isEndDate = true, isPassportID = true, isDescription = true, isDocument = true, isComment = true, isFamily = true;
                if (etPurpose.getText().toString().equals("Pilih")) {
                    isPurpose = false;
                    purposeTIL.startAnimation(AnimationUtils.loadAnimation(ReqSuratVisiActivity.this, R.anim.shake));
                }

                if (destinationET.getText().toString().equals("")) {
                    isDestination = false;
                    destinationTIL.startAnimation(AnimationUtils.loadAnimation(ReqSuratVisiActivity.this, R.anim.shake));
                }

                if (etCity.getText().toString().equals("")) {
                    isCity = false;
                    cityTIL.startAnimation(AnimationUtils.loadAnimation(ReqSuratVisiActivity.this, R.anim.shake));
                }

                if (embassyET.getText().toString().equals("")) {
                    isEmbassy = false;
                    embassyTIL.startAnimation(AnimationUtils.loadAnimation(ReqSuratVisiActivity.this, R.anim.shake));
                }

                if (startDateTIET.getText().toString().equals("")) {
                    isStartDate = false;
                    startDateTIL.startAnimation(AnimationUtils.loadAnimation(ReqSuratVisiActivity.this, R.anim.shake));
                }

                if (endDateTIET.getText().toString().equals("")) {
                    isEndDate = false;
                    endDateTIL.startAnimation(AnimationUtils.loadAnimation(ReqSuratVisiActivity.this, R.anim.shake));
                }

                if (passportTIET.getText().toString().equals("")) {
                    isPassportID = false;
                    passportTIL.startAnimation(AnimationUtils.loadAnimation(ReqSuratVisiActivity.this, R.anim.shake));
                }

                if (descriptionTIET.getText().toString().equals("")) {
                    isDescription = false;
                    descriptionTIL.startAnimation(AnimationUtils.loadAnimation(ReqSuratVisiActivity.this, R.anim.shake));
                }

                if (fileUri == null) {
                    isDocument = false;
                    documentButtonContainerRL.startAnimation(AnimationUtils.loadAnimation(ReqSuratVisiActivity.this, R.anim.shake));
                }

                if (passportTIET.getText().toString().isEmpty()) {
                    isPassportID = false;
                    passportTIL.startAnimation(AnimationUtils.loadAnimation(ReqSuratVisiActivity.this, R.anim.shake));
                }

                if (!disclaimerCB.isChecked()) {
                    disclaimerCB.startAnimation(AnimationUtils.loadAnimation(ReqSuratVisiActivity.this, R.anim.shake));
                }

                if (commentET.getText().toString().equals("")) {
                    isComment = false;
                    commentET.startAnimation(AnimationUtils.loadAnimation(ReqSuratVisiActivity.this, R.anim.shake));
                }

                if (purposeID == 3) {
                    isFamily = additionalDataSetup();
                    additionalFieldRV.startAnimation(AnimationUtils.loadAnimation(ReqSuratVisiActivity.this, R.anim.shake));
                }

                if (!isPurpose || !isComment || !isDescription || !isCity || !isDestination || !isEmbassy || !isStartDate || !isEndDate || !isPassportID || !isDocument || !isFamily || !disclaimerCB.isChecked()) {
                    alertDialog.setMessage("Lengkapi Data dan ceklis desclaimer!");
                    alertDialog.show();
                    return;
                }

                if (fileSize > 3) {
                    fileInfoContainerLL.startAnimation(AnimationUtils.loadAnimation(ReqSuratVisiActivity.this, R.anim.shake));
                    alertDialog.setMessage("Ukuran file melebihi maksimal kapasitas!");
                    alertDialog.show();
                    return;
                }

//                [PPERNRM] token login + [teks Purpose yang dipilih] + [current YYYYMM] (tidak perlu Date) + [Country]

                String docName = PrefUtils.Build(ReqSuratVisiActivity.this).getPref().getString(Constants.KEY_PPERNRM, "") + " " +
                        getResources().getStringArray(R.array.purpose_text)[purposeID] + " " +
                        new SimpleDateFormat("yyyyMM").format(Calendar.getInstance().getTime()) + " " +
                        COUNTRY;

                String docType = getResources().getStringArray(R.array.purpose_text)[purposeID];

                String xmlData = "";
                String k2FieldInJson = "";
                String xmlDataSummay = SKetXML.getXMLDataSummary("SKET", docType, docType+ " Request by " + PrefUtils.Build(ReqSuratVisiActivity.this).getPref().getString(Constants.KEY_PCNAMEM, ""));

                DateUtils dateUtils = new DateUtils();
                String startDate = dateUtils.setInputDate(startDateTIET.getText().toString()).setInputPattern("d MMM yyyy").setOutputPattern("yyyyMMdd").build();
                String endDate = dateUtils.setInputDate(endDateTIET.getText().toString()).setInputPattern("d MMM yyyy").setOutputPattern("yyyyMMdd").build();
                String startDateText = dateUtils.setInputDate(startDateTIET.getText().toString()).setInputPattern("d MMM yyyy").setOutputPattern("d MMMM yyyy").build();
                String endDateText = dateUtils.setInputDate(endDateTIET.getText().toString()).setInputPattern("d MMM yyyy").setOutputPattern("d MMMM yyyy").build();
                if (purposeID == 1) {
                    xmlData = SKetXML.getXMLVisaDinas(ReqSuratVisiActivity.this, PDIRTXM_POSITION_ATTRIBUTE_OF_ROLE_OWNER, CompanyLegalName, docName, docType, commentET.getText().toString(), PPERNRM_EMPLOYEE_HEADER,
                            COUNTRY, etCity.getText().toString(), descriptionTIET.getText().toString(), EMBASSY_ID, OFFICIAL_NAME, ADDRESS, AREA, startDate, startDateText, endDate, endDateText, passportTIET.getText().toString(),
                            FileUtils.getFileName(ReqSuratVisiActivity.this, fileUri));

                    k2FieldInJson = SKetXML.getK2FieldInJsonVisaDinas(ReqSuratVisiActivity.this, docName, docType, commentET.getText().toString(), PDIRTXM_POSITION_ATTRIBUTE_OF_ROLE_OWNER, CompanyLegalName, OFFICIAL_NAME,
                            ADDRESS, AREA, passportTIET.getText().toString(), etCity.getText().toString(), COUNTRY, startDate, endDate);
                } else if (purposeID == 2) {
                    xmlData = SKetXML.getXMLVisaPribadi(ReqSuratVisiActivity.this, PDIRTXM_POSITION_ATTRIBUTE_OF_ROLE_OWNER, CompanyLegalName, docName, docType, commentET.getText().toString(), PPERNRM_EMPLOYEE_HEADER,
                            COUNTRY, etCity.getText().toString(), descriptionTIET.getText().toString(), EMBASSY_ID, OFFICIAL_NAME, ADDRESS, AREA, startDate, startDateText, endDate, endDateText, passportTIET.getText().toString(),
                            FileUtils.getFileName(ReqSuratVisiActivity.this, fileUri));

                    k2FieldInJson = SKetXML.getK2FieldInJsonPribadi(ReqSuratVisiActivity.this, docName, docType, commentET.getText().toString(), PDIRTXM_POSITION_ATTRIBUTE_OF_ROLE_OWNER, CompanyLegalName, OFFICIAL_NAME, ADDRESS,
                            AREA, passportTIET.getText().toString(), etCity.getText().toString(), COUNTRY, startDate, endDate);
                } else if (purposeID == 3) {
                    xmlData = SKetXML.getXMLVisaPribadiKeluarga(ReqSuratVisiActivity.this, PDIRTXM_POSITION_ATTRIBUTE_OF_ROLE_OWNER, CompanyLegalName, docName, docType, commentET.getText().toString(), PPERNRM_EMPLOYEE_HEADER,
                            COUNTRY, etCity.getText().toString(), descriptionTIET.getText().toString(), EMBASSY_ID, OFFICIAL_NAME, ADDRESS, AREA, startDate, startDateText, endDate, endDateText, passportTIET.getText().toString(),
                            FileUtils.getFileName(ReqSuratVisiActivity.this, fileUri), additionalName[0], additionalRelationship[0], additionalPassport[0], additionalName[1], additionalRelationship[1], additionalPassport[1], additionalName[2], additionalRelationship[2], additionalPassport[2],
                            additionalName[3], additionalRelationship[3], additionalPassport[3], additionalName[4], additionalRelationship[4], additionalPassport[4]);

                    k2FieldInJson = SKetXML.getK2FieldInJsonPribadiKeluarga(ReqSuratVisiActivity.this, docName, docType, commentET.getText().toString(), PDIRTXM_POSITION_ATTRIBUTE_OF_ROLE_OWNER, CompanyLegalName, OFFICIAL_NAME, ADDRESS, AREA, passportTIET.getText().toString(),
                            etCity.getText().toString(), COUNTRY, startDate, endDate, additionalName[0], additionalRelationship[0], additionalPassport[0], additionalName[1], additionalRelationship[1], additionalPassport[1], additionalName[2], additionalRelationship[2], additionalPassport[2],
                            additionalName[3], additionalRelationship[3], additionalPassport[3], additionalName[4], additionalRelationship[4], additionalPassport[4]);
                }

                String comment = commentET.getText().toString();
                String requestorADUser = PrefUtils.Build(ReqSuratVisiActivity.this).getPref().getString(Constants.KEY_USERNAME, "");
                String requestorPersonalNumber = PrefUtils.Build(ReqSuratVisiActivity.this).getPref().getString(Constants.KEY_PERSONAL_NUM, "");
                String executorPersonalNumber = PrefUtils.Build(ReqSuratVisiActivity.this).getPref().getString(Constants.KEY_PERSONAL_NUM, "");
                String url = "";
                String k2WorkFlowName = "HRISK2V3\\1010\\Personal Administration\\SuratPengantarVisa";

                submitSKet(k2WorkFlowName, url, executorPersonalNumber, requestorPersonalNumber, requestorADUser, xmlData, xmlDataSummay, comment, k2FieldInJson);
            }
        });

        documentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallerySetup();
            }
        });

        datePickerSetup();

        startDateTIET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerSetup();
                choosenDate = "start";
                datePicker.show();
            }
        });

        endDateTIET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosenDate = "to";
                datePicker.show();
            }
        });

        purposeModels = new ArrayList<>();

        for (int i = 0; i < getResources().getStringArray(R.array.purpose_text).length; i++) {
            PurposeModel purposeModel = new PurposeModel();
            purposeModel.setText(getResources().getStringArray(R.array.purpose_text)[i]);
            purposeModel.setValue(getResources().getStringArray(R.array.purpose_id)[i]);
            purposeModels.add(purposeModel);
        }

        embassyRVET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (embassyModels == null || embassyModels.size() == 0) {
                    return;
                }

                if (charSequence.length() == 0) {
                    embassyAdapter.updateList(embassyModels);
                } else {
                    List<EmbassyModel> filterData = new ArrayList<>();
                    for (int ii = 0; ii < embassyModels.size(); ii++) {
                        if (embassyModels.get(ii).getEmbassyID().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            filterData.add(embassyModels.get(ii));
                        }
                    }

                    embassyAdapter.updateList(filterData);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        embassyDialogContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                embassyDialog.setVisibility(View.GONE);
            }
        });

//        final PurposeAdapter purposeAdapter = new PurposeAdapter(this, android.R.layout.simple_spinner_item, purposeModels);
//        spPurpose.setAdapter(purposeAdapter);
        purposeAdapter = new PurposeRVAdapter(this, purposeModels, this);
        dialogPurposeRV.setLayoutManager(new LinearLayoutManager(this));
        dialogPurposeRV.setAdapter(purposeAdapter);

        additionalFieldRV.setVisibility(View.GONE);

        etPurpose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPurposeContainerRL.setVisibility(View.VISIBLE);
            }
        });

        embassyET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                embassyDialog.setVisibility(View.VISIBLE);
            }
        });

        getEmbassy();
        getCompanies();
        getEmployeeHeaderIdentityOfRoleOwner();
    }

    private boolean additionalDataSetup() {
        for (int i = 0; i < additionalFieldVisaAdapter.getItemCount(); i++) {
            additionalName[i] = ((TextInputEditText) additionalFieldRV.findViewHolderForPosition(i).itemView.findViewById(R.id.listItemAdditionalFieldVisaNameTIET)).getText().toString();
            additionalRelationship[i] = ((TextInputEditText) additionalFieldRV.findViewHolderForPosition(i).itemView.findViewById(R.id.listItemAdditionalFieldVisaTypeTIET)).getText().toString();
            additionalPassport[i] = ((TextInputEditText) additionalFieldRV.findViewHolderForPosition(i).itemView.findViewById(R.id.listItemAdditionalFieldVisaPassportTIET)).getText().toString();
            if (additionalName[i].isEmpty() || additionalPassport[i].isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void openGallerySetup(){
        if (!Utils.hasPermissions(this, Utils.PERMISSIONS)){
            ActivityCompat.requestPermissions(this, Utils.PERMISSIONS, Utils.PERMISSION_ALL);
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, DOCUMENT_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == DOCUMENT_REQUEST){
            if(resultCode == RESULT_OK) {
                fileUri = data.getData();
                filePath = data.getDataString();

                Log.d("datanya", String.valueOf(fileUri));
                Log.d("datanya", String.valueOf(filePath));
                try {

                    fileSize = FileUtils.getSizeInMB(this, fileUri);

                    if (fileSize <= 3) {
                        fileSizeTV.setTextColor(ReqSuratVisiActivity.this.getResources().getColor(R.color.greenLush));
                    } else {
                        fileSizeTV.setTextColor(ReqSuratVisiActivity.this.getResources().getColor(R.color.redDull));
                    }

                    fileSizeTV.setText(FileUtils.getSize(this, fileUri));
                    fileNameTV.setText(FileUtils.getFileName(this, fileUri));

                    fileInfoContainerLL.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    try {
                        File file = new File(filePath);
                        AssetFileDescriptor afd = this.getContentResolver().openAssetFileDescriptor(fileUri,"r");
                        fileSize = (afd.getLength()/1024)/1024;

                        if (file.exists()) {
                            Log.d("datanya", String.valueOf(file.length()));
                        }

                        if (fileSize <= 3) {
                            fileSizeTV.setTextColor(ReqSuratVisiActivity.this.getResources().getColor(R.color.greenLush));
                        } else {
                            fileSizeTV.setTextColor(ReqSuratVisiActivity.this.getResources().getColor(R.color.redDull));
                        }


                        String size = "";
                        if ((afd.getLength()/1024) > 1024) {
                            size = Long.toString((afd.getLength()/1024)/1024) + "MB";
                        } else if (afd.getLength() > 1024) {
                            size = Long.toString((afd.getLength()/1024)) + "KB";
                        } else {
                            size = Long.toString(afd.getLength()) + "B";
                        }

                        fileSizeTV.setText(size);
                        fileNameTV.setText(filePath.substring(filePath.lastIndexOf("/")+1));

                        fileInfoContainerLL.setVisibility(View.VISIBLE);
                        afd.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                }

//                Log.d("datasuratvisi", getFileName(fileUri).split("\\.")[0]);
//                Log.d("datasuratvisi", String.valueOf(sizeIndex/1024));
//                Log.d("datasuratvisi", getMimeType(fileUri));
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (embassyDialog.isShown()) {
            embassyDialog.setVisibility(View.GONE);
            return;
        }

        if (dialogPurposeContainerRL.isShown()) {
            dialogPurposeContainerRL.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }

    private void getEmbassy() {
        loading.show();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        restApi.getEmbassy("MiscellaneousServices", "GetEmbassy")
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

    OnSuccessListener successListener = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            parseJson(strJson);
        }
    };

    private void parseJson(String strJson) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);

        JsonArray jsonArray = jsonObject.getAsJsonArray("Table0");
        embassyModels = new ArrayList<>();

        if (jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                EmbassyModel embassyModel = new EmbassyModel();

                embassyModel.setEmbassyID(jsonArray.get(i).getAsJsonObject().get("EmbassyID").getAsString());
                embassyModel.setCountry(jsonArray.get(i).getAsJsonObject().get("Country").getAsString());
                embassyModel.setOfficialName(jsonArray.get(i).getAsJsonObject().get("OfficialName").getAsString());
                embassyModel.setAddress(jsonArray.get(i).getAsJsonObject().get("Address").getAsString());
                embassyModel.setArea(jsonArray.get(i).getAsJsonObject().get("Area").getAsString());
                embassyModel.setContact(jsonArray.get(i).getAsJsonObject().get("Contact").getAsString());

                embassyModels.add(embassyModel);
            }

            embassyAdapter = new EmbassyAdapter(this, this, embassyModels);
            embassyRV.setLayoutManager(new LinearLayoutManager(this));
            embassyRV.setAdapter(embassyAdapter);

            Log.d("datanyaada", "ada");
        } else {
//            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
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
                                    startActivity(new Intent(ReqSuratVisiActivity.this,
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

    private void datePickerSetup() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker dp, int i, int i1, int i2) {
                final Calendar c = Calendar.getInstance();
                c.set(i, i1, i2);

                String sDate = StringUtils.formatDateSimple(c.getTime());

                if (choosenDate.equalsIgnoreCase("start")) {
                    if (c.getTimeInMillis() > datePicker.getDatePicker().getMaxDate()) {
                        Toast.makeText(ReqSuratVisiActivity.this,
                                "Date Start must be before Date End",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        startDateTIET.setText(sDate);
                        endDateTIET.setText("");
//                        dateFrom = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
//                        datePicker.getDatePicker().setMinDate(0);
                        datePicker.getDatePicker().setMinDate(c.getTimeInMillis());
                    }
                } else {
                    if (c.getTimeInMillis() < datePicker.getDatePicker().getMinDate()) {
                        Toast.makeText(ReqSuratVisiActivity.this,
                                "Date End must be after Date Start",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        endDateTIET.setText(sDate);
//                        dateTo = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
//                        datePicker.getDatePicker().setMaxDate(0);
//                        datePicker.getDatePicker().setMaxDate(c.getTimeInMillis());
                    }
                }
            }
        };

        datePicker = new DatePickerDialog(ReqSuratVisiActivity.this, dateSetListener, year, month, day);
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

    @Override
    public void onEmbassyClicked(EmbassyModel embassyModel) {
        EMBASSY_ID = embassyModel.getEmbassyID();
        OFFICIAL_NAME = embassyModel.getOfficialName();
        COUNTRY = embassyModel.getCountry();
        ADDRESS = embassyModel.getAddress();
        AREA = embassyModel.getArea();
        embassyET.setText(embassyModel.getEmbassyID());
        destinationET.setText(embassyModel.getCountry());
        embassyDialog.setVisibility(View.GONE);
    }

    @Override
    public void onTypeClicked(int position) {
        additionalFieldType = position;
        spAdditionalField.performClick();
    }

    @Override
    public void onPurposeClicked(PurposeModel purposeModel, int position) {
        etPurpose.setText(purposeModel.getText());
        purposeID = position;
        if (purposeModels.size() - 1 == position) {
            additionalFieldVisaModels = new ArrayList<>();
            AdditionalFieldVisaModel additionalFieldVisaModel = new AdditionalFieldVisaModel();
            additionalFieldVisaModel.setName("");
            additionalFieldVisaModel.setType("Spouse");
            additionalFieldVisaModels.add(additionalFieldVisaModel);
            additionalFieldVisaAdapter = new AdditionalFieldVisaAdapter(this, additionalFieldVisaModels, this);
            additionalFieldRV.setLayoutManager(new LinearLayoutManager(this));
            additionalFieldRV.setAdapter(additionalFieldVisaAdapter);

            List<String> types = new ArrayList<>();
            types.add("Spouse");
            types.add("Child");
            additionalFieldAdapter = new AdditionalFieldAdapter(this,  android.R.layout.simple_spinner_item, types);
            spAdditionalField.setAdapter(additionalFieldAdapter);
            additionalFieldRV.setVisibility(View.VISIBLE);

            spAdditionalField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    TextView textView = additionalFieldRV.findViewHolderForAdapterPosition(additionalFieldType).itemView.findViewById(R.id.listItemAdditionalFieldVisaTypeTIET);
                    textView.setText((String) adapterView.getItemAtPosition(i));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } else {
            additionalFieldRV.setVisibility(View.GONE);
        }
        dialogPurposeContainerRL.setVisibility(View.GONE);
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
        try {
            String message = "Could not get data due to:" + strError;

            if (strError.contains("401")) {
                message = "Could not get data. It might be you are Loged in from other device or your session was exiperd.";
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Login again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            startActivity(new Intent(ReqSuratVisiActivity.this,
                                    Class.forName("com.pertamina.portal.activity.LoginActivity")));
                            finish();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            if (alertDialog == null) {
                this.alertDialog = new AlertDialog.Builder(ReqSuratVisiActivity.this)
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // get api

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

    OnSuccessListener successListenerPositionAttributeOfRoleOwner = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            parseJsonPositionAttributeOfRoleOwner(strJson);
        }
    };

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

    OnSuccessListener successListenerCompanies = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            parseJsonCompanies(strJson);
        }
    };

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

    OnSuccessListener successListenerEmployeeHeaderIdentityOfRoleOwner = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            parseJsonEmployeeHeaderIdentityOfRoleOwner(strJson);
        }
    };

    private void parseJsonEmployeeHeaderIdentityOfRoleOwner(String strJson) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);

        JsonArray jsonArray = jsonObject.getAsJsonArray("Table0");

        if (jsonArray.size() > 0) {
            PCNAMEM_EMPLOYEE_HEADER = jsonArray.get(0).getAsJsonObject().get("PCNAMEM").getAsString();
            PPERNRM_EMPLOYEE_HEADER = jsonArray.get(0).getAsJsonObject().get("PPERNRM").getAsString();
//            PDIRTXM_EMPLOYEE_HEADER = jsonArray.get(0).getAsJsonObject().get("PDIRTXM").getAsString();
            PPLANSM_EMPLOYEE_HEADER = jsonArray.get(0).getAsJsonObject().get("PPLANSM").getAsString();

            getPositionAttributeOfRoleOwner(PPLANSM_EMPLOYEE_HEADER);
            Log.d("datanyaada", "ada");
        } else {
//            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        }
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

//    private void createFolder() {
//        loading.show();
//        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
//        restApi.createFolder("FileManagementServices", "CreateFolder", sket, "ROOT\\3rdPartyApps\\IAM\\RequestAttachment", "2")
//                .enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        if (response.isSuccessful()) {
//                            try {
//                                String strResponse = response.body().string();
//                                Log.d("Success submit sket", "getAllWorklist, true _ " + response.raw().toString());
//                                parseXml(strResponse, successListenerCreateFolder);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            handleError(response);
//                            Log.d("Error submit sket", "getAllWorklist, false _ " + response.raw().toString());
//                        }
//
//                        loading.dismiss();
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        loading.dismiss();
//                        Log.d("Failure get history", "getAllWorklist, onFailure..");
//                        t.printStackTrace();
//                    }
//                });
//    }

//    private void createFile() {
//        String strFileName = getFileName(fileUri).split("\\.")[0];
//        String fileExt = "";
//
//        if (getMimeType(fileUri) != null) {
//            fileExt = getMimeType(fileUri);
//        }
//        String fileBinary = "";
//
//        try {
//            InputStream in = getContentResolver().openInputStream(fileUri);
//            bytes=getBytes(in);
//            Log.d("data", "onActivityResult: bytes size="+bytes.length);
//            Log.d("data", "onActivityResult: Base64string="+Base64.encodeToString(bytes, Base64.DEFAULT));
//            fileBinary = Base64.encodeToString(bytes, Base64.DEFAULT);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        loading.show();
//        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
//        restApi.createFile(strFileName, "ROOT\\3rdPartyApps\\IAM\\RequestAttachment\\"+sket, fileExt, fileBinary)
//                .enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        if (response.isSuccessful()) {
//                            try {
//                                String strResponse = response.body().string();
//                                Log.d("Success submit sket", "getAllWorklist, true _ " + response.raw().toString());
//                                parseXml(strResponse, successListenerCreateFile);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            handleError(response);
//                            Log.d("Error submit sket", "getAllWorklist, false _ " + response.raw().toString());
//                        }
//
//                        loading.dismiss();
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        loading.dismiss();
//                        Log.d("Failure get history", "getAllWorklist, onFailure..");
//                        t.printStackTrace();
//                    }
//                });
//    }

    OnSuccessListener successListenerPost = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            parseJsonPost(strJson);
        }
    };

//    OnSuccessListener successListenerCreateFile = new OnSuccessListener() {
//        @Override
//        public void onSuccess(String strJson) {
//            parseJsonCreateFile();
//        }
//    };

//    OnSuccessListener successListenerCreateFolder = new OnSuccessListener() {
//        @Override
//        public void onSuccess(String strJson) {
//            parseJsonCreateFolder(strJson);
//            {"Table0":[{"ReturnType":"S","ReturnMessage":"","ReturnObject":"92835"}]}
//        }
//    };

//    private void parseJsonCreateFolder(String strJson) {
//        Gson gson = new Gson();
//        JsonObject jo = gson.fromJson(strJson, JsonObject.class);
//        JsonArray jArr = jo.getAsJsonArray("Table0");
//
//        if (jArr.get(0).getAsJsonObject().get("ReturnType").getAsString().equalsIgnoreCase("S")) {
//            createFile();
//        }
//    }

    private void parseJsonPost(String strJson) {
        sket = strJson;
        alertDialogDismiss.setMessage(sket);
        alertDialogDismiss.show();
        alertDialogDismiss.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                finish();
            }
        });
        SketVisaBackground.getCreateFolder(this, sket, fileUri).execute();
//        createFolder();
    }

//    private void parseJsonCreateFile() {
//        alertDialogDismiss.setMessage(sket);
//        alertDialogDismiss.show();
//        alertDialogDismiss.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                finish();
//            }
//        });
//    }

//    public byte[] getBytes(InputStream inputStream) throws IOException {
//        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
//        int bufferSize = 1024;
//        byte[] buffer = new byte[bufferSize];
//
//        int len = 0;
//        while ((len = inputStream.read(buffer)) != -1) {
//            byteBuffer.write(buffer, 0, len);
//        }
//        return byteBuffer.toByteArray();
//    }
//
//    public String getMimeType(Uri uri) {
//        String extension;
//
//        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
//            final MimeTypeMap mime = MimeTypeMap.getSingleton();
//            extension = mime.getExtensionFromMimeType(getContentResolver().getType(uri));
//        } else {
//            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
//
//        }
//
//        return extension;
//    }
//
//    public String getFileName(Uri uri) {
//        String result = null;
//        if (uri.getScheme().equals("content")) {
//            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//            try {
//                if (cursor != null && cursor.moveToFirst()) {
//                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
//                }
//            } finally {
//                cursor.close();
//            }
//        }
//        if (result == null) {
//            result = uri.getPath();
//            int cut = result.lastIndexOf('/');
//            if (cut != -1) {
//                result = result.substring(cut + 1);
//            }
//        }
//        return result;
//    }
}
