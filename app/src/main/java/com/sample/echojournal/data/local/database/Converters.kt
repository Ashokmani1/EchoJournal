package com.sample.echojournal.data.local.database

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.time.LocalDateTime

class Converters
{
    private val gson = Gson()

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime?
    {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String?
    {
        return date?.toString()
    }

    @TypeConverter
    fun fromStringList(value: String): List<String>
    {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun toStringList(list: List<String>): String
    {
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromFloatList(value: String): List<Float>
    {
        val listType = object : TypeToken<List<Float>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun toFloatList(list: List<Float>): String
    {
        return gson.toJson(list)
    }
}