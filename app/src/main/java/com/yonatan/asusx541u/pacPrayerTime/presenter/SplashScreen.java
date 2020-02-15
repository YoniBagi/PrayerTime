package com.yonatan.asusx541u.pacPrayerTime.presenter;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.yonatan.asusx541u.pacPrayerTime.R;
import com.yonatan.asusx541u.pacPrayerTime.Utils.UiUtils;

import java.util.ArrayList;

public class SplashScreen extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 4500;
    private DatabaseReference databaseReference;
    private boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        UiUtils.INSTANCE.centerTitle(this);

        //The Firebase Realtime Database client automatically downloads the data at these locations and keeps it in sync even if the reference has no active listeners.
        //And now he get the data faster, it's mean that while the animation works I pull the information.
        FirebaseDatabase.getInstance().getReference().keepSynced(true);
        final ImageView ivIcon = (ImageView) findViewById(R.id.imageViewIcon);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("advertising");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String link_advertising = (String) dataSnapshot.getValue();
                Picasso.with(getApplicationContext()).load(link_advertising).into(ivIcon);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        /*ImageView ivEsc = (ImageView) findViewById(R.id.ivEsc);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale);
        ivEsc.startAnimation(animation);*/
        //Todo: To implement the click on animation "x_pop"
        LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.l_Anim_Esc);
        animationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = true;
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        /*Animation animation = AnimationUtils.loadAnimation(this, R.anim.spin);
        ivIcon.startAnimation(animation);*/

        /*Handler: is an class that allows us to send messages to the messages queue of any thread.
            It is used for two purposes:
            1. To determine actions that the thread will do later in the future.
            2. To set tasks between different threads.
        */
        new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    if(!flag) {
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                }, SPLASH_DISPLAY_LENGTH);

        }
    }

