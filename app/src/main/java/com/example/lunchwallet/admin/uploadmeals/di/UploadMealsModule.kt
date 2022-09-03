package com.example.lunchwallet.admin.uploadmeals.di

import android.content.Context
import com.example.lunchwallet.admin.uploadmeals.remote.UploadMealsApi
import com.example.lunchwallet.admin.uploadmeals.repository.UploadMealsRepository
import com.example.lunchwallet.admin.uploadmeals.repository.UploadMealsRepositoryImpl
import com.example.lunchwallet.admin.uploadmeals.use_cases.UploadMealUseCases
import com.example.lunchwallet.admin.uploadmeals.use_cases.create_meal.CreateMeal
import com.example.lunchwallet.admin.uploadmeals.use_cases.create_meal.CreateMealUseCase
import com.example.lunchwallet.admin.uploadmeals.use_cases.delete_meal.DeleteMeal
import com.example.lunchwallet.admin.uploadmeals.use_cases.delete_meal.DeleteMealUseCase
import com.example.lunchwallet.admin.uploadmeals.use_cases.get_meals.GetAllMeals
import com.example.lunchwallet.admin.uploadmeals.use_cases.get_meals.GetAllMealsUseCase
import com.example.lunchwallet.admin.uploadmeals.use_cases.update_meal.UpdateMeal
import com.example.lunchwallet.admin.uploadmeals.use_cases.update_meal.UpdateMealUseCase
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
class UploadMealsModule {

    @Singleton
    @Provides
    fun provideUploadMealsApi(retrofit: Retrofit): UploadMealsApi =
        retrofit.create(UploadMealsApi::class.java)

    @Singleton
    @Provides
    fun provideUploadMealsRepository(api: UploadMealsApi): UploadMealsRepository {
        return  UploadMealsRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideCreateMealUseCase(repo: UploadMealsRepository,@ApplicationContext context: Context, errorHandler: ErrorHandler): CreateMeal {
        return CreateMealUseCase(repo,context, errorHandler)
    }

    @Singleton
    @Provides
    fun provideUpdateMealUseCase(repo: UploadMealsRepository, errorHandler: ErrorHandler): UpdateMeal {
        return UpdateMealUseCase(repo,errorHandler)
    }

    @Singleton
    @Provides
    fun provideDeleteMealUseCase(repo: UploadMealsRepository, errorHandler: ErrorHandler): DeleteMeal {
        return DeleteMealUseCase(repo,errorHandler)
    }

    @Singleton
    @Provides
    fun provideGetAllMealsUseCase(repo: UploadMealsRepository, errorHandler: ErrorHandler): GetAllMeals {
        return GetAllMealsUseCase(repo,errorHandler)
    }

    @Singleton
    @Provides
    fun provideUploadMealUseCases(
        createMealUseCase: CreateMealUseCase,
        updateMealUseCase: UpdateMealUseCase,
        deleteMealUseCase: DeleteMealUseCase,
        getAllMealsUseCase: GetAllMealsUseCase
    ): UploadMealUseCases =
        UploadMealUseCases(createMealUseCase, updateMealUseCase, deleteMealUseCase, getAllMealsUseCase)
}