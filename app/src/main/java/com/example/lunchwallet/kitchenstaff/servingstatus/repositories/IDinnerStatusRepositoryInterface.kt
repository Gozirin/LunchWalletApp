package com.example.lunchwallet.kitchenstaff.servingstatus.repositories

import com.example.lunchwallet.kitchenstaff.servingstatus.model.DinnerStatusRequest
import com.example.lunchwallet.kitchenstaff.servingstatus.model.DinnerStatusResponse
import com.example.lunchwallet.util.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface IDinnerStatusRepositoryInterface {
    suspend fun dinnerStatus(
        dinnerStatusRequest: DinnerStatusRequest
    ): Flow<Resource<DinnerStatusResponse>>

}

