package com.example.lunchwallet.admin.uploadmeals.remote

import com.example.lunchwallet.admin.uploadmeals.model.Meals
import com.example.lunchwallet.admin.uploadmeals.model.UpdateMeal
import com.example.lunchwallet.common.mealtimetable.model.MealTimeTableResponse
import com.example.lunchwallet.common.model.ApiResponse
import com.example.lunchwallet.util.AUTHORIZATION
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface UploadMealsApi {

    @Multipart
    @POST(value = "api/v1/admin/createtimetable")
    suspend fun createMeal(
        @PartMap() partMap: MutableMap<String,RequestBody>,
        @Part file: MultipartBody.Part?= null): ApiResponse

    @PUT(value = "api/v1/admin/updatemeal/{id}")
    suspend fun updateMeal(
        @Path("id") mealId: String,
        @Body meal: UpdateMeal): ApiResponse

    @DELETE(value = "api/v1/admin/deletemeal/{id}")
    suspend fun deleteMeal(@Path("id") mealId: String): ApiResponse

    @GET(value = "api/v1/user/allfood")
    suspend fun getAllMeals(): MealTimeTableResponse
}