package com.pertamina.portal.iam.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.pertamina.portal.iam.R;

public class EditDestination2Fragment extends Fragment {

    public EditDestination2Fragment() {
        // Required empty public constructor
    }

    public static EditDestination2Fragment newInstance(String param1, String param2) {
        EditDestination2Fragment fragment = new EditDestination2Fragment();
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

        return inflater.inflate(R.layout.activity_add_destination2, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
