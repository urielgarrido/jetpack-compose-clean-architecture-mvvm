package com.example.jetpackcompose_cleanarchitecture_mvvm.data.mappers

import com.example.jetpackcompose_cleanarchitecture_mvvm.data.local.entities.DrinkEntity
import com.example.jetpackcompose_cleanarchitecture_mvvm.data.local.entities.SINGLE_DRINK_ID
import com.example.jetpackcompose_cleanarchitecture_mvvm.data.remote.dto.DrinkDetailDto
import com.example.jetpackcompose_cleanarchitecture_mvvm.domain.model.Drink

fun DrinkEntity.toDomain(): Drink {
    return Drink(
        name = name,
        category = category,
        instructions = instructions,
        imageUrl = imageUrl,
        ingredients = ingredients
    )
}

fun setIngredientsList(drinkDetailDto: DrinkDetailDto): List<Pair<String, String>> {
    val ingredientsList = mutableListOf<Pair<String, String>>()

    with(drinkDetailDto) {
        if (!strIngredient1.isNullOrBlank()) {
            ingredientsList.add(Pair(strIngredient1, strMeasure1.orEmpty()))
        }
        if (!strIngredient2.isNullOrBlank()) {
            ingredientsList.add(Pair(strIngredient2, strMeasure2.orEmpty()))
        }
        if (!strIngredient3.isNullOrBlank()) {
            ingredientsList.add(Pair(strIngredient3, strMeasure3.orEmpty()))
        }
        if (!strIngredient4.isNullOrBlank()) {
            ingredientsList.add(Pair(strIngredient4, strMeasure4.orEmpty()))
        }
        if (!strIngredient5.isNullOrBlank()) {
            ingredientsList.add(Pair(strIngredient5, strMeasure5.orEmpty()))
        }
        if (!strIngredient6.isNullOrBlank()) {
            ingredientsList.add(Pair(strIngredient6, strMeasure6.orEmpty()))
        }
        if (!strIngredient7.isNullOrBlank()) {
            ingredientsList.add(Pair(strIngredient7, strMeasure7.orEmpty()))
        }
        if (!strIngredient8.isNullOrBlank()) {
            ingredientsList.add(Pair(strIngredient8, strMeasure8.orEmpty()))
        }
        if (!strIngredient9.isNullOrBlank()) {
            ingredientsList.add(Pair(strIngredient9, strMeasure9.orEmpty()))
        }
        if (!strIngredient10.isNullOrBlank()) {
            ingredientsList.add(Pair(strIngredient10, strMeasure10.orEmpty()))
        }
        if (!strIngredient11.isNullOrBlank()) {
            ingredientsList.add(Pair(strIngredient11, strMeasure11.orEmpty()))
        }
        if (!strIngredient12.isNullOrBlank()) {
            ingredientsList.add(Pair(strIngredient12, strMeasure12.orEmpty()))
        }
        if (!strIngredient13.isNullOrBlank()) {
            ingredientsList.add(Pair(strIngredient13, strMeasure13.orEmpty()))
        }
        if (!strIngredient14.isNullOrBlank()) {
            ingredientsList.add(Pair(strIngredient14, strMeasure14.orEmpty()))
        }
        if (!strIngredient15.isNullOrBlank()) {
            ingredientsList.add(Pair(strIngredient15, strMeasure15.orEmpty()))
        }
    }

    return ingredientsList
}

fun DrinkDetailDto.toDomain(): Drink {
    return Drink(
        name = strDrink.orEmpty(),
        category = strCategory.orEmpty(),
        instructions = strInstructionsES ?: strInstructions.orEmpty(),
        imageUrl = strDrinkThumb.orEmpty(),
        ingredients = setIngredientsList(this)
    )
}

fun DrinkDetailDto.toEntity(): DrinkEntity {
    return DrinkEntity(
        id = SINGLE_DRINK_ID,
        name = strDrink.orEmpty(),
        category = strCategory.orEmpty(),
        instructions = strInstructions.orEmpty(),
        imageUrl = strDrinkThumb.orEmpty(),
        ingredients = setIngredientsList(this)
    )
}