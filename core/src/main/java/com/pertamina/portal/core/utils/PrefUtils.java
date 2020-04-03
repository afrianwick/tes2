package com.pertamina.portal.core.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class PrefUtils {

    private final Context context;
    private final SharedPreferences pref;
    private final static String TAG = "PrefUtils";

    private Gson gson;

    public PrefUtils(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(Constants.APP_NAME, Activity.MODE_PRIVATE);
        gson = new Gson();
    }

    public static PrefUtils Build(Context applicationContext) {
        return new PrefUtils(applicationContext);
    }

    public SharedPreferences.Editor getPrefEditable() {
        return pref.edit();
    }

    public SharedPreferences getPref() {
        return pref;
    }

    public void removeAll() {
        pref.edit().clear().commit();
    }
}
