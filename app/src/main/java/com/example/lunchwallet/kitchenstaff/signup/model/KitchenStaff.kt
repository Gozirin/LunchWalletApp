package com.example.lunchwallet.kitchenstaff.signup.model

import com.google.gson.annotations.SerializedName

data class KitchenStaff(
    @SerializedName("full_name")
    val fullName: String?= null,
    val email: String?= null,
    val location: String?= null,
    val password: String?= null
)
