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

import com.pertamina.portal.core.utils.StringUtils;
import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.activity.DokumenPreviewActivity;
import com.pertamina.portal.iam.interfaces.MyDocumentView;
import com.pertamina.portal.iam.models.MyDocumentData;

import java.util.List;

public class MyDocumentItemAdapter extends RecyclerView.Adapter<MyDocumentItemAdapter.ViewHolder> {

    private Context context;
    private List<MyDocumentData> datas;
    private MyDocumentView myDocumentView;

    public MyDocumentItemAdapter(Context context, List<MyDocumentData> datas, MyDocumentView myDocumentView) {
        this.context = context;
        this.datas = datas;
        this.myDocumentView = myDocumentView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_sub_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                final MyDocumentData data = (MyDocumentData) datas.get(position);
                int intDateOfIssue = data.dateOfIssue;
                String strRawDate = String.valueOf(intDateOfIssue);
                String strDateOfIssue = strRawDate != null ? StringUtils.reformatDateYyyyMmDd(strRawDate) : "null";

                holder.tvName.setText(data.name);
                holder.tvDocType.setText(data.documentType);
                holder.tvFilename.setText(data.filename);
                holder.tvDateUpload.setText(data.uploadDate);
                holder.tvDateOfIssue.setText(strDateOfIssue);
                holder.tvDocType2.setText(data.documentType);

                holder.cvPersonal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, DokumenPreviewActivity.class);
                        intent.putExtra(DokumenPreviewActivity.URL, data.action);
                        intent.putExtra(DokumenPreviewActivity.FILENAME, data.filename);
                        intent.putExtra(DokumenPreviewActivity.TGL_UPLOAD, data.uploadDate);
                        intent.putExtra(DokumenPreviewActivity.NAME, data.name);
                        context.startActivity(intent);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView tvName, tvDocType, tvFilename, tvDateUpload,
                tvDateOfIssue, tvDocType2;
        public final CardView cvPersonal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDocType = itemView.findViewById(R.id.tvDocType);
            tvFilename = itemView.findViewById(R.id.tvFilename);
            tvDateUpload = itemView.findViewById(R.id.tvDateUpload);
            tvDateOfIssue = itemView.findViewById(R.id.tvDateOfIssue);
            tvDocType2 = itemView.findViewById(R.id.tvDocType2);
            cvPersonal = itemView.findViewById(R.id.cvPersonal);
        }
    }
}
