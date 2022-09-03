package com.example.lunchwallet.kitchenstaff.servingstatus.model

import android.content.Context
import com.google.gson.annotations.SerializedName

class BrunchStatusResponse(
    val `data`: Any,
    val errors: List<String>,
    val message: String,
    val status: String,
    val timestamp: String
)


