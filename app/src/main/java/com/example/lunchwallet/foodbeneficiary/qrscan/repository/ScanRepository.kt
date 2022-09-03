package com.example.lunchwallet.foodbeneficiary.qrscan.repository

import com.example.lunchwallet.common.model.ApiResponse
import com.example.lunchwallet.util.Resource
import kotlinx.coroutines.flow.Flow

interface ScanRepository {

    suspend fun onScanSuccess(mealId: String, userId: String): Flow<Resource<ApiResponse>>
}