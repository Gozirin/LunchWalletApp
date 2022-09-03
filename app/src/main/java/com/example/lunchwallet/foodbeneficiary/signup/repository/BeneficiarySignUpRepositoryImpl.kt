package com.example.lunchwallet.foodbeneficiary.signup.repository

import com.example.lunchwallet.foodbeneficiary.signup.model.Beneficiary
import com.example.lunchwallet.common.model.ApiResponse
import com.example.lunchwallet.foodbeneficiary.signup.remote.BeneficiarySignUpApi
import javax.inject.Inject

class BeneficiarySignUpRepositoryImpl @Inject constructor(private val api: BeneficiarySignUpApi): BeneficiarySignUpRepository{
    override suspend fun signUpBeneficiary(beneficiary: Beneficiary): ApiResponse {
        return  api.signUpBeneficiary(beneficiary)
    }


}