package com.example.lunchwallet.kitchenstaff.qr.repository

import com.example.lunchwallet.common.model.ApiResponse
import com.example.lunchwallet.kitchenstaff.dashboard.GenerateCodeResponse
import com.example.lunchwallet.util.Resource
import kotlinx.coroutines.flow.Flow

interface GenerateCodeRepository {

    suspend fun generateQrCode(mealType: String): Flow<Resource<GenerateCodeResponse>>
}