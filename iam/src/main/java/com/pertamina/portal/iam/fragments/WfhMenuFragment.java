package com.pertamina.portal.iam.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.ConflictOfInterestActivity;
import com.pertamina.portal.iam.activity.LeaveRequestActivity;
import com.pertamina.portal.iam.activity.MyCalendarActivity;
import com.pertamina.portal.iam.activity.MyPersonalActivity;
import com.pertamina.portal.iam.activity.InboxActivity;
import com.pertamina.portal.iam.activity.MyWorklistActivity;
import com.pertamina.portal.iam.activity.ReqWorkActivity;
import com.pertamina.portal.iam.activity.WorkActivity;
import com.pertamina.portal.iam.adapters.MenuAdapter;
import com.pertamina.portal.iam.models.IamMenu;
import com.pertamina.portal.iam.models.Task;
import com.pertamina.portal.iam.models.TaskApproval;
import com.pertamina.portal.iam.models.TaskHistory;
import com.pertamina.portal.iam.models.TaskPending;
import com.pertamina.portal.iam.models.TaskRejected;
import com.pertamina.portal.iam.utils.ResponseErrorHandling;
import com.pertamina.portal.iam.utils.XMLParserUtils.XMLParserInterface;
import com.pertamina.portal.iam.utils.XMLParserUtils.XMLParserUtils;

import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WfhMenuFragment extends Fragment {
    private static final String TAG = "WfhMenuFragment";
    private OnFragmentInteractionListener mListener;
    private String counter;
    private AlertDialog loading;
    private GridView gv;
    private AlertDialog alertDialog;

    public WfhMenuFragment() {
        // Required empty public constructor
    }

    public static WfhMenuFragment newInstance(String param1, String param2) {
        WfhMenuFragment fragment = new WfhMenuFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wfh_menu, container, false);
        ButterKnife.bind(getContext(), view);

        gv = (GridView) view.findViewById(R.id.gridview);

        buildAlert();

        loading = new SpotsDialog.Builder().setContext(getContext()).build();

        menuSetup();


        return view;
    }

    private void menuSetup() {
        List<IamMenu> menus = new ArrayList<>();

        IamMenu m1 = new IamMenu();
        m1.title = "Edit Work List";
        m1.counter = null;
        m1.iconRespurce = R.drawable.ic_inbox;
        m1.intent = new Intent(getActivity(), InboxActivity.class);

        IamMenu m2 = new IamMenu();
        m2.title = "Input Work List";
        m2.counter = null;
        m2.iconRespurce = R.drawable.ic_disposisi;
        m2.intent = new Intent(getActivity(), ReqWorkActivity.class);

        IamMenu m3 = new IamMenu();
        m3.title = "My Worklist";
        m3.counter = counter;
        m3.iconRespurce = R.drawable.ic_worklist_png;
        m3.intent = new Intent(getActivity(), MyWorklistActivity.class);



        menus.add(m1);
        menus.add(m2);

        MenuAdapter adapter = new MenuAdapter(getActivity(), menus);

        gv.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WfhMenuFragment.OnFragmentInteractionListener) {
            mListener = (WfhMenuFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void buildAlert() {
        alertDialog = new AlertDialog.Builder(getContext())
                .setNeutralButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
    }
}
