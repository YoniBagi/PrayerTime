package com.yonatan.asusx541u.pacPrayerTime.presenter;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yonatan.asusx541u.pacPrayerTime.R;
import com.yonatan.asusx541u.pacPrayerTime.model.ItemCategory;

import java.util.ArrayList;
import java.util.Map;

public class BusinessActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    //ArrayList<String> listCategory = new ArrayList<>();
    ArrayList<ItemCategory> listCategory = new ArrayList<>();
    Map<String, Object> mapCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);
        toolbar();

        final GridView gridViewCategory = (GridView) findViewById(R.id.gridViewCategory);
        final BaseAdapter arrayAdapterCategory = new BusinessAdapter(getApplicationContext(),listCategory);
        gridViewCategory.setAdapter(arrayAdapterCategory);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("business").child("categories");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mapCategory = (Map<String, Object>) dataSnapshot.getValue();
                listCategory.clear();
                for(Map.Entry<String, Object> entry : mapCategory.entrySet()){
                    Map singCategory = (Map) entry.getValue();
                    ItemCategory itemCategory = new ItemCategory((String) singCategory.get("name_category"), (String) singCategory.get("link_icon"));
                    listCategory.add(itemCategory);
                }
                arrayAdapterCategory.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        gridViewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.findViewById(R.id.tvItemGrid).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.scale));
                //String itemChacked = (String) parent.getItemAtPosition(position);
                ItemCategory itemChacked = (ItemCategory) parent.getItemAtPosition(position);
                for(Map.Entry<String, Object> entry : mapCategory.entrySet()){
                    Map singCategory = (Map) entry.getValue();
                    if(itemChacked.getNameCategory().equals(singCategory.get("name_category"))){
                        Intent intent = new Intent(BusinessActivity.this, StoresActivity.class);
                        String temp = entry.getKey();
                        intent.putExtra("KIND_CATEGORY",entry.getKey());
                        intent.putExtra("NAME_CATEGORY", (String) singCategory.get("name_category"));
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void toolbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("עסקים מקומיים");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //When user push on back button on toolbar
    public  boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
}
