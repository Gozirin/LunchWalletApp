package com.example.lunchwallet.common.confirmMail.remote

import com.example.lunchwallet.common.model.ApiResponse
import retrofit2.http.PATCH
import retrofit2.http.Query

interface ConfirmEmailApi {


    @PATCH(value = "api/v1/user/beneficiaryverifyemail")
    suspend fun confirmEmailBeneficiary(@Query("token") token: String): ApiResponse

    @PATCH(value = "api/v1/user/kitchenstaffverifyemail")
    suspend fun confirmEmailKitchenStaff(@Query("token")token: String): ApiResponse
}