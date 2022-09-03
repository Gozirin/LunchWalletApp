package com.example.lunchwallet.common.confirmMail.use_cases

data class ConfirmEmailUseCases(
    val confirmBeneficiaryEmailUseCase: ConfirmBeneficiaryEmailUseCase,
    val confirmKitchenStaffEmailUseCase: ConfirmKitchenStaffEmailUseCase
)
