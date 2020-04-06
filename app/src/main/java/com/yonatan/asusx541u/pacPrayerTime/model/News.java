package com.yonatan.asusx541u.pacPrayerTime.model;

import com.yonatan.asusx541u.pacPrayerTime.enums.TypeNewsViewHolder;

import java.io.Serializable;

/**
 * Created by asusX541u on 23/05/2018.
 */

public class News implements Serializable {
    private String img;
    private String title;
    private String content;
    private String writer;
    private String date;
    private String time_create;
    private TypeNewsViewHolder typeNewsViewHolder;

    public News(String link_image, String title, String content, String writer, String date_create, String time_create) {
        this.img = link_image;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.date = date_create;
        this.time_create = time_create;
    }

    public News() {
    }

    public void setDate(String date) {
        this.date = date;
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


    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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

    public String getDate() {
        return date;
    }

    public TypeNewsViewHolder getTypeNewsViewHolder() {
        return typeNewsViewHolder;
    }

    public void setTypeNewsViewHolder(TypeNewsViewHolder typeNewsViewHolder) {
        this.typeNewsViewHolder = typeNewsViewHolder;
    }
}
