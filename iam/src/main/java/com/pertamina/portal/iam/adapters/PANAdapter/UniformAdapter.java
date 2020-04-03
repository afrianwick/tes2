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

public class UniformAdapter extends RecyclerView.Adapter<UniformAdapter.ViewHolder> {

    private Context context;
    private List<PersonalIDModel> personalIDModels;

    public UniformAdapter(Context context, List<PersonalIDModel> personalIDModels) {
        this.context = context;
        this.personalIDModels = personalIDModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_pan_uniform_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.startDateTV.setText(Utils.formatDate(personalIDModels.get(position).getStartDate(), "dd MMM yyyy", "yyyyMMdd"));
        holder.endDateTV.setText(Utils.formatDate(personalIDModels.get(position).getEndDate(), "dd MMM yyyy", "yyyyMMdd"));
        holder.statusTV.setText(status(personalIDModels.get(position).getAction()));
        holder.bajuTV.setText(personalIDModels.get(position).getUkuranBaju());
        holder.celanaTV.setText(personalIDModels.get(position).getUkuranCelana());
        holder.sepatuTV.setText(personalIDModels.get(position).getUkuranSepatu());
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

        private TextView startDateTV, endDateTV, bajuTV, celanaTV, sepatuTV, statusTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            startDateTV = itemView.findViewById(R.id.startDateTV);
            endDateTV = itemView.findViewById(R.id.endDateTV);
            bajuTV = itemView.findViewById(R.id.itemBajuTV);
            celanaTV = itemView.findViewById(R.id.itemCelanaTV);
            sepatuTV = itemView.findViewById(R.id.itemSepatuTV);
            statusTV = itemView.findViewById(R.id.itemStatusTV);
        }
    }
}
