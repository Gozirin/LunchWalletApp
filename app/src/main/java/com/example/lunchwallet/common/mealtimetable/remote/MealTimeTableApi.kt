package com.example.lunchwallet.common.mealtimetable.remote

import com.example.lunchwallet.common.mealtimetable.model.MealTimeTableResponse
import retrofit2.http.GET

interface MealTimeTableApi {

    @GET(value= "api/v1/user/brunch")
    suspend fun getBrunch(): MealTimeTableResponse

    @GET(value= "api/v1/user/dinner")
    suspend fun getDinner(): MealTimeTableResponse
}