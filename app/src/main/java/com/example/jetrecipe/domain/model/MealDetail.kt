package com.example.jetrecipe.domain.model

import com.example.jetrecipe.data.remote.MealIngredient

data class MealDetail (
    val mealID: Int,
    val meal: String,
    val category: String,
    val area: String,
    val instructions: String,
    val mealPictureURL: String,
    val tags: String,
    val youtubeURL: String,
    val dataModified: String,
    val ingredients: List<MealIngredient> = emptyList(),
    val isFavorite: Boolean
)
