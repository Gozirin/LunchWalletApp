package com.example.lunchwallet.common.mealtimetable.repository

import com.example.lunchwallet.common.mealtimetable.model.MealTimeTableResponse
import com.example.lunchwallet.common.mealtimetable.remote.MealTimeTableApi
import javax.inject.Inject

class MealTimeTableRepositoryImpl @Inject constructor(private val api: MealTimeTableApi): MealTimeTableRepository {
    override suspend fun getBrunch(): MealTimeTableResponse {
       return api.getBrunch()
    }

    override suspend fun getDinner(): MealTimeTableResponse {
        return api.getDinner()
    }
}