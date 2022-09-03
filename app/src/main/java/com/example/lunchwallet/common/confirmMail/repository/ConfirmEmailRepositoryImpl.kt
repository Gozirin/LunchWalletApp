package com.example.lunchwallet.common.confirmMail.repository

import com.example.lunchwallet.common.confirmMail.model.ConfirmEmailResponse
import com.example.lunchwallet.common.confirmMail.remote.ConfirmEmailApi
import com.example.lunchwallet.common.model.ApiResponse
import com.example.lunchwallet.util.BaseRepository
import javax.inject.Inject

class ConfirmEmailRepositoryImpl @Inject constructor(private val api: ConfirmEmailApi): ConfirmEmailRepository {

    override suspend fun confirmEmailBeneficiary(token: String): ApiResponse {
       return api.confirmEmailBeneficiary(token)
    }

    override suspend fun confirmEmailKitchenStaff(token: String): ApiResponse {
        return  api.confirmEmailKitchenStaff(token)
    }
}