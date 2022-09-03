package com.example.lunchwallet.foodbeneficiary.signup.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lunchwallet.foodbeneficiary.signup.model.Beneficiary
import com.example.lunchwallet.common.model.ApiResponse
import com.example.lunchwallet.foodbeneficiary.signup.use_cases.BeneficiarySignUpUseCase
import com.example.lunchwallet.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "BeneficiarySignUpViewModel"
@HiltViewModel
class BeneficiarySignUpViewModel @Inject constructor(private val useCase: BeneficiarySignUpUseCase): ViewModel() {

    private val intent = Channel<BeneficiarySignUpIntent>(Channel.UNLIMITED)
    private var _beneficiarySignUpState =
        MutableStateFlow<Resource<ApiResponse>>(Resource.Loading())
    val beneficiarySignUpState: StateFlow<Resource<ApiResponse>> get() = _beneficiarySignUpState

//    private var _beneficiarySignUpState: MutableLiveData<Resource<BeneficiarySignUpResponse>> = MutableLiveData()
//    val beneficiarySignUpState: LiveData<Resource<BeneficiarySignUpResponse>> get() = _beneficiarySignUpState
    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect{
                when(it) {
                    is BeneficiarySignUpIntent.BeneficiarySignUp -> signUpBeneficiary(Beneficiary())
                }
            }
        }
    }
    fun signUpBeneficiary(beneficiary: Beneficiary) {
        viewModelScope.launch {
            useCase.invoke(beneficiary)
                // Update View with the latest response
                // Writes to the value property of MutableStateFlow,
                // adding a new element to the flow and updating all
                // of its collectors
                .collect { beneficiarySignUpResponse ->
                    _beneficiarySignUpState.value = beneficiarySignUpResponse
                    Log.d(TAG, "signUpBeneficiary:beneficiarySignUpResponse $beneficiarySignUpResponse")
                    Log.d(TAG, "signUpBeneficiary:beneficiarySignUpResponse.message ${beneficiarySignUpResponse.message}")
                    Log.d(TAG, "signUpBeneficiary:beneficiarySignUpResponse.data ${beneficiarySignUpResponse.data}")
                }
        }
    }




}

private sealed class BeneficiarySignUpIntent {
    data class BeneficiarySignUp(val beneficiary: Beneficiary) : BeneficiarySignUpIntent()
}