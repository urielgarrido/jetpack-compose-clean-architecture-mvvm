package com.example.jetpackcompose_cleanarchitecture_mvvm.data.repository

import com.example.jetpackcompose_cleanarchitecture_mvvm.data.local.dao.DrinkDao
import com.example.jetpackcompose_cleanarchitecture_mvvm.data.mappers.toDomain
import com.example.jetpackcompose_cleanarchitecture_mvvm.data.mappers.toEntity
import com.example.jetpackcompose_cleanarchitecture_mvvm.data.remote.api.DrinkApi
import com.example.jetpackcompose_cleanarchitecture_mvvm.domain.model.Drink
import com.example.jetpackcompose_cleanarchitecture_mvvm.domain.repository.DrinkRepository
import javax.inject.Inject

class DrinkRepositoryImpl @Inject constructor(
    private val drinkApi: DrinkApi,
    private val drinkDao: DrinkDao
): DrinkRepository {

    override suspend fun getRandomDrink(): Drink {
        try {
            val response = drinkApi.getRandomDrink()
            val drinkDetailDto = response.drinks?.firstOrNull()
            if (drinkDetailDto != null) {
                drinkDao.insertDrink(drinkDetailDto.toEntity())
                return drinkDetailDto.toDomain()
            } else {
                throw Exception("No drinks found in the response")
            }
        }catch (e: Exception) {
            throw Exception("Error fetching random drink: ${e.message}")
        }
    }

    override suspend fun getLastDrinkShowed(): Drink? {
        return try {
            val drinkEntity = drinkDao.getLastDrink()
            drinkEntity?.toDomain()
        }catch (e: Exception) {
            throw Exception("Error fetching last drink: ${e.message}")
        }
    }
}