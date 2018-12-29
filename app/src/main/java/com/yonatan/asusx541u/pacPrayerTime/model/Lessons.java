package com.yonatan.asusx541u.pacPrayerTime.model;

/**
 * Created by asusX541u on 06/05/2018.
 */

public class Lessons {
    String place, time, subject, lecturer;

    public Lessons(String place, String time, String subject, String lecturer) {
        this.place = place;
        this.time = time;
        this.subject = subject;
        this.lecturer = lecturer;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }
}
