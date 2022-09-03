package com.example.lunchwallet.util

import android.annotation.SuppressLint
import android.content.ContentProvider
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.view.View
import android.widget.EditText
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.example.lunchwallet.R
import com.google.android.material.snackbar.Snackbar
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.json.JSONArray
import org.json.JSONObject


fun JSONArray.toArrayList(): ArrayList<String> {
    val result = arrayListOf<String>()
    for (i in 0 until this.length()) {
        result.add(this.getString(i))
    }
    return result
}

/**
 * *Moshi converter to convert JSON response to String
 */
fun getMoshi(): Moshi {
    return Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
}

fun stringToJSONObject(value: String): JSONObject {
    return JSONObject(value)
}
