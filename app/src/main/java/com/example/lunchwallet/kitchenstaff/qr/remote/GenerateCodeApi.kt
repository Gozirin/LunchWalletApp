package com.example.lunchwallet.kitchenstaff.qr.remote

import com.example.lunchwallet.common.model.ApiResponse
import com.example.lunchwallet.kitchenstaff.dashboard.GenerateCodeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GenerateCodeApi {

    @GET(value = "api/v1/staff/generateqrcode")
    suspend fun getQrCode(@Query("mealType") mealType: String): GenerateCodeResponse

}