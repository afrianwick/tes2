package com.pertamina.portal.iam.activity.base;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.pertamina.portal.core.activity.BackableNoActionbarActivity;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.ImageUtils.ImageUtils;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.fragments.PersonalDataFragment;
import com.pertamina.portal.iam.fragments.WorklistCommentFragment;
import com.pertamina.portal.iam.interfaces.LoadProfileListener;
import com.pertamina.portal.iam.models.IamComment;

import java.io.IOException;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseWorklistActivity extends BackableNoActionbarActivity {

    private static final String TAG = "BaseWorklistActivity";
    private AlertDialog mLoading;
    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void loadProfilePic(String username, int resProfileImage) {
        final CircularImageView civ = (CircularImageView) findViewById(resProfileImage);
        username = username.replace("\\", "%5C");
        String url = "ROOT/Public/Images/ProfilePictures/" + username + ".jpg";

        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        restApi.loadImage(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    try {
                        Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                        civ.setImageBitmap(ImageUtils.scaleDown(bmp, 100, false));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.d(TAG, "load image not success" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "load image onFailure");
                t.printStackTrace();
            }
        });
    }

    public void loadComments(ArrayList<IamComment> commentList) {
        Log.d(TAG, "loadComments::" + commentList.size());

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flComments, WorklistCommentFragment.newInstance(commentList));
        ft.commit();
    }

    public void pleaseComment() {
        Toast.makeText(this,
                getResources().getString(R.string.please_comment), Toast.LENGTH_LONG).show();
    }

    public void pleaseCheckDisclaimer() {
        Toast.makeText(this,
                "Checklis disclaimer", Toast.LENGTH_LONG).show();
    }

    public void initPersonalFragment(String personalNum, LoadProfileListener profileListener) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        PersonalDataFragment fragment = PersonalDataFragment.newInstance(personalNum);
        fragment.setProfileListener(profileListener);
        ft.replace(R.id.fmFragmentPersonal, fragment);
        ft.commit();
    }
}
