package com.example.lunchwallet.kitchenstaff.servingstatus.repositories

import com.example.lunchwallet.kitchenstaff.servingstatus.model.BrunchStatusRequest
import com.example.lunchwallet.kitchenstaff.servingstatus.model.BrunchStatusResponse
import com.example.lunchwallet.network.ChangeFoodStatusApi
import com.example.lunchwallet.util.Resource
import com.example.lunchwallet.util.errorhandler.ErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class BrunchStatusRepository @Inject constructor(
    private val changeFoodStatusApi: ChangeFoodStatusApi,
    private val errorHandler: ErrorHandler
) : IBrunchStatusRepositoryInterface {
    override suspend fun brunchStatus(brunchStatusRequest: BrunchStatusRequest): Flow<Resource<BrunchStatusResponse>> {
        return flow {
            try {
                emit(Resource.Loading())
                val response = changeFoodStatusApi.brunchStatus(brunchStatusRequest)
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
