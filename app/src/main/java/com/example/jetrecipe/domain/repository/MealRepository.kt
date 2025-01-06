package com.example.jetrecipe.domain.repository

import com.example.jetrecipe.domain.model.Category
import com.example.jetrecipe.domain.model.Meal
import com.example.jetrecipe.domain.model.MealDetail

interface MealRepository {
    suspend fun getCategories(): List<Category>
    suspend fun getMealsByCategory(category: String): List<Meal>
    suspend fun getMealDetails(id: Int): MealDetail
}