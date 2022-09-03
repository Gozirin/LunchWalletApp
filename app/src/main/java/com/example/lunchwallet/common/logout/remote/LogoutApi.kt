package com.example.lunchwallet.common.logout.remote


import com.example.lunchwallet.common.login.model.User
import com.example.lunchwallet.common.model.ApiResponse
import com.example.lunchwallet.util.AUTHORIZATION
import retrofit2.http.*

interface LogoutApi {
    @POST("api/v1/benefactor/beneficiarylogout")
    suspend fun logoutBeneficiary(
        @Query("email") email: String): ApiResponse

    @POST("api/v1/staff/kitchenstafflogout")
    suspend fun logoutKitchenStaff(@Query("email")email: String): ApiResponse

    @POST("api/v1/admin/adminlogout")
    suspend fun logoutAdmin(@Query("email")email: String): ApiResponse
}