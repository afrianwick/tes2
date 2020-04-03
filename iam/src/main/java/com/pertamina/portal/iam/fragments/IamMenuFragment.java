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
import com.pertamina.portal.iam.activity.MyCalendarActivity;
import com.pertamina.portal.iam.activity.MyComplianceActivity;
import com.pertamina.portal.iam.activity.MyDocumentActivity;
import com.pertamina.portal.iam.activity.MyPersonalActivity;
import com.pertamina.portal.iam.activity.MyWorklistActivity;
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

public class IamMenuFragment extends Fragment {
    private static final String TAG = "IamMenuFragment";
    private OnFragmentInteractionListener mListener;
    private String counter;
    private AlertDialog loading;
    private GridView gv;
    private AlertDialog alertDialog;

    public IamMenuFragment() {
        // Required empty public constructor
    }

    public static IamMenuFragment newInstance(String param1, String param2) {
        IamMenuFragment fragment = new IamMenuFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_iam_menu, container, false);
        ButterKnife.bind(getContext(), view);

        gv = (GridView) view.findViewById(R.id.gridview);

        buildAlert();

        loading = new SpotsDialog.Builder().setContext(getContext()).build();

        menuSetup();

        getAllWorklist();

        return view;
    }

    private void menuSetup() {
        List<IamMenu> menus = new ArrayList<>();

        IamMenu m1 = new IamMenu();
        m1.title = "My Personal";
        m1.counter = null;
        m1.iconRespurce = R.drawable.ic_mypersonal_white;
        m1.intent = new Intent(getActivity(), MyPersonalActivity.class);

        IamMenu m2 = new IamMenu();
        m2.title = "My Calendar";
        m2.counter = null;
        m2.iconRespurce = R.drawable.ic_calendar_png;
        m2.intent = new Intent(getActivity(), MyCalendarActivity.class);

        IamMenu m3 = new IamMenu();
        m3.title = "My Worklist";
        m3.counter = counter;
        m3.iconRespurce = R.drawable.ic_worklist_png;
        m3.intent = new Intent(getActivity(), MyWorklistActivity.class);

        IamMenu m4 = new IamMenu();
        m4.title = "My Compliance";
        m4.counter = null;
        m4.iconRespurce = R.drawable.ic_compliance_png;
        m4.intent = new Intent(getActivity(), MyComplianceActivity.class);

        IamMenu m5 = new IamMenu();
        m5.title = "My Document";
        m5.counter = null;
        m5.iconRespurce = R.drawable.ic_mydocument_png;
        m5.intent = new Intent(getActivity(), MyDocumentActivity.class);

        menus.add(m1);
        menus.add(m2);
        menus.add(m3);
        menus.add(m4);
        menus.add(m5);

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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    private void getAllWorklist() {
        loading.show();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(getContext(), 2000);
        restApi.getWorklist("*", "2", "K2Services", "GetWorklist")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d(TAG, "getAllWorklist, sip.. " + strResponse);
                                XMLParserUtils.parseXml(strResponse, new XMLParserInterface() {
                                    @Override
                                    public void onSuccess(String result) {
                                        parseJson(result);
                                    }

                                    @Override
                                    public void onFailure(NodeList nodeListError) {

                                    }

                                    @Override
                                    public void onSuccessReturnMessage(NodeList nodeListError) {

                                    }
                                });
//                                parseXml(strResponse);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ResponseErrorHandling.handleError(response, alertDialog, getActivity());
                            Log.d(TAG, "getAllWorklist, false _ " + response.raw().toString());
                        }

                        loading.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        Log.d(TAG, "getAllWorklist, onFailure..");
                        t.printStackTrace();
                    }
                });
    }

    private void parseJson(String strJson) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);
        JsonArray jsoArray = jsonObject.getAsJsonArray("records");
        final List<Task> taskList = new ArrayList<>();
        final List<TaskPending> taskListPending = new ArrayList<>();
        final List<TaskApproval> taskListApproved = new ArrayList<>();
        final List<TaskRejected> taskListRejected = new ArrayList<>();
        final List<TaskHistory> taskListHistory = new ArrayList<>();

        counter = String.valueOf(jsoArray.size());
        if (jsoArray.size() > 10) {
            counter = "10+";
        }

        menuSetup();
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
