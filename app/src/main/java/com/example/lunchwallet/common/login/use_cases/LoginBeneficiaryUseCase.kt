package com.example.lunchwallet.common.login.use_cases

import com.example.lunchwallet.common.login.model.LoginResponse
import com.example.lunchwallet.common.login.model.User
import com.example.lunchwallet.common.login.repository.LoginRepository
import com.example.lunchwallet.util.Resource
import com.example.lunchwallet.util.connectivity.ConnectivityObserver
import com.example.lunchwallet.util.errorhandler.LoginErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LoginBeneficiaryUseCase @Inject constructor(
     private val repo: LoginRepository,
     private val loginErrorHandler: LoginErrorHandler
): LoginUseCase {
    override fun invoke(user: User): Flow<Resource<LoginResponse>> {
        return flow {
            try {
                emit(Resource.Loading())
                val response = repo.loginBeneficiary(user)
                emit(Resource.Success(
                    data = response
                ))
                emit(Resource.Error(
                    errorMessage = response.message
                ))
            }catch(e: HttpException){
                val message = loginErrorHandler.parse(e)
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