package com.example.lunchwallet.foodbeneficiary.signup.di

import android.content.Context
import com.example.lunchwallet.foodbeneficiary.signup.use_cases.BeneficiarySignUpUseCase
import com.example.lunchwallet.foodbeneficiary.signup.remote.BeneficiarySignUpApi
import com.example.lunchwallet.foodbeneficiary.signup.repository.BeneficiarySignUpRepository
import com.example.lunchwallet.foodbeneficiary.signup.repository.BeneficiarySignUpRepositoryImpl
import com.example.lunchwallet.util.connectivity.ConnectivityObserver
import com.example.lunchwallet.util.errorhandler.ErrorHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object BeneficiarySignUpModule {

    @Singleton
    @Provides
    fun provideBeneficiarySignUpApi(retrofit: Retrofit): BeneficiarySignUpApi {
        return retrofit.create(BeneficiarySignUpApi::class.java)
    }

    @Singleton
    @Provides
    fun provideBeneficiaryRepository(api: BeneficiarySignUpApi): BeneficiarySignUpRepository {
        return BeneficiarySignUpRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideBeneficiarySignUpUseCase(repo: BeneficiarySignUpRepository, errorHandler: ErrorHandler)
    : BeneficiarySignUpUseCase {
        return BeneficiarySignUpUseCase(repo, errorHandler)
    }


}