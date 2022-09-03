package com.example.lunchwallet.common.authentication.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.lunchwallet.common.authentication.UserAuth
import com.example.lunchwallet.common.authentication.UserDatastore
import com.example.lunchwallet.common.authentication.UserStore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

class RefreshTokenWorker(context: Context, workerParameters: WorkerParameters): Worker(context, workerParameters) {
    override fun doWork(): Result {
//        val userStore: UserStore = UserDatastore(applicationContext)
//        var token = ""
//       return try {
//            if (token.toString() == "") {
//                token =
//                    runBlocking { userStore.getAccessToken() }.toString()
//
//            }
//            Result.success()
//        } catch (e: Throwable) {
//            e.printStackTrace()
//            Log.e("TAG", "doWork: Error refreshing token",)
//            Result.failure()
//        }
      return  Result.success()
    }

}