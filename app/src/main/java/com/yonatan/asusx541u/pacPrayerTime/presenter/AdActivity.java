package com.yonatan.asusx541u.pacPrayerTime.presenter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yonatan.asusx541u.pacPrayerTime.R;

public class AdActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        getWindow().setLayout((int) (displayMetrics.widthPixels * .8), (int) (displayMetrics.heightPixels * .7));
        ImageView ivAd = findViewById(R.id.ivAd);
        Intent intent = getIntent();
        Picasso.with(this).load(intent.getStringExtra("LINK_AD")).into(ivAd);
    }
}
