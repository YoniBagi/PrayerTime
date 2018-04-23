package com.yonatan.asusx541u.pacPrayerTime.presenter;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Map;

public class PrayersActivity extends AppCompatActivity {

    private DatabaseReference mDataBase;
    private ListView mListView;
    ArrayList<Prayer> mListPrayer = new ArrayList<>();
    static final int DIALOG_ID=0;
    Prayer mPrayer;
    int hour_x, minute_x;
    String str_minute, kindPrayerToDB;
    private Calendar currentTime = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayers);

        Intent intent = getIntent();
        kindPrayerToDB = intent.getStringExtra(MainActivity.KIND_PRAYER);
        toolbar();
        mListView = (ListView) findViewById(R.id.listViewPrayers);
        final ArrayAdapter<Prayer> prayerArrayAdapter = new ArrayAdapter<Prayer>(this, R.layout.row_item_s,mListPrayer){
            @NonNull
            @Override
            public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if(convertView == null){
                    convertView = View.inflate(PrayersActivity.this, R.layout.row_item_s, null);
                }
                TextView prayerPlace = (TextView) convertView.findViewById(R.id.textView13);
                TextView prayerTime = (TextView) convertView.findViewById(R.id.textView6);
                ImageView imageViewWaze = (ImageView) convertView.findViewById(R.id.ivWaze);
                prayerPlace.setText(mListPrayer.get(position).getPlace());
                prayerTime.setText(mListPrayer.get(position).getTime());
                imageViewWaze.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uri;
                        switch (mListPrayer.get(position).getPlace()) {
                            case "מרכזי":
                                uri = "geo:0,0?q=31.801166,34.822807&navigate=yes";
                                break;
                            case "תורת החיים":
                                uri = "geo:0,0?q=31.798238, 34.820400&navigate=yes";
                                break;
                            case "שערי ציון":
                                uri = "geo:0,0?q=31.800334, 34.821704&navigate=yes";
                                break;
                            case "קהילתי":
                                uri = "geo:0,0?q=31.796926, 34.821508&navigate=yes";
                                break;
                            case "פעמוני זהב":
                                uri = "geo:0,0?q=31.799785, 34.821791&navigate=yes";
                                break;
                            case "שירת קטיף":
                                uri = "geo:0,0?q=31.801450, 34.822424&navigate=yes";
                                break;
                            case "ותיקים":
                                uri = "geo:0,0?q=31.797079, 34.821515&navigate=yes";
                                break;
                            case "ישיבה לצעירים-תורת החיים":
                                uri = "geo:0,0?q=31.797744, 34.820530&navigate=yes";
                                break;
                            case "משפחת ג'יבלי":
                                uri = "geo:0,0?q=31.793562, 34.825174&navigate=yes";
                                break;
                            case "ספריית הרמן":
                                uri = "geo:0,0?q=31.801450, 34.822424&navigate=yes";
                                break;
                            case "ישיבת נתיבות אש":
                                uri = "geo:0,0?q=31.794517, 34.820958&navigate=yes";
                                break;
                            case "בית חלקיה":
                                uri = "geo:0,0?q=31.791316, 34.809089&navigate=yes";
                                break;
                            case "חסדי דב":
                                uri = "geo:0,0?q=31.795556, 34.822620&navigate=yes";
                                break;
                            case "ליד בן כוכב":
                                uri = "geo:0,0?q=31.798528, 34.825005&navigate=yes";
                                break;
                            case "משפחת דהרי":
                                uri = "geo:0,0?q=31.800664, 34.819863&navigate=yes";
                                break;
                            case "מבקשי פניך-תורת החיים":
                                uri = "geo:0,0?q=31.797767, 34.819690&navigate=yes";
                                break;
                            case "מניין השביל דונה א":
                                uri = "geo:0,0?q=31.796479, 34.824293&navigate=yes";
                                break;
                            case "חפץ-חיים":
                                uri = "geo:0,0?q=31.789837, 34.798124&navigate=yes";
                                break;
                            default:
                                uri = "geo:0,0?q=31.801450, 34.822424&navigate=yes";
                        }
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(intent);
                    }
                });
                return convertView;
            }
        };
        mListView.setAdapter(prayerArrayAdapter);
        mDataBase = FirebaseDatabase.getInstance().getReference().child(kindPrayerToDB);
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> mapMincha = (Map<String, Object>) dataSnapshot.getValue();//mincha:1,2,3,4...
                //  Map<String, Object> treeMincha =new TreeMap<String,Object>(mapMincha);
                mListPrayer.clear();
                for (Map.Entry<String, Object> entry : mapMincha.entrySet()){ //1,2,3,4,5..
                    Map singlePrayer = (Map) entry.getValue();// place:"gbgb", time:"15:46"
                    Prayer prayer = new Prayer((String) singlePrayer.get("place"), (String) singlePrayer.get("time"));
                    mListPrayer.add(prayer);
                }
                if(currentTime.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY || currentTime.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY)
                    for(Prayer prayer: mListPrayer)
                    {
                        if(prayer.getPlace().equals("שערי ציון") && prayer.getTime().equals("6:00"))
                            prayer.setTime("5:50");
                        else if (prayer.getPlace().equals("מרכזי") && prayer.getTime().equals("6:00"))
                            prayer.setTime("5:50");

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
        *  pop time picker dialog with parmter equal to zero, I can to know if it's opened or is first time
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
            return new TimePickerDialog(PrayersActivity.this,R.style.DialogTheme,kTimePickerListener, hour_x, minute_x,true);
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

            mDataBase = FirebaseDatabase.getInstance().getReference().child(kindPrayerToDB);
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
                            Toast.makeText(PrayersActivity.this, "זמן תפילה עודכן משעה " + mPrayer.getTime() + " לשעה " + hour_x + ":" + str_minute , Toast.LENGTH_LONG).show();
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
        actionBar.setDisplayHomeAsUpEnabled(true);
        switch (kindPrayerToDB) {
            case "sahrit":
                actionBar.setTitle("שחרית");
                break;
            case "mincha":
                actionBar.setTitle("מנחה");
                break;
            case "arvit":
                actionBar.setTitle("ערבית");
                break;
        }

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
