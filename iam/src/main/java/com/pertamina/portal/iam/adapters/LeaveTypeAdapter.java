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
import com.pertamina.portal.iam.models.LeaveType;

import java.util.ArrayList;
import java.util.List;

public class LeaveTypeAdapter extends RecyclerView.Adapter<LeaveTypeAdapter.ViewHolder> {

    private Context context;
    private List<LeaveType> leaveTypes;
    private LeaveRequestView leaveRequestView;

    public LeaveTypeAdapter(Context context, LeaveRequestView leaveRequestView, List<LeaveType> leaveTypes) {
        this.context = context;
        this.leaveTypes = leaveTypes;
        this.leaveRequestView = leaveRequestView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_country, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.countryNameTV.setText(leaveTypes.get(position).label);
        holder.countryNameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leaveRequestView.onLeaveTypeItemClicked(leaveTypes.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        try {
            return leaveTypes.size();
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

    public void updateList(List<LeaveType> datas) {
        this.leaveTypes = new ArrayList<>();
        this.leaveTypes.addAll(datas);
        notifyDataSetChanged();
    }
}
