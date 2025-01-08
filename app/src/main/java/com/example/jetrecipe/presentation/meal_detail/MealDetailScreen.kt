package com.example.jetrecipe.presentation.meal_detail

import android.util.Log
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
import com.example.jetrecipe.reusable_components.ShimmerMealDetail
import com.example.jetrecipe.utils.RecipeUiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun MealDetailScreen(
    mealID: Int = 0,
    mealDetail: MealDetail? = null,
    viewModel: MealDetailViewModel = hiltViewModel()
) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val details by viewModel.details.collectAsState()
    var uiState = viewModel.recipeUiState

    if (mealID != 0) {
        LaunchedEffect(key1 = mealID) {
            viewModel.loadMealDetails(mealID, userId)
        }
    } else {
        uiState = RecipeUiState.Success("Успешно")
    }

    when (uiState) {
        is RecipeUiState.Loading -> {
            ShimmerMealDetail(modifier = Modifier.fillMaxSize())
        }
        is RecipeUiState.Error -> {
            ErrorScreen(modifier = Modifier.fillMaxSize())
        }
        is RecipeUiState.Success -> {
            val transferDetails = if (mealID != 0) details else mealDetail
            (transferDetails)?.let {
                DetailsContent(
                    details = it,
                    onFavoriteToggle = { isFavorite ->
                        viewModel.toggleFavorite(transferDetails, isFavorite, userId)
                    }
                )
            }
        }
    }
}

@Composable
fun DetailsContent(
    details: MealDetail,
    onFavoriteToggle: (Boolean) -> Unit
) {
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
                imageVector = if (details.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                tint = if (details.isFavorite) Color.Red else Color.White,
                contentDescription = "Favorite",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 8.dp)
                    .clickable {
                        onFavoriteToggle(!details.isFavorite)
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


fun checkIfFavorite(mealID: Int, userId: String, onResult: (Boolean) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("users")
        .document(userId)
        .collection("favorites")
        .document(mealID.toString())
        .get()
        .addOnSuccessListener { document ->
            onResult(document.exists())
        }
        .addOnFailureListener {
            onResult(false)
        }
}

fun saveFavoriteMeal(mealDetail: MealDetail, userId: String) {
    val db = FirebaseFirestore.getInstance()
    val mealMap = mapOf(
        "id" to mealDetail.mealID,
        "meal" to mealDetail.meal,
        "category" to mealDetail.category,
        "area" to mealDetail.area,
        "instructions" to mealDetail.instructions,
        "ingredients" to mealDetail.ingredients.map { mapOf("ingredient" to it.ingredient, "measure" to it.measure) },
        "mealPictureURL" to mealDetail.mealPictureURL,
        "youtubeURL" to mealDetail.youtubeURL
    )

    db.collection("users")
        .document(userId)
        .collection("favorites")
        .document(mealDetail.mealID.toString())
        .set(mealMap)
        .addOnSuccessListener {
            Log.d("Firestore", "Meal saved successfully")
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error saving meal", e)
        }
}

fun removeFavoriteMeal(mealID: Int, userId: String) {
    val db = FirebaseFirestore.getInstance()
    db.collection("users")
        .document(userId)
        .collection("favorites")
        .document(mealID.toString())
        .delete()
        .addOnSuccessListener {
            Log.d("Firestore", "Meal removed successfully")
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error removing meal", e)
        }
}


