package com.example.lunchwallet.core.di.app

import android.content.Context
import com.example.lunchwallet.common.authentication.AuthenticationInterceptor
import com.example.lunchwallet.common.authentication.UserDatastore
import com.example.lunchwallet.common.authentication.UserStore
import com.example.lunchwallet.network.LunchWalletApi
import com.example.lunchwallet.util.AUTHORIZATION
import com.example.lunchwallet.util.BASE_URL
import com.example.lunchwallet.util.errorhandler.ErrorHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

// Retrofit module
@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

//    @Singleton
//    @Provides
//    fun provideRetrofitInstance(): Retrofit {
//        val logging = HttpLoggingInterceptor()
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
//        val client = OkHttpClient.Builder()
//            .addInterceptor { chain ->
//                chain.proceed(chain.request()
//                    .newBuilder().also {
//                    it.addHeader(AUTHORIZATION, "Bearer ${UserDatastore.ACCESS_TOKEN}") }
//                    .build())
//            }
//            .addInterceptor(logging)
//            .build()
//        return Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(client)
//            .build()
//    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    }


    companion object{
        private const val timeout: Long = 30
    }

    @Provides
    fun provideOkHttpClient(
        userStore: UserStore,
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        val authenticationInterceptor = AuthenticationInterceptor(userStore)
        return OkHttpClient.Builder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(authenticationInterceptor)
            .build()
    }

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    /**
     * provide datastore
     */

    @Provides
    fun provideUserStorePreferences(@ApplicationContext context: Context): UserStore {
       return UserDatastore(context)
    }

    /**
     * provide error handler
     */
    @Provides
    fun provideErrorHandle(@ApplicationContext context: Context): ErrorHandler {
        return ErrorHandler(context)
    }


    @Singleton
    @Provides
    fun provideLunchWalletApi(retrofit: Retrofit): LunchWalletApi {
        return retrofit.create(LunchWalletApi::class.java)
    }

}
