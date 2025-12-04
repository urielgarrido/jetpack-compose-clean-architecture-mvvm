package com.example.jetpackcompose_cleanarchitecture_mvvm.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drinks")
data class DrinkEntity(
    @PrimaryKey
    val id: String = "SINGLE_DRINK_ID",
    val name: String,
    val category: String,
    val instructions: String,
    val imageUrl: String,
    val ingredients: List<Pair<String, String>>
)
