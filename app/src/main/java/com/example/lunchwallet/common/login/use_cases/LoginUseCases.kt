package com.example.lunchwallet.common.login.use_cases

data class LoginUseCases(
    val loginBeneficiaryUseCase: LoginBeneficiaryUseCase,
    val loginKitchenStaffUseCase: LoginKitchenStaffUseCase,
    val loginAdminUseCase: LoginAdminUseCase
)
