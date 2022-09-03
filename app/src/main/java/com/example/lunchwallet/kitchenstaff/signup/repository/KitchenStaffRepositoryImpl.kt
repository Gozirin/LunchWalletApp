package com.example.lunchwallet.kitchenstaff.signup.repository

import com.example.lunchwallet.common.model.ApiResponse
import com.example.lunchwallet.kitchenstaff.signup.model.KitchenStaff
import com.example.lunchwallet.kitchenstaff.signup.remote.KitchenStaffSignUpApi
import com.example.lunchwallet.util.BaseRepository
import javax.inject.Inject

class KitchenStaffRepositoryImpl @Inject constructor(
    private val api: KitchenStaffSignUpApi
): KitchenStaffRepository {
    override suspend fun signUpKitchenStaff(kitchenStaff: KitchenStaff): ApiResponse {
        return  api.signUpKitchenStaff(kitchenStaff)
    }
}