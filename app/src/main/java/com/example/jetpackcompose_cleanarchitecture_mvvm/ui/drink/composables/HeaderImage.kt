package com.example.jetpackcompose_cleanarchitecture_mvvm.ui.drink.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.focused
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.jetpackcompose_cleanarchitecture_mvvm.R
import com.example.jetpackcompose_cleanarchitecture_mvvm.ui.drink.DrinkState
import kotlin.text.uppercase

@Composable
fun HeaderImage(
    drinkUIState: DrinkState
) {
    val imageFocusRequester = remember { FocusRequester() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        val drinkName = drinkUIState.drink?.name?.trim().orEmpty()

        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(imageFocusRequester)
                .focusable(),
            model = drinkUIState.drink?.imageUrl,
            contentDescription = stringResource(id = R.string.drink_image_content_description, drinkName),
            contentScale = ContentScale.FillBounds,
            alignment = Alignment.Center,
            placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
            error = ColorPainter(Color.Gray),
            onSuccess = {
                imageFocusRequester.requestFocus()
            }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                        startY = 100f
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text = drinkUIState.drink?.category?.uppercase()?.trim().orEmpty(),
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
            Text(
                text = drinkName,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.semantics { heading() }
            )
        }
    }
}