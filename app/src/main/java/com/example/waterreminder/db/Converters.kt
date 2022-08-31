package com.example.waterreminder.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Converters {
    private val gson = Gson()

    @TypeConverter
    fun listToString(ids:MutableList<Long>):String{
        return gson.toJson(ids)
    }

    @TypeConverter
    fun stringToList(ids:String):MutableList<Long>{
        val listType = object : TypeToken<MutableList<Long>>(){}.type
        return gson.fromJson(ids,listType)
    }
}