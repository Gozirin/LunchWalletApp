package com.example.lunchwallet.admin.uploadmeals.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.lunchwallet.R
import com.example.lunchwallet.common.mealtimetable.model.Data

class AllMealsAdapter(
    private val onMealSelected: OnMealSelected,
    private val onMealSelectedForUpdate: OnMealSelectedForUpdate,
    private val context: Context): RecyclerView.Adapter<AllMealsAdapter.AllMealsViewHolder>() {
   var meals: MutableList<Data> = mutableListOf()

    fun setAllMealsList(updatedMeals: MutableList<Data>) {
        val diffResult = DiffUtil.calculateDiff(AllMealsCallBack(meals, updatedMeals))
        meals.clear()
        meals.addAll(updatedMeals)
        diffResult.dispatchUpdatesTo(this)
    }


    inner class AllMealsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val mealId: TextView = itemView.findViewById(R.id.upload_meals_rv_mealId_tv)
        val image: ImageView = itemView.findViewById(R.id.upload_meals_rv_meal_iv)
        val mealName:TextView = itemView.findViewById(R.id.upload_meals_rv_meal_name_tv)
        val mealType: TextView = itemView.findViewById(R.id.upload_meals_rv_meal_serving_time_tv)
        val kitchen: TextView = itemView.findViewById(R.id.upload_meals_rv_kitchen_tv)
        val mealDate: TextView = itemView.findViewById(R.id.upload_meals_rv_date_serving_tv)
        val delete: TextView = itemView.findViewById(R.id.upload_meals_rv_remove_tv)
        val edit: TextView = itemView.findViewById(R.id.upload_meals_rv_edit_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllMealsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_admin_upload_meal_recycler_view, parent, false)
        return AllMealsViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: AllMealsViewHolder, position: Int) {
        viewHolder.apply {
            mealId.text = meals[position].ID
            mealName.text= meals[position].name
            mealType.text = meals[position].type
            kitchen.text = meals[position].kitchen
            mealDate.text = itemView.context.getString(R.string.date_serving, meals[position].year, meals[position].month, meals[position].day)
            image.setImageResource(R.drawable.jellofrice)
//            Picasso.get().load(meals[position].images?.get(position)?.url).centerCrop().into(image)
        }

        viewHolder.delete.setOnClickListener {
            onMealSelected.getMealId(meals[position].ID)
        }
        viewHolder.edit.setOnClickListener {
            onMealSelectedForUpdate.getMealDetails(
                meals[position].ID,
                meals[position].name,
                meals[position].type,
                meals[position].kitchen,
                context.getString(R.string.date_serving, meals[position].year, meals[position].month, meals[position].day)
            )
        }
    }

    override fun getItemCount(): Int = meals.size
}