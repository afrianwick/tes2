package com.pertamina.portal.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pertamina.portal.R;
import com.pertamina.portal.core.encryption.Aes;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.PrefUtils;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.interfaces.MessageListener;
import com.pertamina.portal.services.MessageReceiver;
import com.pertamina.portal.utils.KeyboardUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPInputActivity extends AppCompatActivity implements MessageListener {

    private static final String TAG = "OTPInputActivity";

    @BindView(R.id.btnSend)
    Button btnSend;

    @BindView(R.id.tvResend)
    TextView tvResend;

    @BindView(R.id.tvLabelResend)
    TextView tvLabelResend;

    private String[] otp = new String[6];
    private EditText[] arrEt = new EditText[6];
    private int count = 60;
    private int resendClickCount = 0;
    private PortalApiInterface restApi;
    private AlertDialog loading;
    private Context context;
    private int index;
    private PinView pinView;
    private boolean isResetPIN = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpinput);

        ButterKnife.bind(this);
        MessageReceiver.bindListener(this);

        isResetPIN = getIntent().hasExtra("resetpin");

        context = getApplicationContext();
        restApi = RestClient.getRetrofitAuthenticated(this, 2000);
        loading = new SpotsDialog.Builder().setContext(this).build();
        pinView = findViewById(R.id.pinView);

        pinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() < 6)
                    return;

                loading.show();
                String encOtp = Aes.encrypt(pinView.getText().toString(), Constants.SALT);

                Log.d(TAG, "strOtp = " + pinView.getText().toString());
                Log.d(TAG, "encOtp = " + encOtp);

                validateOtp(encOtp);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.show();
                String encOtp = Aes.encrypt(pinView.getText().toString(), Constants.SALT);

                Log.d(TAG, "strOtp = " + pinView.getText().toString());
                Log.d(TAG, "encOtp = " + encOtp);

                validateOtp(encOtp);
            }
        });

        tvResend.setOnClickListener(resendClickListener);

        KeyboardUtils.setupUI(this, findViewById(R.id.flOTPInputParent));
    }

    private void backSpace(int index) {
        Log.d(TAG, "backSpace:" + index);

        arrEt[index].setText("");
    }

    private void showCounter() {
        Log.d(TAG, "showCounter");

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvResend.setText("00:" + count);
//                        btnSend.setEnabled(false);
                        count--;

                        if (count == 0) {
                            tvResend.setText(getResources().getString(R.string.label_resend));
                            tvResend.setOnClickListener(resendClickListener);
                            timer.cancel();
                            count = 60;
                        }
                    }
                });
            }
        }, 1000, 1000);
    }

    View.OnClickListener resendClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (resendClickCount <= 3) {
                requestResend();
                tvResend.setOnClickListener(null);
                tvLabelResend.setText(getResources().getString(R.string.label_didn_get_otp));
                showCounter();

                resendClickCount += 1;
            } else {
                Toast.makeText(getApplicationContext(),
                        "Anda sudah mengklik Resend 3x, silahkan hubungi admin", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public void messageReceived(String message) {
        Log.d("Otp input", "messageReceived:  " + message);

        String strArr[] = message.split(" is ");

        if (strArr.length == 2) {
            Log.d("Otp input", "otp nya " + strArr[1]);
            String encOtp = Aes.encrypt(strArr[1], Constants.SALT);
            validateOtp(encOtp);
        }
    }

    private String getInputOtp() {
        String strOtp = "";

        for (String str : otp) {
            strOtp = strOtp + str;
        }

        return strOtp;
    }

    private void validateOtp(String encPin) {
        try {
            encPin = URLEncoder.encode(encPin.trim(), "UTF-8");
            Log.d(TAG, "encoded OTP=" + encPin);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        restApi.validateOtp(encPin).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loading.hide();

                if (response.isSuccessful()) {
                    Log.d(TAG, "success response = " + response.body().toString());

                    PrefUtils.Build(OTPInputActivity.this).getPref()
                            .edit()
                            .putInt(Constants.KEY_AUTH_STEP, 4)
                            .apply();

                    Intent intent = new Intent(OTPInputActivity.this, PinInputActivity.class);
                    if (isResetPIN) {
                        intent = new Intent(OTPInputActivity.this, CreatePinActivity.class);
                    }
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        String strJson = response.errorBody().string();
                        Log.d(TAG, "strOtp = " + strJson);

                        JsonObject jObjError = new JsonParser().parse(strJson).getAsJsonObject();
                        String errMsg = jObjError.get("message").getAsString();

                        Toast.makeText(context, errMsg, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "onfailure = " + t.getMessage());
                loading.hide();
            }
        });
    }

    private void requestResend() {
        restApi.sendOtp().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.d("Otp", response.body().toString());

                    count = 60;
                } else {
                    ResponseBody resJson = response.errorBody();
                    try {
                        String strJson = resJson.string();
                        Log.d("Otp", "strJson = " + strJson);

                        JsonObject jo = new Gson().fromJson(strJson, JsonObject.class);

                        if (jo.has("message")) {
                            String msg = jo.get("message").getAsString();
                            showAlert(msg);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("Otp", "onFailure");
            }
        });
    }

    private void showAlert(String strError) {
        new AlertDialog.Builder(this)
                .setTitle("Failed to get data")
                .setMessage("Can not get data due to:" + strError)
                .setNeutralButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loading.dismiss();
    }

    View.OnKeyListener onkeyListener = new View.OnKeyListener() {

        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            Log.d(TAG, "View.OnKeyListener");

            if(keyCode == KeyEvent.KEYCODE_DEL) {
                if (otp[index].length() == 0) {
                    backSpace(index-1);
                }
            }

            return false;
        }
    };
}
