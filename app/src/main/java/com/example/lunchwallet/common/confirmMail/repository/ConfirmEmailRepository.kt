package com.example.lunchwallet.common.confirmMail.repository

import com.example.lunchwallet.common.confirmMail.model.ConfirmEmailResponse
import com.example.lunchwallet.common.model.ApiResponse
import com.example.lunchwallet.util.Resource

interface ConfirmEmailRepository {

    suspend fun confirmEmailBeneficiary(token: String): ApiResponse

    suspend fun confirmEmailKitchenStaff(token: String): ApiResponse
}