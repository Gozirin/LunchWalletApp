package com.example.lunchwallet.admin.uploadmeals.use_cases

import com.example.lunchwallet.admin.uploadmeals.use_cases.create_meal.CreateMealUseCase
import com.example.lunchwallet.admin.uploadmeals.use_cases.delete_meal.DeleteMealUseCase
import com.example.lunchwallet.admin.uploadmeals.use_cases.get_meals.GetAllMealsUseCase
import com.example.lunchwallet.admin.uploadmeals.use_cases.update_meal.UpdateMealUseCase

data class UploadMealUseCases(
    val createMealUseCase: CreateMealUseCase,
    val updateMealUseCase: UpdateMealUseCase,
    val deleteMealUseCase: DeleteMealUseCase,
    val getAllMealsUseCase: GetAllMealsUseCase
)
