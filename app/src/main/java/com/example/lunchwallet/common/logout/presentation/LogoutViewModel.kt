package com.example.lunchwallet.common.logout.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lunchwallet.common.login.model.LoginResponse
import com.example.lunchwallet.common.login.model.User
import com.example.lunchwallet.common.login.presentation.LoginIntent
import com.example.lunchwallet.common.login.use_cases.LoginUseCases
import com.example.lunchwallet.common.logout.use_cases.LogoutUseCases
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
class LogoutViewModel @Inject constructor(
    private val useCase: LogoutUseCases
): ViewModel() {

    val intent = Channel<LogoutIntent>(Channel.UNLIMITED)

    private var _logoutState= MutableStateFlow<Resource<ApiResponse>>(Resource.Loading())
    val logoutState: StateFlow<Resource<ApiResponse>> get() = _logoutState

    init {
        handleIntent()
    }


    private fun handleIntent(){
        viewModelScope.launch {
            intent.consumeAsFlow().collect {intent ->
                when (intent) {
                    is LogoutIntent.LogoutBeneficiaryIntent -> logoutBeneficiary()
                    is LogoutIntent.LogoutKitchenStaffIntent -> logoutKitchenStaff()
                    is LogoutIntent.LogoutAdminIntent -> logoutAdmin()
                }
            }
        }
    }

    fun logoutAdmin(email: String? = null) {
        viewModelScope.launch {
            useCase.logoutAdminUseCase.invoke(email!!)
                .collect{ loginResponse ->
                    _logoutState.value = loginResponse
                }
        }
    }

    fun logoutKitchenStaff(email:String? = null) {
        viewModelScope.launch {
            useCase.logoutKitchenStaffUseCase.invoke(email!!)
                .collect{ loginResponse ->
                    _logoutState.value = loginResponse
                }
        }
    }

    fun logoutBeneficiary(email:String? = null){
        viewModelScope.launch {
            useCase.logoutBeneficiaryUseCase.invoke(email!!)
                .collect{ loginResponse ->
                    _logoutState.value = loginResponse
                }
        }
    }
}