package com.pertamina.portal.core.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pertamina.portal.core.interfaces.OnSuccessListener;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.PrefUtils;
import com.pertamina.portal.core.utils.RestClient;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BackableNoActionbarActivity extends AppCompatActivity {

    private static final String TAG = "BackableNoAct";
    private AlertDialog mLoading;
    private AlertDialog mAlertDialog;
    private PortalApiInterface restApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restApi = RestClient.getRetrofitAuthenticated(this, 2000);
        mLoading = new SpotsDialog.Builder().setContext(this).build();
        buildAlert();
    }

    public void onCreateBackable(Activity activity, int backRes) {
        Log.d(TAG, "back from actionbar");
        View vBack = activity.findViewById(backRes);
        vBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
                buildAlert();

                for (int i = 0; i < nodeListError.getLength(); i++) {
                    String strError = nodeListError.item(i).getTextContent();

                    showError(strError);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public AlertDialog getLoading() {
        return mLoading;
    }

    public AlertDialog getmAlertDialog() {
        return mAlertDialog;
    }

    public void buildAlert() {
        this.mAlertDialog = new AlertDialog.Builder(this)
                .setNeutralButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                })
                .create();
    }

    public void showError(String strError) {
        String message = "Could not get data due to:" + strError;

        if (strError.contains("401")) {
            message = "Could not get data. It might be you are Loged in from other device or your session was exiperd.";
            mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Login again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        startActivity(new Intent(BackableNoActionbarActivity.this,
                                Class.forName("com.pertamina.portal.activity.LoginActivity")));
                        finish();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (strError.contains("404")) {
            message = "Could not get data. File Not Found.";
        }

        if (mAlertDialog == null) {
            this.mAlertDialog = new AlertDialog.Builder(this)
                    .setNeutralButton("close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();
                        }
                    })
                    .create();
        }

        mAlertDialog.setMessage(message);

        if (!mAlertDialog.isShowing()) {
            mAlertDialog.show();
        }
    }

    public void refreshToken()
    {

        restApi.refreshToken("refresh_token",Constants.KEY_REFRESH_TOKEN)
                .enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                mLoading.hide();

                if (response.isSuccessful()) {


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
                mLoading.hide();

                Log.d("PinInput", "onFailure = " + t.getMessage());
            }
        });

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
 }
