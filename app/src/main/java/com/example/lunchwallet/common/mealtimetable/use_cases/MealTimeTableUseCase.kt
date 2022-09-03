package com.example.lunchwallet.common.mealtimetable.use_cases

import com.example.lunchwallet.common.mealtimetable.model.MealTimeTableResponse
import com.example.lunchwallet.util.Resource
import kotlinx.coroutines.flow.Flow

interface MealTimeTableUseCase {

    operator fun invoke(): Flow<Resource<MealTimeTableResponse>>
}