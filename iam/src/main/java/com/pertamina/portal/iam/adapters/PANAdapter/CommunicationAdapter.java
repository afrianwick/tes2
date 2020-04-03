package com.pertamina.portal.iam.adapters.PANAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.models.PANModel.CommunicationModel;
import com.pertamina.portal.iam.utils.Utils;

import java.util.List;

import static com.pertamina.portal.iam.adapters.PANAdapter.AddressAdapter.status;

public class CommunicationAdapter extends RecyclerView.Adapter<CommunicationAdapter.ViewHolder> {

    private Context context;
    private List<CommunicationModel> communicationModelList;

    public CommunicationAdapter(Context context, List<CommunicationModel> communicationModelList) {
        this.context = context;
        this.communicationModelList = communicationModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_pan_communication_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.emailTV.setText(communicationModelList.get(position).getValue());
        holder.statusTV.setText(status(communicationModelList.get(position).getAction()));
        holder.startDateTV.setText(Utils.formatDate(communicationModelList.get(position).getStartDate(), "dd MMM yyyy", "yyyyMMdd"));
        holder.endDateTV.setText(Utils.formatDate(communicationModelList.get(position).getEndDate(), "dd MMM yyyy", "yyyyMMdd"));
        holder.typeTV.setText(communicationModelList.get(position).getCommType());
    }

    @Override
    public int getItemCount() {
        return communicationModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView statusTV, typeTV, emailTV, startDateTV, endDateTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            startDateTV = itemView.findViewById(R.id.startDateTV);
            endDateTV = itemView.findViewById(R.id.endDateTV);
            statusTV = itemView.findViewById(R.id.itemStatusTV);
            typeTV = itemView.findViewById(R.id.itemTypeTV);
            emailTV = itemView.findViewById(R.id.itemValueTV);
        }
    }
}
