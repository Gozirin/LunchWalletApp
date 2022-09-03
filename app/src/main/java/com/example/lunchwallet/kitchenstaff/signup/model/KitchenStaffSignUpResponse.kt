package com.example.lunchwallet.kitchenstaff.signup.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//@JsonClass(generateAdapter = true)
data class KitchenStaffSignUpResponse(
    val data: String?,
   @Json(ignore = true) //ignoring errors variable because it returns a JSONArray and not a JSON object.therefore, throws an exception.
    val errors: List<String>? = null,
    val message: String?,
    val status: String?,
)
