package com.pertamina.portal.iam.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.interfaces.ReqSuratKetView;
import com.pertamina.portal.iam.interfaces.ReqWorkView;
import com.pertamina.portal.iam.models.PurposeModel;

import java.util.List;

public class ProgressKegiatanRVAdapter extends RecyclerView.Adapter<ProgressKegiatanRVAdapter.ViewHolder> {

    private Context context;
    private List<PurposeModel> progresskegiatanModels;
    private ReqWorkView ReqWorkView;

    public ProgressKegiatanRVAdapter(Context context, List<PurposeModel> progresskegiatanModels, ReqWorkView reqWorkView) {
        this.context = context;
        this.progresskegiatanModels = progresskegiatanModels;
        this.ReqWorkView = reqWorkView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_country, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.nameTV.setText(progresskegiatanModels.get(position).getText());
        holder.nameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReqWorkView.onPurposeClicked(progresskegiatanModels.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        try {
            return progresskegiatanModels.size();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.listItemCountryNameTV);
        }
    }
}
