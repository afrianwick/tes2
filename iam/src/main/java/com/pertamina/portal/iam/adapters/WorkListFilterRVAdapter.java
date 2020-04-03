package com.pertamina.portal.iam.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.MyWorklistActivity;
import com.pertamina.portal.iam.interfaces.MyWorkListView;
import com.pertamina.portal.iam.utils.Utils;

import java.util.List;

import okhttp3.internal.Util;

public class WorkListFilterRVAdapter extends RecyclerView.Adapter<WorkListFilterRVAdapter.ViewHolder> {

    private Context context;
    private String[] filters;
    private MyWorkListView myWorkListView;

    public WorkListFilterRVAdapter(Context context, String[] filters, MyWorkListView myWorkListView) {
        this.context = context;
        this.filters = filters;
        this.myWorkListView = myWorkListView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_filter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.cb.setText(filters[position] + " (" + Utils.getKeteranganDocType().get(filters[position]) + ")");

        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                myWorkListView.filterChecked(position, b);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filters.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox cb;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cb = itemView.findViewById(R.id.listItemFilterCB);
        }
    }
}
