package com.example.jetpackcompose_cleanarchitecture_mvvm.domain.model

data class Drink(
    val name: String,
    val category: String,
    val alcoholic: String,
    val glass: String,
    val instructions: String,
    val imageUrl: String,
    val ingredients: List<Pair<String, String>> // Lista de Pares: (Ingrediente, Medida)
)
