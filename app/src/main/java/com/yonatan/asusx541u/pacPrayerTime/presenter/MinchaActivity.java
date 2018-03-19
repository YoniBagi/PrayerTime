package com.yonatan.asusx541u.pacPrayerTime.presenter;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class MinchaActivity extends AppCompatActivity {

    private DatabaseReference mDataBase;
    private ListView mListView;
    ArrayList<Prayer> mListPrayer = new ArrayList<Prayer>();
    static final int DIALOG_ID=0;
    Prayer mPrayer;
    int hour_x, minute_x;
    String str_minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mincha);
        toolbar();
        mListView = (ListView) findViewById(R.id.listViewMincha);
        final ArrayAdapter<Prayer>prayerArrayAdapter  = new ArrayAdapter<Prayer>(this, R.layout.row_item_s, mListPrayer){
            //Function that present data inside the list, custome listView.
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if(convertView == null){
                    convertView = View.inflate(MinchaActivity.this, R.layout.row_item_s, null);

                }
                TextView prayerPlace = (TextView) convertView.findViewById(R.id.textView13);
                TextView prayerTime = (TextView) convertView.findViewById(R.id.textView6);
                prayerPlace.setText(mListPrayer.get(position).getPlace());
                prayerTime.setText(mListPrayer.get(position).getTime());
                return convertView;
            }
        };
        mListView.setAdapter(prayerArrayAdapter);
        mDataBase = FirebaseDatabase.getInstance().getReference().child("mincha");
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> mapMincha = (Map<String, Object>) dataSnapshot.getValue();
              //  Map<String, Object> treeMincha =new TreeMap<String,Object>(mapMincha);
                mListPrayer.clear();
                for (Map.Entry<String, Object> entry : mapMincha.entrySet()){
                    Map singlePrayer = (Map) entry.getValue();
                    Prayer prayer = new Prayer((String) singlePrayer.get("place"), (String) singlePrayer.get("time"));
                    mListPrayer.add(prayer);
                   // prayerArrayAdapter.notifyDataSetChanged();
                }
                Collections.sort(mListPrayer);
                prayerArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listViewListener();
    }

    private void listViewListener() {
        /*annonimous class
        * Handle event that user click long on item and when it's appen
        *  pop time picker dialog with parmter equal to zero, let me know if it's opened or is first time
        *  mPrayer = object of class Prayer that beloning to position, the specific item that clicked
        * */
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDialog(DIALOG_ID);
                mPrayer = (Prayer) parent.getItemAtPosition(position);
                return true;
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id){
        if(id == DIALOG_ID)
            return new TimePickerDialog(MinchaActivity.this,R.style.DialogTheme,kTimePickerListener, hour_x, minute_x,true);
        return null;
    }

    protected TimePickerDialog.OnTimeSetListener kTimePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hour_x = hourOfDay;
            minute_x = minute;
            //Need compare int to string because when I setValue in DB it's will need string
            str_minute = String.valueOf(minute_x);
            // the value that come from time picker is int and the value of 01-09 become 1-9 and DB write 01-09, exm: 6:05 and no 6:5
            if(minute_x >= 0 && minute_x <= 9)
                str_minute = "0" + minute_x;

            mDataBase = FirebaseDatabase.getInstance().getReference().child("mincha");
            mDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Passes on all children of the root
                    //each prayerSnapshot handle <K,V> <string, object> <01,{time:6:00, place:...}>
                    for (DataSnapshot prayerSnapshot : dataSnapshot.getChildren()){
                        //singleP handle <K,V> <string, object> <{time:6:00, place:...}>
                        Map singleP = (Map) prayerSnapshot.getValue();
                        //search where is spesific prayer that user clicked
                        if(mPrayer.getPlace().equals(singleP.get("place")) && mPrayer.getTime().equals(singleP.get("time"))){
                            mDataBase.child(prayerSnapshot.getKey().toString()).child("time").setValue(hour_x + ":" + str_minute);
                            Toast.makeText(MinchaActivity.this, "זמן תפילה עודכן משעה " + mPrayer.getTime() + " לשעה " + hour_x + ":" + str_minute , Toast.LENGTH_LONG).show();
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
        actionBar.setTitle("מנחה");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    //When user push on back button on toolbar
    public  boolean onOptionsItemSelected(MenuItem item){
        /* in this case startActivity will launch second activity and
         when you will press back from second activity you will be
         thrown back to first activity(where you pressed action bar icon)*/

        //        Intent intent = new Intent(MinchaActivity.this, MainActivity.class);
        //        startActivity(intent);
            finish();
        return true;
    }
}
