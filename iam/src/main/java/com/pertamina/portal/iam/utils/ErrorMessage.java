package com.pertamina.portal.iam.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.pertamina.portal.iam.R;

public class ErrorMessage {

    public static void errorMessage(Context context, ViewGroup view, String message){
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.sbError));
        snackbar.show();
    }

}
