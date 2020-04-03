package com.pertamina.portal.iam.adapters.worklist;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.worklist.PanDetailGenericActivity.BpjsData;
import com.pertamina.portal.iam.fragments.ListWorklistFragment.OnListFragmentInteractionListener;
import com.pertamina.portal.iam.models.IamComment;
import com.pertamina.portal.iam.models.worklist.RmjData;


import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link IamComment} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class BpjsAdapter extends RecyclerView.Adapter<BpjsAdapter.ViewHolder> {

    private static final String TAG = "BpjsAdapter";
    private final List<BpjsData> list;
    private final Activity mActivity;

    public BpjsAdapter(Activity activity, List<BpjsData> items) {
        mActivity = activity;
        list = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pan_bpjs_detail, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        BpjsData data = list.get(position);

        holder.tvStatus.setText("???");
        holder.tvJamsosNumber.setText(data.jamsostekNumber);
        holder.tvMarriageStatus.setText(data.marritalStatus);
        holder.tvStartDate.setText(data.startDate);
        holder.tvEndDate.setText(data.endDate);

        Log.d(TAG, "data.jamsostekNumber = " + data.jamsostekNumber);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private final TextView tvStatus, tvJamsosNumber, tvMarriageStatus, tvStartDate, tvEndDate;
        public RmjData mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvStatus = (TextView) view.findViewById(R.id.tvStatus);
            tvJamsosNumber = (TextView) view.findViewById(R.id.tvJamsosNumber);
            tvMarriageStatus = (TextView) view.findViewById(R.id.tvMarriageStatus);
            tvStartDate = (TextView) view.findViewById(R.id.tvStartDate);
            tvEndDate = (TextView) view.findViewById(R.id.tvEndDate);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvJamsosNumber.getText() + "'";
        }
    }
}
