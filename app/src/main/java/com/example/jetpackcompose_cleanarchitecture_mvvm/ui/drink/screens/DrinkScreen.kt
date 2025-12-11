package com.example.jetpackcompose_cleanarchitecture_mvvm.ui.drink.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.jetpackcompose_cleanarchitecture_mvvm.R
import com.example.jetpackcompose_cleanarchitecture_mvvm.domain.model.Drink
import com.example.jetpackcompose_cleanarchitecture_mvvm.ui.composables.ErrorBanner
import com.example.jetpackcompose_cleanarchitecture_mvvm.ui.drink.DrinkState
import com.example.jetpackcompose_cleanarchitecture_mvvm.ui.drink.ErrorState
import com.example.jetpackcompose_cleanarchitecture_mvvm.ui.drink.composables.DrinkDetailsSection
import com.example.jetpackcompose_cleanarchitecture_mvvm.ui.drink.composables.HeaderImage
import com.example.jetpackcompose_cleanarchitecture_mvvm.ui.theme.JetpackComposeCleanArchitectureMVVMTheme

@Composable
fun DrinkScreen(
    modifier: Modifier = Modifier,
    drinkUIState: DrinkState,
    onGetRandomDrink: () -> Unit
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        if (drinkUIState.drink == null) {
            onGetRandomDrink()
        }
    }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            if (!drinkUIState.isLoading) {
                FloatingActionButton(onClick = {
                    onGetRandomDrink()
                }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_outline_change_circle),
                        contentDescription = stringResource(R.string.load_another_random_drink)
                    )
                }
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (drinkUIState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (drinkUIState.drink != null) {

                drinkUIState.error?.let { errorState ->
                    when (errorState) {
                        ErrorState.NoConnectionWithData -> ErrorBanner(errorMessage = stringResource(R.string.error_no_connection_cached))
                        else -> Unit
                    }
                }

                HeaderImage(drinkUIState)

                DrinkDetailsSection(drinkUIState)
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    when (val errorState = drinkUIState.error) {
                        ErrorState.NoConnectionWithoutData -> ErrorBanner(errorMessage = stringResource(R.string.error_no_connection_no_data))
                        is ErrorState.Unexpected -> ErrorBanner(errorMessage = stringResource(R.string.error_unexpected, errorState.message))
                        else -> Unit
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Loading State")
@Composable
private fun DrinkScreenLoadingPreview() {
    JetpackComposeCleanArchitectureMVVMTheme {
        DrinkScreen(
            drinkUIState = DrinkState(isLoading = true),
            onGetRandomDrink = {}
        )
    }
}

// 2. Preview del Estado de ÉXITO (Datos dummy)
@Preview(showBackground = true, name = "Success State")
@Composable
private fun DrinkScreenSuccessPreview() {
    // Creamos un trago falso para visualizar
    val mockDrink = Drink(
        name = "Margarita Mock",
        instructions = "Mezclar todo con hielo...",
        imageUrl = "",
        ingredients = listOf(Pair("lime", "1")),
        category = "Licor"
    )

    JetpackComposeCleanArchitectureMVVMTheme {
        DrinkScreen(
            drinkUIState = DrinkState(
                isLoading = false,
                drink = mockDrink
            ),
            onGetRandomDrink = {}
        )
    }
}

// 3. Preview del Estado de ERROR
@Preview(showBackground = true, name = "Error State")
@Composable
private fun DrinkScreenErrorPreview() {
    JetpackComposeCleanArchitectureMVVMTheme {
        DrinkScreen(
            drinkUIState = DrinkState(
                isLoading = false,
                error = ErrorState.NoConnectionWithoutData
            ),
            onGetRandomDrink = {}
        )
    }
}
