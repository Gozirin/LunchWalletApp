package com.example.lunchwallet.kitchenstaff.signup.di

import com.example.lunchwallet.kitchenstaff.signup.remote.KitchenStaffSignUpApi
import com.example.lunchwallet.kitchenstaff.signup.repository.KitchenStaffRepository
import com.example.lunchwallet.kitchenstaff.signup.repository.KitchenStaffRepositoryImpl
import com.example.lunchwallet.kitchenstaff.signup.use_cases.KitchenStaffSignUpUseCase
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
object KitchenStaffSignUpModule {

    @Singleton
    @Provides
    fun provideKitchenStaffSignUpApi(retrofit: Retrofit): KitchenStaffSignUpApi {
        return retrofit.create(KitchenStaffSignUpApi::class.java)
    }

    @Singleton
    @Provides
    fun provideKitchenStaffRepository(api: KitchenStaffSignUpApi): KitchenStaffRepository {
        return KitchenStaffRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideKitchenStaffSignUpUseCase(
        repo: KitchenStaffRepository,
        errorHandler: ErrorHandler
    ): KitchenStaffSignUpUseCase {
        return KitchenStaffSignUpUseCase(repo, errorHandler)
    }
}