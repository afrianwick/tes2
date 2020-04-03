package com.pertamina.portal.iam.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Utils {

    public static int PERMISSION_ALL = 1;
    public static String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static String formatDate(String date, String outputPattern, String inputPattern) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            simpleDateFormat.applyPattern(inputPattern);
            Date dateFormatted = simpleDateFormat.parse(date);
            simpleDateFormat.applyPattern(outputPattern);
            return simpleDateFormat.format(dateFormatted);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String formatDate(Date date, String outPutPettern) {
        return new SimpleDateFormat(outPutPettern).format(date);
    }

    public static HashMap<String, String> getKeteranganDocType() {
        HashMap<String, String> result = new HashMap<>();
        result.put("FLC", "Frame Lense Claim");
        result.put("MPPK", "Masa Persiapan Purna Kerja");
        result.put("RMJ", "Rencana Mutasi Jabatan");
        result.put("DPKP", "Dewan Perencanaan Karir Pekerja");
        result.put("SKMJ", "Surat Keterangan Mutasi Jabatan");
        result.put("MCR", "Medical Claim Card");
        result.put("OPC", "Orthodonthi Prosthodonthi Claim");
        result.put("AHE", "Autisme Help");
        result.put("SKET", "Surat Keterangan");
        result.put("LEV", "Leave");
        result.put("GRV", "Grievance");
        result.put("CLV", "Cancel Leave Request");
        result.put("OVT", "");
        result.put("DP", "");
        result.put("PRN", "");
        result.put("FLX", "");
        result.put("PPRP", "");
        result.put("PDP", "");
        result.put("SKG", "");
        result.put("CCU", "");
        result.put("BAT", "");
        result.put("MCL", "");
        result.put("SKP", "");
        result.put("PAN", "Personal Administration");
        result.put("DGSP", "");
        result.put("PDR", "");
        result.put("PDF", "");
        return result;
    }
}
