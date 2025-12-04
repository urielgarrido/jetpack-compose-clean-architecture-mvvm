package com.example.jetpackcompose_cleanarchitecture_mvvm.data.repository

import com.example.jetpackcompose_cleanarchitecture_mvvm.data.local.dao.DrinkDao
import com.example.jetpackcompose_cleanarchitecture_mvvm.data.remote.api.DrinkApi
import com.example.jetpackcompose_cleanarchitecture_mvvm.data.remote.dto.DrinkDetailDto
import com.example.jetpackcompose_cleanarchitecture_mvvm.data.remote.dto.DrinkDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DrinkRepositoryImplTest {

    private val api: DrinkApi = mockk()
    private val dao: DrinkDao = mockk(relaxed = true)
    private lateinit var repository: DrinkRepositoryImpl

    @Before
    fun setUp() {
        repository = DrinkRepositoryImpl(api, dao)
    }

    @Test
    fun `getRandomDrink calls API and saves to DB`() = runTest {
        // GIVEN
        // Usamos un mock relajado para evitar el constructor gigante con nulls
        val drinkDetail = mockk<DrinkDetailDto>(relaxed = true) {
            every { idDrink } returns "1"
            every { strDrink } returns "Margarita"
            every { strDrinkThumb } returns "url"
            every { strCategory } returns "Cocktail"
            every { strInstructions } returns "Mix"
            every { strIngredient1 } returns "Tequila"
            every { strMeasure1 } returns "2 oz"
        }

        // La API devuelve un objeto DrinkDto que contiene la lista
        val remoteDrinkDto = DrinkDto(drinks = listOf(drinkDetail))

        coEvery { api.getRandomDrink() } returns remoteDrinkDto

        // WHEN
        val result = repository.getRandomDrink()

        // THEN
        assertThat(result.name).isEqualTo("Margarita")

        // Verificamos que llamó a la API exactamente una vez
        coVerify(exactly = 1) { api.getRandomDrink() }

        // Verificamos que guardó en la base de datos (caché)
        coVerify(exactly = 1) { dao.insertDrink(any()) }
    }
}
