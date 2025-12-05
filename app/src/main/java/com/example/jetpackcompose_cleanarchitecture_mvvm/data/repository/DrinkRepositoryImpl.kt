package com.example.jetpackcompose_cleanarchitecture_mvvm.data.repository

import android.util.Log
import com.example.jetpackcompose_cleanarchitecture_mvvm.data.local.dao.DrinkDao
import com.example.jetpackcompose_cleanarchitecture_mvvm.data.local.entities.DrinkEntity
import com.example.jetpackcompose_cleanarchitecture_mvvm.data.mappers.toDomain
import com.example.jetpackcompose_cleanarchitecture_mvvm.data.mappers.toEntity
import com.example.jetpackcompose_cleanarchitecture_mvvm.data.remote.api.DrinkApi
import com.example.jetpackcompose_cleanarchitecture_mvvm.data.remote.exceptions.FetchDrinkException
import com.example.jetpackcompose_cleanarchitecture_mvvm.domain.model.Drink
import com.example.jetpackcompose_cleanarchitecture_mvvm.domain.repository.DrinkRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DrinkRepositoryImpl @Inject constructor(
    private val drinkApi: DrinkApi,
    private val drinkDao: DrinkDao
): DrinkRepository {

    override suspend fun getRandomDrink(): Drink = withContext(Dispatchers.IO) {
        runCatching { fetchFromApi() }
            .onSuccess { drinkEntity ->
                cacheDrinkSafely(drinkEntity)
            }
            .map { it.toDomain() }
            .getOrElse { throwable ->
                throw FetchDrinkException(
                    message = "Failed to fetch random drink",
                    cause = throwable
                )
            }
    }

    private suspend fun fetchFromApi(): DrinkEntity {
        val dto = drinkApi.getRandomDrink()
            .drinks
            ?.firstOrNull()
            ?: throw FetchDrinkException("API returned no drinks")

        return dto.toEntity()
    }

    private suspend fun cacheDrinkSafely(entity: DrinkEntity) {
        runCatching {
            drinkDao.insertDrink(entity)
        }.onFailure { e ->
            Log.e("DrinkRepository", "Failed to cache: $e")
        }
    }


    override suspend fun getLastViewedDrink(): Drink? = withContext(Dispatchers.IO) {
        drinkDao.getLastDrink()?.toDomain()
    }
}