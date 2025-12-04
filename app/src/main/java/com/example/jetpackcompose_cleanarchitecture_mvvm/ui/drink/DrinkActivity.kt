package com.example.jetpackcompose_cleanarchitecture_mvvm.ui.drink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.jetpackcompose_cleanarchitecture_mvvm.ui.drink.screens.DrinkScreen
import com.example.jetpackcompose_cleanarchitecture_mvvm.ui.theme.JetpackComposeCleanArchitectureMVVMTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DrinkActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetpackComposeCleanArchitectureMVVMTheme {
                DrinkScreen(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}