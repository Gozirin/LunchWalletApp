package com.example.lunchwallet.kitchenstaff.servingstatus.model

import com.google.gson.annotations.SerializedName

data class DinnerStatusResponse(
    val `data`: Any,
    val errors: List<String>,
    val message: String,
    val status: String,
    val timestamp: String
)
