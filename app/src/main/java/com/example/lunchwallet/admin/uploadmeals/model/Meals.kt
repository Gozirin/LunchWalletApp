package com.example.lunchwallet.admin.uploadmeals.model
import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.example.lunchwallet.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class Meals(
    @DrawableRes val image: Int?= null,
    val name: String?= null,
    val timeServing: String?= null,
    val kitchen: String?= null,
    val year: String?= null,
    val month: String? = null,
    val date: String? = null

): Parcelable {
    companion object {
        val listOfMeals = arrayListOf<Meals>(
            Meals(R.drawable.chicken, "Jollof rice & chicken", "12.15PM (Brunch)", "Uno kitchen"),
            Meals(R.drawable.jellofrice, "Rice & egg sauce", "12.15PM (Brunch)", "Fara park kitchen"),
            Meals(R.drawable.egusi_soup, "Egusi soup & semo", "12.15PM (Dinner)", "Uno kitchen"),
            Meals(R.drawable.eforiro, "Efo riro & semo", "12.15PM (Dinner)", "Fara park kitchen")
        )
    }
}
