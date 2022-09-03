package com.example.lunchwallet.network

import com.example.lunchwallet.kitchenstaff.servingstatus.model.*
import com.example.lunchwallet.util.Resource
import retrofit2.Response
import retrofit2.http.*

interface ChangeFoodStatusApi {

    @PUT("/api/v1/staff/changebrunchstatus")
    suspend fun brunchStatus(
        @Body brunchStatusRequest: BrunchStatusRequest
    ): BrunchStatusResponse

    @PUT("/api/v1/staff/changedinnerstatus")
    suspend fun dinnerStatus(
        @Body dinnerStatusRequest: DinnerStatusRequest
    ): DinnerStatusResponse
}







