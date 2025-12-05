package com.example.jetpackcompose_cleanarchitecture_mvvm.ui.drink

import app.cash.turbine.test
import com.example.jetpackcompose_cleanarchitecture_mvvm.domain.model.Drink
import com.example.jetpackcompose_cleanarchitecture_mvvm.domain.usecase.GetLastViewedDrinkUseCase
import com.example.jetpackcompose_cleanarchitecture_mvvm.domain.usecase.GetRandomDrinkUseCase
import com.example.jetpackcompose_cleanarchitecture_mvvm.util.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class DrinkViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getRandomDrinkUseCase: GetRandomDrinkUseCase = mockk()
    private val getLastViewedDrinkUseCase: GetLastViewedDrinkUseCase = mockk()

    private lateinit var viewModel: DrinkViewModel

    @Test
    fun `getRandomDrink emits Loading and then Success when API calls succeeds`() = runTest {
        // GIVEN
        val mockDrink = Drink(name = "Gin Tonic", imageUrl = "img", category = "Bar", ingredients = listOf(Pair("lime", "1")), instructions = "Easy")

        // Simulamos un pequeño delay para asegurar que vemos el estado de Loading
        coEvery { getRandomDrinkUseCase() } coAnswers {
            delay(10)
            mockDrink
        }

        viewModel = DrinkViewModel(getRandomDrinkUseCase, getLastViewedDrinkUseCase)

        // WHEN & THEN
        viewModel.state.test {
            // 1. Estado Inicial
            val initialState = awaitItem()
            assertThat(initialState.isLoading).isFalse()
            assertThat(initialState.drink).isNull()

            // 2. Acción
            viewModel.getRandomDrink()

            // 3. Estado Loading
            val loadingState = awaitItem()
            assertThat(loadingState.isLoading).isTrue()

            // 4. Estado de Éxito
            val successState = awaitItem()
            assertThat(successState.isLoading).isFalse()
            assertThat(successState.drink).isEqualTo(mockDrink)
            assertThat(successState.error).isNull()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getRandomDrink emits Error and tries Local when API fails`() = runTest {
        // GIVEN
        // 1. API falla con DELAY
        coEvery { getRandomDrinkUseCase() } coAnswers {
            delay(10)
            throw RuntimeException("No Internet")
        }

        // 2. Local responde con éxito
        val localDrink = Drink(name = "Old Fashioned", imageUrl = "loc", category = "Old", ingredients = listOf(Pair("lime", "1")), instructions = "Classic")
        coEvery { getLastViewedDrinkUseCase() } returns localDrink

        viewModel = DrinkViewModel(getRandomDrinkUseCase, getLastViewedDrinkUseCase)

        // WHEN & THEN
        viewModel.state.test {
            // 1. Estado Inicial
            awaitItem()

            // 2. Ejecutamos la acción
            viewModel.getRandomDrink()

            // 3. Estado Loading
            val loadingState = awaitItem()
            assertThat(loadingState.isLoading).isTrue()

            // 4. Estado Final (Fallback a local)
            val fallbackState = awaitItem()
            assertThat(fallbackState.isLoading).isFalse()
            assertThat(fallbackState.drink).isEqualTo(localDrink)

            // Verificamos el error específico
            assertThat(fallbackState.error).isEqualTo(ErrorState.NoConnectionWithData)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getRandomDrink emits Error NoConnectionWithoutData when API fails and Cache is empty`() = runTest {
        // GIVEN
        // 1. API falla con DELAY
        coEvery { getRandomDrinkUseCase() } coAnswers {
            delay(10)
            throw RuntimeException("No Internet")
        }

        // 2. Cache vacía
        coEvery { getLastViewedDrinkUseCase() } returns null

        viewModel = DrinkViewModel(getRandomDrinkUseCase, getLastViewedDrinkUseCase)

        viewModel.state.test {
            awaitItem() // Inicial

            viewModel.getRandomDrink() // Acción

            val loadingState = awaitItem() // Loading
            assertThat(loadingState.isLoading).isTrue()

            // Estado final
            val errorState = awaitItem()
            assertThat(errorState.isLoading).isFalse()
            assertThat(errorState.drink).isNull()
            assertThat(errorState.error).isEqualTo(ErrorState.NoConnectionWithoutData)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
