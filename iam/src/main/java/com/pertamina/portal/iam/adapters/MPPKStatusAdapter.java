package com.pertamina.portal.iam.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.models.MPPKStatusModel;

import java.util.List;

public class MPPKStatusAdapter extends RecyclerView.Adapter<MPPKStatusAdapter.ViewHolder> {

    private Activity activity;
    private List<MPPKStatusModel> mppkStatusModels;

    public MPPKStatusAdapter(Activity activity, List<MPPKStatusModel> mppkStatusModels) {
        this.activity = activity;
        this.mppkStatusModels = mppkStatusModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.list_item_mppk2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.folioNumberTV.setText(mppkStatusModels.get(position).getFolioNumber());
        holder.nameTV.setText(mppkStatusModels.get(position).getName());
        holder.nopekTV.setText(mppkStatusModels.get(position).getNopek());
        holder.statusTV.setText(mppkStatusModels.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return mppkStatusModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView folioNumberTV, nameTV, nopekTV, statusTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            folioNumberTV = itemView.findViewById(R.id.mppkStatusFolioNumberTV);
            nameTV = itemView.findViewById(R.id.mppkStatusNameTV);
            nopekTV = itemView.findViewById(R.id.mppkStatusNopekTV);
            statusTV = itemView.findViewById(R.id.mppkStatusTV);
        }
    }
}
