package com.example.jetrecipe.utils

sealed interface RecipeUiState {
    data class Success(val data: String) : RecipeUiState
    object Error : RecipeUiState
    object Loading : RecipeUiState
}