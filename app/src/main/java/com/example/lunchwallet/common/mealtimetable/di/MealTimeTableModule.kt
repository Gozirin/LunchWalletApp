package com.example.lunchwallet.common.mealtimetable.di

import com.example.lunchwallet.common.login.remote.LoginApi
import com.example.lunchwallet.common.mealtimetable.remote.MealTimeTableApi
import com.example.lunchwallet.common.mealtimetable.repository.MealTimeTableRepository
import com.example.lunchwallet.common.mealtimetable.repository.MealTimeTableRepositoryImpl
import com.example.lunchwallet.common.mealtimetable.use_cases.GetBrunchTimeTableUseCase
import com.example.lunchwallet.common.mealtimetable.use_cases.GetDinnerTimeTableUseCase
import com.example.lunchwallet.common.mealtimetable.use_cases.MealTimeTableUseCase
import com.example.lunchwallet.common.mealtimetable.use_cases.MealTimeTableUseCases
import com.example.lunchwallet.util.errorhandler.ErrorHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MealTimeTableModule {

    @Singleton
    @Provides
    fun provideMealTimeTableApi(retrofit: Retrofit): MealTimeTableApi {
        return retrofit.create(MealTimeTableApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMealTimeTableRepository(api: MealTimeTableApi): MealTimeTableRepository {
        return  MealTimeTableRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideBrunchTimeTableUseCase(repo: MealTimeTableRepository, errorHandler: ErrorHandler): MealTimeTableUseCase {
        return GetBrunchTimeTableUseCase(repo, errorHandler)
    }

    @Singleton
    @Provides
    fun provideDinnerTimeTableUseCase(repo: MealTimeTableRepository, errorHandler: ErrorHandler): MealTimeTableUseCase {
        return GetDinnerTimeTableUseCase(repo, errorHandler)
    }

    @Singleton
    @Provides
    fun provideMealTimeTableUseCases(
        brunchTimeTableUseCase: GetBrunchTimeTableUseCase,
        dinnerTimeTableUseCase: GetDinnerTimeTableUseCase): MealTimeTableUseCases {
        return MealTimeTableUseCases(brunchTimeTableUseCase, dinnerTimeTableUseCase)
    }
}