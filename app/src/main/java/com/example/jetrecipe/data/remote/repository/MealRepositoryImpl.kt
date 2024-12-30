package com.example.jetrecipe.data.remote.repository

import com.example.jetrecipe.data.remote.MealApi
import com.example.jetrecipe.domain.model.Category
import com.example.jetrecipe.domain.model.Meal
import com.example.jetrecipe.domain.repository.MealRepository
import javax.inject.Inject

class MealRepositoryImpl @Inject constructor(
    private val api: MealApi
):MealRepository {
    override suspend fun getCategories(): List<Category> {
        return api.getCategories().categories.map { dto ->
            Category(
                id = dto.idCategory.toInt(),
                category = dto.strCategory,
                categoryPictureURL = dto.strCategoryThumb,
                categoryDescription = dto.strCategoryDescription
            )
        }
    }

    override suspend fun getMealsByCategory(category: String): List<Meal> {
        return api.getMealsByCategory(category).meals.map { dto ->
            Meal(
                meal = dto.strMeal,
                mealPictureURL = dto.strMealThumb,
                mealID = dto.idMeal.toInt()
            )
        }
    }
}