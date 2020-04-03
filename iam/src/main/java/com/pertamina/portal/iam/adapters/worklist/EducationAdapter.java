package com.pertamina.portal.iam.adapters.worklist;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.fragments.ListWorklistFragment.OnListFragmentInteractionListener;
import com.pertamina.portal.iam.models.IamComment;
import com.pertamina.portal.iam.models.worklist.EducationData;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link IamComment} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class EducationAdapter extends RecyclerView.Adapter<EducationAdapter.ViewHolder> {

    private static final String TAG = "EducationAdapter";
    private final List<EducationData> list;
    private final Activity mActivity;

    public EducationAdapter(Activity activity, List<EducationData> items) {
        mActivity = activity;
        list = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pan_education_detail, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        EducationData data = list.get(position);

        holder.tvActionType.setText("???");
        holder.tvEstablishment.setText(data.establishment);
        holder.tvInsLocation.setText(data.institutionAndLocation);
        holder.tvBranchStudy.setText(data.branchOfStudy);
        holder.tvStartDate.setText(data.strStartDate);
        holder.tvEndDate.setText(data.strEndDate);

        Log.d(TAG, "data.institutionAndLocation = " + data.institutionAndLocation);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private final TextView tvActionType, tvEstablishment, tvInsLocation, tvBranchStudy, tvStartDate, tvEndDate;
        public EducationData mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvActionType = (TextView) view.findViewById(R.id.tvActionType);
            tvEstablishment = (TextView) view.findViewById(R.id.tvEstablishment);
            tvInsLocation = (TextView) view.findViewById(R.id.tvInsLocation);
            tvBranchStudy = (TextView) view.findViewById(R.id.tvBranchStudy);
            tvStartDate = (TextView) view.findViewById(R.id.tvStartDate);
            tvEndDate = (TextView) view.findViewById(R.id.tvEndDate);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvInsLocation.getText() + "'";
        }
    }
}
