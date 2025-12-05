package com.example.jetpackcompose_cleanarchitecture_mvvm.domain.usecase

import com.example.jetpackcompose_cleanarchitecture_mvvm.domain.model.Drink
import com.example.jetpackcompose_cleanarchitecture_mvvm.domain.repository.DrinkRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetLastViewedDrinkUseCaseTest {

    // 1. Mockeamos el Repositorio
    private val repository: DrinkRepository = mockk()

    // 2. El UseCase
    private lateinit var getLastViewedDrinkUseCase: GetLastViewedDrinkUseCase

    @Before
    fun setUp() {
        getLastViewedDrinkUseCase = GetLastViewedDrinkUseCase(repository)
    }

    @Test
    fun `invoke calls repository and returns a drink when cached data exists`() = runTest {
        // GIVEN (Dado que el repositorio devuelve una bebida guardada)
        val cachedDrink = Drink(
            name = "Cached Margarita",
            category = "Cocktail",
            instructions = "Mix it",
            imageUrl = "url_cached",
            ingredients = emptyList()
        )
        coEvery { repository.getLastViewedDrink() } returns cachedDrink

        // WHEN (Cuando invocamos el caso de uso)
        val result = getLastViewedDrinkUseCase()

        // THEN (Entonces el resultado debe ser esa bebida)
        assertThat(result).isEqualTo(cachedDrink)

        // Verificamos que se llamó a la fun correcta del repositorio
        coVerify(exactly = 1) { repository.getLastViewedDrink() }
    }

    @Test
    fun `invoke returns null when no cached data exists`() = runTest {
        // GIVEN (El repositorio no tiene nada guardado)
        coEvery { repository.getLastViewedDrink() } returns null

        // WHEN
        val result = getLastViewedDrinkUseCase()

        // THEN
        assertThat(result).isNull()

        coVerify(exactly = 1) { repository.getLastViewedDrink() }
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository fails`() = runTest {
        // GIVEN (El repositorio falla al leer la DB)
        coEvery { repository.getLastViewedDrink() } throws Exception("Database Error")

        // WHEN (Debería lanzar la excepción hacia arriba)
        getLastViewedDrinkUseCase()
    }
}
