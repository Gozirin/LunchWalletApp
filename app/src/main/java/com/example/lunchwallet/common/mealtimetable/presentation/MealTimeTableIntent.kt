package com.example.lunchwallet.common.mealtimetable.presentation

sealed class MealTimeTableIntent {
    object BrunchTimeTableIntent: MealTimeTableIntent()
    object DinnerTimeTableIntent: MealTimeTableIntent()
}
