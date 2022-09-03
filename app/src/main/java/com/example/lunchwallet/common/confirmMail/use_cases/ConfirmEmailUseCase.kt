package com.example.lunchwallet.common.confirmMail.use_cases

import com.example.lunchwallet.common.model.ApiResponse
import com.example.lunchwallet.util.Resource
import kotlinx.coroutines.flow.Flow


interface ConfirmEmailUseCase {

    operator fun invoke (token: String): Flow<Resource<ApiResponse>>
}