package com.example.lunchwallet.admin.uploadmeals.adapter

import java.time.LocalDate

interface OnMealSelectedForUpdate {

    fun getMealDetails(mealId: String, mealName: String, mealType: String, kitchen: String, mealDate: String, mealImage: Int? = null )
}