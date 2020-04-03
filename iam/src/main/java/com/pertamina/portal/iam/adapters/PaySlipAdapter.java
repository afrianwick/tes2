package com.pertamina.portal.iam.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.interfaces.PaySlipView;
import com.pertamina.portal.iam.models.Payslip;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaySlipAdapter extends RecyclerView.Adapter<PaySlipAdapter.ViewHolder> {

    private Context context;
    private List<Payslip> payslips;
    private PaySlipView paySlipView;

    public PaySlipAdapter(Context context, List<Payslip> payslips, PaySlipView paySlipView) {
        this.context = context;
        this.payslips = payslips;
        this.paySlipView = paySlipView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_country, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.nameTV.setText(payslips.get(position).payslipDesc);
        holder.nameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paySlipView.onPaySlipItemClicked(payslips.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return payslips.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.listItemCountryNameTV);

        }
    }

    public void updateList(List<Payslip> datas) {
        this.payslips = new ArrayList<>();
        this.payslips.addAll(datas);
        notifyDataSetChanged();
    }
}
