package com.pertamina.portal.iam.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.pertamina.portal.core.activity.BackableNoActionbarActivity;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.base.BaseWorklistActivity;
import com.pertamina.portal.iam.fragments.PersonalDataFragment;
import com.pertamina.portal.iam.interfaces.LoadProfileListener;

import dmax.dialog.SpotsDialog;

public class PanDpkpActivity extends BaseWorklistActivity {

    private String TAG = "MPPKActivity";
    private AlertDialog loading;
    private AlertDialog mAlertDialog;
    private String processInstanceId;
    private String personalNum;
    private TextView folioNameTV, lastActivityTV;
    private PortalApiInterface restApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dpkp_detail);
        super.onCreateBackable(this, R.id.ivBack);

        Bundle extra = getIntent().getExtras();
        processInstanceId = extra.getString(Constants.KEY_PROCESS_INSTANCE);
        personalNum = extra.getString(Constants.KEY_PERSONAL_NUM);

        loading = new SpotsDialog.Builder().setContext(this).build();

        folioNameTV = findViewById(R.id.dpkpFolioNameTV);
        lastActivityTV = findViewById(R.id.dpkpLasActivityTV);

        restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);

        buildAlert();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getProcessInstance();
        initPersonalFragment();
    }

    private void initPersonalFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        PersonalDataFragment fragment = PersonalDataFragment.newInstance(personalNum);
        fragment.setProfileListener(profileListener);
        ft.replace(R.id.fmFragmentPersonal, fragment);
        ft.commit();
    }

    LoadProfileListener profileListener = new LoadProfileListener() {
        @Override
        public void loadProfile(String username) {
            Log.d(TAG, "loadProfilePic");
            PanDpkpActivity.this.loadProfilePic(username);
        }
    };

    public void loadProfilePic(String username) {
        super.loadProfilePic(username, R.id.ivProfile);
    }
}
