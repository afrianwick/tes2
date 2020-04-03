package com.pertamina.portal.iam.adapters.PANAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.models.PANModel.FormalEducation;
import com.pertamina.portal.iam.utils.Utils;

import java.util.List;

import static com.pertamina.portal.iam.adapters.PANAdapter.AddressAdapter.status;

public class FormalEducationAdapter extends RecyclerView.Adapter<FormalEducationAdapter.ViewHolder> {

    private Context context;
    private List<FormalEducation> formalEducationList;

    public FormalEducationAdapter(Context context, List<FormalEducation> formalEducationList) {
        this.context = context;
        this.formalEducationList = formalEducationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_pan_education_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.statusTV.setText(status(formalEducationList.get(position).getAction()));
        holder.startDateTV.setText(Utils.formatDate(formalEducationList.get(position).getStartDate(), "dd MMM yyyy", "yyyyMMdd"));
        holder.endDateTV.setText(Utils.formatDate(formalEducationList.get(position).getEndDate(), "dd MMM yyyy", "yyyyMMdd"));
        holder.educationEstablishmentTV.setText(formalEducationList.get(position).getEducationalEstablishment());
        holder.branchOfStudyTV.setText(formalEducationList.get(position).getBranchOfStudy());
        holder.instituteLocationTV.setText(formalEducationList.get(position).getInstitueLocation());
    }

    @Override
    public int getItemCount() {
        return formalEducationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView statusTV, educationEstablishmentTV, instituteLocationTV, startDateTV, endDateTV, branchOfStudyTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            statusTV = itemView.findViewById(R.id.tvActionType);
            startDateTV = itemView.findViewById(R.id.tvStartDate);
            endDateTV = itemView.findViewById(R.id.tvEndDate);
            educationEstablishmentTV = itemView.findViewById(R.id.tvEstablishment);
            branchOfStudyTV = itemView.findViewById(R.id.tvBranchStudy);
            instituteLocationTV = itemView.findViewById(R.id.tvInsLocation);
        }
    }
}
