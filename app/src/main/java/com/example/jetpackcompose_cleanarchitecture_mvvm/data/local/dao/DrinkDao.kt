package com.example.jetpackcompose_cleanarchitecture_mvvm.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jetpackcompose_cleanarchitecture_mvvm.data.local.entities.DrinkEntity

@Dao
interface DrinkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrink(drink: DrinkEntity)

    @Query("SELECT * FROM drinks LIMIT 1")
    suspend fun getLastDrink(): DrinkEntity?

}