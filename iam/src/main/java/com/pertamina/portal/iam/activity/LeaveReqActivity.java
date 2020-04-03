package com.pertamina.portal.iam.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.pertamina.portal.core.activity.BackableNoActionbarActivity;
import com.pertamina.portal.iam.R;

public class LeaveReqActivity extends BackableNoActionbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_participant);
        super.onCreateBackable(this, R.id.ivBack);
    }
}
