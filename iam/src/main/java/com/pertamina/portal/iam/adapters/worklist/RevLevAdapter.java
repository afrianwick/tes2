package com.pertamina.portal.iam.adapters.worklist;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.models.worklist.ReviewerLev;
import com.pertamina.portal.iam.models.worklist.RmjData;

import java.util.List;

public class RevLevAdapter extends RecyclerView.Adapter<RevLevAdapter.ViewHolder> {

    private static final String TAG = "BpjsAdapter";
    private final List<ReviewerLev> list;
    private final Activity mActivity;

    public RevLevAdapter(Activity activity, List<ReviewerLev> items) {
        mActivity = activity;
        list = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_revlev_lev_detail, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ReviewerLev data = list.get(position);

        holder.tvNama.setText(data.revName);
        holder.tvPanNumber.setText(data.revPan);
        holder.tvPosition.setText(data.revPosition);
        holder.tvAdUsername.setText(data.revADUserName);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private final TextView tvNama, tvPanNumber, tvPosition, tvAdUsername;
        public RmjData mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvNama = (TextView) view.findViewById(R.id.tvNama);
            tvPanNumber = (TextView) view.findViewById(R.id.tvPanNumber);
            tvPosition = (TextView) view.findViewById(R.id.tvPosition);
            tvAdUsername = (TextView) view.findViewById(R.id.tvAdUsername);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvNama.getText() + "'";
        }
    }
}
