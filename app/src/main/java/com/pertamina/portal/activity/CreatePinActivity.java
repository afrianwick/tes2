package com.pertamina.portal.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePinActivity extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty
    @Length(min = 6, max = 6, message = "Pin must be 6 digit")
    @BindView(R.id.etPin)
    TextInputEditText etPin;

    @NotEmpty
    @Length(min = 6, max = 6, message = "Pin Confirmation must be 6 digit")
    @BindView(R.id.etPinRepeat)
    TextInputEditText etPinRepeat;

    @BindView(R.id.btnSend)
    Button btnSend;
    private PortalApiInterface restApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pin);

        ButterKnife.bind(this);

        restApi = RestClient.getRetrofitAuthenticated(this, 2000);
        Validator validator = new Validator(this);
        validator.setValidationListener(this);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

        KeyboardUtils.setupUI(this, findViewById(R.id.flCreatePINParent));
    }

    @Override
    public void onValidationSucceeded() {
        String pin = etPin.getText().toString();
        String pinConfirm = etPinRepeat.getText().toString();
        String encryptedPin = Aes.encrypt(pin, Constants.SALT);

        if (pin.equalsIgnoreCase(pinConfirm)) {
            restApi.createPin(encryptedPin).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        PrefUtils.Build(CreatePinActivity.this).getPref()
                                .edit()
                                .putInt(Constants.KEY_AUTH_STEP, 0)
                                .apply();
                        Intent intent = new Intent(CreatePinActivity.this, PortalHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
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
                    Log.d("PinInput", "onFailure = " + t.getMessage());
                }
            });
        } else {
            etPinRepeat.setError("Pin must be same as Confirm Pin");
        }
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
}
