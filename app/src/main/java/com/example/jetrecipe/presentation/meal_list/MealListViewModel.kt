package com.example.jetrecipe.presentation.meal_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetrecipe.domain.model.Category
import com.example.jetrecipe.domain.model.Meal
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
class MealListViewModel @Inject constructor(
    private val repository: MealRepository
) : ViewModel() {

    var recipeUiState: RecipeUiState by mutableStateOf(RecipeUiState.Loading)

    private val _mealsByCategory = MutableStateFlow<List<Meal>>(emptyList())
    val mealsByCategory: StateFlow<List<Meal>> = _mealsByCategory

    fun loadMeals(category: String) {
        viewModelScope.launch {
            recipeUiState = RecipeUiState.Loading
            recipeUiState = try {
                val mealsList = repository.getMealsByCategory(category)
                _mealsByCategory.value = mealsList
                RecipeUiState.Success("${mealsList.size}")
            } catch (e: IOException) {
                RecipeUiState.Error
            } catch (e: HttpException) {
                RecipeUiState.Error
            }
        }
    }
}