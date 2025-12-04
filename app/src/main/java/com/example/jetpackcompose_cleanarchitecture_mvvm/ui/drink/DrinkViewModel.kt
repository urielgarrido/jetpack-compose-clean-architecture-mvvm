package com.example.jetpackcompose_cleanarchitecture_mvvm.ui.drink

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose_cleanarchitecture_mvvm.R
import com.example.jetpackcompose_cleanarchitecture_mvvm.domain.usecase.GetLastDrinkShowedUseCase
import com.example.jetpackcompose_cleanarchitecture_mvvm.domain.usecase.GetRandomDrinkUseCase
import com.example.jetpackcompose_cleanarchitecture_mvvm.domain.util.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DrinkViewModel @Inject constructor(
    private val getRandomDrinkUseCase: GetRandomDrinkUseCase,
    private val getLastDrinkShowedUseCase: GetLastDrinkShowedUseCase,
    private val resourceProvider: ResourceProvider
): ViewModel() {

    private val _state = MutableStateFlow(DrinkState())
    val state: StateFlow<DrinkState> = _state.asStateFlow()

    init {
        getRandomDrink()
    }

    fun getRandomDrink() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = DrinkState(isLoading = true)
            try {
                val result = getRandomDrinkUseCase()
                _state.value = DrinkState(
                    isLoading = false,
                    drink = result
                )
            } catch (e: Exception) {
                e.printStackTrace()
                loadLastDrinkCached(errorMessage = resourceProvider.getString(R.string.error_no_connection_cached))
            }
        }
    }

    private suspend fun loadLastDrinkCached(errorMessage: String) {
        try {
            val localDrink = getLastDrinkShowedUseCase()

            if (localDrink != null) {
                _state.value = DrinkState(
                    isLoading = false,
                    drink = localDrink,
                    error = errorMessage
                )
            } else {
                _state.value = DrinkState(
                    isLoading = false,
                    error = resourceProvider.getString(R.string.error_no_connection_no_data)
                )
            }
        } catch (e: Exception) {
            _state.value = DrinkState(
                isLoading = false,
                error = resourceProvider.getString(R.string.error_unexpected, e.message.toString())
            )
        }
    }

}