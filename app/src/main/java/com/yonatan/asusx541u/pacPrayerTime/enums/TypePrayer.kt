package com.yonatan.asusx541u.pacPrayerTime.enums

enum class TypePrayer constructor(val type: String) {
    SAHRIT("sahrit"),
    MINCHA("mincha"),
    ARVIT("arvit");

    fun getHebPrayer(): String {
        return when(this) {
            SAHRIT -> "שחרית"
            MINCHA -> "מנחה"
            ARVIT -> "ערבית"
        }
    }
}