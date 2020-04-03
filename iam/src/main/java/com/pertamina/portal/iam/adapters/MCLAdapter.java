package com.pertamina.portal.iam.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.interfaces.ReqSuratKetView;

public class MCLAdapter extends RecyclerView.Adapter<MCLAdapter.ViewHolder> {

    private Context context;

    public MCLAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_type_of_letter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (position == 0) {
            holder.header1TV.setText("Tipe klaim");
            holder.header2TV.setText("Penjelasan");
        }
        if (position != 0) {
            holder.headerLL.setVisibility(View.GONE);
        }

        holder.numberTV.setText(String.valueOf(position + 1));
        holder.nameTV.setText(context.getResources().getStringArray(R.array.mcl_)[position]);
        holder.descriptionTV.setText(context.getResources().getStringArray(R.array.mcl_2)[position]);

    }

    @Override
    public int getItemCount() {
        return context.getResources().getStringArray(R.array.mcl_).length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView numberTV, nameTV, descriptionTV, header1TV, header2TV;
        private LinearLayout headerLL, containerLL;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            numberTV = itemView.findViewById(R.id.listItemTypeOfLetterNumberTV);
            containerLL = itemView.findViewById(R.id.listItemTypeOfLetterContainerLL);
            nameTV = itemView.findViewById(R.id.listItemTypeOfLetterNameTV);
            descriptionTV = itemView.findViewById(R.id.listItemTypeOfLetterDescriptionTV);
            headerLL = itemView.findViewById(R.id.listItemTypeOfLetterHeaderLL);
            header1TV = itemView.findViewById(R.id.listItemTypeOfLetterHeader1TV);
            header2TV = itemView.findViewById(R.id.listItemTypeOfLetterHeader2TV);
        }
    }
}
