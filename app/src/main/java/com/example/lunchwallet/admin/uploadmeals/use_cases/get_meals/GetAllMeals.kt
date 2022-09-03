package com.example.lunchwallet.admin.uploadmeals.use_cases.get_meals

import com.example.lunchwallet.admin.uploadmeals.model.Meals
import com.example.lunchwallet.common.mealtimetable.model.MealTimeTableResponse
import com.example.lunchwallet.common.model.ApiResponse
import com.example.lunchwallet.util.Resource
import kotlinx.coroutines.flow.Flow

interface GetAllMeals {
    operator fun invoke(): Flow<Resource<MealTimeTableResponse>>
}