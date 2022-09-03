package com.example.lunchwallet.kitchenstaff.signup.remote

import com.example.lunchwallet.common.model.ApiResponse
import com.example.lunchwallet.kitchenstaff.signup.model.KitchenStaff
import retrofit2.http.Body
import retrofit2.http.POST

interface KitchenStaffSignUpApi {

    @POST(value = "api/v1/user/kitchenstaffsignup")
    suspend fun signUpKitchenStaff(@Body kitchenStaff: KitchenStaff): ApiResponse
}