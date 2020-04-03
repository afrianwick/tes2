package com.pertamina.portal.iam.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pertamina.portal.iam.R;
import com.pertamina.portal.iam.models.EcorrMenu;

import java.util.List;

public class MenuEcorrAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<EcorrMenu> menus;

    public MenuEcorrAdapter(Context context, List<EcorrMenu> menus) {
        this.mContext = context;
        this.menus = menus;
    }

    @Override
    public int getCount() {
        return menus.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final EcorrMenu menu = menus.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.item_menu, null);
        }

        final ImageView ivMenu = (ImageView)convertView.findViewById(R.id.ivMenu);
        final TextView tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
        final TextView tvCounter = convertView.findViewById(R.id.menu_counter);

        ivMenu.setImageResource(menu.iconRespurce);
        tvTitle.setText(menu.title);

        if (menu.counter != null) {
            tvCounter.setText(menu.counter);
            tvCounter.setVisibility(View.VISIBLE);
        } else {
            tvCounter.setVisibility(View.GONE);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(menu.intent);
            }
        });

        return convertView;
    }
}
