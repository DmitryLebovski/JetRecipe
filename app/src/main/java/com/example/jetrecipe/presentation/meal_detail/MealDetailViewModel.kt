package com.example.jetrecipe.presentation.meal_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetrecipe.domain.model.MealDetail
import com.example.jetrecipe.domain.repository.MealRepository
import com.example.jetrecipe.utils.RecipeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MealDetailViewModel @Inject constructor(
    private val repository: MealRepository
) : ViewModel() {

    var recipeUiState: RecipeUiState by mutableStateOf(RecipeUiState.Loading)
        private set

    private val _details = MutableStateFlow(
        MealDetail(
            0,
            "null",
            category = "null",
            area = "null",
            instructions = "null",
            mealPictureURL = "null",
            tags = "null",
            youtubeURL = "null",
            dataModified = "null",
            ingredients = emptyList()
        )
    )
    val details: StateFlow<MealDetail> = _details

    fun loadMealDetails(id: Int) {
        viewModelScope.launch {
            recipeUiState = RecipeUiState.Loading
            recipeUiState = try {
                val mealDetails = repository.getMealDetails(id)
                _details.value = mealDetails
                RecipeUiState.Success(mealDetails.meal)
            } catch (e: IOException) {
                RecipeUiState.Error
            } catch (e: HttpException) {
                RecipeUiState.Error
            }
        }
    }
}
