package com.pertamina.portal.iam.adapters.worklist;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.fragments.ListWorklistFragment.OnListFragmentInteractionListener;
import com.pertamina.portal.iam.models.IamComment;
import com.pertamina.portal.iam.models.worklist.PersonalIdData;
import com.pertamina.portal.iam.models.worklist.TaxData;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link IamComment} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PersonalIdAdapter extends RecyclerView.Adapter<PersonalIdAdapter.ViewHolder> {

    private static final String TAG = "UniformAdapter";
    private final List<PersonalIdData> list;
    private final Activity mActivity;

    public PersonalIdAdapter(Activity activity, List<PersonalIdData> items) {
        mActivity = activity;
        list = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pan_personal_id_detail, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        PersonalIdData data = list.get(position);

        holder.tvActionType.setText(data.actionType);
        holder.tvIdType.setText(data.idType);
        holder.tvIdValue.setText(data.idValue);
        holder.tvStartDate.setText(data.strStartDate);
        holder.tvEndDate.setText(data.strEndDate);

        Log.d(TAG, "data.npwp = " + data.idValue);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private final TextView tvIdType, tvIdValue, tvStartDate, tvEndDate, tvActionType;
        public PersonalIdData mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvIdType = (TextView) view.findViewById(R.id.itemTypeTV);
            tvIdValue = (TextView) view.findViewById(R.id.itemValueTV);
            tvStartDate = (TextView) view.findViewById(R.id.tvStartDate);
            tvEndDate = (TextView) view.findViewById(R.id.tvEndDate);
            tvActionType = (TextView) view.findViewById(R.id.tvActionType);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvIdValue.getText() + "'";
        }
    }
}
