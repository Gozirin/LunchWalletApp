package com.example.lunchwallet.common.model

import com.google.gson.annotations.JsonAdapter
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class ApiResponse(
    val data: String?= null,
    val errors: List<String>?,
    val message: String?,
    val status: String?,
    val timestamp: String?
)
