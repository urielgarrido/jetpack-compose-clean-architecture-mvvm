package com.example.jetpackcompose_cleanarchitecture_mvvm.ui.drink

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose_cleanarchitecture_mvvm.domain.usecase.GetLastViewedDrinkUseCase
import com.example.jetpackcompose_cleanarchitecture_mvvm.domain.usecase.GetRandomDrinkUseCase
import com.example.jetpackcompose_cleanarchitecture_mvvm.domain.usecase.SaveDrinkLocallyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DrinkViewModel @Inject constructor(
    private val getRandomDrinkUseCase: GetRandomDrinkUseCase,
    private val getLastViewedDrinkUseCase: GetLastViewedDrinkUseCase,
    private val saveDrinkLocallyUseCase: SaveDrinkLocallyUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DrinkState())
    val state: StateFlow<DrinkState> = _state.asStateFlow()

    fun getRandomDrink() {
        if (state.value.isLoading) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            runCatching {
                val result = getRandomDrinkUseCase()
                _state.update {
                    it.copy(isLoading = false, drink = result)
                }
            }.onFailure {
                it.printStackTrace()
                loadLastDrinkFromCache()
            }
        }
    }

    private suspend fun loadLastDrinkFromCache() {
        runCatching {
            val localDrink = getLastViewedDrinkUseCase().also {
                it?.let { drink ->
                    saveDrinkLocallyUseCase(drink)
                }
            }
            if (localDrink != null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        drink = localDrink,
                        error = ErrorState.NoConnectionWithData
                    )
                }
            } else {
                _state.update {
                    it.copy(isLoading = false, error = ErrorState.NoConnectionWithoutData)
                }
            }
        }.onFailure { e ->
            _state.update { state ->
                state.copy(
                    isLoading = false,
                    error = ErrorState.Unexpected(e.message.toString())
                )
            }
        }
    }
}
