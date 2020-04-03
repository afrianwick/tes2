package com.pertamina.portal.iam.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.RmjDetailActivity;

public class JabatanLamaFragment extends Fragment {

    private TextView prlBSTV, tmtPRLBSTV, tmtAktifTV, tmtAdminTV, tipeMutasiTV, keteranganMutasiTV, idPosisiTV;
    private TextView namaJabatanTV, prlJabatanTV, workScheduleTV, costCenterTV, tmtJabatanTV, personalAreaTV, personalSubAreaTV;
    private TextView departemenTV, divisiTV, companyNameTV, companyCodeTV, direktoratTV, fungsiTV;
    private LinearLayout profilePekerjaLL, tipeMutasiLL, organisasiPekerjaLL;
    private LinearLayout profilePekerjaContainerLL, tipeMutasiContainerLL, organisasiPekerjaContainerLL;
    private ImageView profilePekerjaIV, tipeMutasiIV, organisasiPekerjaIV;

    public JabatanLamaFragment() {
        // Required empty public constructor
    }

    public static JabatanLamaFragment newInstance(String param1, String param2) {
        JabatanLamaFragment fragment = new JabatanLamaFragment();
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
        return inflater.inflate(R.layout.fragment_jabatan_baru, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prlBSTV = view.findViewById(R.id.prlBSTV);
        tmtPRLBSTV = view.findViewById(R.id.tmtPRLBSTV);
        tmtAktifTV = view.findViewById(R.id.tmtAktifTV);
        tmtAdminTV = view.findViewById(R.id.tmtAdminTV);
        tipeMutasiTV = view.findViewById(R.id.tipeMutasiTV);
        keteranganMutasiTV = view.findViewById(R.id.keteranganMutasiTV);
        idPosisiTV = view.findViewById(R.id.idPosisiTV);
        namaJabatanTV = view.findViewById(R.id.namaJabatanTV);
        prlJabatanTV = view.findViewById(R.id.prlJabatanTV);
        workScheduleTV = view.findViewById(R.id.workScheduleTV);
        costCenterTV = view.findViewById(R.id.costCenterTV);
        tmtJabatanTV = view.findViewById(R.id.tmtJabatanTV);
        personalAreaTV = view.findViewById(R.id.personalAreaTV);
        personalSubAreaTV = view.findViewById(R.id.personalSubAreaTV);
        departemenTV = view.findViewById(R.id.departemenTV);
        divisiTV = view.findViewById(R.id.divisiTV);
        direktoratTV = view.findViewById(R.id.direktoratTV);
        fungsiTV = view.findViewById(R.id.fungsiTV);
        companyNameTV = view.findViewById(R.id.companyNameTV);
        companyCodeTV = view.findViewById(R.id.companyCodeTV);


        prlBSTV.setText(String.valueOf(RmjDetailActivity.data.prlBsBaru));
        tmtPRLBSTV.setText(RmjDetailActivity.data.TMT_PRL_BS);
        tmtAktifTV.setText(RmjDetailActivity.data.TMT_ADMIN_BEGDA);
        tmtAdminTV.setText(RmjDetailActivity.data.TMT_ADMIN_BEGDA);
        tipeMutasiTV.setText(RmjDetailActivity.data.TYPE_MUTASI);
        keteranganMutasiTV.setText(RmjDetailActivity.data.KETERANGAN_MUTASI);
        idPosisiTV.setText(RmjDetailActivity.data.ID_POSISI_TUJUAN);
        namaJabatanTV.setText(RmjDetailActivity.data.PPLTXTM_NEW);
        prlJabatanTV.setText(RmjDetailActivity.data.PRL_NEW);
        personalAreaTV.setText(RmjDetailActivity.data.PPERSAM_NEW);
        personalSubAreaTV.setText(RmjDetailActivity.data.PBTRTLM_NEW);
        departemenTV.setText(RmjDetailActivity.data.PDEPTXM_NEW);
        divisiTV.setText(RmjDetailActivity.data.PDIVTXM_NEW);
        fungsiTV.setText(RmjDetailActivity.data.PFUNTXM_NEW);
        direktoratTV.setText(RmjDetailActivity.data.PDIRTXM_NEW);
        companyCodeTV.setText(RmjDetailActivity.data.PBUKRSM_NEW);
        companyNameTV.setText(RmjDetailActivity.data.PBUTXTM_NEW);

        profilePekerjaIV = view.findViewById(R.id.profilePekerjaIV);
        tipeMutasiIV = view.findViewById(R.id.tipeMutasiIV);
        organisasiPekerjaIV = view.findViewById(R.id.organisasiPekerjaIV);

        profilePekerjaLL = view.findViewById(R.id.profilePekerjaLL);
        tipeMutasiLL = view.findViewById(R.id.tipeMutasiLL);
        organisasiPekerjaLL = view.findViewById(R.id.organisasiPekerjaLL);

        profilePekerjaContainerLL = view.findViewById(R.id.llProfilePekerja);
        tipeMutasiContainerLL = view.findViewById(R.id.llTipeMutasi);
        organisasiPekerjaContainerLL = view.findViewById(R.id.llOrganisasiPekerja);

        profilePekerjaLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (profilePekerjaContainerLL.isShown()) {
                    profilePekerjaContainerLL.setVisibility(View.GONE);
                    profilePekerjaIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    profilePekerjaContainerLL.setVisibility(View.VISIBLE);
                    profilePekerjaIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });

        tipeMutasiLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tipeMutasiContainerLL.isShown()) {
                    tipeMutasiContainerLL.setVisibility(View.GONE);
                    tipeMutasiIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    tipeMutasiContainerLL.setVisibility(View.VISIBLE);
                    tipeMutasiIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });

        organisasiPekerjaLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (organisasiPekerjaContainerLL.isShown()) {
                    organisasiPekerjaContainerLL.setVisibility(View.GONE);
                    organisasiPekerjaIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    organisasiPekerjaContainerLL.setVisibility(View.VISIBLE);
                    organisasiPekerjaIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
