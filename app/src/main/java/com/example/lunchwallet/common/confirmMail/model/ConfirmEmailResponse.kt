package com.example.lunchwallet.common.confirmMail.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


data class ConfirmEmailResponse(
    val data: String?= null,
    val errors: List<String>,
    val message: String,
    val status: String,
    val timestamp: String
)