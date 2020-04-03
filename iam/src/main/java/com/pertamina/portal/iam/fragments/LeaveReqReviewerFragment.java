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
import com.pertamina.portal.iam.activity.LeaveReqAddReviewerActivity;
import com.pertamina.portal.iam.activity.LeaveRequestActivity;
import com.pertamina.portal.iam.adapters.ReviewerAdapter;
import com.pertamina.portal.iam.models.ReviewerModel;

import java.util.ArrayList;
import java.util.List;

import static com.pertamina.portal.iam.activity.LeaveReqAddReviewerActivity.REVIEWER_AD_USERNAME;
import static com.pertamina.portal.iam.activity.LeaveReqAddReviewerActivity.REVIEWER_NAME;
import static com.pertamina.portal.iam.activity.LeaveReqAddReviewerActivity.REVIEWER_PERSONAL_NUMBER;
import static com.pertamina.portal.iam.activity.LeaveReqAddReviewerActivity.REVIEWER_POSITION;
import static com.pertamina.portal.iam.activity.LeaveReqAddReviewerActivity.REVIEWER_RESULT;

public class LeaveReqReviewerFragment extends Fragment {

    private ReviewerAdapter reviewerAdapter;
    private RecyclerView reviewerRV;
    public static List<ReviewerModel> reviewerModels;

    public LeaveReqReviewerFragment() {
        // Required empty public constructor
    }

    public static LeaveReqReviewerFragment newInstance(String param1, String param2) {
        LeaveReqReviewerFragment fragment = new LeaveReqReviewerFragment();
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
        return inflater.inflate(R.layout.fragment_leave_req_reviewer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LeaveReqAddReviewerActivity.class);
                startActivityForResult(intent, 3300);
            }
        });

        reviewerRV = view.findViewById(R.id.fragmentReviewerRV);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3300 && resultCode == REVIEWER_RESULT) {
            if (reviewerModels == null) {
                reviewerModels = new ArrayList<>();
            }

            ReviewerModel reviewerModel = new ReviewerModel(
                    data.getStringExtra(REVIEWER_NAME),
                    data.getStringExtra(REVIEWER_POSITION),
                    data.getStringExtra(REVIEWER_AD_USERNAME),
                    data.getStringExtra(REVIEWER_PERSONAL_NUMBER)
            );

            reviewerModels.add(reviewerModel);

            onUpdateItem();
        }
    }

    public void onUpdateItem() {
        reviewerAdapter = new ReviewerAdapter(reviewerModels, getContext(), true);
        reviewerRV.setLayoutManager(new LinearLayoutManager(getContext()));
        reviewerRV.setAdapter(reviewerAdapter);

        LeaveRequestActivity.lvReqFinalLeaveDetailFragment.showAdditionalReviewer();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        reviewerModels = new ArrayList<>();
    }
}
