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
import com.pertamina.portal.iam.interfaces.ReqSuratVisiView;
import com.pertamina.portal.iam.models.EmbassyModel;

import java.util.ArrayList;
import java.util.List;

public class EmbassyAdapter extends RecyclerView.Adapter<EmbassyAdapter.ViewHolder> {

    private Context context;
    private List<EmbassyModel> embassyModels;
    private ReqSuratVisiView reqSuratVisiView;

    public EmbassyAdapter(Context context, ReqSuratVisiView reqSuratVisiView, List<EmbassyModel> embassyModels) {
        this.context = context;
        this.embassyModels = embassyModels;
        this.reqSuratVisiView = reqSuratVisiView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_country, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.countryNameTV.setText(embassyModels.get(position).getEmbassyID());
        holder.countryNameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reqSuratVisiView.onEmbassyClicked(embassyModels.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        try {
            return embassyModels.size();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView countryNameTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            countryNameTV = itemView.findViewById(R.id.listItemCountryNameTV);
        }
    }

    public void updateList(List<EmbassyModel> datas) {
        this.embassyModels = new ArrayList<>();
        this.embassyModels.addAll(datas);
        notifyDataSetChanged();
    }
}
