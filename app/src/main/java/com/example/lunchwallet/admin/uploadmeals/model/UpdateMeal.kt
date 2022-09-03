package com.example.lunchwallet.admin.uploadmeals.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpdateMeal(
    @DrawableRes val image: Int?= null,
    val name: String?= null,
    val timeServing: String?= null,
    val kitchen: String?= null,
    val day: Int?=null,
    val month: Int?= null,
    val year: Int? = null
): Parcelable
