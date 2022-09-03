package com.example.lunchwallet.foodbeneficiary.signup.model

import com.google.gson.annotations.SerializedName

data class Beneficiary(
    @SerializedName("full_name")
    val fullName: String?= null,
    @SerializedName("email")
    val email: String?= null,
    @SerializedName("stack")
    val stack: String?= null,
    @SerializedName("location")
    val location: String?= null,
    @SerializedName("password")
    val password: String?= null
)

