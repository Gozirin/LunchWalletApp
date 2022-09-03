package com.example.lunchwallet.util

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.lunchwallet.R
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

fun View.snackbar(message: String, context: Context, view: View?, action:(() -> Unit)? = null ) {
    val snackbar = Snackbar.make(this,
        message, Snackbar.LENGTH_LONG)
        .setBackgroundTint(ContextCompat.getColor(context, R.color.loginbutton))
        .setAnchorView(view)
        .show()

}

fun View.clear(view: EditText) {
    val clear = view.text.clear()
}

@SuppressLint("Range")
fun ContentResolver.getFileName(uri: Uri): String {
    var name = ""
    val cursor = query(uri, null, null, null)
    cursor?.use {
        it.moveToFirst()
        name = cursor.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
    }
    return name

}

fun createPartFromString(stringData: String): RequestBody {
    return stringData.toRequestBody("text/plain".toMediaTypeOrNull())
}

fun View.visibility(isVisible: Boolean) {
    if (isVisible){
        View.VISIBLE
    }else{
        View.INVISIBLE
    }
}

