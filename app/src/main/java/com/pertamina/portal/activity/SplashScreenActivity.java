package com.pertamina.portal.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.JsonObject;
import com.pertamina.portal.BuildConfig;
import com.pertamina.portal.MainActivity;
import com.pertamina.portal.R;
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.PrefUtils;
import com.pertamina.portal.models.UpdateVersionModel;

import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SplashScreenActivity extends AppCompatActivity {

    private Context context;

    private FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    private HashMap<String, Object> firebaseDefaultMap;
    private String APP_UPDATE = "app_update";
    private UpdateVersionModel updateVersionModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (PrefUtils.Build(this).getPref().getString(Constants.KEY_REFRESH_TOKEN, "") != null &&
                !PrefUtils.Build(this).getPref().getString(Constants.KEY_REFRESH_TOKEN, "").equalsIgnoreCase("")) {
            Log.d("refresh_token", PrefUtils.Build(this).getPref().getString(Constants.KEY_REFRESH_TOKEN, ""));
        }

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TEST", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();


                        System.out.println("nilai token firebase adalah " + token);



//                        Toast.makeText(SplashActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });

        context = getApplicationContext();
        String strBuildNum = "Build number: " + BuildConfig.VERSION_NAME;
        TextView tvBuildNumber = (TextView) findViewById(R.id.tvBuildNumber);
        tvBuildNumber.setText(strBuildNum);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Inject Token untuk Development!!
//                PrefUtils.Build(context).getPref().edit().putString(Constants.KEY_ACCES_TOKEN, "4gpSxhOIkwp6CZKa0AYLj-urfGwcPBPQRKiUa3gSr72KsqZ9k5kZsVpzc6FbcEkV0JjBhuf8DWAFRzWIM-YKTivOyHeoEy6SlUsuQrr1IITlPyTIiEgmjZDRnNcAVkl6zNNXzyg4TKEtP2TEdbMsUenIiIdMH6h5-UmwFv16Wwr5i1nM4u4lbQRfpsB7DrRQ1qPWt5SjoUjJsrEp5exga1VuzscT5Ik-4aPEsuEAcEn-31TVFer6D88igYh86mjTsWrv7sIYGIkVkVzdF5YbODk6YdWDwbjAt5TFcDwS2UlSOKo8oLOY1CDRNBDw1-0QmBU_oAMU6P_-apLUqc4YyW9wK5f3Vi8SPnFDiZG6rpFAlLP3RfXAYa2ay09zofcoqZDo55J3_ModRQc2H5zmRaOmS6Ee7vH4ttb6KsiEGjqYc8so8Q3TGJVHYHPk2Z8gXCwX6mcCJRdUQHI94kwU87_IXA_zxleqw7fkj3zM7zHJIWjLaHRP2yP0VgnGmtqUZl96ZQzAIw-Q2BQT16sSpP88QY9Zxza6c7Zg58y_zSxR2xmc0qgN_UBim61wVi3kWpHHp5tIWnM8b3RAWJ5bEOHNpUgcfNCxdlclqm-O797CBl8Isq2hMzi2iN3cUtohayI-d0V99d5R4qKmjxe7OqSciESTL2aRHV3pYSzIOud6yAjw3CdyqUZZlHyZbb1-itNmg_rKB-EYtnv62J6ESJ4W1wuhCaqnSikmTD82FKPXw1R1dbSHAeQaRvTHZii952xWF2nJhCLtKsU1TLV0ZIdRQZDgSH4IRjZ1L4eprY0I21nFGYoSM3ZkDPVN0gH7uPw7Ri6ufnLk76VgxCEdr1WgKVmT3ip_bUbtK1BtiiIQvujfZ-GQd-GnElqqVxP87l6coo9Zo_SPggT2fMqlhh6_Y4S-xktZ6rNpN_a5h-B_bQFJgcUURKsyv8jSPsxiLdQ15ezHoMJYWJW3GXzZrb6eL8iZt93h-5V32knOWUcW7pmvSQnSNC_krpTUgZHNYcvwVcyqf9MMah1QEZ5jFGCJI1T-UhIDFC6EVwQthEG-_t01mjSCeUdavoO6xUckDjU_UZIhMVIx7-l-8Ks-gw").apply();

                firebaseRemoteConfigSetup();
                firebaseRemoteConfigListenerSetup();
            }
        }, 3000);
    }

    private void firebaseRemoteConfigSetup() {
        firebaseDefaultMap = new HashMap<>();
        firebaseDefaultMap.put(APP_UPDATE, getCurrentVersionCode());
        firebaseRemoteConfig.setDefaults(firebaseDefaultMap);
        firebaseRemoteConfig.setConfigSettings(
                new FirebaseRemoteConfigSettings.Builder()
                        .build());
    }

    private void firebaseRemoteConfigListenerSetup() {
        firebaseRemoteConfig.fetch(0).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    firebaseRemoteConfig.activateFetched();
                    if (task.isSuccessful()) {
                        firebaseRemoteConfig.activateFetched();
                        Log.d("TESTVERSIONCODE", "Fetched value: " + firebaseRemoteConfig.getString(APP_UPDATE));
                        Log.d("TESTVERSIONCODE", "Fetched value: " + (int) firebaseRemoteConfig.getDouble(APP_UPDATE));

                        try {
                            JSONObject jsonObject = new JSONObject(firebaseRemoteConfig.getString(APP_UPDATE));

                            updateVersionModel = new UpdateVersionModel();
                            updateVersionModel.setForced(jsonObject.getBoolean("isForced"));
                            updateVersionModel.setTitle(jsonObject.getString("title"));
                            updateVersionModel.setMessage(jsonObject.getString("message"));
                            updateVersionModel.setVersionCode(jsonObject.getInt("versionCode"));

                            checkForUpdate();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(SplashScreenActivity.this, "Someting went wrong please try again",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void activityTransition() {
        SharedPreferences pref = PrefUtils.Build(context).getPref();
        String accsTkn = pref
                .getString(Constants.KEY_ACCES_TOKEN, null);
        int authStep = pref.getInt(Constants.KEY_AUTH_STEP, 0);

        Intent intent = null;

        if (accsTkn != null) {
            switch (authStep) {
                case 2:
                    intent = new Intent(getApplicationContext(), OTPRequestActivity.class);
                    break;
                case 3:
                    intent = new Intent(getApplicationContext(), OTPInputActivity.class);
                    break;
                case 4:
                    intent = new Intent(getApplicationContext(), PinInputActivity.class);
                    break;
                default:
                    intent = new Intent(getApplicationContext(), PortalHomeActivity.class);
                    break;
            }
        } else {
            intent = new Intent(getApplicationContext(), LoginActivity.class);
        }

        startActivity(intent);
        finish();
    }

    private int getCurrentVersionCode() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void checkForUpdate() {
        Log.d("versioncode", String.valueOf(updateVersionModel.getVersionCode()));
        Log.d("currentversioncode", String.valueOf(getCurrentVersionCode()));
        if (updateVersionModel.getVersionCode() > getCurrentVersionCode()) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this).setTitle(updateVersionModel.getTitle())
                    .setMessage(updateVersionModel.getMessage()).setPositiveButton(
                    "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                        }
                    });

            if (!updateVersionModel.isForced()) {
                alertDialog.setNegativeButton("Skip", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        activityTransition();
                    }
                });
            }
            alertDialog.setCancelable(!updateVersionModel.isForced());
            alertDialog.show();
        } else {
            activityTransition();
        }
    }
}
