package com.example.jetrecipe.presentation.main_screen

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
class MainViewModel @Inject constructor(
    private val repository: MealRepository
) : ViewModel() {

    var recipeUiState: RecipeUiState by mutableStateOf(RecipeUiState.Loading)
        private set

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _mealsByCategory = MutableStateFlow<Map<String, List<Meal>>>(emptyMap())
    val mealsByCategory: StateFlow<Map<String, List<Meal>>> = _mealsByCategory

    init {
        loadCategoriesAndMeals()
    }

    private fun loadCategoriesAndMeals() {
        viewModelScope.launch {
            recipeUiState = RecipeUiState.Loading
            recipeUiState = try {
                val categories = repository.getCategories()
                _categories.value = categories

                val mealsMap = categories.associate { category ->
                    category.category to repository.getMealsByCategory(category.category).take(5)
                }
                _mealsByCategory.value = mealsMap
                RecipeUiState.Success("${categories.size}")
            } catch (e: IOException) {
                RecipeUiState.Error
            } catch (e: HttpException) {
                RecipeUiState.Error
            }
        }
    }
}