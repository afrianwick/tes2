package com.pertamina.portal.iam.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.LeaveRequestActivity;
import com.pertamina.portal.iam.models.AdditionalDestinationModel;
import com.pertamina.portal.iam.utils.Utils;

import java.util.List;

import okhttp3.internal.Util;

public class AdditionalDestinationAdapter extends RecyclerView.Adapter<AdditionalDestinationAdapter.ViewHolder> {

    private Context context;
    private List<AdditionalDestinationModel> additionalDestinationModels;
    private AlertDialog alertDialog;
    private boolean isAllowedDeleted;

    public AdditionalDestinationAdapter(Context context, List<AdditionalDestinationModel> additionalDestinationModels, boolean isAllowDeleted) {
        this.context = context;
        this.additionalDestinationModels = additionalDestinationModels;
        this.isAllowedDeleted = isAllowDeleted;
        buildAlert();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_additional_destination, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        AdditionalDestinationModel additionalDestinationModel = additionalDestinationModels.get(position);
        holder.countryTV.setText(additionalDestinationModel.getCountry());
        holder.cityTV.setText(additionalDestinationModel.getCity());
        holder.startDateTV.setText(Utils.formatDate(additionalDestinationModel.getStartDate(), "dd MMM yyyy", "yyyyMMdd"));
        holder.endDateTV.setText(Utils.formatDate(additionalDestinationModel.getEndDate(), "dd MMM yyyy", "yyyyMMdd"));
        holder.descriptionTV.setText(additionalDestinationModel.getNote());

        holder.deleteIV.setVisibility(View.GONE);
        if (isAllowedDeleted) {
            holder.deleteIV.setVisibility(View.VISIBLE);
        }

        holder.deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.setMessage("Apakah anda yakin ingin menghapus data?");
                alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        additionalDestinationModels.remove(position);
                        LeaveRequestActivity.lvReqAdditionalDestFragment.onUpdateItem();
                        notifyItemRemoved(position);
                        //this line below gives you the animation and also updates the
                        //list items after the deleted item
                        notifyItemRangeChanged(position, getItemCount());
                    }
                });
                alertDialog.show();
            }
        });
    }

    public void buildAlert() {
        alertDialog = new AlertDialog.Builder(context)
                .setNeutralButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
    }

    @Override
    public int getItemCount() {
        return additionalDestinationModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView countryTV, cityTV, startDateTV, endDateTV, descriptionTV;
        private ImageView deleteIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            countryTV = itemView.findViewById(R.id.listItemAdditionalDestinationCountryTV);
            cityTV = itemView.findViewById(R.id.listItemAdditionalDestinationCityTV);
            startDateTV = itemView.findViewById(R.id.listItemAdditionalDestinationStartDateTV);
            endDateTV = itemView.findViewById(R.id.listItemAdditionalDestinationEndDateTV);
            descriptionTV = itemView.findViewById(R.id.listItemAdditionalDestinationDescriptionTV);
            deleteIV = itemView.findViewById(R.id.listItemAdditionalDestinationDeleteIV);
        }
    }
}
