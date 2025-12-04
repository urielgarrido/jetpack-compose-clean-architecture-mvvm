package com.example.jetpackcompose_cleanarchitecture_mvvm.data.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class IngredientsConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromIngredientsList(list: List<Pair<String, String>>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toIngredientsList(jsonString: String): List<Pair<String, String>> {
        val type = object : TypeToken<List<Pair<String, String>>>() {}.type
        return gson.fromJson(jsonString, type)
    }
}