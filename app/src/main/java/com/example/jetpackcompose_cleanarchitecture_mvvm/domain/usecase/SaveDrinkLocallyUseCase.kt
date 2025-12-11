package com.example.jetpackcompose_cleanarchitecture_mvvm.domain.usecase

import com.example.jetpackcompose_cleanarchitecture_mvvm.domain.model.Drink
import com.example.jetpackcompose_cleanarchitecture_mvvm.domain.repository.DrinkRepository
import javax.inject.Inject

class SaveDrinkLocallyUseCase @Inject constructor(
    private val drinkRepository: DrinkRepository
) {
    suspend operator fun invoke(drink: Drink) = drinkRepository.saveDrinkLocally(drink)
}