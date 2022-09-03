package com.example.lunchwallet.admin.uploadmeals.model

import android.net.Uri

data class Meal(
    val name: String?= null,
    val mealType: String?= null,
    val kitchen: String?= null,
    val year: String?= null,
    val month: String? = null,
    val day: String? = null,
    val imageUri: Uri?= null
)
