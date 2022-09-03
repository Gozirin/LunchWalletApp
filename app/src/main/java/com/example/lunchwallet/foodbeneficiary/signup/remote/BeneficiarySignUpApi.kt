package com.example.lunchwallet.foodbeneficiary.signup.remote

import com.example.lunchwallet.foodbeneficiary.signup.model.Beneficiary
import com.example.lunchwallet.common.model.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface BeneficiarySignUpApi {

    @POST(value = "api/v1/user/beneficiarysignup")
    suspend fun signUpBeneficiary(@Body beneficiary: Beneficiary): ApiResponse
}