package com.pertamina.portal.iam.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.worklist.MCRDetailActivity;
import com.pertamina.portal.iam.models.MCRDetailModel;
import com.pertamina.portal.iam.models.MCRModel;
import com.pertamina.portal.iam.utils.DateUtils.DateUtils;

import java.io.Serializable;
import java.util.List;

public class MCRAdapter extends RecyclerView.Adapter<MCRAdapter.ViewHolder> {

    private Activity activity;
    private List<MCRModel> mcrModel;
    private List<MCRDetailModel> mcrDetailModel;
    private DateUtils dateUtils;

    public MCRAdapter(Activity activity, List<MCRModel> mcrModel, List<MCRDetailModel> mcrDetailModel) {
        this.activity = activity;
        this.mcrModel = mcrModel;
        this.mcrDetailModel = mcrDetailModel;
        dateUtils = new DateUtils();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.list_item_mcr, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.startDateTV.setText(dateUtils.setInputDate(mcrModel.get(position).getStartDate()).setInputPattern("yyyyMMdd").setOutputPattern("dd MMM yyyy").build());
        holder.endDateTV.setText(dateUtils.setInputDate(mcrModel.get(position).getEndDate()).setInputPattern("yyyyMMdd").setOutputPattern("dd MMM yyyy").build());
        holder.statusTV.setText(mcrModel.get(position).getStatus());
        holder.nameTV.setText(mcrModel.get(position).getName());
        holder.clinicCodeTV.setText(mcrModel.get(position).getClinicCode());
        holder.clinicNameTV.setText(mcrModel.get(position).getClinicName());
        holder.bloodTypeTV.setText(mcrModel.get(position).getBloodType());

        holder.containerCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MCRDetailActivity.class);
                intent.putExtra("mcrDetailModel", (Serializable) mcrDetailModel.get(position));
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        try {
            return mcrModel.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView statusTV, nameTV, bloodTypeTV, clinicCodeTV, clinicNameTV, startDateTV, endDateTV;
        private CardView containerCV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            statusTV = itemView.findViewById(R.id.listItemMCRStatusTV);
            nameTV = itemView.findViewById(R.id.listItemMCRNameTV);
            bloodTypeTV = itemView.findViewById(R.id.listItemMCRBloodTypeTV);
            clinicCodeTV = itemView.findViewById(R.id.listItemMCRClinicCodeTV);
            clinicNameTV = itemView.findViewById(R.id.listItemMCRClinicNameTV);
            startDateTV = itemView.findViewById(R.id.listItemMCRStartDateTV);
            endDateTV = itemView.findViewById(R.id.listItemMCREndDateTV);
            containerCV = itemView.findViewById(R.id.listItemMCRContainerCV);
        }
    }
}
