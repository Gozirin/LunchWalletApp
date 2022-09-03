package com.example.lunchwallet.admin.uploadmeals.presentation

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lunchwallet.admin.uploadmeals.model.Meals
import com.example.lunchwallet.admin.uploadmeals.use_cases.UploadMealUseCases
import com.example.lunchwallet.admin.uploadmeals.use_cases.update_meal.UpdateMeal
import com.example.lunchwallet.common.mealtimetable.model.MealTimeTableResponse
import com.example.lunchwallet.common.model.ApiResponse
import com.example.lunchwallet.util.Resource
import com.example.lunchwallet.util.connectivity.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class UploadMealsViewModel @Inject constructor(
    private val useCase: UploadMealUseCases,
    private val connectivityObserver: ConnectivityObserver
): ViewModel() {

    val intent = Channel<UploadMealIntent>(Channel.UNLIMITED)

    private var _mealState= MutableStateFlow<Resource<ApiResponse?>>(Resource.Loading())
    val mealState: StateFlow<Resource<ApiResponse?>> get() = _mealState

    private var _deleteState= MutableStateFlow<Resource<ApiResponse>>(Resource.Loading())
    val deleteState: StateFlow<Resource<ApiResponse>> get() = _deleteState

    private var _getMealsState= MutableStateFlow<Resource<MealTimeTableResponse>>(Resource.Loading())
    val getMealState: StateFlow<Resource<MealTimeTableResponse>> get() = _getMealsState

    private var _updateMealState= MutableStateFlow<Resource<ApiResponse>>(Resource.Loading())
    val updateMealState: StateFlow<Resource<ApiResponse>> get() = _updateMealState

    init {
        handleIntent()
    }


    private fun handleIntent() {
        connectivityObserver.observeNetworkAccess().onEach {
            intent.consumeAsFlow().collect { intent ->
                when (intent) {
                    is UploadMealIntent.CreateMealIntent -> createMeal()
                    is UploadMealIntent.UpdateMealIntent -> updateMeal()
                    is UploadMealIntent.DeleteMealIntent -> deleteMeal()
                    is UploadMealIntent.GetAllMealsIntent -> getAllMeals()
                }
            }
        }.launchIn(viewModelScope)
    }


    fun createMeal( name: String?=null,
                    mealType: String?=null,
                    kitchen: String?=null,
                    year: String?=null,
                    month: String?=null,
                    date: String?=null, imageUri: Uri?= null) {
        viewModelScope.launch {
            if (imageUri != null) {
                useCase.createMealUseCase(name!!, mealType!!, kitchen!!, year!!, month!!, date!!, imageUri )
            }else {
                useCase.createMealUseCase(name!!, mealType!!, kitchen!!, year!!, month!!, date!!)
            }.collect{ mealResponse ->
                    _mealState.value = mealResponse

                    Log.d("TAG", "createMeal:2 ${mealResponse.data} ")
                    Log.d("TAG", "createMeal:3 ${mealResponse.data?.data} ")
                    Log.d("TAG", "createMeal:4 ${mealResponse.data?.errors} ")
                    Log.d("TAG", "createMeal:5 ${mealResponse.data?.message} ")
                    Log.d("TAG", "createMeal:6 ${mealResponse.data?.timestamp} ")
                    Log.d("TAG", "createMeal:7 ${mealResponse.data?.status} ")
                    Log.d("TAG", "createMeal:8 ${mealResponse.message} ")
                }
        }
    }

    fun updateMeal(mealId: String?=null, meal: com.example.lunchwallet.admin.uploadmeals.model.UpdateMeal?= null){
        viewModelScope.launch {
            useCase.updateMealUseCase(mealId!!, meal!!)
                .collect{ updateMealResponse ->
                    _updateMealState.value = updateMealResponse
                }
        }
    }

    fun deleteMeal(id: String? = null) {
        viewModelScope.launch {
            try {
                useCase.deleteMealUseCase(id!!)
                    .collect{ mealResponse ->
                        _deleteState.value = mealResponse
                        Log.d("TAG", "VM/deleteMeal: $mealResponse")
                    }
            }catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun getAllMeals(){
        viewModelScope.launch {
            useCase.getAllMealsUseCase()
                .collect{ mealResponse ->
                    _getMealsState.value = mealResponse
                }
        }
    }
}