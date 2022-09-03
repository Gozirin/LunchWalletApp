package com.example.lunchwallet.kitchenstaff.qr.repository

import com.example.lunchwallet.common.authentication.Code
import com.example.lunchwallet.common.authentication.UserDatastore
import com.example.lunchwallet.kitchenstaff.dashboard.GenerateCodeResponse
import com.example.lunchwallet.kitchenstaff.qr.remote.GenerateCodeApi
import com.example.lunchwallet.util.Resource
import com.example.lunchwallet.util.errorhandler.ErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GenerateCodeRepositoryImpl @Inject constructor(
    private val api: GenerateCodeApi,
    private val errorHandler: ErrorHandler,
    private val datastore: UserDatastore
): GenerateCodeRepository {
    override suspend fun generateQrCode(mealType: String): Flow<Resource<GenerateCodeResponse>> {
        return flow {
            try {
                emit(Resource.Loading())
                val response = api.getQrCode(mealType)
                emit(Resource.Success(
                    data = response
                ))
                emit(Resource.Error(
                    errorMessage = response.message
                ))
                if (mealType == "brunch") {
                    datastore.saveCode(Code(brunchCode = response.data.ID, null))
                }
                if (mealType == "dinner") {
                    datastore.saveCode(Code(null, dinnerCode = response.data.ID))
                }


                //testing purpose. hard-coded qr code
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