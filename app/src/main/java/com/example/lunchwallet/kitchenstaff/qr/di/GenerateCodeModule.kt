package com.example.lunchwallet.kitchenstaff.qr.di

import com.example.lunchwallet.common.authentication.UserDatastore
import com.example.lunchwallet.kitchenstaff.qr.remote.GenerateCodeApi
import com.example.lunchwallet.kitchenstaff.qr.repository.GenerateCodeRepository
import com.example.lunchwallet.kitchenstaff.qr.repository.GenerateCodeRepositoryImpl
import com.example.lunchwallet.util.errorhandler.ErrorHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GenerateCodeModule {

    @Singleton
    @Provides
    fun provideGenerateQRApi(retrofit: Retrofit): GenerateCodeApi {
        return retrofit.create(GenerateCodeApi::class.java)
    }


    @Singleton
    @Provides
    fun provideGenerateCodeRepository(api: GenerateCodeApi, errorHandler: ErrorHandler, datastore: UserDatastore): GenerateCodeRepository {
        return GenerateCodeRepositoryImpl(api, errorHandler, datastore)
    }
}