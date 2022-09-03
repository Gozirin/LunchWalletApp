package com.example.lunchwallet.util.errorhandler

import android.content.Context
import com.example.lunchwallet.R
import com.example.lunchwallet.common.login.model.LoginResponse
import com.google.gson.Gson
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class LoginErrorHandler@Inject constructor(context: Context) {

    private val context = context
    fun parse(exception: Exception): String {
        return when(exception){
            is HttpException -> {
                return try {
                    var errorMessage = ""
                    val obj = Gson().fromJson(
                        exception.response()?.errorBody()?.string(),
                        LoginResponse::class.java
                    )
                    errorMessage += "${obj.message}\n"
//                    if (obj.errors != null){
//                        for (value in obj.errors){
//                            errorMessage += value
//                        }
//                    }
                    return errorMessage.trim()
                } catch (exp: Exception){
                    context.getString(R.string.error_exception_unknown_error)
                }
            }
            is UnknownHostException -> context.getString(R.string.error_exception_network_error)
            is SocketTimeoutException -> context.getString(R.string.error_exception_time_out_error)
            else -> exception.message ?: context.getString(R.string.error_exception_unknown_error)
        }
    }
}