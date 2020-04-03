package com.pertamina.portal.iam.fragments.ConflictOfInterest;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.PrefUtils;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.ConflictOfInterestActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FragmentPage1 extends Fragment {

    private TextView nameTV, jabatanTV, nopekTV;
    private TextView jabatan2TV, name2TV;

    public static RadioButton tidakMempunyaiRB, mempunyaiRB;
    private TextView tidakMempunyaiTV, mempunyaiTV, tanggalTV, ubahKepentinganTV;
    public static EditText mempunyaiET, lokasiET;
    private RadioGroup radioGroup;
    private Calendar calendar = Calendar.getInstance();

    public FragmentPage1() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fargment_conflict_of_interest_1, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ConflictOfInterestActivity conflictOfInterestActivity = (ConflictOfInterestActivity) getActivity();

        nameTV = view.findViewById(R.id.coiNameTV);
        name2TV = view.findViewById(R.id.coiName2TV);
        nopekTV = view.findViewById(R.id.coiNopekTV);
        jabatanTV = view.findViewById(R.id.coiJabatanTV);
        jabatan2TV = view.findViewById(R.id.coiJabatan2TV);
        tidakMempunyaiRB = view.findViewById(R.id.coiTidakMempunyaiRB);
        mempunyaiRB = view.findViewById(R.id.coiMempunyaiRB);
        tidakMempunyaiTV = view.findViewById(R.id.coiTidakMempunyaiTV);
        mempunyaiTV = view.findViewById(R.id.coiMempunyaiTV);
        mempunyaiET = view.findViewById(R.id.coiMempunyaiET);
        lokasiET = view.findViewById(R.id.coiLokasiET);
        radioGroup = view.findViewById(R.id.coiRadionGroup);
        tanggalTV = view.findViewById(R.id.coiDateTV);
        ubahKepentinganTV = view.findViewById(R.id.coiFormUbahKepentinganTV);

        nameTV.setText(PrefUtils.Build(getActivity()).getPref().getString(Constants.KEY_PCNAMEM, ""));
        jabatanTV.setText(PrefUtils.Build(getActivity()).getPref().getString(Constants.KEY_PPLTXTM, ""));
        name2TV.setText(PrefUtils.Build(getActivity()).getPref().getString(Constants.KEY_PCNAMEM, ""));
        jabatan2TV.setText(PrefUtils.Build(getActivity()).getPref().getString(Constants.KEY_PPLTXTM, ""));
        nopekTV.setText(PrefUtils.Build(getActivity()).getPref().getString(Constants.KEY_PPERNRM, ""));

        mempunyaiTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mempunyaiRB.setChecked(true);
            }
        });

        tidakMempunyaiTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tidakMempunyaiRB.setChecked(true);
            }
        });

        tidakMempunyaiRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (!ConflictOfInterestActivity.isSubmitted) {
                        conflictOfInterestActivity.submitButtonActivated(true);
                    } else {
                        conflictOfInterestActivity.submitButtonActivated(false);
                    }
                    tidakMempunyaiTV.setText(getResources().getString(R.string.coi_tidak_mempunyai));
                    mempunyaiTV.setText("Mempunyai");
                    mempunyaiRB.setChecked(!b);
                } else {
                    tidakMempunyaiTV.setText("Tidak Mempunyai");
                }
            }
        });

        mempunyaiRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {
                if (b) {
                    mempunyaiTV.setText(getResources().getString(R.string.coi_mempunyai));
                    tidakMempunyaiTV.setText("Tidak Mempunyai");
                    mempunyaiET.setVisibility(View.VISIBLE);
                    tidakMempunyaiRB.setChecked(!b);

                    mempunyaiET.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if (b && charSequence.length() > 0) {
                                conflictOfInterestActivity.submitButtonActivated(true);
                            }

                            if (charSequence.length() == 0) {
                                conflictOfInterestActivity.submitButtonActivated(false);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                } else {
                    mempunyaiTV.setText("Mempunyai");
                    mempunyaiET.setVisibility(View.GONE);
                }
            }
        });

        if (!ConflictOfInterestActivity.isCOI) {
            tanggalTV.setText(new SimpleDateFormat(", dd MMMM yyyy", ConflictOfInterestActivity.id).format(calendar.getTime()));
        } else {
            tanggalTV.setText(", "+ConflictOfInterestActivity.tanggalResponse);
            lokasiET.setText(ConflictOfInterestActivity.lokasiResponse);
            if (ConflictOfInterestActivity.statusResponse.equalsIgnoreCase("Ada")) {
                mempunyaiRB.setChecked(true);
                mempunyaiET.setVisibility(View.VISIBLE);
                mempunyaiET.setText(ConflictOfInterestActivity.isiResponse);
                tidakMempunyaiTV.setVisibility(View.GONE);
            } else {
                tidakMempunyaiRB.setChecked(true);
                ubahKepentinganTV.setVisibility(View.VISIBLE);
                ubahKepentinganTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ConflictOfInterestActivity conflictOfInterestActivity = (ConflictOfInterestActivity) getActivity();
                        conflictOfInterestActivity.ubahKepentingan();
                    }
                });
                mempunyaiTV.setVisibility(View.GONE);
            }
            tidakMempunyaiRB.setVisibility(View.GONE);
            mempunyaiRB.setVisibility(View.GONE);
        }

        if (ConflictOfInterestActivity.isSubmitted) {
            lokasiET.setEnabled(false);
        }
    }
}
