package com.yonatan.asusx541u.pacPrayerTime.enums

enum class TypeNewsViewHolder(private val value: Int) {
    DETAILS_NEWS(0),
    IMAGE_NEWS(1);

    fun getInt(): Int{
        return value
    }
}