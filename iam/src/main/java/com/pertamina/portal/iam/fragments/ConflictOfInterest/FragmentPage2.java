package com.pertamina.portal.iam.fragments.ConflictOfInterest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.ConflictOfInterestActivity;
import com.pertamina.portal.iam.utils.ErrorMessage;
import com.pertamina.portal.iam.utils.XMLParserUtils.XMLParserInterface;
import com.pertamina.portal.iam.utils.XMLParserUtils.XMLParserUtils;

import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.Arrays;

import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentPage2 extends Fragment {

    public static boolean[] isChecked = new boolean[9];

    public static CheckBox checkBox1;
    public static CheckBox checkBox2;
    public static CheckBox checkBox3;
    public static CheckBox checkBox4;
    public static CheckBox checkBox5;
    public static CheckBox checkBox6;
    public static CheckBox checkBox7;
    public static CheckBox checkBox8;
    public static CheckBox checkBox9;

    private ConflictOfInterestActivity conflictOfInterestActivity;

    public FragmentPage2() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fargment_conflict_of_interest_2, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        conflictOfInterestActivity = (ConflictOfInterestActivity) getActivity();

        checkBox1 = view.findViewById(R.id.coi1CB);
        checkBox2 = view.findViewById(R.id.coi2CB);
        checkBox3 = view.findViewById(R.id.coi3CB);
        checkBox4 = view.findViewById(R.id.coi4CB);
        checkBox5 = view.findViewById(R.id.coi5CB);
        checkBox6 = view.findViewById(R.id.coi6CB);
        checkBox7 = view.findViewById(R.id.coi7CB);
        checkBox8 = view.findViewById(R.id.coi8CB);
        checkBox9 = view.findViewById(R.id.coi9CB);

        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isChecked[0] = b;
                isAllChecked();
            }
        });

        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isChecked[1] = b;
                isAllChecked();
            }
        });

        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isChecked[2] = b;
                isAllChecked();
            }
        });

        checkBox4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isChecked[3] = b;
                isAllChecked();
            }
        });

        checkBox5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isChecked[4] = b;
                isAllChecked();
            }
        });

        checkBox6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isChecked[5] = b;
                isAllChecked();
            }
        });

        checkBox7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isChecked[6] = b;
                isAllChecked();
            }
        });

        checkBox8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isChecked[7] = b;
                isAllChecked();
            }
        });

        checkBox9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isChecked[8] = b;
                isAllChecked();
            }
        });

        if (ConflictOfInterestActivity.isCOI) {
            checkBox1.setChecked(true);
            checkBox2.setChecked(true);
            checkBox3.setChecked(true);
            checkBox4.setChecked(true);
            checkBox5.setChecked(true);
            checkBox6.setChecked(true);
            checkBox7.setChecked(true);
            checkBox8.setChecked(true);
            checkBox9.setChecked(true);
        }

        if (ConflictOfInterestActivity.isSubmitted) {
            checkBox1.setEnabled(false);
            checkBox2.setEnabled(false);
            checkBox3.setEnabled(false);
            checkBox4.setEnabled(false);
            checkBox5.setEnabled(false);
            checkBox6.setEnabled(false);
            checkBox7.setEnabled(false);
            checkBox8.setEnabled(false);
            checkBox9.setEnabled(false);
        }
    }

    private void isAllChecked() {
        if (ConflictOfInterestActivity.isSubmitted) {
            Arrays.fill(isChecked, true);
            conflictOfInterestActivity.page2ButtonActivated(true);
            return;
        }

        for (int i = 0; i < isChecked.length; i++) {
            if (!isChecked[i]) {
                conflictOfInterestActivity.page2ButtonActivated(false);
                conflictOfInterestActivity.submitButtonActivated(false);
                return;
            }
        }

        conflictOfInterestActivity.page2ButtonActivated(true);

        try {
            if ((!FragmentPage1.mempunyaiRB.isChecked() && !FragmentPage1.tidakMempunyaiRB.isChecked()) ||
                    (FragmentPage1.mempunyaiRB.isChecked() && FragmentPage1.mempunyaiET.getText().toString().isEmpty()) ||
                    FragmentPage1.lokasiET.getText().toString().isEmpty()) {
                conflictOfInterestActivity.submitButtonActivated(false);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            conflictOfInterestActivity.submitButtonActivated(false);
            return;
        }

        conflictOfInterestActivity.submitButtonActivated(true);
    }

}
