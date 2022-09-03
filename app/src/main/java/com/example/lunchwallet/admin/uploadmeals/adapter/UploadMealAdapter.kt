package com.example.lunchwallet.admin.uploadmeals.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.lunchwallet.R
import com.example.lunchwallet.admin.uploadmeals.model.Meals
import com.example.lunchwallet.common.mealtimetable.model.Data
import com.squareup.picasso.Picasso

class UploadMealAdapter() : RecyclerView.Adapter<UploadMealAdapter.UploadMealViewHolder>() {
     var meal: MutableList<Data> = mutableListOf()

    fun setMealTimeTableList(updatedMeals: MutableList<Data>) {
        val diffResult = DiffUtil.calculateDiff(AllMealsCallBack(meal, updatedMeals))
        meal.clear()
        meal.addAll(updatedMeals)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class UploadMealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mealImage: ImageView = itemView.findViewById(R.id.upload_meals_rv_meal_iv)
        val mealName: TextView = itemView.findViewById(R.id.upload_meals_rv_meal_name_tv)
        val timeServing: TextView = itemView.findViewById(R.id.upload_meals_rv_meal_serving_time_tv)
        val kitchen: TextView = itemView.findViewById(R.id.upload_meals_rv_kitchen_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadMealViewHolder {
        val view = LayoutInflater.from(
            parent.context
        ).inflate(
            R.layout.fragment_admin_upload_meal_recycler_view,
            parent, false
        )
        return UploadMealViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: UploadMealViewHolder, position: Int) {
        val current = meal[position]
        holder.apply {
//            Picasso.get().load(current.images?.get(position)?.url).placeholder(R.drawable.jellofrice).into(mealImage)
            mealImage.setImageResource(R.drawable.jellofrice)
            mealName.text = current.name
            timeServing.text = current.type
            kitchen.text = current.kitchen
        }
    }

    override fun getItemCount(): Int {
        return meal.size
    }
}
