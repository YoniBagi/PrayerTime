package com.yonatan.asusx541u.pacPrayerTime.model;

import android.support.annotation.NonNull;

/**
 * Created by asusX541u on 18/12/2017.
 */
//class that hold basic node from DB, the class implement Comparable for sort
public class Prayer implements Comparable<Prayer>{
    private String place;
    private String time;
    private String kind;

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

}
