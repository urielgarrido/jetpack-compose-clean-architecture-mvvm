package com.example.jetpackcompose_cleanarchitecture_mvvm

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.jetpackcompose_cleanarchitecture_mvvm.ui.drink.DrinkActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, DrinkActivity::class.java))
        finish()
    }
}