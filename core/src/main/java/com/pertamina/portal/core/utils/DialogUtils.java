package com.pertamina.portal.core.utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;

public class DialogUtils {

    private final Activity activity;
    private final int layout;
    private final int layoutViewGroup;
    private AlertDialog alertDialog;
    private View dialogView;

    public DialogUtils(Activity act, int layout, int layoutViewGroup) {
        this.activity = act;
        this.layout = layout;
        this.layoutViewGroup = layoutViewGroup;

        setup();
    }

    private void setup() {
        ViewGroup viewGroup = activity.findViewById(layoutViewGroup);
        dialogView = LayoutInflater.from(activity).inflate(layout, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView);

        alertDialog = builder.create();
    }

    private AlertDialog getAlertDialog() {
        return alertDialog;
    }

    private View getview() {
        return dialogView;
    }
}
