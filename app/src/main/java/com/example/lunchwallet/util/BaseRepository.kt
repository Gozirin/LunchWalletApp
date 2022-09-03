package com.example.lunchwallet.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

// sample1
abstract class BaseRepository {

    suspend fun <T> safeApiCall(apiToBeCalled: suspend () -> Response<T>): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                val response: Response<T> = apiToBeCalled()
                if (response.isSuccessful) {
                    Resource.Success(data = response.body()!!)
                } else {
                    val errorResponse= response.errorBody()
                    Resource.Error(
                        errorMessage = errorResponse.toString()
                    )
                }
            } catch (e: HttpException) {
                Resource.Error(
                    errorMessage = e.message()
                )
            } catch (e: IOException) {
                Resource.Error(errorMessage = "Please check your network connection")
            } catch (e: Exception) {
                Resource.Error(errorMessage = "Something went wrong")
            }
        }
    }
}