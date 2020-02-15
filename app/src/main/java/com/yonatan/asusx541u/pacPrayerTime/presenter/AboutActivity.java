package com.yonatan.asusx541u.pacPrayerTime.presenter;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yonatan.asusx541u.pacPrayerTime.R;
import com.yonatan.asusx541u.pacPrayerTime.Utils.UiUtils;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        toolbar();

        ImageView iVLinkdin = (ImageView) findViewById(R.id.iVlinkedin);
        ImageView iVBrowser = (ImageView) findViewById(R.id.iVbrowser);
        ImageView iVFacbook = (ImageView) findViewById(R.id.iVfacebook);
        ImageView iVMail = (ImageView) findViewById(R.id.iVmail);
        TextView tVAboutMe = (TextView) findViewById(R.id.tvAboutMe);
        TextView tvAboutApp = (TextView) findViewById(R.id.tvAboutApp);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Assistant-Regular.ttf");
        tVAboutMe.setTypeface(typeface);
        tVAboutMe.setMovementMethod(new ScrollingMovementMethod());
        Typeface typeface1 = Typeface.createFromAsset(getAssets(),"fonts/Assistant-Regular.ttf");
        tvAboutApp.setTypeface(typeface1);
        tvAboutApp.setMovementMethod(new ScrollingMovementMethod());

        iVLinkdin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/bagisoftwareengineer/"));
                startActivity(intent);
            }
        });

        iVBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://bagi-software.000webhostapp.com/"));
                startActivity(intent);
            }
        });

        iVFacbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/yonibagi"));
                startActivity(intent);
            }
        });

        iVMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"bagiyoni@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "אפליקציית זמן תפילה");
                intent.putExtra(Intent.EXTRA_TEXT, "שלום יהונתן,");
                try {
                    startActivity(Intent.createChooser(intent,"שלח הודעה ליהונתן..."));
                }
                catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(AboutActivity.this,"לא נמצאה אפליקציה לשליחת מייל במכשירך",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void toolbar(){
        UiUtils.INSTANCE.centerTitle(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("אודות");
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
}
