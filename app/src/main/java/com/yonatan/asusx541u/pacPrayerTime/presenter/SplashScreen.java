package com.yonatan.asusx541u.pacPrayerTime.presenter;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.database.FirebaseDatabase;
import com.yonatan.asusx541u.pacPrayerTime.R;

public class SplashScreen extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //The Firebase Realtime Database client automatically downloads the data at these locations and keeps it in sync even if the reference has no active listeners.
        //And now he get the data faster, it's mean that while the animation works I pull the information.
        FirebaseDatabase.getInstance().getReference().keepSynced(true);
        ImageView ivIcon = (ImageView) findViewById(R.id.imageViewIcon);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.spin);
        ivIcon.startAnimation(animation);
        /*Handler: is an class that allows us to send messages to the messages queue of any thread.
            It is used for two purposes:
            1. To determine actions that the thread will do later in the future.
            2. To set tasks between different threads.
        */
        new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                }, SPLASH_DISPLAY_LENGTH);

        }
    }

