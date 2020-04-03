package com.pertamina.portal.iam.adapters.PANAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.models.PANModel.NPWPModel;
import com.pertamina.portal.iam.utils.Utils;

import java.util.List;

import static com.pertamina.portal.iam.adapters.PANAdapter.AddressAdapter.status;

public class NPWPAdapter extends RecyclerView.Adapter<NPWPAdapter.ViewHolder> {

    private Context context;
    private List<NPWPModel> npwpModels;

    public NPWPAdapter(Context context, List<NPWPModel> npwpModels) {
        this.context = context;
        this.npwpModels = npwpModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_npwp, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.valueTV.setText(npwpModels.get(position).getNpwpNumber());
        holder.statusTV.setText(status(npwpModels.get(position).getAction()));
        holder.startDateTV.setText(Utils.formatDate(npwpModels.get(position).getStartDate(), "dd MMM yyyy", "yyyyMMdd"));
        holder.endDateTV.setText(Utils.formatDate(npwpModels.get(position).getEndDate(), "dd MMM yyyy", "yyyyMMdd"));
        holder.kTV.setText(npwpModels.get(position).getStatusPajak());
        holder.spouseBenefitCB.setChecked(npwpModels.get(position).getSpouseBenefit().equalsIgnoreCase("X"));
        holder.spouseBenefitCB.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        return npwpModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView statusTV, kTV, valueTV, startDateTV, endDateTV;
        private CheckBox spouseBenefitCB;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            startDateTV = itemView.findViewById(R.id.startDateTV);
            endDateTV = itemView.findViewById(R.id.endDateTV);
            spouseBenefitCB = itemView.findViewById(R.id.spouseBenefitCB);
            kTV = itemView.findViewById(R.id.itemKTV);
            statusTV = itemView.findViewById(R.id.itemStatusTV);
            valueTV = itemView.findViewById(R.id.itemValueTV);
        }
    }
}
