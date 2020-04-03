package com.pertamina.portal.iam.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.RmjDetailActivity;

public class JabatanBaruFragment extends Fragment {

    private TextView personIDTV, personNumberTV, namaPekerjaTV, tglLahirTV, tmtJabatanTV, prlBSTV, tmtPRLBSTV, usiaTV, tglAktifTV, tglPensiunTV, kinerjaTV;
    private TextView idPosisiTV, namaJabatanTV, prlJabatanTV, personalAreaTV, personalSubAreaTV, departemenTV, divisiTV, fungsiTV, direktoratTV, companyNameTV, companyCodeTV;

    private LinearLayout profilePekerjaLL, organisasiPekerjaLL;
    private LinearLayout llProfilePekerja, llOrganisasiPekerjaLL;

    private ImageView profilePekerjaIV, organisasiPekerjaIV;

    public JabatanBaruFragment() {
        // Required empty public constructor
    }

    public static JabatanBaruFragment newInstance(String param1, String param2) {
        JabatanBaruFragment fragment = new JabatanBaruFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_jabatan_lama, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        personIDTV = view.findViewById(R.id.personIDTV);
        personNumberTV = view.findViewById(R.id.personNumberTV);
        namaPekerjaTV = view.findViewById(R.id.namaPekerjaTV);
        tglLahirTV = view.findViewById(R.id.tglLahirTV);
        tmtJabatanTV = view.findViewById(R.id.tmtJabatanTV);
        prlBSTV = view.findViewById(R.id.prlBSTV);
        tmtPRLBSTV = view.findViewById(R.id.tmtPRLBSTV);
        usiaTV = view.findViewById(R.id.usiaTV);
        tglAktifTV = view.findViewById(R.id.tglAktifTV);
        tglPensiunTV = view.findViewById(R.id.tglPensiunTV);
        kinerjaTV = view.findViewById(R.id.kinerjaTV);
        idPosisiTV = view.findViewById(R.id.idPosisiTV);
        namaJabatanTV = view.findViewById(R.id.namaJabatanTV);
        prlJabatanTV = view.findViewById(R.id.prlJabatanTV);
        personalAreaTV = view.findViewById(R.id.personalAreaTV);
        personalSubAreaTV = view.findViewById(R.id.personalSubAreaTV);
        departemenTV = view.findViewById(R.id.departemenTV);
        divisiTV = view.findViewById(R.id.divisiTV);
        fungsiTV = view.findViewById(R.id.fungsiTV);
        direktoratTV = view.findViewById(R.id.direktoratTV);
        companyNameTV = view.findViewById(R.id.companyNameTV);
        companyCodeTV = view.findViewById(R.id.companyCodeTV);

        personIDTV.setText(RmjDetailActivity.data.PersonID);
        personNumberTV.setText(RmjDetailActivity.data.NOPEK);
        namaPekerjaTV.setText(RmjDetailActivity.data.PCNAMEM);
        tglLahirTV.setText(RmjDetailActivity.data.PGBDATM);
        tmtJabatanTV.setText(RmjDetailActivity.data.TMT_JABATAN);
        prlBSTV.setText(RmjDetailActivity.data.TMT_PRL_BS_OLD);
        usiaTV.setText(RmjDetailActivity.data.AGE);
        tglAktifTV.setText(RmjDetailActivity.data.HIRING_DATE);
        tglPensiunTV.setText(RmjDetailActivity.data.TMT_PENSIUN);
        kinerjaTV.setText(RmjDetailActivity.data.FIRSTSCORE + " - " + RmjDetailActivity.data.SECONDSCORE);
        idPosisiTV.setText(RmjDetailActivity.data.PPLANSM);
        namaJabatanTV.setText(RmjDetailActivity.data.PPLTXTM);
        prlJabatanTV.setText(RmjDetailActivity.data.PRL);
        personalAreaTV.setText(RmjDetailActivity.data.PPERSAM + " - " + RmjDetailActivity.data.PPERTXM);
        personalSubAreaTV.setText(RmjDetailActivity.data.PBTRTLM + " - " + RmjDetailActivity.data.PBTEXTM);
        departemenTV.setText(RmjDetailActivity.data.PDEPTXM);
        divisiTV.setText(RmjDetailActivity.data.PDIVTXM);
        fungsiTV.setText(RmjDetailActivity.data.PFUNTXM);
        direktoratTV.setText(RmjDetailActivity.data.PDIRTXM);
        companyNameTV.setText(RmjDetailActivity.data.PBUTXTM);
        companyCodeTV.setText(RmjDetailActivity.data.PBUKRSM);

        llOrganisasiPekerjaLL = view.findViewById(R.id.llOrganisasiPekerja);
        llProfilePekerja = view.findViewById(R.id.llProfilePekerja);

        organisasiPekerjaLL = view.findViewById(R.id.organisasiPekerjaLL);
        profilePekerjaLL = view.findViewById(R.id.profilePekerjaLL);

        organisasiPekerjaIV = view.findViewById(R.id.organisasiPekerjaIV);
        profilePekerjaIV = view.findViewById(R.id.profilePekerjaIV);

        llOrganisasiPekerjaLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (organisasiPekerjaLL.isShown()) {
                    organisasiPekerjaLL.setVisibility(View.GONE);
                    organisasiPekerjaIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    organisasiPekerjaLL.setVisibility(View.VISIBLE);
                    organisasiPekerjaIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });

        llProfilePekerja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (profilePekerjaLL.isShown()) {
                    profilePekerjaLL.setVisibility(View.GONE);
                    profilePekerjaIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    profilePekerjaLL.setVisibility(View.VISIBLE);
                    profilePekerjaIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
