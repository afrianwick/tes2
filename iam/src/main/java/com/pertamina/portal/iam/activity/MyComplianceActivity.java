package com.pertamina.portal.iam.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.pertamina.portal.core.activity.BackableNoActionbarActivity;
import com.pertamina.portal.iam.R;

public class MyComplianceActivity extends BackableNoActionbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_compliance);
        super.onCreateBackable(this, R.id.ivBack);

        View llInputGratifikasi = findViewById(R.id.llInputGratifikasi);
        View llReportGratifikasi = findViewById(R.id.llReportGratifikasi);
        View llCodeOfConduct = findViewById(R.id.llCodeOfConduct);
        View llConflictOfInterest = findViewById(R.id.llConflictOfInterest);

        llInputGratifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyComplianceActivity.this, InputGratifikasiActivity.class);
                startActivity(intent);
            }
        });

        llReportGratifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyComplianceActivity.this, ReportGratifikasiActivity.class);
                startActivity(intent);
            }
        });

        llCodeOfConduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MyComplianceActivity.this, "This feature will be coming soon.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MyComplianceActivity.this, CodeOfConductActivity.class);
                startActivity(intent);
            }
        });

        llConflictOfInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MyComplianceActivity.this, "This feature will be coming soon.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MyComplianceActivity.this, ConflictOfInterestActivity.class);
                startActivity(intent);
            }
        });
    }
}
