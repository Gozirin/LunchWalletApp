package com.example.lunchwallet.common.mealtimetable.repository

import com.example.lunchwallet.common.mealtimetable.model.MealTimeTableResponse

interface MealTimeTableRepository {

    suspend fun getBrunch(): MealTimeTableResponse
    suspend fun getDinner(): MealTimeTableResponse
}