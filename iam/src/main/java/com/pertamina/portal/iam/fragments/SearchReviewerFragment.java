package com.pertamina.portal.iam.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.pertamina.portal.iam.R;

public class SearchReviewerFragment extends Fragment {

    public SearchReviewerFragment() {
        // Required empty public constructor
    }

    public static SearchReviewerFragment newInstance(String param1, String param2) {
        SearchReviewerFragment fragment = new SearchReviewerFragment();
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
        return inflater.inflate(R.layout.activity_search_reviewer, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
