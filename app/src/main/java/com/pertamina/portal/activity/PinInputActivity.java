package com.pertamina.portal.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.pertamina.portal.R;
import com.pertamina.portal.core.encryption.Aes;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.PrefUtils;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.utils.KeyboardUtils;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PinInputActivity extends AppCompatActivity implements Validator.ValidationListener {

    @BindView(R.id.btnSend)
    Button btnNext;

    @BindView(R.id.tvCreatePin)
    TextView tvCreatePin;

    @BindView(R.id.tvLabelCreatePIN)
    TextView tvLabelCreatePIN;

    @BindView(R.id.tvSignOut)
    TextView tvSignOut;

    @NotEmpty
    @Length(min = 6, max = 6, message = "Pin must be 6 digit")
    @BindView(R.id.etPin)
    TextInputEditText etPin;
    private PortalApiInterface restApi;
    private Context context;
    private AlertDialog loading;
    private AlertDialog alertDialog;
    private boolean isNoPhoneNumber = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_input);

        ButterKnife.bind(this);

        alertDialog = new AlertDialog.Builder(this)
                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(PinInputActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).create();

        context = getApplicationContext();
        restApi = RestClient.getRetrofitAuthenticated(this, 2000);
        loading = new SpotsDialog.Builder().setContext(this).build();
        Validator validator = new Validator(this);
        validator.setValidationListener(this);
        
        if (getIntent().hasExtra("refresh_token")) {
            requestToken();
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

        tvCreatePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PinInputActivity.this, OTPRequestActivity.class);
                intent.putExtra("resetpin", "yes");
                startActivity(intent);
                finish();
            }
        });

        tvSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrefUtils.Build(PinInputActivity.this).removeAll();
                Intent intent = new Intent(PinInputActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        KeyboardUtils.setupUI(this, findViewById(R.id.flPINInputParent));
    }

    private void requestToken() {
        loading.show();
        restApi.refreshToken("refresh_token", PrefUtils.Build(this).getPref().getString(Constants.KEY_REFRESH_TOKEN, ""))
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        loading.dismiss();

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

                        } else {
                            PrefUtils.Build(PinInputActivity.this).removeAll();
                            Intent intent = new Intent(PinInputActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        loading.dismiss();
                        PrefUtils.Build(PinInputActivity.this).removeAll();
                        Intent intent = new Intent(PinInputActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // TODO set text name "Please Enter PIN" dynamicaly

        boolean pinAlreadyCreated = true; // dummy value

        if (pinAlreadyCreated) {
            tvLabelCreatePIN.setText("Please enter your PIN");
        } else {
            tvLabelCreatePIN.setText("Register your PIN");
        }

    }

    @Override
    public void onValidationSucceeded() {
        String encryptedPin = Aes.encrypt(etPin.getText().toString(), Constants.SALT);

        loading.show();
        restApi.validatePin(encryptedPin).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loading.hide();

                if (response.isSuccessful()) {
                    PrefUtils.Build(PinInputActivity.this).getPref()
                            .edit()
                            .putInt(Constants.KEY_AUTH_STEP, 5)
                            .apply();

                    Intent intent = new Intent(PinInputActivity.this, PortalHomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    String strJson = null;

                    try {
                        strJson = response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Log.d("PinInput", "strOtp = " + strJson);

                    JsonObject jObjError = new JsonParser().parse(strJson).getAsJsonObject();
                    String errMsg = jObjError.get("message").getAsString();

                    Toast.makeText(context, errMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loading.hide();

                Log.d("PinInput", "onFailure = " + t.getMessage());
            }
        });

//        // Go to main page
//        Intent intent = new Intent(PinInputActivity.this, PortalHomeActivity.class);
//        startActivity(intent);
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
        loading.dismiss();
    }
}
