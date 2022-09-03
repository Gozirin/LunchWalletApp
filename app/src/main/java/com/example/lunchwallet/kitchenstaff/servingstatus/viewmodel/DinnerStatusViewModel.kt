package com.example.lunchwallet.kitchenstaff.servingstatus.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lunchwallet.common.login.model.LoginResponse
import com.example.lunchwallet.common.login.model.User
import com.example.lunchwallet.common.model.ApiResponse
import com.example.lunchwallet.kitchenstaff.servingstatus.model.DinnerStatusRequest
import com.example.lunchwallet.kitchenstaff.servingstatus.model.DinnerStatusResponse
import com.example.lunchwallet.kitchenstaff.servingstatus.repositories.IDinnerStatusRepositoryInterface
import com.example.lunchwallet.util.Resource
import com.example.lunchwallet.util.errorhandler.ErrorHandler
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DinnerStatusViewModel @Inject constructor(
    private val dinnerStatusRepositoryInterface: IDinnerStatusRepositoryInterface): ViewModel(){

    private val TAG = "DINNER_STATUS"

    private var _dinnerStatus= MutableStateFlow<Resource<DinnerStatusResponse>>(Resource.Loading())
    val dinnerStatus: StateFlow<Resource<DinnerStatusResponse>> get() = _dinnerStatus


    fun getDinnerStatus(dinnerStatusRequest: DinnerStatusRequest) {
        viewModelScope.launch {
            dinnerStatusRepositoryInterface.dinnerStatus(dinnerStatusRequest)
                .collect{ dinnerResponse ->
                    _dinnerStatus.value = dinnerResponse
                    Log.d(TAG, "getDinnerStatus: viewmodel1 ${_dinnerStatus.value.data}")
                    Log.d(TAG, "getDinnerStatus: viewmodel4 ${dinnerStatus.value.message}")

                }
        }
    }
}





