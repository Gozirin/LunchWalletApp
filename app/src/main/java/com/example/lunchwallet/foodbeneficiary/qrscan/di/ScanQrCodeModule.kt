package com.example.lunchwallet.foodbeneficiary.qrscan.di

import com.example.lunchwallet.foodbeneficiary.qrscan.remote.ScanApi
import com.example.lunchwallet.foodbeneficiary.qrscan.repository.ScanRepository
import com.example.lunchwallet.foodbeneficiary.qrscan.repository.ScanRepositoryImpl
import com.example.lunchwallet.util.errorhandler.ErrorHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ScanQrCodeModule {

    @Singleton
    @Provides
    fun provideScanApi(retrofit: Retrofit): ScanApi {
        return retrofit.create(ScanApi::class.java)
    }


    @Singleton
    @Provides
    fun provideScanRepository(api: ScanApi, errorHandler: ErrorHandler): ScanRepository {
        return ScanRepositoryImpl(api, errorHandler)
    }
}