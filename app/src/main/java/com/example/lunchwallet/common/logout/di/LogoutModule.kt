package com.example.lunchwallet.common.logout.di

import com.example.lunchwallet.common.logout.remote.LogoutApi
import com.example.lunchwallet.common.logout.repository.LogoutRepository
import com.example.lunchwallet.common.logout.repository.LogoutRepositoryImpl
import com.example.lunchwallet.common.logout.use_cases.*
import com.example.lunchwallet.util.connectivity.ConnectivityObserver
import com.example.lunchwallet.util.errorhandler.ErrorHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LogoutModule {

    @Singleton
    @Provides
    fun provideLogoutApi(retrofit: Retrofit): LogoutApi {
        return retrofit.create(LogoutApi::class.java)
    }


    @Singleton
    @Provides
    fun provideLogoutRepository(api: LogoutApi): LogoutRepository {
        return LogoutRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideLogoutBeneficiaryUseCase(repo: LogoutRepository, errorHandler: ErrorHandler):
            LogoutUseCase{
        return LogoutBeneficiaryUseCase(repo, errorHandler)
    }

    @Singleton
    @Provides
    fun provideLogoutKitchenStaffUseCase(repo: LogoutRepository, errorHandler: ErrorHandler): LogoutUseCase {
        return LogoutKitchenStaffUseCase(repo, errorHandler)
    }

    @Singleton
    @Provides
    fun provideLogoutAdminUseCase(repo: LogoutRepository, errorHandler: ErrorHandler): LogoutUseCase {
        return LogoutAdminUseCase(repo, errorHandler)
    }

    @Singleton
    @Provides
    fun provideLogoutUseCases(
        logoutBeneficiaryUseCase: LogoutBeneficiaryUseCase,
        logoutKitchenStaffUseCase: LogoutKitchenStaffUseCase,
        logoutAdminUseCase: LogoutAdminUseCase
    ):
            LogoutUseCases {
        return LogoutUseCases(logoutBeneficiaryUseCase, logoutKitchenStaffUseCase, logoutAdminUseCase)
    }

}