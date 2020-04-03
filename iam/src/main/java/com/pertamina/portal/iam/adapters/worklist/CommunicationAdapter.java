package com.pertamina.portal.iam.adapters.worklist;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.fragments.ListWorklistFragment.OnListFragmentInteractionListener;
import com.pertamina.portal.iam.models.IamComment;
import com.pertamina.portal.iam.models.worklist.ContactData;

import java.util.List;

public class CommunicationAdapter extends RecyclerView.Adapter<CommunicationAdapter.ViewHolder> {

    private static final String TAG = "CommunicationAdapter";
    private final List<ContactData> list;
    private final Activity mActivity;

    public CommunicationAdapter(Activity activity, List<ContactData> items) {
        mActivity = activity;
        list = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pan_communication_detail, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ContactData data = list.get(position);

        holder.tvAccountType.setText(data.typeStr);
        holder.tvContactLabel.setText(data.contactLabel);
        holder.tvContactValue.setText(data.contactValue);
        holder.tvStartDate.setText(data.sstrStartDate);
        holder.tvEndDate.setText(data.strEndDate);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private final TextView tvAccountType, tvContactLabel, tvContactValue, tvStartDate, tvEndDate;
        public ContactData mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvAccountType = (TextView) view.findViewById(R.id.itemTypeTV);
            tvContactLabel = (TextView) view.findViewById(R.id.itemStatusTV);
            tvContactValue = (TextView) view.findViewById(R.id.itemValueTV);
            tvStartDate = (TextView) view.findViewById(R.id.tvStartDate);
            tvEndDate = (TextView) view.findViewById(R.id.tvEndDate);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvContactValue.getText() + "'";
        }
    }
}
