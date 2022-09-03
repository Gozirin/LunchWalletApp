package com.example.lunchwallet.foodbeneficiary.qrscan.repository

import com.example.lunchwallet.common.model.ApiResponse
import com.example.lunchwallet.foodbeneficiary.qrscan.remote.ScanApi
import com.example.lunchwallet.util.Resource
import com.example.lunchwallet.util.errorhandler.ErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ScanRepositoryImpl @Inject constructor(private val api: ScanApi, private val errorHandler: ErrorHandler): ScanRepository {
    override suspend fun onScanSuccess(mealId: String, userId: String): Flow<Resource<ApiResponse>> {
        return flow {
            try {
                emit(Resource.Loading())
                val response = api.onScanSuccess(mealId, userId)
                emit(
                    Resource.Success(
                        data = response
                    )
                )
                emit(
                    Resource.Error(
                        errorMessage = response.message
                    )
                )
            } catch (e: HttpException) {
                val message = errorHandler.parse(e)
                emit(
                    Resource.Error(
                        errorMessage = message
                    )
                )
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        errorMessage = "Couldn't reach server check your internet connection"
                    )
                )
            }
        }
    }
}