package com.yonatan.asusx541u.pacPrayerTime.presenter;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yonatan.asusx541u.pacPrayerTime.R;
import com.yonatan.asusx541u.pacPrayerTime.model.Store;

import java.util.ArrayList;
import java.util.Map;

public class StoresActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    String kind_category, name_category;
    ExpandableListView expandableLVStore;
    ArrayList<Store> storeArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores);
        Intent intent = getIntent();
        kind_category = intent.getStringExtra("KIND_CATEGORY");
        name_category = intent.getStringExtra("NAME_CATEGORY");
        toolbar();


        final StoreAdapter storeAdapter = new StoreAdapter(getApplicationContext(),storeArrayList);
        expandableLVStore = (ExpandableListView) findViewById(R.id.expand_list_store);
        expandableLVStore.setAdapter(storeAdapter);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("business").child("categories").child(kind_category);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                storeArrayList.clear();
                Map<String,Object> mapCategory = (Map<String,Object>) dataSnapshot.getValue();
                for(Map.Entry<String,Object> entry : mapCategory.entrySet()){
                    if(!entry.getKey().equals("name_category") && !entry.getKey().equals("link_icon")){
                        Map singStore = (Map) entry.getValue();
                        Store store = new Store(
                                (String)singStore.get("adress"),
                                (String)singStore.get("name_store"),
                                (String)singStore.get("note"),
                                (String)singStore.get("number"),
                                (Map)singStore.get("opening_hours")
                        );
                        storeArrayList.add(store);
                        storeAdapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void toolbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(name_category);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public  boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
}
