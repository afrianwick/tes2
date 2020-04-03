package com.pertamina.portal.iam.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.interfaces.AdditionalDestinationView;
import com.pertamina.portal.iam.interfaces.LeaveRequestView;

import java.util.ArrayList;
import java.util.List;

public class CountryAdditionalDestinationAdapter extends RecyclerView.Adapter<CountryAdditionalDestinationAdapter.ViewHolder> {

    private Context context;
    private List<String> country, countryIDs;
    private AdditionalDestinationView additionalDestinationView;

    public CountryAdditionalDestinationAdapter(Context context, AdditionalDestinationView additionalDestinationView, List<String> country, List<String> countryIDs) {
        this.context = context;
        this.country = country;
        this.countryIDs = countryIDs;
        this.additionalDestinationView = additionalDestinationView;
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
                Log.d("country name and id", country.get(position) + " = " + countryIDs.get(position));
                additionalDestinationView.onDestinationCountryItemClicked(country.get(position), countryIDs.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return country.size();
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
