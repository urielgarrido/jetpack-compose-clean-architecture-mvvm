package com.example.jetpackcompose_cleanarchitecture_mvvm.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.jetpackcompose_cleanarchitecture_mvvm.data.local.converters.IngredientsConverter
import com.example.jetpackcompose_cleanarchitecture_mvvm.data.local.dao.DrinkDao
import com.example.jetpackcompose_cleanarchitecture_mvvm.data.local.entities.DrinkEntity

const val DRINK_DATABASE_NAME = "drink_database"

@Database(entities = [DrinkEntity::class], version = 1, exportSchema = false)
@TypeConverters(IngredientsConverter::class)
abstract class DrinkDatabase: RoomDatabase() {
    abstract val drinkDao: DrinkDao
}