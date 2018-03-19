package com.yonatan.asusx541u.pacPrayerTime.presenter;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
//import android.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yonatan.asusx541u.pacPrayerTime.model.Prayer;
import com.yonatan.asusx541u.pacPrayerTime.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class SahritActivity extends AppCompatActivity {

    private DatabaseReference mDataBase;
    private ListView mListView;
    ArrayList<Prayer> placeList = new ArrayList<Prayer>();
    static final int DIALOG_ID = 0;
    private int hour_x, miutus_x;
    Prayer mPrayer;
    String str_minuts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sahrit);
        toolbar();
        mListView = (ListView) findViewById(R.id.list_sahrit);
        mListView.setLongClickable(true);
        final ArrayAdapter<Prayer> arrayAdapter = new ArrayAdapter<Prayer>(this, R.layout.row_item_s, placeList){

            @Override
            public View getView(int position,  View convertView,  ViewGroup parent) {
                if (convertView == null){
                    convertView = View.inflate(SahritActivity.this,R.layout.row_item_s, null);
                    convertView.setMinimumHeight(20);
                }
                TextView prayPlace = (TextView) convertView.findViewById(R.id.textView13);
                TextView prayTime = (TextView) convertView.findViewById(R.id.textView6);
                prayPlace.setText(placeList.get(position).getPlace());
                prayTime.setText(placeList.get(position).getTime());
                return convertView;
            }
        };
        mListView.setAdapter(arrayAdapter);
        mDataBase = FirebaseDatabase.getInstance().getReference().child("sahrit");
        //Query mQuery = mDataBase.orderByKey(); //for sort, but not work.
        mDataBase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> tSharitPlace = (Map<String,Object>) dataSnapshot.getValue();
                /*The data that retrieve from firebase arrived not sorted so I enter the "tSharitPlace",that it is kind of
                  Map, to TreeMap and it is automatically put entries sorted by keys. in this case the key is the
                    childs of "sahrit"*/
             //   Map<String, Object> sharitPlace = new TreeMap<String, Object>(tSharitPlace);
                placeList.clear();
                for(Map.Entry<String, Object> entry : tSharitPlace.entrySet()) {
                    Map singlePray = (Map) entry.getValue();
                    Prayer prayer = new Prayer((String) singlePray.get("place"),(String) singlePray.get("time"));
                    placeList.add(prayer);

                }
                Collections.sort(placeList);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listViewListener();
    }

    protected void listViewListener(){
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDialog(DIALOG_ID);
                mPrayer = (Prayer) parent.getItemAtPosition(position);
//                Toast.makeText(SahritActivity.this,mPrayer.getPlace(),Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id){
        if(id == DIALOG_ID)
            return new TimePickerDialog(SahritActivity.this,R.style.DialogTheme,kTimePickerListener,hour_x,miutus_x,true);
        return null;
    }

    protected TimePickerDialog.OnTimeSetListener kTimePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hour_x = hourOfDay;
            miutus_x = minute;
            str_minuts = String.valueOf(miutus_x);
            if(miutus_x >= 0 && miutus_x <= 9)
                str_minuts = "0"+ miutus_x;

            mDataBase = FirebaseDatabase.getInstance().getReference().child("sahrit");
            mDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot prayerSnapshot : dataSnapshot.getChildren()){
                        Map singleP = (Map) prayerSnapshot.getValue();
                        if(mPrayer.getPlace().equals((String) singleP.get("place")) && mPrayer.getTime().equals((String) singleP.get("time"))) {
                            mDataBase.child(prayerSnapshot.getKey().toString()).child("time").setValue(hour_x + ":" + str_minuts);
                            Toast.makeText(SahritActivity.this, "זמן תפילה עודכן משעה " + mPrayer.getTime() + " לשעה " + hour_x + ":" + str_minuts , Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    };

    public void toolbar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("שחרית");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    //When user push on back button on toolbar
    public  boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }

    /*private void collectPlace(Map<String, Object> sharitPlace){


        for(Map.Entry<String, Object> entry : sharitPlace.entrySet()){
            Map singlePray = (Map) entry.getValue();

            placeList.add((String) singlePray.get("place"));

        }*/
    }


