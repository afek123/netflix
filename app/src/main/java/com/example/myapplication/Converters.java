package com.example.myapplication;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {

    // Convert List<List<String>> to JSON
    @TypeConverter
    public static String fromWatchedBy(List<List<String>> watchedBy) {
        Gson gson = new Gson();
        return gson.toJson(watchedBy);
    }

    // Convert JSON to List<List<String>>
    @TypeConverter
    public static List<List<String>> toWatchedBy(String value) {
        Type type = new TypeToken<List<List<String>>>() {}.getType();
        return new Gson().fromJson(value, type);
    }

    // Convert List<String> to JSON
    @TypeConverter
    public static String fromCategoryIds(List<String> categoryIds) {
        Gson gson = new Gson();
        return gson.toJson(categoryIds);
    }

    // Convert JSON to List<String>
    @TypeConverter
    public static List<String> toCategoryIds(String value) {
        Type type = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(value, type);
    }
}