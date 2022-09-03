package com.example.lunchwallet.util.validation

import com.example.lunchwallet.util.*

 fun checkUserType(email: String): String {
    return when {
        email.matches(BENEFICIARY_EMAIL_PATTERN) -> {
            BENEFICIARY
        }
        email.matches(ADMIN_EMAIL_PATTERN) -> {
            ADMIN
        }
        else -> {
            KITCHENSTAFF
        }
    }
}