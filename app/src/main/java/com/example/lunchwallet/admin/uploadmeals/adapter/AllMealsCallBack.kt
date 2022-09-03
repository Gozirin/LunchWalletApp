package com.example.lunchwallet.admin.uploadmeals.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.lunchwallet.common.mealtimetable.model.Data
import com.example.lunchwallet.common.mealtimetable.model.MealTimeTableResponse

class AllMealsCallBack(private val oldMeals: MutableList<Data>, private val newMeals: MutableList<Data>): DiffUtil.Callback()  {
    override fun getOldListSize(): Int {
        return oldMeals.size
    }

    override fun getNewListSize(): Int {
        return newMeals.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldMeals[oldItemPosition].ID == newMeals[oldItemPosition].ID
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when{
            oldMeals[oldItemPosition].ID == newMeals[oldItemPosition].ID -> true
            else -> false
        }
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}