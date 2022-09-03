package com.example.lunchwallet.common.confirmMail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lunchwallet.common.confirmMail.model.ConfirmEmailResponse
import com.example.lunchwallet.common.confirmMail.use_cases.ConfirmEmailUseCases
import com.example.lunchwallet.common.model.ApiResponse
import com.example.lunchwallet.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmEmailViewModel @Inject constructor(private val useCase: ConfirmEmailUseCases): ViewModel() {

    private val intent = Channel<ConfirmEmailIntent>(Channel.UNLIMITED)

    private var _confirmEmailState=
        MutableStateFlow<Resource<ApiResponse>>(Resource.Loading())
    val confirmEmailState: StateFlow<Resource<ApiResponse>> get() = _confirmEmailState

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect{ intent ->
                when(intent) {
                    ConfirmEmailIntent.ConfirmBeneficiaryEmailIntent -> verifyBeneficiaryEmail()
                    ConfirmEmailIntent.ConfirmKitchenStaffEmailIntent -> verifyKitchenStaffEmail()
                }
            }
        }
    }
    fun verifyBeneficiaryEmail(token: String?= null) {
        viewModelScope.launch {
            useCase.confirmBeneficiaryEmailUseCase.invoke(token!!)
                .collect { confirmEmailResponse ->
                    _confirmEmailState.value = confirmEmailResponse
                }
        }
    }


    fun verifyKitchenStaffEmail(token: String?= null) {
        viewModelScope.launch {
            useCase.confirmKitchenStaffEmailUseCase.invoke(token!!)
                .collect{
                    _confirmEmailState.value = it
                }
        }
    }
}


private sealed class ConfirmEmailIntent{
    object ConfirmBeneficiaryEmailIntent: ConfirmEmailIntent()
    object ConfirmKitchenStaffEmailIntent: ConfirmEmailIntent()

}