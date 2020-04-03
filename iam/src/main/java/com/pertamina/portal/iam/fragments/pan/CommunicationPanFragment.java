package com.pertamina.portal.iam.fragments.pan;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pertamina.portal.core.fragments.BaseFragmentApi;
import com.pertamina.portal.core.interfaces.OnSuccessListener;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.interfaces.LoadProfileListener;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunicationPanFragment extends BaseFragmentApi {
    private static final String ARG_PRSNL_NUM = "personal-number";
    private static final String TAG = "PersonalDataFragment";
    private LoadProfileListener loadProfileListener;
    private JsonArray jsonArr;
    private TextView tvPersonalNum;
    private TextView tvFullName, tvEmail;
    private TextView tvCompany;
    private TextView tvKbo;
    private TextView tvPersonelArea;
    private TextView tvPersonelSubArea;
    private TextView tvPosition;


    public CommunicationPanFragment() {
        // Required empty public constructor
    }

    public static CommunicationPanFragment newInstance(String personalNum) {
        CommunicationPanFragment fragment = new CommunicationPanFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PRSNL_NUM, personalNum);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setProfileListener(LoadProfileListener listener) {
        this.loadProfileListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personel_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvPersonalNum = (TextView) view.findViewById(R.id.tvPersonalNum);
        tvFullName = (TextView) view.findViewById(R.id.tvFullName);
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        tvCompany = (TextView) view.findViewById(R.id.tvCompany);
        tvKbo = (TextView) view.findViewById(R.id.tvKbo);
        tvPersonelArea = (TextView) view.findViewById(R.id.tvPersonelArea);
        tvPersonelSubArea = (TextView) view.findViewById(R.id.tvPersonelSubArea);
        tvPosition = (TextView) view.findViewById(R.id.tvPosition);

        String personalNum = getArguments().getString(ARG_PRSNL_NUM);
        initPersonalFragment(personalNum);
    }

    private void initPersonalFragment(String personelNumber) {
        Log.d(TAG, "initPersonalFragment: " + personelNumber);

        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(getActivity(), 2000);
        restApi.getPersonalData("PersonalAdministrationServices", "GetEmployeeHeaderIdentity",
                personelNumber)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d(TAG, "initPersonalFragment sip.. " + strResponse);
                                parseXml(strResponse, successListener);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d(TAG, "initPersonalFragment false _ " + response.raw().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d(TAG, "initPersonalFragment onFailure..");
                        t.printStackTrace();
                    }
                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void parseJson(String strJson) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);
        jsonArr = jsonObject.getAsJsonArray("Table0");

        for (JsonElement je : jsonArr) {
            JsonObject jo = je.getAsJsonObject();
            String personalNum = jo.get("PPERNRM").getAsString();
            String personalName = jo.get("PCNAMEM").getAsString();
            String email = jo.get("EmailUserID").getAsString();
            String company = jo.get("PNAME1M").getAsString();
            String kbo = jo.get("KBO").getAsString();
            String personalArea = jo.get("PNAME1M").getAsString();
            String subArea = jo.get("PBTRTLM_TEXT").getAsString();
            String position = jo.get("PVERAKM").getAsString();
            String userName = jo.get("ADUserName").getAsString();

            tvPersonalNum.setText(personalNum.trim());
            tvFullName.setText(personalName);
            tvEmail.setText(email);
            tvCompany.setText(company);
            tvKbo.setText(kbo);
            tvPersonelArea.setText(personalArea);
            tvPersonelSubArea.setText(subArea);
            tvPosition.setText(position);

            tvPersonalNum.setVisibility(View.VISIBLE);
            tvFullName.setVisibility(View.VISIBLE);
            tvEmail.setVisibility(View.VISIBLE);
            tvCompany.setVisibility(View.VISIBLE);
            tvKbo.setVisibility(View.VISIBLE);
            tvPersonelArea.setVisibility(View.VISIBLE);
            tvPersonelSubArea.setVisibility(View.VISIBLE);
            tvPosition.setVisibility(View.VISIBLE);

            String url = "https://apps.pertamina.com/PTM.HRIS.Service.WebService.Internet.DEV/" +
                    "GetFM.ashx?URL=ROOT/Public/Images/ProfilePictures/" +
                    userName + ".jpg";

            Log.d(TAG, "url= " + url);
            loadProfileListener.loadProfile(userName);
        }
    }

    OnSuccessListener successListener = new OnSuccessListener() {

        @Override
        public void onSuccess(String strJson) {
            parseJson(strJson);
        }
    };
}
