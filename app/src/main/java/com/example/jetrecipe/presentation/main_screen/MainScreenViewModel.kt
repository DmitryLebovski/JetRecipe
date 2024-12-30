package com.example.jetrecipe.presentation.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetrecipe.domain.model.Category
import com.example.jetrecipe.domain.model.Meal
import com.example.jetrecipe.domain.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MealRepository
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _mealsByCategory = MutableStateFlow<Map<String, List<Meal>>>(emptyMap())
    val mealsByCategory: StateFlow<Map<String, List<Meal>>> = _mealsByCategory

    init {
        loadCategoriesAndMeals()
    }

    private fun loadCategoriesAndMeals() {
        viewModelScope.launch {
            val categories = repository.getCategories()
            _categories.value = categories

            val mealsMap = categories.associate { category ->
                category.category to repository.getMealsByCategory(category.category).take(5)
            }
            _mealsByCategory.value = mealsMap
        }
    }
}