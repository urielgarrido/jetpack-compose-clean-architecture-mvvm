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

class GetRandomDrinkUseCaseTest {

    private val repository: DrinkRepository = mockk()

    private lateinit var getRandomDrinkUseCase: GetRandomDrinkUseCase

    @Before
    fun setUp() {
        getRandomDrinkUseCase = GetRandomDrinkUseCase(repository)
    }

    @Test
    fun `invoke calls repository and returns a drink`() = runTest {
        // GIVEN (Dado un drink)
        val expectedDrink = Drink(name = "Mojito", imageUrl = "url", category = "Cocktail", ingredients = listOf(Pair("lime","1"), Pair("sugar","2")), instructions = "Mix")

        // Cuando el repo sea llamado, devuelve esto
        coEvery { repository.getRandomDrink() } returns expectedDrink

        // WHEN (Cuando ejecutamos la acción)
        val result = getRandomDrinkUseCase()

        // THEN (Entonces verificamos el resultado)
        assertThat(result).isEqualTo(expectedDrink)

        // Verificamos que el repositorio fue llamado exactamente una vez
        coVerify(exactly = 1) { repository.getRandomDrink() }
    }

    @Test(expected = Exception::class)
    fun `invoke throws exception when repository fails`() = runTest {
        // GIVEN
        coEvery { repository.getRandomDrink() } throws Exception("Network Error")

        // WHEN
        getRandomDrinkUseCase()

        // THEN (Se espera que lance la excepción definida en @Test)
    }
}
