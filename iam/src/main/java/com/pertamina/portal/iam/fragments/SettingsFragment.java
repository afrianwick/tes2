package com.pertamina.portal.iam.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.adapters.TaskAdapter;
import com.pertamina.portal.iam.fragments.dummy.DummyWorkList;
import com.pertamina.portal.iam.models.Task;

public class SettingsFragment extends Fragment {

    // Mandatory constructor
    public SettingsFragment() {
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        View backToPortal = view.findViewById(R.id.llBackToPortal);
        View llAboutIam = view.findViewById(R.id.llAboutIam);
        View llTerm = view.findViewById(R.id.llTerm);

        backToPortal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(getActivity(),
                            Class.forName("com.pertamina.portal.activity.PortalHomeActivity"));

                    startActivity(intent);
                    getActivity().finish();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        llAboutIam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Go to About Activity
            }
        });

        llTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Go to Term
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
