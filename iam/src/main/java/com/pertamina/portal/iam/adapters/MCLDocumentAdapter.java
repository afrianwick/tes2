package com.pertamina.portal.iam.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.interfaces.MCLView;
import com.pertamina.portal.iam.models.MCLDocumentModel;

import java.util.List;

public class MCLDocumentAdapter extends RecyclerView.Adapter<MCLDocumentAdapter.ViewHolder> {

    private Context context;
    private List<MCLDocumentModel> mclDocumentModels;
    private MCLView mclView;

    public MCLDocumentAdapter(Context context, List<MCLDocumentModel> mclDocumentModels, MCLView mclView) {
        this.context = context;
        this.mclDocumentModels = mclDocumentModels;
        this.mclView = mclView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_mcl_document, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.nameTV.setText(String.valueOf(position + 1) + ". " + context.getResources().getStringArray(R.array.berkas_dokumen_mcl)[Integer.parseInt(mclDocumentModels.get(position).getName().split("_")[0])]);

        holder.downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("mcldocumentmodelsurl", mclDocumentModels.get(position).getUrl().replace("\\", "/"));
                Log.d("mcldocumentmodelsname", mclDocumentModels.get(position).getName());
                mclView.onDownloadButtonClicked(mclDocumentModels.get(position).getUrl().replace("\\", "/"), mclDocumentModels.get(position).getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mclDocumentModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTV;
        private RelativeLayout downloadButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.listItemMCLDocumentNameTV);
            downloadButton = itemView.findViewById(R.id.listItemMCLDocumentDownloadButton);
        }
    }
}
