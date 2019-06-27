package com.yonatan.asusx541u.pacPrayerTime.presenter;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yonatan.asusx541u.pacPrayerTime.R;
import com.yonatan.asusx541u.pacPrayerTime.adapters.NewsAdapter;
import com.yonatan.asusx541u.pacPrayerTime.model.News;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static com.yonatan.asusx541u.pacPrayerTime.presenter.MainActivity.KIND_PRAYER;

public class NewsActivity extends AppCompatActivity {

    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    ArrayList<News> newsArrayList;
    ArrayList<String> linkImageArrayList;
    DatabaseReference database;
    DrawerLayout drawerLayout;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        navigation();
        toolbar();


        newsArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale);
        LayoutAnimationController  animationController = new LayoutAnimationController(animation,.2f);
        recyclerView.setLayoutAnimation(animationController);
        final NewsAdapter newsAdapter = new NewsAdapter(newsArrayList,getApplicationContext());
        recyclerView.setAdapter(newsAdapter);
        database = FirebaseDatabase.getInstance().getReference().child("news");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                newsArrayList.clear();
                for (Map.Entry<String, Object> entry: map.entrySet()){
                    if(!entry.getKey().equals("name")){
                    Map sinNews = (Map) entry.getValue();
                    Map<String, String> mapLinkImage = (Map<String, String>) sinNews.get("link_images");
                    linkImageArrayList = new ArrayList<>();
                    for(Map.Entry<String,String> imageEntry : mapLinkImage.entrySet()){
                        linkImageArrayList.add(imageEntry.getValue());
                    }
                    //Date currDate = new Date();
                    News news = new News(
                            linkImageArrayList,
                            (String) sinNews.get("title"),
                            (String) sinNews.get("content"),
                            (String) sinNews.get("name_writer"),
                            (String) sinNews.get("date_create"),
                            (String) sinNews.get("time_create")
                    );
                    newsArrayList.add(news);
                }
                }
                newsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                    /*case R.id.nav_news:
                        drawerLayout.closeDrawers();
                        return true;*/
                    case R.id.nav_nextPrayer:
                        drawerLayout.closeDrawers();
                        startActivity(new Intent(NewsActivity.this, MainActivity.class));
                        return true;
                    case R.id.nav_prayers:
                        //Collapsible menu item inside navigation drawer
                        boolean b = !(menu.findItem(R.id.sahrit).isVisible());
                        collapsePrayers(b);
                        collapseLessons(false);
                        return true;
                    case R.id.sahrit:
                        drawerLayout.closeDrawers();
                        Intent i = new Intent(NewsActivity.this, PrayersActivity.class);
                        i.putExtra(KIND_PRAYER,"sahrit");
                        startActivity(i);
                        return true;
                    case R.id.mincha:
                        drawerLayout.closeDrawers();
                        Intent intent = new Intent(NewsActivity.this, PrayersActivity.class);
                        intent.putExtra(KIND_PRAYER,"mincha");
                        startActivity(intent);
                        return true;
                    case R.id.arvit:
                        drawerLayout.closeDrawers();
                        Intent intentArvit = new Intent(NewsActivity.this, PrayersActivity.class);
                        intentArvit.putExtra(KIND_PRAYER,"arvit");
                        startActivity(intentArvit);
                        return true;
                    case R.id.about:
                        drawerLayout.closeDrawers();
                        Intent intentAbout = new Intent(NewsActivity.this, AboutActivity.class);
                        startActivity(intentAbout);
                        return  true;
                    case R.id.nav_businees:
                        drawerLayout.closeDrawers();
                        startActivity(new Intent(NewsActivity.this, BusinessActivity.class));
                        return true;
                    case R.id.lessons:
                        //Collapsible menu item inside navigation drawer
                        boolean visible = !menu.findItem(R.id.lessons_1).isVisible();
                        collapseLessons(visible);
                        collapsePrayers(false);
                        return true;
                    case R.id.lessons_1:
                        drawerLayout.closeDrawers();
                        Intent intent1 = new Intent(NewsActivity.this,LessonsActivity.class);
                        intent1.putExtra("KEY_DAY","sunday");
                        startActivity(intent1);
                        return true;
                    case R.id.lessons_2:
                        drawerLayout.closeDrawers();
                        Intent intent2 = new Intent(NewsActivity.this,LessonsActivity.class);
                        intent2.putExtra("KEY_DAY","monday");
                        startActivity(intent2);
                        return true;
                    case R.id.lessons_3:
                        drawerLayout.closeDrawers();
                        Intent intent3 = new Intent(NewsActivity.this,LessonsActivity.class);
                        intent3.putExtra("KEY_DAY","tuesday");
                        startActivity(intent3);
                        return true;
                    case R.id.lessons_4:
                        drawerLayout.closeDrawers();
                        Intent intent4 = new Intent(NewsActivity.this,LessonsActivity.class);
                        intent4.putExtra("KEY_DAY","wednesday");
                        startActivity(intent4);
                        return true;
                    case R.id.lessons_5:
                        drawerLayout.closeDrawers();
                        Intent intent5 = new Intent(NewsActivity.this,LessonsActivity.class);
                        intent5.putExtra("KEY_DAY","thursday");
                        startActivity(intent5);
                        return true;
                    case R.id.lessons_6:
                        drawerLayout.closeDrawers();
                        Intent intent6 = new Intent(NewsActivity.this,LessonsActivity.class);
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
        //menu.findItem(R.id.lessons_7).setVisible(bool);
    }

    private void collapsePrayers(boolean b) {
        menu.findItem(R.id.sahrit).setVisible(b);
        menu.findItem(R.id.mincha).setVisible(b);
        menu.findItem(R.id.arvit).setVisible(b);
    }
    public void toolbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("חדשות המועצה");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        }
        else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.news_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
