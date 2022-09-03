package com.example.lunchwallet.common.mealtimetable.model

data class MealTimeTableResponse(
    val `data`: List<Data>?= null,
    val errors: Any,
    val message: String,
    val status: String,
    val timestamp: String
)

data class Data(
    val ID: String,
    val adminName: String,
    val created_at: String,
    val day: Int,
    val deleted_at: String,
    val images: List<Image>?=null,
    val kitchen: String,
    val month: Int,
    val name: String,
    val status: String,
    val type: String,
    val updated_at: String,
    val weekday: String,
    val year: Int
)

data class Image(
    val ID: String,
    val created_at: String,
    val deleted_at: String,
    val product_id: Int,
    val updated_at: String,
    val url: String
)