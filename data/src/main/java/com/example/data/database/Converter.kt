package com.example.data.database

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromLongList(list: List<Long>?): String {
        return list?.joinToString(",") ?: ""
    }
    @TypeConverter
    fun toLongList(data: String?): List<Long> {
        return data?.split(",")?.mapNotNull { it.toLongOrNull() } ?: emptyList()
    }
}