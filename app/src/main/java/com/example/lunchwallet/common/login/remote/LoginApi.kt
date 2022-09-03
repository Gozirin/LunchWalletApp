package com.example.lunchwallet.common.login.remote

import com.example.lunchwallet.common.login.model.LoginResponse
import com.example.lunchwallet.common.login.model.User
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("api/v1/user/benefactorlogin")
    suspend fun loginBeneficiary(
        @Body user: User
    ): LoginResponse

    @POST("api/v1/user/kitchenstafflogin")
    suspend fun loginKitchenStaff(
        @Body user: User
    ): LoginResponse

    @POST("api/v1/user/adminlogin")
    suspend fun loginAdmin(
        @Body user: User
    ): LoginResponse
}