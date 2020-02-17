package com.yonatan.asusx541u.pacPrayerTime.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yonatan.asusx541u.pacPrayerTime.R;

import java.util.List;

/**
 * Created by asusX541u on 25/03/2018.
 */

public class CustomAdapterSyn extends BaseAdapter {
    List<String> synagogueArray;
    Context mContext;
    private LayoutInflater mInflater;
    public CustomAdapterSyn(List<String> synagogueArray, Context context) {
        this.synagogueArray = synagogueArray;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return synagogueArray.size();
    }

    @Override
    public Object getItem(int position) {
        return synagogueArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.row_item_synagogue, null);
        }

        TextView prayerPlace = (TextView) convertView.findViewById(R.id.textView2);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView3);
        imageView.setImageResource(R.drawable.waze_nav);
        prayerPlace.setText(synagogueArray.get(position));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri;
                switch (synagogueArray.get(position)){
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
                        uri = "geo:0,0?q=31.796926, 34.821508&navigate=yes";
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
                        uri = "geo:0,0?q=31.793562, 34.825174&navigate=yes";
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
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
}
