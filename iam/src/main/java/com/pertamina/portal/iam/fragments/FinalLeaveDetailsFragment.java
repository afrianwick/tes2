package com.pertamina.portal.iam.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.core.activity.BackableNoActionbarActivity;
import com.pertamina.portal.core.interfaces.OnSuccessListener;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.PrefUtils;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.LeaveReqAddReviewerActivity;
import com.pertamina.portal.iam.activity.worklist.MclDetailActivity;
import com.pertamina.portal.iam.adapters.AdditionalDestinationAdapter;
import com.pertamina.portal.iam.adapters.AdditionalParticipantAdapter;
import com.pertamina.portal.iam.adapters.ReviewerAdapter;
import com.pertamina.portal.iam.models.AdditionalParticipantModel;
import com.pertamina.portal.iam.view.IamButton;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinalLeaveDetailsFragment extends Fragment {

    public static LinearLayout finalLeaveDetailsInfoContainerLL;
    public static TextView leaveTypeTV, reasonTV, leaveDateTV, outOfTownTV, cityTV, countryTV;
    public static TextView leaveQuotaTV, leaveTotalTV, remainingQuotaTV, forecastTV, leaveOnProcessTV, messageTV;
    private IamButton submitButton;
    private AlertDialog loading, mAlertDialog, mAlertDialog2;
    private EditText commetET;
    private RelativeLayout additionalDestinationRL, additionalReviewerRL, additionalParticipantRL;
    private ConstraintLayout additionalDestinationCL, additionalReviewerCL, additionalParticipantCL;
    private RecyclerView additionalDestinationRV, additionalReviewerRV, additionalParticipantRV;

    private AdditionalParticipantAdapter additionalParticipantAdapter;
    private AdditionalDestinationAdapter additionalDestinationAdapter;
    private ReviewerAdapter reviewerAdapter;

    public static ConstraintLayout additionalContainer1CL;
    public static LinearLayout additionalContainer1LL, additionalContainer2LL;

    public FinalLeaveDetailsFragment() {
        // Required empty public constructor
    }

    public static FinalLeaveDetailsFragment newInstance(String param1, String param2) {
        FinalLeaveDetailsFragment fragment = new FinalLeaveDetailsFragment();
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
        return inflater.inflate(R.layout.fragment_final_leave_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loading = new SpotsDialog.Builder().setContext(getContext()).build();

        finalLeaveDetailsInfoContainerLL = view.findViewById(R.id.finalLeaveDetailInfoContainerLL);
        additionalContainer1CL = view.findViewById(R.id.additionalContainerTV);
        additionalContainer1LL = view.findViewById(R.id.fragmentRequestLeaveDetailsAdditionalDaysContainerLL);
        additionalContainer2LL = view.findViewById(R.id.fragmentRequestLeaveDetailsAdditionalDays2ContainerLL);
        leaveTypeTV = view.findViewById(R.id.finalLeaveDetailLeaveTypeTV);
        reasonTV = view.findViewById(R.id.finalLeaveDetailReasonTV);
        leaveDateTV = view.findViewById(R.id.finalLeaveDetailDateTV);
        forecastTV = view.findViewById(R.id.fragmentFinalLeaveDetailForecastQuotaTV);
        leaveOnProcessTV = view.findViewById(R.id.fragmentFinalLeaveDetailLeaveOnProcessTV);
        messageTV = view.findViewById(R.id.fragmentFinalLeaveDetailMessageTV);
        outOfTownTV = view.findViewById(R.id.finalLeaveDetailOutOfTownTV);
        cityTV = view.findViewById(R.id.finalLeaveDetailCityTV);
        countryTV = view.findViewById(R.id.finalLeaveDetailCountryTV);
        leaveQuotaTV = view.findViewById(R.id.finalLeaveDetailLeaveQuotaTV);
        leaveTotalTV = view.findViewById(R.id.finalLeaveDetailLeaveTotalTV);
        remainingQuotaTV = view.findViewById(R.id.finalLeaveDetailRemainingQuotaTV);
        submitButton = view.findViewById(R.id.finalLeaveDetailSubmitButton);
        commetET = view.findViewById(R.id.finalLeaveDetailCommetET);

        additionalParticipantRV = view.findViewById(R.id.finalLeaveDetailAdditionalParticipantRV);
        additionalDestinationRV = view.findViewById(R.id.finalLeaveDetailAdditionalDestinationRV);
        additionalReviewerRV = view.findViewById(R.id.finalLeaveDetailAdditionalReviewerRV);

        additionalParticipantRL = view.findViewById(R.id.finalLeaveDetailAdditionalParticipantContainerRL);
        additionalDestinationRL = view.findViewById(R.id.finalLeaveDetailAdditionalDestinationContainerRL);
        additionalReviewerRL = view.findViewById(R.id.finalLeaveDetailAdditionalReviewerContainerRL);

        additionalParticipantCL = view.findViewById(R.id.finalLeaveDetailAdditionalParticipantHeaderCL);
        additionalDestinationCL = view.findViewById(R.id.finalLeaveDetailAdditionalDestinationHeaderCL);
        additionalReviewerCL = view.findViewById(R.id.finalLeaveDetailAdditionalReviewerHeaderCL);

        additionalParticipantCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                additionalParticipantRL.setVisibility(additionalParticipantRL.isShown() ? View.GONE : View.VISIBLE);
            }
        });

        additionalDestinationCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                additionalDestinationRL.setVisibility(additionalDestinationRL.isShown() ? View.GONE : View.VISIBLE);
            }
        });

        additionalReviewerCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                additionalReviewerRL.setVisibility(additionalReviewerRL.isShown() ? View.GONE : View.VISIBLE);
            }
        });

        buildAlert();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (RequestLeaveFragment.returnType != null && !RequestLeaveFragment.returnType.equals("") && !RequestLeaveFragment.returnType.equals("E")) {
                    if (commetET.getText().toString().isEmpty()) {
                        mAlertDialog.setMessage("Comment tidak boleh kosong!");
                        mAlertDialog.show();
                        return;
                    }
                    submitLeave();
                } else {
                    try {
                        if (RequestLeaveFragment.returnType.equals("E")) {
                            mAlertDialog.setMessage(RequestLeaveFragment.returnMessage);
                            mAlertDialog.show();
                        } else {
                            mAlertDialog.setMessage("Lengkapi data terlebih dahulu!");
                            mAlertDialog.show();
                        }
                    } catch (Exception e) {
                        
                    }
                }
            }
        });
    }

    public void showAdditionalDestination() {
        if (AdditionalDestinationFragment.additionalDestinationModelList == null || AdditionalDestinationFragment.additionalDestinationModelList.size() == 0) {
            additionalDestinationRL.setVisibility(View.GONE);
            additionalDestinationCL.setVisibility(View.GONE);
        } else {
            additionalDestinationRL.setVisibility(View.VISIBLE);
            additionalDestinationCL.setVisibility(View.VISIBLE);

            additionalDestinationAdapter = new AdditionalDestinationAdapter(getContext(), AdditionalDestinationFragment.additionalDestinationModelList, false);
            additionalDestinationRV.setLayoutManager(new LinearLayoutManager(getContext()));
            additionalDestinationRV.setAdapter(additionalDestinationAdapter);
        }
    }

    public void showAdditionalParticipant() {
        if (AdditionalParticipantFragment.additionalParticipantModels == null || AdditionalParticipantFragment.additionalParticipantModels.size() == 0) {
            additionalParticipantRL.setVisibility(View.GONE);
            additionalParticipantCL.setVisibility(View.GONE);
        } else {
            additionalParticipantRL.setVisibility(View.VISIBLE);
            additionalParticipantCL.setVisibility(View.VISIBLE);

            additionalParticipantAdapter = new AdditionalParticipantAdapter(getContext(), AdditionalParticipantFragment.additionalParticipantModels, false);
            additionalParticipantRV.setLayoutManager(new LinearLayoutManager(getContext()));
            additionalParticipantRV.setAdapter(additionalParticipantAdapter);
        }
    }

    public void showAdditionalReviewer() {
        if (LeaveReqReviewerFragment.reviewerModels == null || LeaveReqReviewerFragment.reviewerModels.size() == 0) {
            additionalReviewerRL.setVisibility(View.GONE);
            additionalReviewerCL.setVisibility(View.GONE);
        } else {
            additionalReviewerRL.setVisibility(View.VISIBLE);
            additionalReviewerCL.setVisibility(View.VISIBLE);

            reviewerAdapter = new ReviewerAdapter(LeaveReqReviewerFragment.reviewerModels, getContext(), false);
            additionalReviewerRV.setLayoutManager(new LinearLayoutManager(getContext()));
            additionalReviewerRV.setAdapter(reviewerAdapter);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private String k2DataFieldsInJSON() {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ExecutorPERNR", PrefUtils.Build(getContext()).getPref().getString(Constants.KEY_PERSONAL_NUM, ""));
            jsonObject.put("ExecutorADName", PrefUtils.Build(getContext()).getPref().getString(Constants.KEY_USERNAME, ""));
            jsonObject.put("RequesterPERNR", PrefUtils.Build(getContext()).getPref().getString(Constants.KEY_PERSONAL_NUM, ""));
            jsonObject.put("RequesterADName", PrefUtils.Build(getContext()).getPref().getString(Constants.KEY_USERNAME, ""));
            jsonObject.put("LeaveDateStart", RequestLeaveFragment.dateFrom);
            jsonObject.put("LeaveDateEnd", RequestLeaveFragment.dateTo);
            jsonObject.put("LeaveTypeCode", RequestLeaveFragment.attendanceorAbsenceType);
            jsonObject.put("LeaveTypeText", RequestLeaveFragment.attendanceorAbsenceName);
            jsonObject.put("LeaveDestinationTown", RequestLeaveFragment.etCity.getText());
            jsonObject.put("LeaveDestinationCountry", RequestLeaveFragment.etCountry.getText().toString());
            jsonObject.put("LeavePermitRemainder", "26");
            jsonObject.put("LeavePermitRequest", "2");
            jsonObject.put("FileName", "");
            String reviewer = "";
            if (LeaveReqReviewerFragment.reviewerModels != null && LeaveReqReviewerFragment.reviewerModels.size() != 0) {
                for (int i = 0; i < LeaveReqReviewerFragment.reviewerModels.size(); i++) {
                    reviewer += LeaveReqReviewerFragment.reviewerModels.get(i).getAdUserName();

                    if (i == LeaveReqReviewerFragment.reviewerModels.size() - 1) {
                        reviewer += "";
                    } else {
                        reviewer += "|";
                    }
                }
            }
            jsonObject.put("ReviewerADName", reviewer);
            return jsonObject.toString();
        } catch (JSONException e) {
            return "";
        }
    }

    private String createXMLSummary() {
        String xmlParram;

        xmlParram = "<DataSummary>";
        xmlParram += "<DataType>LEV</DataType>";
        xmlParram += "<DataSubtype>" + RequestLeaveFragment.attendanceorAbsenceType + "</DataSubtype>";
        xmlParram += "<DataValue>" + RequestLeaveFragment.attendanceorAbsenceName + "</DataValue>";
        xmlParram += "</DataSummary>";

        return xmlParram;
    }

    private String createXML() {
        String outOfTown = RequestLeaveFragment.checkBox.isChecked() ? "X" : "";

        String xmlParam;

        xmlParam = "<TimeManagementDataSet>";

        xmlParam += "<PTM_MS_IT_ABSENCES>";
        xmlParam += "<PPERNRM>" + PrefUtils.Build(getContext()).getPref().getString(Constants.KEY_PERSONAL_NUM, "") + "</PPERNRM>";
        xmlParam += "<ORI_PPERNRM>" + PrefUtils.Build(getContext()).getPref().getString(Constants.KEY_PERSONAL_NUM, "") + "</ORI_PPERNRM>";
        xmlParam += "<PSUBTYM>" + RequestLeaveFragment.attendanceorAbsenceType + "</PSUBTYM>";
        xmlParam += "<ORI_PSUBTYM>" + RequestLeaveFragment.attendanceorAbsenceType + "</ORI_PSUBTYM>";
        xmlParam += "<PENDDAM>" + RequestLeaveFragment.dateTo + "</PENDDAM>";
        xmlParam += "<ORI_PENDDAM>" + RequestLeaveFragment.dateTo + "</ORI_PENDDAM>";
        xmlParam += "<PBEGDAM>" + RequestLeaveFragment.dateFrom + "</PBEGDAM>";
        xmlParam += "<ORI_PBEGDAM>" + RequestLeaveFragment.dateFrom + "</ORI_PBEGDAM>";
        xmlParam += "<PSEQNRM>0</PSEQNRM><ORI_PSEQNRM>0</ORI_PSEQNRM><PRESE1M>IN</PRESE1M>";
        xmlParam += "<PRESE2M>" + outOfTown + "</PRESE2M>";
        xmlParam += "<PAWARTM>" + RequestLeaveFragment.attendanceorAbsenceType + "</PAWARTM>";
        xmlParam += "<PALLDFM>X</PALLDFM>";
        xmlParam += "</PTM_MS_IT_ABSENCES>";

        xmlParam += "<AdditionalInfo>";
        xmlParam += "<TambahanHari>"+(RequestLeaveFragment.checkBox.isChecked() ? "Yes" : "No")+"</TambahanHari>";
        xmlParam += "<DestinationCity>"+ RequestLeaveFragment.etCity.getText().toString() +"</DestinationCity>";
        xmlParam += "<DestinationCountry>"+ RequestLeaveFragment.etCountry.getText().toString() +"</DestinationCountry>";
        xmlParam += "<Reason>"+ RequestLeaveFragment.reasonET.getText().toString() +"</Reason>";
        xmlParam += "<LeaveTypeText>"+ RequestLeaveFragment.attendanceorAbsenceName +"</LeaveTypeText>";
        xmlParam += "<DestinationCountryCode>"+ RequestLeaveFragment.selectedCountryID +"</DestinationCountryCode>";
        xmlParam += "<LeaveTotal>"+ RequestLeaveFragment.leaveTotalTV.getText().toString() +"</LeaveTotal>";
        xmlParam += "<TotalLeaveonProcess>"+ RequestLeaveFragment.leaveQuotaTV.getText().toString() +"</TotalLeaveonProcess>";
        xmlParam += "<RemainingQuota>"+ RequestLeaveFragment.remainingQuotaTV.getText().toString() +"</RemainingQuota>";
        xmlParam += "</AdditionalInfo>";

        if (AdditionalDestinationFragment.additionalDestinationModelList != null &&
                AdditionalDestinationFragment.additionalDestinationModelList.size()!= 0) {
            for (int i = 0; i < AdditionalDestinationFragment.additionalDestinationModelList.size(); i++) {
                xmlParam += "<Destination>";
                xmlParam += "<From>" + AdditionalDestinationFragment.additionalDestinationModelList.get(i).getCity() + "</From>";
                xmlParam += "<To>" + AdditionalDestinationFragment.additionalDestinationModelList.get(i).getCountry() + "</To>";
                xmlParam += "<CountryID>" + AdditionalDestinationFragment.additionalDestinationModelList.get(i).getCountryID() + "</CountryID>";
                xmlParam += "<SDate>" + AdditionalDestinationFragment.additionalDestinationModelList.get(i).getStartDate() + "</SDate>";
                xmlParam += "<EDate>" + AdditionalDestinationFragment.additionalDestinationModelList.get(i).getEndDate() + "</EDate>";
                xmlParam += "<Description>" + AdditionalDestinationFragment.additionalDestinationModelList.get(i).getNote() + "</Description>";
                xmlParam += "</Destination>";
            }
        }

        if (AdditionalParticipantFragment.additionalParticipantModels != null &&
                AdditionalParticipantFragment.additionalParticipantModels.size() != 0) {
            for (int i = 0; i < AdditionalParticipantFragment.additionalParticipantModels.size(); i++) {
                xmlParam += "<Participant>";
                xmlParam += "<Name>" + AdditionalParticipantFragment.additionalParticipantModels.get(i).getName() + "</Name>";
                xmlParam += "<Note>" + AdditionalParticipantFragment.additionalParticipantModels.get(i).getNote() + "</Note>";
                xmlParam += "</Participant>";
            }
        }

        if (LeaveReqReviewerFragment.reviewerModels != null && LeaveReqReviewerFragment.reviewerModels.size() != 0) {
            for (int i = 0; i < LeaveReqReviewerFragment.reviewerModels.size(); i++) {
                xmlParam += "<Reviewer>";

                xmlParam += "<ReviewPersonalNumber>" + LeaveReqReviewerFragment.reviewerModels.get(i).getPersonelNumber() + "</ReviewPersonalNumber>";
                xmlParam += "<Name>" + LeaveReqReviewerFragment.reviewerModels.get(i).getName() + "</Name>";
                xmlParam += "<Position>" + LeaveReqReviewerFragment.reviewerModels.get(i).getPosition() + "</Position>";
                xmlParam += "<ADUserName>" + LeaveReqReviewerFragment.reviewerModels.get(i).getAdUserName() + "</ADUserName>";

                xmlParam += "</Reviewer>";
            }
        }

        xmlParam += "<AbsenceCalculation>";

        xmlParam += "<ReturnType>" + RequestLeaveFragment.returnType + "</ReturnType>";
        xmlParam += "<TotalNumberOfDays>" + RequestLeaveFragment.totalNumberOfDays + "</TotalNumberOfDays>";
        xmlParam += "<TotalNumberOfWorkingDays>" + RequestLeaveFragment.leaveTotal + "</TotalNumberOfWorkingDays>";
        xmlParam += "<CurrentQuota>" + RequestLeaveFragment.leaveQuota + "</CurrentQuota>";
        xmlParam += "<RemainingQuota>" + RequestLeaveFragment.remainingQuota + "</RemainingQuota>";
        xmlParam += "<SuperiorName>" + RequestLeaveFragment.superiorName + "</SuperiorName>";
        xmlParam += "<SuperiorPosition>" + RequestLeaveFragment.superiorPosition + "</SuperiorPosition>";
        xmlParam += "<NextAttendance>" + RequestLeaveFragment.nextAttendance + "</NextAttendance>";
        xmlParam += "<DeductionFrom>" + RequestLeaveFragment.deductionFrom + "</DeductionFrom>";
        xmlParam += "<DeductionTo>" + RequestLeaveFragment.deductionTo + "</DeductionTo>";
        xmlParam += "<LeaveOnProcessDays>" + RequestLeaveFragment.leaveOnProcess + "</LeaveOnProcessDays>";
        xmlParam += "<OutOfTownLeave>" + RequestLeaveFragment.outOfTownLeave + "</OutOfTownLeave>";

        xmlParam += "</AbsenceCalculation>";

        xmlParam += "</TimeManagementDataSet>";

        return xmlParam;
    }

    private void submitLeave() {
        System.out.println("submit leave xml data " + createXML());
        System.out.println("submit leave xml data summary " + createXMLSummary());
        System.out.println("submit leave json " + k2DataFieldsInJSON());
        loading.show();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticated(getContext(), 2000);
        restApi.submitLeaveRequest("K2Services", "SubmitRequest",
                "HRISK2V3\\1010\\Time Management\\LeaveRequest",
                "",
                PrefUtils.Build(getContext()).getPref().getString(Constants.KEY_PERSONAL_NUM, ""),
                PrefUtils.Build(getContext()).getPref().getString(Constants.KEY_PERSONAL_NUM, ""),
                PrefUtils.Build(getContext()).getPref().getString(Constants.KEY_USERNAME, ""),
                createXML().replace("&", "&amp;"),
                createXMLSummary().replace("&", "&amp;"),
                commetET.getText().toString().replace("&", "&amp;"),
                k2DataFieldsInJSON().replace("&", "&amp;"))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d("submitleave", "sumbitApproval sip.. " + strResponse);
                                parseXml(strResponse);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d("submitleave", "sumbitApproval false _ " + response.raw().toString());
                        }

                        loading.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        Log.d("submitleave", "sumbitApproval onFailure..");
                        t.printStackTrace();
                    }
                });
    }

    public void parseXml(String strXml){
        DocumentBuilderFactory factory;
        DocumentBuilder builder;
        Document dom;

        try {
            InputStream is = new ByteArrayInputStream(strXml.getBytes("utf-8"));
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            dom = builder.parse(is);

            NodeList nodeListError = dom.getElementsByTagName("ReturnMessage");
            NodeList returnType = dom.getElementsByTagName("ReturnType");
            String strReturnType = returnType.item(0).getTextContent();

            if (strReturnType.equalsIgnoreCase("S")) {
                NodeList nodeListSuccess = dom.getElementsByTagName("ReturnObject");

                if (nodeListSuccess.getLength() > 0) {
                    this.mAlertDialog = new AlertDialog.Builder(getContext())
                            .setNeutralButton("close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    getActivity().finish();
                                }
                            })
                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    getActivity().finish();
                                }
                            })
                            .create();
                    mAlertDialog.setMessage(nodeListError.item(0).getTextContent());
                    mAlertDialog.show();
                }
            } else {
                for (int i = 0; i < nodeListError.getLength(); i++) {
                    String strError = nodeListError.item(i).getTextContent();

                    showError(strError);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

//    private void parseJson(String strJson) {
//        Log.d(TAG, "parseJson: strJson" + strJson);
//        Gson gson = new Gson();
//        JsonObject jo = gson.fromJson(strJson, JsonObject.class);
//
//        list = new ArrayList<>();
//        final HashMap<String, List<MyDocumentData>> listGrouped = new HashMap();
//
//        JsonArray jarr = jo.getAsJsonArray("records");
//        JsonObject itemJo;
//        MyDocumentData mdd;
//    }

    public void buildAlert() {
        this.mAlertDialog = new AlertDialog.Builder(getContext())
                .setNeutralButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
    }

    public void showError(String strError) {
        String message = "Could not get data due to:" + strError;

        if (strError.contains("401")) {
            message = "Could not get data. It might be you are Loged in from other device or your session was exiperd.";
            mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Login again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        startActivity(new Intent(getContext(),
                                Class.forName("com.pertamina.portal.activity.LoginActivity")));
                        getActivity().finish();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (strError.contains("404")) {
            message = "Could not get data. File Not Found.";
        }

        if (mAlertDialog == null) {
            this.mAlertDialog = new AlertDialog.Builder(getContext())
                    .setNeutralButton("close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            getActivity().finish();
                        }
                    })
                    .create();
        }

        mAlertDialog.setMessage(message);

        if (!mAlertDialog.isShowing()) {
            mAlertDialog.show();
        }
    }
}
