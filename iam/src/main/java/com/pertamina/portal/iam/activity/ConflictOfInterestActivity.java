package com.pertamina.portal.iam.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.fragments.CodeOfConduct.FragmentSuratPernyataan;
import com.pertamina.portal.iam.fragments.CodeOfConduct.FragmentUjiPemahaman;
import com.pertamina.portal.iam.fragments.ConflictOfInterest.FragmentForm;
import com.pertamina.portal.iam.fragments.ConflictOfInterest.FragmentPage1;
import com.pertamina.portal.iam.fragments.ConflictOfInterest.FragmentPage2;
import com.pertamina.portal.iam.utils.ErrorMessage;
import com.pertamina.portal.iam.utils.XMLParserUtils.XMLParserInterface;
import com.pertamina.portal.iam.utils.XMLParserUtils.XMLParserUtils;

import org.w3c.dom.NodeList;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConflictOfInterestActivity extends AppCompatActivity {

    private AppCompatImageButton backIB;
    private FrameLayout fragmentContainerRL;
    private Button page1Button, page2Button, formButton;
    private FragmentPage2 fragmentPage1;
    private FragmentPage1 fragmentPage2;
    private FragmentForm fragmentForm;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private LinearLayout coiParentLL;
    private PortalApiInterface restApi;
    private AlertDialog alertDialog, alertDialogDismiss, loading;
    public static boolean isCOI = false, isSubmitted = false;
    private boolean isUbahKepentingan = false;
    public static Locale id = new Locale("in", "ID");
    public static String lokasiResponse, statusResponse, isiResponse, tanggalResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conflict_of_interest);

        restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);

        buildAlert();

        loading = new SpotsDialog.Builder().setContext(this).build();

        backIB = findViewById(R.id.ic_back);
        fragmentContainerRL = findViewById(R.id.fragment_holder);
        page1Button = findViewById(R.id.btn_page1);
        page2Button = findViewById(R.id.btn_page2);
        formButton = findViewById(R.id.btn_form);
        coiParentLL = findViewById(R.id.coiParentLL);

        page2ButtonActivated(false);
        submitButtonActivated(false);

        getCOI();

        backIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            FragmentPage2.checkBox1.setChecked(false);
            FragmentPage2.checkBox2.setChecked(false);
            FragmentPage2.checkBox3.setChecked(false);
            FragmentPage2.checkBox4.setChecked(false);
            FragmentPage2.checkBox5.setChecked(false);
            FragmentPage2.checkBox6.setChecked(false);
            FragmentPage2.checkBox7.setChecked(false);
            FragmentPage2.checkBox8.setChecked(false);
            FragmentPage2.checkBox9.setChecked(false);

            FragmentPage1.lokasiET.setText("");
            FragmentPage1.mempunyaiET.setText("");
            FragmentPage1.mempunyaiRB.setChecked(false);
            FragmentPage1.tidakMempunyaiRB.setChecked(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fragmentSetup() {
        fragmentPage1 = new FragmentPage2();
        fragmentPage2 = new FragmentPage1();
        fragmentForm = new FragmentForm();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_holder, fragmentPage1);
        fragmentTransaction.commit();

        backIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        page1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isUbahKepentingan = false;
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_holder, fragmentPage1);
                fragmentTransaction.commit();
            }
        });

        page2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < FragmentPage2.isChecked.length; i++) {
                    if (!FragmentPage2.isChecked[i]) {
                        ErrorMessage.errorMessage(ConflictOfInterestActivity.this, coiParentLL, "Mohon checklis semua data di form 1.");
                        return;
                    }
                }
                isUbahKepentingan = false;
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_holder, fragmentPage2);
                fragmentTransaction.commit();
            }
        });

        formButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_holder, fragmentForm);
//                fragmentTransaction.commit();
                for (int i = 0; i < FragmentPage2.isChecked.length; i++) {
                    if (!FragmentPage2.isChecked[i]) {
                        ErrorMessage.errorMessage(ConflictOfInterestActivity.this, coiParentLL, "Mohon checklis semua data di form 1.");
                        return;
                    }
                }

                try {
                    if ((!FragmentPage1.mempunyaiRB.isChecked() && !FragmentPage1.tidakMempunyaiRB.isChecked()) ||
                            (FragmentPage1.mempunyaiRB.isChecked() && FragmentPage1.mempunyaiET.getText().toString().isEmpty()) ||
                            FragmentPage1.lokasiET.getText().toString().isEmpty()) {
                        ErrorMessage.errorMessage(ConflictOfInterestActivity.this, coiParentLL, "Mohon lengkapi data di form 2.");
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ErrorMessage.errorMessage(ConflictOfInterestActivity.this, coiParentLL, "Mohon lengkapi data di form 2.");
                    return;
                }

                getSubmitCOI();
            }
        });
    }

    private void getCOI() {
        loading.show();
        restApi.getCOI().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String strResponse = response.body().string();
                        System.out.println("getCompanyCodes success");
                        XMLParserUtils.parseXml(strResponse, new XMLParserInterface() {
                            @Override
                            public void onSuccess(String result) {
                                Gson gson = new Gson();
                                JsonObject jo = gson.fromJson(result, JsonObject.class);
                                JsonArray jArr = jo.getAsJsonArray("tb_Coi");

                                if (jArr.size() != 0) {
                                    lokasiResponse = jArr.get(0).getAsJsonObject().get("tempat2").getAsString();
                                    statusResponse = jArr.get(0).getAsJsonObject().get("status").getAsString();
                                    isiResponse = jArr.get(0).getAsJsonObject().get("isi1").getAsString();
                                    tanggalResponse = jArr.get(0).getAsJsonObject().get("tanggal2").getAsString();
                                    isCOI = true;
                                    if (jArr.get(0).getAsJsonObject().get("Approve2").getAsString() != null) {
                                        isSubmitted = true;
                                        page2ButtonActivated(true);
                                        submitButtonActivated(false);
                                    }

                                    if (statusResponse.equalsIgnoreCase("Ada")) {
                                        formButton.setVisibility(View.GONE);
                                    }
                                }
                                fragmentSetup();
                            }

                            @Override
                            public void onFailure(NodeList nodeListError) {
                                onGetFailure(nodeListError);
                            }

                            @Override
                            public void onSuccessReturnMessage(NodeList nodeListError) {

                            }
                        });
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
        String message = "Could not get data due to:" + strError;

        if (strError.contains("401")) {
            message = "Could not get data. It might be you are Loged in from other device or your session was exiperd.";
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Login again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        startActivity(new Intent(ConflictOfInterestActivity.this,
                                Class.forName("com.pertamina.portal.activity.LoginActivity")));
                        finish();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        if (alertDialog == null) {
            this.alertDialog = new AlertDialog.Builder(this)
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
    }

    private void buildAlert() {
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
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        finish();
                    }
                })
                .create();
    }

    private void onGetFailure(NodeList nodeListError) {
        for (int i = 0; i < nodeListError.getLength(); i++) {
            String strError = nodeListError.item(i).getTextContent();
            String message = "Could not get data due to:" + strError;

            if (strError.contains("401")) {
                message = "Could not get data. It might be you are Loged in from other device or your session was exiperd.";
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Login again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            startActivity(new Intent(ConflictOfInterestActivity.this,
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

    private void getSubmitCOI() {
        if (isSubmitted && !isUbahKepentingan) {
            return;
        }

        loading.show();
        Calendar calendar = Calendar.getInstance();
        String tanggal =  new SimpleDateFormat("dd MMMM yyyy", id).format(calendar.getTime());
        String status = FragmentPage1.mempunyaiRB.isChecked() ? "Ada" : "Tidak Ada";

        RequestBody lokasiBody = RequestBody.create(MediaType.parse("text/plain"), isUbahKepentingan ? FragmentForm.lokasiET.getText().toString() : FragmentPage1.lokasiET.getText().toString());
        RequestBody statusBody = RequestBody.create(MediaType.parse("text/plain"), isUbahKepentingan ? "Ada" : status);
        RequestBody mempunaiBody = RequestBody.create(MediaType.parse("text/plain"), isUbahKepentingan ? FragmentForm.mempunyaiET.getText().toString() : FragmentPage1.mempunyaiET.getText().toString());
        RequestBody tanggalBody = RequestBody.create(MediaType.parse("text/plain"), tanggal);

        restApi.getSubmitCOI(lokasiBody, statusBody, mempunaiBody, tanggalBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String strResponse = response.body().string();
                        System.out.println("getCompanyCodes success");
                        XMLParserUtils.parseXml(strResponse, new XMLParserInterface() {
                            @Override
                            public void onSuccess(String result) {
                                alertDialogDismiss.setMessage(result);
                                alertDialogDismiss.show();
                            }

                            @Override
                            public void onFailure(NodeList nodeListError) {
                                onGetFailure(nodeListError);
                            }

                            @Override
                            public void onSuccessReturnMessage(NodeList nodeListError) {
                                alertDialogDismiss.setMessage(nodeListError.item(0).getTextContent());
                                alertDialogDismiss.show();
                            }
                        });
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

    public void ubahKepentingan() {
        isUbahKepentingan = true;
        isSubmitted = false;
        submitButtonActivated(false);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_holder, fragmentForm);
        fragmentTransaction.commit();
    }

    public void page2ButtonActivated(boolean isActivated) {
        if (isActivated) {
            page2Button.setBackground(getResources().getDrawable(R.drawable.button_bg_rounded_corners_blue_oval));
        } else {
            page2Button.setBackground(getResources().getDrawable(R.drawable.button_bg_rounded_corners_grey_oval));
        }
    }

    public void submitButtonActivated(boolean isActivated) {
        if (isActivated) {
            formButton.setBackground(getResources().getDrawable(R.drawable.button_bg_rounded_corners_blue_oval));
        } else {
            formButton.setBackground(getResources().getDrawable(R.drawable.button_bg_rounded_corners_grey_oval));
        }
    }

}
