package com.example.jetpackcompose_cleanarchitecture_mvvm.ui.drink

import com.example.jetpackcompose_cleanarchitecture_mvvm.domain.model.Drink

data class DrinkState(
    val isLoading: Boolean = false,
    val drink: Drink? = null,
    val error: String = ""
)
