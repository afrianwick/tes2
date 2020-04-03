package com.pertamina.portal.iam.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.core.utils.StringUtils;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.fragments.ListWorklistFragment.OnListFragmentInteractionListener;
import com.pertamina.portal.iam.models.TaskPending;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link TaskPending} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ListWorklistPendingAdapter extends RecyclerView.Adapter<ListWorklistPendingAdapter.ViewHolder> {

    private final List<TaskPending> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final Activity mActivity;

    public ListWorklistPendingAdapter(Activity activity, List<TaskPending> items, OnListFragmentInteractionListener listener) {
        mActivity = activity;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_worklist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
//        holder.mIdView.setText(mValues.get(position).id);
//        holder.mContentView.setText(mValues.get(position).folioNumber);

        String strDate = StringUtils.formatDateSimple(holder.mItem.requestDate);

        holder.tvFolioNumber.setText(holder.mItem.folioNumber);
        holder.tvTitle.setText(holder.mItem.lastActivity);
        holder.tvProcessName.setText(holder.mItem.processName);
        holder.tvDate.setText(strDate);
        holder.tvCreatedBy.setText(holder.mItem.karyawan.fullName);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem, position);
                }
            }
        });

//        if ((mValues.size() > 0 ) && (position == (mValues.size() - 1))) {
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//            layoutParams.setMargins(30, 20, 30, 24);
//
//            holder.mView.setLayoutParams(layoutParams);
//
//            Log.d("ListWorklistAdapter", "set margin");
//        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
//        public final TextView mIdView;
        public final TextView mContentView;
        private final TextView tvFolioNumber;
        private final TextView tvTitle;
        private final TextView tvProcessName;
        private final TextView tvDate, tvCreatedBy;
        public TaskPending mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
//            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
            tvFolioNumber = (TextView) view.findViewById(R.id.tvFolioNumber);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvProcessName = (TextView) view.findViewById(R.id.tvProcessName);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvCreatedBy = (TextView) view.findViewById(R.id.tvCreatedBy);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
