package com.pertamina.portal.iam.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.core.fragments.BaseFragmentApi;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.adapters.ListCommentAdapter;
import com.pertamina.portal.iam.models.IamComment;

import java.util.ArrayList;

public class WorklistCommentFragment extends BaseFragmentApi {
    private static final String ARG_COMMENTS = "comments";
    private static final String TAG = "WorklistCommentFragment";
    private RecyclerView rvComment;
    private ArrayList<IamComment> list;
    private LinearLayout headerLL;
    private ImageView expandCollapseIV;


    public WorklistCommentFragment() {
        // Required empty public constructor
    }

    public static WorklistCommentFragment newInstance(ArrayList<IamComment> listComment) {
        WorklistCommentFragment fragment = new WorklistCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARG_COMMENTS, listComment);
        fragment.setArguments(bundle);
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
        return inflater.inflate(R.layout.fragment_comment_worklist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        headerLL = view.findViewById(R.id.llSectionSeparatorComments);

        expandCollapseIV = view.findViewById(R.id.commentExpandCollapseIV);

        headerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rvComment.isShown()) {
                    rvComment.setVisibility(View.GONE);
                    expandCollapseIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand));
                } else {
                    rvComment.setVisibility(View.VISIBLE);
                    expandCollapseIV.setImageDrawable(getResources().getDrawable(R.drawable.ic_collapse));
                }
            }
        });

        rvComment = (RecyclerView) view.findViewById(R.id.rvComment);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());

        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvComment.setLayoutManager(llm);

        list = getArguments().getParcelableArrayList(ARG_COMMENTS);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        ListCommentAdapter adapter = new ListCommentAdapter(getActivity(), list);
        rvComment.setAdapter(adapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
