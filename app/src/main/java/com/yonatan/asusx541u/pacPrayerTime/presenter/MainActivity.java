package com.yonatan.asusx541u.pacPrayerTime.presenter;

import android.animation.ValueAnimator;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yonatan.asusx541u.pacPrayerTime.R;
import com.yonatan.asusx541u.pacPrayerTime.Utils.Consts;
import com.yonatan.asusx541u.pacPrayerTime.Utils.UiUtils;
import com.yonatan.asusx541u.pacPrayerTime.Utils.prefs.IPrefs;
import com.yonatan.asusx541u.pacPrayerTime.Utils.prefs.Prefs;
import com.yonatan.asusx541u.pacPrayerTime.adapters.CustomAdapterSyn;
import com.yonatan.asusx541u.pacPrayerTime.adapters.NewsAdapter;
import com.yonatan.asusx541u.pacPrayerTime.adapters.PrayersViewPagerAdapter;
import com.yonatan.asusx541u.pacPrayerTime.broadcastReceiver.PrayerAlertReceiver;
import com.yonatan.asusx541u.pacPrayerTime.databinding.ActivityMainBinding;
import com.yonatan.asusx541u.pacPrayerTime.enums.TypeNewsViewHolder;
import com.yonatan.asusx541u.pacPrayerTime.enums.TypePrayer;
import com.yonatan.asusx541u.pacPrayerTime.managers.AnalyticsManager;
import com.yonatan.asusx541u.pacPrayerTime.model.News;
import com.yonatan.asusx541u.pacPrayerTime.model.Prayer;
import com.yonatan.asusx541u.pacPrayerTime.popUps.NotificationDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import static com.yonatan.asusx541u.pacPrayerTime.Utils.Consts.EXTRA_PUSH_NOTIFICATION_PRAYER;
import static com.yonatan.asusx541u.pacPrayerTime.Utils.Consts.PRAYER_REMINDER_REQUEST_CODE;

public class MainActivity extends AppCompatActivity implements PrayersViewPagerAdapter.ClickPrayerCallBack, NewsAdapter.OnClickNewsCallBack{

    private static final int INTREVAL_TIME = 1000 * 60 * 5;
    private DatabaseReference mDataBase;
    private TextView textViewNextPrayer, tvTimePrayer;
    private ListView lvSynagogue;
    private String nextTimeT, tempNextTimePrayer;
    private Calendar currentTime = Calendar.getInstance();
    private int hour, minutes, currentPrayer;
    private boolean flag;
    ArrayList<Prayer> mListPrayer = new ArrayList<>();
    ArrayList<String> mListSynagogue = new ArrayList<>();
    ArrayList<Prayer> allPrayers = new ArrayList<>();
    //This final variable to send info to another activity
    final static String KIND_PRAYER = "com.yonatan.asusx541u.pacPrayerTime.kindPrayer";
    private DrawerLayout drawerLayout;
    LottieAnimationView animationView_prayer, animationView_clock, animationView_location;
    Menu menu;
    private ActivityMainBinding binding;
    private IPrefs iPrefs = new Prefs();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setMainActivity(this);
        binding.setLifecycleOwner(this);
        navigation();
        toolbar();
        //nextPrayer("sahrit");
        checkAvailableNetwork();
        allPrayers();
        initAnimation();
        initRecyclerNews();
        setAppBarLayout();
        showPopupNotification();
    }

    private void showPopupNotification() {
        if (!iPrefs.getDontShoeNotificationDialog()){
            NotificationDialog.Companion.newInstance().show(getSupportFragmentManager(), "");
        }
    }

    /*private void setAdsListener() {
        NetworkManager.INSTANCE.setAdsListener(this);
    }
*/
    private void setAppBarLayout() {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) binding.appBarMainActivity.getLayoutParams();
        params.setBehavior(new AppBarLayout.Behavior());
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        assert behavior != null;
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                return false;
            }
        });
    }

    private void initRecyclerNews() {
        ArrayList<News> newsArrayList = new ArrayList<>();
        NewsAdapter newsAdapter =  new NewsAdapter(newsArrayList, this, this);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL);
        //layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        binding.rvNews.setLayoutManager(layoutManager);
        binding.rvNews.setAdapter(newsAdapter);
        FirebaseDatabase.getInstance().getReference().child("newsAndAds").
        addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    newsArrayList.clear();
                    for (DataSnapshot itemDataSnapshot: dataSnapshot.getChildren()){
                        News news = itemDataSnapshot.getValue(News.class);
                        if (news.getContent() != null && !news.getContent().isEmpty()){
                            news.setTypeNewsViewHolder(TypeNewsViewHolder.DETAILS_NEWS);
                        }else {
                            news.setTypeNewsViewHolder(TypeNewsViewHolder.IMAGE_NEWS);
                        }
                        newsArrayList.add(news);
                    }
                    newsAdapter.notifyDataSetChanged();
                }catch (Exception e){
                    if (e.getMessage() !=null)
                    Log.d("Request News:", e.getMessage());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initAnimation() {
        animationView_prayer = findViewById(R.id.lottie_prayer);
        final ValueAnimator animator = ValueAnimator.ofFloat(0f,1f).setDuration(3500);
        //animator.setRepeatCount(1);
        final LottieAnimationView finalAnimationView = animationView_prayer;
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                finalAnimationView.setProgress((Float) animator.getAnimatedValue());
            }
        });
        animator.start();
    }


    private void navigation() {
        drawerLayout = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);

       drawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {}

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
                            menu = navigationView.getMenu();
                            collapsePrayers(false);
                            collapseLessons(false);

                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {}
                }
        );

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                menu = navigationView.getMenu();
                switch (item.getItemId()){
                   /* case R.id.nav_news:
                        drawerLayout.closeDrawers();
                        startActivity(new Intent(MainActivity.this, NewsActivity.class));*/
                    case R.id.nav_nextPrayer:
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_prayers:
                        //Collapsible menu item inside navigation drawer
                        boolean b = !(menu.findItem(R.id.sahrit).isVisible());
                        collapsePrayers(b);
                        collapseLessons(false);
                        return true;
                    case R.id.sahrit:
                        drawerLayout.closeDrawers();
                        Intent i = new Intent(MainActivity.this, PrayersActivity.class);
                        i.putExtra(KIND_PRAYER,"sahrit");
                        startActivity(i);
                        return true;
                    case R.id.mincha:
                        drawerLayout.closeDrawers();
                        Intent intent = new Intent(MainActivity.this, PrayersActivity.class);
                        intent.putExtra(KIND_PRAYER,"mincha");
                        startActivity(intent);
                        return true;
                    case R.id.arvit:
                        drawerLayout.closeDrawers();
                        Intent intentArvit = new Intent(MainActivity.this, PrayersActivity.class);
                        intentArvit.putExtra(KIND_PRAYER,"arvit");
                        startActivity(intentArvit);
                        return true;
                    case R.id.about:
                        drawerLayout.closeDrawers();
                        Intent intentAbout = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(intentAbout);
                        return  true;
                    case R.id.nav_businees:
                        drawerLayout.closeDrawers();
                        startActivity(new Intent(MainActivity.this, BusinessActivity.class));
                        return true;
                    case R.id.lessons:
                        //Collapsible menu item inside navigation drawer
                        boolean visible = !menu.findItem(R.id.lessons_1).isVisible();
                        collapseLessons(visible);
                        collapsePrayers(false);
                        return true;
                    case R.id.lessons_1:
                        drawerLayout.closeDrawers();
                        Intent intent1 = new Intent(MainActivity.this,LessonsActivity.class);
                        intent1.putExtra("KEY_DAY","sunday");
                        startActivity(intent1);
                        return true;
                    case R.id.lessons_2:
                        drawerLayout.closeDrawers();
                        Intent intent2 = new Intent(MainActivity.this,LessonsActivity.class);
                        intent2.putExtra("KEY_DAY","monday");
                        startActivity(intent2);
                        return true;
                    case R.id.lessons_3:
                        drawerLayout.closeDrawers();
                        Intent intent3 = new Intent(MainActivity.this,LessonsActivity.class);
                        intent3.putExtra("KEY_DAY","tuesday");
                        startActivity(intent3);
                        return true;
                    case R.id.lessons_4:
                        drawerLayout.closeDrawers();
                        Intent intent4 = new Intent(MainActivity.this,LessonsActivity.class);
                        intent4.putExtra("KEY_DAY","wednesday");
                        startActivity(intent4);
                        return true;
                    case R.id.lessons_5:
                        drawerLayout.closeDrawers();
                        Intent intent5 = new Intent(MainActivity.this,LessonsActivity.class);
                        intent5.putExtra("KEY_DAY","thursday");
                        startActivity(intent5);
                        return true;
                    case R.id.lessons_6:
                        drawerLayout.closeDrawers();
                        Intent intent6 = new Intent(MainActivity.this,LessonsActivity.class);
                        intent6.putExtra("KEY_DAY","friday");
                        startActivity(intent6);
                        return true;
                }
                return true;
            }
        });

    }

    private void collapseLessons(boolean bool) {
        menu.findItem(R.id.lessons_1).setVisible(bool);
        menu.findItem(R.id.lessons_2).setVisible(bool);
        menu.findItem(R.id.lessons_3).setVisible(bool);
        menu.findItem(R.id.lessons_4).setVisible(bool);
        menu.findItem(R.id.lessons_5).setVisible(bool);
        menu.findItem(R.id.lessons_6).setVisible(bool);
    }

    private void collapsePrayers(boolean b) {
            menu.findItem(R.id.sahrit).setVisible(b);
            menu.findItem(R.id.mincha).setVisible(b);
            menu.findItem(R.id.arvit).setVisible(b);
    }

    private void checkAvailableNetwork() {
        if(!isNetworkAvailable()) {
                startActivity(new Intent(MainActivity.this, PopActivity.class));
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        //setAdsListener();
        //nextPrayer("sahrit");
        setCurrentPrayer();
        AnalyticsManager.INSTANCE.logScreenOpen(this.getLocalClassName());
    }

    private void setCurrentPrayer() {
        binding.prayersVP.setCurrentItem(getCurrentPrayer());
    }

    public void nextPrayer(final String prayer){
        mDataBase = FirebaseDatabase.getInstance().getReference().child(prayer);
        lvSynagogue =  findViewById(R.id.listViewSynagogue);
        tvTimePrayer =  findViewById(R.id.textViewTimePrayer);
        textViewNextPrayer = findViewById(R.id.textViewNextPrayer);
        currentTime = Calendar.getInstance();
        flag = false;
        //A special case, if it's Saturday night I'm not interested in Arvit prayers of the weekday
        // but will present me the earliest prayer on Sunday.
        if(currentTime.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
            getEarlyPrayer();
            return;
        }
        final CustomAdapterSyn synagogueArrayAdapter = new CustomAdapterSyn(mListSynagogue,getApplicationContext());
        lvSynagogue.setAdapter(synagogueArrayAdapter);
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mListPrayer.clear();
                /*dataSnapshot handle the reference to whole DB of a certain prayer and func getValue give all child to HashMap */
                Map<String, Object> mapPrayer = (Map<String, Object>) dataSnapshot.getValue();
                for (Map.Entry<String, Object> entry : mapPrayer.entrySet()) {
                    Map singlePrayer = (Map) entry.getValue();
                    Prayer mPrayer = new Prayer((String) singlePrayer.get("place"), (String) singlePrayer.get("time"));
                    mListPrayer.add(mPrayer);
                }
                if (currentTime.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY || currentTime.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY)
                    for (Prayer prayer : mListPrayer) {
                        if (prayer.getPlace().equals("שערי ציון") && prayer.getTime().equals("6:00"))
                            prayer.setTime("5:50");
                        else if (prayer.getPlace().equals("מרכזי") && prayer.getTime().equals("6:00"))
                            prayer.setTime("5:50");

                    }
                Collections.sort(mListPrayer);
                currentPrayer = 0;
                mListSynagogue.clear();

                for (Prayer singlePrayer : mListPrayer) {
                    nextTimeT = singlePrayer.getTime();
                    hour = Integer.parseInt(nextTimeT.substring(0, nextTimeT.indexOf(":")));
                    minutes = Integer.parseInt(nextTimeT.substring(nextTimeT.indexOf(":") + 1));
                    if (hour == currentTime.get(Calendar.HOUR_OF_DAY)) {
                        if (minutes >= currentTime.get(Calendar.MINUTE)) {
                            flag = true;
                            mListSynagogue.add(singlePrayer.getPlace());
                            /*Add all places of prayer at the same time */
                            while (mListPrayer.size() != currentPrayer + 1) {
                                tempNextTimePrayer = mListPrayer.get(currentPrayer + 1).getTime();
                                if (tempNextTimePrayer.equals(nextTimeT)) {
                                    mListSynagogue.add(mListPrayer.get(currentPrayer + 1).getPlace());
                                }
                                currentPrayer++;
                            }
                            synagogueArrayAdapter.notifyDataSetChanged();
                            tvTimePrayer.setText(singlePrayer.getTime());
                            return;
                        }
                    } else if (hour > currentTime.get(Calendar.HOUR_OF_DAY)) {
                        mListSynagogue.add(singlePrayer.getPlace());
                        while (mListPrayer.size() != currentPrayer + 1) {
                            tempNextTimePrayer = mListPrayer.get(currentPrayer + 1).getTime();
                            if (tempNextTimePrayer.equals(nextTimeT)) {
                                mListSynagogue.add(mListPrayer.get(currentPrayer + 1).getPlace());
                            }
                            currentPrayer++;
                        }
                        tvTimePrayer.setText(singlePrayer.getTime());
                        synagogueArrayAdapter.notifyDataSetChanged();
                        flag = true;
                        //textViewNextPrayer.setText(namePrayerToHeb(dataSnapshot.getKey()));
                        return;
                    }
                    currentPrayer++;
                }

                if (dataSnapshot.getKey() == "sahrit" && !flag) {
                    mListPrayer.clear();
                    nextPrayer("mincha");
                } else if (dataSnapshot.getKey() == "mincha" && !flag) {
                    mListPrayer.clear();
                    nextPrayer("arvit");
                }
                /*This case happens after the last prayer and it is not yet 00:00.*/
                else if (!flag) getEarlyPrayer();
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
        final CustomAdapterSyn synagogueArrayAdapter = new CustomAdapterSyn(mListSynagogue,getApplicationContext());
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
    public void allPrayers(){
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> prayersMap = (Map<String, Object>) dataSnapshot.getValue();
                for (Map.Entry<String, Object> entry: prayersMap.entrySet()){
                    if(entry.getKey().equals("sahrit") || entry.getKey().equals("mincha") || entry.getKey().equals("arvit")){
                        Map<String, Object> sinPrayer = (Map) entry.getValue();
                        for(Map.Entry<String, Object> entryPrayer: sinPrayer.entrySet()){
                            Map singMinyan = (Map) entryPrayer.getValue();
                            Prayer prayer = new Prayer(
                                    (String) singMinyan.get("place"),
                                    (String) singMinyan.get("time")
                            );
                            prayer.setKind(entry.getKey());
                            allPrayers.add(prayer);
                        }
                    }
                }
                if (currentTime.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY || currentTime.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY)
                    for (Prayer prayer : allPrayers) {
                        if (prayer.getPlace().equals("שערי ציון") && prayer.getTime().equals("6:00"))
                            prayer.setTime("5:50");
                        else if (prayer.getPlace().equals("מרכזי") && prayer.getTime().equals("6:00"))
                            prayer.setTime("5:50");

                    }
                Collections.sort(allPrayers);
                initPrayersVP();
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

    public void toolbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("שורק נט");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        UiUtils.INSTANCE.centerTitle(this);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.times_prayers, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        textViewNextPrayer = findViewById(R.id.textViewNextPrayer);
        animationView_clock = findViewById(R.id.lottie_clock);
        animationView_location = findViewById(R.id.lottie_location);
        animationView_location.playAnimation();
        animationView_clock.playAnimation();
        animationView_prayer.playAnimation();
        String flag;
        switch (item.getItemId()) {
            case android.R.id.home:
                if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawers();
                }
                else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
            case R.id.next_prayer:
                if(textViewNextPrayer.getText().toString().matches("")){
                    startActivity(new Intent(MainActivity.this, PopActivity.class));
                }
                else {
                    flag = "next";
                    String strTimeAppears = tvTimePrayer.getText().toString();
                    int intTimeApeers = Integer.parseInt((strTimeAppears.substring(0, strTimeAppears.indexOf(":"))) + (strTimeAppears.substring(strTimeAppears.indexOf(":") + 1)));
                    for (int i = 0; i < allPrayers.size(); i++) {
                        String strTimeAtList = allPrayers.get(i).getTime();
                        int intTimeAtList = Integer.parseInt(((strTimeAtList.substring(0, strTimeAtList.indexOf(":"))) + (strTimeAtList.substring(strTimeAtList.indexOf(":") + 1))));
                        if (intTimeAtList > intTimeApeers) {
                            //updateFieldWhenUserClickNextAndPrev(i, flag);
                            return true;
                        }
                    }
                }
                return true;

            case R.id.prev_prayer:
                if(textViewNextPrayer.getText().toString().matches("")){
                    startActivity(new Intent(MainActivity.this, PopActivity.class));
                }
                else {
                    flag = "prev";
                    String strTimeAppearsPrev = tvTimePrayer.getText().toString();
                    int intTimeApeersPrev = Integer.parseInt((strTimeAppearsPrev.substring(0, strTimeAppearsPrev.indexOf(":"))) + (strTimeAppearsPrev.substring(strTimeAppearsPrev.indexOf(":") + 1)));
                    for (int i = allPrayers.size() - 1; i >= 0; i--) {
                        String strTimeAtList = allPrayers.get(i).getTime();
                        int intTimeAtList = Integer.parseInt(((strTimeAtList.substring(0, strTimeAtList.indexOf(":"))) + (strTimeAtList.substring(strTimeAtList.indexOf(":") + 1))));
                        if (intTimeApeersPrev > intTimeAtList) {
                            //updateFieldWhenUserClickNextAndPrev(i, flag);
                            return true;
                        }
                    }
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

   /* public void updateFieldWhenUserClickNextAndPrev(int i, String flag){
        textViewNextPrayer.setText(namePrayerToHeb(allPrayers.get(i).getKind()));
        tvTimePrayer.setText(allPrayers.get(i).getTime());
        mListSynagogue.clear();
        final CustomAdapterSyn synagogueArrayAdapter = new CustomAdapterSyn(mListSynagogue,getApplicationContext());
        lvSynagogue.setAdapter(synagogueArrayAdapter);
        currentPrayer = i;
        mListSynagogue.add(allPrayers.get(i).getPlace());
        if(currentPrayer+1 < allPrayers.size() && flag.equals("next")) {
            tempNextTimePrayer = allPrayers.get(currentPrayer + 1).getTime();
            while (tempNextTimePrayer.equals(allPrayers.get(i).getTime())) {
                mListSynagogue.add(allPrayers.get(currentPrayer + 1).getPlace());
                currentPrayer++;
                tempNextTimePrayer = allPrayers.get(currentPrayer + 1).getTime();
            }
        }
        else if(currentPrayer-1 >= 0 && flag.equals("prev")){
            tempNextTimePrayer = allPrayers.get(currentPrayer-1).getTime();
            while (tempNextTimePrayer.equals(allPrayers.get(i).getTime())){
                mListSynagogue.add(allPrayers.get(currentPrayer-1).getPlace());
                currentPrayer--;
                if(currentPrayer-1 >= 0)
                    tempNextTimePrayer = allPrayers.get(currentPrayer -1).getTime();
                else return;;
            }
        }
        synagogueArrayAdapter.notifyDataSetChanged();
    }*/

    private void initPrayersVP(){
        calculateSameTimeOfPrayers();
        binding.prayersVP.setAdapter(new PrayersViewPagerAdapter(allPrayers, this));
        binding.prayersVP.setPageMargin(16);
        binding.prayersVP.setCurrentItem(getCurrentPrayer());
    }

    private int getCurrentPrayer() {
        currentPrayer = 0;
        for (Prayer singlePrayer : allPrayers) {
            nextTimeT = singlePrayer.getTime();
            hour = Integer.parseInt(nextTimeT.substring(0, nextTimeT.indexOf(":")));
            minutes = Integer.parseInt(nextTimeT.substring(nextTimeT.indexOf(":") + 1));
            mListSynagogue.clear();
            if (hour == currentTime.get(Calendar.HOUR_OF_DAY)) {
                if (minutes >= currentTime.get(Calendar.MINUTE)) {
                    return currentPrayer++;
                }
            } else if (hour > currentTime.get(Calendar.HOUR_OF_DAY)) {
                return currentPrayer++;
            }
            currentPrayer++;
        }
        return 0;
    }

    private void calculateSameTimeOfPrayers(){
        List<Prayer> prayerListToRemove = new ArrayList<>();
          String time;
          int mainPrayer = 0;
        for (int i=0; i<allPrayers.size(); i++){
            time = allPrayers.get(i).getTime();
            if (i+1<allPrayers.size() && TextUtils.equals(time, allPrayers.get(i+1).getTime())){
                allPrayers.get(mainPrayer).getSynagogueList().add(allPrayers.get(i+1).getPlace());
                prayerListToRemove.add(allPrayers.get(i+1));
            }else {
                allPrayers.get(mainPrayer).getSynagogueList().add(allPrayers.get(mainPrayer).getPlace());
                mainPrayer = i+1;
            }
        }
        allPrayers.removeAll(prayerListToRemove);
    }

    @Override
    public void onClickPrayer(@NotNull TypePrayer typePrayer) {
        Intent i = new Intent(MainActivity.this, PrayersActivity.class);
        i.putExtra(KIND_PRAYER, typePrayer.getType());
        startActivity(i);
    }

    @Override
    public void onClickNewsListener(News news) {
        switch (news.getTypeNewsViewHolder()){
            case IMAGE_NEWS:
                goToImageScreen(news.getImg());
                return;
            case DETAILS_NEWS:
                goToNewsScreen(news);
        }
    }

    private void goToNewsScreen(News news) {
        Intent intent = new Intent(MainActivity.this, NewsActivity.class);
        intent.putExtra(Consts.News, news);
        startActivity(intent);
    }

    private void goToImageScreen(String img) {
        Intent intent = new Intent(MainActivity.this, ImagePopUp.class);
        intent.putExtra(Consts.LINK_IMAGE, img);
        startActivity(intent);
    }

    @Override
    public void onClickAlert(@NotNull Prayer prayer) {
        Calendar calendar = getTimeToNotify(prayer);
        Toast.makeText(this, "ההתראה הופעלה", Toast.LENGTH_LONG).show();
        Intent notifyIntent = new Intent(this, PrayerAlertReceiver.class);
        notifyIntent.putExtra(EXTRA_PUSH_NOTIFICATION_PRAYER, prayer.getTypePrayer().getHebPrayer());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                PRAYER_REMINDER_REQUEST_CODE,
                notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() - INTREVAL_TIME, pendingIntent);
            }
            else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() - INTREVAL_TIME, pendingIntent);
            }
        }
    }

    private Calendar getTimeToNotify(Prayer prayer) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, prayer.getHours());
        calendar.set(Calendar.MINUTE, prayer.getMinutes());
        if (calendar.getTimeInMillis() < System.currentTimeMillis() && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY){
            calendar.add(Calendar.DATE, 1);
        }
        return calendar;
    }
}