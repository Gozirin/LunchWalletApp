package com.example.lunchwallet.admin.uploadmeals.use_cases.delete_meal

import com.example.lunchwallet.admin.uploadmeals.repository.UploadMealsRepository
import com.example.lunchwallet.common.model.ApiResponse
import com.example.lunchwallet.util.Resource
import com.example.lunchwallet.util.errorhandler.ErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DeleteMealUseCase @Inject constructor(
    private val repo: UploadMealsRepository,
    private val errorHandler: ErrorHandler
): DeleteMeal {
    override fun invoke(mealId: String): Flow<Resource<ApiResponse>> {
        return flow {
            try {
                emit(Resource.Loading())
                val response = repo.deleteMeal(mealId)
                emit(
                    Resource.Success(
                    data = response
                ))
                emit(
                    Resource.Error(
                    errorMessage = response.message
                ))
            }catch(e: HttpException){
                val message = errorHandler.parse(e)
                emit(
                    Resource.Error(
                    errorMessage = message
                ))
            }catch (e: IOException){
                emit(
                    Resource.Error(
                    errorMessage = "Couldn't reach server check your internet connection"
                ))
            }
        }
    }
}