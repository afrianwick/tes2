package com.pertamina.portal.iam.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.adapters.TaskAdapter;
import com.pertamina.portal.iam.fragments.dummy.DummyWorkList;
import com.pertamina.portal.iam.models.Task;

public class RecentTaskFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;

    // Mandatory constructor
    public RecentTaskFragment() {
    }

    public static RecentTaskFragment newInstance() {
        RecentTaskFragment fragment = new RecentTaskFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            TaskAdapter adapter = new TaskAdapter(getActivity(), DummyWorkList.ITEMS, mListener);
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Task item);
    }
}
