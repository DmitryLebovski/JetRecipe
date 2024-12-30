package com.example.jetrecipe.di

import com.example.jetrecipe.data.remote.MealApi
import com.example.jetrecipe.data.remote.repository.MealRepositoryImpl
import com.example.jetrecipe.domain.repository.MealRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://www.themealdb.com/api/json/v1/1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideMealApi(retrofit: Retrofit): MealApi = retrofit.create(MealApi::class.java)

    @Provides
    @Singleton
    fun provideMealRepository(api: MealApi): MealRepository = MealRepositoryImpl(api)
}