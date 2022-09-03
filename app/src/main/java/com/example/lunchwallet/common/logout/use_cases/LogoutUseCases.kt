package com.example.lunchwallet.common.logout.use_cases

data class LogoutUseCases(
    val logoutBeneficiaryUseCase: LogoutBeneficiaryUseCase,
    val logoutKitchenStaffUseCase: LogoutKitchenStaffUseCase,
    val logoutAdminUseCase: LogoutAdminUseCase
)
