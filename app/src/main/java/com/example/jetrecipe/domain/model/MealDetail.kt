package com.example.jetrecipe.domain.model

data class MealDetail (
    val mealID: Int,
    val meal: String,
    val category: String,
    val area: String,
    val instructions: String,
    val mealPictureURL: String,
    val tags: String,
    val youtubeURL: String,
    val ingredients: List<String>,
    val measure: List<String>,
    val dataModified: String
)