package com.pertamina.portal.iam.adapters.worklist;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pertamina.portal.iam.models.CostCenter;
import com.pertamina.portal.iam.models.CostCenter;

import java.util.List;

public class CostCenterAdapter extends ArrayAdapter<CostCenter> {

    private Context context;
    private List<CostCenter> values;

    public CostCenterAdapter(Context context, int textViewResourceId, List<CostCenter> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount(){
        return values.size();
    }

    @Override
    public CostCenter getItem(int position){
        return values.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText("[" + values.get(position).PKOSTLM + "] " + values.get(position).PVERAKM);

        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText("[" + values.get(position).PKOSTLM + "] " + values.get(position).PVERAKM);

        return label;
    }
}
