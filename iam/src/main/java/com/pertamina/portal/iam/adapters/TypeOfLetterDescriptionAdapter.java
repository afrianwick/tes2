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

public class TypeOfLetterDescriptionAdapter extends RecyclerView.Adapter<TypeOfLetterDescriptionAdapter.ViewHolder> {

    private Context context;
    private ReqSuratKetView reqSuratKetView;

    public TypeOfLetterDescriptionAdapter(Context context, ReqSuratKetView reqSuratKetView) {
        this.context = context;
        this.reqSuratKetView = reqSuratKetView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_type_of_letter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (position != 0) {
            holder.headerLL.setVisibility(View.GONE);
        }

        holder.numberTV.setText(String.valueOf(position + 1));
        holder.nameTV.setText(context.getResources().getStringArray(R.array.reference_letter_description_kind)[position]);
        holder.descriptionTV.setText(context.getResources().getStringArray(R.array.reference_letter_description)[position]);
        holder.containerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reqSuratKetView.onTypeOfLetterClicked(position);
            }
        });

        if (position == 4 || position == 5) {
            holder.numberTV.setTextColor(context.getResources().getColor(R.color.white));
            holder.nameTV.setTextColor(context.getResources().getColor(R.color.white));
            holder.descriptionTV.setTextColor(context.getResources().getColor(R.color.white));
            holder.containerLL.setBackgroundColor(context.getResources().getColor(R.color.disable));
        }
    }

    @Override
    public int getItemCount() {
        return context.getResources().getStringArray(R.array.reference_letter_description).length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView numberTV, nameTV, descriptionTV;
        private LinearLayout headerLL, containerLL;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            numberTV = itemView.findViewById(R.id.listItemTypeOfLetterNumberTV);
            containerLL = itemView.findViewById(R.id.listItemTypeOfLetterContainerLL);
            nameTV = itemView.findViewById(R.id.listItemTypeOfLetterNameTV);
            descriptionTV = itemView.findViewById(R.id.listItemTypeOfLetterDescriptionTV);
            headerLL = itemView.findViewById(R.id.listItemTypeOfLetterHeaderLL);
        }
    }
}
