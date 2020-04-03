package com.pertamina.portal.iam.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.pertamina.portal.core.activity.BackableNoActionbarActivity;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.PrefUtils;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.core.utils.StringUtils;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.base.BaseWorklistActivity;
import com.pertamina.portal.iam.utils.FileDownloader;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DokumenPreviewActivity extends BaseWorklistActivity {

    public static final String URL = "url";
    public static final String FILENAME = "filename";
    public static final String TGL_UPLOAD = "tgl_upload";
    public static final String NAME = "name";
    private String TAG = "DokumenPreviewActivity";
    private AlertDialog loading, alertDialog, openFolderDialog;
    private PortalApiInterface restApi;
    private PDFView pdfview;
    private static final int REQUEST_CODE_WES = 1;
    private String strUrl;
    private String filename;
    private RelativeLayout btDownload;
    private String path;
    private TextView tvNamaDokumen;
    private TextView tvTglUpload;
    private ImageView imageViewDocumentIV;
    private String tglUpload;
    private boolean isDirectToOpenFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_document_preview);
        super.onCreateBackable(this, R.id.ivBack);

        loading = getLoading();
        restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        pdfview = (PDFView) findViewById(R.id.pdfView);
        imageViewDocumentIV = findViewById(R.id.imageviewDocumentIV);
        btDownload = findViewById(R.id.btDownload);
        tvNamaDokumen = (TextView) findViewById(R.id.tvNamaDokumen);
        tvTglUpload = (TextView) findViewById(R.id.tvTglUpload);

        String[] fName = DokumenPreviewActivity.URL.split(Pattern.quote(File.separator));
        strUrl = getIntent().getStringExtra(DokumenPreviewActivity.URL);
        filename = getIntent().getStringExtra(fName[fName.length - 1]);
        tglUpload = getIntent().getStringExtra(DokumenPreviewActivity.TGL_UPLOAD);

        Log.d(TAG, "tglUpload = " + tglUpload);
        Date date = StringUtils.toDate(tglUpload);
        tglUpload = StringUtils.formatDateFull(date);

        buildAlert();

        tvNamaDokumen.setText(getIntent().getStringExtra(NAME));
        tvTglUpload.setText(tglUpload);
        btDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (path != null) {
                    Log.d("filepath", path);
//                    if (isDirectToOpenFolder) {
                    openFolderDialog.setMessage("File tersimpan di internal storage di folder pertamina-i-am-document");
                    openFolderDialog.show();
//                        return;
//                    }
//                    downloadPDF();
                }
            }
        });
    }

    private void downloadPDF() {
        loading.show();
        Log.d("download file", PortalApiInterface.DOWNLOAD_DOCUMENT+strUrl + " + " + filename);
        new DownloadFile().execute(PortalApiInterface.DOWNLOAD_DOCUMENT+strUrl, filename);
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
            loadDocument(strUrl, filename);
        } else {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Permission Required")
                    .setMessage("This applicaiton need WRITE & READ External Storage Permission")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(DokumenPreviewActivity.this,
                                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WES);

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            Toast.makeText(getApplicationContext(), "Document can't be downloaded", Toast.LENGTH_LONG).show();
                        }
                    })
                    .create();

            dialog.show();
        }
    }

    private void loadDocument(String strUrl, final String filename) {
        Log.d(TAG, "loadDocument: " + strUrl);

        loading.show();
        restApi.loadFile(strUrl)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                        final Response<ResponseBody> tmResponse = response;
                        if (response.isSuccessful()) {
                            new AsyncTask<Void, Void, String>() {
                                @Override
                                protected String doInBackground(Void... voids) {
                                    Log.d("apa responsenya", response.body() + " filename " + filename);
//                                    DokumenPreviewActivity.this.path = writeResponseBodyToDisk(response.body(), filename);
                                    DokumenPreviewActivity.this.path = writeResponseBodyToTemporary(response.body(), filename);

                                    Log.d(TAG, "file download was a success? " + path);
                                    return path;
                                }

                                @Override
                                protected void onPostExecute(String string) {
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
                                                imageViewDocumentIV.setVisibility(View.GONE);
                                                pdfview.setVisibility(View.VISIBLE);
                                            } else if (pdfExtention.equalsIgnoreCase(".png") ||
                                                    pdfExtention.equalsIgnoreCase(".jpg") ||
                                                    pdfExtention.equalsIgnoreCase("jpeg")) {
                                                pdfview.setVisibility(View.GONE);
                                                imageViewDocumentIV.setVisibility(View.VISIBLE);
                                                imageViewDocumentIV.setImageURI(Uri.parse("file://" + path));
                                            } else {
                                                isDirectToOpenFolder = true;
//                                                openFolder();
                                            }
                                        }
                                    });
                                }
                            }.execute();
                        } else {
                            Log.d(TAG, "getProcessInstance false _ " + response.raw().toString());
                            DokumenPreviewActivity.super.handleError(response);
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

    private String writeResponseBodyToTemporary(ResponseBody body, String filename) {
        try {
            File file = new File(this.getCacheDir(), filename);

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
                File folder = new File(extStorageDirectory, "pertamina-i-am-document");
                folder.mkdir();

                writer = new FileWriter(folder + "/" + path.substring(path.lastIndexOf("/")+1));

                /** Saving the contents to the file*/
                writer.write(content.toString());

                /** Closing the writer object */
                writer.close();

//                Toast.makeText(getBaseContext(), "Temporarily saved contents in " + tempFile.getPath(), Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private String writeResponseBodyToDisk(ResponseBody body, String filename) {
        try {

            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "pertamina-i-am");
            folder.mkdir();

            File futureStudioIconFile = new File(folder, filename);
//            File futureStudioIconFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//            + File.separator + "iam_mydocument_"+ filename);
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
                loadDocument(strUrl, filename);
            }
        }
    }

    public void openFolder(){
//        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//        + File.separator );
//        String tmpPath = file.getAbsolutePath() + "/";
//
//        Log.d(TAG, "tmpPath = " + tmpPath);
//
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        Uri uri = Uri.parse(tmpPath);
//        intent.setDataAndType(uri, "*/*");
//        startActivity(Intent.createChooser(intent, "Open folder"));

        startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
    }

    private void openFolder2() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
                +  File.separator + "pertamina-i-am-document" + File.separator);
        intent.setDataAndType(uri, "*/*");
        startActivity(Intent.createChooser(intent, "Open folder"));
    }

    private class DownloadFile extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "pertamina-i-am-document");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            FileDownloader.downloadFile(DokumenPreviewActivity.this, fileUrl, pdfFile);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loading.dismiss();
            Toast.makeText(DokumenPreviewActivity.this, "File telah di-Download ke folder pertamina-i-am-document di internal storage", Toast.LENGTH_LONG).show();
        }
    }

    public void buildAlert() {
        alertDialog = new AlertDialog.Builder(this)
                .setNeutralButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();

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
}
