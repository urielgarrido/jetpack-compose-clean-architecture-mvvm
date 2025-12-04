package com.example.jetpackcompose_cleanarchitecture_mvvm.ui.drink.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpackcompose_cleanarchitecture_mvvm.R
import com.example.jetpackcompose_cleanarchitecture_mvvm.ui.drink.DrinkState

@Composable
fun DrinkDetailsSection(
    drinkUIState: DrinkState,
    modifier: Modifier = Modifier
) {
    val drink = drinkUIState.drink ?: return

    Column(modifier = modifier.padding(24.dp)) {

        if (drink.ingredients.isNotEmpty()) {
            Text(
                text = stringResource(R.string.ingredients_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .semantics { heading() }
            )

            drink.ingredients.forEach { (ingredient, measure) ->
                IngredientRow(ingredient = ingredient, measure = measure)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        Text(
            text = stringResource(R.string.instructions_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .semantics { heading() }
        )

        Text(
            text = drink.instructions.trim(),
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = 24.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun IngredientRow(
    ingredient: String,
    measure: String
) {
    val displayText = remember(ingredient, measure) {
        if (measure.isNotBlank()) "$ingredient ($measure)" else ingredient
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
            .semantics(mergeDescendants = true) { }
    ) {
        // Bullet point
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = displayText.uppercase().trim(),
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
