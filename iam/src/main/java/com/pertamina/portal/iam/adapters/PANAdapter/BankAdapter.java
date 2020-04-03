package com.pertamina.portal.iam.adapters.PANAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.models.PANModel.BankModel;
import com.pertamina.portal.iam.utils.Utils;

import java.util.List;

import static com.pertamina.portal.iam.adapters.PANAdapter.AddressAdapter.status;

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.ViewHolder> {

    private Context context;
    private List<BankModel> bankModels;

    public BankAdapter(Context context, List<BankModel> bankModels) {
        this.context = context;
        this.bankModels = bankModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_bank, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.valueTV.setText(bankModels.get(position).getBankName());
        holder.statusTV.setText(status(bankModels.get(position).getAction()));
        holder.startDateTV.setText(Utils.formatDate(bankModels.get(position).getStartDate(), "dd MMM yyyy", "yyyyMMdd"));
        holder.endDateTV.setText(Utils.formatDate(bankModels.get(position).getEndDate(), "dd MMM yyyy", "yyyyMMdd"));
        holder.mainTV.setText(bankModels.get(position).getBankType());
        holder.nameTV.setText(bankModels.get(position).getPayee());
        holder.rekTV.setText(bankModels.get(position).getAccountNumber());
        holder.countryTV.setText(bankModels.get(position).getBankCountry());
    }

    @Override
    public int getItemCount() {
        return bankModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView statusTV, mainTV, nameTV, valueTV, startDateTV, endDateTV;
        private TextView countryTV, rekTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            startDateTV = itemView.findViewById(R.id.startDateTV);
            endDateTV = itemView.findViewById(R.id.endDateTV);
            statusTV = itemView.findViewById(R.id.itemStatusTV);
            mainTV = itemView.findViewById(R.id.itemMainTV);
            valueTV = itemView.findViewById(R.id.itemValueTV);
            countryTV = itemView.findViewById(R.id.itemCountryTV);
            rekTV = itemView.findViewById(R.id.itemNoRekTV);
            nameTV = itemView.findViewById(R.id.itemNameTV);
        }
    }
}
