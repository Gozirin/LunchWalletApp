package com.example.lunchwallet.kitchenstaff.servingstatus.repositories

import com.example.lunchwallet.kitchenstaff.servingstatus.model.BrunchStatusRequest
import com.example.lunchwallet.kitchenstaff.servingstatus.model.BrunchStatusResponse
import com.example.lunchwallet.util.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface IBrunchStatusRepositoryInterface {
    suspend fun brunchStatus(
        brunchStatusRequest: BrunchStatusRequest
    ): Flow<Resource<BrunchStatusResponse>>
}
