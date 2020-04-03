package com.pertamina.portal.iam.activity;

import android.os.Bundle;

import com.pertamina.portal.core.activity.BackableNoActionbarActivity;
import com.pertamina.portal.iam.R;

public class LeaveReqAddDestination2Activity extends BackableNoActionbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_destination2);
        super.onCreateBackable(this, R.id.ivBack);
    }
}
