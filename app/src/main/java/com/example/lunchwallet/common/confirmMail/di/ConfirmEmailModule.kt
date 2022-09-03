package com.example.lunchwallet.common.confirmMail.di

import com.example.lunchwallet.common.confirmMail.remote.ConfirmEmailApi
import com.example.lunchwallet.common.confirmMail.repository.ConfirmEmailRepository
import com.example.lunchwallet.common.confirmMail.repository.ConfirmEmailRepositoryImpl
import com.example.lunchwallet.common.confirmMail.use_cases.ConfirmBeneficiaryEmailUseCase
import com.example.lunchwallet.common.confirmMail.use_cases.ConfirmEmailUseCase
import com.example.lunchwallet.common.confirmMail.use_cases.ConfirmEmailUseCases
import com.example.lunchwallet.common.confirmMail.use_cases.ConfirmKitchenStaffEmailUseCase
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
class ConfirmEmailModule {
    @Singleton
    @Provides
    fun provideConfirmEmailApi(retrofit: Retrofit): ConfirmEmailApi {
        return retrofit.create(ConfirmEmailApi::class.java)
    }

    @Singleton
    @Provides
    fun provideConfirmEmailRepository(api: ConfirmEmailApi): ConfirmEmailRepository {
        return ConfirmEmailRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideConfirmBeneficiaryEmailUseCase(
        repo: ConfirmEmailRepository,
        errorHandler: ErrorHandler
    ): ConfirmEmailUseCase {
        return ConfirmBeneficiaryEmailUseCase(repo, errorHandler)
    }

    @Singleton
    @Provides
    fun provideConfirmKitchenStaffEmailUseCase(
        repo: ConfirmEmailRepository,
        errorHandler: ErrorHandler
    ): ConfirmEmailUseCase {
        return ConfirmKitchenStaffEmailUseCase(repo, errorHandler)
    }

    @Singleton
    @Provides
    fun provideConfirmEmailUseCases(
        confirmBeneficiaryEmailUseCase: ConfirmBeneficiaryEmailUseCase,
        confirmKitchenStaffEmailUseCase: ConfirmKitchenStaffEmailUseCase
    ):
            ConfirmEmailUseCases {
        return ConfirmEmailUseCases(confirmBeneficiaryEmailUseCase, confirmKitchenStaffEmailUseCase)
    }
}