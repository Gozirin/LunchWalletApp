package com.example.lunchwallet.foodbeneficiary.signup.repository

import com.example.lunchwallet.foodbeneficiary.signup.model.Beneficiary
import com.example.lunchwallet.common.model.ApiResponse

interface BeneficiarySignUpRepository {

    suspend fun signUpBeneficiary(beneficiary: Beneficiary): ApiResponse
}