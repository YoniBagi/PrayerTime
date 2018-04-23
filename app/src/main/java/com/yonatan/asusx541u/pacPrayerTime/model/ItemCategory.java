package com.yonatan.asusx541u.pacPrayerTime.model;

/**
 * Created by asusX541u on 21/04/2018.
 */

public class ItemCategory {
    private String nameCategory;
    private String linkIcon;

    public ItemCategory(String nameCategory, String linkIcon) {
        this.nameCategory = nameCategory;
        this.linkIcon = linkIcon;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public String getLinkIcon() {
        return linkIcon;
    }

    public void setLinkIcon(String linkIcon) {
        this.linkIcon = linkIcon;
    }
}
