package com.example.lunchwallet.admin.uploadmeals.use_cases.delete_meal

import com.example.lunchwallet.common.model.ApiResponse
import com.example.lunchwallet.util.Resource
import kotlinx.coroutines.flow.Flow

interface DeleteMeal {
    operator fun invoke(mealId: String): Flow<Resource<ApiResponse>>
}