package com.pertamina.portal.iam.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.WebViewActivity;
import com.pertamina.portal.iam.fragments.SuratKeterangan.FragmentSKetVisa;
import com.pertamina.portal.iam.interfaces.FragmentSKetVisaView;
import com.pertamina.portal.iam.models.HistorySuratKeteranganModel;
import com.pertamina.portal.iam.utils.Utils;
import com.pertamina.portal.iam.utils.WebViewUtil;

import java.util.List;

public class HistorySuratKeteranganAdapter extends RecyclerView.Adapter<HistorySuratKeteranganAdapter.ViewHolder> {

    private Context context;
    private List<HistorySuratKeteranganModel> historySuratKeteranganModels;
    private FragmentSKetVisaView fragmentSKetVisaView;

    public HistorySuratKeteranganAdapter(Context context, FragmentSKetVisaView fragmentSKetVisaView, List<HistorySuratKeteranganModel> historySuratKeteranganModels) {
        this.context = context;
        this.fragmentSKetVisaView = fragmentSKetVisaView;
        this.historySuratKeteranganModels = historySuratKeteranganModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_history_surat_keterangan, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.typeTV.setText(historySuratKeteranganModels.get(position).getType());
        holder.nameTV.setText(historySuratKeteranganModels.get(position).getName());
        holder.approvalDateTV.setText(Utils.formatDate(historySuratKeteranganModels.get(position).getApprovalDate(), "dd MMM yyyy", "yyyyMMdd"));
        holder.expiredDateTV.setText(Utils.formatDate(historySuratKeteranganModels.get(position).getExpiredDate(), "dd MMM yyyy", "yyyyMMdd"));
        holder.containerCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, WebViewActivity.class);
//                intent.putExtra("DocumentName", historySuratKeteranganModels.get(position).getName());
//                intent.putExtra("PersonID", historySuratKeteranganModels.get(position).getPersonID());
//                context.startActivity(intent);
                fragmentSKetVisaView.onSketItemClicked(historySuratKeteranganModels.get(position).getName(), historySuratKeteranganModels.get(position).getPersonID());
            }
        });
    }

    @Override
    public int getItemCount() {
        return historySuratKeteranganModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTV, typeTV, approvalDateTV, expiredDateTV;
        private CardView containerCV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.txt_document_name);
            typeTV = itemView.findViewById(R.id.txt_document_type);
            approvalDateTV = itemView.findViewById(R.id.txt_approval_date);
            expiredDateTV = itemView.findViewById(R.id.txt_expiry_date);
            containerCV = itemView.findViewById(R.id.row_container_surat_keterangan);
        }
    }
}
