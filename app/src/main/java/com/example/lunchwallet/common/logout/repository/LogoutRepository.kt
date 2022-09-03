package com.example.lunchwallet.common.logout.repository

import com.example.lunchwallet.common.model.ApiResponse

interface LogoutRepository {

    suspend fun logoutBeneficiary(email: String): ApiResponse
    suspend fun logoutKitchenStaff(email: String): ApiResponse
    suspend fun logoutAdmin(email: String): ApiResponse
}