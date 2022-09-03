package com.example.lunchwallet.foodbeneficiary.qrscan.remote

import com.example.lunchwallet.common.model.ApiResponse
import retrofit2.http.POST
import retrofit2.http.Query

interface ScanApi {

    @POST(value = "api/v1/benefactor/qrmealrecords")
    suspend fun onScanSuccess(@Query("meal_id")mealId: String, @Query("user_id")userId: String): ApiResponse
}