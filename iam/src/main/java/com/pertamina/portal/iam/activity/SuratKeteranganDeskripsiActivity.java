package com.pertamina.portal.iam.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.pertamina.portal.iam.R;

public class SuratKeteranganDeskripsiActivity extends AppCompatActivity {

    private AppCompatImageButton backIB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surat_keterangan_deskripsi);

        backIB = findViewById(R.id.backIB);

        backIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
