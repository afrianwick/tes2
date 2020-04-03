package com.pertamina.portal.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.pertamina.portal.R;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.ImageUtils.ImageUtils;
import com.pertamina.portal.core.utils.PrefUtils;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.iam.fragments.ConflictOfInterest.FragmentForm;
import com.pertamina.portal.iam.fragments.ConflictOfInterest.FragmentPage1;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class PortalHomeActivity extends AppCompatActivity {

    @BindView(R.id.cvIam)
    CardView cvIam;
    @BindView(R.id.cvEcorr)
    CardView cvEcorr;
    @BindView(R.id.cvWfh)
    CardView cvWfh;
    @BindView(R.id.account_view) View accountView;
    @BindView(R.id.portal_home_view) ConstraintLayout portalHomeView;
    @BindView(R.id.imageView2) ImageView imageView;
    @BindView(R.id.accountSignOutContainerLL)
    LinearLayout signOutLL;
    @BindView(R.id.accountEmailTV)
    TextView emailTV;
    @BindView(R.id.accountUsernameTV)
    TextView usernameTV;
    @BindView(R.id.accountIV)
    ImageView photoIV;

    @BindView(R.id.accountPersonalIDTV) TextView personalIDTV;
    @BindView(R.id.accountPersonalTokenTV) TextView tokenTV;
    @BindView(R.id.accountPersonalNumberTV) TextView personalNumberTV;
    @BindView(R.id.accountPositionTV) TextView positionTV;
    @BindView(R.id.accountKBOTV) TextView kboTV;
    @BindView(R.id.accountCompanyTV) TextView companyTV;

    private AlertDialog alertDialog;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    accountView.setVisibility(View.GONE);
                    portalHomeView.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_notifications:
                    accountView.setVisibility(View.VISIBLE);
                    portalHomeView.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ButterKnife.bind(this);


        cvIam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(PortalHomeActivity.this,
                            Class.forName("com.pertamina.portal.iam.activity.IamHomeActivity"));

                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        cvEcorr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(PortalHomeActivity.this,
                            Class.forName("com.pertamina.portal.iam.activity.EcorrHomeActivity"));

                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        cvWfh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(PortalHomeActivity.this,
                            Class.forName("com.pertamina.portal.iam.activity.WfhHomeActivity"));

                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });



        try {
            emailTV.setText(PrefUtils.Build(this).getPref().getString(Constants.KEY_EMAIL, ""));
            usernameTV.setText(PrefUtils.Build(this).getPref().getString(Constants.KEY_PCNAMEM, ""));
            personalIDTV.setText(PrefUtils.Build(this).getPref().getString(Constants.KEY_PERSON_ID, ""));
            personalNumberTV.setText(PrefUtils.Build(this).getPref().getString(Constants.KEY_PERSONAL_NUM, ""));
            companyTV.setText(PrefUtils.Build(this).getPref().getString(Constants.KEY_PBUTXTM, ""));
            positionTV.setText(PrefUtils.Build(this).getPref().getString(Constants.KEY_PPLTXTM, ""));
            kboTV.setText(PrefUtils.Build(this).getPref().getString(Constants.KEY_KBO, ""));
            loadProfilePic();

        } catch (Exception e) {

        }

        buildAlert();

        signOutLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.setMessage("Apakah anda yakin ingin keluar?");
                alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PrefUtils.Build(PortalHomeActivity.this).removeAll();
                        Intent intent = new Intent(PortalHomeActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });
                alertDialog.show();
            }
        });

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


                        setFCM(token);



//                        Toast.makeText(SplashActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void buildAlert() {
        alertDialog = new AlertDialog.Builder(this)
                .setNeutralButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
    }

    private void loadProfilePic() {

        String url = "ROOT/Public/Images/ProfilePictures/" + PrefUtils.Build(this).getPref().getString(Constants.KEY_USERNAME, "") + ".jpg";

        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        restApi.loadImage(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                        photoIV.setImageBitmap(ImageUtils.scaleDown(bmp, 100, false));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.d("profile", "load image not success" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("profile", "load image onFailure");
                t.printStackTrace();
            }
        });
    }

    private void setFCM(String token) {
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);
        restApi.fcm(token).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
//                    tokenTV.setText(token);
                    Log.d("setfcm success", "yes");
                } else {
                    try {
                        Log.d("profile", "load image not success" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("profile", "load image onFailure");
                t.printStackTrace();
            }
        });
    }

}
