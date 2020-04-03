package com.pertamina.portal.iam.adapters.PANAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.models.PANModel.FamilyModel;
import com.pertamina.portal.iam.utils.Utils;

import java.util.List;

import static com.pertamina.portal.iam.adapters.PANAdapter.AddressAdapter.status;

public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.ViewHolder> {

    private Context context;
    private List<FamilyModel> familyModelList;

    public FamilyAdapter(Context context, List<FamilyModel> familyModelList) {
        this.context = context;
        this.familyModelList = familyModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_family, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.statusTV.setText(status(familyModelList.get(position).getAction()));
        holder.valueTV.setText(familyModelList.get(position).getFamilyType());
        holder.nameTV.setText(familyModelList.get(position).getName());
        holder.startDateTV.setText(Utils.formatDate(familyModelList.get(position).getStartDate(), "dd MMM yyyy", "yyyyMMdd"));
        holder.endDateTV.setText(Utils.formatDate(familyModelList.get(position).getEndDate(), "dd MMM yyyy", "yyyyMMdd"));
    }

    @Override
    public int getItemCount() {
        return familyModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView statusTV, valueTV, nameTV, startDateTV, endDateTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            statusTV = itemView.findViewById(R.id.itemStatusTV);
            valueTV = itemView.findViewById(R.id.itemValueTV);
            nameTV = itemView.findViewById(R.id.itemNameTV);
            startDateTV = itemView.findViewById(R.id.startDateTV);
            endDateTV = itemView.findViewById(R.id.endDateTV);
        }
    }
}
