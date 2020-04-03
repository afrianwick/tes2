package com.pertamina.portal.iam.adapters.worklist;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.core.utils.StringUtils;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.worklist.MclClaimFormActivity;
import com.pertamina.portal.iam.models.ClaimRequest;
import com.pertamina.portal.iam.utils.Utils;

import java.util.List;

import static com.pertamina.portal.iam.activity.worklist.MclDetailActivity.CLAIM_REQUEST;
import static com.pertamina.portal.iam.activity.worklist.MclDetailActivity.CLAIM_REQ_CODE;

public class MCLHistoryAdapter extends RecyclerView.Adapter<MCLHistoryAdapter.ViewHolder> {

    private static final String TAG = "ClaimReqAdapter";
    private final List<ClaimRequest> list;
    private final Activity mActivity;
    private final String type;

    public MCLHistoryAdapter(Activity activity, List<ClaimRequest> items, String type) {
        mActivity = activity;
        list = items;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mcl_detail, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ClaimRequest data = list.get(position);

        Log.d(TAG, "data.patient = " + data.patient);

        try {
            holder.tvFolioNumber.setText(data.folioNumber);
            holder.tvClaimType.setText(data.claimType);
            holder.tvBillDate.setText(Utils.formatDate(data.billDate, "dd MMM yyyy", "yyyyMMdd"));
            holder.tvPatient.setText(data.patient);
            holder.tvStatus.setText(getStatus(data.strStatus));
            holder.tvClaimAmount.setText("Rp " + StringUtils.formatCurrency(data.claimAmount));
            holder.tvGrantedAmount.setText("Rp " + StringUtils.formatCurrency(data.grantedAmount));
            holder.tvPostedClaimAmount.setText(data.postedClaimAmount);

            if (type != null) {
                if (type.equalsIgnoreCase("pending")) {
                    holder.tvPostedClaimAmountLabel.setVisibility(View.GONE);
                    holder.tvGrantedAmount.setVisibility(View.GONE);
                    holder.tvGrantedAmountLabel.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getStatus(String type) {
        switch (type) {
            case "P":
                return "Pasien";
            case "A":
                return "Anak";
            case "I":
                return "Istri";
            case "S":
                return "Suami";
            default:
                return "";
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private final TextView tvFolioNumber, tvClaimType, tvBillDate, tvPatient, tvStatus,
                tvClaimAmount, tvGrantedAmount, tvPostedClaimAmount, tvPostedClaimAmountLabel, tvGrantedAmountLabel;
        public ClaimRequest mItem;
        private final CardView containerCV;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvGrantedAmountLabel = (TextView) view.findViewById(R.id.tvGrantedAmountLabel);
            tvPostedClaimAmountLabel = (TextView) view.findViewById(R.id.tvPostedClaimAmountLabel);
            tvFolioNumber = (TextView) view.findViewById(R.id.tvFolioNumber);
            tvClaimType = (TextView) view.findViewById(R.id.tvClaimType);
            tvBillDate = (TextView) view.findViewById(R.id.tvBillDate);
            tvPatient = (TextView) view.findViewById(R.id.tvPatient);
            tvStatus = (TextView) view.findViewById(R.id.tvStatus);
            tvClaimAmount = (TextView) view.findViewById(R.id.tvClaimAmount);
            tvGrantedAmount = (TextView) view.findViewById(R.id.tvGrantedAmount);
            tvPostedClaimAmount = (TextView) view.findViewById(R.id.tvPostedClaimAmount);
            containerCV = view.findViewById(R.id.itemMclContainerCV);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvPatient.getText() + "'";
        }
    }
}
