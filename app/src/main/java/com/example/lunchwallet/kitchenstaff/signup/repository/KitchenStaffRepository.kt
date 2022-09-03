package com.example.lunchwallet.kitchenstaff.signup.repository

import com.example.lunchwallet.common.model.ApiResponse
import com.example.lunchwallet.kitchenstaff.signup.model.KitchenStaff

interface KitchenStaffRepository {

    suspend fun signUpKitchenStaff(kitchenStaff: KitchenStaff): ApiResponse
}