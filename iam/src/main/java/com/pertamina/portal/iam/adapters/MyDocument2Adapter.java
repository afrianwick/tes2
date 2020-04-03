package com.pertamina.portal.iam.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.interfaces.MyDocumentView;
import com.pertamina.portal.iam.models.MyDocumentData;

import java.util.HashMap;
import java.util.List;

public class MyDocument2Adapter extends RecyclerView.Adapter<MyDocument2Adapter.ViewHolder> {

    private Context context;
    private HashMap<Integer, List<MyDocumentData>> hashMap;
    private MyDocumentView myDocumentView;

    public MyDocument2Adapter(Context context, MyDocumentView myDocumentView, HashMap<Integer, List<MyDocumentData>> hashMap) {
        this.context = context;
        this.myDocumentView = myDocumentView;
        this.hashMap = hashMap;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_item_document, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyDocument2Adapter.ViewHolder holder, final int position) {
        holder.tvDocumentType.setText(hashMap.get(position).get(0).name);

        holder.clContainerDocumentType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.items.isShown()) {
                    holder.items.setVisibility(View.GONE);
                } else {
                    holder.items.setVisibility(View.VISIBLE);
                }
            }
        });

        MyDocumentItemAdapter myDocumentItemAdapter = new MyDocumentItemAdapter(context, hashMap.get(position), myDocumentView);
        holder.items.setLayoutManager(new LinearLayoutManager(context));
        holder.items.setAdapter(myDocumentItemAdapter);
    }

    @Override
    public int getItemCount() {
        return hashMap.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView signIV;
        public final ConstraintLayout clContainerDocumentType;
        public final TextView tvDocumentType;
        public final RecyclerView items;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            signIV = itemView.findViewById(R.id.listItemMyDocumentExpandCollapseSignIV);
            clContainerDocumentType = itemView.findViewById(R.id.clContainerDocumentType);
            tvDocumentType = itemView.findViewById(R.id.tvDocumentType);
            items = itemView.findViewById(R.id.listItemItemRV);
        }
    }
}
