package com.example.lunchwallet.admin.uploadmeals.repository

import android.net.Uri
import com.example.lunchwallet.admin.uploadmeals.model.Meals
import com.example.lunchwallet.admin.uploadmeals.model.UpdateMeal
import com.example.lunchwallet.common.mealtimetable.model.MealTimeTableResponse
import com.example.lunchwallet.common.model.ApiResponse
import com.example.lunchwallet.util.Resource
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

interface UploadMealsRepository {

    suspend fun createMeal(partMap: MutableMap<String, RequestBody>,
                           file: MultipartBody.Part?=null): ApiResponse

    suspend fun updateMeal(mealId: String, meal: UpdateMeal): ApiResponse

    suspend fun deleteMeal(mealId: String): ApiResponse

    suspend fun getAllMeals(): MealTimeTableResponse
}