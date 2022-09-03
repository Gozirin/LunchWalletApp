package com.example.lunchwallet.common.login.presentation

import com.example.lunchwallet.common.authentication.UserAuth
import com.example.lunchwallet.common.login.model.User


sealed class LoginIntent {
    data class LoginBeneficiaryIntent(val user: User) : LoginIntent()
    data class LoginKitchenStaffIntent(val user: User): LoginIntent()
    data class LoginAdminIntent(val user: User): LoginIntent()
}