package com.pertamina.portal.iam.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pertamina.portal.core.encryption.Aes;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.PrefUtils;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.utils.KeyboardUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InputPinPayslipActivity extends AppCompatActivity {

    private PortalApiInterface restApi;
    com.google.android.material.textfield.TextInputEditText edpin;
    Button submit;

    TextView tvdissmiss;
    private AlertDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_pin_payslip);

        restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);

        edpin = findViewById(R.id.etPin);
        submit = findViewById(R.id.btnSend);
        tvdissmiss = findViewById(R.id.tvdissmiss);

        loading = new SpotsDialog.Builder().setContext(this).build();

        tvdissmiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validate()){

                    return;
                }
                checkValidatePin();
            }
        });
        KeyboardUtils.setupUI(this, findViewById(R.id.flPINInputParent));
    }
    public void checkValidatePin()
    {

        String encryptedPin = Aes.encrypt(edpin.getText().toString(), Constants.SALT);
        loading.show();
        restApi.validatePin(encryptedPin)
                .enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loading.hide();
                System.out.println("input pin done");
                if (response.isSuccessful()) {
                    System.out.println("menuju kesini ntuk pathnya");
                    Intent intent = new Intent(getApplicationContext(), PayslipActivity.class);
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

                    Toast.makeText(getApplicationContext(), errMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loading.hide();
                System.out.println("input pin salah");
                Log.d("PinInput", "onFailure = " + t.getMessage());
            }
        });
    }
    public boolean validate() {
        boolean valid = true;

        String vessel = edpin.getText().toString();


        if (vessel.isEmpty()) {
            edpin.setError("Pin field is still empty");
            valid = false;
        } else {
            edpin.setError(null);
        }

        if ( vessel.length() !=6) {
            edpin.setError("Pin must be 6 input digit number");
            valid = false;
        } else {
            edpin.setError(null);
        }

        return valid;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loading.dismiss();
    }


}
