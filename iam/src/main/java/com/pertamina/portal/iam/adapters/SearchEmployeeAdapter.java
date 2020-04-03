package com.pertamina.portal.iam.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.models.Karyawan;

import java.util.ArrayList;
import java.util.List;

public class SearchEmployeeAdapter extends ArrayAdapter<Karyawan> {
    private Context context;
    private int resourceId;
    private List<Karyawan> items, tempItems, suggestions;

    public SearchEmployeeAdapter(@NonNull Context context, int resourceId, List<Karyawan> items) {
        super(context, resourceId, items);
        this.items = items;
        this.context = context;
        this.resourceId = resourceId;
        tempItems = new ArrayList<>();
        suggestions = new ArrayList<>();

        tempItems.addAll(items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                view = inflater.inflate(resourceId, parent, false);
            }

            Karyawan karyawan = getItem(position);
            TextView name = (TextView) view.findViewById(R.id.tvEmployee);

            Log.d(SearchEmployeeAdapter.class.getSimpleName(), "name : " + karyawan.fullName);

            name.setText(karyawan.fullName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Nullable
    @Override
    public Karyawan getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return karyawanFilter;
    }
    private Filter karyawanFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            Karyawan karyawan = (Karyawan) resultValue;
            return karyawan.fullName;
        }
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                suggestions.clear();

                for (Karyawan karyawan: tempItems) {
                    if (karyawan.fullName.toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
                        suggestions.add(karyawan);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                FilterResults filterResults = new FilterResults();
                return filterResults;
            }
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ArrayList<Karyawan> tempValues = (ArrayList<Karyawan>) filterResults.values;

            if (filterResults != null && filterResults.count > 0) {
                clear();
                for (Karyawan karyawan : tempValues) {
                    add(karyawan);
                    notifyDataSetChanged();
                }
            } else {
                clear();
                notifyDataSetChanged();
            }
        }
    };
}
