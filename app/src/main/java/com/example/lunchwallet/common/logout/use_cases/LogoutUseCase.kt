package com.example.lunchwallet.common.logout.use_cases

import com.example.lunchwallet.common.model.ApiResponse
import com.example.lunchwallet.util.Resource
import kotlinx.coroutines.flow.Flow

interface LogoutUseCase {
    operator fun invoke(email: String): Flow<Resource<ApiResponse>>
}