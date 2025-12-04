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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jetpackcompose_cleanarchitecture_mvvm.R
import com.example.jetpackcompose_cleanarchitecture_mvvm.ui.composables.ErrorBanner
import com.example.jetpackcompose_cleanarchitecture_mvvm.ui.drink.DrinkViewModel
import com.example.jetpackcompose_cleanarchitecture_mvvm.ui.drink.composables.DrinkDetailsSection
import com.example.jetpackcompose_cleanarchitecture_mvvm.ui.drink.composables.HeaderImage

@Composable
fun DrinkScreen(
    modifier: Modifier = Modifier,
    viewModel: DrinkViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val drinkUIState by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            if (!drinkUIState.isLoading) {
                FloatingActionButton(onClick = {
                    viewModel.getRandomDrink()
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

                drinkUIState.error?.let { errorMsg ->
                    ErrorBanner(errorMessage = errorMsg)
                }

                HeaderImage(drinkUIState)

                DrinkDetailsSection(drinkUIState)
            }
        }
    }
}
