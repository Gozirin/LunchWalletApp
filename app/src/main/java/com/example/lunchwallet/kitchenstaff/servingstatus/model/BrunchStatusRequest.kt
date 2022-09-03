package com.example.lunchwallet.kitchenstaff.servingstatus.model

import com.google.gson.annotations.SerializedName

data class BrunchStatusRequest(
    @SerializedName("status")
    val status: String?

)
