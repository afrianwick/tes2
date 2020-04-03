package com.pertamina.portal.iam.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;

import com.pertamina.portal.core.activity.BackableNoActionbarActivity;
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.base.BaseWorklistActivity;
import com.pertamina.portal.iam.fragments.PersonalDataFragment;

public class SuratKetActivity extends BaseWorklistActivity {

    private String personalInstanceId;
    private String personalNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_surat_ket);
        super.onCreateBackable(this, R.id.ivBack);

        View llReqSurat = findViewById(R.id.llReqSurat);
        View llReqSuratVisa = findViewById(R.id.llReqSuratVisa);
        View llHistorySuratKeterangan = findViewById(R.id.llHistorySuratKeterangan);

        llReqSurat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuratKetActivity.this, ReqSuratKetActivity.class);
                startActivity(intent);
            }
        });

        llReqSuratVisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(SuratKetActivity.this, "This feature will be coming soon.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SuratKetActivity.this, ReqSuratVisiActivity.class);
                startActivity(intent);
            }
        });

        llHistorySuratKeterangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuratKetActivity.this, HistorySuratKetActivity.class);
                startActivity(intent);
            }
        });

//        Bundle extra = getIntent().getExtras();
//        personalInstanceId = extra.getString(Constants.KEY_PROCESS_INSTANCE);
//        personalNum = extra.getString(Constants.KEY_PERSONAL_NUM);
//        personalNum = extra.getString(Constants.KEY_PERSONAL_NUM);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        getProcessInstance();
//        initPersonalFragment();
    }

//    private void initPersonalFragment() {
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.fmFragmentPersonal, PersonalDataFragment.newInstance(personalNum));
//        ft.commit();
//    }
}
