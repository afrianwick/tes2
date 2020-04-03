package com.pertamina.portal.iam.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pertamina.portal.core.activity.BackableNoActionbarActivity;
import com.pertamina.portal.core.interfaces.OnSuccessListener;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.PrefUtils;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.adapters.FamilyRVAdapter;
import com.pertamina.portal.iam.adapters.SimpleDropDownAdapter;
import com.pertamina.portal.iam.models.FamilyModel;
import com.pertamina.portal.iam.utils.KeyboardUtils;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InputGratifikasiActivity extends BackableNoActionbarActivity {

    private AlertDialog loading;
    private AlertDialog alertDialog, alertDialogDismiss;

    private static final String TAG = "inputGratifikasi";

    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

    private String[] inputPemberian, inputPermintaan, inputPenerimaan;
    private List<String> kodePenerimaanList, currencyList, peristiwaPenerimaanList;
    private String KODE_PENERIMAAN = "tbM_KodePenerimaan", CURRENCY = "tbM_Currency", PERISTIWA_PENERIMAAN = "tbM_PeristiwaPenerimaan";

    private PortalApiInterface restApi;

    private TextInputEditText tglPenerimaan;
    private TextInputEditText jenisPenerimaan;
    private TextInputEditText nilaiPenerimaan;
    private TextInputEditText jumlahPenerimaan;
    private TextInputEditText penerimaPenerimaan;
    private TextInputEditText keteranganPenerimaan;
    private String tglsubmitPenerimaan;
    private TextInputEditText peristiwaPenerimaan;
    private TextInputEditText currencyPenerimaan;

    private TextInputEditText tglPemberian;
    private TextInputEditText jenisPemberian;
    private TextInputEditText nilaiPemberian;
    private TextInputEditText jumlahPemberian;
    private TextInputEditText penerimaPemberian;
    private TextInputEditText keteranganPemberian;
    private String tglsubmitPemberian;
    private TextInputEditText peristiwaPemberian;
    private TextInputEditText currencyPemberian;

    private TextInputEditText tglPermintaan;
    private TextInputEditText jenisPermintaan;
    private TextInputEditText nilaiPermintaan;
    private TextInputEditText jumlahPermintaan;
    private TextInputEditText penerimaPermintaan;
    private TextInputEditText keteranganPermintaan;
    private String tglsubmitPermintaan;
    private TextInputEditText peristiwaPermintaan;
    private TextInputEditText currencyPermintaan;

    private TextView penerimaanLabelTV, pemberianLabelTV, permintaanLabelTV;
    private TextView penerimaanDateTV, pemberianDateTV, permintaanDateTV;
    private Spinner jenisPenerimaanS, jenisPermintaanS, jenisPemberianS;
    private Spinner jenisPenerimaanCurrencyS, jenisPermintaanCurrencyS, jenisPemberianCurrencyS;
    private Spinner jenisPenerimaanPeristiwaS, jenisPermintaanPeristiwaS, jenisPemberianPeristiwaS;

    private Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_gratifikasi);
        super.onCreateBackable(this, R.id.ivBack);

        ButterKnife.bind(this);

        restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);

        permintaanLabelTV = findViewById(R.id.inputGratifikasiPermintaanLabelTV);
        permintaanDateTV = findViewById(R.id.inputGratifikasiPermintaanDateTV);
        pemberianLabelTV = findViewById(R.id.inputGratifikasiPemberianLabelTV);
        pemberianDateTV = findViewById(R.id.inputGratifikasiPemberianDateTV);
        penerimaanLabelTV = findViewById(R.id.inputGratifikasiPenerimaanLabelTV);
        penerimaanDateTV = findViewById(R.id.inputGratifikasiPenerimaanDateTV);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        GratifikasiAdapter sectionsPagerAdapter = new GratifikasiAdapter(this, getSupportFragmentManager());
//        ViewPager viewPager = findViewById(R.id.view_pager);
//        viewPager.setAdapter(sectionsPagerAdapter);
//        TabLayout tabs = findViewById(R.id.tabs);
//        tabs.setupWithViewPager(viewPager);

        loading = new SpotsDialog.Builder().setContext(this).build();

        final View penerimaanFormLL = findViewById(R.id.llInputGratifikasiForm1);
        final View pemberianFormLL = findViewById(R.id.llInputGratifikasiForm2);
        final View permintaanFormLL = findViewById(R.id.llInputGratifikasiForm3);

        RadioGroup penerimaanRG = findViewById(R.id.rgInputGratifikasiPenerimaan);
        RadioGroup pemberianRG = findViewById(R.id.rgInputGratifikasiPemberian);
        RadioGroup permintaanRG = findViewById(R.id.rgInputGratifikasiPermintaan);

        jenisPemberianS = findViewById(R.id.spPemberianDropDownPemberian);
        jenisPermintaanS = findViewById(R.id.spPemberianDropDownPermintaan);
        jenisPemberianS = findViewById(R.id.spPemberianDropDownPemberian);

        tglPenerimaan = findViewById(R.id.tietTglPenerimaan);
        jenisPenerimaan = findViewById(R.id.tietJenisPenerimaan);
        nilaiPenerimaan = findViewById(R.id.tietNilaiPenerimaan);
        jumlahPenerimaan = findViewById(R.id.tietJumlahPenerimaan);
        penerimaPenerimaan = findViewById(R.id.tietPenerimaPenerimaan);
        keteranganPenerimaan = findViewById(R.id.tietKeteranganPenerimaan);
        tglsubmitPenerimaan = simpleDateFormat.format(calendar.getTime());
        peristiwaPenerimaan = findViewById(R.id.tietPeristiwaPenerimaan);
        currencyPenerimaan = findViewById(R.id.tietSelectCurrencyPenerimaan);
        jenisPenerimaanS = findViewById(R.id.spPemberianDropDownPenerimaan);
        jenisPenerimaanCurrencyS = findViewById(R.id.spPemberianDropDownPenerimaanCurrency);
        jenisPenerimaanPeristiwaS = findViewById(R.id.spPemberianDropDownPenerimaanPeristiwa);

        tglPenerimaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarSetup(tglPenerimaan);
            }
        });

        jenisPenerimaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jenisPenerimaanS.performClick();
            }
        });

        currencyPenerimaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jenisPenerimaanCurrencyS.performClick();
            }
        });

        peristiwaPenerimaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jenisPenerimaanPeristiwaS.performClick();
            }
        });

        tglPemberian = findViewById(R.id.tietTglPemberian);
        jenisPemberian = findViewById(R.id.tietJenisPemberian);
        nilaiPemberian = findViewById(R.id.tietNilaiPemberian);;
        jumlahPemberian = findViewById(R.id.tietJumlahPemberian);
        penerimaPemberian = findViewById(R.id.tietPenerimaPemberian);
        keteranganPemberian = findViewById(R.id.tietKeteranganPemberian);
        tglsubmitPemberian = simpleDateFormat.format(calendar.getTime());
        peristiwaPemberian = findViewById(R.id.tietPeristiwaPemberian);
        currencyPemberian = findViewById(R.id.tietSelectCurrencyPemberian);
        jenisPemberianS = findViewById(R.id.spPemberianDropDownPemberian);
        jenisPemberianCurrencyS = findViewById(R.id.spPemberianDropDownPemberianCurrency);
        jenisPemberianPeristiwaS = findViewById(R.id.spPemberianDropDownPemberianPeristiwa);

        tglPemberian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarSetup(tglPemberian);
            }
        });

        jenisPemberian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jenisPemberianS.performClick();
            }
        });

        currencyPemberian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jenisPemberianCurrencyS.performClick();
            }
        });

        peristiwaPemberian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jenisPemberianPeristiwaS.performClick();
            }
        });

        tglPermintaan = findViewById(R.id.tietTglPermintaan);
        jenisPermintaan = findViewById(R.id.tietJenisPermintaan);
        nilaiPermintaan = findViewById(R.id.tietNilaiPermintaan);
        jumlahPermintaan = findViewById(R.id.tietJumlahPermintaan);
        penerimaPermintaan = findViewById(R.id.tietPenerimaPermintaan);
        keteranganPermintaan = findViewById(R.id.tietKeteranganPermintaan);
        tglsubmitPermintaan = simpleDateFormat.format(calendar.getTime());
        peristiwaPermintaan = findViewById(R.id.tietPeristiwaPermintaan);
        currencyPermintaan = findViewById(R.id.tietSelectCurrencyPermintaanCurrency);
        jenisPermintaanS = findViewById(R.id.spPemberianDropDownPermintaan);
        jenisPermintaanCurrencyS = findViewById(R.id.spPemberianDropDownPermintaanCurrency);
        jenisPermintaanPeristiwaS = findViewById(R.id.spPemberianDropDownPermintaanPeristiwa);

        tglPermintaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarSetup(tglPermintaan);
            }
        });

        jenisPermintaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jenisPermintaanS.performClick();
            }
        });

        currencyPermintaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jenisPermintaanCurrencyS.performClick();
            }
        });

        peristiwaPermintaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jenisPermintaanPeristiwaS.performClick();
            }
        });

        kodePenerimaanList = new ArrayList<>();
        currencyList = new ArrayList<>();
        peristiwaPenerimaanList = new ArrayList<>();

        Button simpanButton = findViewById(R.id.buttonSimpan);

        alertDialog = new AlertDialog.Builder(this)
                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();

        alertDialogDismiss = new AlertDialog.Builder(this)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).create();

        penerimaanRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                penerimaanFormLL.setVisibility(radioGroup.getCheckedRadioButtonId() == R.id.radioButtonPenerimaan1 ? View.GONE : View.VISIBLE);
            }
        });

        pemberianRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                pemberianFormLL.setVisibility(radioGroup.getCheckedRadioButtonId() == R.id.radioButtonPemberian1 ? View.GONE : View.VISIBLE);
            }
        });

        permintaanRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                permintaanFormLL.setVisibility(radioGroup.getCheckedRadioButtonId() == R.id.radioButtonPermintaan1 ? View.GONE : View.VISIBLE);
            }
        });

        simpanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                inputPemberianSetup();
                inputPenerimaanSetup();
                inputPermintaanSetup();

                if (!penerimaanFormLL.isShown()) {
                    inputPenerimaan = new String[8];
                    Arrays.fill(inputPenerimaan, "");
                } else if (penerimaanFormLL.isShown()) {
                    for (int i = 0; i < inputPenerimaan.length; i++) {
                        if (inputPenerimaan[i].trim().equals("") || inputPenerimaan[i].isEmpty()) {
                            alertDialog.setMessage("Lengkapi Input Penerimaan!");
                            alertDialog.show();
                            return;
                        }
                    }
                }

                if (!pemberianFormLL.isShown()) {
                    inputPemberian = new String[8];
                    Arrays.fill(inputPemberian, "");
                } else if (pemberianFormLL.isShown()) {
                    for (int i = 0; i < inputPemberian.length; i++) {
                        if (inputPemberian[i].trim().equals("") || inputPemberian[i].isEmpty()) {
                            alertDialog.setMessage("Lengkapi Input Pemberian!");
                            alertDialog.show();
                            return;
                        }
                    }
                }

                if (!permintaanFormLL.isShown()) {
                    inputPermintaan = new String[8];
                    Arrays.fill(inputPermintaan, "");
                } else if (permintaanFormLL.isShown()) {
                    for (int i = 0; i < inputPermintaan.length; i++) {
                        if (inputPermintaan[i].trim().equals("") || inputPermintaan[i].isEmpty()) {
                            alertDialog.setMessage("Lengkapi Input Permintaan!");
                            alertDialog.show();
                            return;
                        }
                    }
                }

                submitInput();
            }
        });

        KeyboardUtils.setupUI(InputGratifikasiActivity.this, findViewById(R.id.clInputGratifikasiParent));

        getLastSubmitGratifikasi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loading.show();
        restApi.getRefDataGratification().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String body = response.body().string();
                        Log.d(TAG + " S", "response:" + body);

                        parseXml(body);

                        SimpleDropDownAdapter kodePenerimaanAdapter = new SimpleDropDownAdapter(InputGratifikasiActivity.this,
                                android.R.layout.simple_spinner_item, kodePenerimaanList);

                        SimpleDropDownAdapter currencyAdapter = new SimpleDropDownAdapter(InputGratifikasiActivity.this,
                                android.R.layout.simple_spinner_item, currencyList);

                        SimpleDropDownAdapter peristiwaAdapter = new SimpleDropDownAdapter(InputGratifikasiActivity.this,
                                android.R.layout.simple_spinner_item, peristiwaPenerimaanList);

                        jenisPenerimaanS.setAdapter(kodePenerimaanAdapter);
                        jenisPemberianS.setAdapter(kodePenerimaanAdapter);
                        jenisPermintaanS.setAdapter(kodePenerimaanAdapter);

                        jenisPenerimaanCurrencyS.setAdapter(currencyAdapter);
                        jenisPemberianCurrencyS.setAdapter(currencyAdapter);
                        jenisPermintaanCurrencyS.setAdapter(currencyAdapter);

                        jenisPenerimaanPeristiwaS.setAdapter(peristiwaAdapter);
                        jenisPemberianPeristiwaS.setAdapter(peristiwaAdapter);
                        jenisPermintaanPeristiwaS.setAdapter(peristiwaAdapter);

                        jenisPenerimaanS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                jenisPenerimaan.setText(kodePenerimaanList.get(i));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        jenisPemberianS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                jenisPemberian.setText(kodePenerimaanList.get(i));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        jenisPermintaanS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                jenisPermintaan.setText(kodePenerimaanList.get(i));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        jenisPenerimaanCurrencyS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                currencyPenerimaan.setText(currencyList.get(i));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        jenisPemberianCurrencyS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                currencyPemberian.setText(currencyList.get(i));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        jenisPermintaanCurrencyS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                currencyPermintaan.setText(currencyList.get(i));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        jenisPenerimaanPeristiwaS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                peristiwaPenerimaan.setText(peristiwaPenerimaanList.get(i));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        jenisPemberianPeristiwaS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                peristiwaPemberian.setText(peristiwaPenerimaanList.get(i));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        jenisPermintaanPeristiwaS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                peristiwaPermintaan.setText(peristiwaPenerimaanList.get(i));
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        String strJson = response.errorBody().string();
                        Log.d(TAG+ " F", "response = " + strJson);

                        if (alertDialog != null) {
                            alertDialog.setMessage(strJson);
                            alertDialog.show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                loading.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading.dismiss();
            }
        });
    }

    private void getLastSubmitGratifikasi() {
        loading.show();
        Calendar calendar = Calendar.getInstance();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        restApi.getLatestSubmittedGratifikasi()
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d("Success personal data", "getAllWorklist, true _ " + response.raw().toString());
                                parseXml(strResponse, successListener);
//                                parseXml(strResponse);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            handleError(response);
                            Log.d("Error get history", "getAllWorklist, false _ " + response.raw().toString());
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

    OnSuccessListener successListener = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            parseJson(strJson);
        }
    };

    OnSuccessListener successListenerSubmit = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            parseJsonSubmit(strJson);
        }
    };

    private void parseJsonSubmit(String strJson) {
        String[] strings = strJson.split(";");
        String message = "";
        for (int i = 0; i < strings.length; i ++) {
            message += (i+1) + ". " + strings[i] + "\n\n";
        }
        System.out.println("messagenya adalah " + message);
        alertDialogDismiss.setTitle("Input Gratifikasi");
        alertDialogDismiss.setMessage(message);
        alertDialogDismiss.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                finish();
            }
        });
        alertDialogDismiss.show();
    }

    private void parseJson(String strJson) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);

        JsonArray jsonArray = jsonObject.getAsJsonArray("Table");

        if (jsonArray.size() > 0) {
            JsonObject jo = jsonArray.get(0).getAsJsonObject();
            penerimaanLabelTV.setText(jo.get("KetPenerimaan").getAsString().substring(0, jo.get("KetPenerimaan").getAsString().length() - 11).split(": ")[1]);
            penerimaanDateTV.setText(jo.get("KetPenerimaan").getAsString().substring(jo.get("KetPenerimaan").getAsString().length() - 11));
            pemberianLabelTV.setText(jo.get("KetPemberian").getAsString().substring(0, jo.get("KetPemberian").getAsString().length() - 11).split(": ")[1]);
            pemberianDateTV.setText(jo.get("KetPemberian").getAsString().substring(jo.get("KetPemberian").getAsString().length() - 11));
            permintaanLabelTV.setText(jo.get("KetPermintaan").getAsString().substring(0, jo.get("KetPermintaan").getAsString().length() - 11).split(": ")[1]);
            permintaanDateTV.setText(jo.get("KetPermintaan").getAsString().substring(jo.get("KetPermintaan").getAsString().length() - 11));

            Log.d("datanyaadafamily", "ada");
        } else {
//            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    private void submitInput() {
        loading.show();

        RequestBody[] requestBodies = new RequestBody[24];
        int i1 = 0;
        int i2 = 0;
        int i3 = 0;
        for (int i = 0; i < inputPemberian.length * 3; i++) {
            if (i < 8) {
                requestBodies[i] = RequestBody.create(MediaType.parse("text/plain"), inputPemberian[i1]);
                i1++;
            } else if (i < 16) {
                requestBodies[i] = RequestBody.create(MediaType.parse("text/plain"), inputPenerimaan[i2]);
                i1++;
            } else if (i < 24) {
                requestBodies[i] = RequestBody.create(MediaType.parse("text/plain"), inputPermintaan[i3]);
                i3++;
            }
        }

        restApi.submitInputGratification(requestBodies[0], requestBodies[1], requestBodies[2], requestBodies[3], requestBodies[4], requestBodies[5], requestBodies[6], requestBodies[7],
                requestBodies[8], requestBodies[9], requestBodies[10], requestBodies[11], requestBodies[12], requestBodies[13], requestBodies[14], requestBodies[15],
                requestBodies[16], requestBodies[17], requestBodies[18], requestBodies[19], requestBodies[20], requestBodies[21], requestBodies[22], requestBodies[23]).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String strResponse = response.body().string();
                        Log.d("Success personal data", "getAllWorklist, true _ " + response.raw().toString());
                        parseXmlSubmit(strResponse);
//                                parseXml(strResponse);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        String strJson = response.errorBody().string();
                        Log.d(TAG+ " F", "response = " + strJson);

                        String message = "Could not get data. It might be you are Loged in from other device or your session was exiperd.";
                        if (response.code() == 401) {
                            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Login again", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try {
                                        startActivity(new Intent(InputGratifikasiActivity.this,
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
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                loading.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading.dismiss();
            }
        });
    }

    private void inputPemberianSetup() {
        inputPemberian = new String[8];
        inputPemberian[0] = tglPemberian.getText().toString();
        inputPemberian[1] = jenisPemberian.getText().toString();
        inputPemberian[2] = nilaiPemberian.getText().toString() + currencyPemberian.getText().toString();
        inputPemberian[3] = jumlahPemberian.getText().toString();
        inputPemberian[4] = penerimaPemberian.getText().toString();
        inputPemberian[5] = keteranganPemberian.getText().toString();
        inputPemberian[6] = tglsubmitPemberian;
        inputPemberian[7] = peristiwaPemberian.getText().toString();
    }

    private void inputPenerimaanSetup() {
        inputPenerimaan = new String[8];
        inputPenerimaan[0] = tglPenerimaan.getText().toString();
        inputPenerimaan[1] = jenisPenerimaan.getText().toString();
        inputPenerimaan[2] = nilaiPenerimaan.getText().toString() + currencyPenerimaan.getText().toString();
        inputPenerimaan[3] = jumlahPenerimaan.getText().toString();
        inputPenerimaan[4] = penerimaPenerimaan.getText().toString();
        inputPenerimaan[5] = keteranganPenerimaan.getText().toString();
        inputPenerimaan[6] = tglsubmitPenerimaan;
        inputPenerimaan[7] = peristiwaPenerimaan.getText().toString();
    }

    private void inputPermintaanSetup() {
        inputPermintaan = new String[8];
        inputPermintaan[0] = tglPermintaan.getText().toString();
        inputPermintaan[1] = jenisPermintaan.getText().toString();
        inputPermintaan[2] = nilaiPermintaan.getText().toString() + currencyPermintaan.getText().toString();
        inputPermintaan[3] = jumlahPermintaan.getText().toString();
        inputPermintaan[4] = penerimaPermintaan.getText().toString();
        inputPermintaan[5] = keteranganPermintaan.getText().toString();
        inputPermintaan[6] = tglsubmitPermintaan;
        inputPermintaan[7] = peristiwaPermintaan.getText().toString();
    }

    public void parseXml(String strXml){
        DocumentBuilderFactory factory;
        DocumentBuilder builder;
        Document dom;

        try {
            InputStream is = new ByteArrayInputStream(strXml.getBytes("utf-8"));
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            dom = builder.parse(is);

            NodeList nodeListError = dom.getElementsByTagName("ReturnMessage");

            if (dom.getElementsByTagName("ReturnObject") != null) {
                NodeList nodeListSuccess = dom.getElementsByTagName("ReturnObject");

                if (nodeListSuccess.getLength() > 0) {
                    parseJson(nodeListSuccess.item(0).getTextContent(), KODE_PENERIMAAN);
                    parseJson(nodeListSuccess.item(0).getTextContent(), CURRENCY);
                    parseJson(nodeListSuccess.item(0).getTextContent(), PERISTIWA_PENERIMAAN);
                }
            } else {
                for (int i = 0; i < nodeListError.getLength(); i++) {
                    String strError = nodeListError.item(i).getTextContent();
                    String message = "Could not get data due to:" + strError;

                    if (strError.contains("401")) {
                        message = "Could not get data. It might be you are Loged in from other device or your session was exiperd.";
                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Login again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    startActivity(new Intent(InputGratifikasiActivity.this,
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
        } catch(Exception e){
            Log.d("errorparsexml", e.getMessage());
        }
    }

    public void parseXmlSubmit(String strXml){
        DocumentBuilderFactory factory;
        DocumentBuilder builder;
        Document dom;

        try {
            InputStream is = new ByteArrayInputStream(strXml.getBytes("utf-8"));
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            dom = builder.parse(is);

            NodeList nodeListError = dom.getElementsByTagName("ReturnMessage");

            parseJsonSubmit(nodeListError.item(0).getTextContent());
            if (dom.getElementsByTagName("ReturnObject") != null) {
                NodeList nodeListSuccess = dom.getElementsByTagName("ReturnObject");

            } else {
                for (int i = 0; i < nodeListError.getLength(); i++) {
                    String strError = nodeListError.item(i).getTextContent();
                    String message = "Could not get data due to:" + strError;

                    if (strError.contains("401")) {
                        message = "Could not get data. It might be you are Loged in from other device or your session was exiperd.";
                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Login again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    startActivity(new Intent(InputGratifikasiActivity.this,
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
        } catch(Exception e){
            Log.d("errorparsexml", e.getMessage());
        }
    }

    private void parseJson(String strJson, String payload) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);
        JsonArray jsonArray = jsonObject.getAsJsonArray(payload);

        if (jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jo = jsonArray.get(i).getAsJsonObject();
                if (payload.equals(KODE_PENERIMAAN)) {
                    kodePenerimaanList.add(jo.get("Nama_penerimaan").getAsString());
                    Log.d("datanya " + payload, jo.get("Nama_penerimaan").getAsString());
                } else if (payload.equals(CURRENCY)) {
                    currencyList.add(jo.get("Nama_Currency").getAsString());
                    Log.d("datanya " + payload, jo.get("Nama_Currency").getAsString());
                } else if (payload.equals(PERISTIWA_PENERIMAAN)) {
                    peristiwaPenerimaanList.add(jo.get("Peristiwa_penerimaan").getAsString());
                    Log.d("datanya " + payload, jo.get("Peristiwa_penerimaan").getAsString());
                }
            }

        } else {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }

    }

    private void calendarSetup(final TextInputEditText tvDate) {
        c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        tvDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}