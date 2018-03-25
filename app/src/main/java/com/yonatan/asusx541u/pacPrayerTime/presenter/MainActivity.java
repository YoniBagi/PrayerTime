package com.yonatan.asusx541u.pacPrayerTime.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kobakei.ratethisapp.RateThisApp;
import com.yonatan.asusx541u.pacPrayerTime.model.Prayer;
import com.yonatan.asusx541u.pacPrayerTime.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDataBase;
    private TextView textViewNextPrayer, tvTimePrayer;
    private ListView lvSynagogue;
    private String nextTimeT, tempNextTimePrayer;
    private Calendar currentTime = Calendar.getInstance();
    private int hour, minutes, currentPrayer,counterLaunch, afterTimes;
    private boolean flag;
    ArrayList<Prayer> mListPrayer = new ArrayList<Prayer>();
    ArrayList<String> mListSynagogue = new ArrayList<String>();
    //The next two variables are to keep the number of times the user enter to the app.
    private SharedPreferences mPref, mPrefAfter ;
    final static String KIND_PRAYER = "com.yonatan.asusx541u.pacPrayerTime.kindPrayer";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar();
        checkAvailableNetwork();
        nextPrayer("sahrit");
        initialRateTheApp();
        mPref = getPreferences(Context.MODE_PRIVATE);
        mPrefAfter = getPreferences(Context.MODE_PRIVATE);
    }

    private void initialRateTheApp() {
        RateThisApp.onCreate(this);
        RateThisApp.Config config = new RateThisApp.Config();
        config.setTitle(R.string.RateTitle);
        config.setMessage(R.string.RateMessage);
        config.setYesButtonText(R.string.RateYes);
        config.setNoButtonText(R.string.RateNo);
        config.setCancelButtonText(R.string.RateLater);
        RateThisApp.init(config);
    }

    private void checkAvailableNetwork() {
        if(!isNetworkAvailable())
            Toast.makeText(MainActivity.this,"אין חיבור לאינטרנט, התחבר בכדי לקבל מידע עדכני",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume(){
        super.onResume();
        nextPrayer("sahrit");
        showRateTheApp();
    }

    /*
        General: This function calculates the number of times the user enter to the app
         and then displays the message to rate.
        Paramters: NONE
        Return Value: NONE
    */
    private void showRateTheApp() {
        counterLaunch = mPref.getInt("numRun",0);
        afterTimes = mPrefAfter.getInt("numAfter",1);
        if (counterLaunch > afterTimes){
            RateThisApp.showRateDialog(this);
            afterTimes *= 2;
            mPrefAfter.edit().putInt("numAfter",afterTimes).commit();
        }
        counterLaunch++;
        mPref.edit().putInt("numRun",counterLaunch).commit();
    }

  /*  public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus) nextPrayer("sahrit");
    }*/

    public void nextPrayer(final String prayer){
        mDataBase = FirebaseDatabase.getInstance().getReference().child(prayer);
        lvSynagogue = (ListView) findViewById(R.id.listViewSynagogue);
        tvTimePrayer = (TextView) findViewById(R.id.textViewTimePrayer);
        textViewNextPrayer = (TextView) findViewById(R.id.textViewNextPrayer);
        //ImageView imageView = (ImageView) findViewById(R.id.imageView3) ;
        currentTime = Calendar.getInstance();
        flag = false;
        //A special case, if it's Saturday night I'm not interested in Arvit prayers of the weekday
        // but will present me the earliest prayer on Sunday.
        if(currentTime.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
            getEarlyPrayer();
            return;
        }
        final ArrayAdapter<String> synagogueArrayAdapter = new ArrayAdapter<String>(this, R.layout.row_item_synagogue,mListSynagogue){
            @NonNull
            @Override
            public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if(convertView == null){
                    convertView = View.inflate(MainActivity.this, R.layout.row_item_synagogue, null);
                }

                TextView prayerPlace = (TextView) convertView.findViewById(R.id.textView2);
                ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView3);
                prayerPlace.setText(mListSynagogue.get(position));
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uri;
                        switch (mListSynagogue.get(position)){
                            case "מרכזי" :
                                uri = "geo:0,0?q=31.801166,34.822807&navigate=yes";
                                break;
                            case "תורת החיים":
                                uri="geo:0,0?q=31.798238, 34.820400&navigate=yes";
                                break;
                            case "שערי ציון":
                                uri = "geo:0,0?q=31.800334, 34.821704&navigate=yes";
                                break;
                            case "קהילתי":
                                uri = "geo:0,0?q=31.797079, 34.821515&navigate=yes";
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
                                uri = "geo:0,0?q=31.797565, 34.825567&navigate=yes";
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

                        startActivity(new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse(uri)));
                    }
                });
                return convertView;
            }
        };

        lvSynagogue.setAdapter(synagogueArrayAdapter);
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mListPrayer.clear();
                /*dataSnapshot handle the reference to whole DB of a certain prayer and func getValue give all child to HashMap */
                Map<String, Object> mapPrayer = (Map<String, Object>) dataSnapshot.getValue();
               // Map<String,Object> treePrayer = new TreeMap<String, Object>(mapPrayer);
                for (Map.Entry<String,Object> entry : mapPrayer.entrySet()) {
                    Map singlePrayer = (Map) entry.getValue();
                    Prayer mPrayer = new Prayer((String) singlePrayer.get("place"), (String) singlePrayer.get("time"));
                    mListPrayer.add(mPrayer);
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
                currentPrayer = 0;
                mListSynagogue.clear();

                for (Prayer singlePrayer : mListPrayer){
                    nextTimeT = singlePrayer.getTime();
                    hour = Integer.parseInt(nextTimeT.substring(0, nextTimeT.indexOf(":")));
                    minutes = Integer.parseInt(nextTimeT.substring(nextTimeT.indexOf(":") +1));
                    if(hour ==  currentTime.get(Calendar.HOUR_OF_DAY)){
                        if (minutes >= currentTime.get(Calendar.MINUTE)){
                            flag = true;
                            mListSynagogue.add(singlePrayer.getPlace());
                            /*Add all places of prayer at the same time */
                            while(mListPrayer.size() != currentPrayer+1) {
                                tempNextTimePrayer = mListPrayer.get(currentPrayer + 1).getTime();
                                if (tempNextTimePrayer.equals(nextTimeT)) {
                                    mListSynagogue.add(mListPrayer.get(currentPrayer + 1).getPlace());
//                                    currentPrayer++;
                                }
                               /* else*/currentPrayer++;
                            }
                            synagogueArrayAdapter.notifyDataSetChanged();
                            tvTimePrayer.setText(singlePrayer.getTime());
                            if(dataSnapshot.getKey() == "sahrit"){
                                textViewNextPrayer.setText("שחרית");
                            }
                            else if(dataSnapshot.getKey() == "mincha"){
                                textViewNextPrayer.setText("מנחה");
                            }
                            else {textViewNextPrayer.setText("ערבית");}
                            return;
                        }
                    }
                    else if(hour > currentTime.get(Calendar.HOUR_OF_DAY)) {
                        mListSynagogue.add(singlePrayer.getPlace());
                        while(mListPrayer.size() != currentPrayer+1) {
                            tempNextTimePrayer = mListPrayer.get(currentPrayer + 1).getTime();
                            if (tempNextTimePrayer.equals(nextTimeT)) {
                                mListSynagogue.add(mListPrayer.get(currentPrayer + 1).getPlace());
                            }
                             currentPrayer++;
                        }
                        tvTimePrayer.setText(singlePrayer.getTime());
                        synagogueArrayAdapter.notifyDataSetChanged();
                        flag = true;
                        if(dataSnapshot.getKey() == "sahrit"){
                            textViewNextPrayer.setText("שחרית");
                        }
                        else if(dataSnapshot.getKey() == "mincha"){
                            textViewNextPrayer.setText("מנחה");
                        }
                        else {textViewNextPrayer.setText("ערבית");}
                        return;
                    }
                    currentPrayer ++;
                }

                if(dataSnapshot.getKey() == "sahrit" && !flag){
                    mListPrayer.clear();
                    nextPrayer("mincha");
                }
                else if(dataSnapshot.getKey() == "mincha" && !flag){
                    mListPrayer.clear();
                    nextPrayer("arvit");
                }
                /*This case happens after the last prayer and it is not yet 00:00.*/
                else if(!flag) getEarlyPrayer();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


    }
/*  General: This method used for to cases:
*       1. when the day is Saturday.
*       2. when the time is after the last prayer in all prayers, so we need to get the first prayer else we will
*           have to wait for the time will arrive to 00:00 (according to my algorithm)
        Paramters: NONE
        Return Value: NONE
         */

    public void getEarlyPrayer(){
        mDataBase = FirebaseDatabase.getInstance().getReference().child("sahrit");
        lvSynagogue = (ListView) findViewById(R.id.listViewSynagogue);
        tvTimePrayer = (TextView) findViewById(R.id.textViewTimePrayer);
        textViewNextPrayer = (TextView) findViewById(R.id.textViewNextPrayer);
        final ArrayAdapter<String> synagogueArrayAdapter = new ArrayAdapter<String>(this, R.layout.row_item_synagogue,mListSynagogue){
            @NonNull
            @Override
            public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if(convertView == null){
                    convertView = View.inflate(MainActivity.this, R.layout.row_item_synagogue, null);
                }

                TextView prayerPlace = (TextView) convertView.findViewById(R.id.textView2);
                ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView3);
                prayerPlace.setText(mListSynagogue.get(position));
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uri;
                        switch (mListSynagogue.get(position)){
                            case "מרכזי" :
                                uri = "geo:0,0?q=31.801166,34.822807&navigate=yes";
                                break;
                            case "תורת החיים":
                                uri="geo:0,0?q=31.798238, 34.820400&navigate=yes";
                                break;
                            case "שערי ציון":
                                uri = "geo:0,0?q=31.800334, 34.821704&navigate=yes";
                                break;
                            case "קהילתי":
                                uri = "geo:0,0?q=31.797079, 34.821515&navigate=yes";
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
                                uri = "geo:0,0?q=31.797565, 34.825567&navigate=yes";
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

                        startActivity(new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse(uri)));
                    }
                });
                return convertView;
            }
        };
        lvSynagogue.setAdapter(synagogueArrayAdapter);
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mListPrayer.clear();
                Map<String, Object> mapPrayer = (Map<String, Object>) dataSnapshot.getValue();
                for (Map.Entry<String,Object> entry : mapPrayer.entrySet()) {
                    Map singlePrayer = (Map) entry.getValue();
                    Prayer mPrayer = new Prayer((String) singlePrayer.get("place"), (String) singlePrayer.get("time"));
                    mListPrayer.add(mPrayer);
                }

                Collections.sort(mListPrayer);
                currentPrayer = 0;
                mListSynagogue.clear();
                for (Prayer singlePrayer : mListPrayer){
                    if(currentTime.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY || currentTime.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY){
                        if(singlePrayer.getPlace().equals("שערי ציון") && singlePrayer.getTime().equals("6:00"))
                            singlePrayer.setTime("5:50");
                        else if (singlePrayer.getPlace().equals("מרכזי") && singlePrayer.getTime().equals("6:00"))
                            singlePrayer.setTime("5:50");
                    }
                    nextTimeT = singlePrayer.getTime();
                    mListSynagogue.add(singlePrayer.getPlace());
                    tempNextTimePrayer = mListPrayer.get(currentPrayer + 1).getTime();
                    while (tempNextTimePrayer.equals(nextTimeT)) {
                        mListSynagogue.add(mListPrayer.get(currentPrayer + 1).getPlace());
                        currentPrayer++;
                        tempNextTimePrayer = mListPrayer.get(currentPrayer + 1).getTime();
                    }
                    synagogueArrayAdapter.notifyDataSetChanged();
                    tvTimePrayer.setText(singlePrayer.getTime());
                    textViewNextPrayer.setText("שחרית");
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }



    public void toolbar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("המניין הבא");
        // actionBar.setDisplayHomeAsUpEnabled(true);
    }

    //When the user push on menu button
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.times_prayers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
     /*   Intent intent =  new Intent(MainActivity.this,PrayersActivity.class);
        intent.putExtra(KIND_PRAYER,item.getItemId());
        startActivity(intent);
        return true;*/
        switch (item.getItemId()){
            case R.id.sahrit:
                Intent i = new Intent(MainActivity.this, PrayersActivity.class);
                i.putExtra(KIND_PRAYER,"sahrit");
                startActivity(i);
                return true;
            case R.id.mincha:
                Intent intent = new Intent(MainActivity.this, PrayersActivity.class);
                intent.putExtra(KIND_PRAYER,"mincha");
                startActivity(intent);
                return true;
            case R.id.arvit:
                Intent intentArvit = new Intent(MainActivity.this, PrayersActivity.class);
                intentArvit.putExtra(KIND_PRAYER,"arvit");
                startActivity(intentArvit);
                return true;
            case R.id.about:
                Intent intentAbout = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intentAbout);
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
