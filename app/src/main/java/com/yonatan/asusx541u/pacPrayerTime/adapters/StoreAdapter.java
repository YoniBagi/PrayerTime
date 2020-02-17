package com.yonatan.asusx541u.pacPrayerTime.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yonatan.asusx541u.pacPrayerTime.R;
import com.yonatan.asusx541u.pacPrayerTime.model.Store;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by asusX541u on 16/04/2018.
 */

public class StoreAdapter extends BaseExpandableListAdapter {
    Context context;
    ArrayList<Store> storeArrayList = new ArrayList<>();
    LayoutInflater inflater;
    ArrayList<String> listOpeningHours = new ArrayList<>();

    public StoreAdapter(Context context, ArrayList<Store> storeArrayList) {
        this.context = context;
        this.storeArrayList = storeArrayList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return storeArrayList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return storeArrayList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return storeArrayList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.row_store_group, null);
        Store store = (Store) getGroup(groupPosition);
        TextView tvNameStore = (TextView) convertView.findViewById(R.id.tvNameStore);
        TextView tvNumberPhoneStore = (TextView) convertView.findViewById(R.id.tvNumberPhoneStore);
        TextView tvAdressStore = (TextView) convertView.findViewById(R.id.tvAdressStore);
        ImageView ivItemStore = (ImageView) convertView.findViewById(R.id.ivItemStore);

        tvNameStore.setText(storeArrayList.get(groupPosition).getName());
        tvNumberPhoneStore.setText(storeArrayList.get(groupPosition).getNumber_phone());
        //tvNumberPhoneStore.setTextIsSelectable(true);
        tvAdressStore.setText(storeArrayList.get(groupPosition).getAdress());
        Picasso.with(context).load(storeArrayList.get(groupPosition).getLink_icon_store()).placeholder(R.mipmap.ic_launcher_light).resize(100,100).into(ivItemStore);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.row_store_child, null);
        Store store = (Store) getGroup(groupPosition);
        Map<String,String> openingHours = store.getOpening_hours();
        //ListView lvOpeningHours = (ListView) convertView.findViewById(R.id.lv_openning_hours);
        TextView tvOpeningHours = (TextView) convertView.findViewById(R.id.tv_openning_hours);
        TextView tvNote = (TextView) convertView.findViewById(R.id.tv_note);
        TextView tvOpeningDay = (TextView) convertView.findViewById(R.id.tv_openning_day);
        listOpeningHours.clear();
       // ArrayAdapter<String> adapterOpenHours = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,listOpeningHours);
        //lvOpeningHours.setAdapter(adapterOpenHours);
        String textOpenHours="";
        String textOpenDay="";
        for(Map.Entry<String,String> entry : openingHours.entrySet()){
            //listOpeningHours.add(entry.getKey() + " " + entry.getValue());
            textOpenDay += entry.getKey() + "\n";
            textOpenHours += entry.getValue() + "\n";
        }
        tvNote.setText(store.getNote());
        tvOpeningHours.setText(textOpenHours);
        tvOpeningDay.setText(textOpenDay);
        //adapterOpenHours.notifyDataSetChanged();
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
