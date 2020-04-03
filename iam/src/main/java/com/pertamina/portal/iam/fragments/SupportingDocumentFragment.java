package com.pertamina.portal.iam.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.LeaveReqAddDestination2Activity;
import com.pertamina.portal.iam.activity.LeaveReqAddParticipantActivity;

public class SupportingDocumentFragment extends Fragment {

    private View fab;

    public SupportingDocumentFragment() {
        // Required empty public constructor
    }

    public static SupportingDocumentFragment newInstance(String param1, String param2) {
        SupportingDocumentFragment fragment = new SupportingDocumentFragment();
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
        return inflater.inflate(R.layout.fragment_supporting_document, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LeaveReqAddDestination2Activity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
