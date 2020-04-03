package com.pertamina.portal.iam.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class ResponseErrorHandling {

    public static void handleError(Response<ResponseBody> response, AlertDialog alertDialog, Activity activity) {
        if (response.code() == 401) {
            showError("401", alertDialog, activity);
        } else if (response.code() == 404) {
            showError("404", alertDialog, activity);
        } else {
            try {
                showError(response.errorBody().string(), alertDialog, activity);
            } catch (IOException e) {
                showError(e.getMessage(), alertDialog, activity);
                e.printStackTrace();
            }
        }
    }

    private static void showError(String strError, AlertDialog alertDialog, final Activity activity) {
        String message = "Could not get data due to:" + strError;

        if (strError.contains("401")) {
            message = "Could not get data. It might be you are Loged in from other device or your session was exiperd.";
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Login again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        activity.startActivity(new Intent(activity,
                                Class.forName("com.pertamina.portal.activity.LoginActivity")));
                        activity.finish();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(activity)
                    .setNeutralButton("close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            activity.finish();
                        }
                    })
                    .create();
        }

        alertDialog.setMessage(message);

        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

}
