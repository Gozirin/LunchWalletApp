package com.example.lunchwallet.common.logout.presentation

sealed class LogoutIntent{
    data class LogoutBeneficiaryIntent(val email:String): LogoutIntent()
    data class LogoutKitchenStaffIntent(val email:String): LogoutIntent()
    data class LogoutAdminIntent(val email:String): LogoutIntent()
}
