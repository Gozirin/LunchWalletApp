package com.example.lunchwallet.admin.uploadmeals.presentation

import com.example.lunchwallet.admin.uploadmeals.model.Meals

sealed class UploadMealIntent{
    data class CreateMealIntent(val meal: Meals): UploadMealIntent()
    data class UpdateMealIntent(val meal: Meals): UploadMealIntent()
    data class DeleteMealIntent(val id: String): UploadMealIntent()
    object GetAllMealsIntent: UploadMealIntent()
}
