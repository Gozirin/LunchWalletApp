package com.example.lunchwallet.kitchenstaff.qr.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lunchwallet.common.authentication.UserDatastore
import com.example.lunchwallet.kitchenstaff.dashboard.GenerateCodeResponse
import com.example.lunchwallet.kitchenstaff.qr.repository.GenerateCodeRepository
import com.example.lunchwallet.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenerateCodeViewModel @Inject constructor(
    private val repo: GenerateCodeRepository,
    private val datastore: UserDatastore) : ViewModel() {

    private var _qrCodeState =
        MutableStateFlow<Resource<GenerateCodeResponse>>(Resource.Loading())
    val qrCodeState: StateFlow<Resource<GenerateCodeResponse>> get() = _qrCodeState

    fun getQrCode(mealType: String) {
        viewModelScope.launch {
            repo.generateQrCode(mealType)
                // Update View with the latest response
                // Writes to the value property of MutableStateFlow,
                // adding a new element to the flow and updating all
                // of its collectors
                .collect { qrCodeResponse ->
                    _qrCodeState.value = qrCodeResponse
                    Log.d("TAG", "signUpBeneficiary:beneficiarySignUpResponse ${qrCodeResponse.data}")
                }
        }
    }

    fun getBrunchCode(): String? {
        var brunchCode: String? = null
        viewModelScope.launch {
            datastore.brunchCode.collect{
                brunchCode = it
            }
        }
        return brunchCode
    }

    fun getDinnerCode(): String? {
        var dinnerCode: String? = null
        viewModelScope.launch {
            datastore.brunchCode.collect{
                dinnerCode = it
            }
        }
        return dinnerCode
    }
}
