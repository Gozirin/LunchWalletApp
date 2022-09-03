package com.example.lunchwallet.kitchenstaff.servingstatus.repositories

import com.example.lunchwallet.kitchenstaff.servingstatus.model.DinnerStatusRequest
import com.example.lunchwallet.kitchenstaff.servingstatus.model.DinnerStatusResponse
import com.example.lunchwallet.network.ChangeFoodStatusApi
import com.example.lunchwallet.util.Resource
import com.example.lunchwallet.util.errorhandler.ErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class DinnerStatusRepository @Inject constructor(
    private val changeFoodStatusApi: ChangeFoodStatusApi,
    private val errorHandler: ErrorHandler
) : IDinnerStatusRepositoryInterface {
    override suspend fun dinnerStatus(dinnerStatusRequest: DinnerStatusRequest): Flow<Resource<DinnerStatusResponse>> {
        return flow {
            try {
                emit(Resource.Loading())
                val response = changeFoodStatusApi.dinnerStatus(dinnerStatusRequest)
                emit(Resource.Success(
                    data = response
                ))
                emit(Resource.Error(
                    errorMessage = response.message
                ))
            }catch(e: HttpException){
                val message = errorHandler.parse(e)
                emit(Resource.Error(
                    errorMessage = message
                ))
            }catch (e: IOException){
                emit(Resource.Error(
                    errorMessage = "Couldn't reach server check your internet connection"
                ))
            }
        }
    }
}
