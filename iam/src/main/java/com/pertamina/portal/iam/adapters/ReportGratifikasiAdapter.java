package com.pertamina.portal.iam.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.models.ReportGratifikasiModel;
import com.pertamina.portal.iam.utils.Utils;

import java.util.List;

public class ReportGratifikasiAdapter extends RecyclerView.Adapter<ReportGratifikasiAdapter.ViewHolder> {

    private Context context;
    private List<ReportGratifikasiModel> reportGratifikasiModelList;
    private String type;

    public ReportGratifikasiAdapter(Context context, List<ReportGratifikasiModel> reportGratifikasiModelList, String type) {
        this.context = context;
        this.reportGratifikasiModelList = reportGratifikasiModelList;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_report_gratifikasi, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.dateTV.setText(Utils.formatDate(reportGratifikasiModelList.get(position).getDate(), "dd MMM yyyy", "dd-MM-yyyy"));
        holder.valueTV.setText(reportGratifikasiModelList.get(position).getValue());
        holder.tanggalGratifikasiTV.setText(Utils.formatDate(reportGratifikasiModelList.get(position).getTglGratifikasi(), "dd MMM yyyy", "dd-MM-yyyy"));
        holder.jenisTV.setText(reportGratifikasiModelList.get(position).getJenis());
        holder.nilaiTV.setText(reportGratifikasiModelList.get(position).getNilaiGratifikasi());
        holder.pemintaTV.setText(reportGratifikasiModelList.get(position).getPeminta());
        holder.alasanTV.setText(reportGratifikasiModelList.get(position).getAlasan());
        holder.typeTV.setText(type);
    }

    @Override
    public int getItemCount() {
        return reportGratifikasiModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView dateTV, valueTV, tanggalGratifikasiTV, jenisTV, nilaiTV, pemintaTV, alasanTV, typeTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dateTV = itemView.findViewById(R.id.txt_report_date);
            valueTV = itemView.findViewById(R.id.txt_report_status);
            tanggalGratifikasiTV = itemView.findViewById(R.id.txt_gratifikasi_date);
            jenisTV = itemView.findViewById(R.id.txt_jenis);
            nilaiTV = itemView.findViewById(R.id.txt_jumlah);
            pemintaTV = itemView.findViewById(R.id.txt_peminta);
            alasanTV = itemView.findViewById(R.id.txt_alasan);
            typeTV = itemView.findViewById(R.id.txt_type);
        }
    }
}
