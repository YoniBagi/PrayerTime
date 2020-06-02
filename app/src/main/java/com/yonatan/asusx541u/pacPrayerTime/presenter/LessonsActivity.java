package com.yonatan.asusx541u.pacPrayerTime.presenter;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yonatan.asusx541u.pacPrayerTime.R;
import com.yonatan.asusx541u.pacPrayerTime.Utils.UiUtils;
import com.yonatan.asusx541u.pacPrayerTime.managers.AnalyticsManager;
import com.yonatan.asusx541u.pacPrayerTime.model.Lessons;

import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class LessonsActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    private String name_day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);

        initAnim();
        Intent intent = getIntent();
        String day_of_lessons_to_db = intent.getStringExtra("KEY_DAY");
        ListView listView = findViewById(R.id.lvLessons);
        final ArrayList<Lessons> lessonsArrayList = new ArrayList<>();
        final ArrayAdapter<Lessons> lessonsArrayAdapter = new ArrayAdapter<Lessons>(this, R.layout.row_item_lesson, lessonsArrayList){
            @NonNull
            @Override
            public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = View.inflate(LessonsActivity.this, R.layout.row_item_lesson, null);
                }
                TextView tvSubject = convertView.findViewById(R.id.tvSubject);
                TextView tvPlaceLesson = convertView.findViewById(R.id.tvPlaceLesson);
                TextView tvLecturer = convertView.findViewById(R.id.tvLecturer);
                TextView tvTimeLesson = convertView.findViewById(R.id.tvTimeLesson);
                tvSubject.setText(lessonsArrayList.get(position).getSubject());
                tvPlaceLesson.setText(lessonsArrayList.get(position).getPlace());
                tvLecturer.setText(lessonsArrayList.get(position).getLecturer());
                tvTimeLesson.setText(lessonsArrayList.get(position).getTime());
                return convertView;
            }
        };
        listView.setAdapter(lessonsArrayAdapter);
        if(!day_of_lessons_to_db.equals("friday")) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("lessons").child("allDays");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<Map> strings = (ArrayList<Map>) dataSnapshot.getValue();
                    for(int i=1; i<strings.size(); i++){
                        Map singLesson = strings.get(i);
                        Lessons lesson = new Lessons(
                                (String) singLesson.get("place"),
                                (String) singLesson.get("time"),
                                (String) singLesson.get("subject"),
                                (String) singLesson.get("lecturer")
                        );
                        lessonsArrayAdapter.add(lesson);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        databaseReference = FirebaseDatabase.getInstance().getReference().child("lessons").child(day_of_lessons_to_db);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,Object> mapLessons = (Map<String, Object>) dataSnapshot.getValue();
                for (Map.Entry<String,Object> entry : mapLessons.entrySet()){
                    if(!entry.getKey().equals("nameDay")){
                        Map singDay = (Map) entry.getValue();
                        Lessons lesson = new Lessons(
                                (String)singDay.get("place"),
                                (String)singDay.get("time"),
                                (String)singDay.get("subject"),
                                (String)singDay.get("lecturer")
                        );
                        lessonsArrayAdapter.add(lesson);
                    }
                    else {
                        name_day = (String) entry.getValue();
                    }
                }
                toolbar();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initAnim() {
        final LottieAnimationView newsPaperAnim = findViewById(R.id.newsPaperAnim);
        final ValueAnimator animator = ValueAnimator.ofFloat(0f,0.5f).setDuration(3500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                newsPaperAnim.setProgress((Float) animator.getAnimatedValue());
            }
        });
        animator.start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        AnalyticsManager.INSTANCE.logScreenOpen(this.getLocalClassName());
    }

    public void toolbar(){
        UiUtils.INSTANCE.centerTitle(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(name_day);
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
