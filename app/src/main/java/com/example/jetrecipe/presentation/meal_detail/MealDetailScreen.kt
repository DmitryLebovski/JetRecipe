package com.example.jetrecipe.presentation.meal_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.jetrecipe.R
import com.example.jetrecipe.domain.model.MealDetail
import com.example.jetrecipe.presentation.main_screen.ErrorScreen
import com.example.jetrecipe.presentation.main_screen.LoadingScreen
import com.example.jetrecipe.utils.RecipeUiState

@Composable
fun MealDetailScreen(
    mealID: Int,
    viewModel: MealDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = mealID) {
        viewModel.loadMealDetails(mealID)
    }

    val details by viewModel.details.collectAsState()
    val uiState = viewModel.recipeUiState

    when (uiState) {
        is RecipeUiState.Loading -> {
            LoadingScreen(modifier = Modifier.fillMaxSize())
        }
        is RecipeUiState.Error -> {
            ErrorScreen(modifier = Modifier.fillMaxSize())
        }
        is RecipeUiState.Success -> {
            DetailsContent(details = details)
        }
    }
}

@Composable
fun DetailsContent(details: MealDetail) {
    var isFavorite by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
        ) {
            Image(
                painter = rememberAsyncImagePainter(details.mealPictureURL),
                contentDescription = "Meal image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            Text(
                text = details.meal,
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
            )

            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                tint = if (isFavorite) Color.Red else Color.White,
                contentDescription = "Favorite",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 8.dp)
                    .clickable {
                        isFavorite = !isFavorite
                    }
            )
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.category, details.category),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = stringResource(R.string.area, details.area),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.instruction),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = details.instructions,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(12.dp))

        if (details.ingredients.isNotEmpty()) {
            Text(
                text = stringResource(R.string.ingredients),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            details.ingredients.forEach { ing ->
                Text(
                    text = "• ${ing.ingredient} — ${ing.measure}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        if (details.youtubeURL.isNotEmpty()) {
            Text(
                text = stringResource(R.string.youtube_link),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = details.youtubeURL,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Blue,
                modifier = Modifier.clickable {
                    TODO()
                }
            )
        }
    }
}
