package com.yonatan.asusx541u.pacPrayerTime.model;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by asusX541u on 15/04/2018.
 */

public class Store {
    private String adress;
    private String name;
    private String note;
    private String number_phone;
    private String link_icon_store;
    private Map opening_hours;

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setNumber_phone(String number_phone) {
        this.number_phone = number_phone;
    }

    public void setOpening_hours(Map opening_hours) {
        this.opening_hours = opening_hours;
    }

    public String getAdress() {

        return adress;
    }

    public String getName() {
        return name;
    }

    public String getNote() {
        return note;
    }

    public String getNumber_phone() {
        return number_phone;
    }

    public String getLink_icon_store() {
        return link_icon_store;
    }

    public void setLink_icon_store(String link_icon_store) {
        this.link_icon_store = link_icon_store;
    }


    public Map getOpening_hours() {
        return opening_hours;
    }

    public Store(String adress, String name, String note, String number_phone, String link_icon_store, Map opening_hours) {

        this.adress = adress;
        this.name = name;
        this.note = note;
        this.number_phone = number_phone;
        this.link_icon_store = link_icon_store;
        this.opening_hours = opening_hours;
    }
}
