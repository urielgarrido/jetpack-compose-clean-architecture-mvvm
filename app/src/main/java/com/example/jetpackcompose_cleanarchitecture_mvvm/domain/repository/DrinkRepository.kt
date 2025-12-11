package com.example.jetpackcompose_cleanarchitecture_mvvm.domain.repository

import com.example.jetpackcompose_cleanarchitecture_mvvm.domain.model.Drink

interface DrinkRepository {
    suspend fun getRandomDrink(): Drink
    suspend fun getLastViewedDrink(): Drink?
    suspend fun saveDrinkLocally(drink: Drink)
}