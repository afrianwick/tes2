package com.pertamina.portal.iam.adapters.worklist;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.fragments.ListWorklistFragment.OnListFragmentInteractionListener;
import com.pertamina.portal.iam.models.IamComment;
import com.pertamina.portal.iam.models.worklist.TaxData;
import com.pertamina.portal.iam.models.worklist.UniformData;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link IamComment} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TaxAdapter extends RecyclerView.Adapter<TaxAdapter.ViewHolder> {

    private static final String TAG = "UniformAdapter";
    private final List<TaxData> list;
    private final Activity mActivity;

    public TaxAdapter(Activity activity, List<TaxData> items) {
        mActivity = activity;
        list = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pan_tax_detail, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        TaxData data = list.get(position);

        holder.tvActionType.setText(data.actionType);
        holder.tvNPWP.setText(data.npwp);
        holder.tvTaxStatus.setText(data.taxStatus);
        holder.tvStartDate.setText(data.strStartDate);
        holder.tvEndDate.setText(data.strEndDate);

        if (!data.taxStatus.equalsIgnoreCase("TK0")) {
            holder.cbSpuseBenefit.setChecked(true);
        } else {
            holder.cbSpuseBenefit.setChecked(false);
        }

        Log.d(TAG, "data.npwp = " + data.npwp);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private final TextView tvNPWP, tvTaxStatus, tvStartDate, tvEndDate, tvActionType;
        private final CheckBox cbSpuseBenefit;
        public TaxData mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvNPWP = (TextView) view.findViewById(R.id.tvNPWP);
            cbSpuseBenefit = (CheckBox) view.findViewById(R.id.cbSpuseBenefit);
            tvTaxStatus = (TextView) view.findViewById(R.id.tvTaxStatus);
            tvStartDate = (TextView) view.findViewById(R.id.tvStartDate);
            tvEndDate = (TextView) view.findViewById(R.id.tvEndDate);
            tvActionType = (TextView) view.findViewById(R.id.tvActionType);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvNPWP.getText() + "'";
        }
    }
}
