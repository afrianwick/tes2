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
import com.pertamina.portal.iam.interfaces.ReqSuratVisiView;
import com.pertamina.portal.iam.models.PurposeModel;

import java.util.List;

public class PurposeRVAdapter extends RecyclerView.Adapter<PurposeRVAdapter.ViewHolder> {

    private Context context;
    private List<PurposeModel> purposeModels;
    private ReqSuratVisiView reqSuratVisiView;

    public PurposeRVAdapter(Context context, List<PurposeModel> purposeModels, ReqSuratVisiView reqSuratVisiView) {
        this.context = context;
        this.purposeModels = purposeModels;
        this.reqSuratVisiView = reqSuratVisiView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_country, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.nameTV.setText(purposeModels.get(position).getText());
        holder.nameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reqSuratVisiView.onPurposeClicked(purposeModels.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        try {
            return purposeModels.size();
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
