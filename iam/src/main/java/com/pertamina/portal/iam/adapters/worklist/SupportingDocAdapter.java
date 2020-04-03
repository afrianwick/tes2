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
import com.pertamina.portal.iam.models.worklist.SupportingDoc;

import java.util.List;

public class SupportingDocAdapter extends RecyclerView.Adapter<SupportingDocAdapter.ViewHolder> {

    private static final String TAG = "BpjsAdapter";
    private final List<SupportingDoc> list;
    private final Activity mActivity;

    public SupportingDocAdapter(Activity activity, List<SupportingDoc> items) {
        mActivity = activity;
        list = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_supdoc_lev_detail, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        SupportingDoc data = list.get(position);

        holder.tvDocName.setText(data.docName);
        holder.tvDocTypeName.setText(data.docTypeName);
        holder.tvDocIssuer.setText(data.docIssuer);
        holder.tvDateIssued.setText(data.dateOfIssue);
        holder.tvDescription.setText(data.docDesc);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private final TextView tvDocName, tvDocTypeName, tvDocIssuer, tvDateIssued, tvDescription;
        public RmjData mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvDocName = (TextView) view.findViewById(R.id.tvDocName);
            tvDocTypeName = (TextView) view.findViewById(R.id.tvDocTypeName);
            tvDocIssuer = (TextView) view.findViewById(R.id.tvDocIssuer);
            tvDateIssued = (TextView) view.findViewById(R.id.tvDateIssued);
            tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvDescription.getText() + "'";
        }
    }
}
