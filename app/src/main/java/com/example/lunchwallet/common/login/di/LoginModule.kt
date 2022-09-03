package com.example.lunchwallet.common.login.di

import android.content.Context
import com.example.lunchwallet.common.login.remote.LoginApi
import com.example.lunchwallet.common.login.repository.LoginRepository
import com.example.lunchwallet.common.login.repository.LoginRepositoryImpl
import com.example.lunchwallet.common.login.use_cases.*
import com.example.lunchwallet.util.connectivity.ConnectivityObserver
import com.example.lunchwallet.util.errorhandler.LoginErrorHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LoginModule {

    @Singleton
    @Provides
    fun provideLoginApi(retrofit: Retrofit): LoginApi {
        return retrofit.create(LoginApi::class.java)
    }


    @Singleton
    @Provides
    fun provideLoginRepository(api: LoginApi): LoginRepository {
        return LoginRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideLoginBeneficiaryUseCase(repo: LoginRepository,
                                       loginErrorHandler: LoginErrorHandler): LoginUseCase{
        return LoginBeneficiaryUseCase(repo, loginErrorHandler)
    }

    @Singleton
    @Provides
    fun provideLoginKitchenStaffUseCase(repo: LoginRepository,
                                        loginErrorHandler: LoginErrorHandler): LoginUseCase{
        return LoginKitchenStaffUseCase(repo, loginErrorHandler)
    }

    @Singleton
    @Provides
    fun provideLoginAdminUseCase(repo: LoginRepository, loginErrorHandler: LoginErrorHandler): LoginUseCase{
        return LoginAdminUseCase(repo, loginErrorHandler)
    }

    @Singleton
    @Provides
    fun provideLoginUseCases(
        loginBeneficiaryUseCase: LoginBeneficiaryUseCase,
        loginKitchenStaffUseCase: LoginKitchenStaffUseCase,
        loginAdminUseCase: LoginAdminUseCase):
            LoginUseCases{
        return LoginUseCases(loginBeneficiaryUseCase, loginKitchenStaffUseCase, loginAdminUseCase)
    }

    /**
     * provide error handler
     */
    @Provides
    @Singleton
    fun provideLoginErrorHandle(@ApplicationContext context: Context): LoginErrorHandler {
        return LoginErrorHandler(context)
    }

}