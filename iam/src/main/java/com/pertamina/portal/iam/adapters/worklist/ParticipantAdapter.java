package com.pertamina.portal.iam.adapters.worklist;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.models.worklist.Participant;
import com.pertamina.portal.iam.models.worklist.RmjData;

import java.util.List;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ViewHolder> {

    private static final String TAG = "BpjsAdapter";
    private final List<Participant> list;
    private final Activity mActivity;

    public ParticipantAdapter(Activity activity, List<Participant> items) {
        mActivity = activity;
        list = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_participant_lev_detail, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Participant data = list.get(position);

        holder.tvNama.setText(data.name);
        holder.tvNote.setText(data.note);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private final TextView tvNama, tvNote;
        public RmjData mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvNama = (TextView) view.findViewById(R.id.tvName);
            tvNote = (TextView) view.findViewById(R.id.tvNote);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvNama.getText() + "'";
        }
    }
}
