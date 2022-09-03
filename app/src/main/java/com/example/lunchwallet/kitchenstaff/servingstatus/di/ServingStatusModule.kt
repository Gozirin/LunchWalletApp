package com.example.lunchwallet.kitchenstaff.servingstatus.di

import com.example.lunchwallet.kitchenstaff.servingstatus.repositories.BrunchStatusRepository
import com.example.lunchwallet.kitchenstaff.servingstatus.repositories.DinnerStatusRepository
import com.example.lunchwallet.kitchenstaff.servingstatus.repositories.IBrunchStatusRepositoryInterface
import com.example.lunchwallet.kitchenstaff.servingstatus.repositories.IDinnerStatusRepositoryInterface
import com.example.lunchwallet.network.ChangeFoodStatusApi
import com.example.lunchwallet.util.errorhandler.ErrorHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServingStatusModule {

    @Singleton
    @Provides
    fun provideServingStatus(retrofit: Retrofit): ChangeFoodStatusApi {
        return retrofit.create(ChangeFoodStatusApi::class.java)
    }

    @Singleton
    @Provides
    fun provideBrunchStatusRepository(api: ChangeFoodStatusApi, errorHandler: ErrorHandler): IBrunchStatusRepositoryInterface {
        return BrunchStatusRepository(api, errorHandler)
    }

    @Singleton
    @Provides
    fun provideDinnerStatusRepository(api: ChangeFoodStatusApi, errorHandler: ErrorHandler): IDinnerStatusRepositoryInterface{
        return DinnerStatusRepository(api, errorHandler)
    }
}

