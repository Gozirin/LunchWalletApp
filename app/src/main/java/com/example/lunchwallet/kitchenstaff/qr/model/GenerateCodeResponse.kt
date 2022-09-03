package com.example.lunchwallet.kitchenstaff.dashboard

data class GenerateCodeResponse(
    val `data`: QrData,
    val errors: Any,
    val message: String,
    val status: String,
    val timestamp: String
)

data class QrData(
    val ID: String,
    val adminName: String,
    val created_at: String,
    val day: Int,
    val deleted_at: String,
    val images: List<Any>,
    val kitchen: String,
    val month: Int,
    val name: String,
    val status: String,
    val type: String,
    val updated_at: String,
    val weekday: String,
    val year: Int
)