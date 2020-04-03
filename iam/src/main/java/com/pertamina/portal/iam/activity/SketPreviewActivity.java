package com.pertamina.portal.iam.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.core.utils.StringUtils;
import com.pertamina.portal.iam.R;

import java.io.File;
import java.util.Date;
import java.util.regex.Pattern;

public class SketPreviewActivity extends AppCompatActivity {

    public static final String URL = "url";
    public static final String FILENAME = "filename";
    public static final String TGL_UPLOAD = "tgl_upload";
    public static final String NAME = "name";
    private String TAG = "DokumenPreviewActivity";
    private AlertDialog loading, alertDialog;
    private PortalApiInterface restApi;
    private PDFView pdfview;
    private static final int REQUEST_CODE_WES = 1;
    private String strUrl;
    private String filename;
    private Button btDownload;
    private String path;
    private TextView tvNamaDokumen;
    private TextView tvTglUpload;
    private String tglUpload;
    private boolean isDirectToOpenFolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sket_preview);

//        loading = getLoading();
        restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        pdfview = (PDFView) findViewById(R.id.pdfView);
        btDownload = (Button) findViewById(R.id.btDownload);
        tvNamaDokumen = (TextView) findViewById(R.id.tvNamaDokumen);
        tvTglUpload = (TextView) findViewById(R.id.tvTglUpload);

        String[] fName = DokumenPreviewActivity.URL.split(Pattern.quote(File.separator));
        strUrl = getIntent().getStringExtra(DokumenPreviewActivity.URL);
        filename = getIntent().getStringExtra(fName[fName.length - 1]);
        tglUpload = getIntent().getStringExtra(DokumenPreviewActivity.TGL_UPLOAD);

        Log.d(TAG, "tglUpload = " + tglUpload);
        Date date = StringUtils.toDate(tglUpload);
        tglUpload = StringUtils.formatDateFull(date);

//        buildAlert();

        tvNamaDokumen.setText(getIntent().getStringExtra(NAME));
        tvTglUpload.setText(tglUpload);
        btDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (path != null) {
                    Log.d("filepath", path);
                    if (isDirectToOpenFolder) {
//                        openFolder();
                        return;
                    }
//                    downloadPDF();
                }
            }
        });
    }
}
