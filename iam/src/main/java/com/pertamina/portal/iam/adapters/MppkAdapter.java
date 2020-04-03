package com.pertamina.portal.iam.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.models.MPPKModel;

import java.util.List;

public class MppkAdapter extends RecyclerView.Adapter<MppkAdapter.ViewHolder> {

    private Context context;
    private List<MPPKModel> data;

    public MppkAdapter(Context context, List<MPPKModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_mppk, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.companyCodeTV.setText(data.get(position).getPBUKRSM());
        holder.birthDateTV.setText(data.get(position).getPGBDATM());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView companyCodeTV, birthDateTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            companyCodeTV = itemView.findViewById(R.id.mppkCompanyCodeTV);
            birthDateTV = itemView.findViewById(R.id.mppkBirthDateTV);
        }
    }
}
