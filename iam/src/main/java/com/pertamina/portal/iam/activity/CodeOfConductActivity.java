package com.pertamina.portal.iam.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pertamina.portal.core.activity.BackableNoActionbarActivity;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CodeOfConductActivity extends AppCompatActivity {

    private AppCompatImageButton backIB;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FragmentUjiPemahaman fragmentUjiPemahaman;
    private FragmentSuratPernyataan fragmentSuratPernyataan;
    private Button prevButton, nextButton, submitButton;
    private AlertDialog alertDialog, alertDialogDismiss, loading;
    private PortalApiInterface restApi;
    private LinearLayout cocParentLL;
    public static String lokasiReponse, tanggalResponse;
    public static boolean isCOC = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_of_conduct);

        buildAlert();
        loading = new SpotsDialog.Builder().setContext(this).build();

        restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);

        backIB = findViewById(R.id.backIB);
        prevButton = findViewById(R.id.btn_prev);
        nextButton = findViewById(R.id.btn_next_or_submit);
        cocParentLL = findViewById(R.id.cocParentLL);
        submitButton = findViewById(R.id.coc_submit);

        backIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        page2ActivatedButton(false);
        submitActivatedButton(false);

        getCOC();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            FragmentSuratPernyataan.lokasiET.setText("");
            FragmentSuratPernyataan.isPedomanClicked = false;
            FragmentUjiPemahaman.selectedAnswer = new String[3];
        } catch (Exception  e) {
            e.printStackTrace();
        }
    }

    private void getCOC() {
        loading.show();
        restApi.getCOC().enqueue(new Callback<ResponseBody>() {
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
                                JsonArray jArr = jo.getAsJsonArray("tb_coc");
                                JsonArray jArr2 = jo.getAsJsonArray("TblT_COC_HasilJawaban");

                                if (jArr.size() != 0) {
//                                    tempat":"Jakarta","lampiran1":"08/01/2020"
                                    isCOC = true;
                                    lokasiReponse = jArr.get(0).getAsJsonObject().get("tempat").getAsString();
                                    tanggalResponse = jArr.get(0).getAsJsonObject().get("lampiran1").getAsString();

                                    submitButton.setVisibility(View.GONE);
//                                    statusResponse = jArr.get(0).getAsJsonObject().get("status").getAsString();
//                                    isiResponse = jArr.get(0).getAsJsonObject().get("isi1").getAsString();

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
                            startActivity(new Intent(CodeOfConductActivity.this,
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
                        startActivity(new Intent(CodeOfConductActivity.this,
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

    private void fragmentSetup() {
        fragmentSuratPernyataan = new FragmentSuratPernyataan();
        fragmentUjiPemahaman = new FragmentUjiPemahaman();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_holder, fragmentSuratPernyataan);
        fragmentTransaction.commit();

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_holder, fragmentSuratPernyataan);
                fragmentTransaction.commit();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isCOC) {
                    if (!FragmentSuratPernyataan.isPedomanClicked) {
                        ErrorMessage.errorMessage(CodeOfConductActivity.this, cocParentLL, "Mohon klik pedoman prilaku di page 1!");
                        return;
                    }

                    try {

                        if (FragmentSuratPernyataan.lokasiET.getText().toString().isEmpty()) {
                            ErrorMessage.errorMessage(CodeOfConductActivity.this, cocParentLL, "Mohon isi lokasi di page 1!");
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ErrorMessage.errorMessage(CodeOfConductActivity.this, cocParentLL, "Mohon isi lokasi di page 1!");
                        return;
                    }
                }

                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_holder, fragmentUjiPemahaman);
                fragmentTransaction.commit();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!FragmentSuratPernyataan.isPedomanClicked) {
                    ErrorMessage.errorMessage(CodeOfConductActivity.this, cocParentLL, "Mohon klik pedoman prilaku di page 1!");
                    return;
                }

                try {
                    if (FragmentSuratPernyataan.lokasiET.getText().toString().isEmpty()) {
                        ErrorMessage.errorMessage(CodeOfConductActivity.this, cocParentLL, "Mohon isi lokasi di page 1!");
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ErrorMessage.errorMessage(CodeOfConductActivity.this, cocParentLL, "Mohon isi lokasi di page 1!");
                    return;
                }

                try {
                    for (int i = 0; i < FragmentUjiPemahaman.selectedAnswer.length; i++) {
                        if (FragmentUjiPemahaman.selectedAnswer[i] == "-1") {
                            ErrorMessage.errorMessage(CodeOfConductActivity.this, cocParentLL, "Mohon lengkapi jawaban anda di page 2");
                            return;
                        }
                    }

                    Log.d("fragmentuijpemahaman", FragmentUjiPemahaman.selectedAnswer[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                    ErrorMessage.errorMessage(CodeOfConductActivity.this, cocParentLL, "Mohon lengkapi jawaban anda di page 2");
                    return;
                }
                getSubmitCOC();
            }
        });
    }

    private void getSubmitCOC() {
        loading.show();

        RequestBody lokasi = RequestBody.create(MediaType.parse("text/plain"), FragmentSuratPernyataan.lokasiET.getText().toString());
        RequestBody idSoal1 = RequestBody.create(MediaType.parse("text/plain"), "1");
        RequestBody idSoal2 = RequestBody.create(MediaType.parse("text/plain"), "2");
        RequestBody idSoal3 = RequestBody.create(MediaType.parse("text/plain"), "3");
        RequestBody idJawaban1 = RequestBody.create(MediaType.parse("text/plain"), FragmentUjiPemahaman.selectedAnswer[0]);
        RequestBody idJawaban2 = RequestBody.create(MediaType.parse("text/plain"), FragmentUjiPemahaman.selectedAnswer[1]);
        RequestBody idJawaban3 = RequestBody.create(MediaType.parse("text/plain"), FragmentUjiPemahaman.selectedAnswer[2]);

        restApi.getSubmitCOC(lokasi,
                idSoal1, idJawaban1,
                idSoal2, idJawaban2,
                idSoal3, idJawaban3).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String strResponse = response.body().string();
                        System.out.println("getCompanyCodes success");
                        XMLParserUtils.parseXml(strResponse, new XMLParserInterface() {
                            @Override
                            public void onSuccess(String result) {
                                if (result.toLowerCase().contains("salah")) {
                                    alertDialog.setMessage(result);
                                    alertDialog.show();
                                } else {
                                    alertDialogDismiss.setMessage(result);
                                    alertDialogDismiss.show();
                                }
                            }

                            @Override
                            public void onFailure(NodeList nodeListError) {
                                onGetFailure(nodeListError);
                            }

                            @Override
                            public void onSuccessReturnMessage(NodeList nodeListError) {
                                if (nodeListError.item(0).getTextContent().toLowerCase().contains("salah")) {
                                    alertDialog.setMessage(nodeListError.item(0).getTextContent());
                                    alertDialog.show();
                                } else {
                                    alertDialogDismiss.setMessage(nodeListError.item(0).getTextContent());
                                    alertDialogDismiss.show();
                                }
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

    public void page2ActivatedButton(boolean isActivated) {
        if (isActivated) {
            nextButton.setBackground(getResources().getDrawable(R.drawable.button_bg_rounded_corners_blue_oval));
        } else {
            nextButton.setBackground(getResources().getDrawable(R.drawable.button_bg_rounded_corners_grey_oval));
        }
    }

    public void submitActivatedButton(boolean isActivated) {
        if (isActivated) {
            submitButton.setBackground(getResources().getDrawable(R.drawable.button_bg_rounded_corners_blue_oval));
        } else {
            submitButton.setBackground(getResources().getDrawable(R.drawable.button_bg_rounded_corners_grey_oval));
        }
    }
}
