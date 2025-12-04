package com.example.jetpackcompose_cleanarchitecture_mvvm.ui.drink

import app.cash.turbine.test
import com.example.jetpackcompose_cleanarchitecture_mvvm.domain.model.Drink
import com.example.jetpackcompose_cleanarchitecture_mvvm.domain.usecase.GetLastDrinkShowedUseCase
import com.example.jetpackcompose_cleanarchitecture_mvvm.domain.usecase.GetRandomDrinkUseCase
import com.example.jetpackcompose_cleanarchitecture_mvvm.util.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class DrinkViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getRandomDrinkUseCase: GetRandomDrinkUseCase = mockk()
    private val getLastDrinkShowedUseCase: GetLastDrinkShowedUseCase = mockk()

    private lateinit var viewModel: DrinkViewModel

    @Test
    fun `getRandomDrink emits Loading and then Success when API calls succeeds`() = runTest {
        // GIVEN
        val mockDrink = Drink(name = "Gin Tonic", imageUrl = "img", category = "Bar", ingredients = listOf(Pair("lime","1")), instructions = "Easy")
        // Importante: Agrega un pequeño delay simulado al UseCase para que nos dé tiempo de observar el estado de Loading
        coEvery { getRandomDrinkUseCase() } coAnswers {
            kotlinx.coroutines.delay(10) // Simula red
            mockDrink
        }

        // WHEN
        viewModel = DrinkViewModel(getRandomDrinkUseCase, getLastDrinkShowedUseCase)

        // THEN
        viewModel.state.test {
            // 1. Estado inicial o Loading (dependiendo de la velocidad de ejecución)
            // Como pusimos un delay en el mock, el primer estado será Loading = true casi seguro
            // O el estado inicial default y luego Loading.

            var state = awaitItem()

            // Si el primer estado es el default (loading=false, drink=null), esperamos el siguiente
            if (!state.isLoading && state.drink == null) {
                state = awaitItem() // Debería ser Loading
            }

            // Verificamos Loading
            assertThat(state.isLoading).isTrue()

            // 2. Esperamos el resultado final (después del delay del mock)
            val successState = awaitItem()
            assertThat(successState.isLoading).isFalse()
            assertThat(successState.drink).isEqualTo(mockDrink)

            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `getRandomDrink emits Error and tries Local when API fails`() = runTest {
        // GIVEN
        // 1. API falla
        coEvery { getRandomDrinkUseCase() } throws RuntimeException("No Internet")
        // 2. Local responde con éxito
        val localDrink = Drink(name = "Old Fashioned", imageUrl = "loc", category = "Old", ingredients = listOf(Pair("lime","1"), Pair("sugar","2")), instructions = "Classic")
        coEvery { getLastDrinkShowedUseCase() } returns localDrink

        // WHEN
        viewModel = DrinkViewModel(getRandomDrinkUseCase, getLastDrinkShowedUseCase)

        // THEN
        viewModel.state.test {
            awaitItem() // Estado inicial

            // Debería llegar el estado con la data local y quizás un mensaje de error/warning
            val fallbackState = awaitItem()

            assertThat(fallbackState.isLoading).isFalse()
            assertThat(fallbackState.drink).isEqualTo(localDrink)
            // Según tu lógica, si carga de local por fallo de red, tal vez pongas un mensaje en 'error'
            assertThat(fallbackState.error).contains("Sin conexión")

            cancelAndIgnoreRemainingEvents()
        }
    }
}
