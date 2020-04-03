package com.pertamina.portal.iam.adapters;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.fragments.RecentTaskFragment.OnListFragmentInteractionListener;
import com.pertamina.portal.iam.models.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private final List<Task> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final Context context;

    public TaskAdapter(Context context, List<Task> items, OnListFragmentInteractionListener listener) {
        this.context = context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = mValues.get(position);
        int statusVisibility = holder.item.statusNotif ? View.VISIBLE : View.INVISIBLE;
        boolean docStatusColor = holder.item.processName.equalsIgnoreCase("Manager Approval");

        holder.ivStatus.setVisibility(statusVisibility);
        holder.tvTitle.setText(holder.item.processName);
        holder.tvFolioNum.setText(holder.item.folioNumber);
        holder.tvInfo.setText(holder.item.lastActivity);

        if (docStatusColor) {
            holder.tvTitle.setTextColor(context.getResources().getColor(R.color.iamGreenLight));
        } else {
            holder.tvTitle.setTextColor(context.getResources().getColor(R.color.iamYellowSmooth));
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final CardView mContentView;
        private final ImageView ivStatus;
        private final TextView tvTitle;
        private final TextView tvFolioNum;
        private final TextView tvInfo;
        public Task item;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (CardView) view.findViewById(R.id.cvWraper);
            ivStatus = (ImageView) view.findViewById(R.id.ivNotifStatus);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvFolioNum = (TextView) view.findViewById(R.id.tvFolioNumber);
            tvInfo = (TextView) view.findViewById(R.id.tvCreatedBy);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvFolioNum.getText() + "'";
        }
    }
}
