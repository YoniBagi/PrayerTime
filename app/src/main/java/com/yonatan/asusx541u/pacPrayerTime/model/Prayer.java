package com.yonatan.asusx541u.pacPrayerTime.model;

import android.support.annotation.NonNull;

/**
 * Created by asusX541u on 18/12/2017.
 */
//class that hold basic node from DB, the class implement Comparable for sort
public class Prayer implements Comparable<Prayer>{
    private String place;
    private String time;

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