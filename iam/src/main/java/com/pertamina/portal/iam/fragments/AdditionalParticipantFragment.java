package com.pertamina.portal.iam.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.LeaveReqAddDestinationActivity;
import com.pertamina.portal.iam.activity.LeaveReqAddParticipantActivity;
import com.pertamina.portal.iam.activity.LeaveRequestActivity;
import com.pertamina.portal.iam.adapters.AdditionalParticipantAdapter;
import com.pertamina.portal.iam.models.AdditionalParticipantModel;

import java.util.ArrayList;
import java.util.List;

import static com.pertamina.portal.iam.activity.LeaveReqAddParticipantActivity.ADDITIONAL_PARTICIPANT_RESULT;
import static com.pertamina.portal.iam.activity.LeaveReqAddParticipantActivity.NAME;
import static com.pertamina.portal.iam.activity.LeaveReqAddParticipantActivity.NOTE;

public class AdditionalParticipantFragment extends Fragment {

    private View fab;
    private AdditionalParticipantAdapter additionalParticipantAdapter;
    public static List<AdditionalParticipantModel> additionalParticipantModels;
    private RecyclerView additionalParticipantRV;

    public AdditionalParticipantFragment() {
        // Required empty public constructor
    }

    public static AdditionalParticipantFragment newInstance(String param1, String param2) {
        AdditionalParticipantFragment fragment = new AdditionalParticipantFragment();
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
        return inflater.inflate(R.layout.fragment_additional_participant, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LeaveReqAddParticipantActivity.class);
                startActivityForResult(intent, 2200);
            }
        });

        additionalParticipantRV = view.findViewById(R.id.additionalParticipantRV);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2200 && resultCode == ADDITIONAL_PARTICIPANT_RESULT) {
            if (additionalParticipantModels == null) {
                additionalParticipantModels = new ArrayList<>();
            }

            AdditionalParticipantModel additionalParticipantModel = new AdditionalParticipantModel(
                    data.getStringExtra(NAME),
                    data.getStringExtra(NOTE)
            );

            additionalParticipantModels.add(additionalParticipantModel);
            onUpdateItem();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        additionalParticipantModels = new ArrayList<>();
    }

    public void onUpdateItem() {
        additionalParticipantAdapter = new AdditionalParticipantAdapter(getContext(), additionalParticipantModels, true);
        additionalParticipantRV.setLayoutManager(new LinearLayoutManager(getContext()));
        additionalParticipantRV.setAdapter(additionalParticipantAdapter);

        LeaveRequestActivity.lvReqFinalLeaveDetailFragment.showAdditionalParticipant();
    }
}
