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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {

    private Context context;
    private List<String> country, countryIDs;
    private LeaveRequestView leaveRequestView;

    public CountryAdapter(Context context, LeaveRequestView leaveRequestView, List<String> country, List<String> countryIDs) {
        this.context = context;
        this.country = country;
        this.countryIDs = countryIDs;
        this.leaveRequestView = leaveRequestView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_country, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.countryNameTV.setText(country.get(position));
        holder.countryNameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leaveRequestView.onDestinationCountryItemClicked(country.get(position), countryIDs.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        try {
            return country.size();
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

    public void updateList(List<String> datas, List<String> countryIDs) {
        this.country = new ArrayList<>();
        this.countryIDs = new ArrayList<>();
        this.countryIDs.addAll(countryIDs);
        this.country.addAll(datas);
        notifyDataSetChanged();
    }
}
