package com.example.lunchwallet.foodbeneficiary.signup.use_cases

import android.util.Log
import com.example.lunchwallet.foodbeneficiary.signup.model.Beneficiary
import com.example.lunchwallet.common.model.ApiResponse
import com.example.lunchwallet.foodbeneficiary.signup.repository.BeneficiarySignUpRepository
import com.example.lunchwallet.util.errorhandler.ErrorHandler
import com.example.lunchwallet.util.Resource
import com.example.lunchwallet.util.connectivity.ConnectivityObserver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


private const val TAG = "SignUpBeneficiary"
class BeneficiarySignUpUseCase@Inject constructor(
    private val repo: BeneficiarySignUpRepository,
    private val errorHandler: ErrorHandler,
) {
        operator fun invoke(beneficiary: Beneficiary): Flow<Resource<ApiResponse>> {
            return flow {
                try {
                    emit(Resource.Loading())
                    val response = repo.signUpBeneficiary(beneficiary)
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
