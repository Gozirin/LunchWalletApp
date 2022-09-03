package com.example.lunchwallet.common.logout.use_cases

import com.example.lunchwallet.common.logout.repository.LogoutRepository
import com.example.lunchwallet.common.model.ApiResponse
import com.example.lunchwallet.util.Resource
import com.example.lunchwallet.util.connectivity.ConnectivityObserver
import com.example.lunchwallet.util.errorhandler.ErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LogoutKitchenStaffUseCase @Inject constructor(
    private val repository: LogoutRepository,
    private val errorHandler: ErrorHandler
): LogoutUseCase {
    override fun invoke(email: String): Flow<Resource<ApiResponse>> {
        return flow {
            try {
                emit(Resource.Loading())
                val response = repository.logoutKitchenStaff(email)
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