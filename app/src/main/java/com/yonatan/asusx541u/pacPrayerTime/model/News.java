package com.yonatan.asusx541u.pacPrayerTime.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by asusX541u on 23/05/2018.
 */

public class News implements Serializable {
    private String link_image;
    private String title;
    private String content;
    private String writer;
    private String date_create;
    private String time_create;

    public News(String link_image, String title, String content, String writer, String date_create, String time_create) {
        this.link_image = link_image;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.date_create = date_create;
        this.time_create = time_create;
    }

    public News() {
    }

    public void setDate_create(String date_create) {
        this.date_create = date_create;
    }

    public String getTime_create() {
        return time_create;
    }

    public void setTime_create(String time_create) {
        this.time_create = time_create;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }


    public String getLink_image() {
        return link_image;
    }

    public void setLink_image(String link_image) {
        this.link_image = link_image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate_create() {
        return date_create;
    }
}
