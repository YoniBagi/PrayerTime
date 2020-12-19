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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.yonatan.asusx541u.pacPrayerTime.R;
import com.yonatan.asusx541u.pacPrayerTime.Utils.Consts;
import com.yonatan.asusx541u.pacPrayerTime.Utils.UiUtils;
import com.yonatan.asusx541u.pacPrayerTime.Utils.prefs.IPrefs;
import com.yonatan.asusx541u.pacPrayerTime.Utils.prefs.Prefs;
import com.yonatan.asusx541u.pacPrayerTime.adapters.NewsAdapter;
import com.yonatan.asusx541u.pacPrayerTime.adapters.PrayersViewPagerAdapter;
import com.yonatan.asusx541u.pacPrayerTime.broadcastReceiver.PrayerAlertReceiver;
import com.yonatan.asusx541u.pacPrayerTime.databinding.ActivityMainBinding;
import com.yonatan.asusx541u.pacPrayerTime.enums.TypePrayer;
import com.yonatan.asusx541u.pacPrayerTime.managers.AnalyticsManager;
import com.yonatan.asusx541u.pacPrayerTime.managers.NetworkManager;
import com.yonatan.asusx541u.pacPrayerTime.model.News;
import com.yonatan.asusx541u.pacPrayerTime.model.Prayer;
import com.yonatan.asusx541u.pacPrayerTime.popUps.NotificationDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.yonatan.asusx541u.pacPrayerTime.Utils.Consts.EXTRA_PUSH_NOTIFICATION_PRAYER;
import static com.yonatan.asusx541u.pacPrayerTime.Utils.Consts.PRAYER_REMINDER_REQUEST_CODE;

public class MainActivity extends AppCompatActivity implements PrayersViewPagerAdapter.ClickPrayerCallBack, NewsAdapter.OnClickNewsCallBack, NetworkManager.DataListener {

    private static final int INTREVAL_TIME = 1000 * 60 * 5;
    private TextView textViewNextPrayer;
    private String nextTimeT;
    private Calendar currentTime = Calendar.getInstance();
    private int hour, minutes, currentPrayer;
    ArrayList<String> mListSynagogue = new ArrayList<>();
    ArrayList<Prayer> allPrayers = new ArrayList<>();
    final static String KIND_PRAYER = "com.yonatan.asusx541u.pacPrayerTime.kindPrayer";
    private DrawerLayout drawerLayout;
    LottieAnimationView animationView_prayer, animationView_clock, animationView_location;
    Menu menu;
    private ActivityMainBinding binding;
    private NewsAdapter newsAdapter;
    private IPrefs iPrefs = new Prefs();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setMainActivity(this);
        binding.setLifecycleOwner(this);
        navigation();
        toolbar();
        checkAvailableNetwork();
        initPrayersVP();
        initAnimation();
        initRecyclerNews();
        setAppBarLayout();
        showPopupNotification();
    }

    private void setListeners() {
        NetworkManager.INSTANCE.setDataListener(this);
    }



    private void showPopupNotification() {
        if (!iPrefs.getDontShoeNotificationDialog()){
            NotificationDialog.Companion.newInstance().show(getSupportFragmentManager(), "");
        }
    }

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
        ArrayList<News> newsArrayList = NetworkManager.INSTANCE.getNewsList();
        newsAdapter =  new NewsAdapter(newsArrayList, this, this);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL);
        //layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        binding.rvNews.setLayoutManager(layoutManager);
        binding.rvNews.setAdapter(newsAdapter);
        updateRecyclerNews();
    }

    private void updateRecyclerNews() {
        newsAdapter.notifyDataSetChanged();
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
        setCurrentPrayer();
        AnalyticsManager.INSTANCE.logScreenOpen(this.getLocalClassName());
        setListeners();
    }

    private void setCurrentPrayer() {
        binding.prayersVP.setCurrentItem(getCurrentPrayer());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        textViewNextPrayer = findViewById(R.id.textViewNextPrayer);
        animationView_clock = findViewById(R.id.lottie_clock);
        animationView_location = findViewById(R.id.lottie_location);
        animationView_location.playAnimation();
        animationView_clock.playAnimation();
        animationView_prayer.playAnimation();
        switch (item.getItemId()) {
            case android.R.id.home:
                if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawers();
                }
                else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initPrayersVP(){
        allPrayers = NetworkManager.INSTANCE.getAllPrayers();
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
            allPrayers.get(i).setSynagogueList(new ArrayList<>());
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

    @Override
    public void onClickDeletePost(String idPost) {
        Log.d("Main", idPost);
        NetworkManager.INSTANCE.removePost(idPost);
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

    @Override
    public void firstDataFetched() {
        initRecyclerNews();
    }

    @Override
    protected void onStop() {
        super.onStop();
        NetworkManager.INSTANCE.removeListener(this);
    }
}