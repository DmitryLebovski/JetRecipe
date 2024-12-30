package com.example.jetrecipe.domain.repository

import com.example.jetrecipe.domain.model.Category
import com.example.jetrecipe.domain.model.Meal

interface MealRepository {
    suspend fun getCategories(): List<Category>
    suspend fun getMealsByCategory(category: String): List<Meal>
}