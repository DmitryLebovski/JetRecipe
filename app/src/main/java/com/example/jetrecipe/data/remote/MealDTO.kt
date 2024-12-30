package com.example.jetrecipe.data.remote

data class CategoryResponse(
    val categories: List<CategoryDTO>
)

data class CategoryDTO(
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String,
    val strCategoryDescription: String
)

data class MealResponse(
    val meals: List<MealDTO>
)

data class MealDTO(
    val strMeal: String,
    val strMealThumb: String,
    val idMeal: String
)