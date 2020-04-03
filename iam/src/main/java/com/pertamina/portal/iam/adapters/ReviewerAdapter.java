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
import com.pertamina.portal.iam.fragments.LeaveReqReviewerFragment;
import com.pertamina.portal.iam.models.ReviewerModel;

import java.util.List;

public class ReviewerAdapter extends RecyclerView.Adapter<ReviewerAdapter.ViewHolder> {

    private List<ReviewerModel> reviewerModelList;
    private Context context;
    private AlertDialog alertDialog;
    private boolean isAllowDeleted;

    public ReviewerAdapter(List<ReviewerModel> reviewerModelList, Context context, boolean isAllowDeleted) {
        this.reviewerModelList = reviewerModelList;
        this.context = context;
        this.isAllowDeleted = isAllowDeleted;
        buildAlert();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_reviewer, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        ReviewerModel reviewerModel = reviewerModelList.get(position);

        holder.nameTV.setText(reviewerModel.getName());
        holder.personalNumberTV.setText(reviewerModel.getPersonelNumber());
        holder.adUserNameTV.setText(reviewerModel.getAdUserName());
        holder.positionTV.setText(reviewerModel.getPosition());

        holder.deleteIV.setVisibility(View.GONE);
        if (isAllowDeleted) {
            holder.deleteIV.setVisibility(View.VISIBLE);
        }

        holder.deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.setMessage("Apakah anda yakin ingin menghapus data?");
                alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        reviewerModelList.remove(position);
                        LeaveRequestActivity.lvReqReviewerFragment.onUpdateItem();
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
        return reviewerModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTV, adUserNameTV, personalNumberTV, positionTV;
        private ImageView deleteIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.listItemReviewerNameTV);
            adUserNameTV = itemView.findViewById(R.id.listItemReviewerAdUsernameTV);
            personalNumberTV = itemView.findViewById(R.id.listItemReviewerPersonalNumberTV);
            positionTV = itemView.findViewById(R.id.listItemReviewerPositionTV);
            deleteIV = itemView.findViewById(R.id.listItemReviewerDeleteIV);
        }
    }
}
