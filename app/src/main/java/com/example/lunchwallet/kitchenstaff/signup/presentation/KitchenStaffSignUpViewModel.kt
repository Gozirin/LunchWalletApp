package com.example.lunchwallet.kitchenstaff.signup.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lunchwallet.common.model.ApiResponse
import com.example.lunchwallet.kitchenstaff.signup.model.KitchenStaff
import com.example.lunchwallet.kitchenstaff.signup.use_cases.KitchenStaffSignUpUseCase
import com.example.lunchwallet.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "KitchenStaffSignUpViewModel"
@HiltViewModel
class KitchenStaffSignUpViewModel @Inject constructor(
    private val useCase: KitchenStaffSignUpUseCase
) : ViewModel() {
    val intent = Channel<KitchenStaffSignUpIntent>(Channel.UNLIMITED)
    private var _kitchenStaffSignUpState =
        MutableStateFlow<Resource<ApiResponse>>(Resource.Loading())
    val kitchenStaffSignUpState: StateFlow<Resource<ApiResponse>> get() = _kitchenStaffSignUpState

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect{
                when(it) {
                    is KitchenStaffSignUpIntent.KitchenStaffSignUp -> signUpKitchenStaff(KitchenStaff())
                }
            }
        }
    }

    fun signUpKitchenStaff(kitchenStaff: KitchenStaff) {
        viewModelScope.launch {
            useCase.invoke(kitchenStaff)
                // Update View with the latest response
                // Writes to the value property of MutableStateFlow,
                // adding a new element to the flow and updating all
                // of its collectors
                .collect { beneficiarySignUpResponse ->
                    _kitchenStaffSignUpState.value = beneficiarySignUpResponse
                    Log.d(TAG, "signUpBeneficiary:beneficiarySignUpResponse $beneficiarySignUpResponse")
                    Log.d(TAG, "signUpBeneficiary:beneficiarySignUpResponse.message ${beneficiarySignUpResponse.message}")
                    Log.d(TAG, "signUpBeneficiary:beneficiarySignUpResponse.data ${beneficiarySignUpResponse.data}")
                }
        }
    }


}

sealed class KitchenStaffSignUpIntent {
    data class KitchenStaffSignUp(val kitchenStaff: KitchenStaff) : KitchenStaffSignUpIntent()
}
