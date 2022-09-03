package com.example.lunchwallet.common.login.model

data class LoginResponse(
    val data: Data?,
    val errors: Any?,
    val message: String?,
    val status: String?,
    val timestamp: String?
)

data class Data(
    val access_token: String,
    val refresh_token: String,
    val user: UserZ
)

data class UserZ(
    val ID: String,
    val avatar: String,
    val created_at: String,
    val deleted_at: Any,
    val email: String,
    val full_name: String,
    val is_active: Boolean,
    val location: String,
    val password_hash: String,
    val stack: String,
    val status: String,
    val token: String,
    val updated_at: String
)
