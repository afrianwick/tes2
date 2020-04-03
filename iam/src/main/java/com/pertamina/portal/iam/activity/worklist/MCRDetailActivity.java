package com.pertamina.portal.iam.activity.worklist;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.models.MCRDetailModel;

public class MCRDetailActivity extends AppCompatActivity {

    private TextInputEditText startDateTIET, endDateTIET, familyMemberTIET, familyTypeTIET, ageYearTIET, ageMonthTIET;
    private TextInputEditText pisaNumberTIET, clinicNameTIET, bloodTypeTIET;
    private ImageView backIV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcr_detail);

        backIV = findViewById(R.id.ivBack);
        startDateTIET = findViewById(R.id.mcrDetailStartDateTIET);
        endDateTIET = findViewById(R.id.mcrDetailEndDateTIET);
        familyMemberTIET = findViewById(R.id.mcrDetailFamilyMemberTIET);
        familyTypeTIET = findViewById(R.id.mcrDetailFamilyTypeTIET);
        ageYearTIET = findViewById(R.id.mcrDetailAgeYearTIET);
        ageMonthTIET = findViewById(R.id.mcrDetailAgeMonthTIET);
        pisaNumberTIET = findViewById(R.id.mcrDetailPisaNumberTIET);
        clinicNameTIET = findViewById(R.id.mcrDetailClinicNameTIET);
        bloodTypeTIET = findViewById(R.id.mcrDetailBloodTypeTIET);

        MCRDetailModel mcrDetailModel = (MCRDetailModel) getIntent().getSerializableExtra("mcrDetailModel");

        startDateTIET.setText(mcrDetailModel.getStartDate());
        endDateTIET.setText(mcrDetailModel.getEndDate());
        familyMemberTIET.setText(mcrDetailModel.getFamilyMember());
        familyTypeTIET.setText(mcrDetailModel.getFamilyType());
        ageYearTIET.setText(mcrDetailModel.getAgeYear());
        ageMonthTIET.setText(mcrDetailModel.getAgeMonth());
        pisaNumberTIET.setText(mcrDetailModel.getPisaNumber());
        clinicNameTIET.setText(mcrDetailModel.getClinicName());
        bloodTypeTIET.setText(mcrDetailModel.getBloodType());

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
