package com.pertamina.portal.iam.fragments.ConflictOfInterest;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.PrefUtils;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.ConflictOfInterestActivity;

public class FragmentForm extends Fragment {

    private TextView namaTV, jabatanTV, nopekTV, nama2TV, jabatan2TV, tanggalTV;
    public static EditText mempunyaiET, lokasiET;

    public FragmentForm() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fargment_conflict_of_interest_3, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ConflictOfInterestActivity conflictOfInterestActivity = (ConflictOfInterestActivity) getActivity();

        namaTV = view.findViewById(R.id.coiFormNameTV);
        nama2TV = view.findViewById(R.id.coiFormName2TV);
        jabatanTV = view.findViewById(R.id.coiFormJabatanTV);
        jabatan2TV = view.findViewById(R.id.coiFormJabatan2TV);
        nopekTV = view.findViewById(R.id.coiFormNopekTV);
        tanggalTV = view.findViewById(R.id.coiFormTanggalTV);
        mempunyaiET = view.findViewById(R.id.coiFormMempunyaiET);
        lokasiET = view.findViewById(R.id.coiFormLokasiET);

        namaTV.setText(PrefUtils.Build(getActivity()).getPref().getString(Constants.KEY_PCNAMEM, ""));
        jabatanTV.setText(PrefUtils.Build(getActivity()).getPref().getString(Constants.KEY_PPLTXTM, ""));
        nama2TV.setText(PrefUtils.Build(getActivity()).getPref().getString(Constants.KEY_PCNAMEM, ""));
        jabatan2TV.setText(PrefUtils.Build(getActivity()).getPref().getString(Constants.KEY_PPLTXTM, ""));
        nopekTV.setText(PrefUtils.Build(getActivity()).getPref().getString(Constants.KEY_PPERNRM, ""));
        tanggalTV.setText(", "+ ConflictOfInterestActivity.tanggalResponse);
        lokasiET.setText(ConflictOfInterestActivity.lokasiResponse);

        lokasiET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0 && !mempunyaiET.getText().toString().isEmpty()) {
                    conflictOfInterestActivity.submitButtonActivated(true);
                } else {
                    conflictOfInterestActivity.submitButtonActivated(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mempunyaiET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0 && !lokasiET.getText().toString().isEmpty()) {
                    conflictOfInterestActivity.submitButtonActivated(true);
                } else {
                    conflictOfInterestActivity.submitButtonActivated(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
