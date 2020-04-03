package com.pertamina.portal.iam.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.LeaveRequestActivity;
import com.pertamina.portal.iam.models.AdditionalParticipantModel;

import java.util.List;

public class AdditionalParticipantAdapter extends RecyclerView.Adapter<AdditionalParticipantAdapter.ViewHolder> {

    private Context context;
    private List<AdditionalParticipantModel> additionalParticipantModelList;
    private AlertDialog alertDialog;
    private boolean isAllowDeleted;

    public AdditionalParticipantAdapter(Context context, List<AdditionalParticipantModel> additionalParticipantModelList, boolean isAllowDeleted) {
        this.context = context;
        this.additionalParticipantModelList = additionalParticipantModelList;
        this.isAllowDeleted = isAllowDeleted;
        buildAlert();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_participant, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        AdditionalParticipantModel additionalParticipantModel = additionalParticipantModelList.get(position);

        holder.deleteIV.setVisibility(View.GONE);
        if (isAllowDeleted) {
            holder.deleteIV.setVisibility(View.VISIBLE);
        }

        holder.nameTV.setText(additionalParticipantModel.getName());
        holder.noteTV.setText(additionalParticipantModel.getNote());
        holder.deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.setMessage("Apakah anda yakin ingin menghapus data?");
                alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        additionalParticipantModelList.remove(position);
                        LeaveRequestActivity.lvReqAdditionalParticipantFragment.onUpdateItem();
                        notifyItemRemoved(position);
                        //this line below gives you the animation and also updates the
                        //list items after the deleted item
                        notifyItemRangeChanged(position, getItemCount());
                    }
                });
                alertDialog.show();
            }
        });
    }

    public void buildAlert() {
        alertDialog = new AlertDialog.Builder(context)
                .setNeutralButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
    }

    @Override
    public int getItemCount() {
        return additionalParticipantModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTV, noteTV;
        private ImageView deleteIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.listItemParticipantNameTV);
            noteTV = itemView.findViewById(R.id.listItemParticipantNoteTV);
            deleteIV = itemView.findViewById(R.id.listItemParticipantDeleteIV);
        }
    }
}
