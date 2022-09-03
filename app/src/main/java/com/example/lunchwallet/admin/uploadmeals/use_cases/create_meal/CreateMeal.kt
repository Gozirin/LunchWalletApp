package com.example.lunchwallet.admin.uploadmeals.use_cases.create_meal

import android.net.Uri
import com.example.lunchwallet.admin.uploadmeals.model.Meals
import com.example.lunchwallet.common.model.ApiResponse
import com.example.lunchwallet.util.Resource
import kotlinx.coroutines.flow.Flow

interface CreateMeal {
    operator fun invoke ( name: String, mealType: String, kitchen: String,
                          year: String, month: String, day: String, imageUri: Uri?=null): Flow<Resource<ApiResponse?>>
}