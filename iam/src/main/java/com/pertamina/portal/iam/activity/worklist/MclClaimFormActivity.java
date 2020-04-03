package com.pertamina.portal.iam.activity.worklist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.pertamina.portal.core.interfaces.OnSuccessListener;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.core.utils.StringUtils;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.base.BaseWorklistActivity;
import com.pertamina.portal.iam.adapters.worklist.CostCenterAdapter;
import com.pertamina.portal.iam.adapters.worklist.MclOptionsAdapters;
import com.pertamina.portal.iam.models.ClaimRequest;
import com.pertamina.portal.iam.models.CostCenter;
import com.pertamina.portal.iam.models.MclOption;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MclClaimFormActivity extends BaseWorklistActivity {
    // TODO tinggal tambah access
    private static final String TAG = "LevDetailActivity";
    private AlertDialog loading;
    private String processInstanceId;
    private String personalNum;
    private ClaimRequest claimRequest;
    private EditText etClaimType, etPatient, etPatientAge, etClaimAmount, etGrantedAmount,
            etBillDate, etBillNumber;
    private Button btSubmit;
    private Button btCancel;
    private CheckBox cbPek;
    private CheckBox cbIs;
    private CheckBox cbSu;
    private CheckBox cbAn;
    private EditText etDiagnos1;
    private EditText etDiagnos2;
    private EditText etDiagnos3;
    private EditText etDiagnos4;
    private List<MclOption> list = new ArrayList<>();
    private List<CostCenter> listCostCenter = new ArrayList<>();
    private Spinner spDiagnose1;
    private Spinner spDiagnose2;
    private Spinner spDiagnose3;
    private Spinner spCostCenter;
    private TextInputLayout tilDiagnos1;
    private TextInputLayout tilDiagnos2;
    private TextInputLayout tilDiagnos3;
    private TextInputLayout tilDiagnos4;
    private TextInputEditText costCenterTIET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcl_claim_form);
        super.onCreateBackable(this, R.id.ivBack);

        loading = getLoading();
        etClaimType = (EditText) findViewById(R.id.etClaimType);
        etPatient = (EditText) findViewById(R.id.etPatient);
        etPatientAge = (EditText) findViewById(R.id.etPatientAge);
        etClaimAmount = (EditText) findViewById(R.id.etClaimAmount);
        etGrantedAmount = (EditText) findViewById(R.id.etGrantedAmount);
        etBillDate = (EditText) findViewById(R.id.etBillDate);
        etBillNumber = (EditText) findViewById(R.id.etBillNumber);
        etDiagnos1 = (EditText) findViewById(R.id.etDiagnose1);
        etDiagnos2 = (EditText) findViewById(R.id.etDiagnose2);
        etDiagnos3 = (EditText) findViewById(R.id.etDiagnose3);
        etDiagnos4 = (EditText) findViewById(R.id.etDiagnose4);
        tilDiagnos1 = findViewById(R.id.tilDiagnose1);
        tilDiagnos2 = findViewById(R.id.tilDiagnose2);
        tilDiagnos3 = findViewById(R.id.tilDiagnose3);
        tilDiagnos4 = findViewById(R.id.tilDiagnose4);
        btSubmit = (Button) findViewById(R.id.btSubmit);
        btCancel = (Button) findViewById(R.id.btCancel);
        cbPek = (CheckBox) findViewById(R.id.cbPek);
        cbIs = (CheckBox) findViewById(R.id.cbIs);
        cbSu = (CheckBox) findViewById(R.id.cbSu);
        cbAn = (CheckBox) findViewById(R.id.cbAn);
        spDiagnose1 = (Spinner) findViewById(R.id.spDiagnose1);
        spDiagnose2 = (Spinner) findViewById(R.id.spDiagnose2);
        spDiagnose3 = (Spinner) findViewById(R.id.spDiagnose3);
        spCostCenter = (Spinner) findViewById(R.id.spCostCenter);
        costCenterTIET = findViewById(R.id.etCostCenter);

        disableAllView();

        claimRequest = (ClaimRequest) getIntent().getSerializableExtra(MclDetailActivity.CLAIM_REQUEST);

        if (claimRequest.diagnoseId1.equalsIgnoreCase("")) {
            tilDiagnos1.setVisibility(View.GONE);
        } else {
            etDiagnos1.setText(claimRequest.diagnoseId1 + " - " + claimRequest.diagnoseTxt1);
        }

        if (claimRequest.diagnoseId2.equalsIgnoreCase("")) {
            tilDiagnos2.setVisibility(View.GONE);
        } else {
            etDiagnos2.setText(claimRequest.diagnoseId2 + " - " + claimRequest.diagnoseTxt2);
        }

        if (claimRequest.diagnoseId3.equalsIgnoreCase("")) {
            tilDiagnos3.setVisibility(View.GONE);
        } else {
            etDiagnos3.setText(claimRequest.diagnoseId3 + " - " + claimRequest.diagnoseTxt3);
        }

        if (claimRequest.diagnose4.equalsIgnoreCase("")) {
            tilDiagnos4.setVisibility(View.GONE);
        } else {
            etDiagnos4.setText(claimRequest.diagnose4);
        }

        if (claimRequest != null) {
            try {
                String strClaimAmount = "Rp " + StringUtils.formatCurrency(claimRequest.claimAmount);
                etClaimAmount.setText(strClaimAmount);
            } catch (Exception e) {
                e.printStackTrace();
            }

            etClaimType.setText(claimRequest.claimType);
            etPatient.setText(claimRequest.patient);
            etPatientAge.setText(claimRequest.patientAge);
            try {
                String strGrantedAmount = "Rp " + StringUtils.formatCurrency(claimRequest.grantedAmount);
                etGrantedAmount.setText(strGrantedAmount);
            } catch (Exception e) {
                e.printStackTrace();
            }
            etBillNumber.setText(claimRequest.billNumber);

            String strBillDate = StringUtils.reformatDateYyyyMmDd(claimRequest.billDate);
            etBillDate.setText(strBillDate);

            String status = claimRequest.strStatus;

            if (status.equalsIgnoreCase("P")) {
                cbPek.setChecked(true);
            } else if (status.equalsIgnoreCase("I")) {
                cbIs.setChecked(true);
            } else if (status.equalsIgnoreCase("S")) {
                cbSu.setChecked(true);
            } else if (status.equalsIgnoreCase("A")) {
                cbAn.setChecked(true);
            }
        }

        buildSpinner();

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String grantedAmount = etGrantedAmount.getText().toString();
                claimRequest.grantedAmount = grantedAmount;
                claimRequest.diagnose4 = etDiagnos4.getText().toString();
                Intent returnIntent = new Intent();

                returnIntent.putExtra(MclDetailActivity.CLAIM_REQUEST, claimRequest);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });

        costCenterTIET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spCostCenter.performClick();
            }
        });

        etDiagnos1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spDiagnose1.performClick();
            }
        });

        etDiagnos2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spDiagnose2.performClick();
            }
        });

        etDiagnos3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spDiagnose3.performClick();
            }
        });

    }

    private void disableAllView() {
        etClaimType.setEnabled(false);
        etPatient.setEnabled(false);
        etPatientAge.setEnabled(false);
        etClaimAmount.setEnabled(false);
        etBillDate.setEnabled(false);
        etBillNumber.setEnabled(false);
        btSubmit.setEnabled(false);
        btCancel.setEnabled(false);
        cbPek.setEnabled(false);
        cbIs.setEnabled(false);
        cbSu.setEnabled(false);
        cbAn.setEnabled(false);
        if (getIntent().hasExtra("type")) {
            etGrantedAmount.setEnabled(false);
            etDiagnos1.setEnabled(false);
            etDiagnos2.setEnabled(false);
            etDiagnos3.setEnabled(false);
            etDiagnos4.setEnabled(false);
            spDiagnose1.setEnabled(false);
            spDiagnose2.setEnabled(false);
            spDiagnose3.setEnabled(false);

            spCostCenter.setEnabled(false);
            costCenterTIET.setEnabled(false);

            btCancel.setVisibility(View.GONE);
            btSubmit.setVisibility(View.GONE);

            if (getIntent().getStringExtra("type").equalsIgnoreCase("pending")) {
                etGrantedAmount.setVisibility(View.GONE);
            }
        } else {
            etGrantedAmount.setEnabled(true);
            etDiagnos1.setEnabled(true);
            etDiagnos2.setEnabled(true);
            etDiagnos3.setEnabled(true);
            etDiagnos4.setEnabled(true);
            spDiagnose1.setEnabled(true);
            spDiagnose2.setEnabled(true);
            spDiagnose3.setEnabled(true);

            spCostCenter.setEnabled(true);
            costCenterTIET.setEnabled(true);

            btCancel.setVisibility(View.VISIBLE);
            btSubmit.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getMclOptions();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getMclOptions() {
        loading.show();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        restApi.getMclOptions("BenefitServices", "GetICD10")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d(TAG, "getProcessInstance sip.. " + strResponse);
                                parseXml(strResponse, mclOpsSuccessListener);
                                getCostCenter();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            MclClaimFormActivity.super.handleError(response);
                            Log.d(TAG, "getProcessInstance false _ " + response.raw().toString());
                        }

                        loading.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        Log.d(TAG, "getWorklistPending onFailure..");
                        MclClaimFormActivity.super.showError("Failed connect to server");
                        t.printStackTrace();
                    }
                });
    }

    private void getCostCenter() {
        loading.show();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        restApi.getCostCenter("BenefitServices", "GetMedicalCostCenter", "",
                "1010", "", "", "0")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d(TAG, "getProcessInstance sip.. " + strResponse);
                                parseXml(strResponse, costCenterSuccessListener);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            MclClaimFormActivity.super.handleError(response);
                            Log.d(TAG, "getProcessInstance false _ " + response.raw().toString());
                        }

                        loading.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        Log.d(TAG, "getWorklistPending onFailure..");
                        MclClaimFormActivity.super.showError("Failed connect to server");
                        t.printStackTrace();
                    }
                });
    }

    public void parseJsonMclOps(String json) {
        Log.d(TAG, "parseJsonMclOps");
        try {
            JSONObject jo = new JSONObject(json);
            JSONObject joItem;
            MclOption mclOption;
            JSONArray jArr = jo.getJSONArray("Table0");

            for (int i = 0; i < jArr.length(); i++) {
                joItem = jArr.getJSONObject(i);

                mclOption = new MclOption();
                mclOption.icdId = joItem.getString("ICDID");
                mclOption.icdDescr = joItem.getString("ICDDescr");
                mclOption.requestType = joItem.getString("RequestType");

                list.add(mclOption);
            }

            MclOptionsAdapters adapter1 = new MclOptionsAdapters(this, android.R.layout.simple_spinner_item, list);
            MclOptionsAdapters adapter2 = new MclOptionsAdapters(this, android.R.layout.simple_spinner_item, list);
            MclOptionsAdapters adapter3 = new MclOptionsAdapters(this, android.R.layout.simple_spinner_item, list);

            spDiagnose1.setAdapter(adapter1);
            spDiagnose2.setAdapter(adapter2);
            spDiagnose3.setAdapter(adapter3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void buildSpinner() {
        spDiagnose1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MclOption data = (MclOption) adapterView.getItemAtPosition(i);
                claimRequest.diagnoseId1 = data.icdId;
                claimRequest.diagnoseTxt1 = data.icdDescr;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spDiagnose2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MclOption data = (MclOption) adapterView.getItemAtPosition(i);
                claimRequest.diagnoseId2 = data.icdId;
                claimRequest.diagnoseTxt2 = data.icdDescr;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spDiagnose3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MclOption data = (MclOption) adapterView.getItemAtPosition(i);
                claimRequest.diagnoseId3 = data.icdId;
                claimRequest.diagnoseTxt3 = data.icdDescr;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spCostCenter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CostCenter costCenter = (CostCenter) adapterView.getItemAtPosition(i);
                claimRequest.costCenter = costCenter.PKOSTLM;
                costCenterTIET.setText("[" + costCenter.PKOSTLM + "] " + costCenter.PVERAKM);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    OnSuccessListener mclOpsSuccessListener = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            Log.d(TAG, "onSuccess:strJson = " + strJson);
            parseJsonMclOps(strJson);
        }
    };

    OnSuccessListener costCenterSuccessListener = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            Log.d(TAG, "onSuccess:strJson = " + strJson);
            parseJsonCostCenter(strJson);
        }
    };

    public void parseJsonCostCenter(String json) {
        Log.d(TAG, "parseJsonCostCenter");
        try {
            JSONObject jo = new JSONObject(json);
            JSONObject joItem;
            CostCenter costCenter;
            JSONArray jArr = jo.getJSONArray("Table0");

            for (int i = 0; i < jArr.length(); i++) {
                joItem = jArr.getJSONObject(i);

                costCenter = new CostCenter();
                costCenter.PKOKRSM = joItem.getString("PKOKRSM");
                costCenter.PKOSTLM = joItem.getString("PKOSTLM");
                costCenter.PDATBIM = joItem.getString("PDATBIM");
                costCenter.PVERAKM = joItem.getString("PVERAKM");

                listCostCenter.add(costCenter);
            }

            claimRequest.costCenter = listCostCenter.get(0).PKOSTLM;

            CostCenterAdapter adapter1 = new CostCenterAdapter(this, android.R.layout.simple_spinner_item, listCostCenter);
            spCostCenter.setAdapter(adapter1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
