package com.yonatan.asusx541u.pacPrayerTime.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.yonatan.asusx541u.pacPrayerTime.R;
import com.yonatan.asusx541u.pacPrayerTime.Utils.Consts;
import com.yonatan.asusx541u.pacPrayerTime.Utils.UiUtils;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 4500;
    private DatabaseReference databaseReference;
    private boolean flag;
    private String link_advertising;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        UiUtils.INSTANCE.centerTitle(this);
        //NetworkManager.INSTANCE.fetchData();
        setImageAds();
        initLottieAnim();
        handleSplashDisplay();
    }

    private void handleSplashDisplay() {
        new Handler().postDelayed(() -> {
            if (!flag) {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    private void initLottieAnim() {
        LottieAnimationView animationView = findViewById(R.id.l_Anim_Esc);
        animationView.setOnClickListener(v -> {
            flag = true;
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

    }

    private void setImageAds() {
        //The Firebase Realtime Database client automatically downloads the data at these locations and keeps it in sync even if the reference has no active listeners.
        //And now he get the data faster, it's mean that while the animation works I pull the information.
        FirebaseDatabase.getInstance().getReference().keepSynced(true);
        final ImageView ivIcon = findViewById(R.id.imageViewIcon);
        ivIcon.setOnClickListener(v ->{onClickAdsImage();});

        databaseReference = FirebaseDatabase.getInstance().getReference().child("advertising");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                link_advertising = (String) dataSnapshot.getValue();
                Picasso.with(getApplicationContext()).load(link_advertising).into(ivIcon);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void onClickAdsImage() {
        flag = true;
        Intent mainScreenIntent = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(mainScreenIntent);
        Intent imagePopUpIntent = new Intent(SplashScreen.this, ImagePopUp.class);
        imagePopUpIntent.putExtra(Consts.LINK_IMAGE, link_advertising);
        startActivity(imagePopUpIntent);
        finish();
    }
}

