package com.pertamina.portal.iam.adapters.worklist;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.fragments.ListWorklistFragment.OnListFragmentInteractionListener;
import com.pertamina.portal.iam.models.IamComment;
import com.pertamina.portal.iam.models.worklist.EducationData;
import com.pertamina.portal.iam.models.worklist.UniformData;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link IamComment} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class UniformAdapter extends RecyclerView.Adapter<UniformAdapter.ViewHolder> {

    private static final String TAG = "UniformAdapter";
    private final List<UniformData> list;
    private final Activity mActivity;

    public UniformAdapter(Activity activity, List<UniformData> items) {
        mActivity = activity;
        list = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pan_uniform_detail, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        UniformData data = list.get(position);

        holder.tvActionType.setText("???");
        holder.tvUkuranBaju.setText(data.ukuranBaju);
        holder.tvUkuranCelana.setText(data.ukuranCelana);
        holder.tvUkuranSepatu.setText(data.ukuranSepatu);
        holder.tvStartDate.setText(data.strStartDate);
        holder.tvEndDate.setText(data.strEndDate);

        Log.d(TAG, "data.ukuranBaju = " + data.ukuranBaju);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private final TextView tvUkuranBaju, tvUkuranCelana, tvUkuranSepatu, tvStartDate, tvEndDate, tvActionType;
        public UniformData mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvUkuranBaju = (TextView) view.findViewById(R.id.itemBajuTV);
            tvUkuranCelana = (TextView) view.findViewById(R.id.itemCelanaTV);
            tvUkuranSepatu = (TextView) view.findViewById(R.id.itemSepatuTV);
            tvStartDate = (TextView) view.findViewById(R.id.tvStartDate);
            tvEndDate = (TextView) view.findViewById(R.id.tvEndDate);
            tvActionType = (TextView) view.findViewById(R.id.tvActionType);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvUkuranBaju.getText() + "'";
        }
    }
}
