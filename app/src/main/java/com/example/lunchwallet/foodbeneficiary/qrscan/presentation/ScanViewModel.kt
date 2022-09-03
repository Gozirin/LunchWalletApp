package com.example.lunchwallet.foodbeneficiary.qrscan.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lunchwallet.common.authentication.UserDatastore
import com.example.lunchwallet.common.model.ApiResponse
import com.example.lunchwallet.foodbeneficiary.qrscan.repository.ScanRepository
import com.example.lunchwallet.kitchenstaff.dashboard.GenerateCodeResponse
import com.example.lunchwallet.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ScanViewModel @Inject constructor(
    private val repo: ScanRepository,
    private val datastore: UserDatastore ): ViewModel() {

    private var _qrScanState =
        MutableStateFlow<Resource<ApiResponse>>(Resource.Loading())
    val qrScanState: StateFlow<Resource<ApiResponse>> get() = _qrScanState

    fun onScanSuccess(mealId: String, userId: String) {
        viewModelScope.launch {
            repo.onScanSuccess(mealId, userId)
                // Update View with the latest response
                // Writes to the value property of MutableStateFlow,
                // adding a new element to the flow and updating all
                // of its collectors
                .collect { qrScanResponse ->
                    _qrScanState.value = qrScanResponse
                    Log.d("TAG", "signUpBeneficiary:beneficiarySignUpResponse ${qrScanResponse.data}")
                }
        }
    }


    fun getUserId(): String? {
        var userId: String? = null
        viewModelScope.launch {
            datastore.userId.collect{
                userId = it
            }
        }
        return userId
    }
}