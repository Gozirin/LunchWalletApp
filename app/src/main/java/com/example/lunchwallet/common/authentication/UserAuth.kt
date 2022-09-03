package com.example.lunchwallet.common.authentication

data class UserAuth(
    val userId: String,
    val email: String,
    val accessToken: String,
    val refreshToken: String
)

data class Code(
    val brunchCode: String?,
    val dinnerCode: String?
)
