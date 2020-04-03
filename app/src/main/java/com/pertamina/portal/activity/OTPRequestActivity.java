package com.pertamina.portal.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pertamina.portal.R;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.PrefUtils;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.utils.KeyboardUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPRequestActivity extends AppCompatActivity {

    @BindView(R.id.btnSend)
    Button btnSend;

    @BindView(R.id.tvPhoneNumber)
    TextView tvPhoneNumber;
    private AlertDialog loading;
    private boolean isResetPIN = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otprequest);

        ButterKnife.bind(this);

        isResetPIN = getIntent().hasExtra("resetpin");

        PortalApiInterface restApi = RestClient.getRetrofitAuthenticated(this, 2000);
        String phoneNum = getIntent().getStringExtra(Constants.KEY_PHONE_NUM);
        loading = new SpotsDialog.Builder().setContext(this).build();

        if (phoneNum != null) {
            tvPhoneNumber.setText(phoneNum);
        } else {
            phoneNum = PrefUtils.Build(this).getPref().getString(Constants.KEY_PHONE_NUM, null);

            if (phoneNum != null) {
                tvPhoneNumber.setText(phoneNum);
            }
        }

        Log.d("Otp", "phoneNum = " + phoneNum);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.show();

                restApi.sendOtp().enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        loading.hide();

                        if (response.body() != null) {
                            Log.d("Otp", response.body().toString());
                        } else {
                            Log.d("Otp", "response.body() null");

                            if (response.errorBody() != null) {
                                try {
                                    String jsonString = response.errorBody().string();

                                    Log.e("Otp", jsonString);

                                    JsonObject jo = new JsonParser().parse(jsonString).getAsJsonObject();
                                    String msg = jo.get("message").getAsString();

                                    if (msg == null) {
                                        msg = "Error on requesting OTP. Please clear App Data " +
                                                "or reinstall the App.";
                                    } else {
                                        msg = msg + " Please clear App Data or reinstall the App.";
                                    }

                                    Toast.makeText(OTPRequestActivity.this,
                                            msg, Toast.LENGTH_LONG).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        if (response.isSuccessful()) {
                            PrefUtils.Build(OTPRequestActivity.this).getPref()
                                    .edit()
                                    .putInt(Constants.KEY_AUTH_STEP, 3)
                                    .apply();

                            Intent intent = new Intent(OTPRequestActivity.this, OTPInputActivity.class);
                            if (isResetPIN) {
                                intent.putExtra("resetpin", getIntent().getStringExtra("resetpin"));
                            }
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.d("Otp", "onFailure");

                        loading.hide();
                    }
                });
            }
        });

        KeyboardUtils.setupUI(this, findViewById(R.id.flOTPRequestParent));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loading.dismiss();
    }
}
