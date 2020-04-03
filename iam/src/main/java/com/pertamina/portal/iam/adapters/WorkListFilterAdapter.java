package com.pertamina.portal.iam.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.interfaces.MyWorkListView;

import java.util.List;

public class WorkListFilterAdapter extends RecyclerView.Adapter<WorkListFilterAdapter.ViewHolder> {

    private Context context;
    private MyWorkListView myWorkListView;
    private List<String> filters;
    private List<Integer> filtersIndex;

    public WorkListFilterAdapter(Context context, MyWorkListView myWorkListView, List<String> filters, List<Integer> filtersIndex) {
        this.context = context;
        this.filters = filters;
        this.myWorkListView = myWorkListView;
        this.filtersIndex = filtersIndex;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_filter_work_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.nameTV.setText(filters.get(position));
        holder.deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myWorkListView.onDeleteFilter(filtersIndex.get(position));
                filters.remove(position);
                filtersIndex.remove(position);
                notifyItemRemoved(position);
                //this line below gives you the animation and also updates the
                //list items after the deleted item
                notifyItemRangeChanged(position, getItemCount());
                myWorkListView.onRefilter();
            }
        });
    }

    @Override
    public int getItemCount() {
        return filters.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView deleteIV;
        private TextView nameTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            deleteIV = itemView.findViewById(R.id.listItemFilterWorkListDeleteIV);
            nameTV = itemView.findViewById(R.id.listItemFilterWorkListNameTV);
        }
    }
}
