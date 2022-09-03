package com.example.lunchwallet.admin.uploadmeals.use_cases.update_meal

import com.example.lunchwallet.admin.uploadmeals.model.Meals
import com.example.lunchwallet.admin.uploadmeals.model.UpdateMeal
import com.example.lunchwallet.common.model.ApiResponse
import com.example.lunchwallet.util.Resource
import kotlinx.coroutines.flow.Flow

interface UpdateMeal {

    operator fun invoke (mealId: String, meal: UpdateMeal): Flow<Resource<ApiResponse>>
}