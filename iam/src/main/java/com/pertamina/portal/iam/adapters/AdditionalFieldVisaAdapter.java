package com.pertamina.portal.iam.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.interfaces.ReqSuratVisiView;
import com.pertamina.portal.iam.models.AdditionalFieldVisaModel;

import java.util.List;

public class AdditionalFieldVisaAdapter extends RecyclerView.Adapter<AdditionalFieldVisaAdapter.ViewHolder> {

    private Context context;
    private List<AdditionalFieldVisaModel> additionalFieldVisaModels;
    private ReqSuratVisiView reqSuratVisiView;

    public AdditionalFieldVisaAdapter(Context context, List<AdditionalFieldVisaModel> additionalFieldVisaModels, ReqSuratVisiView reqSuratVisiView) {
        this.context = context;
        this.additionalFieldVisaModels = additionalFieldVisaModels;
        this.reqSuratVisiView = reqSuratVisiView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_additional_field_visa, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.actionButton.setColorFilter(context.getResources().getColor(R.color.white));
        if (position == 0) {
            holder.actionButton.setBackground(context.getResources().getDrawable(R.drawable.button_bg_rounded_green));
            holder.actionButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add));
        } else {
            holder.actionButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_close));
            holder.actionButton.setBackground(context.getResources().getDrawable(R.drawable.button_bg_rounded_red));
        }
        holder.typeET.setText(additionalFieldVisaModels.get(position).getType());
        holder.actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == 0) {
                    if (getItemCount() >= 5) {
                        return;
                    }
                    AdditionalFieldVisaModel additionalFieldVisaModel = new AdditionalFieldVisaModel();
                    additionalFieldVisaModel.setName("");
                    additionalFieldVisaModel.setType("Spouse");
                    additionalFieldVisaModels.add(additionalFieldVisaModel);
                    notifyDataSetChanged();
                } else {
                    additionalFieldVisaModels.remove(position);
                    notifyItemRemoved(position);
                    //this line below gives you the animation and also updates the
                    //list items after the deleted item
                    notifyItemRangeChanged(position, getItemCount());
                }
            }
        });

        holder.typeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reqSuratVisiView.onTypeClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return additionalFieldVisaModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextInputEditText nameET, typeET;
        private ImageButton actionButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameET = itemView.findViewById(R.id.listItemAdditionalFieldVisaNameTIET);
            typeET = itemView.findViewById(R.id.listItemAdditionalFieldVisaTypeTIET);
            actionButton = itemView.findViewById(R.id.listItemAdditionalFieldVisaActionButton);
        }
    }
}
