package com.pertamina.portal.iam.activity;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pertamina.portal.core.activity.BackableNoActionbarActivity;
import com.pertamina.portal.core.interfaces.PortalApiInterface;
import com.pertamina.portal.core.utils.Constants;
import com.pertamina.portal.core.utils.PrefUtils;
import com.pertamina.portal.core.utils.RestClient;
import com.pertamina.portal.core.utils.StringUtils;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.adapters.CountryAdapter;
import com.pertamina.portal.iam.adapters.LeaveTypeAdapter;
import com.pertamina.portal.iam.fragments.AdditionalDestinationFragment;
import com.pertamina.portal.iam.fragments.AdditionalParticipantFragment;
import com.pertamina.portal.iam.fragments.FinalLeaveDetailsFragment;
import com.pertamina.portal.iam.fragments.LeaveReqReviewerFragment;
import com.pertamina.portal.iam.fragments.RequestLeaveFragment;
import com.pertamina.portal.iam.fragments.SupportingDocumentFragment;
import com.pertamina.portal.iam.interfaces.LeaveRequestView;
import com.pertamina.portal.iam.models.LeaveType;
import com.pertamina.portal.iam.utils.ErrorMessage;
import com.pertamina.portal.iam.utils.KeyboardUtils;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveRequestActivity extends BackableNoActionbarActivity implements LeaveRequestView {

    private static final String TAG = "LeaveRequestActivity";
    private final FragmentManager fm = getSupportFragmentManager();
    public static AdditionalDestinationFragment lvReqAdditionalDestFragment;
    public static RequestLeaveFragment lvReqDetailFragment;
    public static AdditionalParticipantFragment lvReqAdditionalParticipantFragment;
    private Fragment activeFragment;
    private ImageView ivAdditionalDest;
    private ImageView ivAdditionalParticipant;
    private ImageView ivLeaveDetail;
    private ImageView ivReviewer;
    private ImageView ivFinalLeaveDetail;
    private ImageView ivAdditionalDestTriangle;
    private ImageView ivAdditionalParticipantTriangle;
    private ImageView ivLeaveDetailTriangle;
    private ImageView ivReviewerTriangle;
    private ImageView ivFinalLeaveDetailTriangle;
    public static LeaveReqReviewerFragment lvReqReviewerFragment;
    public static SupportingDocumentFragment lvReqSupportingDocFragment;
    public static FinalLeaveDetailsFragment lvReqFinalLeaveDetailFragment;
    private AlertDialog loading;
    private AlertDialog alertDialog;
    private TextView tvYearly, tvDeduction, tvAdvance, tvQuotaEnd;
    private View dialogCountryView;
    private View dialogLeaveTypeView;
    private LeaveTypeAdapter leaveTypeAdapter;
    private int lastSelectedPosition= 0;

    private ConstraintLayout leaveRequestParentCL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_request);
        super.onCreateBackable(this, R.id.ivBack);

        tvYearly = (TextView) findViewById(R.id.tvYearly);
        tvDeduction = (TextView) findViewById(R.id.tvDeduction);
        tvAdvance = (TextView) findViewById(R.id.tvAdvance);
        tvQuotaEnd = (TextView) findViewById(R.id.tvQuotaEnd);

        leaveRequestParentCL = findViewById(R.id.leaveRequestParentCL);

        lvReqDetailFragment = RequestLeaveFragment.newInstance(null, null);
        lvReqAdditionalDestFragment = AdditionalDestinationFragment.newInstance(null, null);
        lvReqAdditionalParticipantFragment = AdditionalParticipantFragment.newInstance(null, null);
        lvReqReviewerFragment = LeaveReqReviewerFragment.newInstance(null, null);
        lvReqSupportingDocFragment = SupportingDocumentFragment.newInstance(null, null);
        lvReqFinalLeaveDetailFragment = FinalLeaveDetailsFragment.newInstance(null, null);

        fm.beginTransaction().add(R.id.flMain, lvReqDetailFragment, "detail").commit();
        fm.beginTransaction().add(R.id.flMain, lvReqAdditionalDestFragment, "additional_dest").commit();
        fm.beginTransaction().add(R.id.flMain, lvReqAdditionalParticipantFragment, "additional_par").commit();
        fm.beginTransaction().add(R.id.flMain, lvReqReviewerFragment, "review").commit();
        fm.beginTransaction().add(R.id.flMain, lvReqSupportingDocFragment, "support_doc").commit();
        fm.beginTransaction().add(R.id.flMain, lvReqFinalLeaveDetailFragment, "final_leave").commit();

        fm.beginTransaction()
                .show(lvReqDetailFragment)
                .hide(lvReqAdditionalDestFragment)
                .hide(lvReqAdditionalParticipantFragment)
                .hide(lvReqReviewerFragment)
                .hide(lvReqSupportingDocFragment)
                .hide(lvReqFinalLeaveDetailFragment)
                .commit();

        activeFragment = lvReqDetailFragment;

        ivLeaveDetail = findViewById(R.id.ivLeaveDetail);
        ivAdditionalDest = findViewById(R.id.ivAdditionalDest);
        ivAdditionalParticipant = findViewById(R.id.ivAdditionalParticipant);
        ivReviewer = findViewById(R.id.ivReviewer);
        ivFinalLeaveDetail = findViewById(R.id.ivFinalLeaveDetail);

        ivLeaveDetailTriangle = findViewById(R.id.ivLeaveDetailTriangle);
        ivAdditionalDestTriangle = findViewById(R.id.ivAdditionalDestTriangle);
        ivAdditionalParticipantTriangle = findViewById(R.id.ivAdditionalParticipantTriangle);
        ivReviewerTriangle = findViewById(R.id.ivReviewerTriangle);
        ivFinalLeaveDetailTriangle = findViewById(R.id.ivFinalLeaveDetailTriangle);

        ivAdditionalDestTriangle.setVisibility(View.INVISIBLE);
        ivAdditionalParticipantTriangle.setVisibility(View.INVISIBLE);
        ivReviewerTriangle.setVisibility(View.INVISIBLE);
        ivFinalLeaveDetailTriangle.setVisibility(View.INVISIBLE);

        ivLeaveDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.beginTransaction().hide(activeFragment).show(lvReqDetailFragment).commit();
                activeFragment = lvReqDetailFragment;
                ivLeaveDetail.setBackground(getResources().getDrawable(R.drawable.ic_list_cyrcle));
                selectTab(0);
            }
        });

        ivAdditionalDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (RequestLeaveFragment.returnType == null) {
                    ErrorMessage.errorMessage(LeaveRequestActivity.this, leaveRequestParentCL, "Leave Detail harus diisi!");
                    return;
                }
                fm.beginTransaction().hide(activeFragment).show(lvReqAdditionalDestFragment).commit();
                activeFragment = lvReqAdditionalDestFragment;
                selectTab(1);
            }
        });

        ivAdditionalParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (RequestLeaveFragment.returnType == null) {
                    ErrorMessage.errorMessage(LeaveRequestActivity.this, leaveRequestParentCL, "Leave Detail harus diisi!");
                    return;
                }
                fm.beginTransaction().hide(activeFragment).show(lvReqAdditionalParticipantFragment).commit();
                activeFragment = lvReqAdditionalParticipantFragment;
                selectTab(2);
            }
        });

        ivReviewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (RequestLeaveFragment.returnType == null) {
                    ErrorMessage.errorMessage(LeaveRequestActivity.this, leaveRequestParentCL, "Leave Detail harus diisi!");
                    return;
                }
                fm.beginTransaction().hide(activeFragment).show(lvReqReviewerFragment).commit();
                activeFragment = lvReqReviewerFragment;
                selectTab(3);
            }
        });

        ivFinalLeaveDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (RequestLeaveFragment.returnType == null) {
                    ErrorMessage.errorMessage(LeaveRequestActivity.this, leaveRequestParentCL, "Leave Detail harus diisi!");
                    return;
                }
                fm.beginTransaction().hide(activeFragment).show(lvReqFinalLeaveDetailFragment).commit();
                activeFragment = lvReqFinalLeaveDetailFragment;
                selectTab(4);
            }
        });

        loading = new SpotsDialog.Builder().setContext(this).build();
        buildAlert();

        KeyboardUtils.setupUI(this, findViewById(R.id.leaveRequestParentCL));
    }

    private void selectTab(int position) {
        if (lastSelectedPosition != position) {
            lastSelectedState();
        }
        currentSelectedState(position);
        lastSelectedPosition = position;
    }

    @Override
    public void onBackPressed() {
        if (dialogCountryView != null) {
            if (dialogCountryView.isShown()) {
                dialogCountryView.setVisibility(View.GONE);
                return;
            }
        }

        if (dialogLeaveTypeView != null) {
            if (dialogLeaveTypeView.isShown()) {
                dialogLeaveTypeView.setVisibility(View.GONE);
                return;
            }
        }
        super.onBackPressed();
    }

    private void lastSelectedState() {
        switch (lastSelectedPosition) {
            case 0:
                ivLeaveDetail.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_black));
                ivLeaveDetailTriangle.setVisibility(View.INVISIBLE);
                break;
            case 1:
                ivAdditionalDest.setImageDrawable(getResources().getDrawable(R.drawable.ic_pin_cyrcle2));
                ivAdditionalDestTriangle.setVisibility(View.INVISIBLE);
                break;
            case 2:
                ivAdditionalParticipant.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_cyrcle));
                ivAdditionalParticipantTriangle.setVisibility(View.INVISIBLE);
                break;
            case 3:
                ivReviewer.setImageDrawable(getResources().getDrawable(R.drawable.ic_teropong_cyrcel));
                ivReviewerTriangle.setVisibility(View.INVISIBLE);
                break;
            case 4:
                ivFinalLeaveDetail.setImageDrawable(getResources().getDrawable(R.drawable.ic_centrang_cyrcle));
                ivFinalLeaveDetailTriangle.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void currentSelectedState(int position) {
        switch (position) {
            case 0:
                ivLeaveDetail.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_cyrcle));
                ivLeaveDetailTriangle.setVisibility(View.VISIBLE);
                break;
            case 1:
                ivAdditionalDest.setImageDrawable(getResources().getDrawable(R.drawable.ic_pin_red));
                ivAdditionalDestTriangle.setVisibility(View.VISIBLE);
                break;
            case 2:
                ivAdditionalParticipant.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_red));
                ivAdditionalParticipantTriangle.setVisibility(View.VISIBLE);
                break;
            case 3:
                ivReviewer.setImageDrawable(getResources().getDrawable(R.drawable.ic_teropong_red));
                ivReviewerTriangle.setVisibility(View.VISIBLE);
                break;
            case 4:
                ivFinalLeaveDetail.setImageDrawable(getResources().getDrawable(R.drawable.ic_centrang_red));
                ivFinalLeaveDetailTriangle.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void dialogCountrySetup(final List<String> country, final List<String> countryIDs) {
        dialogCountryView = findViewById(R.id.leaveRequestDialogCountryView);
        ConstraintLayout dialogCountryContainer = dialogCountryView.findViewById(R.id.dialogCountryContainerCL);
        RecyclerView dialogCountryRV = dialogCountryView.findViewById(R.id.dialogCountryRV);
        EditText dialogCountrySearchET = dialogCountryView.findViewById(R.id.dialogCountrySearcET);

        dialogCountryView.setVisibility(View.VISIBLE);

        final CountryAdapter countryAdapter = new CountryAdapter(this, this, country, countryIDs);
        dialogCountryRV.setLayoutManager(new LinearLayoutManager(this));
        dialogCountryRV.setAdapter(countryAdapter);

        dialogCountryContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCountryView.setVisibility(View.GONE);
            }
        });

        dialogCountrySearchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (country == null || country.size() == 0) {
                    return;
                }
                if (charSequence.length() == 0) {
                    countryAdapter.updateList(country, countryIDs);
                } else {
                    List<String> filterData = new ArrayList<>();
                    List<String> filterCountryID = new ArrayList<>();
                    for (int ii = 0; ii < country.size(); ii++) {
                        if (country.get(ii).toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            filterData.add(country.get(ii));
                            filterCountryID.add(countryIDs.get(ii));
                        }
                    }

                    countryAdapter.updateList(filterData, filterCountryID);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void dialogLeaveTypeSetup(final List<LeaveType> leaveTypes) {
        dialogLeaveTypeView = findViewById(R.id.leaveRequestDialogLeaveTypeView);
        ConstraintLayout dialogCountryContainer = dialogLeaveTypeView.findViewById(R.id.dialogCountryContainerCL);
        RecyclerView dialogCountryRV = dialogLeaveTypeView.findViewById(R.id.dialogCountryRV);
        EditText dialogCountrySearchET = dialogLeaveTypeView.findViewById(R.id.dialogCountrySearcET);
        TextView title = dialogLeaveTypeView.findViewById(R.id.dialogCountryTitleTV);

        title.setText("Leave Type");

        dialogLeaveTypeView.setVisibility(View.VISIBLE);

        final LeaveTypeAdapter leaveTypeAdapter = new LeaveTypeAdapter(this, this, leaveTypes);
        dialogCountryRV.setLayoutManager(new LinearLayoutManager(this));
        dialogCountryRV.setAdapter(leaveTypeAdapter);

        dialogCountryContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogLeaveTypeView.setVisibility(View.GONE);
            }
        });

        dialogCountrySearchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (leaveTypes == null || leaveTypes.size() == 0) {
                    return;
                }
                if (charSequence.length() == 0) {
                    leaveTypeAdapter.updateList(leaveTypes);
                } else {
                    List<LeaveType> filterData = new ArrayList<>();
                    for (int ii = 0; ii < leaveTypes.size(); ii++) {
                        if (leaveTypes.get(ii).label.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            filterData.add(leaveTypes.get(ii));
                        }
                    }

                    leaveTypeAdapter.updateList(filterData);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getDataQuota();
    }

    private void getDataQuota() {
        String personelNum = PrefUtils.Build(this).getPref().getString(Constants.KEY_PERSONAL_NUM, "");
        Date today = new Date();
        String strEndDate = StringUtils.formatDateNoSpace(today);
        String currentYear = strEndDate.substring(0, 4);

        Log.d(TAG, "strEndDate=" + strEndDate);
        Log.d(TAG, "currentYear=" + currentYear);

        loading.show();
        PortalApiInterface restApi = RestClient.getRetrofitAuthenticatedXML(this, 2000);
        restApi.getLeaveQuotaHilight("PersonalAdministrationServices",
                "GetInfoTypeRawData",
                personelNum, strEndDate, strEndDate)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String strResponse = response.body().string();
                                Log.d(TAG, "sip.. " + strResponse);
                                parseXml(strResponse);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d(TAG, "false _ " + response.raw().toString());
                        }

                        loading.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        Log.d(TAG, "onFailure..");
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

            if (dom.getElementsByTagName("ReturnObject") != null) {
                NodeList nodeListSuccess = dom.getElementsByTagName("ReturnObject");

                if (nodeListSuccess.getLength() > 0) {
                    parseJson(nodeListSuccess.item(0).getTextContent());
                }
            } else {
                for (int i = 0; i < nodeListError.getLength(); i++) {
                    String strError = nodeListError.item(i).getTextContent();

                    alertDialog.setMessage("Can not get data due to:" + strError);

                    if (!alertDialog.isShowing()) {
                        alertDialog.show();
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void parseJson(String strJson) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(strJson, JsonObject.class);
        JsonArray jsonArray = jsonObject.getAsJsonArray("Table0");

        tvYearly.setText("0");
        tvDeduction.setText("0");
        tvAdvance.setText("0");
        tvQuotaEnd.setText("0");

        int currentQuota = 0;
        int deduction = 0;
        int advances = 0;
        int quotaEnd = 0;
        if (jsonArray.size() > 0) {
            Log.d("kenapaarray", "yes");
            for (int i = 0; i < jsonArray.size(); i++) {
                Log.d("kenapafor", "yes");
                JsonObject jo = jsonArray.get(i).getAsJsonObject();
                if (jo.get("PSUBTYM").getAsString().replace(" ", "").equals("01")) {
                    Log.d("kenapa01", "yes");
                    currentQuota += jo.get("PANZHLM").getAsInt();
                    deduction += jo.get("PKVERBM").getAsInt();
                    quotaEnd = quotaEnd < jo.get("PDEENDM").getAsInt() ? jo.get("PDEENDM").getAsInt() : quotaEnd;
                }
            }
            advances = deduction > currentQuota ? deduction - currentQuota : 0;
            String strQuotaEnd = String.valueOf(quotaEnd);
            Date quotaEndDate = StringUtils.toDateYyyyMmDd(strQuotaEnd);
            strQuotaEnd = StringUtils.formatDateSimpleMM(quotaEndDate);

            tvYearly.setText(String.valueOf(currentQuota));
            tvDeduction.setText(String.valueOf(deduction));
            tvAdvance.setText(String.valueOf(advances));
            tvQuotaEnd.setText(String.valueOf(strQuotaEnd));
        } else {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }

    }

    public void buildAlert() {
        this.alertDialog = new AlertDialog.Builder(this)
                .setNeutralButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        finish();
                    }
                })
                .create();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            RequestLeaveFragment.returnType = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestinationCountryClicked(List<String> country, List<String> countryIDs) {
        try {
            dialogCountrySetup(country, countryIDs);
        } catch (NullPointerException e) {
            alertDialog.setMessage("Tidak ada pilihan destination country!");
            alertDialog.show();
        }
    }

    @Override
    public void onLeaveTypeClicked(List<LeaveType> leaveTypes) {
        try {
            dialogLeaveTypeSetup(leaveTypes);
        } catch (NullPointerException e) {
            alertDialog.setMessage("Tidak ada pilihan leave type!");
            alertDialog.show();
        }
    }

    @Override
    public void onLeaveTypeItemClicked(LeaveType leaveType) {
        lvReqDetailFragment.setLeaveType(leaveType);
        dialogLeaveTypeView.setVisibility(View.GONE);
        KeyboardUtils.hideKeyboard(this, findViewById(R.id.leaveRequestParentCL));
    }

    @Override
    public void onDestinationCountryItemClicked(String country, String countryID) {
        lvReqDetailFragment.setDestinationCountry(country, countryID);
        dialogCountryView.setVisibility(View.GONE);
        KeyboardUtils.hideKeyboard(this, findViewById(R.id.leaveRequestParentCL));
    }
}
