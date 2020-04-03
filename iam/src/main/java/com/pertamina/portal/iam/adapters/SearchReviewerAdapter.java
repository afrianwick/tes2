package com.pertamina.portal.iam.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.interfaces.SearchEmployeeView;
import com.pertamina.portal.iam.models.ReviewerModel;

import java.util.List;

public class SearchReviewerAdapter extends RecyclerView.Adapter<SearchReviewerAdapter.ViewHolder> {

    private Context context;
    private List<ReviewerModel> reviewers;
    private SearchEmployeeView searchEmployeeView;
    private int lastPosition = -1;

    public SearchReviewerAdapter(Context context, List<ReviewerModel> reviewers, SearchEmployeeView searchEmployeeView) {
        this.context = context;
        this.reviewers = reviewers;
        this. searchEmployeeView = searchEmployeeView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_search_reviewer, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final ReviewerModel reviewerModel = reviewers.get(position);

        holder.nameTV.setText(reviewerModel.getName() + " / " + reviewerModel.getPersonelNumber());

        holder.checkedRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (lastPosition == position) {
                    return;
                }
                searchEmployeeView.onEmployeeClicked(reviewerModel, lastPosition);
                lastPosition = position;
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTV;
        private RadioButton checkedRB;
        private LinearLayout containerLL;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.listItemSearchReviewerNameTV);
            checkedRB = itemView.findViewById(R.id.listItemSearchReviewerRB);
            containerLL = itemView.findViewById(R.id.listItemReviewerContainerLL);
        }
    }
}
