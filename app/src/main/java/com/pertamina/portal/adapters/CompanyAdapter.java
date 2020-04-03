package com.pertamina.portal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.R;
import com.pertamina.portal.interfaces.LoginView;
import com.pertamina.portal.models.Company;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.ViewHolder> {

    private Context context;
    private List<Company> companies;
    private LoginView loginView;

    public CompanyAdapter(Context context, List<Company> companies, LoginView loginView) {
        this.context = context;
        this.companies = companies;
        this.loginView = loginView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_country, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameTV.setText(companies.get(position).getCompanyName());
        holder.nameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginView.onCompanyClicked(companies.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return companies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.listItemCountryNameTV) TextView nameTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void updateList(List<Company> datas) {
        this.companies = new ArrayList<>();
        this.companies.addAll(datas);
        notifyDataSetChanged();
    }
}
