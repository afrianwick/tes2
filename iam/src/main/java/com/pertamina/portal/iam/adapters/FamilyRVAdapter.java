package com.pertamina.portal.iam.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.interfaces.LeaveRequestView;
import com.pertamina.portal.iam.interfaces.ReqSuratKetView;
import com.pertamina.portal.iam.models.FamilyModel;

import java.util.ArrayList;
import java.util.List;

public class FamilyRVAdapter extends RecyclerView.Adapter<FamilyRVAdapter.ViewHolder> {

    private Context context;
    private List<FamilyModel> familyModels;
    private ReqSuratKetView reqSuratKetView;

    public FamilyRVAdapter(Context context, List<FamilyModel> familyModels, ReqSuratKetView reqSuratKetView) {
        this.context = context;
        this.familyModels = familyModels;
        this.reqSuratKetView = reqSuratKetView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_country, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.nameTV.setText(familyModels.get(position).getName());
        holder.nameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reqSuratKetView.onFamilyClicked(familyModels.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        try {
            return familyModels.size();
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
