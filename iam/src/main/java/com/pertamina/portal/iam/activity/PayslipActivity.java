package com.pertamina.portal.iam.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
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
import com.pertamina.portal.iam.adapters.PaySlipAdapter;
import com.pertamina.portal.iam.adapters.PayslipAdapters;
import com.pertamina.portal.iam.interfaces.PaySlipView;
import com.pertamina.portal.iam.models.Payslip;
import com.pertamina.portal.iam.utils.FileDownloader;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayslipActivity extends BackableNoActionbarActivity implements PaySlipView {

    private static final String TAG = "PayslipActivity";
    private static final int REQUEST_CODE_WES = 1;
    private AlertDialog loading;
    private AlertDialog alertDialog, openFolderDialog;
    private ArrayList<Payslip> listPayslip;
    private PDFView pdfview;
    private PortalApiInterface restApi;
    private Context context;
    private RelativeLayout payslipDownloadButton;
    private String contactID, payslipName;
    private PayslipAdapters payslipAdapters;
    private PaySlipAdapter paySlipAdapter;
    private View dialogPaySlipView;
    private ConstraintLayout dialogPaySlipContainerCL;
    private TextView dialogPaySlipTitleTV;
    private EditText dialogPaySlipSearchET;
    private RecyclerView dialogPaySlipRV;
    private EditText payslipET;
    private RelativeLayout paySlipContainerRL;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_payslip);
        super.onCreateBackable(this, R.id.ivBack);

        openDialogBuild();

        context = getApplicationContext();
        pdfview = (PDFView) findViewById(R.id.pdfView);
        payslipET = findViewById(R.id.payslipET);
        payslipDownloadButton = findViewById(R.id.payslipDownloadButton);
        dialogPaySlipView = findViewById(R.id.payslipDialog);
        dialogPaySlipContainerCL = dialogPaySlipView.findViewById(R.id.dialogCountryContainerCL);
        dialogPaySlipSearchET = dialogPaySlipView.findViewById(R.id.dialogCountrySearcET);
        dialogPaySlipRV = dialogPaySlipView.findViewById(R.id.dialogCountryRV);
        dialogPaySlipTitleTV = dialogPaySlipView.findViewById(R.id.dialogCountryTitleTV);
        dialogPaySlipTitleTV.setText("Payslip");
        paySlipContainerRL = findViewById(R.id.paySlipContainerRL);
        loading = getLoading();
        alertDialog = getmAlertDialog();
        restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);


        payslipET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paySlipContainerRL.setVisibility(View.VISIBLE);
            }
        });

        dialogPaySlipContainerCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paySlipContainerRL.setVisibility(View.GONE);
            }
        });

        dialogPaySlipSearchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (listPayslip == null || listPayslip.size() == 0) {
                    return;
                }
                if (charSequence.length() == 0) {
                    paySlipAdapter.updateList(listPayslip);
                } else {
                    List<Payslip> filterData = new ArrayList<>();
                    for (int ii = 0; ii < listPayslip.size(); ii++) {
                        if (listPayslip.get(ii).payslipDesc.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            filterData.add(listPayslip.get(ii));
                        }
                    }

                    paySlipAdapter.updateList(filterData);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        payslipDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                openFolder();
//                downloadPDF();
                if (path != null) {
                    Log.d("filepath", path);
//                    if (isDirectToOpenFolder) {
                    openFolderDialog.setMessage("File tersimpan di internal storage di folder pertamina-i-am-payslip");
                    openFolderDialog.show();
//                        return;
//                    }
//                    downloadPDF();
                }
            }
        });
    }

    private void openDialogBuild() {
        openFolderDialog = new AlertDialog.Builder(this)
                .setPositiveButton("Open", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getFileFromTemp();
                        openFolder2();
                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
    }

    private void openFolder2() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
                +  File.separator + "pertamina-i-am-payslip" + File.separator);
        intent.setDataAndType(uri, "*/*");
        startActivity(Intent.createChooser(intent, "Open folder"));
    }

    private void getFileFromTemp() {
        /** Reading contents of the temporary file, if already exists */
        try {
            /** Getting a reference to temporary file, if created earlier */
            File tempFile = new File(path) ;

            /** Getting reference to edittext et_content */

            FileReader fReader = new FileReader(tempFile);
            BufferedReader bReader = new BufferedReader(fReader);

            /** Reading the contents of the file , line by line */
            String strLine="";
            StringBuilder content = new StringBuilder();

            while( (strLine=bReader.readLine()) != null  ){
                content.append(strLine+"\n");
            }

            FileWriter writer=null;
            try {
                String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                File folder = new File(extStorageDirectory, "pertamina-i-am-payslip");
                folder.mkdir();

                writer = new FileWriter(folder + "/" + path.substring(path.lastIndexOf("/")+1));

                /** Saving the contents to the file*/
                writer.write(content.toString());

                /** Closing the writer object */
                writer.close();

                Toast.makeText(getBaseContext(), "Temporarily saved contents in " + tempFile.getPath(), Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (dialogPaySlipView.isShown()) {
            paySlipContainerRL.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }

    private void downloadPDF() {
        loading.show();
        new DownloadFile().execute(PortalApiInterface.DOWNLOAD_PAYSLIP+contactID, payslipName + ".pdf");
    }

    @Override
    protected void onStart() {
        super.onStart();

        int writeExternalDisk = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int redExternalDisk = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if ((writeExternalDisk == PackageManager.PERMISSION_GRANTED) &&
                (redExternalDisk == PackageManager.PERMISSION_GRANTED)
        ) {
            Log.v(TAG,"Permission is granted");
            getListPayslip();
        } else {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Permission Required")
                    .setMessage("This applicaiton need WRITE & READ External Storage Permission")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(PayslipActivity.this,
                                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WES);

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            Toast.makeText(context, "Payslip can't be downloaded", Toast.LENGTH_LONG).show();
                        }
                    })
                    .create();

            dialog.show();
        }
    }

    private void getListPayslip() {
        loading.show();
        restApi.getListPayslip( "PersonalAdministrationServices", "GetPayslipList")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d(TAG, "getListPayslip sip.. " + strResponse);
                                parseXml(strResponse, successListener);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ResponseBody error = response.errorBody();

                            if (error != null) {
                                try {
                                    alertDialog.setMessage("Can not open Payslip due to: " + error.string());
                                    alertDialog.show();
                                } catch (IOException e) {
                                    alertDialog.setMessage("Failed contacting server. IOE");
                                    alertDialog.show();
                                    e.printStackTrace();
                                }
                            }

                            Log.d(TAG, "getListPayslip false _ " + response.raw().toString());
                        }

                        loading.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        alertDialog.setMessage("Failed contacting server.");
                        alertDialog.show();

                        Log.d(TAG, "getListPayslip onFailure..");
                        t.printStackTrace();
                    }
                });
    }

    private void loadPayslipDocument(final String strPeriod) {
        Log.d(TAG, "loadPayslipDocument: " + strPeriod);

        loading.show();
        restApi.getListPayslipFile(strPeriod)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                        final Response<ResponseBody> tmResponse = response;
                        if (response.isSuccessful()) {
                            Log.d(TAG, "get payslip file success" );
                            new AsyncTask<Void, Void, String>() {
                                @Override
                                protected String doInBackground(Void... voids) {
                                    Log.d("apa responsenya", response.body() + " filename " + strPeriod);
                                    path = writeResponseBodyToTemporary(response.body(), strPeriod);

                                    Log.d(TAG, "file download was a success? " + path);
                                    return path;
                                }

                                @Override
                                protected void onPostExecute(String string) {
//                                    super.onPostExecute(string);
//                                    final String path = string;
//                                    Log.d(TAG, "onPost path=" + path);
//
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            pdfview.fromUri(Uri.parse("file://" + path))
//                                                    .onError(new OnErrorListener() {
//                                                        @Override
//                                                        public void onError(Throwable t) {
//                                                            Log.e(TAG, "read pdf error");
//                                                            t.printStackTrace();
//                                                        }
//                                                    })
//                                                    .onLoad(new OnLoadCompleteListener() {
//                                                        @Override
//                                                        public void loadComplete(int nbPages) {
//                                                            Log.d(TAG, "read pdf complete " + nbPages);
//                                                        }
//                                                    })
//                                            .load();
//                                        }
//                                    });
                                    super.onPostExecute(string);
                                    final String path = string;
                                    Log.d(TAG, "onPost path=" + path);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            int startIndexExtention = path.length() - 4;
                                            String pdfExtention = path.substring(startIndexExtention, path.length());

                                            Log.d(TAG, "pdfExtention = " + pdfExtention);

                                            if (pdfExtention.equalsIgnoreCase(".pdf")) {
                                                pdfview.fromUri(Uri.parse("file://" + path))
                                                        .onError(new OnErrorListener() {
                                                            @Override
                                                            public void onError(Throwable t) {
                                                                Log.e(TAG, "read pdf error");
                                                                t.printStackTrace();
                                                            }
                                                        })
                                                        .onLoad(new OnLoadCompleteListener() {
                                                            @Override
                                                            public void loadComplete(int nbPages) {
                                                                Log.d(TAG, "read pdf complete " + nbPages);
                                                            }
                                                        })
                                                        .load();
                                            } else {
//                                                openFolder();
                                            }
                                        }
                                    });
                                }
                            }.execute();
                        } else {
                            Log.d(TAG, "getProcessInstance false _ " + response.raw().toString());
                        }

                        loading.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        Log.d(TAG, "getWorklistPending onFailure..");
                        t.printStackTrace();
                    }
                });
    }

    public void openFolder() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        + File.separator );
        String tmpPath = file.getAbsolutePath() + "/";

        Log.d(TAG, "tmpPath = " + tmpPath);

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(tmpPath);
        intent.setDataAndType(uri, "*/*");
        startActivity(Intent.createChooser(intent, "Open folder"));
    }

//    public void parseXml(String strXml){
//        DocumentBuilderFactory factory;
//        DocumentBuilder builder;
//        Document dom;
//
//        try {
//            InputStream is = new ByteArrayInputStream(strXml.getBytes("utf-8"));
//            factory = DocumentBuilderFactory.newInstance();
//            builder = factory.newDocumentBuilder();
//            dom = builder.parse(is);
//
//            NodeList nodeListError = dom.getElementsByTagName("ReturnMessage");
//            Log.d(TAG, "nodeList.getLength():" + nodeListError.getLength());
//
//            if (dom.getElementsByTagName("ReturnObject") != null) {
//                // TODO handle success request
//                NodeList nodeListSuccess = dom.getElementsByTagName("ReturnObject");
//
//                if (nodeListSuccess.getLength() > 0) {
//                    Log.d(TAG, "silahkan diproses");
//                    parseJson(nodeListSuccess.item(0).getTextContent());
//                }
//            } else {
//                // TODO handle failed request
//                for (int i = 0; i < nodeListError.getLength(); i++) {
//                    String strError = nodeListError.item(i).getTextContent();
//                    Log.d(TAG, "nodeList:" + i);
//                    Log.d(TAG, "value=" + strError);
//
//                    alertDialog.setMessage("Can not get data due to:" + strError);
//
//                    if (!alertDialog.isShowing()) {
//                        alertDialog.show();
//                    }
//                }
//            }
//        } catch(Exception e){
//            e.printStackTrace();
//        }
//    }

    private void parseJson(String strJson) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);
        JsonArray jsonArray = jsonObject.getAsJsonArray("Table0");
        listPayslip = new ArrayList<>();

        if (jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jo = jsonArray.get(i).getAsJsonObject();
                String sPIPENDM = jo.get("PIPENDM").getAsString();
                String sPIPBEGM = jo.get("PIPBEGM").getAsString();

                Payslip ps = new Payslip();
                ps.payslipConcatId = sPIPBEGM + sPIPENDM ;
                ps.payslipDesc = jo.get("PHDESCM").getAsString();

                listPayslip.add(ps);
            }
        } else {
            alertDialog.setMessage("You doesn't have payslip.");
            alertDialog.show();
        }

        contactID = listPayslip.get(0).payslipConcatId;
        payslipName = listPayslip.get(0).payslipDesc;

        payslipET.setText(payslipName);

        loadPayslipDocument(contactID);

        paySlipAdapter = new PaySlipAdapter(this, listPayslip, this);
        dialogPaySlipRV.setLayoutManager(new LinearLayoutManager(this));
        dialogPaySlipRV.setAdapter(paySlipAdapter);
    }

    private String writeResponseBodyToTemporary(ResponseBody body, String filename) {
        try {
            File file = new File(this.getCacheDir(),  File.separator + "payslip_"+ filename +".pdf");

            String strPath = file.getAbsolutePath();
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                }

                outputStream.flush();
                return strPath;
            } catch (IOException e) {
                Log.e(TAG, "IOException=" + e.getMessage());

                e.printStackTrace();
                return null;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String writeResponseBodyToDisk(ResponseBody body, String period) {
        try {
            File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator + "payslip_"+ period +".pdf");
            String strPath = futureStudioIconFile.getAbsolutePath();
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                }

                outputStream.flush();
                return strPath;
            } catch (IOException e) {
                Log.e(TAG, "IOException=" + e.getMessage());

                e.printStackTrace();
                return null;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_WES) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getListPayslip();
            }
        }
    }

    private OnSuccessListener successListener = new OnSuccessListener() {
        @Override
        public void onSuccess(String strJson) {
            parseJson(strJson);
        }
    };

    @Override
    public void onPaySlipItemClicked(Payslip payslip) {
        if (payslip != null) {
            Log.d(TAG, "payslip desc = " + payslip.payslipDesc);
            contactID = payslip.payslipConcatId;
            payslipName = payslip.payslipDesc;
            loadPayslipDocument(payslip.payslipConcatId);
            payslipET.setText(payslipName);
            paySlipContainerRL.setVisibility(View.GONE);
        }
    }

    private class DownloadFile extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "pertamina-i-am-payslip");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            FileDownloader.downloadFile(PayslipActivity.this, fileUrl, pdfFile);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loading.dismiss();
            Toast.makeText(PayslipActivity.this, "File telah di-Download ke folder pertamina-i-am-payslip di internal storage", Toast.LENGTH_LONG).show();
        }
    }
}
