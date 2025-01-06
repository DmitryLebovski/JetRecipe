package com.example.jetrecipe.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("categories.php")
    suspend fun getCategories(): CategoryResponse

    @GET("filter.php")
    suspend fun getMealsByCategory(@Query("c") category: String): MealResponse

    @GET("lookup.php")
    suspend fun getMealDetails(@Query("i") id: Int): MealDetailResponse

}