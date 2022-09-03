package com.example.lunchwallet.util

import com.example.lunchwallet.admin.uploadmeals.presentation.OptionMealBottomSheetDialogFragment
import java.time.LocalDate

class UploadMealValidation{

    fun verifyDate(location: LocalDate): String? {
        val list = OptionMealBottomSheetDialogFragment().getNextDaysOnIntervalOf(30, 1)
        return if (list.contains(location)) {
            null
        } else {
            "Please select a date"
        }
    }

    fun verifyName(name: String): String? {
        return if (name.isEmpty()) {
            "Please enter first name & last name"
        } else {
            null
        }
    }

    fun verifyServingTime(servingTime: String): String? {
        val list = listOf("Brunch", "Dinner")
        for (i in list) {
           if (!list.contains(servingTime)) {
             return   "Please select serving time"
            }
        }
        return null
    }

    fun verifyKitchen(location: String): String? {
        val list = listOf("Farah Park", "Uno", "Edo Tech Park")
        return if (list.contains(location)) {
            null
        } else {
            "Please select kitchen"
        }
    }
}
