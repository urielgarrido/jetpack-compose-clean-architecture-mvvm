package com.example.jetpackcompose_cleanarchitecture_mvvm.ui.drink

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose_cleanarchitecture_mvvm.domain.usecase.GetLastViewedDrinkUseCase
import com.example.jetpackcompose_cleanarchitecture_mvvm.domain.usecase.GetRandomDrinkUseCase
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
    private val getLastViewedDrink: GetLastViewedDrinkUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DrinkState())
    val state: StateFlow<DrinkState> = _state.asStateFlow()

    fun getRandomDrink() {
        if (state.value.isLoading) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val result = getRandomDrinkUseCase()
                _state.update {
                    it.copy(isLoading = false, drink = result)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                loadLastDrinkFromCache()
            }
        }
    }

    private suspend fun loadLastDrinkFromCache() {
        try {
            val localDrink = getLastViewedDrink()
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
        } catch (e: Exception) {
            _state.update {
                it.copy(
                    isLoading = false,
                    error = ErrorState.Unexpected(e.message.toString())
                )
            }
        }
    }
}
