package com.example.jetpackcompose_cleanarchitecture_mvvm.data.remote.api

import com.example.jetpackcompose_cleanarchitecture_mvvm.data.remote.dto.DrinkDto
import retrofit2.http.GET

interface DrinkApi {

    @GET("random.php")
    suspend fun getRandomDrink(): DrinkDto
}