package com.example.lunchwallet.common.logout.repository

import com.example.lunchwallet.common.logout.remote.LogoutApi
import com.example.lunchwallet.common.model.ApiResponse

class LogoutRepositoryImpl(private val api: LogoutApi): LogoutRepository {


    override suspend fun logoutBeneficiary(email: String): ApiResponse {
       return api.logoutBeneficiary(email)
    }

    override suspend fun logoutKitchenStaff(email: String): ApiResponse {
        return api.logoutKitchenStaff(email)
    }

    override suspend fun logoutAdmin(email: String): ApiResponse {
        return api.logoutAdmin(email)
    }
}