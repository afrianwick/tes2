package com.pertamina.portal.iam.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pertamina.portal.core.activity.BackableNoActionbarActivity;
import com.pertamina.portal.core.encryption.Aes;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.iam.R;

import java.io.IOException;

import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InboxActivity extends BackableNoActionbarActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        super.onCreateBackable(this, R.id.ivBack);

        View vPayslip = findViewById(R.id.llPayslip);
        View vSuratKet = findViewById(R.id.llSuratKet);

        restApi = RestClient.getRetrofitAuthenticated(this, 2000);

        loading = new SpotsDialog.Builder().setContext(this).build();

        vPayslip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MyPersonalActivity.this, InputPinPayslipActivity.class);
//                startActivity(intent);

                final EditText edpind;

                final AlertDialog dialog;

                AlertDialog.Builder builder = new AlertDialog.Builder(InboxActivity.this);
                LayoutInflater inflater = (LayoutInflater) InboxActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View  dialogview = inflater.inflate(R.layout.row_pin_payslip, null);
                edpind = dialogview.findViewById(R.id.editpinfield);
                builder.setView(dialogview);
                builder.setTitle("Input pin");
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog = builder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String pin = edpind.getText().toString();
                        checkValidatePin(pin,dialog);

                        dialog.dismiss();
                    }
                });

            }
        });

        vSuratKet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InboxActivity.this, SuratKetActivity.class);
                startActivity(intent);
            }
        });
    }

    private PortalApiInterface restApi;
    private AlertDialog loading;

    public void checkValidatePin(String pinpassword, final  AlertDialog alertDialog)
    {

        String encryptedPin = Aes.encrypt(pinpassword, Constants.SALT);
        loading.show();
        restApi.resumeValidatePin(encryptedPin)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.hide();
                        alertDialog.dismiss();
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
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.hide();
                        alertDialog.dismiss();
                        System.out.println("input pin salah");
                        Log.d("PinInput", "onFailure = " + t.getMessage());
                    }
                });
    }
}
