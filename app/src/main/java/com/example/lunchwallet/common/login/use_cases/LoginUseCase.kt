package com.example.lunchwallet.common.login.use_cases

import com.example.lunchwallet.common.login.model.LoginResponse
import com.example.lunchwallet.common.login.model.User
import com.example.lunchwallet.util.Resource
import kotlinx.coroutines.flow.Flow

interface LoginUseCase {

    operator fun invoke(user: User): Flow<Resource<LoginResponse>>
}