package com.example.jetrecipe.presentation.meal_favorite

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetrecipe.data.remote.MealIngredient
import com.example.jetrecipe.domain.model.MealDetail
import com.example.jetrecipe.utils.RecipeUiState
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MealFavoriteViewModel @Inject constructor() : ViewModel() {

    var recipeUiState: RecipeUiState by mutableStateOf(RecipeUiState.Loading)

    private val _favoriteMeals = MutableStateFlow<List<MealDetail>>(emptyList())
    val favoriteMeals: StateFlow<List<MealDetail>> = _favoriteMeals.asStateFlow()

    private val db = FirebaseFirestore.getInstance()

    fun loadFavoriteMeals(userId: String) {
        viewModelScope.launch {
            recipeUiState = RecipeUiState.Loading
            recipeUiState = try {
                if (userId.isBlank()) RecipeUiState.Error

                db.collection("users")
                    .document(userId)
                    .collection("favorites")
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        val meals = querySnapshot.documents.mapNotNull { doc ->
                            val data = doc.data ?: return@mapNotNull null

                            MealDetail(
                                mealID = (data["id"] as? Long)?.toInt() ?: 0,
                                meal = data["meal"] as? String ?: "",
                                category = data["category"] as? String ?: "",
                                area = data["area"] as? String ?: "",
                                instructions = data["instructions"] as? String ?: "",
                                mealPictureURL = data["mealPictureURL"] as? String ?: "",
                                tags = "",
                                youtubeURL = data["youtubeURL"] as? String ?: "",
                                dataModified = "",
                                ingredients = parseIngredients(data["ingredients"]),
                                isFavorite = true
                            )
                        }
                        _favoriteMeals.value = meals
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Error getting favorites", e)
                    }
                RecipeUiState.Success("Успешно")

            } catch (e: IOException) {
                RecipeUiState.Error
            } catch (e: HttpException) {
                RecipeUiState.Error
            }
        }
    }

    private fun parseIngredients(any: Any?): List<MealIngredient> {
        val list = any as? List<*>
        return list?.mapNotNull { ingMap ->
            val map = ingMap as? Map<*, *>
            val ingredient = map?.get("ingredient") as? String
            val measure = map?.get("measure") as? String
            if (ingredient != null && measure != null) {
                MealIngredient(ingredient, measure)
            } else null
        } ?: emptyList()
    }
}
