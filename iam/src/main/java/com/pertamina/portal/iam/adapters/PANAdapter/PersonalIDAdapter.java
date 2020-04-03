package com.pertamina.portal.iam.adapters.PANAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.models.PANModel.PersonalIDModel;
import com.pertamina.portal.iam.utils.Utils;

import java.util.List;

public class PersonalIDAdapter extends RecyclerView.Adapter<PersonalIDAdapter.ViewHolder> {

    private Context context;
    private List<PersonalIDModel> personalIDModels;

    public PersonalIDAdapter(Context context, List<PersonalIDModel> personalIDModels) {
        this.context = context;
        this.personalIDModels = personalIDModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_pan_personal_id_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.startDateTV.setText(Utils.formatDate(personalIDModels.get(position).getStartDate(), "dd MMM yyyy", "yyyyMMdd"));
        holder.endDateTV.setText(Utils.formatDate(personalIDModels.get(position).getEndDate(), "dd MMM yyyy", "yyyyMMdd"));
        holder.statusTV.setText(status(personalIDModels.get(position).getAction()));
        holder.valueTV.setText(personalIDModels.get(position).getIdNumber());
        holder.typeTV.setText(personalIDModels.get(position).getIdType());
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
        return personalIDModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView startDateTV, endDateTV, valueTV, typeTV, statusTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            startDateTV = itemView.findViewById(R.id.startDateTV);
            endDateTV = itemView.findViewById(R.id.endDateTV);
            valueTV = itemView.findViewById(R.id.itemValueTV);
            typeTV = itemView.findViewById(R.id.itemTypeTV);
            statusTV = itemView.findViewById(R.id.itemStatusTV);
        }
    }
}
