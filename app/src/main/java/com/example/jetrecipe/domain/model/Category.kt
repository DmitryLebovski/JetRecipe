package com.example.jetrecipe.domain.model

data class Category (
    val id: Int,
    val category: String,
    val categoryPictureURL: String,
    val categoryDescription: String
)