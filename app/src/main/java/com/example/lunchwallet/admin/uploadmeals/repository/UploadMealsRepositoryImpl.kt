package com.example.lunchwallet.admin.uploadmeals.repository


import android.net.Uri
import com.example.lunchwallet.admin.uploadmeals.model.Meals
import com.example.lunchwallet.admin.uploadmeals.model.UpdateMeal
import com.example.lunchwallet.admin.uploadmeals.remote.UploadMealsApi
import com.example.lunchwallet.common.mealtimetable.model.MealTimeTableResponse
import com.example.lunchwallet.common.model.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class UploadMealsRepositoryImpl @Inject constructor(
    private val api: UploadMealsApi,
): UploadMealsRepository  {
    override suspend fun createMeal(
        partMap: MutableMap<String, RequestBody>,
        file: MultipartBody.Part?
    ): ApiResponse {
        return api.createMeal(partMap, file)
    }

    override suspend fun updateMeal(mealId: String, meal: UpdateMeal): ApiResponse {
        return api.updateMeal(mealId, meal)
    }

    override suspend fun deleteMeal(mealId: String): ApiResponse {
        return api.deleteMeal(mealId)
    }

    override suspend fun getAllMeals(): MealTimeTableResponse {
       return api.getAllMeals()
    }


}