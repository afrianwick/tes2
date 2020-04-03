package com.pertamina.portal.iam.activity;

import android.os.Bundle;

import com.pertamina.portal.core.activity.BackableNoActionbarActivity;
import com.pertamina.portal.iam.R;

public class SscExecutionActivity extends BackableNoActionbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ssc_excecution_rmj);
        super.onCreateBackable(this, R.id.ivBack);
    }
}
