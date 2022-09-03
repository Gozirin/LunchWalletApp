package com.example.lunchwallet.admin.uploadmeals.use_cases.create_meal

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import com.example.lunchwallet.admin.uploadmeals.repository.UploadMealsRepository
import com.example.lunchwallet.common.model.ApiResponse
import com.example.lunchwallet.util.Resource
import com.example.lunchwallet.util.createPartFromString
import com.example.lunchwallet.util.errorhandler.ErrorHandler
import com.example.lunchwallet.util.getFileName
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import javax.inject.Inject

class CreateMealUseCase @Inject constructor(
    private val repo: UploadMealsRepository,
    @ApplicationContext private val context: Context,
    private val errorHandler: ErrorHandler
): CreateMeal {
    override fun invoke( name: String, mealType: String, kitchen: String,
                         year: String, month: String, day: String, imageUri: Uri?): Flow<Resource<ApiResponse?>> {
        return flow {
            try {
                emit(Resource.Loading())
                val map: MutableMap<String, RequestBody> = mutableMapOf()
                val nameRB = createPartFromString(name)
                val mealTypeRB = createPartFromString(mealType)
                val kitchenRB = createPartFromString(kitchen)
                val yearRB = createPartFromString(year)
                val monthRB = createPartFromString(month)
                val dayRB = createPartFromString(day)

                map["name"] = nameRB
                map["type"]= mealTypeRB
                map["kitchen"]= kitchenRB
                map["year"] = yearRB
                map["month"]= monthRB
                map["day"]= dayRB

                var multipartImage: MultipartBody.Part?= null
                if (imageUri != null){
                    val file = Uri.fromFile(File(context.cacheDir, context.contentResolver.getFileName(imageUri))).toFile()
                    multipartImage = MultipartBody.Part.createFormData("image", file.name)
                }

//                val file = Uri.fromFile(File(context.cacheDir, context.contentResolver.getFileName(imageUri))).toFile()

//                val requestFile: RequestBody = RequestBody.create(
//                    "image/jpg".toMediaType(),file)

                val response = if (imageUri != null) {
                    repo.createMeal(map, multipartImage!!)
                }else{
                    repo.createMeal(map)
                }


//                val request = Meal(name, mealType, kitchen, year, month, date)
//                val gson = Gson()
//                val response = repo.createMeal(newMeal =
//                MultipartBody.Part.createFormData("newMeal", gson.toJson(request)),
//                    file= MultipartBody.Part.createFormData(name = "meal image", filename = file.name, body = file.asRequestBody()))

                emit(Resource.Success(
                    data= response
                ))
                emit(Resource.Error(
                    errorMessage = response.message ?: "Something went wrong."
                ))
            }catch(e: HttpException){
                val message = errorHandler.parse(e)
                emit(Resource.Error(
                    errorMessage = message ?: "Oops! something went wrong. Please try again"
                ))
            }catch (e: IOException){
                emit(Resource.Error(
                    errorMessage = "Oops! couldn't reach server, check your internet connection."
                ))
            }
        }
    }
}
