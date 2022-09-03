package com.example.lunchwallet.common.mealtimetable.use_cases

import android.util.Log
import com.example.lunchwallet.common.mealtimetable.model.MealTimeTableResponse
import com.example.lunchwallet.common.mealtimetable.repository.MealTimeTableRepository
import com.example.lunchwallet.util.Resource
import com.example.lunchwallet.util.errorhandler.ErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class GetDinnerTimeTableUseCase @Inject constructor(
    private val repo: MealTimeTableRepository,
    private val errorHandler: ErrorHandler
): MealTimeTableUseCase {
    override fun invoke(): Flow<Resource<MealTimeTableResponse>> {
        return flow {
            try {
                emit(Resource.Loading())
                val response = repo.getDinner()
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