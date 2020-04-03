package com.pertamina.portal.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.pertamina.portal.BuildConfig;
import com.pertamina.portal.R;
import com.pertamina.portal.adapters.CompanyAdapter;
import com.pertamina.portal.adapters.CompanyAdapters;
import com.pertamina.portal.core.encryption.Aes;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.PrefUtils;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.iam.activity.ReqSuratKetActivity;
import com.pertamina.portal.interfaces.LoginView;
import com.pertamina.portal.models.Company;
import com.pertamina.portal.utils.KeyboardUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements Validator.ValidationListener, LoginView {

    private static final String TAG = "app:LoginActivity";
    @BindView(R.id.tiPin)
    TextInputLayout tiPassword;

    @BindView(R.id.btnSend)
    Button btnSignin;

    @NotEmpty
    @BindView(R.id.etCompanyCode)
    TextInputEditText etCompanyCode;

    @NotEmpty
//    @Length(min = 3, max = 25, message = "Min 3 character and max 25")
    @BindView(R.id.etUserId)
    TextInputEditText etUserId;

    @NotEmpty
//    @Length(min = 3, max = 20, message = "Min 3 character and max 20")
    @BindView(R.id.etPin)
    TextInputEditText etPassword;

    @BindView(R.id.tvBuildNumber)
    TextView tvBuildNumber;

    @BindView(R.id.loginDialogCountryView)
    View dialogCompanyView;

    @BindView(R.id.dialogCountryRV)
    RecyclerView companyRV;

    @BindView(R.id.dialogCountrySearcET)
    EditText companySearchET;

    @BindView(R.id.dialogCountryContainerCL)
    ConstraintLayout commpanyContainerCL;

    @BindView(R.id.dialogCountryTitleTV) TextView companyTitleTV;

    private PortalApiInterface restApiBasic;
    private String companyDomain;
    private AlertDialog loading;
    private AlertDialog alertDialog;
    private CompanyAdapter companyAdapter;
    private List<Company> companyList;
    private boolean isNoPhoneNumber = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        if (PrefUtils.Build(this).getPref().getString(Constants.KEY_REFRESH_TOKEN, "") != null &&
                !PrefUtils.Build(this).getPref().getString(Constants.KEY_REFRESH_TOKEN, "").equalsIgnoreCase("")) {
            Intent intent = new Intent(this, PinInputActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("refresh_token", "yes");
            startActivity(intent);
            finishAffinity();
            return;
        }

        Validator validator = new Validator(this);
        validator.setValidationListener(this);

        restApiBasic = RestClient.getRetrofitInstanceBasic(this, 2000);
        loading = new SpotsDialog.Builder().setContext(this).build();

        companyTitleTV.setText("Please Select");

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

        etCompanyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCompanyView.setVisibility(View.VISIBLE);
            }
        });

        tvBuildNumber.setText("BN " + BuildConfig.VERSION_NAME);
        etCompanyCode.setEnabled(false);

        alertDialog = new AlertDialog.Builder(this)
                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        if (isNoPhoneNumber) {
                            finish();
                        }
                    }
                }).create();

        companySearchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (companyList == null || companyList.size() == 0) {
                    return;
                }
                if (charSequence.length() == 0) {
                    companyAdapter.updateList(companyList);
                } else {
                    List<Company> filterData = new ArrayList<>();
                    for (int ii = 0; ii < companyList.size(); ii++) {
                        if (companyList.get(ii).getCompanyName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            filterData.add(companyList.get(ii));
                        }
                    }

                    companyAdapter.updateList(filterData);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        KeyboardUtils.setupUI(this, findViewById(R.id.flLoginParent));
    }

    @OnClick(R.id.dialogCountryContainerCL)
    public void onCompanyContainerCLClicked() {
        dialogCompanyView.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (dialogCompanyView.isShown()) {
            dialogCompanyView.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!loading.isShowing()) {
            loading.show();
        }

        restApiBasic.getCompanyCodes().enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    Log.d("Login", response.body().toString());

                    JsonArray jsonArray = response.body();
                    companyList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.size(); i++) {
                        String companyName = jsonArray.get(i)
                                .getAsJsonObject()
                                .get("CompanyName")
                                .getAsString();
                        String companyDomain = jsonArray.get(i)
                                .getAsJsonObject()
                                .get("DomainName")
                                .getAsString();

                        Company company = new Company();
                        company.setCompanyName(companyName);
                        company.setDomainName(companyDomain);

                        companyList.add(company);
                    }

                    etCompanyCode.setText(companyList.get(0).getCompanyName());
                    companyDomain = companyList.get(0).getDomainName();

                    companyAdapter = new CompanyAdapter(LoginActivity.this, companyList, LoginActivity.this::onCompanyClicked);
                    companyRV.setLayoutManager(new LinearLayoutManager(LoginActivity.this));
                    companyRV.setAdapter(companyAdapter);
                }

                etCompanyCode.setEnabled(true);
                loading.hide();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("Login", "onFailure");
                Log.d("Login", "onFailure" + t.getMessage());
                etCompanyCode.setEnabled(true);
                loading.hide();
            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        PortalApiInterface restApi = RestClient.getRetrofitInstance(this, 200);
        String username = companyDomain + "\\" + etUserId.getText().toString();
        String password = etPassword.getText().toString();
        String encPassword = "";

        try {
            encPassword = Aes.encrypt(password, Constants.SALT);

            Log.d("Login", "encPassword:" + encPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }

        loading.show();

        restApi.login("password", username, encPassword).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject jsonObject = response.body();
                    Log.d("LoginActivity", "response:" + jsonObject.toString());

                    if (jsonObject.get("PUSRIDM") == null) {
                        alertDialog.setMessage("No HP anda belum terdaftar, Silahkan daftarkan di web-i-am!");
                        alertDialog.show();
                        return;
                    }

                    String accessToken = "";
                    if (jsonObject.get("access_token") != null) {
                        accessToken = jsonObject.get("access_token").getAsString();
                    }

                    String refreshToken = "";
                    if (jsonObject.get("refresh_token") != null) {
                        refreshToken = jsonObject.get("refresh_token").getAsString();
                    }

                    String phoneNum = "";
                    if (jsonObject.get("PUSRIDM") != null) {
                        phoneNum = jsonObject.get("PUSRIDM").getAsString();
                    }

                    String personalNumber = "";
                    if (jsonObject.get("PPERNRM") != null) {
                        personalNumber = jsonObject.get("PPERNRM").getAsString();
                    }

                    String username = "";
                    if (jsonObject.get("ADUserName") != null) {
                        username = jsonObject.get("ADUserName").getAsString();
                    }

                    // PPERNRM

                    String pcNamem = "";
                    if (jsonObject.get("PCNAMEM") != null) {
                        pcNamem = jsonObject.get("PCNAMEM").getAsString();
                    }

                    String email = "";
                    if (jsonObject.get("EmailUserID") != null) {
                        email = jsonObject.get("EmailUserID").getAsString();
                    }

                    String photo = "";
                    if (jsonObject.get("UrlPhotoNew") != null) {
                        photo = jsonObject.get("UrlPhotoNew").getAsString();
                    }

                    String PPLANSM = "";
                    if (jsonObject.get("PPLANSM") != null) {
                        PPLANSM = jsonObject.get("PPLANSM").getAsString();
                    }

                    String PBUKRSM = "";
                    if (jsonObject.get("PBUKRSM") != null) {
                        PBUKRSM = jsonObject.get("PBUKRSM").getAsString();
                    }

                    String PBUTXTM = "";
                    if (jsonObject.get("PBUTXTM") != null) {
                        PBUTXTM = jsonObject.get("PBUTXTM").getAsString();
                    }

                    String PPERNRM = "";
                    if (jsonObject.get("PPERNRM") != null) {
                        PPERNRM = jsonObject.get("PPERNRM").getAsString();
                    }

                    String PPLTXTM = "";
                    if (jsonObject.get("PPLTXTM") != null) {
                        PPLTXTM = jsonObject.get("PPLTXTM").getAsString();
                    }

                    String KBO = "";
                    if (jsonObject.get("KBO") != null) {
                        KBO = jsonObject.get("KBO").getAsString();
                    }

                    String PersonID = "";
                    if (jsonObject.get("PersonID") != null) {
                        PersonID = jsonObject.get("PersonID").getAsString();
                    }

                    PrefUtils.Build(getApplicationContext()).getPrefEditable()
                            .putString(Constants.KEY_ACCES_TOKEN, accessToken)
                            .putString(Constants.KEY_REFRESH_TOKEN, refreshToken)
                            .putString(Constants.KEY_AUTH_DATA, jsonObject.toString())
                            .putString(Constants.KEY_PERSONAL_NUM, personalNumber)
                            .putString(Constants.KEY_PHONE_NUM, phoneNum)
                            .putString(Constants.KEY_USERNAME, username)
                            .putString(Constants.KEY_PCNAMEM, pcNamem)
                            .putString(Constants.KEY_EMAIL, email)
                            .putString(Constants.KEY_PHOTO, photo)
                            .putString(Constants.KEY_PPLANSM, PPLANSM)
                            .putString(Constants.KEY_PBUKRSM, PBUKRSM)
                            .putString(Constants.KEY_PBUTXTM, PBUTXTM)
                            .putString(Constants.KEY_PPERNRM, PPERNRM)
                            .putString(Constants.KEY_PPLTXTM, PPLTXTM)
                            .putString(Constants.KEY_PERSON_ID, PersonID)
                            .putString(Constants.KEY_KBO, KBO)
                            .putInt(Constants.KEY_AUTH_STEP, 4)
                            .apply();

//                    Intent intent = new Intent(LoginActivity.this, OTPRequestActivity.class);
                    Intent intent = new Intent(LoginActivity.this, PinInputActivity.class);
                    intent.putExtra(Constants.KEY_PHONE_NUM, phoneNum);
                    startActivity(intent);

                    finish();
                } else {
                    try {
                        String strJson = response.errorBody().string();
                        Log.d(TAG, "response = " + strJson);

                        JsonObject jObjError = new JsonParser().parse(strJson).getAsJsonObject();
                        String errMsg = jObjError.get("error_description").getAsString();

                        if (alertDialog != null) {
                            alertDialog.setMessage(errMsg);
                            alertDialog.show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                loading.hide();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loading.hide();
            }
        });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof TextInputEditText) {
                ((TextInputEditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            loading.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompanyClicked(Company company) {
        etCompanyCode.setText(company.getCompanyName());
        companyDomain = company.getDomainName();
        etCompanyCode.setError(null);
        dialogCompanyView.setVisibility(View.GONE);
    }
}
