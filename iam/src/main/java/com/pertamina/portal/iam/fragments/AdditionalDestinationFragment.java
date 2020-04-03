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
import com.pertamina.portal.iam.activity.LeaveRequestActivity;
import com.pertamina.portal.iam.adapters.AdditionalDestinationAdapter;
import com.pertamina.portal.iam.models.AdditionalDestinationModel;

import java.util.ArrayList;
import java.util.List;

import static com.pertamina.portal.iam.activity.LeaveReqAddDestinationActivity.ADDITIONAL_DESTINATION_RESULT;
import static com.pertamina.portal.iam.activity.LeaveReqAddDestinationActivity.CITY;
import static com.pertamina.portal.iam.activity.LeaveReqAddDestinationActivity.COUNTRY;
import static com.pertamina.portal.iam.activity.LeaveReqAddDestinationActivity.COUNTRY_ID;
import static com.pertamina.portal.iam.activity.LeaveReqAddDestinationActivity.END_DATE;
import static com.pertamina.portal.iam.activity.LeaveReqAddDestinationActivity.NOTE;
import static com.pertamina.portal.iam.activity.LeaveReqAddDestinationActivity.START_DATE;

public class AdditionalDestinationFragment extends Fragment {

    public static List<AdditionalDestinationModel> additionalDestinationModelList;
    private AdditionalDestinationAdapter additionalDestinationAdapter;
    private RecyclerView additionalDestinationRV;

    public AdditionalDestinationFragment() {
        // Required empty public constructor
    }

    public static AdditionalDestinationFragment newInstance(String param1, String param2) {
        AdditionalDestinationFragment fragment = new AdditionalDestinationFragment();
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
        return inflater.inflate(R.layout.fragment_additional_destination, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LeaveReqAddDestinationActivity.class);
                startActivityForResult(intent, 1100);
            }
        });

        additionalDestinationRV = view.findViewById(R.id.additionalDestinationRV);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1100 && resultCode == ADDITIONAL_DESTINATION_RESULT) {
            if (additionalDestinationModelList == null) {
                additionalDestinationModelList = new ArrayList<>();
            }
            AdditionalDestinationModel additionalDestinationModel = new AdditionalDestinationModel(
                    data.getStringExtra(CITY),
                    data.getStringExtra(COUNTRY_ID),
                    data.getStringExtra(COUNTRY),
                    data.getStringExtra(START_DATE),
                    data.getStringExtra(END_DATE),
                    data.getStringExtra(NOTE)
            );
            additionalDestinationModelList.add(additionalDestinationModel);
            onUpdateItem();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        additionalDestinationModelList = new ArrayList<>();
    }

    public void onUpdateItem() {
        additionalDestinationAdapter = new AdditionalDestinationAdapter(getContext(), additionalDestinationModelList, true);
        additionalDestinationRV.setLayoutManager(new LinearLayoutManager(getContext()));
        additionalDestinationRV.setAdapter(additionalDestinationAdapter);

        LeaveRequestActivity.lvReqFinalLeaveDetailFragment.showAdditionalDestination();
    }
}
