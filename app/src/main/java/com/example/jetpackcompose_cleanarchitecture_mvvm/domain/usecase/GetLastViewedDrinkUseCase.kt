package com.example.jetpackcompose_cleanarchitecture_mvvm.domain.usecase

import com.example.jetpackcompose_cleanarchitecture_mvvm.domain.repository.DrinkRepository
import javax.inject.Inject

class GetLastViewedDrinkUseCase @Inject constructor(
    private val drinkRepository: DrinkRepository
) {
    suspend operator fun invoke() = drinkRepository.getLastViewedDrink()
}