package com.yonatan.asusx541u.pacPrayerTime.presenter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.yonatan.asusx541u.pacPrayerTime.R;
import com.yonatan.asusx541u.pacPrayerTime.model.ItemCategory;

import java.util.ArrayList;

/**
 * Created by asusX541u on 14/04/2018.
 */

public class BusinessAdapter extends BaseAdapter {

    private Context context;
    //ArrayList<String> listCategory = new ArrayList<>();
    ArrayList<ItemCategory> listCategory = new ArrayList<>();

    public BusinessAdapter(Context context, ArrayList<ItemCategory> listCategory) {
        this.context = context;
        this.listCategory = listCategory;
    }

    @Override
    public int getCount() {
        return listCategory.size();
    }

    @Override
    public Object getItem(int position) {
        return listCategory.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.row_item_grid_category, null);
        }
        TextView tvGridCategory = (TextView) convertView.findViewById(R.id.tvItemGrid);
        final ImageView ivGridCategory = (ImageView) convertView.findViewById(R.id.ivItemGrid);
        tvGridCategory.setText(listCategory.get(position).getNameCategory());
        Picasso.with(context).load(listCategory.get(position).getLinkIcon()).networkPolicy(NetworkPolicy.OFFLINE).resize(100,100).into(ivGridCategory, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(context).load(listCategory.get(position).getLinkIcon()).resize(100,100).into(ivGridCategory);
            }
        });
        return convertView;
    }
}
