package com.pertamina.portal.iam.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.models.LeaveDetailModel;
import com.pertamina.portal.iam.utils.Utils;

import java.util.List;

import static com.pertamina.portal.iam.adapters.PANAdapter.AddressAdapter.status;

public class LeaveDetailAdapter extends RecyclerView.Adapter<LeaveDetailAdapter.ViewHolder> {

    private Context context;
    private List<LeaveDetailModel> leaveDetailModels;

    public LeaveDetailAdapter(Context context, List<LeaveDetailModel> leaveDetailModels) {
        this.context = context;
        this.leaveDetailModels = leaveDetailModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_leave_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.statusTV.setText(status(leaveDetailModels.get(position).getStatus()));
        holder.typeTV.setText(leaveDetailModels.get(position).getType());
        holder.startDateTV.setText(Utils.formatDate(leaveDetailModels.get(position).getStartDate(), "dd MMM yyyy", "yyyyMMdd"));
        holder.endDateTV.setText(Utils.formatDate(leaveDetailModels.get(position).getEndDate(), "dd MMM yyyy", "yyyyMMdd"));
    }

    @Override
    public int getItemCount() {
        return leaveDetailModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView statusTV, typeTV, startDateTV, endDateTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            statusTV = itemView.findViewById(R.id.listItemStatusCLV);
            typeTV = itemView.findViewById(R.id.listItemTypeCLV);
            startDateTV = itemView.findViewById(R.id.listItemStartDateCLV);
            endDateTV = itemView.findViewById(R.id.listItemEndDateCLV);
        }
    }
}
