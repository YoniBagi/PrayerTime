package com.yonatan.asusx541u.pacPrayerTime.model;

import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.yonatan.asusx541u.pacPrayerTime.enums.TypePrayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asusX541u on 18/12/2017.
 */
//class that hold basic node from DB, the class implement Comparable for sort
public class Prayer implements Comparable<Prayer>{
    private String place;
    private String time;
    private String kind;
    private List<String> synagogueList = new ArrayList<>();

    public Prayer(String place, String time) {
        this.place = place;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlace() {

        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getKind() {
        return kind;
    }

    public TypePrayer getTypePrayer(){
        switch (kind){
            case "sahrit":
                return TypePrayer.SAHRIT;
            case "mincha":
                return TypePrayer.MINCHA;
        }
        return TypePrayer.ARVIT;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getHours(){
        int x = Integer.parseInt((time.substring(0, time.indexOf(":"))));
        return Integer.parseInt((time.substring(0, time.indexOf(":"))));
    }

    public int getMinutes(){
        return Integer.parseInt(time.substring(time.indexOf(":") + 1));
    }
    @Override
    public int compareTo(@NonNull Prayer mPrayer) {
        // newTime = for example, to "1531" and this mean to 15:31
        String timeToComp = mPrayer.getTime();
        int newTime = Integer.parseInt(((timeToComp.substring(0, timeToComp.indexOf(":"))) + (timeToComp.substring(timeToComp.indexOf(":") + 1))));
       // int newMinutes = Integer.parseInt(mPrayer.getTime().substring(mPrayer.getTime().indexOf(":") + 1));
        String nextTime = this.time;
        int cTime = Integer.parseInt((nextTime.substring(0, nextTime.indexOf(":")))  + (nextTime.substring(nextTime.indexOf(":") + 1)));
        //int minutes = (nextTime.substring(nextTime.indexOf(":") + 1));

        return cTime - newTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {return true;}
        if (!(obj instanceof Prayer)) {return false;}
        String timeToComp = ((Prayer)obj).getTime();
        return TextUtils.equals(timeToComp, this.time) &&
                TextUtils.equals(((Prayer)obj).place, this.place);
    }

    public List<String> getSynagogueList() {
        return synagogueList;
    }

    public void setSynagogueList(List<String> synagogueList) {
        this.synagogueList = synagogueList;
    }
}
