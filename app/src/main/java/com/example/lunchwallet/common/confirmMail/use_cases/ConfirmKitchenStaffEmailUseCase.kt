package com.example.lunchwallet.common.confirmMail.use_cases

import android.util.Log
import com.example.lunchwallet.common.confirmMail.repository.ConfirmEmailRepository
import com.example.lunchwallet.common.model.ApiResponse
import com.example.lunchwallet.util.errorhandler.ErrorHandler
import com.example.lunchwallet.util.Resource
import com.example.lunchwallet.util.connectivity.ConnectivityObserver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val TAG= "ConfirmEmailKitchenStaffUseCase"
class ConfirmKitchenStaffEmailUseCase @Inject constructor(
    private val repo: ConfirmEmailRepository,
    private val errorHandler: ErrorHandler
): ConfirmEmailUseCase {
    override operator fun invoke(token: String): Flow<Resource<ApiResponse>> {
        return flow {
            try {
                emit(Resource.Loading())
                val response = repo.confirmEmailKitchenStaff(token)
                emit(Resource.Success(data = response))
                Log.d(TAG, "invoke:response,response.data:$response, ${response.data}")
                emit(Resource.Error(errorMessage = response.message))
                Log.d(TAG, "invoke:response,response.data:$response, ${response.message}")
            }catch (e: HttpException) {
                val message = errorHandler.parse(e)
                emit(Resource.Error(message))
            } catch (e: IOException) {
                emit(Resource.Error("Couldn't reach server check your internet connection"))
            }
        }
    }
}