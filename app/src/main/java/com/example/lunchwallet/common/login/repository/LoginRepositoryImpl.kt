package com.example.lunchwallet.common.login.repository

import com.example.lunchwallet.common.login.model.User
import com.example.lunchwallet.common.login.model.LoginResponse
import com.example.lunchwallet.common.login.remote.LoginApi
import com.example.lunchwallet.util.Resource
import com.example.lunchwallet.util.connectivity.ConnectivityObserver
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val api: LoginApi): LoginRepository{
    override suspend fun loginBeneficiary(user: User): LoginResponse {
        return api.loginBeneficiary(user)
    }

    override suspend fun loginKitchenStaff(user: User): LoginResponse {
          return  api.loginKitchenStaff(user)
    }

    override suspend fun loginAdmin(user: User): LoginResponse {
        return api.loginAdmin(user)
    }


}