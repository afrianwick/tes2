package com.pertamina.portal.iam.adapters.PANAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.models.PANModel.AddressModel;
import com.pertamina.portal.iam.utils.Utils;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private Context context;
    private List<AddressModel> addressModels;

    public AddressAdapter(Context context, List<AddressModel> addressModels) {
        this.context = context;
        this.addressModels = addressModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_pan_alamat, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.startDateTV.setText(Utils.formatDate(addressModels.get(position).getStartDate(), "dd MMM yyyy", "yyyyMMdd"));
        holder.endDateTV.setText(Utils.formatDate(addressModels.get(position).getEndDate(), "dd MMM yyyy", "yyyyMMdd"));
        holder.statusTV.setText(status(addressModels.get(position).getAction()));
        holder.nameTV.setText(addressModels.get(position).getContactName());
        holder.alamatType.setText(addressModels.get(position).getAddressType());
        holder.alamatTV.setText(addressModels.get(position).getStreetHouseNumber());
    }

    public static String status(String type) {
        switch (type) {
            case "IN":
                return "New";
            case "UP":
                return "Update";
            case "DE":
                return "Delete";
            default:
                return "";
        }
    }

    @Override
    public int getItemCount() {
        return addressModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView startDateTV, endDateTV, alamatTV, alamatType, nameTV, statusTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            startDateTV = itemView.findViewById(R.id.listItemAlamatStartDateTV);
            endDateTV = itemView.findViewById(R.id.listItemAlamatEndDateTV);
            alamatTV = itemView.findViewById(R.id.listItemAlamatTV);
            alamatType = itemView.findViewById(R.id.listItemAlamatTypeTV);
            nameTV = itemView.findViewById(R.id.listItemAlamatNameTV);
            statusTV = itemView.findViewById(R.id.listItemAlamatStatusTV);
        }
    }
}
