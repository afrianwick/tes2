package com.pertamina.portal.iam.fragments.CodeOfConduct;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pertamina.portal.iam.BuildConfig;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.CodeOfConductActivity;
import com.pertamina.portal.iam.utils.ErrorMessage;

import java.util.Arrays;

public class FragmentUjiPemahaman extends Fragment {

    public static String[] correctAnswersProd = {"5", "11", "15"};
    public static String[] correctAnswersDEV = {"5", "11", "15"};
    public static String[] selectedAnswer = new String[3];

    public static RadioGroup radioGroup1, radioGroup2, radioGroup3;
    private RadioButton rb1Salah, rb2Salah, rb3Salah, rb4Salah, rb5Salah, rb6Salah;
    private RadioButton rb7Salah, rb8Salah, rb9Salah, rb10Salah, rb11Salah, rb12Salah;
    private RadioButton rb1Benar, rb2Benar, rb3Benar;
    private boolean isChecked1 = false, isChecked2 = false, isChecked3 = false;

    public FragmentUjiPemahaman() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conduct_uji_pemahaman, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final CodeOfConductActivity codeOfConductActivity = (CodeOfConductActivity) getActivity();
        radioGroup1 = view.findViewById(R.id.coc1RG);
        radioGroup2 = view.findViewById(R.id.coc2RG);
        radioGroup3 = view.findViewById(R.id.coc3RG);

        rb1Benar = view.findViewById(R.id.coc1RB);
        rb2Benar = view.findViewById(R.id.coc2RB);
        rb3Benar = view.findViewById(R.id.coc3RB);
        rb1Salah = view.findViewById(R.id.coc1SalahRB);
        rb2Salah = view.findViewById(R.id.coc2SalahRB);
        rb3Salah = view.findViewById(R.id.coc3SalahRB);
        rb4Salah = view.findViewById(R.id.coc4SalahRB);
        rb5Salah = view.findViewById(R.id.coc5SalahRB);
        rb6Salah = view.findViewById(R.id.coc6SalahRB);
        rb7Salah = view.findViewById(R.id.coc7SalahRB);
        rb8Salah = view.findViewById(R.id.coc8SalahRB);
        rb9Salah = view.findViewById(R.id.coc9SalahRB);
        rb10Salah = view.findViewById(R.id.coc10SalahRB);
        rb11Salah = view.findViewById(R.id.coc11SalahRB);
        rb12Salah = view.findViewById(R.id.coc12SalahRB);

        if (CodeOfConductActivity.isCOC) {
            radioGroup1.check(R.id.coc1RB);
            radioGroup2.check(R.id.coc2RB);
            radioGroup3.check(R.id.coc3RB);
        }

        Arrays.fill(selectedAnswer, "-1");

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.d("jawaban1", String.valueOf(i));
                Log.d("jawaban1", String.valueOf(radioGroup.getCheckedRadioButtonId()));
                if (radioGroup.getCheckedRadioButtonId() == view.findViewById(R.id.coc1RB).getId()) {
                    if (BuildConfig.BUILD_TYPE == "debug") {
                        selectedAnswer[0] = correctAnswersDEV[0];
                    } else {
                        selectedAnswer[0] = correctAnswersProd[0];
                    }
                } else {
                    selectedAnswer[0] = "0";
                }

                isChecked1 = true;
                if (isChecked1 && isChecked2 && isChecked3) {
                    codeOfConductActivity.submitActivatedButton(true);
                }
            }
        });

        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.d("jawaban2", String.valueOf(i));
                Log.d("jawaban2", String.valueOf(radioGroup.getCheckedRadioButtonId()));
                if (radioGroup.getCheckedRadioButtonId() == view.findViewById(R.id.coc2RB).getId()) {
                    if (BuildConfig.BUILD_TYPE == "debug") {
                        selectedAnswer[1] = correctAnswersDEV[1];
                    } else {
                        selectedAnswer[1] = correctAnswersProd[1];
                    }
                } else {
                    selectedAnswer[1] = "0";
                }

                isChecked2 = true;
                if (isChecked1 && isChecked2 && isChecked3) {
                    codeOfConductActivity.submitActivatedButton(true);
                }
            }
        });

        radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.d("jawaban3", String.valueOf(i));
                Log.d("jawaban3", String.valueOf(radioGroup.getCheckedRadioButtonId()));
                if (radioGroup.getCheckedRadioButtonId() == view.findViewById(R.id.coc3RB).getId()) {
                    if (BuildConfig.BUILD_TYPE == "debug") {
                        selectedAnswer[2] = correctAnswersDEV[2];
                    } else {
                        selectedAnswer[2] = correctAnswersProd[2];
                    }
                } else {
                    selectedAnswer[2] = "0";
                }

                isChecked3 = true;
                if (isChecked1 && isChecked2 && isChecked3) {
                    codeOfConductActivity.submitActivatedButton(true);
                }
            }
        });

        if (CodeOfConductActivity.isCOC) {
            radioGroup1.setEnabled(false);
            radioGroup2.setEnabled(false);
            radioGroup3.setEnabled(false);

            rb1Benar.setEnabled(false);
            rb2Benar.setEnabled(false);
            rb3Benar.setEnabled(false);

            rb1Salah.setEnabled(false);
            rb2Salah.setEnabled(false);
            rb3Salah.setEnabled(false);
            rb4Salah.setEnabled(false);
            rb5Salah.setEnabled(false);
            rb6Salah.setEnabled(false);
            rb7Salah.setEnabled(false);
            rb8Salah.setEnabled(false);
            rb9Salah.setEnabled(false);
            rb10Salah.setEnabled(false);
            rb11Salah.setEnabled(false);
            rb12Salah.setEnabled(false);
        }
    }
}
