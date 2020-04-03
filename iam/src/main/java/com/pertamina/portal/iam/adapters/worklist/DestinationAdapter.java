package com.pertamina.portal.iam.adapters.worklist;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.models.worklist.Destination;
import com.pertamina.portal.iam.models.worklist.RmjData;

import java.util.List;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.ViewHolder> {

    private static final String TAG = "BpjsAdapter";
    private final List<Destination> list;
    private final Activity mActivity;

    public DestinationAdapter(Activity activity, List<Destination> items) {
        mActivity = activity;
        list = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_destination_lev_detail, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Destination data = list.get(position);

        holder.tvCityTo.setText(data.to);
        holder.tvCityFrom.setText(data.from);
        holder.tvStartDate.setText(data.startDate);
        holder.tvEndDate.setText(data.endDate);
        holder.tvDescription.setText(data.description);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private final TextView tvCityTo, tvCityFrom, tvStartDate, tvEndDate, tvDescription;
        public RmjData mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvCityTo = (TextView) view.findViewById(R.id.tvCityTo);
            tvCityFrom = (TextView) view.findViewById(R.id.tvCityFrom);
            tvStartDate = (TextView) view.findViewById(R.id.tvStartDate);
            tvEndDate = (TextView) view.findViewById(R.id.tvEndDate);
            tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvDescription.getText() + "'";
        }
    }
}
