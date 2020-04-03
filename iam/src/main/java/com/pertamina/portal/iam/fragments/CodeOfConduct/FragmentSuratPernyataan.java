package com.pertamina.portal.iam.fragments.CodeOfConduct;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.PrefUtils;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.CodeOfConductActivity;
import com.pertamina.portal.iam.activity.ConflictOfInterestActivity;
import com.pertamina.portal.iam.activity.WebViewA;
import com.pertamina.portal.iam.utils.DateUtils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FragmentSuratPernyataan extends Fragment {

    public static boolean isPedomanClicked = false;
    public static EditText lokasiET;
    public static TextView namaJabatanTV, tanggalTV;
    private Calendar calendar = Calendar.getInstance();
    private Button pedomanButton;

    public FragmentSuratPernyataan() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conduct_surat_pernyataan, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final CodeOfConductActivity codeOfConductActivity = (CodeOfConductActivity) getActivity();
        lokasiET = view.findViewById(R.id.cocLokasiET);
        namaJabatanTV = view.findViewById(R.id.cocNamaJabatanTV);
        tanggalTV = view.findViewById(R.id.cocTanggalTV);

        try {
            DateUtils dateUtils = new DateUtils();
            lokasiET.setText(CodeOfConductActivity.lokasiReponse);
            tanggalTV.setText(dateUtils.setInputDate(CodeOfConductActivity.tanggalResponse).setInputPattern("dd/MM/yyyy").setOutputPattern("dd MMMM yyyy").build());
        } catch (Exception e) {
            e.printStackTrace();
        }

        tanggalTV.setText(new SimpleDateFormat(", dd MMMM yyyy", ConflictOfInterestActivity.id).format(calendar.getTime()));
        namaJabatanTV.setText(PrefUtils.Build(getActivity()).getPref().getString(Constants.KEY_PCNAMEM, "") + ", " +
                PrefUtils.Build(getActivity()).getPref().getString(Constants.KEY_PPLTXTM, ""));

        pedomanButton = view.findViewById(R.id.btn_conduct_guide);

        pedomanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPedomanClicked = true;
                if (lokasiET.getText().toString().length() > 0) {
                    codeOfConductActivity.page2ActivatedButton(true);
                }
//                Intent intent = new Intent(getContext(), WebViewA.class);
//                startActivity(intent);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://apps.pertamina.com/sdmonline_iam/personal/COC_Read.aspx"));
                startActivity(browserIntent);
            }
        });

        if (CodeOfConductActivity.isCOC) {
            lokasiET.setEnabled(false);
        }


        lokasiET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    if (isPedomanClicked) {
                        codeOfConductActivity.page2ActivatedButton(true);
                        try {
                            for (int ii = 0; ii < FragmentUjiPemahaman.selectedAnswer.length; ii++) {
                                if (FragmentUjiPemahaman.selectedAnswer[ii] == "-1") {
                                    return;
                                }
                            }
                            Log.d("kenapasubmitactive", FragmentUjiPemahaman.selectedAnswer[0]);
                            codeOfConductActivity.submitActivatedButton(true);
                        } catch (Exception e) {
                            codeOfConductActivity.submitActivatedButton(false);
                            e.printStackTrace();
                        }
                    }
                } else {
                    codeOfConductActivity.page2ActivatedButton(false);
                    codeOfConductActivity.submitActivatedButton(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
