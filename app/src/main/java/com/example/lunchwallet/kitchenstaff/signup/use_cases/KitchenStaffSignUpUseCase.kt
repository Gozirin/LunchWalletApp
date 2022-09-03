package com.example.lunchwallet.kitchenstaff.signup.use_cases

import com.example.lunchwallet.common.model.ApiResponse
import com.example.lunchwallet.kitchenstaff.signup.model.KitchenStaff
import com.example.lunchwallet.kitchenstaff.signup.repository.KitchenStaffRepository
import com.example.lunchwallet.util.errorhandler.ErrorHandler
import com.example.lunchwallet.util.Resource
import com.example.lunchwallet.util.connectivity.ConnectivityObserver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class KitchenStaffSignUpUseCase@Inject constructor(
    private val repo: KitchenStaffRepository,
    private val errorHandler: ErrorHandler,
) {
    operator fun invoke(kitchenStaff: KitchenStaff): Flow<Resource<ApiResponse>> {
        return flow {
            try {
                emit(Resource.Loading())
                val response = repo.signUpKitchenStaff(kitchenStaff)
                emit(Resource.Success(data = response))
                emit(Resource.Error(errorMessage = response.message))
            }catch (e: HttpException) {
                val message = errorHandler.parse(e)
                emit(Resource.Error(message))
            } catch (e: IOException) {
                emit(Resource.Error("Couldn't reach server check your internet connection"))
            }
        }
    }
}