package com.example.lunchwallet.common.login.presentation

import androidx.lifecycle.*
import com.example.lunchwallet.common.authentication.UserAuth
import com.example.lunchwallet.common.authentication.UserDatastore
import com.example.lunchwallet.common.login.model.LoginResponse
import com.example.lunchwallet.common.login.model.User
import com.example.lunchwallet.common.login.use_cases.LoginUseCases
import com.example.lunchwallet.util.Resource
import com.example.lunchwallet.util.connectivity.Connectivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val useCase: LoginUseCases,
    private val userDatastore: UserDatastore,
    private val connectivityObserver: Connectivity ): ViewModel() {


    val intent = Channel<LoginIntent>(Channel.UNLIMITED)

        private var _loginState= MutableStateFlow<Resource<LoginResponse>>(Resource.Loading())
    val loginState: StateFlow<Resource<LoginResponse>> get() = _loginState

    init {
        handleIntent()
    }


     private fun handleIntent() {
         connectivityObserver.observeNetworkAccess().onEach {
                 intent.consumeAsFlow().collect { intent ->
                     when (intent) {
                         is LoginIntent.LoginBeneficiaryIntent -> loginBeneficiary()
                         is LoginIntent.LoginKitchenStaffIntent -> loginKitchenStaff()
                         is LoginIntent.LoginAdminIntent -> loginAdmin()
                     }
                 }
             }.launchIn(viewModelScope)
     }


     fun loginAdmin(user: User? = null) {
        viewModelScope.launch {
            useCase.loginAdminUseCase.invoke(user!!)
                .collect{ loginResponse ->
                    _loginState.value = loginResponse
                }
        }
    }

     fun loginKitchenStaff(user: User? = null) {
        viewModelScope.launch {
            useCase.loginKitchenStaffUseCase.invoke(user!!)
                .collect{ loginResponse ->
                    _loginState.value = loginResponse
                }
        }
    }

     fun loginBeneficiary(user: User? = null){
        viewModelScope.launch {
            useCase.loginBeneficiaryUseCase.invoke(user!!)
                .collect{ loginResponse ->
                    _loginState.value = loginResponse
                }
        }
    }

    //using a viewModel scope, function will not complete before navigation to destination takes effect.
    // therefore, altered to a suspend function
    suspend fun saveToDataStore(userAuth: UserAuth){
            userDatastore.saveToDataStore(userAuth)
        }
}


