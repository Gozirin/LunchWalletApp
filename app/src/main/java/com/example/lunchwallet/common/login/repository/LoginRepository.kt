package com.example.lunchwallet.common.login.repository

import com.example.lunchwallet.common.login.model.User
import com.example.lunchwallet.common.login.model.LoginResponse
import com.example.lunchwallet.util.Resource

interface LoginRepository {

    suspend fun loginBeneficiary(user: User): LoginResponse

    suspend fun loginKitchenStaff(user: User): LoginResponse

    suspend fun loginAdmin(user: User): LoginResponse
}